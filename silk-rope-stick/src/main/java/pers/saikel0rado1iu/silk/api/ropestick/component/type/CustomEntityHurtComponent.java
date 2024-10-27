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
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <h2 style="color:FFC800">自定义实体受伤组件</h2>
 * 用于需要自定义盔甲物品所阻挡的伤害<br>
 * 注！此属性仅能用于在装备栏中的物品，否则不会生效<br>
 * <br>
 * 参数 {@code  expression} 可设置值示例：
 * <pre>{@code
 * "6"
 * "6.5"
 * "1 + 2 * 3 / 4"
 * "1 + 2 * 3 / 4.0"
 * "amount * 0.5"
 * "(count * 0.2 > amount) ? 0 : amount - count * 0.2"
 * }</pre>
 *
 * @param damageTypes 可以被应用的伤害类型
 * @param expression  获得修改方法的数学表达式;<br>
 *                    目前此数学表达式提供两个变量 {@code count}(堆栈大小) 和 {@code amount}(原始伤害);<br>
 *                    如果表达式的值小于 0 则返回 0，可以参考上述代码赋予表达式<br>
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public record CustomEntityHurtComponent(List<RegistryKey<DamageType>> damageTypes, String expression) {
	public static final Logger LOGGER = LoggerFactory.getLogger(CustomEntityHurtComponent.class);
	public static final Codec<CustomEntityHurtComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
					RegistryKey.createCodec(RegistryKeys.DAMAGE_TYPE).listOf().fieldOf("damage_types").forGetter(CustomEntityHurtComponent::damageTypes),
					Codec.STRING.fieldOf("expression").forGetter(CustomEntityHurtComponent::expression))
			.apply(builder, CustomEntityHurtComponent::new));
	public static final PacketCodec<RegistryByteBuf, CustomEntityHurtComponent> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);
	
	public float evaluateExpression(ItemStack stack, float amount) {
		try {
			Expression exp = new ExpressionBuilder(expression)
					.variables("count", "amount")
					.build()
					.setVariable("count", stack.getCount())
					.setVariable("amount", amount);
			return (float) exp.evaluate();
		} catch (Exception e) {
			String msg = "Expression parsing error: Unable to parse the expression \" " + expression + " \". Please reset to a correct expression.";
			LOGGER.warn(msg, e);
			return amount;
		}
	}
}
