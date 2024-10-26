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
 * <h2 style="color:FFC800">在持有时调整视场角组件</h2>
 * 持有物品时的视场角缩放组件
 *
 * @param adjustFov    调整视场角组件
 * @param canAdjustFov 是否能够调整视场角
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public record AdjustFovWhileHoldComponent(AdjustFovComponent adjustFov, boolean canAdjustFov) {
	public static final AdjustFovWhileHoldComponent DEFAULT = new AdjustFovWhileHoldComponent(AdjustFovComponent.DEFAULT, true);
	public static final Codec<AdjustFovWhileHoldComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
					AdjustFovComponent.CODEC.optionalFieldOf("adjust_fov", AdjustFovComponent.DEFAULT).forGetter(AdjustFovWhileHoldComponent::adjustFov),
					Codec.BOOL.optionalFieldOf("can_adjust_fov", true).forGetter(AdjustFovWhileHoldComponent::canAdjustFov))
			.apply(builder, AdjustFovWhileHoldComponent::new));
	public static final PacketCodec<RegistryByteBuf, AdjustFovWhileHoldComponent> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);
	
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
