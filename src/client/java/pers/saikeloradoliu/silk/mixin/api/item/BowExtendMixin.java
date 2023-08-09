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

package pers.saikeloradoliu.silk.mixin.api.item;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import pers.saikeloradoliu.silk.api.item.BowExtend;

import static pers.saikeloradoliu.silk.api.item.BowExtend.DEFAULT_USING_SPEED_RATIO;

/**
 * <p><b style="color:FFC800"><font size="+1">设置远程武器的扩展</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
final class BowExtendMixin {
	/**
	 * 设置在拉弓时的移动速度
	 */
	@Mixin(ClientPlayerEntity.class)
	abstract static class SetUsingMovementMultiple extends AbstractClientPlayerEntity {
		@Shadow
		public Input input;
		
		private SetUsingMovementMultiple(ClientWorld world, GameProfile profile) {
			super(world, profile);
		}
		
		@Shadow
		public abstract Hand getActiveHand();
		
		@Shadow
		public abstract boolean isUsingItem();
		
		@Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "L net/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", shift = At.Shift.AFTER))
		private void applyMovementMultiple(CallbackInfo ci) {
			if (isUsingItem() && getStackInHand(getActiveHand()).getItem() instanceof BowExtend bow) {
				input.movementSideways /= DEFAULT_USING_SPEED_RATIO;
				input.movementSideways *= bow.getUsingMovementMultiple();
				input.movementForward /= DEFAULT_USING_SPEED_RATIO;
				input.movementForward *= bow.getUsingMovementMultiple();
			}
		}
	}
	
	/**
	 * 设置弓使用时的视角缩放
	 */
	@Mixin(GameRenderer.class)
	abstract static class SetUsingFovScale implements AutoCloseable {
		@Shadow
		@Final
		MinecraftClient client;
		@Shadow
		private float fovMultiplier;
		
		@Inject(method = "tick", at = @At(value = "INVOKE", target = "L net/minecraft/client/render/GameRenderer;updateFovMultiplier()V", shift = At.Shift.AFTER))
		private void setFovScale(CallbackInfo ci) {
			ClientPlayerEntity player = client.player;
			if (player == null) return;
			if (player.getActiveItem().getItem() instanceof BowExtend bow) {
				float pullProgress = bow.getBowPullProgress(bow.getMaxUseTicks() - player.getItemUseTimeLeft());
				float fovChangeAmount = fovMultiplier - fovMultiplier * bow.getUsingFovMultiple() * pullProgress;
				fovMultiplier -= fovChangeAmount;
			}
		}
	}
	
	/**
	 * 设置鼠标移动缩放
	 */
	@Mixin(Mouse.class)
	abstract static class SetMouseMoveScale {
		@Shadow
		@Final
		private MinecraftClient client;
		
		@ModifyArgs(method = "updateMouse", at = @At(value = "INVOKE", target = "L net/minecraft/client/tutorial/TutorialManager;onUpdateMouse(DD)V"))
		private void setMove(Args args) {
			ClientPlayerEntity player = client.player;
			if (player == null) return;
			if (client.options.getPerspective().isFirstPerson() && player.getActiveItem().getItem() instanceof BowExtend bow) {
				float pullProgress = bow.getBowPullProgress(bow.getMaxUseTicks() - player.getItemUseTimeLeft());
				float moveMultiple = 1 - (1 - bow.getDamageMultiple()) * pullProgress;
				args.set(0, (double) args.get(0) * moveMultiple);
				args.set(1, (double) args.get(1) * moveMultiple);
			}
		}
		
		@ModifyArgs(method = "updateMouse", at = @At(value = "INVOKE", target = "L net/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"))
		private void setLookDirection(Args args) {
			ClientPlayerEntity player = client.player;
			if (player == null) return;
			if (client.options.getPerspective().isFirstPerson() && player.getActiveItem().getItem() instanceof BowExtend bow) {
				float pullProgress = bow.getBowPullProgress(bow.getMaxUseTicks() - player.getItemUseTimeLeft());
				float moveMultiple = 1 - (1 - bow.getDamageMultiple()) * pullProgress;
				args.set(0, (double) args.get(0) * moveMultiple);
				args.set(1, (double) args.get(1) * moveMultiple);
			}
		}
	}
}