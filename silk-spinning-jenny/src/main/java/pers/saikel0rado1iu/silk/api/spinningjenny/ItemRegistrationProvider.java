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

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
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

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <h2>物品注册提供器</h2>
 * 用于整合物品并注册物品以供使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.0.0
 */
@ApiStatus.OverrideOnly
@ServerRegistration(registrar = ItemRegistrationProvider.MainRegistrar.class,
                    type = ItemRegistrationProvider.Builder.class, generics = Item.class)
@ClientRegistration(registrar = ItemRegistrationProvider.ClientRegistrar.class, type = Item.class)
public interface ItemRegistrationProvider
        extends MainRegistrationProvider<Item>, ClientRegistrationProvider<Item> {
    /**
     * 构建方法，用于专门构建物品用于注册
     *
     * @param id 物品唯一标识符
     * @return 物品构建器
     */
    static Builder<Item> builder(Identifier id) {
        return new Builder<>(Item::new, id);
    }

    /**
     * 构建方法，用于专门构建物品用于注册
     *
     * @param id 物品不带命名空间的 ID 字符串
     * @return 物品构建器
     */
    static Builder<Item> builder(String id) {
        // 获取调用当前方法的类名
        final int index = 3;

        return builder(Item::new, Identifier.of(RegistrationProvider.getNamespace(index), id));
    }

    /**
     * 方块物品构建方法，用于专门构建方块物品用于注册
     *
     * @param block 物品所指向的方块
     * @param id    物品唯一标识符
     * @return 物品构建器
     */
    static Builder<BlockItem> builder(Block block, Identifier id) {
        return new Builder<>(settings -> new BlockItem(block, settings), id);
    }

    /**
     * 方块物品构建方法，用于专门构建方块物品用于注册
     *
     * @param block 物品所指向的方块
     * @param id    物品不带命名空间的 ID 字符串
     * @return 物品构建器
     */
    static Builder<BlockItem> builder(Block block, String id) {
        // 获取调用当前方法的类名
        final int index = 3;

        return builder(settings -> new BlockItem(block, settings),
                Identifier.of(RegistrationProvider.getNamespace(index), id));
    }

    /**
     * 特殊物品构建方法，用于专门构建特殊物品用于注册
     *
     * @param factory 物品工厂方法
     * @param id      物品唯一标识符
     * @param <T>     物品类型
     * @return 物品构建器
     */
    static <T extends Item> Builder<T> builder(Function<Item.Settings, T> factory, Identifier id) {
        return new Builder<>(factory, id);
    }

    /**
     * 特殊物品构建方法，用于专门构建特殊物品用于注册
     *
     * @param factory 物品工厂方法
     * @param id      物品不带命名空间的 ID 字符串
     * @param <T>     物品类型
     * @return 物品构建器
     */
    static <T extends Item> Builder<T> builder(Function<Item.Settings, T> factory, String id) {
        // 获取调用当前方法的类名
        final int index = 3;

        return builder(factory, Identifier.of(RegistrationProvider.getNamespace(index), id));
    }

    /**
     * <h2>物品构建器</h2>
     * 用于专门构建物品用于注册，使用请参考：
     * <p>
     * {@link ItemRegistrationProvider#builder(Identifier)}<br>
     * {@link ItemRegistrationProvider#builder(String)}<br>
     * {@link ItemRegistrationProvider#builder(Block, Identifier)}<br>
     * {@link ItemRegistrationProvider#builder(Block, String)}<br>
     * {@link ItemRegistrationProvider#builder(Function, Identifier)}<br>
     * {@link ItemRegistrationProvider#builder(Function, String)}
     *
     * @param <T> 物品类型
     * @author <a href="https://github.com/Saikel-Orado-Liu">
     *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
     *         </a>
     * @since 1.2.2
     */
    final class Builder<T extends Item> {
        private final Function<Item.Settings, T> factory;
        private final Identifier id;
        private Item.Settings settings;

        private Builder(Function<Item.Settings, T> factory, Identifier id) {
            this.factory = factory;
            this.id = id;
            this.settings = new Item.Settings();
        }

        /**
         * 设置物品设置属性
         *
         * @param settings 物品设置
         * @return 物品构建器
         */
        public Builder<T> setting(Item.Settings settings) {
            this.settings = settings;
            return this;
        }

        private T build() {
            RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
            T item = factory.apply(settings.registryKey(key));
            if (item instanceof BlockItem blockItem) {
                blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
            }
            Registry.register(Registries.ITEM, key, item);

            return item;
        }
    }

    /**
     * <h2>物品主注册器</h2>
     * 请使用 {@link ItemRegistry#registrar(Supplier)} 注册
     *
     * @param <T> 物品类型
     * @author <a href="https://github.com/Saikel-Orado-Liu">
     *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
     *         </a>
     * @since 1.0.0
     */
    final class MainRegistrar<T extends Item>
            extends MainRegistrationProvider.Registrar<Builder<T>, Item, T, MainRegistrar<T>> {
        MainRegistrar(Supplier<Builder<T>> type) {
            super(type);
        }

        /**
         * 注册物品组
         *
         * @param groups 物品组
         * @return 注册器
         */
        @SafeVarargs
        public final MainRegistrar<T> group(RegistryKey<ItemGroup>... groups) {
            Arrays.stream(groups)
                  .forEach(group -> ItemGroupEvents
                          .modifyEntriesEvent(group)
                          .register(content -> content.add(reg(null))));
            return this;
        }

        /**
         * 注册物品组
         * <p>
         * 将此物品按照特定的顺序放置在物品组中
         *
         * @param group       物品组
         * @param addConsumer 添加方法
         * @return 注册器
         */
        public MainRegistrar<T> group(RegistryKey<ItemGroup> group,
                                      BiConsumer<FabricItemGroupEntries, Item> addConsumer) {
            ItemGroupEvents.modifyEntriesEvent(group)
                           .register(content -> addConsumer.accept(content, reg(null)));
            return this;
        }

        /**
         * 注册物品组
         * <p>
         * 将此物品按照特定的顺序放置在每一个物品组中
         *
         * @param groupMap 物品组图表
         * @return 注册器
         */
        public MainRegistrar<T> group(
                Map<RegistryKey<ItemGroup>, BiConsumer<FabricItemGroupEntries, Item>> groupMap) {
            for (Map.Entry<RegistryKey<ItemGroup>,
                    BiConsumer<FabricItemGroupEntries, Item>> entry : groupMap.entrySet()) {
                ItemGroupEvents.modifyEntriesEvent(entry.getKey())
                               .register(content -> entry.getValue().accept(content, reg(null)));
            }
            return this;
        }

        /**
         * 注册 {@link Item} 无需提供标识符
         *
         * @return 注册项
         */
        public T register() {
            var reg = reg(null);
            logging(Objects.requireNonNull(registry().getId(reg)), reg);
            return reg;
        }

        /**
         * 注册 {@link Item} 无需提供标识符
         *
         * @return 注册表项
         */
        public RegistryEntry<Item> registerReference() {
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
        public RegistryEntry<Item> registerReference(Identifier id) {
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
        public RegistryEntry<Item> registerReference(String id) {
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
        protected Registry<Item> registry() {
            return Registries.ITEM;
        }
    }

    /**
     * <h2>物品客户端注册器</h2>
     * 请使用 {@link ItemRegistry#registrar(Runnable)} 注册
     *
     * @param <T> 物品类型
     * @author <a href="https://github.com/Saikel-Orado-Liu">
     *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
     *         </a>
     * @since 1.0.0
     */
    final class ClientRegistrar<T extends Item> extends ClientRegistrationProvider.Registrar<T> {
        ClientRegistrar(Runnable run) {
            super(run);
        }

        @Override
        protected Identifier getId(T t) {
            return Registries.ITEM.getId(t);
        }
    }
}
