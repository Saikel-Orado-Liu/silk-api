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

package pers.saikel0rado1iu.silk.test.generate.data;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import pers.saikel0rado1iu.silk.api.base.common.util.TickUtil;
import pers.saikel0rado1iu.silk.api.generate.data.RecipeGenUtil;
import pers.saikel0rado1iu.silk.api.generate.data.family.EquipFamily;
import pers.saikel0rado1iu.silk.api.generate.data.server.recipe.ShapedRecipeWithComponentsJsonBuilder;
import pers.saikel0rado1iu.silk.api.generate.data.server.recipe.ShapelessRecipeWithComponentsJsonBuilder;
import pers.saikel0rado1iu.silk.impl.SilkGenerate;

import java.util.concurrent.CompletableFuture;

/**
 * Test {@link RecipeGenUtil}
 */
public final class RecipeGenUtilTest extends FabricRecipeProvider {
	/**
	 * @param output           数据输出
	 * @param registriesFuture 注册表 Future
	 */
	public RecipeGenUtilTest(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}
	
	@Override
	public void generate(RecipeExporter exporter) {
		RecipeGenUtil.generateFamily(exporter, Ingredient.ofItems(Items.DIAMOND), EquipFamily.builder()
				.shovel(Items.DIAMOND_SHOVEL).pickaxe(Items.DIAMOND_PICKAXE).axe(Items.DIAMOND_AXE).hoe(Items.DIAMOND_HOE).sword(Items.DIAMOND_SWORD)
				.helmet(Items.DIAMOND_HELMET).chestplate(Items.DIAMOND_CHESTPLATE).leggings(Items.DIAMOND_LEGGINGS).boots(Items.DIAMOND_BOOTS).build());
		RecipeGenUtil.offer2x2CompactingRecipeWithRecipeGroup(exporter, RecipeCategory.MISC, Items.DIAMOND, Items.DIAMOND_BLOCK);
		RecipeGenUtil.offer2x2CrossCompactingRecipe(exporter, RecipeCategory.MISC, Ingredient.ofItems(Items.DIAMOND), Ingredient.ofItems(Items.GOLD_INGOT), Items.NETHER_BRICK);
		RecipeGenUtil.offerCrossCompactingRecipe(exporter, RecipeCategory.MISC, Ingredient.ofItems(Items.DIAMOND), Ingredient.ofItems(Items.GOLD_INGOT), Items.NETHERITE_BLOCK);
		RecipeGenUtil.offerReversibleSmithingRecipe(exporter, RecipeCategory.MISC, Ingredient.ofItems(Items.DIAMOND), Ingredient.ofItems(Items.GOLD_INGOT), Items.NETHER_BRICK);
		RecipeGenUtil.offerSmelting(exporter, ImmutableSet.of(Items.GOLDEN_SHOVEL, Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_SWORD, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS),
				Items.GOLD_NUGGET, 0.15F, TickUtil.getTick(20), getItemPath(Items.GOLD_NUGGET));
		RecipeGenUtil.offerBlasting(exporter, ImmutableSet.of(Items.GOLDEN_SHOVEL, Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_SWORD, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS),
				Items.GOLD_NUGGET, 0.15F, TickUtil.getTick(10), getItemPath(Items.GOLD_NUGGET));
		ItemStack stack = PotionContentsComponent.createStack(Items.TIPPED_ARROW, Potions.STRONG_POISON);
		stack.setCount(4);
		ShapedRecipeWithComponentsJsonBuilder.create(RecipeCategory.COMBAT, stack).group(getItemPath(Items.TIPPED_ARROW)).input('#', Items.STRING).input('X', Items.STICK).input('@', Items.FEATHER)
				.pattern("#")
				.pattern("X")
				.pattern("@")
				.criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
				.criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
				.criterion(hasItem(Items.FEATHER), conditionsFromItem(Items.FEATHER))
				.offerTo(exporter, SilkGenerate.getInstance().ofId("poison_tipped_arrow_with_spider_fang"));
		ShapelessRecipeWithComponentsJsonBuilder.create(RecipeCategory.COMBAT, PotionContentsComponent.createStack(Items.TIPPED_ARROW, Potions.STRONG_HEALING)).group(getItemPath(Items.TIPPED_ARROW)).input(Items.SADDLE).input(Items.STICK).input(Items.FEATHER)
				.criterion(hasItem(Items.SADDLE), conditionsFromItem(Items.SADDLE))
				.criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
				.criterion(hasItem(Items.FEATHER), conditionsFromItem(Items.FEATHER))
				.offerTo(exporter, SilkGenerate.getInstance().ofId("healing_tipped_arrow"));
	}
}
