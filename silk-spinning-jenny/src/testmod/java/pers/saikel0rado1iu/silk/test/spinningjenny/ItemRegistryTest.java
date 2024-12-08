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

package pers.saikel0rado1iu.silk.test.spinningjenny;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import pers.saikel0rado1iu.silk.api.annotation.RegistryNamespace;
import pers.saikel0rado1iu.silk.api.spinningjenny.ItemRegistrationProvider;
import pers.saikel0rado1iu.silk.api.spinningjenny.ItemRegistry;
import pers.saikel0rado1iu.silk.impl.SilkId;
import pers.saikel0rado1iu.silk.impl.SilkSpinningJenny;

/**
 * Test {@link ItemRegistry}
 */
@RegistryNamespace(SilkId.SILK_SPINNING_JENNY)
public interface ItemRegistryTest extends ItemRegistry {
    /**
     * test_item
     */
    Item TEST_ITEM = ItemRegistry
            .registrar(() -> ItemRegistrationProvider
                    .builder(Item::new, "test_item")
                    .setting(new Item.Settings().component(
                            ComponentTypeRegistryTest.TEST_DATA_COMPONENT_TYPE, 99)))
            .group(ItemGroups.BUILDING_BLOCKS)
            .other(item -> SilkSpinningJenny.INSTANCE.logger().info("other"))
            .register();
    /**
     * test_block
     */
    @SuppressWarnings("unused")
    BlockItem TEST_BLOCK = ItemRegistry
            .registrar(() -> ItemRegistrationProvider
                    .builder(BlockRegistryTest.TEST_BLOCK,
                            SilkSpinningJenny.INSTANCE.ofId("test_block")))
            .group(ItemGroups.BUILDING_BLOCKS)
            .register();
}
