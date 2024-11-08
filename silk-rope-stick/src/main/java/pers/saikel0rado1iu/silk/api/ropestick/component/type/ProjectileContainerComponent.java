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
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2 style="color:FFC800">发射物容器组件</h2>
 * 用于设置最大可存储多少发射物
 *
 * @param maxCapacity 最大发射物容量
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public record ProjectileContainerComponent(int maxCapacity) {
	public static final ProjectileContainerComponent DEFAULT = ProjectileContainerComponent.of(1);
	public static final Codec<ProjectileContainerComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
					Codec.INT.optionalFieldOf("max_capacity", 1).forGetter(ProjectileContainerComponent::maxCapacity))
			.apply(builder, ProjectileContainerComponent::new));
	public static final PacketCodec<RegistryByteBuf, ProjectileContainerComponent> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);
	
	/**
	 * 创建发射物容器组件方法
	 *
	 * @param maxCapacity 最大发射物容量
	 * @return 发射物容器组件
	 */
	public static ProjectileContainerComponent of(int maxCapacity) {
		return new ProjectileContainerComponent(maxCapacity);
	}
	
	/**
	 * 获取已装填发射物
	 *
	 * @param stack 装填发射物的物品堆栈
	 * @return 已装填发射物列表
	 */
	public static List<ItemStack> getChargedProjectiles(ItemStack stack) {
		if (!stack.contains(DataComponentTypes.CHARGED_PROJECTILES)) return ImmutableList.of();
		return stack.getOrDefault(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT).getProjectiles();
	}
	
	/**
	 * *随机*取出一个已装填发射物<br>
	 * 因 NBT 技术原因，无法顺序取出放入的发射物，因为放入就不是顺序的
	 *
	 * @param stack 装填发射物的物品堆栈
	 * @return 发射物
	 */
	public static ItemStack popChargedProjectiles(ItemStack stack) {
		if (!stack.contains(DataComponentTypes.CHARGED_PROJECTILES)) return ItemStack.EMPTY;
		List<ItemStack> projectiles = Lists.newCopyOnWriteArrayList(stack.getOrDefault(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT).getProjectiles());
		if (projectiles.isEmpty()) return ItemStack.EMPTY;
		ItemStack popStack = projectiles.removeFirst();
		stack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(projectiles));
		return popStack;
	}
	
	/**
	 * 获取已装填发射物数量
	 *
	 * @param stack 装填发射物的物品堆栈
	 * @return 已装填发射物数量
	 */
	public static int getChargedAmount(ItemStack stack) {
		return getChargedProjectiles(stack).size();
	}
	
	/**
	 * 放入已装填发射物<br>
	 * 如果需装填的发射物数量大于可装填的数量则会舍弃部分发射物
	 *
	 * @param stack       装填发射物的物品堆栈
	 * @param projectiles 需装填发射物堆栈列表
	 */
	public void putChargedProjectiles(ItemStack stack, List<ItemStack> projectiles, LivingEntity shooter) {
		List<ItemStack> projectileList = new ArrayList<>();
		for (int count = 0; count < Math.min(projectiles.size(), getLoadableAmount(stack, shooter)); count++) {
			projectileList.add(projectiles.get(count));
		}
		stack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(projectileList));
	}
	
	/**
	 * 获取能装填发射物数量
	 *
	 * @param stack 装填发射物的物品堆栈
	 * @param user  装填的玩家实体，用于判断是否为创造
	 * @return 能装填发射物数量
	 */
	public int getLoadableAmount(ItemStack stack, LivingEntity user) {
		return (user instanceof PlayerEntity player && !player.isCreative())
				? Math.min(maxCapacity, player.getInventory().count(RangedWeaponComponent.getProjectileType(player, stack).getItem()))
				: maxCapacity - getChargedAmount(stack);
	}
}
