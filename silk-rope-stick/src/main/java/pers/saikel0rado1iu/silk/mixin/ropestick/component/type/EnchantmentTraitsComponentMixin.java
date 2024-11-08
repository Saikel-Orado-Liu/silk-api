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

import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.DataComponentType;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.api.ropestick.component.DataComponentTypes;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.EnchantmentTraitsComponent;

import static net.minecraft.component.DataComponentTypes.ENCHANTMENTS;

/**
 * <h2 style="color:FFC800">{@link EnchantmentTraitsComponent} 混入</h2>
 * 设置自定义物品附魔
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
interface EnchantmentTraitsComponentMixin {
	@Mixin(Enchantment.class)
	abstract class SetAcceptEnchantment {
		@Unique
		private static Identifier getId(Enchantment enchantment) {
			return Registries.ENCHANTMENT.getId(enchantment);
		}
		
		/**
		 * 如果物品为自定义物品判断此魔咒是否包含在自定义魔咒中，所以请忽略 'EqualsBetweenInconvertibleTypes' 警告
		 */
		@SuppressWarnings({"DataFlowIssue"})
		@Inject(method = "isAcceptableItem", at = @At("RETURN"), cancellable = true)
		private void acceptEnchantment(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
			EnchantmentTraitsComponent component = stack.get(DataComponentTypes.ENCHANTMENT_TRAITS);
			if (component == null) return;
			component.enchantments().forEach(trait -> {
				if (!getId(trait.enchantment()).equals(getId((Enchantment) (Object) this))) return;
				int conflictNum = 0;
				for (Enchantment enchantment : trait.conflicts()) {
					if (stack.getEnchantments().getEnchantments().stream().anyMatch(entry -> getId(enchantment).equals(getId(entry.value())))) conflictNum++;
				}
				if (conflictNum > trait.threshold()) cir.setReturnValue(false);
				else cir.setReturnValue(true);
			});
		}
	}
	
	@Mixin(ItemStack.class)
	abstract class SetTableEnchantment implements ComponentHolder, FabricItemStack {
		@Shadow
		public abstract Item getItem();
		
		@Shadow
		@Nullable
		public abstract <T> T set(DataComponentType<? super T> type, @Nullable T value);
		
		@Inject(method = "isEnchantable", at = @At("HEAD"), cancellable = true)
		private void setEnchantment(CallbackInfoReturnable<Boolean> cir) {
			if (!getItem().isEnchantable((ItemStack) (Object) this)) {
				cir.setReturnValue(false);
			} else {
				if (contains(DataComponentTypes.ENCHANTMENT_TRAITS) && !contains(ENCHANTMENTS)) set(ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
				ItemEnchantmentsComponent itemEnchantmentsComponent = get(ENCHANTMENTS);
				cir.setReturnValue(itemEnchantmentsComponent != null && itemEnchantmentsComponent.isEmpty());
			}
		}
	}
}
