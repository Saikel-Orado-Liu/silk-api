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

import net.minecraft.component.ComponentType;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.*;
import pers.saikel0rado1iu.silk.api.spinningjenny.ComponentTypeRegistry;
import pers.saikel0rado1iu.silk.impl.SilkApi;

/**
 * <h2 style="color:FFC800">数据组件类型</h2>
 * 储存了扩展的数据组件类型
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public interface ComponentTypes extends ComponentTypeRegistry {
	ComponentType<AdjustFovWhileHoldComponent> ADJUST_FOV_WHILE_HOLD = ComponentTypeRegistry.registrar(() ->
					ComponentType.<AdjustFovWhileHoldComponent>builder().codec(AdjustFovWhileHoldComponent.CODEC).packetCodec(AdjustFovWhileHoldComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("adjust_fov_while_hold"));
	ComponentType<AdjustFovWhileUseComponent> ADJUST_FOV_WHILE_USE = ComponentTypeRegistry.registrar(() ->
					ComponentType.<AdjustFovWhileUseComponent>builder().codec(AdjustFovWhileUseComponent.CODEC).packetCodec(AdjustFovWhileUseComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("adjust_fov_while_use"));
	ComponentType<ModifyMoveWhileHoldComponent> MODIFY_MOVE_WHILE_HOLD = ComponentTypeRegistry.registrar(() ->
					ComponentType.<ModifyMoveWhileHoldComponent>builder().codec(ModifyMoveWhileHoldComponent.CODEC).packetCodec(ModifyMoveWhileHoldComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("modify_move_while_hold"));
	ComponentType<ModifyMoveWhileUseComponent> MODIFY_MOVE_WHILE_USE = ComponentTypeRegistry.registrar(() ->
					ComponentType.<ModifyMoveWhileUseComponent>builder().codec(ModifyMoveWhileUseComponent.CODEC).packetCodec(ModifyMoveWhileUseComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("modify_move_while_use"));
	ComponentType<ProjectileContainerComponent> PROJECTILE_CONTAINER = ComponentTypeRegistry.registrar(() ->
					ComponentType.<ProjectileContainerComponent>builder().codec(ProjectileContainerComponent.CODEC).packetCodec(ProjectileContainerComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("projectile_container"));
	ComponentType<ShootProjectilesComponent> SHOOT_PROJECTILES = ComponentTypeRegistry.registrar(() ->
					ComponentType.<ShootProjectilesComponent>builder().codec(ShootProjectilesComponent.CODEC).packetCodec(ShootProjectilesComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("shoot_projectiles"));
	ComponentType<RangedWeaponComponent> RANGED_WEAPON = ComponentTypeRegistry.registrar(() ->
					ComponentType.<RangedWeaponComponent>builder().codec(RangedWeaponComponent.CODEC).packetCodec(RangedWeaponComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("ranged_weapon"));
	ComponentType<InherentStatusEffectsComponent> INHERENT_STATUS_EFFECTS = ComponentTypeRegistry.registrar(() ->
					ComponentType.<InherentStatusEffectsComponent>builder().codec(InherentStatusEffectsComponent.CODEC).packetCodec(InherentStatusEffectsComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("inherent_status_effects"));
	ComponentType<CustomEntityHurtComponent> CUSTOM_ENTITY_HURT = ComponentTypeRegistry.registrar(() ->
					ComponentType.<CustomEntityHurtComponent>builder().codec(CustomEntityHurtComponent.CODEC).packetCodec(CustomEntityHurtComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("custom_entity_hurt"));
	ComponentType<EnchantmentTraitsComponent> ENCHANTMENT_TRAITS = ComponentTypeRegistry.registrar(() ->
					ComponentType.<EnchantmentTraitsComponent>builder().codec(EnchantmentTraitsComponent.CODEC).packetCodec(EnchantmentTraitsComponent.PACKET_CODEC).build())
			.register(SilkApi.getInternal().ofId("enchantment_traits"));
}