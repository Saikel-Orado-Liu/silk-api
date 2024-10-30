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

package pers.saikel0rado1iu.silk.api.generate.data.server.recipe;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.Objects;

/**
 * <h2 style="color:FFC800">带数据组件的无序配方 JSON 构建器</h2>
 * 将 {@link ShapelessRecipeJsonBuilder} 中的 {@code output} 属性改为 {@link ItemStack}
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.2.2
 */
public class ShapelessRecipeWithComponentsJsonBuilder extends ShapelessRecipeJsonBuilder {
	private final ItemStack result;
	
	protected ShapelessRecipeWithComponentsJsonBuilder(RecipeCategory category, ItemStack result) {
		super(category, result.getItem(), result.getCount());
		this.result = result;
	}
	
	/**
	 * 创建方法
	 *
	 * @param category 配方类型
	 * @param result   输出结果
	 * @return 带数据组件的无序配方 JSON 构建器
	 */
	public static ShapelessRecipeWithComponentsJsonBuilder create(RecipeCategory category, ItemStack result) {
		return new ShapelessRecipeWithComponentsJsonBuilder(category, result);
	}
	
	@Override
	public void offerTo(RecipeExporter exporter, Identifier recipeId) {
		validate(recipeId);
		Advancement.Builder builder = exporter.getAdvancementBuilder().criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
		Objects.requireNonNull(builder);
		advancementBuilder.forEach(builder::criterion);
		ShapelessRecipe shapelessRecipe = new ShapelessRecipe(Objects.requireNonNullElse(group, ""), CraftingRecipeJsonBuilder.toCraftingCategory(category), result, inputs);
		exporter.accept(recipeId, shapelessRecipe, builder.build(recipeId.withPrefixedPath("recipes/" + category.getName() + "/")));
	}
}
