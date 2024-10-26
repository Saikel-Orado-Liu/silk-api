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

package pers.saikel0rado1iu.silk.api.ropestick.component;

import net.minecraft.component.DataComponentType;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.*;
import pers.saikel0rado1iu.silk.api.spinningjenny.DataComponentTypeRegistry;
import pers.saikel0rado1iu.silk.impl.SilkApi;

/**
 * <h2 style="color:FFC800">数据组件类型</h2>
 * 储存了扩展的数据组件类型
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public interface DataComponentTypes extends DataComponentTypeRegistry {
	DataComponentType<AdjustFovComponent> ADJUST_FOV = DataComponentTypeRegistry.registrar(() ->
					DataComponentType.<AdjustFovComponent>builder().codec(AdjustFovComponent.CODEC).packetCodec(AdjustFovComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("adjust_fov"));
	DataComponentType<AdjustFovWhileHoldComponent> ADJUST_FOV_WHILE_HOLD = DataComponentTypeRegistry.registrar(() ->
					DataComponentType.<AdjustFovWhileHoldComponent>builder().codec(AdjustFovWhileHoldComponent.CODEC).packetCodec(AdjustFovWhileHoldComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("adjust_fov_while_hold"));
	DataComponentType<AdjustFovWhileUseComponent> ADJUST_FOV_WHILE_USE = DataComponentTypeRegistry.registrar(() ->
					DataComponentType.<AdjustFovWhileUseComponent>builder().codec(AdjustFovWhileUseComponent.CODEC).packetCodec(AdjustFovWhileUseComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("adjust_fov_while_use"));
	DataComponentType<ModifyMoveComponent> MODIFY_MOVE = DataComponentTypeRegistry.registrar(() ->
					DataComponentType.<ModifyMoveComponent>builder().codec(ModifyMoveComponent.CODEC).packetCodec(ModifyMoveComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("modify_move"));
	DataComponentType<ModifyMoveWhileHoldComponent> MODIFY_MOVE_WHILE_HOLD = DataComponentTypeRegistry.registrar(() ->
					DataComponentType.<ModifyMoveWhileHoldComponent>builder().codec(ModifyMoveWhileHoldComponent.CODEC).packetCodec(ModifyMoveWhileHoldComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("modify_move_while_hold"));
	DataComponentType<ModifyMoveWhileUseComponent> MODIFY_MOVE_WHILE_USE = DataComponentTypeRegistry.registrar(() ->
					DataComponentType.<ModifyMoveWhileUseComponent>builder().codec(ModifyMoveWhileUseComponent.CODEC).packetCodec(ModifyMoveWhileUseComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("modify_move_while_use"));
	DataComponentType<ProjectileContainerComponent> PROJECTILE_CONTAINER = DataComponentTypeRegistry.registrar(() ->
					DataComponentType.<ProjectileContainerComponent>builder().codec(ProjectileContainerComponent.CODEC).packetCodec(ProjectileContainerComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("projectile_container"));
	DataComponentType<ShootProjectilesComponent> SHOOT_PROJECTILES = DataComponentTypeRegistry.registrar(() ->
					DataComponentType.<ShootProjectilesComponent>builder().codec(ShootProjectilesComponent.CODEC).packetCodec(ShootProjectilesComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("shoot_projectiles"));
	DataComponentType<RangedWeaponComponent> RANGED_WEAPON = DataComponentTypeRegistry.registrar(() ->
					DataComponentType.<RangedWeaponComponent>builder().codec(RangedWeaponComponent.CODEC).packetCodec(RangedWeaponComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("ranged_weapon"));
}
