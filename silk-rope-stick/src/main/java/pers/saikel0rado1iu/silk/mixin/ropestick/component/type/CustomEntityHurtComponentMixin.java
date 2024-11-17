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

package pers.saikel0rado1iu.silk.mixin.ropestick.component.type;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import pers.saikel0rado1iu.silk.api.ropestick.component.ComponentTypes;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.CustomEntityHurtComponent;

/**
 * <h2 style="color:FFC800">{@link CustomEntityHurtComponent} 混入</h2>
 * 设置需要自定义盔甲物品所阻挡的伤害
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
@Mixin(LivingEntity.class)
abstract class CustomEntityHurtComponentMixin {
	@Unique
	private DamageSource damageSource;
	
	@Shadow
	public abstract ItemStack getEquippedStack(EquipmentSlot slot);
	
	@Shadow
	public abstract boolean damage(DamageSource source, float amount);
	
	@ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private DamageSource getSource(DamageSource source) {
		return damageSource = source;
	}
	
	@ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float setDamage(float amount) {
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			ItemStack stack = getEquippedStack(slot);
			CustomEntityHurtComponent component = stack.get(ComponentTypes.CUSTOM_ENTITY_HURT);
			if (component == null) continue;
			if (!component.damageTypes().contains(damageSource.getTypeRegistryEntry().getKey().orElseThrow())) continue;
			return component.evaluateExpression(stack, amount);
		}
		return amount;
	}
}
