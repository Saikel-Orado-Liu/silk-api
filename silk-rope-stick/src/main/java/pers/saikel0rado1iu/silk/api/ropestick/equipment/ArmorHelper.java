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

package pers.saikel0rado1iu.silk.api.ropestick.equipment;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Supplier;

/**
 * <h2>盔甲助手</h2>
 * 辅助盔甲的创建的数据直观和清晰
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.1.3
 */
public interface ArmorHelper {
    /**
     * 击退抗性比例
     */
    int KR_RATIO = 10;

    /**
     * 创建盔甲材料
     *
     * @param armorHelper 盔甲接口
     * @return 盔甲材料
     */
    static ArmorMaterial createMaterial(ArmorHelper armorHelper) {
        return new ArmorMaterial(
                armorHelper.durability(),
                armorHelper.defense(),
                armorHelper.enchantability(),
                armorHelper.equipSound(),
                armorHelper.toughness(),
                armorHelper.knockbackResistance() / KR_RATIO,
                armorHelper.repairIngredient(),
                armorHelper.modelId());
    }

    /**
     * 创建头盔
     *
     * @param settings 物品设置
     * @return 头盔物品
     */
    default ArmorItem createHelmet(Item.Settings settings) {
        var material = material().get();
        return new ArmorItem(material, EquipmentType.HELMET,
                material.applySettings(settings, EquipmentType.HELMET));
    }

    /**
     * 创建胸甲
     *
     * @param settings 物品设置
     * @return 头盔胸甲
     */
    default ArmorItem createChestplate(Item.Settings settings) {
        var material = material().get();
        return new ArmorItem(material, EquipmentType.CHESTPLATE,
                material.applySettings(settings, EquipmentType.CHESTPLATE));
    }

    /**
     * 创建护腿
     *
     * @param settings 物品设置
     * @return 头盔护腿
     */
    default ArmorItem createLeggings(Item.Settings settings) {
        var material = material().get();
        return new ArmorItem(material, EquipmentType.LEGGINGS,
                material.applySettings(settings, EquipmentType.LEGGINGS));
    }

    /**
     * 创建靴子
     *
     * @param settings 物品设置
     * @return 头盔靴子
     */
    default ArmorItem createBoots(Item.Settings settings) {
        var material = material().get();
        return new ArmorItem(material, EquipmentType.BOOTS,
                material.applySettings(settings, EquipmentType.BOOTS));
    }

    /**
     * 盔甲模型属性
     *
     * @return 盔甲模型标识符
     */
    default Identifier modelId() {
        return id();
    }

    /**
     * 盔甲的唯一标识符
     *
     * @return 标识符
     */
    Identifier id();

    /**
     * 耐久度
     *
     * @return 耐久度
     */
    int durability();

    /**
     * 盔甲防御属性
     *
     * @return 盔甲防御图表
     */
    Map<EquipmentType, Integer> defense();

    /**
     * 附魔能力属性
     *
     * @return 附魔能力
     */
    int enchantability();

    /**
     * 装备音效属性
     *
     * @return 装备音效注册项
     */
    RegistryEntry<SoundEvent> equipSound();

    /**
     * 修复成分属性
     *
     * @return 物品标签键
     */
    TagKey<Item> repairIngredient();

    /**
     * 韧性属性
     *
     * @return 盔甲韧性
     */
    float toughness();

    /**
     * 击退抗性属性
     *
     * @return 击退抗性
     */
    float knockbackResistance();

    /**
     * 盔甲材料属性
     *
     * @return 盔甲材料提供器
     */
    Supplier<ArmorMaterial> material();
}
