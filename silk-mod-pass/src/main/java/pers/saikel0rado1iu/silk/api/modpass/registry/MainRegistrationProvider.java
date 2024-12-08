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

package pers.saikel0rado1iu.silk.api.modpass.registry;

import com.google.common.base.Suppliers;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.api.annotation.RegistryNamespace;
import pers.saikel0rado1iu.silk.api.annotation.ServerRegistration;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;
import pers.saikel0rado1iu.silk.impl.SilkApi;
import pers.saikel0rado1iu.silk.impl.SilkId;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <h2>主要注册提供器</h2>
 * 用于显式说明是需要同时进行客户端与服务端注册并提供主要方法
 *
 * @param <T> 注册的数据类
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.0.0
 */
@ServerRegistration(registrar = Class.class, type = Class.class, generics = Class.class)
public non-sealed interface MainRegistrationProvider<T> extends RegistrationProvider<T> {
    /**
     * <h2>主要注册器</h2>
     * 提供 {@link Supplier} 进行注册，注册后返回注册项
     *
     * @param <T> 注册所提供的包装对象，如 {@code Builder} 等
     * @param <U> 注册的数据基本类型，用于提供注册表
     * @param <V> 注册的数据类型
     * @param <W> 自身的注册器类型
     * @author <a href="https://github.com/Saikel-Orado-Liu">
     *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
     *         </a>
     * @since 1.0.0
     */
    non-sealed abstract class Registrar<T, U, V extends U, W extends Registrar<T, U, V, W>>
            extends RegistrationProvider.Registrar {
        protected final T supplier;
        private V reg;

        protected Registrar(Supplier<T> supplier) {
            this.supplier = Suppliers.memoize(supplier::get).get();
        }

        /**
         * 使用此方法记录注册注册项的日志
         *
         * @param id  注册项的唯一标识符
         * @param obj 注册项
         */
        protected static void logging(Identifier id, Object obj) {
            ModPass modPass = id.getNamespace().equals(SilkId.SILK_API)
                    ? SilkApi.INTERNAL
                    : ModPass.of(id.getNamespace());
            RegistrationProvider.loggingRegistration(modPass, obj, id, RegistrationType.VANILLA_MAIN);
        }

        /**
         * 其他注册内容
         *
         * @param consumer 注册方法
         * @return 主要注册器
         */
        public W other(Consumer<T> consumer) {
            consumer.accept(supplier);
            return self();
        }

        /**
         * 进行注册
         * <p>
         * 注册标识符为 {@code id}
         *
         * @param id 注册 ID
         * @return 注册表项
         */
        public RegistryEntry<U> registerReference(Identifier id) {
            var entry = Registry.registerReference(registry(), id, reg(id));
            logging(id, entry.value());
            return entry;
        }

        /**
         * 进行注册
         * <p>
         * 注册标识符为 {@link RegistryNamespace#value()} : {@code id}
         *
         * @param id 注册 ID
         * @return 注册表项
         */
        public RegistryEntry<U> registerReference(String id) {
            Identifier identifier = Identifier.of(namespace(), id);
            var entry = Registry.registerReference(registry(), identifier, reg(identifier));
            logging(identifier, entry.value());
            return entry;
        }

        /**
         * 进行注册
         * <p>
         * 注册标识符为 {@code id}
         *
         * @param id 注册 ID
         * @return 注册项
         */
        public V register(Identifier id) {
            var reg = Registry.register(registry(), id, reg(id));
            logging(id, reg);
            return reg;
        }

        /**
         * 进行注册
         * <p>
         * 注册标识符为 {@link RegistryNamespace#value()} : {@code id}
         *
         * @param id 注册 ID
         * @return 注册项
         */
        public V register(String id) {
            Identifier identifier = Identifier.of(namespace(), id);
            var entry = Registry.register(registry(), identifier, reg(identifier));
            logging(identifier, entry);
            return entry;
        }

        /**
         * 获取注册项属性
         *
         * @param id 注册标识符
         * @return 注册项
         */
        protected final V reg(@Nullable Identifier id) {
            if (reg == null) {
                reg = getReg(id);
            }
            return reg;
        }

        protected abstract W self();

        /**
         * 获取注册项
         * <p>
         * 获取注册项属性应使用 {@link Registrar#reg(Identifier)} 而不是此方法
         *
         * @param id 注册标识符
         * @return 注册项
         */
        @ApiStatus.OverrideOnly
        protected abstract V getReg(@Nullable Identifier id);

        /**
         * 注册表属性
         *
         * @return 注册表
         */
        protected abstract Registry<U> registry();
    }
}
