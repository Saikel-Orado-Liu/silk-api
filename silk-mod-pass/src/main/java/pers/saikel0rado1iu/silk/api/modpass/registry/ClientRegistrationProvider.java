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
import pers.saikel0rado1iu.silk.api.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.api.modpass.ModBasicData;

/**
 * <h2>客户端注册提供器</h2>
 * 用于显式说明是客户端注册并提供客户端方法
 *
 * @param <T> 注册的数据类
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.0.0
 */
@ClientRegistration(registrar = Class.class, type = Class.class)
public non-sealed interface ClientRegistrationProvider<T> extends RegistrationProvider<T> {
    /**
     * <h2>客户端注册器</h2>
     * 提供 {@link Runnable} 进行注册
     *
     * @param <T> 注册的数据类
     * @author <a href="https://github.com/Saikel-Orado-Liu">
     *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
     *         </a>
     * @since 1.0.0
     */
    non-sealed abstract class Registrar<T> extends RegistrationProvider.Registrar {
        protected final Runnable run;

        protected Registrar(Runnable run) {
            this.run = run;
        }

        /**
         * 进行注册
         *
         * @param t 需要注册的项目
         */
        public void register(T t) {
            run.run();
            RegistrationProvider.loggingRegistration(((ModBasicData) () -> getId(t).getNamespace()),
                    t, getId(t), RegistrationType.CLIENT_ONLY);
        }

        protected abstract Identifier getId(T t);
    }
}
