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

package pers.saikel0rado1iu.silk.mixin.api.item;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.api.item.tool.HoldingMovementMultiplier;

/**
 * <p><b style="color:FFC800"><font size="+1">设置在持有物品时的移动速度</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@Mixin(ClientPlayerEntity.class)
abstract class HoldingMovementMultiplierMixin extends AbstractClientPlayerEntity {
	@Shadow
	public Input input;
	
	public HoldingMovementMultiplierMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}
	
	@Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "L net/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", shift = At.Shift.BEFORE))
	private void applyMovementMultiple(CallbackInfo ci) {
		Item mainHandItem = getMainHandStack().getItem();
		Item offHandItem = getOffHandStack().getItem();
		if (mainHandItem instanceof HoldingMovementMultiplier multiplier) {
			input.movementSideways *= multiplier.getHoldingMovementMultiple();
			input.movementForward *= multiplier.getHoldingMovementMultiple();
		}
		if (offHandItem instanceof HoldingMovementMultiplier multiplier && !multiplier.isConflictItems(mainHandItem)) {
			input.movementSideways *= multiplier.getHoldingMovementMultiple();
			input.movementForward *= multiplier.getHoldingMovementMultiple();
		}
	}
}