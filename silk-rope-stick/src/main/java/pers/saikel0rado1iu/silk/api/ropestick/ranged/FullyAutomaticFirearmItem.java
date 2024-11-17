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
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.api.ropestick.component.ComponentUtil;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.ProjectileContainerComponent;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.RangedWeaponComponent;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.ShootProjectilesComponent;

import java.util.List;
import java.util.Optional;

import static pers.saikel0rado1iu.silk.api.ropestick.component.ComponentTypes.*;

/**
 * <h2 style="color:FFC800">全自动枪械</h2>
 * 用于创建一个有装填容量的，一次性装填多个发射物，并且能够连续发射的远程武器
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public abstract class FullyAutomaticFirearmItem extends CrossbowLikeItem {
	protected int maxUseTicks = 0;
	protected int loadableAmount = 0;
	
	/**
	 * @param settings 物品设置
	 */
	public FullyAutomaticFirearmItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public ComponentMap dynamicComponents(ItemStack stack) {
		return ComponentMap.builder()
				.addAll(super.dynamicComponents(stack))
				.add(PROJECTILE_CONTAINER, projectileContainer(Optional.of(stack)))
				.add(SHOOT_PROJECTILES, shootProjectiles(Optional.of(stack)).setShot(stack.getOrDefault(SHOOT_PROJECTILES, shootProjectiles(Optional.of(stack))).shot()))
				.build();
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack, LivingEntity user) {
		return maxUseTicks;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		ShootProjectilesComponent shootProjectiles = stack.getOrDefault(SHOOT_PROJECTILES, shootProjectiles(Optional.of(stack)));
		stack.set(SHOOT_PROJECTILES, shootProjectiles.resetShot());
		// 如果没有弹药同时未装填则不使用物品
		if (!isCharged(stack) && RangedWeaponComponent.getProjectileType(user, stack).isEmpty()) return TypedActionResult.fail(stack);
		loadableAmount = stack.getOrDefault(PROJECTILE_CONTAINER, projectileContainer(Optional.of(stack))).getLoadableAmount(stack, user);
		if (isCharged(stack)) {
			maxUseTicks = ProjectileContainerComponent.getChargedAmount(stack) * shootProjectiles.interval();
			user.setCurrentHand(hand);
			return TypedActionResult.consume(stack);
		}
		charged = false;
		loaded = false;
		maxUseTicks = RangedWeaponComponent.getQuickTicks(stack, user, stack.getOrDefault(RANGED_WEAPON, rangedWeapon(Optional.of(stack))).maxUseTicks());
		user.setCurrentHand(hand);
		return TypedActionResult.consume(stack);
	}
	
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (world.isClient) return;
		ShootProjectilesComponent shootProjectiles = stack.getOrDefault(SHOOT_PROJECTILES, shootProjectiles(Optional.of(stack)));
		if (ProjectileContainerComponent.getChargedAmount(stack) > 0) stack.set(SHOOT_PROJECTILES, shootProjectiles.resetShot());
		if (isCharged(stack)) {
			double useTicks = getMaxUseTime(stack, user) - remainingUseTicks;
			if (useTicks >= getMaxUseTime(stack, user) || useTicks % shootProjectiles.interval() != 0) return;
			RangedWeaponComponent rangedWeapon = stack.getOrDefault(RANGED_WEAPON, rangedWeapon(Optional.of(stack)));
			shootAll(world, user, user.getActiveHand(), stack, rangedWeapon.getMaxProjectileSpeed(stack), rangedWeapon.firingError(), null);
		} else {
			super.usageTick(world, user, stack, remainingUseTicks);
		}
	}
	
	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (getUsingProgress(stack, user, getMaxUseTime(stack, user) - remainingUseTicks) != 1 || isCharged(stack) || !load(user, stack)) return;
		// 播放弩装填结束音效
		stateSounds(stack).loadings().end().ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), sound, user.getSoundCategory(), 1, 1 / (world.getRandom().nextFloat() * 0.5F + 1) + 0.2F));
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
			crossbow.getOrDefault(PROJECTILE_CONTAINER, projectileContainer(Optional.of(crossbow))).putChargedProjectiles(crossbow, list, shooter);
			return false;
		}
		int size = crossbow.getOrDefault(PROJECTILE_CONTAINER, projectileContainer(Optional.of(crossbow))).getLoadableAmount(crossbow, shooter);
		for (int count = 0; count < size; count++) list.add(getProjectile(crossbow, projectile, shooter, false));
		crossbow.getOrDefault(PROJECTILE_CONTAINER, projectileContainer(Optional.of(crossbow))).putChargedProjectiles(crossbow, list, shooter);
		return true;
	}
	
	@Override
	public void shootAll(World world, LivingEntity shooter, Hand hand, ItemStack stack, float speed, float divergence, @Nullable LivingEntity livingEntity) {
		if (!(world instanceof ServerWorld serverWorld)) return;
		Optional<ChargedProjectilesComponent> chargedProjectilesComponent = ComponentUtil.getOrSetDefault(stack, DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
		if (chargedProjectilesComponent.isEmpty() || chargedProjectilesComponent.get().isEmpty()) return;
		shootAll(serverWorld, shooter, hand, stack, List.of(ProjectileContainerComponent.popChargedProjectiles(stack)), speed, divergence, shooter instanceof PlayerEntity, livingEntity);
		if (shooter instanceof ServerPlayerEntity serverPlayerEntity) {
			Criteria.SHOT_CROSSBOW.trigger(serverPlayerEntity, stack);
			serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
		}
	}
	
	@Override
	protected void shootAll(ServerWorld world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target) {
		for (ItemStack projectile : projectiles) {
			super.shootAll(world, shooter, hand, stack, load(stack, projectile, shooter), speed, divergence, critical, target);
		}
	}
	
	@Override
	protected void postShot(World world, LivingEntity shooter, ItemStack stack) {
		ShootProjectilesComponent component = stack.getOrDefault(SHOOT_PROJECTILES, shootProjectiles(Optional.of(stack)));
		if (component.state() == ShootProjectilesComponent.State.EVERY || ProjectileContainerComponent.getChargedAmount(stack) == 0) {
			stack.set(SHOOT_PROJECTILES, component.setShot());
		}
	}
	
	/**
	 * 物品的发射物容器组件
	 *
	 * @param stack 当前的物品堆栈
	 * @return 发射物容器组件
	 */
	public abstract ProjectileContainerComponent projectileContainer(Optional<ItemStack> stack);
	
	/**
	 * 物品的射击发射物组件
	 *
	 * @param stack 当前的物品堆栈
	 * @return 射击发射物组件
	 */
	public abstract ShootProjectilesComponent shootProjectiles(Optional<ItemStack> stack);
}