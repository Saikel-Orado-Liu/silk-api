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

package pers.saikel0rado1iu.silk.mixin.generate.advancement.criterion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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
import pers.saikel0rado1iu.silk.api.generate.advancement.criterion.Criteria;
import pers.saikel0rado1iu.silk.api.generate.advancement.criterion.ShotProjectileCriterion;

import java.util.List;

/**
 * <h2 style="color:FFC800">{@link ShotProjectileCriterion} 混入</h2>
 * 设置射击弹药标准在原版中弓弩可触发
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
interface ShotProjectileCriterionMixin {
	/**
	 * 远程武器物品混入
	 */
	@Mixin(net.minecraft.item.RangedWeaponItem.class)
	abstract class RangedWeaponItem {
		@Unique
		private Entity persistentProjectileEntity;
		
		@ModifyArg(method = "shootAll", at = @At(value = "INVOKE", target = "L net/minecraft/world/World;spawnEntity(L net/minecraft/entity/Entity;)Z"))
		private Entity getEntity(Entity entity) {
			return persistentProjectileEntity = entity;
		}
		
		@Inject(method = "shootAll", at = @At(value = "INVOKE", target = "L net/minecraft/world/World;spawnEntity(L net/minecraft/entity/Entity;)Z", shift = At.Shift.AFTER))
		private void shootAll(World world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, LivingEntity target, CallbackInfo ci) {
			if (shooter instanceof ServerPlayerEntity serverPlayer) Criteria.SHOT_PROJECTILE_CRITERION.trigger(serverPlayer, stack, persistentProjectileEntity);
		}
	}
}