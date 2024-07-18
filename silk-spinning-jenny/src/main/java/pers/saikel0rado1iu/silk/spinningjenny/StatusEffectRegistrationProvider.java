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

package pers.saikel0rado1iu.silk.spinningjenny;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import pers.saikel0rado1iu.silk.annotation.ServerRegistration;
import pers.saikel0rado1iu.silk.modpass.ModPass;
import pers.saikel0rado1iu.silk.modpass.registry.MainRegistrationProvider;

/**
 * <h2 style="color:FFC800">状态效果注册提供器</h2>
 * 用于整合状态效果并注册状态效果以供使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@ServerRegistration(registrar = StatusEffectRegistrationProvider.SERVER_REGISTRAR, type = StatusEffectRegistrationProvider.TYPE)
interface StatusEffectRegistrationProvider extends MainRegistrationProvider<StatusEffect> {
	String SERVER_REGISTRAR = "pers.saikel0rado1iu.silk.spinningjenny.StatusEffectRegistrationProvider.MainRegistrar";
	String TYPE = "net.minecraft.entity.effect.StatusEffect";
	
	/**
	 * 状态效果主注册器
	 *
	 * @param <T> 状态效果
	 */
	final class MainRegistrar<T extends StatusEffect> extends Registrar<T, MainRegistrar<T>> {
		MainRegistrar(T type) {
			super(type);
		}
		
		@Override
		protected MainRegistrar<T> self() {
			return this;
		}
		
		public T register(ModPass modPass, String id) {
			Registry.register(Registries.STATUS_EFFECT, modPass.modData().ofId(id), type);
			return super.register(modPass, id);
		}
	}
}