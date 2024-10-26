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
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * <h2 style="color:FFC800">自带状态效果组件</h2>
 * 用于说明自带状态效果的物品的状态效果组件
 *
 * @param effect              物品的状态效果
 * @param baseLevel           附魔效果的基础等级，如果 &lt; 1 则按 1 算
 * @param maxLevel            状态效果的最大等级，如果 &lt; 1 则按 1 算
 * @param stackingLevel       状态效果的叠加等级，&lt; 0 则递减，== 0 则不变
 * @param statusEffectKit     状态效果套装，如果图表为空 {@link Set#of()} 则说明没有套装效果。
 * @param kitTriggerThreshold 套装触发阈值，如果 &lt; 1 则按 1 算，如果为 2 则需要 2 个套装中的物品才能激活效果。
 *                            如果 {@code statusEffectKit} 为 {@link Set#of()}，则为需要多少个自己
 * @param effectiveItemSlot   有效物品槽
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public record InherentStatusEffectComponent(RegistryEntry<StatusEffect> effect,
                                            int baseLevel,
                                            int maxLevel,
                                            float stackingLevel,
                                            Supplier<List<Item>> statusEffectKit,
                                            int kitTriggerThreshold,
                                            EffectiveItemSlotComponent effectiveItemSlot) {
	public static final Codec<InherentStatusEffectComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
					Registries.STATUS_EFFECT.getEntryCodec().fieldOf("effect").forGetter(InherentStatusEffectComponent::effect),
					Codec.INT.fieldOf("base_level").forGetter(InherentStatusEffectComponent::baseLevel),
					Codec.INT.fieldOf("max_level").forGetter(InherentStatusEffectComponent::maxLevel),
					Codec.FLOAT.fieldOf("stacking_level").forGetter(InherentStatusEffectComponent::stackingLevel),
					Registries.ITEM.getCodec().listOf().fieldOf("status_effect_kit").forGetter(component -> component.statusEffectKit.get()),
					Codec.INT.fieldOf("kit_trigger_threshold").forGetter(InherentStatusEffectComponent::kitTriggerThreshold),
					EffectiveItemSlotComponent.CODEC.fieldOf("effective_item_slot").forGetter(InherentStatusEffectComponent::effectiveItemSlot))
			.apply(builder, (effect, baseLevel, maxLevel, stackingLevel, statusEffectKit, kitTriggerThreshold, effectiveItemSlot) ->
					new InherentStatusEffectComponent(effect, baseLevel, maxLevel, stackingLevel, () -> statusEffectKit, kitTriggerThreshold, effectiveItemSlot)));
	public static final PacketCodec<RegistryByteBuf, InherentStatusEffectComponent> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);
}
