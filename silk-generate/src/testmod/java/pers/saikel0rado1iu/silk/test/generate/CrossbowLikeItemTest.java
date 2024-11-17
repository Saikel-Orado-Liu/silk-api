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

package pers.saikel0rado1iu.silk.test.generate;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import pers.saikel0rado1iu.silk.api.generate.advancement.criterion.Criteria;
import pers.saikel0rado1iu.silk.api.generate.advancement.criterion.RangedKilledEntityCriterion;
import pers.saikel0rado1iu.silk.api.ropestick.component.ComponentTypes;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.AdjustFovData;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.AdjustFovWhileHoldComponent;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.ModifyMoveWhileHoldComponent;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.RangedWeaponComponent;
import pers.saikel0rado1iu.silk.api.ropestick.ranged.CrossbowLikeItem;

import java.util.Optional;

/**
 * Test {@link CrossbowLikeItem}
 */
public final class CrossbowLikeItemTest extends CrossbowLikeItem {
	/**
	 * @param settings 物品设置
	 */
	public CrossbowLikeItemTest(Settings settings) {
		super(settings
				.component(ComponentTypes.ADJUST_FOV_WHILE_HOLD, AdjustFovWhileHoldComponent.create(true, Optional.of(AdjustFovData.POWDER_SNOW_OUTLINE), false, AdjustFovData.DEFAULT_FOV_SCALING))
				.component(ComponentTypes.MODIFY_MOVE_WHILE_HOLD, ModifyMoveWhileHoldComponent.of(10)));
	}
	
	@Override
	public RangedWeaponComponent rangedWeapon(Optional<ItemStack> stack) {
		return RangedWeaponComponent.builder()
				.maxSpeed(RangedWeaponComponent.CROSSBOW_MAX_PROJECTILE_SPEED)
				.maxDamage(50)
				.maxUseTicks(RangedWeaponComponent.CROSSBOW_MAX_USE_TICKS)
				.maxPullTicks(RangedWeaponComponent.CROSSBOW_MAX_USE_TICKS)
				.firingError(RangedWeaponComponent.DEFAULT_FIRING_ERROR)
				.defaultProjectile(Items.ARROW.getDefaultStack())
				.launchableProjectiles(ImmutableList.of(
						Items.ARROW,
						Items.SPECTRAL_ARROW,
						Items.TIPPED_ARROW,
						Items.FIREWORK_ROCKET))
				.build();
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
		RangedKilledEntityCriterion.setRangedWeapon(projectile, ranged);
		Criteria.SHOT_PROJECTILE_CRITERION.trigger(serverPlayer, ranged, projectile);
	}
}
