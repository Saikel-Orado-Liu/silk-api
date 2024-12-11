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

package pers.saikel0rado1iu.silk.api.ropestick.tool;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.tag.TagKey;

import java.util.function.Supplier;

/**
 * <h2>工具助手</h2>
 * 辅助工具的创建的数据直观和清晰
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.1.2
 */
public interface ToolHelper {
    /**
     * 基础伤害
     */
    int BASE_DAMAGE = 1;
    /**
     * 基础攻速
     */
    int BASE_SPEED = 4;

    /**
     * 创建工具材料
     *
     * @param toolHelper 工具接口
     * @return 工具材料
     */
    static ToolMaterial createMaterial(ToolHelper toolHelper) {
        return new ToolMaterial(
                toolHelper.incorrectBlocksForDrops(),
                toolHelper.durability(),
                toolHelper.speed(),
                toolHelper.attackDamageBonus(),
                toolHelper.enchantability(),
                toolHelper.repairItems()
        );
    }

    /**
     * 创建锹
     *
     * @param damage   伤害
     * @param settings 物品设置
     * @return 锹物品
     */
    default ShovelItem createShovel(float damage, Item.Settings settings) {
        return new ShovelItem(material().get(), getDamage(damage), getSpeed(1), settings);
    }

    /**
     * 创建镐
     *
     * @param damage   伤害
     * @param settings 物品设置
     * @return 镐物品
     */
    default PickaxeItem createPickaxe(float damage, Item.Settings settings) {
        return new PickaxeItem(material().get(), getDamage(damage), getSpeed(1.2F), settings);
    }

    /**
     * 创建斧
     *
     * @param damage   伤害
     * @param speed    攻击速度
     * @param settings 物品设置
     * @return 斧物品
     */
    default AxeItem createAxe(float damage, float speed, Item.Settings settings) {
        return new AxeItem(material().get(), getDamage(damage), getSpeed(speed), settings);
    }

    /**
     * 创建锄
     *
     * @param speed    攻击速度
     * @param settings 物品设置
     * @return 锄物品
     */
    default HoeItem createHoe(float speed, Item.Settings settings) {
        return new HoeItem(material().get(), getDamage(1), getSpeed(speed), settings);
    }

    /**
     * 创建剑
     *
     * @param damage   伤害
     * @param settings 物品设置
     * @return 剑物品
     */
    default SwordItem createSword(float damage, Item.Settings settings) {
        return new SwordItem(material().get(), getDamage(damage), getSpeed(1.6F), settings);
    }

    /**
     * 获取伤害
     *
     * @param damage 攻击伤害
     * @return 实际设置数据
     */
    private float getDamage(float damage) {
        return damage - BASE_DAMAGE - attackDamageBonus();
    }

    /**
     * 获取攻速
     *
     * @param speed 攻击速度
     * @return 实际设置攻速
     */
    private float getSpeed(float speed) {
        return speed - BASE_SPEED;
    }

    /**
     * 工具的挖掘等级属性
     *
     * @return 方块标签键
     */
    TagKey<Block> incorrectBlocksForDrops();

    /**
     * 耐久度属性
     *
     * @return 耐久度属性
     */
    int durability();

    /**
     * 使用速度属性
     *
     * @return 使用速度
     */
    float speed();

    /**
     * 攻击伤害加成属性
     *
     * @return 攻击伤害加成
     */
    float attackDamageBonus();

    /**
     * 附魔能力属性
     *
     * @return 附魔能力
     */
    int enchantability();

    /**
     * 修复物品属性
     *
     * @return 物品标签键
     */
    TagKey<Item> repairItems();

    /**
     * 物品材料属性
     *
     * @return 物品材料提供器
     */
    Supplier<ToolMaterial> material();
}
