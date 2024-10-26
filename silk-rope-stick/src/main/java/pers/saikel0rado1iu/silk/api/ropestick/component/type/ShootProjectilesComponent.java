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
 * <h2 style="color:FFC800">射击发射物组件</h2>
 * 用于设置弓弩的发射物发射属性的数据组件
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public record ShootProjectilesComponent(boolean shot, int interval, State state) {
	public static final int DEFAULT_SHOOTING_INTERVAL = TickUtil.getTick(0.25F);
	public static final ShootProjectilesComponent DEFAULT = new ShootProjectilesComponent(false, DEFAULT_SHOOTING_INTERVAL, State.EVERY);
	public static final Codec<ShootProjectilesComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
					Codec.BOOL.optionalFieldOf("shot", false).forGetter(ShootProjectilesComponent::shot),
					Codec.INT.optionalFieldOf("interval", DEFAULT_SHOOTING_INTERVAL).forGetter(ShootProjectilesComponent::interval),
					Codec.STRING.optionalFieldOf("state", State.EVERY.name()).forGetter(component -> component.state.name()))
			.apply(builder, (shot, interval, state) -> new ShootProjectilesComponent(shot, interval, State.valueOf(state))));
	public static final PacketCodec<RegistryByteBuf, ShootProjectilesComponent> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);
	
	/**
	 * 设置物品已射击
	 *
	 * @param stack 物品堆栈
	 */
	public void setShot(ItemStack stack) {
		setShot(stack, true);
	}
	
	/**
	 * 重置物品至未射击状态
	 *
	 * @param stack 物品堆栈
	 */
	public void resetShot(ItemStack stack) {
		setShot(stack, false);
	}
	
	/**
	 * 设置物品的射击状态
	 *
	 * @param stack 物品堆栈
	 * @param shot  是否已射击
	 */
	public void setShot(ItemStack stack, boolean shot) {
		stack.set(DataComponentTypes.SHOOT_PROJECTILES, new ShootProjectilesComponent(shot, interval, state));
	}
	
	/**
	 * 射击状态枚举
	 */
	public enum State {
		/**
		 * 每个发射物发射后都会设置已发射状态
		 */
		EVERY,
		/**
		 * 只有当所有发射物都被发射后才会设置已发射状态
		 */
		ALL
	}
}
