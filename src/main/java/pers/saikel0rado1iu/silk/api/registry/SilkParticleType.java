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

package pers.saikel0rado1iu.silk.api.registry;

import com.google.common.collect.Sets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.api.ModMain;

import java.util.Set;
import java.util.function.Consumer;

/**
 * <p><b style="color:FFC800"><font size="+1">用于模组所有粒子组成粒子集与粒子注册</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link ModMain} 中覆盖 {@link ModMain#particleTypes()}方法</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkParticleType {
	public static final Set<ParticleType<?>> ALL_MOD_PARTICLE_TYPES = Sets.newLinkedHashSetWithExpectedSize(8);
	
	protected static <P extends ParticleType<?>> Builder<P> builder(P particle) {
		return new Builder<>(particle);
	}
	
	/**
	 * 你需要在 client 模块中重新创建继承 {@link SilkParticleType} 类来创建一个专用于客户端的注册类并覆盖 ModClient.particles()方法
	 */
	@SilkApi
	@Environment(EnvType.CLIENT)
	protected static <P extends ParticleType<?>> void clientRegister(P particle, Consumer<P> clientRegister) {
		clientRegister.accept(particle);
	}
	
	@SilkApi
	public static final class Builder<P extends ParticleType<?>> {
		private final P particle;
		
		@SilkApi
		private Builder(P particle) {
			ALL_MOD_PARTICLE_TYPES.add(this.particle = particle);
		}
		
		@SilkApi
		public Builder<P> put(Set<ParticleType<?>> particles) {
			particles.add(particle);
			return this;
		}
		
		@SilkApi
		public Builder<P> otherRegister(Consumer<P> particleRegister) {
			particleRegister.accept(particle);
			return this;
		}
		
		@SilkApi
		public P build(ModBasicData mod, String id) {
			Registry.register(Registries.PARTICLE_TYPE, new Identifier(mod.getId(), id), particle);
			return particle;
		}
	}
}