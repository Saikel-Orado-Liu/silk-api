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

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.api.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.api.annotation.ServerRegistration;
import pers.saikel0rado1iu.silk.api.modpass.registry.ClientRegistrationProvider;
import pers.saikel0rado1iu.silk.api.modpass.registry.MainRegistrationProvider;

import java.util.function.Supplier;

/**
 * <h2>粒子类型注册提供器</h2>
 * 用于整合粒子类型并注册粒子类型以供使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.0.0
 */
@ApiStatus.OverrideOnly
@ServerRegistration(registrar = ParticleTypeRegistrationProvider.MainRegistrar.class,
                    type = ParticleType.class, generics = ParticleEffect.class)
@ClientRegistration(registrar = ParticleTypeRegistrationProvider.ClientRegistrar.class,
                    type = ParticleType.class)
public interface ParticleTypeRegistrationProvider
        extends MainRegistrationProvider<ParticleType<?>>,
                ClientRegistrationProvider<ParticleType<?>> {
    /**
     * <h2>粒子类型主注册器</h2>
     * 请使用 {@link ParticleTypeRegistry#registrar(Supplier)} 注册
     *
     * @param <T> 粒子类型
     * @author <a href="https://github.com/Saikel-Orado-Liu">
     *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
     *         </a>
     * @since 1.0.0
     */
    final class MainRegistrar<T extends ParticleEffect>
            extends MainRegistrationProvider.Registrar<ParticleType<T>, ParticleType<?>,
            ParticleType<T>, MainRegistrar<T>> {
        MainRegistrar(Supplier<ParticleType<T>> type) {
            super(type);
        }

        @Override
        protected MainRegistrar<T> self() {
            return this;
        }

        @Override
        protected ParticleType<T> getReg(@Nullable Identifier id) {
            return supplier;
        }

        @Override
        protected Registry<ParticleType<?>> registry() {
            return Registries.PARTICLE_TYPE;
        }
    }

    /**
     * <h2>粒子类型客户端注册器</h2>
     * 请使用 {@link ParticleTypeRegistry#registrar(Runnable)} 注册
     *
     * @param <T> 粒子类型
     * @author <a href="https://github.com/Saikel-Orado-Liu">
     *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
     *         </a>
     * @since 1.0.0
     */
    final class ClientRegistrar<T extends ParticleType<?>>
            extends ClientRegistrationProvider.Registrar<T> {
        ClientRegistrar(Runnable run) {
            super(run);
        }

        @Override
        protected Identifier getId(T t) {
            return Registries.PARTICLE_TYPE.getId(t);
        }
    }
}
