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

import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.*;
import net.minecraft.util.math.ColorHelper;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.EnchantmentTraitsComponent;
import pers.saikel0rado1iu.silk.api.spinningjenny.ItemRegistry;
import pers.saikel0rado1iu.silk.impl.SilkRopeStick;

import java.util.List;

import static net.minecraft.component.DataComponentTypes.DYED_COLOR;
import static pers.saikel0rado1iu.silk.api.ropestick.component.DataComponentTypes.ENCHANTMENT_TRAITS;

/**
 * 物品
 */
@SuppressWarnings("unused")
public interface Items extends ItemRegistry {
	/**
	 * test_item
	 */
	BreakingShieldTest TEST_ITEM = ItemRegistry.registrar(() -> new BreakingShieldTest(new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance().ofId("test_item"));
	/**
	 * test_bolt_action_firearm
	 */
	BoltActionFirearmItemTest TEST_BOLT_ACTION_FIREARM = ItemRegistry.registrar(() -> new BoltActionFirearmItemTest(new Item.Settings().maxDamage(100)
					.component(ENCHANTMENT_TRAITS, EnchantmentTraitsComponent.of(Enchantments.AQUA_AFFINITY))))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance().ofId("test_bolt_action_firearm"));
	/**
	 * test_bolt_action_repeating_firearm
	 */
	BoltActionRepeatingFirearmItemTest TEST_BOLT_ACTION_REPEATING_FIREARM = ItemRegistry.registrar(() -> new BoltActionRepeatingFirearmItemTest(new Item.Settings().maxDamage(100)
					.component(ENCHANTMENT_TRAITS, EnchantmentTraitsComponent.create(List.of(Enchantments.INFINITY), List.of(EnchantmentTraitsComponent.SpecialEnchantment.create(Enchantments.POWER))))))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance().ofId("test_bolt_action_repeating_firearm"));
	/**
	 * test_semi_automatic_firearm
	 */
	SemiAutomaticFirearmItemTest TEST_SEMI_AUTOMATIC_FIREARM = ItemRegistry.registrar(() -> new SemiAutomaticFirearmItemTest(new Item.Settings().maxDamage(100)
					.component(ENCHANTMENT_TRAITS, EnchantmentTraitsComponent.of(
							EnchantmentTraitsComponent.SpecialEnchantment.create(Enchantments.POWER, List.of(Enchantments.UNBREAKING, Enchantments.MENDING), 1),
							EnchantmentTraitsComponent.SpecialEnchantment.create(Enchantments.UNBREAKING, List.of(Enchantments.POWER, Enchantments.MENDING), 1),
							EnchantmentTraitsComponent.SpecialEnchantment.create(Enchantments.MENDING, List.of(Enchantments.UNBREAKING, Enchantments.POWER), 1)))))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance().ofId("test_semi_automatic_firearm"));
	/**
	 * test_fully_automatic_firearm
	 */
	FullyAutomaticFirearmItemTest TEST_FULLY_AUTOMATIC_FIREARM = ItemRegistry.registrar(() -> new FullyAutomaticFirearmItemTest(new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance().ofId("test_fully_automatic_firearm"));
	/**
	 * test_bow
	 */
	BowLikeItemTest TEST_BOW = ItemRegistry.registrar(() -> new BowLikeItemTest(new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance().ofId("test_bow"));
	/**
	 * test_crossbow
	 */
	CrossbowLikeItemTest TEST_CROSSBOW = ItemRegistry.registrar(() -> new CrossbowLikeItemTest(new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance().ofId("test_crossbow"));
	/**
	 * test_shovel
	 */
	ShovelItem TEST_SHOVEL = ItemRegistry.registrar(() -> ToolHelperTest.MATERIAL.createShovel(4, ToolHelperTest.createToolSettings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance().ofId("test_shovel"));
	/**
	 * test_pickaxe
	 */
	PickaxeItem TEST_PICKAXE = ItemRegistry.registrar(() -> ToolHelperTest.MATERIAL.createPickaxe(4, ToolHelperTest.createToolSettings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance().ofId("test_pickaxe"));
	/**
	 * test_axe
	 */
	AxeItem TEST_AXE = ItemRegistry.registrar(() -> ToolHelperTest.MATERIAL.createAxe(12, 1.2F, ToolHelperTest.createToolSettings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance().ofId("test_axe"));
	/**
	 * test_hoe
	 */
	HoeItem TEST_HOE = ItemRegistry.registrar(() -> ToolHelperTest.MATERIAL.createHoe(4, ToolHelperTest.createToolSettings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance().ofId("test_hoe"));
	/**
	 * test_sword
	 */
	SwordItem TEST_SWORD = ItemRegistry.registrar(() -> ToolHelperTest.MATERIAL.createSword(8, ToolHelperTest.createToolSettings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance().ofId("test_sword"));
	/**
	 * test_helmet
	 */
	ArmorItem TEST_HELMET = ItemRegistry.registrar(() -> ArmorHelperTest.MATERIAL.createHelmet(ArmorHelperTest.createArmorSettings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance().ofId("test_helmet"));
	/**
	 * test_chestplate
	 */
	ArmorItem TEST_CHESTPLATE = ItemRegistry.registrar(() -> ArmorHelperTest.MATERIAL.createChestplate(ArmorHelperTest.createArmorSettings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance().ofId("test_chestplate"));
	/**
	 * test_leggings
	 */
	ArmorItem TEST_LEGGINGS = ItemRegistry.registrar(() -> ArmorHelperTest.MATERIAL.createLeggings(ArmorHelperTest.createArmorSettings().component(DYED_COLOR, new DyedColorComponent(ColorHelper.Argb.fullAlpha(0xFFFFB3), true))))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance().ofId("test_leggings"));
	/**
	 * test_boots
	 */
	ArmorItem TEST_BOOTS = ItemRegistry.registrar(() -> ArmorHelperTest.MATERIAL.createBoots(ArmorHelperTest.createArmorSettings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance().ofId("test_boots"));
}
