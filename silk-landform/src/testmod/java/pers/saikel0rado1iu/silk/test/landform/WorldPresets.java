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

package pers.saikel0rado1iu.silk.test.landform;

import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.WorldPreset;
import pers.saikel0rado1iu.silk.generate.world.WorldPresetEntry;

import static pers.saikel0rado1iu.silk.test.landform.Launch.MOD_PASS;

/**
 * WorldPresets
 */
public interface WorldPresets extends WorldPresetEntry {
	/**
	 * INSTANCE
	 */
	WorldPresets INSTANCE = new WorldPresets() {
	};
	/**
	 * test
	 */
	RegistryKey<WorldPreset> TEST = WorldPresetEntry.of(MOD_PASS, "test");
	
	@Override
	default RegistryBuilder.BootstrapFunction<WorldPreset> bootstrap() {
		return registerable -> TestChunkGenerator.register(TEST, registerable);
	}
}
