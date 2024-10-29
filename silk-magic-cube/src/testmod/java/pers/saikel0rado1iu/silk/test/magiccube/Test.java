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

package pers.saikel0rado1iu.silk.test.magiccube;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.apache.commons.lang3.function.TriConsumer;
import pers.saikel0rado1iu.silk.api.event.magiccube.FireIgniteBlockCallback;
import pers.saikel0rado1iu.silk.api.event.magiccube.FlintAndSteelIgniteBlockCallback;
import pers.saikel0rado1iu.silk.api.modpass.ModData;
import pers.saikel0rado1iu.silk.api.modpass.ModMain;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;
import pers.saikel0rado1iu.silk.api.modpass.registry.MainRegistrationProvider;
import pers.saikel0rado1iu.silk.impl.SilkMagicCube;
import pers.saikel0rado1iu.silk.test.magiccube.cauldron.CauldronLikeBehaviorTest;
import pers.saikel0rado1iu.silk.test.magiccube.entity.EntityTypes;

import java.util.Set;
import java.util.function.BiConsumer;

/**
 * 测试
 */
public final class Test implements ModMain {
	/**
	 * 模组主函数
	 *
	 * @param mod 提供的模组通
	 */
	@Override
	public void main(ModPass mod) {
		CauldronLikeBehaviorTest.INSTANCE.registerBehavior();
		FireIgniteBlockCallback.EVENT.register((state, world, pos, spreadFactor, random, currentAge) -> {
			if (!state.isOf(Blocks.TNT_LIKE_BLOCK)) return false;
			Blocks.TNT_LIKE_BLOCK.primeTnt(world, pos);
			return true;
		});
		FlintAndSteelIgniteBlockCallback.EVENT.register((state, pointer, world, direction, pos, stack) -> {
			if (!state.isOf(Blocks.TNT_LIKE_BLOCK)) return false;
			Blocks.TNT_LIKE_BLOCK.primeTnt(world, pos);
			return true;
		});
		SilkMagicCube.getInstance().logger().warn(((TriConsumer<BlockView, BlockPos, BiConsumer<BlockPos, BlockState>>) Blocks.TEST_BLOCK::placeBlock).toString());
	}
	
	/**
	 * 注册表方法，提供注册表以供注册
	 *
	 * @return 注册表的类型集合
	 */
	@Override
	public Set<Class<? extends MainRegistrationProvider<?>>> registry() {
		return ImmutableSet.of(
				Items.class,
				Blocks.class,
				BlockEntityTypes.class,
				EntityTypes.class
		);
	}
	
	/**
	 * 用于提供模组数据以基于模组数据实现功能
	 *
	 * @return 模组数据
	 */
	@Override
	public ModData modData() {
		return SilkMagicCube.getInstance();
	}
}
