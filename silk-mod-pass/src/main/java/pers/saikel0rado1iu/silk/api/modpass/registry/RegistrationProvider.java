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

import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.api.annotation.RegistryNamespace;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;
import pers.saikel0rado1iu.silk.impl.Minecraft;
import pers.saikel0rado1iu.silk.impl.SilkModPass;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <h2>注册提供器</h2>
 * 相较于 {@link ModPass} 添加了相关方法以供注册任务使用，用于专门创建特殊注册表
 *
 * @param <T> 注册的数据类
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.2.2
 */
public sealed interface RegistrationProvider<T> extends ModPass
        permits MainRegistrationProvider, ServerRegistrationProvider, ClientRegistrationProvider,
                LaunchRegistrationProvider {
    /**
     * 记录注册事件
     *
     * @param modPass          发起注册事件的模组的模组通
     * @param clazz            {@link RegistrationProvider} 的类参数
     * @param registrationType 注册类型
     */
    static void loggingRegistration(ModPass modPass, Class<? extends RegistrationProvider<?>> clazz,
                                    RegistrationType registrationType) {
        // 处理当前接口
        boolean foundTargetType = processInterface(modPass, clazz, registrationType);
        // 处理父接口
        if (foundTargetType) {
            return;
        }
        Type[] interfaces = clazz.getGenericInterfaces();
        for (Type type : interfaces) {
            if (!(type instanceof Class<?> classType && classType.isInterface())) {
                continue;
            }
            if (!RegistrationProvider.class.isAssignableFrom(classType)) {
                continue;
            }
            try {
                Class.forName(clazz.getName(), true, clazz.getClassLoader());
            } catch (ClassNotFoundException e) {
                String msg = String.format(
                        "The class '%s' provided for registration was not found.",
                        clazz.getName());
                SilkModPass.INSTANCE.logger().error(msg);
                throw new RuntimeException(e);
            }
            for (Field field : clazz.getDeclaredFields()) {
                String msg = String.format(
                        "Register field '%s' with illegal access modifier.",
                        field.getName());
                try {
                    Object ignored = field.get(new Object());
                } catch (IllegalAccessException e) {
                    SilkModPass.INSTANCE.logger().error(msg);
                    throw new RuntimeException(msg, e);
                }
            }
            //noinspection unchecked
            var c = (Class<? extends RegistrationProvider<?>>) classType;
            loggingRegistration(modPass, c, registrationType);
        }
    }

    /**
     * 记录注册事件
     *
     * @param modPass          发起注册事件的模组的模组通
     * @param object           注册数据
     * @param id               数据的标识符
     * @param registrationType 注册类型
     */
    static void loggingRegistration(ModPass modPass, Object object, Identifier id,
                                    RegistrationType registrationType) {
        String name = object.getClass().getSimpleName().isEmpty()
                ? object.getClass().getName()
                : object.getClass().getSimpleName();
        SilkModPass.INSTANCE.logger().debug(
                "Register {} ({}): {} '{}' from {} has been successfully registered.",
                name, registrationType.key(), name, id, modPass.modData().debugName());
    }

    /**
     * 处理接口方法
     *
     * @param modPass          发起注册事件的模组的模组通
     * @param clazz            {@link RegistrationProvider} 的类参数
     * @param registrationType 注册类型
     * @return 是否找到目标类型
     */
    static boolean processInterface(ModPass modPass, Class<? extends RegistrationProvider<?>> clazz,
                                    RegistrationType registrationType) {
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        for (Type type : genericInterfaces) {
            if (!(type instanceof ParameterizedType parameterizedType)) {
                continue;
            }
            if (processParameterizedType(modPass, parameterizedType, registrationType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取注册项的命名空间
     *
     * @param callerMethodIndex 调用方法索引
     * @return 命名空间
     */
    static String getNamespace(int callerMethodIndex) {
        // 获取调用当前方法的类名
        final String callerClassName = Thread
                .currentThread().getStackTrace()[callerMethodIndex].getClassName();

        try {
            Class<?> callerClass = Class.forName(callerClassName);
            RegistryNamespace anno = callerClass.getAnnotation(RegistryNamespace.class);
            return anno.value();
        } catch (ClassNotFoundException e) {
            String msg = String.format(
                    "Special Error: Unable to find class \"%s\" in the code environment.",
                    callerClassName);
            SilkModPass.INSTANCE.logger().error(msg, e);
        }

        return Minecraft.ID;
    }

    private static boolean processParameterizedType(ModPass modPass,
                                                    ParameterizedType parameterizedType,
                                                    RegistrationType registrationType) {
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        for (Type argument : typeArguments) {
            // 找到目标类型参数，输出日志并返回 true
            if (!(argument instanceof Class<?> argumentClass)) {
                continue;
            }
            registrationLog(modPass, argumentClass, registrationType);
            return true;
        }
        // 没有找到目标类型参数，返回 false
        return false;
    }

    private static void registrationLog(ModPass modPass,
                                        Class<?> type,
                                        RegistrationType registrationType) {
        SilkModPass.INSTANCE.logger().debug(
                "Register {} ({}): All {}(s) in {} has been successfully registered.",
                type.getSimpleName(), registrationType.key(),
                type.getSimpleName().toLowerCase(), modPass.modData().debugName());
    }

    /**
     * @return T
     */
    T unused();

    /**
     * <h2>注册器</h2>
     * 注册提供器的注册器
     *
     * @author <a href="https://github.com/Saikel-Orado-Liu">
     *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
     *         </a>
     * @since 1.2.2
     */
    sealed abstract class Registrar
            permits MainRegistrationProvider.Registrar, ServerRegistrationProvider.Registrar,
                    ClientRegistrationProvider.Registrar, LaunchRegistrationProvider.Registrar {
        String namespace() {
            final int index = 4;

            return getNamespace(index);
        }
    }
}
