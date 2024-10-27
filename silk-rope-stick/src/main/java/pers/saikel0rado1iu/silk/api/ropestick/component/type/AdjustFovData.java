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

package pers.saikel0rado1iu.silk.api.ropestick.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">调整视场角数据</h2>
 * 物品的视场角缩放通用组件数据
 *
 * @param onlyFirstPerson 是否只在第一人称进行缩放
 * @param hudOverlay      抬头显示叠加层，如果为 {@link Optional#empty()} 则不显示叠加层
 * @param canStretchHud   是否可以拉伸抬头显示
 * @param fovScaling      视场角缩放倍数，视场角缩放倍数，&gt; 1 则为放大，0 &lt; x &lt; 1 则为缩小
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public record AdjustFovData(boolean onlyFirstPerson, Optional<Identifier> hudOverlay, boolean canStretchHud, float fovScaling) {
	/**
	 * 弓的视场角缩放值
	 */
	public static final float BOW_FOV_SCALING = 1.08F;
	/**
	 * 默认视场角缩放
	 */
	public static final float DEFAULT_FOV_SCALING = 1.2F;
	/**
	 * 暗角纹理
	 */
	public static final Identifier VIGNETTE_TEXTURE = new Identifier("textures/misc/vignette.png");
	/**
	 * 南瓜头模糊纹理
	 */
	public static final Identifier PUMPKIN_BLUR = new Identifier("textures/misc/pumpkinblur.png");
	/**
	 * 望远镜镜头纹理
	 */
	public static final Identifier SPYGLASS_SCOPE = new Identifier("textures/misc/spyglass_scope.png");
	/**
	 * 细雪轮廓纹理
	 */
	public static final Identifier POWDER_SNOW_OUTLINE = new Identifier("textures/misc/powder_snow_outline.png");
	public static final AdjustFovData DEFAULT = AdjustFovData.create(false, Optional.empty(), false, DEFAULT_FOV_SCALING);
	public static final Codec<AdjustFovData> CODEC = RecordCodecBuilder.create(builder -> builder.group(
					Codec.BOOL.optionalFieldOf("only_first_person", false).forGetter(AdjustFovData::onlyFirstPerson),
					Identifier.CODEC.optionalFieldOf("hud_overlay").forGetter(AdjustFovData::hudOverlay),
					Codec.BOOL.optionalFieldOf("can_stretch_hud", false).forGetter(AdjustFovData::canStretchHud),
					Codec.FLOAT.optionalFieldOf("fov_scaling", DEFAULT_FOV_SCALING).forGetter(AdjustFovData::fovScaling))
			.apply(builder, AdjustFovData::new));
	public static final PacketCodec<RegistryByteBuf, AdjustFovData> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);
	
	/**
	 * 调整视场角数据创建方法
	 *
	 * @param onlyFirstPerson 是否只在第一人称进行缩放
	 * @param hudOverlay      抬头显示叠加层，如果为 {@link Optional#empty()} 则不显示叠加层
	 * @param canStretchHud   是否可以拉伸抬头显示
	 * @param fovScaling      视场角缩放倍数，视场角缩放倍数，&gt; 1 则为放大，0 &lt; x &lt; 1 则为缩小
	 * @return 调整视场角数据
	 */
	public static AdjustFovData create(boolean onlyFirstPerson, Optional<Identifier> hudOverlay, boolean canStretchHud, float fovScaling) {
		return new AdjustFovData(onlyFirstPerson, hudOverlay, canStretchHud, fovScaling);
	}
	
	/**
	 * 视场角缩放倍数
	 *
	 * @return 经过修正的缩放倍数。将倍数的分子分母对调，使得倍数可以正确应用于调整方法
	 */
	public float fovScalingMultiple() {
		return 1 / fovScaling();
	}
}
