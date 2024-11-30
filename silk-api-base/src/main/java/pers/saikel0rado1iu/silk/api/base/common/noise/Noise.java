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

/**
 * <h2>抽象噪声类</h2>
 * 所有噪声类的抽象父类
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.0.3
 */
public abstract class Noise {
    /** 噪声字符集 */
    public static final char[] NOISE_CHARACTERS = {' ', '░', '▒', '▓', '█'};
    private final int width;
    private final int height;
    private final long seed;
    protected double[][] noiseMap;

    /**
     * @param width  噪声图宽度
     * @param height 噪声图高度
     * @param seed   种子
     */
    protected Noise(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.noiseMap = new double[height][width];
    }

    /**
     * 将噪声数组以字符图像的形式输出。噪声越高，显示的字符越黑
     *
     * @param noiseMap 需要转换的噪声数组
     * @return 显示噪声图的字符串
     */
    public static String getPrintString(double[][] noiseMap) {
        if (noiseMap == null) {
            return "";
        }
        int width = noiseMap[0].length;
        StringBuilder builder = new StringBuilder();

        builder.append("┌").append("─".repeat(Math.max(0, width * 2))).append("┐\n");
        for (double[] row : noiseMap) {
            builder.append("│");
            for (double noise : row) {
                char c = getNoiseCharacter(noise);
                builder.append(c).append(c);
            }
            builder.append("│\n");
        }
        builder.append("└").append("─".repeat(Math.max(0, width * 2))).append("┘\n");

        return builder.toString();
    }

    /**
     * 获取原始噪声数组中指定样本位置的插值值。
     *
     * @param originalNoise 原始噪声
     * @param sampleX       样本的水平位置
     * @param sampleY       样本的垂直位置
     * @return 插值得到的样本值
     */
    protected static double getInterpolatedValue(double[][] originalNoise, double sampleX,
                                                 double sampleY) {
        int x0 = (int) sampleX;
        int x1 = Math.min(x0 + 1, originalNoise[0].length - 1);
        int y0 = (int) sampleY;
        int y1 = Math.min(y0 + 1, originalNoise.length - 1);

        double dx = sampleX - x0;
        double dy = sampleY - y0;

        double topInterpolation = linearInterpolate(originalNoise[y0][x0], originalNoise[y0][x1], dx);
        double bottomInterpolation = linearInterpolate(originalNoise[y1][x0], originalNoise[y1][x1], dx);
        return linearInterpolate(topInterpolation, bottomInterpolation, dy);
    }

    /**
     * 获取噪声字符
     *
     * @param noise 噪声值
     * @return 噪声字符
     */
    private static char getNoiseCharacter(double noise) {
        final double increment = 1.0 / (NOISE_CHARACTERS.length - 1);
        int index = (int) (noise / increment);
        index = Math.min(Math.max(index, 0), NOISE_CHARACTERS.length - 1);
        return NOISE_CHARACTERS[index];
    }

    /**
     * 双线性插值函数。
     *
     * @param a 第一个样本值
     * @param b 第二个样本值
     * @param t 插值系数
     * @return 插值结果
     */
    private static double linearInterpolate(double a, double b, double t) {
        return a * (1 - t) + b * t;
    }

    /**
     * 获取噪声图宽度属性
     *
     * @return 噪声图宽度
     */
    public int width() {
        return width;
    }

    /**
     * 获取噪声图高度属性
     *
     * @return 噪声图高度
     */
    public int height() {
        return height;
    }

    /**
     * 获取噪声图种子属性
     *
     * @return 噪声图种子
     */
    public long seed() {
        return seed;
    }

    /**
     * 获取噪声图
     *
     * @return 噪声图
     */
    public double[][] noiseMap() {
        return noiseMap;
    }

    /**
     * 将噪声数组转换为字符串，用于显示噪声图。
     *
     * @return 显示噪声图的字符串
     */
    public String toString() {
        return getPrintString(noiseMap);
    }

    /**
     * 用于生成噪声的方法
     */
    protected abstract void generate();

    /**
     * 将原始噪声数组按照指定的宽度和高度放大，返回一个新的 {@link Noise} 对象。
     *
     * @param newWidth  新的宽度
     * @param newHeight 新的高度
     * @return 放大后的 {@link Noise} 对象
     */
    public abstract Noise scale(int newWidth, int newHeight);
}
