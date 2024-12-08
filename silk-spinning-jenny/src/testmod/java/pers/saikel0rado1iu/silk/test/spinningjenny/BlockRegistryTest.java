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

import net.minecraft.block.Block;
import pers.saikel0rado1iu.silk.api.annotation.RegistryNamespace;
import pers.saikel0rado1iu.silk.api.spinningjenny.BlockRegistrationProvider;
import pers.saikel0rado1iu.silk.api.spinningjenny.BlockRegistry;
import pers.saikel0rado1iu.silk.impl.SilkId;

/**
 * Test {@link BlockRegistry}
 */
@RegistryNamespace(SilkId.SILK_SPINNING_JENNY)
public interface BlockRegistryTest extends BlockRegistry {
    /**
     * test_block
     */
    Block TEST_BLOCK = BlockRegistry
            .registrar(() -> BlockRegistrationProvider.builder("test_block"))
            .register();
}
