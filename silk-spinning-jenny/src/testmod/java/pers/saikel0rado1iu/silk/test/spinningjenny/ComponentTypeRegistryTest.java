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

import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.dynamic.Codecs;
import pers.saikel0rado1iu.silk.api.spinningjenny.ComponentTypeRegistry;

/**
 * Test {@link ComponentType}
 */
public interface ComponentTypeRegistryTest extends ComponentTypeRegistry {
	/**
	 * test_data_component_type
	 */
	ComponentType<Integer> TEST_DATA_COMPONENT_TYPE = ComponentTypeRegistry.registrar(() ->
			ComponentType.<Integer>builder().codec(Codecs.rangedInt(1, 99)).packetCodec(PacketCodecs.VAR_INT).build()).register("test_data_component_type");
}
