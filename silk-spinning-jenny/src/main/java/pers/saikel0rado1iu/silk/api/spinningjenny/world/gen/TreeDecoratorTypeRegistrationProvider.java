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

package pers.saikel0rado1iu.silk.api.spinningjenny.world.gen;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.api.annotation.ServerRegistration;
import pers.saikel0rado1iu.silk.api.modpass.registry.MainRegistrationProvider;

import java.util.function.Supplier;

/**
 * <h2>树木装饰器类型注册提供器</h2>
 * 用于整合树木装饰器类型并注册树木装饰器类型以供使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.0.0
 */
@ApiStatus.OverrideOnly
@ServerRegistration(registrar = TreeDecoratorTypeRegistrationProvider.MainRegistrar.class,
                    type = TreeDecoratorType.class, generics = TreeDecorator.class)
public interface TreeDecoratorTypeRegistrationProvider
        extends MainRegistrationProvider<TreeDecoratorType<?>> {
    /**
     * <h2>树木装饰器类型主注册器</h2>
     * 请使用 {@link TreeDecoratorTypeRegistry#registrar(Supplier)} 注册
     *
     * @param <T> 树木装饰器
     * @author <a href="https://github.com/Saikel-Orado-Liu">
     *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
     *         </a>
     * @since 1.0.0
     */
    final class MainRegistrar<T extends TreeDecorator>
            extends Registrar<TreeDecoratorType<T>, TreeDecoratorType<?>,
            TreeDecoratorType<T>, MainRegistrar<T>> {
        MainRegistrar(Supplier<TreeDecoratorType<T>> type) {
            super(type);
        }

        @Override
        protected MainRegistrar<T> self() {
            return this;
        }

        @Override
        protected TreeDecoratorType<T> getReg(@Nullable Identifier id) {
            return supplier;
        }

        @Override
        protected Registry<TreeDecoratorType<?>> registry() {
            return Registries.TREE_DECORATOR_TYPE;
        }
    }
}
