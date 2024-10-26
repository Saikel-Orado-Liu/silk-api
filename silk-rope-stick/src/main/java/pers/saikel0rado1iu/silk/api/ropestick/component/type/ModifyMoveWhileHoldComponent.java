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
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import pers.saikel0rado1iu.silk.api.ropestick.component.DataComponentTypes;

/**
 * <h2 style="color:FFC800">在持有时修改移动组件</h2>
 * 握持物品时的移动速度修改器组件
 *
 * @param modifyMove    修改移动组件
 * @param canModifyMove 是否能够修改移动速度
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public record ModifyMoveWhileHoldComponent(ModifyMoveComponent modifyMove, boolean canModifyMove) {
	public static final ModifyMoveWhileHoldComponent DEFAULT = new ModifyMoveWhileHoldComponent(ModifyMoveComponent.DEFAULT, true);
	public static final Codec<ModifyMoveWhileHoldComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
					ModifyMoveComponent.CODEC.optionalFieldOf("modify_move", ModifyMoveComponent.DEFAULT).forGetter(ModifyMoveWhileHoldComponent::modifyMove),
					Codec.BOOL.optionalFieldOf("can_modify_move", true).forGetter(ModifyMoveWhileHoldComponent::canModifyMove))
			.apply(builder, ModifyMoveWhileHoldComponent::new));
	public static final PacketCodec<RegistryByteBuf, ModifyMoveWhileHoldComponent> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);
	
	/**
	 * 判断是否为冲突物品
	 *
	 * @param checkItem 检查物品
	 * @return 是否冲突
	 */
	public boolean isConflictItem(ItemStack checkItem) {
		return checkItem.contains(DataComponentTypes.ADJUST_FOV_WHILE_HOLD);
	}
}
