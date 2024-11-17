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

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantedCountIncreaseLootFunction;
import net.minecraft.loot.function.FurnaceSmeltLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import pers.saikel0rado1iu.silk.api.generate.data.LootTableGenUtil;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Test {@link LootTableGenUtil}
 */
public interface LootTableGenUtilTest {
	/**
	 * Block
	 */
	final class Block extends FabricBlockLootTableProvider {
		/**
		 * @param dataOutput     数据输出
		 * @param registryLookup 注册表 Future
		 */
		public Block(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataOutput, registryLookup);
		}
		
		@Override
		public void generate() {
			LootTableGenUtil.addBlockDrop(this::addDrop, Blocks.GRASS_BLOCK, Items.DIRT, Items.SHORT_GRASS);
		}
	}
	
	/**
	 * Entity
	 */
	final class Entity extends SimpleFabricLootTableProvider {
		private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup;
		
		/**
		 * @param output         数据输出
		 * @param registryLookup 注册表 Future
		 */
		public Entity(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(output, registryLookup, LootContextTypes.ENTITY);
			this.registryLookup = registryLookup;
		}
		
		@Override
		public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {
			lootTableBiConsumer.accept(EntityType.PIG.getLootTableId(), LootTable.builder()
					.pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1))
							.with(ItemEntry.builder(Items.BEEF)
									.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0, 2)))
									.apply(FurnaceSmeltLootFunction.builder().conditionally(LootTableGenUtil.createSmeltLootCondition(registryLookup.join())))
									.apply(EnchantedCountIncreaseLootFunction.builder(registryLookup.join(), UniformLootNumberProvider.create(1, 1)))))
					.pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1))
							.with(ItemEntry.builder(Items.ACACIA_BOAT)
									.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(-1, 1)))
									.apply(EnchantedCountIncreaseLootFunction.builder(registryLookup.join(), UniformLootNumberProvider.create(1, 1))))));
		}
	}
}
