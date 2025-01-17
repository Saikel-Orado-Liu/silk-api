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

package pers.saikel0rado1iu.silk.mixin.event.modplus;

import com.google.gson.JsonElement;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import pers.saikel0rado1iu.silk.api.event.modplus.ModifyRecipeEvents;

import java.util.HashMap;
import java.util.Map;

/**
 * <h2 style="color:FFC800">{@link ModifyRecipeEvents} 混入</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface ModifyRecipeEventsMixin {
	/**
	 * {@link ModifyRecipeEvents#REMOVE} 混入
	 */
	@Mixin(RecipeManager.class)
	abstract class Remove {
		@ModifyVariable(method = "apply(L java/util/Map;L net/minecraft/resource/ResourceManager;L net/minecraft/util/profiler/Profiler;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
		private Map<Identifier, JsonElement> remove(Map<Identifier, JsonElement> map) {
			Map<Identifier, JsonElement> result = new HashMap<>(map);
			for (Identifier recipeId : map.keySet()) if (ModifyRecipeEvents.REMOVE.invoker().canRemove(recipeId)) result.remove(recipeId);
			return result;
		}
	}
}
