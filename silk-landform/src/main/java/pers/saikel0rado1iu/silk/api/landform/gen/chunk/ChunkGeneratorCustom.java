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

package pers.saikel0rado1iu.silk.api.landform.gen.chunk;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * <h2 style="color:FFC800">区块生成器可自定义</h2>
 * 此接口专门用于在 {@link DefaultChunkGenerator} 基础上进行的自定义区块修改操作
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
public interface ChunkGeneratorCustom {
	/**
	 * 用于储存所有的区块生成器与其种子
	 */
	Map<ChunkGeneratorCustom, Long> SEED_MAP = Maps.newHashMapWithExpectedSize(4);
	
	/**
	 * @return 生成世界的种子
	 */
	default Long seed() {
		return SEED_MAP.get(this);
	}
	
	/**
	 * locate 命令的新定位生物群系方法，如果使用了特殊方式生成了生物群系则必须覆盖此方法才能正确定位
	 *
	 * @param origin                       原始块坐标
	 * @param radius                       侦测半径
	 * @param horizontalBlockCheckInterval 水平方块检查间隔
	 * @param verticalBlockCheckInterval   垂直方块检查间隔
	 * @param predicate                    生物群系谓词
	 * @param noiseSampler                 多重噪声采样器
	 * @param world                        服务端游戏世界
	 * @return 块坐标与生物群系的配对
	 */
	default Optional<Pair<BlockPos, RegistryEntry<Biome>>> locateBiome(BlockPos origin, int radius, int horizontalBlockCheckInterval, int verticalBlockCheckInterval, Predicate<RegistryEntry<Biome>> predicate, MultiNoiseUtil.MultiNoiseSampler noiseSampler, ServerWorld world) {
		return Optional.ofNullable(world.locateBiome(predicate, origin, radius, horizontalBlockCheckInterval, verticalBlockCheckInterval));
	}
	
	/**
	 * 通过在世界中的块坐标判断生物群系，用于设置以特殊方法而非原版方法生成生物群系
	 *
	 * @param pos 块坐标
	 * @return 在坐标上应生成的生物群系源
	 */
	BiomeSource getBiomeSource(BlockPos pos);
	
	/**
	 * 修改地形噪声的一种方法，用于设置以特殊方法而非原版方法生成地形噪声
	 *
	 * @param pos                   块坐标
	 * @param originBlock           原始块
	 * @param estimateSurfaceHeight 预期表面高度
	 * @return 应设置的噪声块
	 */
	Optional<BlockState> getTerrainNoise(BlockPos pos, Optional<BlockState> originBlock, int estimateSurfaceHeight);
}
