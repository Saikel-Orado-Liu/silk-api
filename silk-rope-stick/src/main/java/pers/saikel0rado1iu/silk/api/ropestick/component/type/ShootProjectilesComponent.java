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
import pers.saikel0rado1iu.silk.api.base.common.util.TickUtil;
import pers.saikel0rado1iu.silk.api.ropestick.component.DataComponentTypes;

/**
 * <h2>射击发射物组件</h2>
 * 用于设置弓弩的发射物发射属性的数据组件
 *
 * @param interval 射击间隔
 * @param state    射击状态
 * @param shot     是否已射击
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.1.2
 */
public record ShootProjectilesComponent(int interval, State state, boolean shot) {
    /** 已射击 NBT 谓词 */
    public static final String SHOT_KEY = "shot";
    /** 默认射击间隔 */
    public static final int DEFAULT_SHOOTING_INTERVAL = TickUtil.getTick(0.25F);
    /** 射击发射物组件的默认值 */
    public static final ShootProjectilesComponent DEFAULT =
            ShootProjectilesComponent.create(DEFAULT_SHOOTING_INTERVAL, State.EVERY);
    /** 射击发射物组件的编解码器 */
    public static final Codec<ShootProjectilesComponent> CODEC = RecordCodecBuilder
            .create(builder -> builder
                    .group(Codec.INT.optionalFieldOf("interval", DEFAULT_SHOOTING_INTERVAL)
                                    .forGetter(ShootProjectilesComponent::interval),
                            Codec.STRING.optionalFieldOf("state", State.EVERY.name())
                                        .forGetter(component -> component.state.name()),
                            Codec.BOOL.optionalFieldOf("shot", false)
                                      .forGetter(ShootProjectilesComponent::shot))
                    .apply(builder, (interval, state, shot) ->
                            new ShootProjectilesComponent(interval, State.valueOf(state), shot)));
    /** 射击发射物组件的数据包编解码器 */
    public static final PacketCodec<RegistryByteBuf, ShootProjectilesComponent> PACKET_CODEC =
            PacketCodecs.registryCodec(CODEC);

    /**
     * 创建射击发射物组件方法
     *
     * @param interval 射击间隔
     * @param state    射击状态
     * @return 射击发射物组件
     */
    public static ShootProjectilesComponent create(int interval, State state) {
        return new ShootProjectilesComponent(interval, state, false);
    }

    /**
     * 获取物品的射击状态
     *
     * @param stack 物品堆栈
     * @return 是否已射击
     */
    public static boolean isShot(ItemStack stack) {
        ShootProjectilesComponent shot = stack.get(DataComponentTypes.SHOOT_PROJECTILES);
        return shot != null && shot.shot;
    }

    /**
     * 设置已射击
     *
     * @return 射击发射物组件
     */
    public ShootProjectilesComponent setShot() {
        return setShot(true);
    }

    /**
     * 重置至未射击状态
     *
     * @return 射击发射物组件
     */
    public ShootProjectilesComponent resetShot() {
        return setShot(false);
    }

    /**
     * 设置射击状态
     *
     * @param shot 是否已射击
     * @return 射击发射物组件
     */
    public ShootProjectilesComponent setShot(boolean shot) {
        return new ShootProjectilesComponent(interval, state, shot);
    }

    /**
     * <h2>射击状态枚举</h2>
     * 射击发射物的射击状态
     *
     * @author <a href="https://github.com/Saikel-Orado-Liu">
     *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
     *         </a>
     * @since 1.1.2
     */
    public enum State {
        /** 每个发射物发射后都会设置已发射状态 */
        EVERY,
        /** 只有当所有发射物都被发射后，才会设置已发射状态 */
        ALL
    }
}
