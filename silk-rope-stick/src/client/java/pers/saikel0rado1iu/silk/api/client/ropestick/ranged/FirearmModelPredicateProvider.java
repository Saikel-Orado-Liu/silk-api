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

package pers.saikel0rado1iu.silk.api.client.ropestick.ranged;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.CrossbowItem;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.RangedWeaponComponent;
import pers.saikel0rado1iu.silk.api.ropestick.ranged.CrossbowLikeItem;

import static pers.saikel0rado1iu.silk.api.ropestick.component.DataComponentTypes.RANGED_WEAPON;

/**
 * <h2 style="color:FFC800">枪械模型谓词提供器</h2>
 * 用于枪械的模型谓词提供器
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface FirearmModelPredicateProvider {
	/**
	 * 注册模型谓词
	 *
	 * @param firearm 枪械
	 * @param <T>     枪械类型
	 */
	static <T extends CrossbowLikeItem> void register(T firearm) {
		ModelPredicateProviderRegistry.register(firearm, new Identifier(RangedWeaponComponent.PULLING_KEY), (stack, world, entity, seed) -> {
			if (entity == null) return 0;
			return entity.isUsingItem() && entity.getActiveItem() == stack ? 1 : 0;
		});
		ModelPredicateProviderRegistry.register(firearm, new Identifier(RangedWeaponComponent.PULL_KEY), (stack, world, entity, seed) -> {
			if (entity == null) return 0;
			return entity.getActiveItem() != stack ? 0 : ((CrossbowLikeItem) stack.getItem()).getUsingProgress(stack.getMaxUseTime() - entity.getItemUseTimeLeft() - 1, stack);
		});
		ModelPredicateProviderRegistry.register(firearm, new Identifier(RangedWeaponComponent.CHARGED_KEY), (stack, world, entity, seed) -> {
			if (entity == null) return 0;
			return CrossbowItem.isCharged(stack) ? 1 : 0;
		});
		ModelPredicateProviderRegistry.register(firearm, new Identifier(RangedWeaponComponent.PROJECTILE_INDEX_KEY), (stack, world, entity, seed) -> {
			if (entity == null) return 0;
			return stack.getOrDefault(RANGED_WEAPON, RangedWeaponComponent.CROSSBOW).getProjectileIndex(entity, stack);
		});
		ShootProjectilesModelPredicateProvider.register(firearm);
	}
}
