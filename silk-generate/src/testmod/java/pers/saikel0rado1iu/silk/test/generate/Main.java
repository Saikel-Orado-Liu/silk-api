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

package pers.saikel0rado1iu.silk.test.generate;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import pers.saikel0rado1iu.silk.impl.SilkApi;
import pers.saikel0rado1iu.silk.modpass.ModData;
import pers.saikel0rado1iu.silk.modpass.ModMain;
import pers.saikel0rado1iu.silk.modpass.ModPass;
import pers.saikel0rado1iu.silk.modpass.registry.MainRegistrationProvider;
import pers.saikel0rado1iu.silk.modpass.pack.DataPack;
import pers.saikel0rado1iu.silk.modpass.pack.ResourcePack;

import java.util.Optional;
import java.util.Set;

/**
 * Main
 */
public final class Main implements ModMain {
	/**
	 * MOD_PASS
	 */
	public static final SilkApi MOD_PASS = new SilkApi() {
		@Override
		public String id() {
			return "silk-generate-test";
		}
		
		@Override
		public Optional<DataPack> dataPack() {
			return Optional.of(DataPack.create("data", ResourcePackActivationType.ALWAYS_ENABLED, modData()));
		}
		
		@Override
		public Optional<ResourcePack> resourcePack() {
			return Optional.of(ResourcePack.create("resource", ResourcePackActivationType.ALWAYS_ENABLED, modData()));
		}
	};
	
	/**
	 * 模组主函数
	 *
	 * @param mod 提供的模组通
	 */
	@Override
	public void main(ModPass mod) {
	}
	
	/**
	 * 注册表方法，提供注册表以供注册
	 *
	 * @return 注册表的类型集合
	 */
	@Override
	public Set<Class<? extends MainRegistrationProvider<?>>> registry() {
		return ImmutableSet.of(Items.class);
	}
	
	/**
	 * 用于提供模组数据以基于模组数据实现功能
	 *
	 * @return 模组数据
	 */
	@Override
	public ModData modData() {
		return MOD_PASS;
	}
}