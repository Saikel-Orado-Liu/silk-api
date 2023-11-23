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

package pers.saikel0rado1iu.silk.mixin.api.criterion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.api.registry.gen.data.criterion.SilkCriteria;

/**
 * <p><b style="color:FFC800"><font size="+1">设置射击弹药标准在原版中弓弩可触发</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
interface ShotProjectileCriterionMixin {
	@Mixin(BowItem.class)
	abstract class Bow {
		@Unique
		private Entity persistentProjectileEntity;
		
		@ModifyArg(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "L net/minecraft/world/World;spawnEntity(L net/minecraft/entity/Entity;)Z"))
		private Entity getEntity(Entity entity) {
			return persistentProjectileEntity = entity;
		}
		
		@Inject(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "L net/minecraft/world/World;spawnEntity(L net/minecraft/entity/Entity;)Z", shift = At.Shift.AFTER))
		private void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
			if (user instanceof ServerPlayerEntity serverPlayer)
				SilkCriteria.SHOT_PROJECTILE_CRITERION.trigger(serverPlayer, stack, persistentProjectileEntity, 1);
		}
	}
	
	@Mixin(CrossbowItem.class)
	abstract class Crossbow {
		@Unique
		private static Entity projectileEntity;
		
		@ModifyArg(method = "shoot", at = @At(value = "INVOKE", target = "L net/minecraft/world/World;spawnEntity(L net/minecraft/entity/Entity;)Z"))
		private static Entity getEntity(Entity entity) {
			return projectileEntity = entity;
		}
		
		@Inject(method = "shoot", at = @At("TAIL"))
		private static void shoot(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated, CallbackInfo ci) {
			if (shooter instanceof ServerPlayerEntity serverPlayer) SilkCriteria.SHOT_PROJECTILE_CRITERION.trigger(serverPlayer, crossbow, projectileEntity, 1);
		}
	}
}
