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

package pers.saikel0rado1iu.silk.test.ropestick;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import pers.saikel0rado1iu.silk.api.ropestick.component.ComponentTypes;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.CustomEntityHurtComponent;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.EffectiveItemSlotData;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.InherentStatusEffectData;
import pers.saikel0rado1iu.silk.api.ropestick.component.type.InherentStatusEffectsComponent;
import pers.saikel0rado1iu.silk.api.ropestick.equipment.ArmorHelper;
import pers.saikel0rado1iu.silk.impl.SilkRopeStick;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Test {@link ArmorHelper}
 */
public enum ArmorHelperTest implements ArmorHelper {
    /** 材料 */
    MATERIAL("test", 2, Util.make(new EnumMap<>(EquipmentType.class), map -> {
        map.put(EquipmentType.BOOTS, 2);
        map.put(EquipmentType.LEGGINGS, 5);
        map.put(EquipmentType.CHESTPLATE, 6);
        map.put(EquipmentType.HELMET, 2);
        map.put(EquipmentType.BODY, 5);
    }), 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 5, 2, ItemTags.REPAIRS_IRON_ARMOR);

    private final String name;
    private final int durability;
    private final Map<EquipmentType, Integer> defense;
    private final int enchantability;
    private final RegistryEntry<SoundEvent> equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final TagKey<Item> ingredient;
    private final Supplier<ArmorMaterial> material;

    ArmorHelperTest(String name, int durability, Map<EquipmentType, Integer> defense,
                    int enchantability, RegistryEntry<SoundEvent> equipSound, float toughness,
                    float knockbackResistance, TagKey<Item> ingredient) {
        this.name = name;
        this.durability = durability;
        this.defense = defense;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.ingredient = ingredient;
        this.material = Suppliers.memoize(() -> ArmorHelper.createMaterial(this));
    }

    /**
     * 创建盔甲设置
     *
     * @return 物品设置
     */
    public static Item.Settings createArmorSettings() {
        return new Item.Settings()
                .component(ComponentTypes.INHERENT_STATUS_EFFECTS, InherentStatusEffectsComponent.of(
                        InherentStatusEffectData.create(
                                StatusEffects.LUCK,
                                0,
                                5,
                                1,
                                () -> ImmutableList.of(Items.TEST_HELMET, Items.TEST_CHESTPLATE, Items.TEST_LEGGINGS, Items.TEST_BOOTS),
                                0,
                                EffectiveItemSlotData.ARMOR),
                        InherentStatusEffectData.create(
                                StatusEffects.UNLUCK,
                                1,
                                2,
                                0.5F,
                                () -> ImmutableList.of(Items.TEST_HELMET, Items.TEST_CHESTPLATE, Items.TEST_LEGGINGS, Items.TEST_BOOTS),
                                2,
                                EffectiveItemSlotData.ALL)))
                .component(ComponentTypes.CUSTOM_ENTITY_HURT, new CustomEntityHurtComponent(
                        ImmutableList.of(DamageTypes.MOB_ATTACK),
                        "amount * 3"));
    }

    @Override
    public Identifier id() {
        return SilkRopeStick.INSTANCE.ofId(name);
    }

    @Override
    public int durability() {
        return durability;
    }

    @Override
    public Map<EquipmentType, Integer> defense() {
        return defense;
    }

    @Override
    public int enchantability() {
        return enchantability;
    }

    @Override
    public RegistryEntry<SoundEvent> equipSound() {
        return equipSound;
    }

    @Override
    public TagKey<Item> repairIngredient() {
        return ingredient;
    }

    @Override
    public float toughness() {
        return toughness;
    }

    @Override
    public float knockbackResistance() {
        return knockbackResistance;
    }

    @Override
    public Supplier<ArmorMaterial> material() {
        return material;
    }
}
