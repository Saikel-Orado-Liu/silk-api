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

import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModMain;
import pers.saikel0rado1iu.silk.api.registry.gen.world.feature.SilkFeature;
import pers.saikel0rado1iu.silk.api.registry.gen.world.foliage.SilkFoliagePlacerType;
import pers.saikel0rado1iu.silk.api.registry.gen.world.treedecorator.SilkTreeDecoratorType;
import pers.saikel0rado1iu.silk.api.registry.gen.world.trunk.SilkTrunkPlacerType;

/**
 * <p><b style="color:FFC800"><font size="+1">用于模组所有的世界数据注册</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link ModMain} 中覆盖 {@link ModMain#worldData()}方法</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkWorldData {
	@ApiStatus.Internal
	public void register() {
		features();
		trunkPlacerTypes();
		foliagePlacerTypes();
		treeDecoratorTypes();
	}
	
	/**
	 * 提供来自模组的地物以供注册
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkFeature features() {
		return null;
	}
	
	/**
	 * 提供来自模组的树干放置器类型以供注册
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkTrunkPlacerType trunkPlacerTypes() {
		return null;
	}
	
	/**
	 * 提供来自模组的树叶放置器类型以供注册
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkFoliagePlacerType foliagePlacerTypes() {
		return null;
	}
	
	/**
	 * 提供来自模组的树木装饰器类型以供注册
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkTreeDecoratorType treeDecoratorTypes() {
		return null;
	}
}