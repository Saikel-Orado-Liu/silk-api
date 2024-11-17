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
import net.minecraft.enchantment.Enchantment;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.Arrays;
import java.util.List;

/**
 * <h2 style="color:FFC800">附魔特质组件</h2>
 * 可以用于完全自定义物品的可附魔魔咒与魔咒兼容性的数据组件<br>
 * 附魔特质组件拥有最高的魔咒属性优先级，其他在标签中声明或硬编码的魔咒都会被替换。
 *
 * @param enchantments 魔咒特质列表
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public record EnchantmentTraitsComponent(List<EnchantmentTrait> enchantments) {
	public static final Codec<EnchantmentTraitsComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
					EnchantmentTrait.CODEC.listOf().optionalFieldOf("enchantments", List.of()).forGetter(EnchantmentTraitsComponent::enchantments))
			.apply(builder, EnchantmentTraitsComponent::new));
	public static final PacketCodec<RegistryByteBuf, EnchantmentTraitsComponent> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);
	
	/**
	 * 创建附魔特质组件
	 *
	 * @param enchantments 附魔列表
	 * @return 附魔特质组件
	 */
	@SafeVarargs
	public static EnchantmentTraitsComponent of(RegistryKey<Enchantment>... enchantments) {
		return new EnchantmentTraitsComponent(Arrays.stream(enchantments).map(EnchantmentTrait::create).toList());
	}
	
	/**
	 * 创建附魔特质组件
	 *
	 * @param enchantments 附魔列表
	 * @return 附魔特质组件
	 */
	public static EnchantmentTraitsComponent of(EnchantmentTrait... enchantments) {
		return new EnchantmentTraitsComponent(Arrays.stream(enchantments).toList());
	}
	
	/**
	 * 魔咒特质
	 *
	 * @param enchantment 附魔魔咒
	 * @param conflicts   冲突魔咒
	 * @param threshold   冲突阈值，冲突阈值代表了附魔几种冲突魔咒后才无法附魔其他的冲突魔咒。
	 *                    默认为 0，不允许附魔冲突魔咒。
	 */
	public record EnchantmentTrait(RegistryKey<Enchantment> enchantment, List<RegistryKey<Enchantment>> conflicts, int threshold) {
		public static final Codec<EnchantmentTrait> CODEC = RecordCodecBuilder.create(builder -> builder.group(
						RegistryKey.createCodec(RegistryKeys.ENCHANTMENT).fieldOf("enchantment").forGetter(EnchantmentTrait::enchantment),
						RegistryKey.createCodec(RegistryKeys.ENCHANTMENT).listOf().optionalFieldOf("conflicts", List.of()).forGetter(EnchantmentTrait::conflicts),
						Codec.INT.optionalFieldOf("threshold", 0).forGetter(EnchantmentTrait::threshold))
				.apply(builder, EnchantmentTrait::new));
		
		/**
		 * 创建魔咒特质
		 *
		 * @param enchantment 附魔魔咒
		 * @param conflicts   冲突魔咒
		 * @return 特殊附魔数据
		 */
		@SafeVarargs
		public static EnchantmentTrait create(RegistryKey<Enchantment> enchantment, RegistryKey<Enchantment>... conflicts) {
			return create(enchantment, Arrays.stream(conflicts).toList(), 0);
		}
		
		/**
		 * 创建魔咒特质
		 *
		 * @param enchantment 附魔魔咒
		 * @param conflicts   冲突魔咒
		 * @param threshold   冲突阈值，冲突阈值代表了附魔几种冲突魔咒后才无法附魔其他的冲突魔咒。
		 *                    默认为 0，不允许附魔冲突魔咒。
		 * @return 特殊附魔数据
		 */
		public static EnchantmentTrait create(RegistryKey<Enchantment> enchantment, List<RegistryKey<Enchantment>> conflicts, int threshold) {
			return new EnchantmentTrait(enchantment, conflicts, threshold);
		}
	}
}
