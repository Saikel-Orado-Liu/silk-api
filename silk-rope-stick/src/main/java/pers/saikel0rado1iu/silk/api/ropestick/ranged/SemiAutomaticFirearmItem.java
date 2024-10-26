/*
 * This file is part of Silk API.
 * Copyright (C) 2023 Saikel Orado Liu
 *
 * Silk API is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Silk API is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Silk API. If not, see <https://www.gnu.org/licenses/>.
 */

package pers.saikel0rado1iu.silk.api.ropestick.ranged;

import com.google.common.collect.Lists;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.api.ropestick.component.DataComponentUtil;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.ProjectileContainerComponent;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.RangedWeaponComponent;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.ShootProjectilesComponent;

import java.util.List;
import java.util.Optional;

import static pers.saikel0rado1iu.silk.api.ropestick.component.DataComponentTypes.*;

/**
 * <h2 style="color:FFC800">半自动枪械物品</h2>
 * 用于创建一个有装填容量的，一次性装填多个发射物，并且一颗一颗发射的远程武器
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public abstract class SemiAutomaticFirearmItem extends CrossbowLikeItem {
	/**
	 * @param settings 物品设置
	 */
	public SemiAutomaticFirearmItem(Settings settings) {
		super(settings
				.component(PROJECTILE_CONTAINER, ProjectileContainerComponent.DEFAULT)
				.component(SHOOT_PROJECTILES, ShootProjectilesComponent.DEFAULT));
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		ShootProjectilesComponent shootProjectiles = DataComponentUtil.setOrGetValue(stack, SHOOT_PROJECTILES, shootProjectiles());
		shootProjectiles.resetShot(stack);
		// 如果已装填
		if (isCharged(stack)) {
			RangedWeaponComponent rangedWeapon = DataComponentUtil.setOrGetValue(stack, RANGED_WEAPON, rangedWeapon());
			shootAll(world, user, hand, stack, rangedWeapon.getMaxProjectileSpeed(stack), rangedWeapon.firingError(), null);
			return TypedActionResult.pass(stack);
		}
		// 如果使用者有弹药
		if (!RangedWeaponComponent.getProjectileType(user, stack).isEmpty()) {
			charged = false;
			loaded = false;
			user.setCurrentHand(hand);
			return TypedActionResult.consume(stack);
		}
		// 如果未装填
		return TypedActionResult.fail(stack);
	}
	
	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (getUsingProgress(getMaxUseTime(stack) - remainingUseTicks, stack) != 1 || isCharged(stack) || !load(user, stack)) return;
		// 获取声音类别
		SoundCategory soundCategory = user instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
		// 播放弩装填结束音效
		world.playSound(null, user.getX(), user.getY(), user.getZ(), loadedSound(), soundCategory, 1, 1 / (world.getRandom().nextFloat() * 0.5F + 1) + 0.2F);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		super.appendTooltip(stack, context, tooltip, type);
		// 获取弹药
		List<ItemStack> projectiles = ProjectileContainerComponent.getChargedProjectiles(stack);
		// 如果已装填且弹药不为空
		if (isCharged(stack) && projectiles.size() > 1) {
			MutableText text = (MutableText) tooltip.getLast();
			tooltip.remove(text);
			tooltip.add(text.append(" x ").append(String.valueOf(ProjectileContainerComponent.getChargedAmount(stack))));
		}
	}
	
	@Override
	protected boolean load(LivingEntity shooter, ItemStack crossbow) {
		List<ItemStack> list = Lists.newCopyOnWriteArrayList(crossbow.getOrDefault(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT).getProjectiles());
		ItemStack projectile = RangedWeaponComponent.getProjectileType(shooter, crossbow);
		if (projectile.isEmpty()) {
			crossbow.getOrDefault(PROJECTILE_CONTAINER, projectileContainer()).putChargedProjectiles(crossbow, list);
			return false;
		}
		int size = DataComponentUtil.setOrGetValue(crossbow, PROJECTILE_CONTAINER, projectileContainer()).getLoadableAmount(crossbow, Optional.of(shooter));
		for (int count = 0; count < size; count++) list.add(getProjectile(crossbow, projectile, shooter, false));
		crossbow.getOrDefault(PROJECTILE_CONTAINER, projectileContainer()).putChargedProjectiles(crossbow, list);
		return true;
	}
	
	@Override
	public void shootAll(World world, LivingEntity shooter, Hand hand, ItemStack stack, float speed, float divergence, @Nullable LivingEntity livingEntity) {
		if (world.isClient()) return;
		Optional<ChargedProjectilesComponent> chargedProjectilesComponent = DataComponentUtil.getOrSetDefault(stack, DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
		if (chargedProjectilesComponent.isEmpty() || chargedProjectilesComponent.get().isEmpty()) return;
		shootAll(world, shooter, hand, stack, List.of(ProjectileContainerComponent.popChargedProjectiles(stack)), speed, divergence, shooter instanceof PlayerEntity, livingEntity);
		if (shooter instanceof ServerPlayerEntity serverPlayerEntity) {
			Criteria.SHOT_CROSSBOW.trigger(serverPlayerEntity, stack);
			serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
		}
	}
	
	@Override
	protected void shootAll(World world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target) {
		for (ItemStack projectile : projectiles) {
			super.shootAll(world, shooter, hand, stack, load(stack, projectile, shooter), speed, divergence, critical, target);
		}
	}
	
	@Override
	protected void postShot(World world, LivingEntity shooter, ItemStack stack) {
		ShootProjectilesComponent component = DataComponentUtil.setOrGetValue(stack, SHOOT_PROJECTILES, shootProjectiles());
		if (shooter instanceof PlayerEntity player && component.interval() != 0) player.getItemCooldownManager().set(this, component.interval());
		if (component.state() == ShootProjectilesComponent.State.EVERY || ProjectileContainerComponent.getChargedAmount(stack) == 0) component.setShot(stack);
	}
	
	/**
	 * 物品的发射物容器组件
	 *
	 * @return 发射物容器组件
	 */
	public ProjectileContainerComponent projectileContainer() {
		return ProjectileContainerComponent.DEFAULT;
	}
	
	/**
	 * 物品的射击发射物组件
	 *
	 * @return 射击发射物组件
	 */
	public ShootProjectilesComponent shootProjectiles() {
		return ShootProjectilesComponent.DEFAULT;
	}
}