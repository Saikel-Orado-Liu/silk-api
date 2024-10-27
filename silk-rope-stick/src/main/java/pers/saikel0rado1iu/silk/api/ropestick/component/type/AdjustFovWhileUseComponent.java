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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import pers.saikel0rado1iu.silk.api.ropestick.ranged.BowLikeItem;
import pers.saikel0rado1iu.silk.api.ropestick.ranged.CrossbowLikeItem;

/**
 * <h2 style="color:FFC800">在使用时调整视场角组件</h2>
 * 使用物品时的视场角缩放组件
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public record AdjustFovWhileUseComponent(AdjustFovComponent adjustFov) {
	public static final AdjustFovWhileUseComponent DEFAULT = new AdjustFovWhileUseComponent(AdjustFovComponent.DEFAULT);
	public static final Codec<AdjustFovWhileUseComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
					AdjustFovComponent.CODEC.optionalFieldOf("adjust_fov", AdjustFovComponent.DEFAULT).forGetter(AdjustFovWhileUseComponent::adjustFov))
			.apply(builder, AdjustFovWhileUseComponent::new));
	public static final PacketCodec<RegistryByteBuf, AdjustFovWhileUseComponent> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);
	
	/**
	 * 获取使用进度
	 *
	 * @param useTicks 使用刻数
	 * @param stack    物品堆栈
	 * @return 使用进度，在 0 和 1 之间的浮点数
	 */
	public static float getUsingProgress(int useTicks, ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof BowLikeItem bow) return bow.getUsingProgress(useTicks, stack);
		else if (item instanceof CrossbowLikeItem crossbow) return crossbow.getUsingProgress(useTicks, stack);
		return Math.min(1, (float) useTicks / stack.getMaxUseTime());
	}
}
