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

package pers.saikel0rado1iu.silk.mixin.client.ropestick.component.type;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.api.ropestick.component.DataComponentTypes;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.ModifyMoveWhileHoldComponent;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">{@link ModifyMoveWhileHoldComponent} 混入</h2>
 * 设置在持有物品时的移动速度
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
@Mixin(ClientPlayerEntity.class)
abstract class ModifyMoveWhileHoldComponentMixin extends AbstractClientPlayerEntity {
	@Shadow
	public Input input;
	
	public ModifyMoveWhileHoldComponentMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}
	
	@Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "L net/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", shift = At.Shift.AFTER))
	private void applyMovementMultiple(CallbackInfo ci) {
		ItemStack mainHandItem = getMainHandStack();
		ItemStack offHandItem = getOffHandStack();
		Optional.ofNullable(mainHandItem.get(DataComponentTypes.MODIFY_MOVE_WHILE_HOLD)).ifPresent(component -> {
			if (!component.canModifyMove()) return;
			input.movementSideways *= component.modifyMove().moveSpeedMultiple();
			input.movementForward *= component.modifyMove().moveSpeedMultiple();
		});
		Optional.ofNullable(offHandItem.get(DataComponentTypes.MODIFY_MOVE_WHILE_HOLD)).ifPresent(component -> {
			if (component.isConflictItem(mainHandItem)) return;
			if (!component.canModifyMove()) return;
			input.movementSideways *= component.modifyMove().moveSpeedMultiple();
			input.movementForward *= component.modifyMove().moveSpeedMultiple();
		});
	}
}
