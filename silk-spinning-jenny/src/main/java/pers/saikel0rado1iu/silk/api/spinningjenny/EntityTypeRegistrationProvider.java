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

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.api.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.api.annotation.ServerRegistration;
import pers.saikel0rado1iu.silk.api.modpass.registry.ClientRegistrationProvider;
import pers.saikel0rado1iu.silk.api.modpass.registry.MainRegistrationProvider;
import pers.saikel0rado1iu.silk.impl.SilkSpinningJenny;

import java.util.function.Supplier;

/**
 * <h2>实体类型注册提供器</h2>
 * 用于整合实体类型并注册实体类型以供使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.0.0
 */
@ApiStatus.OverrideOnly
@ServerRegistration(registrar = EntityTypeRegistrationProvider.MainRegistrar.class,
                    type = EntityType.Builder.class, generics = Entity.class)
@ClientRegistration(registrar = EntityTypeRegistrationProvider.ClientRegistrar.class,
                    type = EntityType.class)
public interface EntityTypeRegistrationProvider
        extends MainRegistrationProvider<EntityType<?>>, ClientRegistrationProvider<EntityType<?>> {
    /**
     * <h2>实体类型主注册器</h2>
     * 请使用 {@link EntityTypeRegistry#registrar(Supplier)} 注册
     *
     * @param <T> 实体
     * @author <a href="https://github.com/Saikel-Orado-Liu">
     *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
     *         </a>
     * @since 1.0.0
     */
    final class MainRegistrar<T extends Entity>
            extends MainRegistrationProvider.Registrar<EntityType.Builder<T>, EntityType<?>,
            EntityType<T>, MainRegistrar<T>> {
        MainRegistrar(Supplier<EntityType.Builder<T>> type) {
            super(type);
        }

        @Override
        protected MainRegistrar<T> self() {
            return this;
        }

        @Override
        protected EntityType<T> getReg(@Nullable Identifier id) {
            if (id == null) {
                String msg = "Special Error: " +
                        "The identifier of the registered EntityType object is null.";
                SilkSpinningJenny.INSTANCE.logger().debug(msg);
                throw new NullPointerException(msg);
            }
            return supplier.build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, id));
        }

        @Override
        protected Registry<EntityType<?>> registry() {
            return Registries.ENTITY_TYPE;
        }
    }

    /**
     * <h2>实体类型客户端注册器</h2>
     * 请使用 {@link EntityTypeRegistry#registrar(Runnable)} 注册
     *
     * @param <T> 实体类型
     * @author <a href="https://github.com/Saikel-Orado-Liu">
     *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
     *         </a>
     * @since 1.0.0
     */
    final class ClientRegistrar<T extends EntityType<?>>
            extends ClientRegistrationProvider.Registrar<T> {
        ClientRegistrar(Runnable run) {
            super(run);
        }

        @Override
        protected Identifier getId(T t) {
            return Registries.ENTITY_TYPE.getId(t);
        }
    }
}
