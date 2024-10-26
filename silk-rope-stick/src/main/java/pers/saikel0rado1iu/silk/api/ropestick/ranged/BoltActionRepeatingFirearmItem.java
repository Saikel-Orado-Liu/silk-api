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
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
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
 * <h2 style="color:FFC800">栓动式连发枪械物品</h2>
 * 用于创建一个有装填容量的，需要一颗一颗装填发射物，但是能够连续发射的远程武器
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public abstract class BoltActionRepeatingFirearmItem extends CrossbowLikeItem {
	protected int maxUseTicks = 0;
	protected int loadableAmount = 0;
	
	/**
	 * @param settings 物品设置
	 */
	public BoltActionRepeatingFirearmItem(Settings settings) {
		super(settings
				.component(PROJECTILE_CONTAINER, ProjectileContainerComponent.DEFAULT)
				.component(SHOOT_PROJECTILES, ShootProjectilesComponent.DEFAULT));
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return maxUseTicks;
	}
	
	@Override
	public float getUsingProgress(int useTicks, ItemStack stack) {
		if (useTicks >= getMaxUseTime(stack) && isCharged(stack)) return -1;
		if (loadableAmount != DataComponentUtil.setOrGetValue(stack, PROJECTILE_CONTAINER, projectileContainer()).maxCapacity() && !isCharged(stack)) {
			return -1;
		}
		return useTicks >= getMaxUseTime(stack) ? 1 : (Math.min(1, useTicks / (float) getMaxUseTime(stack)) * loadableAmount) % 1;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = tempStack = user.getStackInHand(hand);
		ShootProjectilesComponent shootProjectiles = DataComponentUtil.setOrGetValue(stack, SHOOT_PROJECTILES, shootProjectiles());
		shootProjectiles.resetShot(stack);
		// 如果没有弹药同时未装填则不使用物品
		RangedWeaponComponent rangedWeapon = DataComponentUtil.setOrGetValue(stack, RANGED_WEAPON, rangedWeapon());
		if (!isCharged(stack) && RangedWeaponComponent.getProjectileType(user, stack).isEmpty()) return TypedActionResult.fail(stack);
		loadableAmount = DataComponentUtil.setOrGetValue(stack, PROJECTILE_CONTAINER, projectileContainer()).getLoadableAmount(stack, Optional.of(user));
		if (isCharged(stack)) {
			maxUseTicks = ProjectileContainerComponent.getChargedAmount(stack) * shootProjectiles.interval();
			user.setCurrentHand(hand);
			return TypedActionResult.consume(stack);
		}
		charged = false;
		loaded = false;
		maxUseTicks = Math.round((float) rangedWeapon.maxUseTicks() * loadableAmount);
		user.setCurrentHand(hand);
		return TypedActionResult.consume(stack);
	}
	
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (world.isClient()) return;
		RangedWeaponComponent rangedWeapon = DataComponentUtil.setOrGetValue(stack, RANGED_WEAPON, rangedWeapon());
		ShootProjectilesComponent shootProjectiles = DataComponentUtil.setOrGetValue(stack, SHOOT_PROJECTILES, shootProjectiles());
		if (ProjectileContainerComponent.getChargedAmount(stack) > 0) shootProjectiles.resetShot(stack);
		if (!isCharged(stack) && maxUseTicks == Math.round((float) rangedWeapon.maxUseTicks() * loadableAmount)) {
			int level = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
			int useTicks = getMaxUseTime(stack) - remainingUseTicks;
			double pullProgress = getUsingProgress(useTicks, stack);
			if (useTicks != 0 && (pullProgress == 0 || pullProgress == 1)) load(user, stack);
			if (pullProgress < 0.2) {
				charged = false;
				loaded = false;
			} else if (pullProgress > 0.3 && !charged) {
				charged = true;
				world.playSound(null, user.getX(), user.getY(), user.getZ(), getQuickChargeSound(level), SoundCategory.PLAYERS, 1, 1);
			} else if (pullProgress > 0.9 && level == 0 && !loaded) {
				loaded = true;
				world.playSound(null, user.getX(), user.getY(), user.getZ(), loadingSound(), SoundCategory.PLAYERS, 1, 1);
			}
		} else {
			double useTicks = getMaxUseTime(stack) - remainingUseTicks;
			if (useTicks >= getMaxUseTime(stack) || useTicks % shootProjectiles.interval() != 0) return;
			shootAll(world, user, user.getActiveHand(), tempStack = stack, rangedWeapon.getMaxProjectileSpeed(stack), rangedWeapon.firingError(), null);
		}
	}
	
	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (ProjectileContainerComponent.getChargedAmount(stack) <= 0 || isCharged(stack)) return;
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
		list.add(getProjectile(crossbow, projectile, shooter, false));
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