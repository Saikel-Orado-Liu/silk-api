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

package pers.saikel0rado1iu.silk.api.spinningjenny;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.api.annotation.ServerRegistration;
import pers.saikel0rado1iu.silk.api.modpass.registry.MainRegistrationProvider;

import java.util.function.Supplier;

/**
 * <h2>声音事件注册提供器</h2>
 * 用于整合声音事件并注册声音事件以供使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.0.0
 */
@ApiStatus.OverrideOnly
@ServerRegistration(registrar = SoundEventRegistrationProvider.MainRegistrar.class,
                    type = SoundEvent.class, generics = SoundEvent.class)
public interface SoundEventRegistrationProvider extends MainRegistrationProvider<SoundEvent> {
    /**
     * <h2>声音事件主注册器</h2>
     * 请使用 {@link SoundEventRegistry#registrar(Supplier)} 注册
     *
     * @param <T> 声音事件
     * @author <a href="https://github.com/Saikel-Orado-Liu">
     *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
     *         </a>
     * @since 1.0.0
     */
    final class MainRegistrar<T extends SoundEvent>
            extends Registrar<T, SoundEvent, T, MainRegistrar<T>> {
        MainRegistrar(Supplier<T> type) {
            super(type);
        }

        /**
         * 注册 {@link SoundEvent} 无需提供标识符
         *
         * @return 注册项
         */
        public T register() {
            var reg = reg(null);
            var r = Registry.register(registry(), reg.id(), reg);
            logging(reg.id(), r);
            return r;
        }

        /**
         * 注册 {@link SoundEvent} 无需提供标识符
         *
         * @return 注册表项
         */
        public RegistryEntry<SoundEvent> registerReference() {
            var reg = reg(null);
            var entry = Registry.registerReference(registry(), reg.id(), reg);
            logging(reg.id(), entry.value());
            return entry;
        }

        /**
         * 请改用 {@link MainRegistrar#registerReference()}
         *
         * @param id 注册 ID
         * @return 注册表项
         */
        @Deprecated
        @Override
        public RegistryEntry<SoundEvent> registerReference(Identifier id) {
            return registerReference();
        }

        /**
         * 请改用 {@link MainRegistrar#registerReference()}
         *
         * @param id 注册 ID
         * @return 注册表项
         */
        @Deprecated
        @Override
        public RegistryEntry<SoundEvent> registerReference(String id) {
            return registerReference();
        }

        /**
         * 请改用 {@link MainRegistrar#register()}
         *
         * @param id 注册 ID
         * @return 注册项
         */
        @Deprecated
        @Override
        public T register(Identifier id) {
            return register();
        }

        /**
         * 请改用 {@link MainRegistrar#register()}
         *
         * @param id 注册 ID
         * @return 注册项
         */
        @Deprecated
        @Override
        public T register(String id) {
            return register();
        }

        @Override
        protected MainRegistrar<T> self() {
            return this;
        }

        @Override
        protected T getReg(@Nullable Identifier id) {
            return supplier;
        }

        @Override
        protected Registry<SoundEvent> registry() {
            return Registries.SOUND_EVENT;
        }
    }
}
