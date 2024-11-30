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
 * <h2>柏林噪声</h2>
 * 用于生成二维柏林噪声
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.0.0
 */
public final class PerlinNoise extends Noise {
    private final double frequency;
    private final double amplitude;
    private final int octaves;
    private final double persistence;

    /**
     * 初始化 {@link PerlinNoise} 对象
     *
     * @param width       噪声数组的宽度
     * @param height      噪声数组的高度
     * @param frequency   频率
     * @param amplitude   振幅
     * @param octaves     八度
     * @param persistence 持续度
     * @param seed        随机种子
     */
    public PerlinNoise(int width, int height, double frequency, double amplitude, int octaves,
                       double persistence, long seed) {
        super(width, height, seed);
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.octaves = octaves;
        this.persistence = persistence;

        generate();
    }

    private PerlinNoise(int width, int height, double frequency, double amplitude, int octaves,
                        double persistence, long seed, double[][] noiseMap) {
        super(width, height, seed);
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.octaves = octaves;
        this.persistence = persistence;
        this.noiseMap = noiseMap;
    }

    /**
     * 生成平滑噪声。
     *
     * @param random 随机数生成器对象，用于生成随机数。
     * @return 返回生成的平滑噪声值。
     */
    private static double smoothNoise(Random random) {
        double corners = (random.nextDouble() - 0.5) * 2;
        double sides = (random.nextDouble() - 0.5) * 2;
        double center = (random.nextDouble() - 0.5) * 2;
        double edges = (corners + sides) / 2;
        return (center + edges) / 2;
    }

    /**
     * 生成插值噪声
     *
     * @param x      水平方向的位置
     * @param y      垂直方向的位置
     * @param random 随机数生成器
     * @return 插值噪声值
     */
    private static double interpolatedNoise(double x, double y, Random random) {
        int intX = (int) x;
        int intY = (int) y;
        double fracX = x - intX;
        double fracY = y - intY;

        double v1 = smoothNoise(random);
        double v2 = smoothNoise(random);
        double v3 = smoothNoise(random);
        double v4 = smoothNoise(random);

        double i1 = interpolate(v1, v2, fracX);
        double i2 = interpolate(v3, v4, fracX);

        return interpolate(i1, i2, fracY);
    }

    /**
     * 执行线性插值以获取给定值v在a和b之间的插值。
     *
     * @param a 插值范围的起始值
     * @param b 插值范围的结束值
     * @param v 要插值的值，应该在 0 到 1 之间
     * @return 在 a 和 b 之间进行插值后得到的结果
     */
    private static double interpolate(double a, double b, double v) {
        double ft = v * Math.PI;
        double f = (1 - Math.cos(ft)) * 0.5;
        return a * (1 - f) + b * f;
    }

    @Override
    protected void generate() {
        final Random random = new Random(seed());

        // Generate noise
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                double total = 0;
                double frequency = this.frequency;
                double amplitude = this.amplitude;

                for (int i = 0; i < octaves; i++) {
                    total += interpolatedNoise(x * frequency, y * frequency, random) * amplitude;
                    frequency *= 2;
                    amplitude *= persistence;
                }

                noiseMap[y][x] = total;
            }
        }
    }

    @Override
    public PerlinNoise scale(int newWidth, int newHeight) {
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

        return new PerlinNoise(newWidth, newHeight, frequency, amplitude,
                octaves, persistence, seed(), scaledNoise);
    }
}
