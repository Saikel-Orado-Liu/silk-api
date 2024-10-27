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
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import pers.saikel0rado1iu.silk.api.ropestick.armor.ArmorHelper;
import pers.saikel0rado1iu.silk.api.ropestick.component.DataComponentTypes;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.CustomEntityHurtComponent;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.EffectiveItemSlotData;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.InherentStatusEffectData;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.InherentStatusEffectsComponent;
import pers.saikel0rado1iu.silk.impl.SilkRopeStick;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Test {@link ArmorHelper}
 */
public enum ArmorHelperTest implements ArmorHelper {
	MATERIAL("test", Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 2);
		map.put(ArmorItem.Type.LEGGINGS, 5);
		map.put(ArmorItem.Type.CHESTPLATE, 6);
		map.put(ArmorItem.Type.HELMET, 2);
		map.put(ArmorItem.Type.BODY, 5);
	}), 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 5, 2, () -> Ingredient.ofItems(Items.TEST_ITEM));
	
	private final String name;
	private final Map<ArmorItem.Type, Integer> defense;
	private final int enchantability;
	private final RegistryEntry<SoundEvent> equipSound;
	private final float toughness;
	private final float knockbackResistance;
	private final Supplier<Ingredient> ingredient;
	private final Supplier<RegistryEntry<ArmorMaterial>> material;
	
	ArmorHelperTest(String name, Map<ArmorItem.Type, Integer> defense, int enchantability, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> ingredient) {
		this.name = name;
		this.defense = defense;
		this.enchantability = enchantability;
		this.equipSound = equipSound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.ingredient = Suppliers.memoize(ingredient::get);
		this.material = Suppliers.memoize(() -> ArmorHelper.registerMaterial(this));
	}
	
	public static Item.Settings createArmorSettings() {
		return new Item.Settings()
				.component(DataComponentTypes.INHERENT_STATUS_EFFECTS, InherentStatusEffectsComponent.of(
						InherentStatusEffectData.create(
								StatusEffects.LUCK,
								0,
								5,
								1,
								() -> ImmutableList.of(Items.TEST_HELMET, Items.TEST_CHESTPLATE, Items.TEST_LEGGINGS, Items.TEST_BOOTS),
								0,
								EffectiveItemSlotData.ARMOR),
						InherentStatusEffectData.create(
								StatusEffects.UNLUCK,
								1,
								2,
								0.5F,
								() -> ImmutableList.of(Items.TEST_HELMET, Items.TEST_CHESTPLATE, Items.TEST_LEGGINGS, Items.TEST_BOOTS),
								2,
								EffectiveItemSlotData.ALL)))
				.component(DataComponentTypes.CUSTOM_ENTITY_HURT, new CustomEntityHurtComponent(
						ImmutableList.of(DamageTypes.MOB_ATTACK),
						"amount * 3"));
	}
	
	@Override
	public Identifier id() {
		return SilkRopeStick.getInstance().ofId(name);
	}
	
	@Override
	public Map<ArmorItem.Type, Integer> defense() {
		return defense;
	}
	
	@Override
	public int enchantability() {
		return enchantability;
	}
	
	@Override
	public RegistryEntry<SoundEvent> equipSound() {
		return equipSound;
	}
	
	@Override
	public Supplier<Ingredient> repairIngredient() {
		return ingredient;
	}
	
	@Override
	public float toughness() {
		return toughness;
	}
	
	@Override
	public float knockbackResistance() {
		return knockbackResistance;
	}
	
	@Override
	public Supplier<RegistryEntry<ArmorMaterial>> material() {
		return material;
	}
}
