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

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import pers.saikel0rado1iu.silk.api.annotation.RegistryNamespace;
import pers.saikel0rado1iu.silk.api.spinningjenny.SoundEventRegistry;
import pers.saikel0rado1iu.silk.impl.SilkId;
import pers.saikel0rado1iu.silk.impl.SilkSpinningJenny;

/**
 * Test {@link SoundEventRegistry}
 */
@RegistryNamespace(SilkId.SILK_SPINNING_JENNY)
public interface SoundEventRegistryTest extends SoundEventRegistry {
    /**
     * test_sound_event
     */
    @SuppressWarnings("unused")
    SoundEvent TEST_SOUND_EVENT = SoundEventRegistry
            .registrar(() -> SoundEvent.of(SilkSpinningJenny.INSTANCE.ofId("test")))
            .register();
    /**
     * test_sound_event_entry
     */
    @SuppressWarnings("unused")
    RegistryEntry<SoundEvent> TEST_SOUND_EVENT_ENTRY = SoundEventRegistry
            .registrar(() -> SoundEvent.of(SilkSpinningJenny.INSTANCE.ofId("test_entry")))
            .registerReference();
}
