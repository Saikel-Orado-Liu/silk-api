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

package pers.saikel0rado1iu.silk.gen.data;

import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.util.function.BiConsumer;

/**
 * <h2 style="color:FFC800">用于提供模组中常用但未提供更方便方法的战利品表方法</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 */
@SilkApi
public interface SilkLootTableGenerator {
	@SilkApi
	EntityPredicate.Builder NEEDS_ENTITY_ON_FIRE = EntityPredicate.Builder.create().flags(EntityFlagsPredicate.Builder.create().onFire(true));
	
	@SilkApi
	static void addBlockDrop(BiConsumer<Block, LootTable.Builder> addDrop, Block block, ItemConvertible... drops) {
		LootTable.Builder lootTableBuilder = LootTable.builder();
		for (ItemConvertible drop : drops) {
			lootTableBuilder = lootTableBuilder.pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1))
					.with(ItemEntry.builder(drop).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1)))));
		}
		addDrop.accept(block, lootTableBuilder);
	}
}
