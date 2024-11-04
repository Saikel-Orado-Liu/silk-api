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
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemConvertible;

import java.util.List;

/**
 * <h2 style="color:FFC800">有效物品槽数据</h2>
 * 用于有特殊属性的物品能够生效的物品槽
 *
 * @param slots 有效的物品槽集合，如果列表为空 {@link List#isEmpty()}，则意味着能在任意物品栏内生效
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public record EffectiveItemSlotData(List<EquipmentSlot> slots) {
	/**
	 * 盔甲物品槽
	 */
	public final static EffectiveItemSlotData ARMOR = EffectiveItemSlotData.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
	/**
	 * 手部物品槽
	 */
	public final static EffectiveItemSlotData HAND = EffectiveItemSlotData.of(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	/**
	 * 所有物品槽
	 */
	public final static EffectiveItemSlotData ALL = EffectiveItemSlotData.of();
	public static final Codec<EffectiveItemSlotData> CODEC = RecordCodecBuilder.create(builder -> builder.group(
					EquipmentSlot.CODEC.listOf().fieldOf("slots").forGetter(EffectiveItemSlotData::slots))
			.apply(builder, EffectiveItemSlotData::new));
	
	/**
	 * 有效物品槽数据创建方法
	 *
	 * @param slots 装备槽列表
	 * @return 有效物品槽数据
	 */
	public static EffectiveItemSlotData of(EquipmentSlot... slots) {
		return new EffectiveItemSlotData(ImmutableList.copyOf(slots));
	}
	
	/**
	 * 判断是否有效
	 *
	 * @param entity 判断实体
	 * @param item   判断物品
	 * @return 是否有效
	 */
	public boolean isEffective(LivingEntity entity, ItemConvertible item) {
		if (slots.isEmpty()) return true;
		return slots.stream().anyMatch(slot -> entity.getEquippedStack(slot).isOf(item.asItem()));
	}
}
