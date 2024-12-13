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

import net.minecraft.component.ComponentMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * <h2>动态组件</h2>
 * 此接口应在 {@link Item} 上实现。<p> 会在每次玩家更改物品栏时，对此物品栏中的 {@link ItemStack} 进行数据组件修改。<p>
 * 目前此动态组件的赋予对非玩家实体无效。
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.1.2
 */
public interface DynamicComponent {
    /**
     * 动态数据组件图表
     *
     * @param stack 物品堆栈
     * @return 组件图表
     */
    ComponentMap dynamicComponents(ItemStack stack);
}
