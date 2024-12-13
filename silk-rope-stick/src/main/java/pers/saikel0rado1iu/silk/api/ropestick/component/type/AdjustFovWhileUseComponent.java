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
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.api.ropestick.ranged.BowLikeItem;
import pers.saikel0rado1iu.silk.api.ropestick.ranged.CrossbowLikeItem;

/**
 * <h2>在使用时调整视场角组件</h2>
 * 使用物品时的视场角缩放组件
 *
 * @param adjustFov 调整视场角数据
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.1.2
 */
public record AdjustFovWhileUseComponent(AdjustFovData adjustFov) {
    /** 默认的在使用时调整视场角组件 */
    public static final AdjustFovWhileUseComponent DEFAULT =
            new AdjustFovWhileUseComponent(AdjustFovData.DEFAULT);
    /** 在使用时调整视场角组件的编解码器 */
    public static final Codec<AdjustFovWhileUseComponent> CODEC = RecordCodecBuilder
            .create(builder -> builder
                    .group(AdjustFovData.CODEC
                            .optionalFieldOf("adjust_fov", AdjustFovData.DEFAULT)
                            .forGetter(AdjustFovWhileUseComponent::adjustFov))
                    .apply(builder, AdjustFovWhileUseComponent::new));
    /** 在使用时调整视场角组件的数据包编解码器 */
    public static final PacketCodec<RegistryByteBuf, AdjustFovWhileUseComponent> PACKET_CODEC =
            PacketCodecs.registryCodec(CODEC);

    /**
     * 在使用时调整视场角组件创建方法
     *
     * @param onlyFirstPerson 是否只在第一人称进行缩放
     * @param hudOverlay      抬头显示叠加层，如果为 {@code null} 则不显示叠加层
     * @param canStretchHud   是否可以拉伸抬头显示
     * @param fovScaling      视场角缩放倍数，视场角缩放倍数，&gt; 1 则为放大，0 &lt; x &lt; 1 则为缩小
     * @return 在使用时调整视场角组件
     */
    public static AdjustFovWhileUseComponent create(boolean onlyFirstPerson,
                                                    @Nullable Identifier hudOverlay,
                                                    boolean canStretchHud, float fovScaling) {
        return new AdjustFovWhileUseComponent(AdjustFovData.create(
                onlyFirstPerson, hudOverlay, canStretchHud, fovScaling));
    }

    /**
     * 获取使用进度
     *
     * @param stack    物品堆栈
     * @param user     使用实体
     * @param useTicks 使用刻数
     * @return 使用进度，在 0 和 1 之间的浮点数
     */
    public static float getUsingProgress(ItemStack stack, LivingEntity user, int useTicks) {
        Item item = stack.getItem();
        if (item instanceof BowLikeItem bow) {
            return bow.getUsingProgress(stack, user, useTicks);
        } else if (item instanceof CrossbowLikeItem crossbow) {
            return crossbow.getUsingProgress(stack, user, useTicks);
        }
        return Math.min(1, (float) useTicks / stack.getMaxUseTime(user));
    }
}
