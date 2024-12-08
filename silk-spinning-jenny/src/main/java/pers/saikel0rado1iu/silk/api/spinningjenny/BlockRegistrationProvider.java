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

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.api.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.api.annotation.ServerRegistration;
import pers.saikel0rado1iu.silk.api.modpass.registry.ClientRegistrationProvider;
import pers.saikel0rado1iu.silk.api.modpass.registry.MainRegistrationProvider;
import pers.saikel0rado1iu.silk.api.modpass.registry.RegistrationProvider;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <h2>方块注册提供器</h2>
 * 用于整合方块并注册方块以供使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.0.0
 */
@ApiStatus.OverrideOnly
@ServerRegistration(registrar = BlockRegistrationProvider.MainRegistrar.class,
                    type = BlockRegistrationProvider.Builder.class, generics = Block.class)
@ClientRegistration(registrar = BlockRegistrationProvider.ClientRegistrar.class, type = Block.class)
public interface BlockRegistrationProvider
        extends MainRegistrationProvider<Block>, ClientRegistrationProvider<Block> {
    /**
     * 构建方法，用于专门构建方块用于注册
     *
     * @param id 方块唯一标识符
     * @return 方块构建器
     */
    static Builder<Block> builder(Identifier id) {
        return new Builder<>(Block::new, id);
    }

    /**
     * 构建方法，用于专门构建方块用于注册
     *
     * @param id 方块不带命名空间的 ID 字符串
     * @return 方块构建器
     */
    static Builder<Block> builder(String id) {
        // 获取调用当前方法的类名
        final int index = 3;

        return builder(Block::new, Identifier.of(RegistrationProvider.getNamespace(index), id));
    }

    /**
     * 特殊方块构建方法，用于专门构建特殊方块用于注册
     *
     * @param factory 方块工厂方法
     * @param id      方块唯一标识符
     * @param <T>     方块类型
     * @return 方块构建器
     */
    static <T extends Block> Builder<T> builder(Function<AbstractBlock.Settings, T> factory,
                                                Identifier id) {
        return new Builder<>(factory, id);
    }

    /**
     * 特殊方块构建方法，用于专门构建特殊方块用于注册
     *
     * @param factory 方块工厂方法
     * @param id      方块不带命名空间的 ID 字符串
     * @param <T>     方块类型
     * @return 方块构建器
     */
    static <T extends Block> Builder<T> builder(Function<AbstractBlock.Settings, T> factory,
                                                String id) {
        // 获取调用当前方法的类名
        final int index = 3;

        return builder(factory, Identifier.of(RegistrationProvider.getNamespace(index), id));
    }

    /**
     * <h2>方块构建器</h2>
     * 用于专门构建方块用于注册，使用请参考：
     * <p>
     * {@link BlockRegistrationProvider#builder(Identifier)}
     * {@link BlockRegistrationProvider#builder(String)}<br>
     * {@link BlockRegistrationProvider#builder(Function, Identifier)}<br>
     * {@link BlockRegistrationProvider#builder(Function, String)}<br>
     *
     * @param <T> 方块类型
     * @author <a href="https://github.com/Saikel-Orado-Liu">
     *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
     *         </a>
     * @since 1.2.2
     */
    final class Builder<T extends Block> {
        private final Function<AbstractBlock.Settings, T> factory;
        private final Identifier id;
        private AbstractBlock.Settings settings;

        private Builder(Function<AbstractBlock.Settings, T> factory, Identifier id) {
            this.factory = factory;
            this.id = id;
            this.settings = AbstractBlock.Settings.create();
        }

        /**
         * 设置方块设置属性
         *
         * @param settings 方块设置
         * @return 方块构建器
         */
        public Builder<T> setting(AbstractBlock.Settings settings) {
            this.settings = settings;
            return this;
        }

        private T build() {
            RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, id);
            T block = factory.apply(settings.registryKey(key));
            Registry.register(Registries.BLOCK, key, block);

            return block;
        }
    }

    /**
     * <h2>方块主注册器</h2>
     * 请使用 {@link BlockRegistry#registrar(Supplier)} 注册
     *
     * @param <T> 方块类型
     * @author <a href="https://github.com/Saikel-Orado-Liu">
     *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
     *         </a>
     * @since 1.0.0
     */
    final class MainRegistrar<T extends Block>
            extends MainRegistrationProvider.Registrar<Builder<T>, Block, T, MainRegistrar<T>> {
        MainRegistrar(Supplier<Builder<T>> type) {
            super(type);
        }

        /**
         * 注册 {@link Block} 无需提供标识符
         *
         * @return 注册项
         */
        public T register() {
            var reg = reg(null);
            logging(Objects.requireNonNull(registry().getId(reg)), reg);
            return reg;
        }

        /**
         * 注册 {@link Block} 无需提供标识符
         *
         * @return 注册表项
         */
        public RegistryEntry<Block> registerReference() {
            var reg = reg(null);
            var entry = registry().getEntry(reg);
            logging(Objects.requireNonNull(registry().getId(reg)), entry.value());
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
        public RegistryEntry<Block> registerReference(Identifier id) {
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
        public RegistryEntry<Block> registerReference(String id) {
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
            return supplier.build();
        }

        @Override
        protected Registry<Block> registry() {
            return Registries.BLOCK;
        }
    }

    /**
     * <h2>方块客户端注册器</h2>
     * 请使用 {@link BlockRegistry#registrar(Runnable)} 注册
     *
     * @param <T> 方块类型
     * @author <a href="https://github.com/Saikel-Orado-Liu">
     *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
     *         </a>
     * @since 1.0.0
     */
    final class ClientRegistrar<T extends Block> extends ClientRegistrationProvider.Registrar<T> {
        ClientRegistrar(Runnable run) {
            super(run);
        }

        @Override
        protected Identifier getId(T t) {
            return Registries.BLOCK.getId(t);
        }
    }
}
