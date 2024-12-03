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
import pers.saikel0rado1iu.silk.api.annotation.ServerRegistration;
import pers.saikel0rado1iu.silk.api.modpass.ModMain;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;
import pers.saikel0rado1iu.silk.impl.SilkModPass;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
@ServerRegistration(registrar = Class.class, type = Class.class)
public interface MainRegistrationProvider<T> extends RegisterableModPass<T> {
    /**
     * 记录注册事件
     *
     * @param modPass          发起注册事件的模组的模组通
     * @param clazz            {@link RegisterableModPass} 的类参数
     * @param registrationType 注册类型
     */
    static void loggingRegistration(ModPass modPass, Class<?> clazz,
                                    RegistrationType registrationType) {
        // 处理当前接口
        boolean foundTargetType = RegisterableModPass.processInterface(modPass, clazz, registrationType);
        // 处理父接口
        if (foundTargetType) {
            return;
        }
        Type[] interfaces = clazz.getGenericInterfaces();
        for (Type type : interfaces) {
            if (!(type instanceof Class<?> classType && classType.isInterface())) {
                continue;
            }
            for (Field field : clazz.getDeclaredFields()) {
                loggingRegistration(modPass, field);
            }
            loggingRegistration(modPass, classType, registrationType);
        }
    }

    /**
     * 记录注册事件
     *
     * @param modPass 发起注册事件的模组的模组通
     * @param field   当前处理的字段
     */
    private static void loggingRegistration(ModPass modPass, Field field) {
        try {
            Object obj = field.get(new Object());
            Registrar.RegistryData data = Registrar.THREAD_LOCAL_TAGS.get().get(obj);
            Identifier id;
            if (data == null) {
                return;
            } else if (data.registry.isEmpty()) {
                return;
            } else if (data.id.isEmpty()) {
                id = modPass.modData().ofId(field.getName().toLowerCase());
            } else if (data.id.contains(":")) {
                id = Identifier.tryParse(data.id);
            } else {
                id = modPass.modData().ofId(data.id);
            }
            //noinspection unchecked
            Registry.register((Registry<? super Object>) data.registry.get(), id, obj);
            RegisterableModPass.loggingRegistration(modPass, obj, id, RegistrationType.VANILLA_MAIN);
            Registrar.THREAD_LOCAL_TAGS.get().remove(obj);
            if (Registrar.THREAD_LOCAL_TAGS.get().isEmpty()) {
                Registrar.THREAD_LOCAL_TAGS.remove();
            }
        } catch (IllegalAccessException e) {
            String msg = String.format(
                    "Register field '%s' with illegal access modifier.",
                    field.getName());
            SilkModPass.getInstance().logger().error(msg);
            throw new RuntimeException(msg);
        }
    }

    /**
     * 主要注册器
     * <p>
     * 提供注册项进行注册，注册后返回注册项
     *
     * @param <T> 注册的数据类
     */
    abstract class Registrar<T, R extends Registrar<T, R>> {
        private static final ThreadLocal<Map<Object, RegistryData>> THREAD_LOCAL_TAGS =
                ThreadLocal.withInitial(HashMap::new);
        protected final T type;

        protected Registrar(Supplier<T> type) {
            this.type = Suppliers.memoize(type::get).get();
        }

        protected abstract R self();

        /**
         * 获取注册表
         *
         * @return 注册表，如果需要特殊方法注册进行则返回 {@link Optional#empty()}，但需要自己另外实现注册
         */
        protected abstract Optional<Registry<?>> registry();

        /**
         * 其他注册内容
         *
         * @param consumer 注册方法
         * @return 主要注册器
         */
        public R other(Consumer<T> consumer) {
            consumer.accept(type);
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
        @SuppressWarnings({"rawtypes", "unchecked"})
        public RegistryEntry<T> registerReference(Identifier id) {
            return registry()
                    .map(registry -> (RegistryEntry) Registry
                            .registerReference((Registry<? super Object>) registry, id, type))
                    .orElseGet(() -> RegistryEntry.of(register(id)));
        }

        /**
         * 进行注册
         * <p>
         * 注册标识符为 {@code id}
         *
         * @param id 注册 ID
         * @return 注册项
         */
        public T register(Identifier id) {
            return register(id.toString());
        }

        /**
         * 进行注册
         * <p>
         * 注册标识符为 {@link  ModMain#modData()}{@code .id():id}
         *
         * @param id 注册 ID
         * @return 注册项
         */
        public T register(String id) {
            THREAD_LOCAL_TAGS.get().put(type, new RegistryData(registry(), id));
            return type;
        }

        /**
         * 进行注册
         * <p>
         * 注册标识符为 {@link  ModMain#modData()}{@code .id():<field_name>}
         * <p>
         * 不推荐使用此方法，使用此方法不利于模组的可扩展性与兼容性，请改用 {@link Registrar#register(String)}
         *
         * @return 注册项
         */
        @Deprecated
        @ApiStatus.Obsolete
        public T register() {
            return register("");
        }

        private record RegistryData(Optional<Registry<?>> registry, String id) {
        }
    }
}
