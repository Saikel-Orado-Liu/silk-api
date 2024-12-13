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

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.List;

/**
 * <h2>自带多状态效果组件</h2>
 * 用于说明自带状态效果的物品的状态效果属性
 *
 * @param inherentStatusEffects 自带状态效果列表
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.1.2
 */
public record InherentStatusEffectsComponent(List<InherentStatusEffectData> inherentStatusEffects) {
    /** 自带多状态效果组件的编解码器 */
    public static final Codec<InherentStatusEffectsComponent> CODEC = RecordCodecBuilder
            .create(builder -> builder
                    .group(InherentStatusEffectData.CODEC
                            .listOf()
                            .fieldOf("inherent_status_effect")
                            .forGetter(InherentStatusEffectsComponent::inherentStatusEffects))
                    .apply(builder, InherentStatusEffectsComponent::new));
    /** 自带多状态效果组件的数据包编解码器 */
    public static final PacketCodec<RegistryByteBuf, InherentStatusEffectsComponent> PACKET_CODEC =
            PacketCodecs.registryCodec(CODEC);

    /**
     * 自带多个状态效果组件创建方法
     *
     * @param inherentStatusEffects 自带状态效果列表
     * @return 自带多个状态效果组件
     */
    public static InherentStatusEffectsComponent of(
            InherentStatusEffectData... inherentStatusEffects) {
        return new InherentStatusEffectsComponent(ImmutableList.copyOf(inherentStatusEffects));
    }
}
