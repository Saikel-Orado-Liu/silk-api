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

package pers.saikel0rado1iu.silk.api.ropestick.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * <h2 style="color:FFC800">修改移动数据</h2>
 * 物品的移动速度修改通用组件数据
 *
 * @param moveSpeedMultiple 相较于行走时的移动速度倍率
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public record ModifyMoveData(float moveSpeedMultiple) {
	/**
	 * 默认移动速度倍速
	 */
	public static final float DEFAULT_SPEED_MULTIPLE = 0.2F;
	public static final ModifyMoveData DEFAULT = ModifyMoveData.of(DEFAULT_SPEED_MULTIPLE);
	public static final Codec<ModifyMoveData> CODEC = RecordCodecBuilder.create(builder -> builder.group(
					Codec.FLOAT.optionalFieldOf("move_speed_multiple", DEFAULT_SPEED_MULTIPLE).forGetter(ModifyMoveData::moveSpeedMultiple))
			.apply(builder, ModifyMoveData::new));
	
	/**
	 * 修改移动数据创建方法
	 *
	 * @param moveSpeedMultiple 相较于行走时的移动速度倍率
	 * @return 修改移动数据
	 */
	public static ModifyMoveData of(float moveSpeedMultiple) {
		return new ModifyMoveData(moveSpeedMultiple);
	}
}
