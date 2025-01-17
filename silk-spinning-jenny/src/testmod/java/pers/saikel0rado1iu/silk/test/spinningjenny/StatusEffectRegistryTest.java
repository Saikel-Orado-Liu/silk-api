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

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import pers.saikel0rado1iu.silk.api.annotation.RegistryNamespace;
import pers.saikel0rado1iu.silk.api.spinningjenny.StatusEffectRegistry;
import pers.saikel0rado1iu.silk.impl.SilkApi;
import pers.saikel0rado1iu.silk.impl.SilkId;

/**
 * Test {@link StatusEffectRegistry}
 */
@RegistryNamespace(SilkId.SILK_SPINNING_JENNY)
public interface StatusEffectRegistryTest extends StatusEffectRegistry {
    /**
     * test_status_effect
     */
    @SuppressWarnings("unused")
    RegistryEntry<StatusEffect> TEST_STATUS_EFFECT = StatusEffectRegistry
            .registrar(TestStatusEffect::new)
            .registerReference(SilkApi.INTERNAL.ofId("test_status_effect"));

    /**
     * TestStatusEffect
     */
    class TestStatusEffect extends StatusEffect {
        private TestStatusEffect() {
            super(StatusEffectCategory.HARMFUL, 0xFF6A00);
        }

        @Override
        public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
            super.applyUpdateEffect(world, entity, amplifier);
            return entity.damage(world, entity.getDamageSources().create(DamageTypes.ARROW), 1);
        }

        @Override
        public boolean canApplyUpdateEffect(int duration, int amplifier) {
            int mod = 0x19 >> amplifier;
            if (mod > 0) {
                return duration % mod == 0;
            }
            return true;
        }
    }
}
