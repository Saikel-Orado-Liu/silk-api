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

package pers.saikel0rado1iu.silk.api.modpass;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import pers.saikel0rado1iu.silk.api.annotation.RegistryNamespace;
import pers.saikel0rado1iu.silk.api.modpass.registry.LaunchRegistrationProvider;
import pers.saikel0rado1iu.silk.api.modpass.registry.RegistrationProvider;
import pers.saikel0rado1iu.silk.api.modpass.registry.RegistrationType;
import pers.saikel0rado1iu.silk.impl.Minecraft;

/**
 * <h2>模组启动主类</h2>
 * 继承自 {@link PreLaunchEntrypoint}。用于在模组启动时就执行某些操作，部分注册任务会要求在启动时注册。
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 0.1.0
 */
public non-sealed interface ModLaunch extends PreLaunchEntrypoint, ModEntry<LaunchRegistrationProvider<?>> {
    @Override
    default void onPreLaunch() {
        if (isExecuted()) {
            return;
        }
        ENTRYPOINT_EXECUTED.put(getClass(), true);
        main(this);
        for (Class<? extends RegistrationProvider<?>> clazz : registries()) {
            RegistryNamespace anno = clazz.getAnnotation(RegistryNamespace.class);
            LaunchRegistrationProvider.loggingRegistration(
                    ModPass.of(anno == null ? Minecraft.ID : anno.value()),
                    clazz, RegistrationType.PRE_LAUNCH);
        }
    }
}
