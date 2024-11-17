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
import net.minecraft.item.ItemStack;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">组件实用工具</h2>
 * 用于提供数据组件的部分常用的操作方法
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public interface ComponentUtil {
	/**
	 * 获取一个物品堆栈中的一个数据组件或者设置此组件的默认值<br>
	 * 优先获取存储在物品堆栈中的数据组件，如果此数据组件不存在，则为这个物品设置默认组件值。如果无法设置值则返回 {@link Optional#empty()}
	 *
	 * @param stack        物品堆栈
	 * @param type         组件类型
	 * @param defaultValue 组件默认值
	 * @param <T>          组件类型
	 * @return 储存的物品中的组件的值
	 */
	static <T> Optional<T> getOrSetDefault(ItemStack stack, ComponentType<T> type, T defaultValue) {
		T component = stack.get(type);
		if (component == null) component = stack.set(type, defaultValue);
		return Optional.ofNullable(component);
	}
	
	/**
	 * 设置一个物品堆栈中的一个数据组件或者返回此组件的设置值<br>
	 * 设置存储在物品堆栈中的数据组件，如果无法设置数据组件，则为返回物品的组件值
	 *
	 * @param stack 物品堆栈
	 * @param type  组件类型
	 * @param value 组件设置值
	 * @param <T>   组件类型
	 * @return 储存的物品中的组件的值
	 */
	static <T> T setOrGetValue(ItemStack stack, ComponentType<T> type, T value) {
		T component = stack.set(type, value);
		return component == null ? stack.getOrDefault(type, value) : component;
	}
}
