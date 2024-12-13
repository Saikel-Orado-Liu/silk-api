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

package pers.saikel0rado1iu.silk.mixin.ropestick.component;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import pers.saikel0rado1iu.silk.api.ropestick.component.DynamicComponent;

/**
 * <h2>{@link DynamicComponent} 混入</h2>
 * 设置动态数据组件
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.1.2
 */
@Mixin(PlayerInventory.class)
abstract class DynamicComponentMixin {
    @ModifyVariable(method = "setStack", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private ItemStack setStack(ItemStack stack, @Local(argsOnly = true) int slot) {
        if (!(stack.getItem() instanceof DynamicComponent dynamicComponent)) {
            return stack;
        }
        stack.applyComponentsFrom(dynamicComponent.dynamicComponents(stack));
        return stack;
    }
}
