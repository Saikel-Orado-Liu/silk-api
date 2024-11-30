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

package pers.saikel0rado1iu.silk.api.base.common.noise;

import java.util.Random;

/**
 * <h2>白噪声</h2>
 * 用于生成二维白噪声
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.0.0
 */
public final class WhiteNoise extends Noise {
    private final double intensity;

    /**
     * 构造一个 WhiteNoise 实例。
     *
     * @param seed      随机数种子
     * @param intensity 噪声强度
     * @param width     噪声图像的宽度
     * @param height    噪声图像的高度
     */
    public WhiteNoise(int width, int height, double intensity, long seed) {
        super(width, height, seed);
        this.intensity = intensity;

        generate();
    }

    private WhiteNoise(int width, int height, double intensity, long seed, double[][] noiseMap) {
        super(width, height, seed);
        this.intensity = intensity;
        this.noiseMap = noiseMap;
    }

    @Override
    protected void generate() {
        final Random random = new Random(seed());

        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                noiseMap[i][j] = random.nextDouble() * intensity;
            }
        }
    }

    @Override
    public WhiteNoise scale(int newWidth, int newHeight) {
        final double[][] noiseMap = noiseMap();
        final double[][] scaledNoise = new double[newHeight][newWidth];

        double scaleX = (double) noiseMap[0].length / newWidth;
        double scaleY = (double) noiseMap.length / newHeight;

        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                double sampleX = x * scaleX;
                double sampleY = y * scaleY;
                double interpolatedValue = getInterpolatedValue(noiseMap, sampleX, sampleY);
                scaledNoise[y][x] = interpolatedValue;
            }
        }

        return new WhiteNoise(newWidth, newHeight, intensity, seed(), scaledNoise);
    }
}