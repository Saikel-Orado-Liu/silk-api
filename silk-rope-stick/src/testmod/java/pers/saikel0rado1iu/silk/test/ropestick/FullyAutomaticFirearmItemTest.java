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

package pers.saikel0rado1iu.silk.test.ropestick;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import pers.saikel0rado1iu.silk.api.base.common.util.TickUtil;
import pers.saikel0rado1iu.silk.api.ropestick.component.DataComponentTypes;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.*;
import pers.saikel0rado1iu.silk.api.ropestick.ranged.FullyAutomaticFirearmItem;

import java.util.Optional;

/**
 * Test {@link FullyAutomaticFirearmItem}
 */
public final class FullyAutomaticFirearmItemTest extends FullyAutomaticFirearmItem {
	/**
	 * @param settings 物品设置
	 */
	public FullyAutomaticFirearmItemTest(Settings settings) {
		super(settings
				.component(DataComponentTypes.ADJUST_FOV_WHILE_HOLD, AdjustFovWhileHoldComponent.create(false, Optional.of(AdjustFovData.POWDER_SNOW_OUTLINE), true, 0.8F))
				.component(DataComponentTypes.MODIFY_MOVE_WHILE_HOLD, ModifyMoveWhileHoldComponent.of(10)));
	}
	
	@Override
	public RangedWeaponComponent rangedWeapon(Optional<ItemStack> stack) {
		return RangedWeaponComponent.builder()
				.maxSpeed(RangedWeaponComponent.CROSSBOW_MAX_PROJECTILE_SPEED)
				.maxDamage(1)
				.maxUseTicks(TickUtil.getTick(5))
				.maxPullTicks(TickUtil.getTick(5))
				.firingError(RangedWeaponComponent.DEFAULT_FIRING_ERROR)
				.defaultProjectile(Items.ARROW.getDefaultStack())
				.launchableProjectiles(ImmutableList.of(
						Items.ARROW,
						Items.FIREWORK_ROCKET))
				.build();
	}
	
	@Override
	public ProjectileContainerComponent projectileContainer(Optional<ItemStack> stack) {
		return ProjectileContainerComponent.of(100);
	}
	
	@Override
	public ShootProjectilesComponent shootProjectiles(Optional<ItemStack> stack) {
		return ShootProjectilesComponent.create(1, ShootProjectilesComponent.State.EVERY);
	}
	
	/**
	 * 触发进度条件
	 *
	 * @param serverPlayer 服务端玩家
	 * @param ranged       远程武器物品堆栈
	 * @param projectile   发射物
	 */
	@Override
	public void triggerCriteria(ServerPlayerEntity serverPlayer, ItemStack ranged, ProjectileEntity projectile) {
	}
}
