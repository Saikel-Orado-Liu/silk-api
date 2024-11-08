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
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

/**
 * <h2 style="color:FFC800">在使用时修改移动组件</h2>
 * 使用物品时的移动速度修改器组件
 *
 * @param modifyMove 修改移动数据
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public record ModifyMoveWhileUseComponent(ModifyMoveData modifyMove) {
	public static final ModifyMoveWhileUseComponent DEFAULT = new ModifyMoveWhileUseComponent(ModifyMoveData.DEFAULT);
	public static final Codec<ModifyMoveWhileUseComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
					ModifyMoveData.CODEC.optionalFieldOf("modify_move", ModifyMoveData.DEFAULT).forGetter(ModifyMoveWhileUseComponent::modifyMove))
			.apply(builder, ModifyMoveWhileUseComponent::new));
	public static final PacketCodec<RegistryByteBuf, ModifyMoveWhileUseComponent> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);
	
	/**
	 * 在使用时修改移动组件创建方法
	 *
	 * @param moveSpeedMultiple 相较于行走时的移动速度倍率
	 * @return 在使用时修改移动组件
	 */
	public static ModifyMoveWhileUseComponent of(float moveSpeedMultiple) {
		return new ModifyMoveWhileUseComponent(ModifyMoveData.of(moveSpeedMultiple));
	}
}
