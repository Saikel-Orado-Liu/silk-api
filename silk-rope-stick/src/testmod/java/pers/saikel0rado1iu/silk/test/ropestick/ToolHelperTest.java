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

package pers.saikel0rado1iu.silk.test.ropestick;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import pers.saikel0rado1iu.silk.api.ropestick.component.ComponentTypes;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.EffectiveItemSlotData;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.InherentStatusEffectData;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.InherentStatusEffectsComponent;
import pers.saikel0rado1iu.silk.api.ropestick.tool.ToolHelper;

import java.util.function.Supplier;

/**
 * Test {@link ToolHelper}
 */
public enum ToolHelperTest implements ToolHelper {
	MATERIAL(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 15, 15, 2, 15, () -> Ingredient.ofItems(Items.TEST_ITEM));
	
	private final TagKey<Block> inverseTag;
	private final int durability;
	private final float miningSpeed;
	private final float materialDamage;
	private final int enchantability;
	private final Supplier<Ingredient> repairIngredient;
	
	ToolHelperTest(final TagKey<Block> inverseTag, final int durability, final float miningSpeed, final float materialDamage, final int enchantability, final Supplier<Ingredient> repairIngredient) {
		this.inverseTag = inverseTag;
		this.durability = durability;
		this.miningSpeed = miningSpeed;
		this.materialDamage = materialDamage;
		this.enchantability = enchantability;
		this.repairIngredient = Suppliers.memoize(repairIngredient::get);
	}
	
	public static Item.Settings createToolSettings() {
		return new Item.Settings().component(ComponentTypes.INHERENT_STATUS_EFFECTS,
				InherentStatusEffectsComponent.of(
						InherentStatusEffectData.create(
								StatusEffects.HEALTH_BOOST,
								10,
								5,
								1,
								() -> ImmutableList.of(Items.TEST_SHOVEL, Items.TEST_PICKAXE, Items.TEST_AXE, Items.TEST_HOE, Items.TEST_SWORD),
								1,
								EffectiveItemSlotData.HAND)
				));
	}
	
	@Override
	public float getMaterialDamage() {
		return materialDamage;
	}
	
	@Override
	public int getDurability() {
		return durability;
	}
	
	@Override
	public float getMiningSpeedMultiplier() {
		return miningSpeed;
	}
	
	@Override
	public TagKey<Block> getInverseTag() {
		return inverseTag;
	}
	
	@Override
	public int getEnchantability() {
		return enchantability;
	}
	
	@Override
	public Ingredient getRepairIngredient() {
		return repairIngredient.get();
	}
}
