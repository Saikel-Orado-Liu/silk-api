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
import net.minecraft.registry.Registries;

import java.util.Arrays;
import java.util.List;

/**
 * <h2 style="color:FFC800">附魔特质组件</h2>
 * 可以用于完全自定义物品的可附魔魔咒与魔咒兼容性的数据组件<br>
 * 附魔特质组件拥有最高的魔咒属性优先级，其他在标签中声明或硬编码的魔咒都会被替换。
 *
 * @param normals  普通附魔列表
 * @param specials 特殊附魔列表
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public record EnchantmentTraitsComponent(List<Enchantment> normals, List<SpecialEnchantment> specials) {
	public static final Codec<EnchantmentTraitsComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
					Registries.ENCHANTMENT.getCodec().listOf().fieldOf("normals").forGetter(EnchantmentTraitsComponent::normals),
					SpecialEnchantment.CODEC.listOf().optionalFieldOf("specials", List.of()).forGetter(EnchantmentTraitsComponent::specials))
			.apply(builder, EnchantmentTraitsComponent::new));
	public static final PacketCodec<RegistryByteBuf, EnchantmentTraitsComponent> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);
	
	/**
	 * 创建附魔特质组件
	 *
	 * @param enchantments 附魔列表
	 * @return 附魔特质组件
	 */
	public static EnchantmentTraitsComponent of(Enchantment... enchantments) {
		return create(Arrays.stream(enchantments).toList(), List.of());
	}
	
	/**
	 * 创建附魔特质组件
	 *
	 * @param enchantments 附魔列表
	 * @return 附魔特质组件
	 */
	public static EnchantmentTraitsComponent of(SpecialEnchantment... enchantments) {
		return create(List.of(), Arrays.stream(enchantments).toList());
	}
	
	/**
	 * 创建附魔特质组件
	 *
	 * @param normals  普通附魔列表
	 * @param specials 特殊附魔列表
	 * @return 附魔特质组件
	 */
	public static EnchantmentTraitsComponent create(List<Enchantment> normals, List<SpecialEnchantment> specials) {
		return new EnchantmentTraitsComponent(normals, specials);
	}
	
	/**
	 * 特殊魔咒
	 *
	 * @param enchantment 附魔魔咒
	 * @param conflicts   冲突魔咒
	 * @param threshold   冲突阈值，冲突阈值代表了附魔几种冲突魔咒后才无法附魔其他的冲突魔咒。
	 *                    默认为 0，不允许附魔冲突魔咒。
	 */
	public record SpecialEnchantment(Enchantment enchantment, List<Enchantment> conflicts, int threshold) {
		public static final Codec<SpecialEnchantment> CODEC = RecordCodecBuilder.create(builder -> builder.group(
						Registries.ENCHANTMENT.getCodec().fieldOf("enchantment").forGetter(SpecialEnchantment::enchantment),
						Registries.ENCHANTMENT.getCodec().listOf().optionalFieldOf("conflicts", List.of()).forGetter(SpecialEnchantment::conflicts),
						Codec.INT.optionalFieldOf("threshold", 0).forGetter(SpecialEnchantment::threshold))
				.apply(builder, SpecialEnchantment::new));
		
		/**
		 * 创建特殊附魔
		 *
		 * @param enchantment 附魔魔咒
		 * @param conflicts   冲突魔咒
		 * @return 特殊附魔数据
		 */
		public static SpecialEnchantment create(Enchantment enchantment, Enchantment... conflicts) {
			return create(enchantment, Arrays.stream(conflicts).toList(), 0);
		}
		
		/**
		 * 创建特殊附魔
		 *
		 * @param enchantment 附魔魔咒
		 * @param conflicts   冲突魔咒
		 * @param threshold   冲突阈值，冲突阈值代表了附魔几种冲突魔咒后才无法附魔其他的冲突魔咒。
		 *                    默认为 0，不允许附魔冲突魔咒。
		 * @return 特殊附魔数据
		 */
		public static SpecialEnchantment create(Enchantment enchantment, List<Enchantment> conflicts, int threshold) {
			return new SpecialEnchantment(enchantment, conflicts, threshold);
		}
	}
}
