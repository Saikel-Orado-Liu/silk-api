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

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;

import java.util.List;
import java.util.function.Predicate;

import static net.minecraft.component.DataComponentTypes.CHARGED_PROJECTILES;
import static pers.saikel0rado1iu.silk.api.ropestick.component.DataComponentTypes.RANGED_WEAPON;

/**
 * <h2 style="color:FFC800">远程武器组件</h2>
 * 用于扩展远程武器特性的数据组件
 *
 * @param maxSpeed              最大发射物速度
 * @param maxDamage             最大非暴击伤害
 * @param maxUseTicks           最大使用刻数
 * @param maxPullTicks          最大拉弓刻数
 * @param firingError           射击误差
 * @param defaultProjectile     默认的发射物
 * @param launchableProjectiles 远程武器能发射的所有的发射物
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public record RangedWeaponComponent(float maxSpeed,
                                    float maxDamage,
                                    int maxUseTicks,
                                    int maxPullTicks,
                                    float firingError,
                                    ItemStack defaultProjectile,
                                    List<Item> launchableProjectiles) {
	/**
	 * 用于模型谓词，拉弓中状态键
	 */
	public static final String PULLING_KEY = "pulling";
	/**
	 * 用于模型谓词，拉弓状态键
	 */
	public static final String PULL_KEY = "pull";
	/**
	 * 用于模型谓词，弹药状态键
	 */
	public static final String PROJECTILE_INDEX_KEY = "projectile";
	/**
	 * 用于模型谓词，已装填状态键
	 */
	public static final String CHARGED_KEY = "charged";
	/**
	 * 弓最大发射物速度
	 */
	public static final float BOW_MAX_PROJECTILE_SPEED = 3;
	/**
	 * 弓最大使用刻数
	 */
	public static final int BOW_MAX_USE_TICKS = 72000;
	/**
	 * 弓最大拉弓时间
	 */
	public static final int BOW_MAX_PULL_TICKS = 20;
	/**
	 * 弓的最大非暴击伤害
	 */
	public static final float BOW_MAX_DAMAGE = 6;
	/**
	 * 弩最大使用刻数
	 */
	public static final int CROSSBOW_MAX_USE_TICKS = 25;
	/**
	 * 弩最大发射物速度
	 */
	public static final float CROSSBOW_MAX_PROJECTILE_SPEED = 3.15F;
	/**
	 * 弩的最大非暴击伤害
	 */
	public static final float CROSSBOW_MAX_DAMAGE = 7;
	/**
	 * 默认射击误差
	 */
	public static final float DEFAULT_FIRING_ERROR = 1;
	public static final RangedWeaponComponent BOW = builder()
			.maxSpeed(BOW_MAX_PROJECTILE_SPEED)
			.maxDamage(BOW_MAX_DAMAGE)
			.maxUseTicks(BOW_MAX_USE_TICKS)
			.maxPullTicks(BOW_MAX_PULL_TICKS)
			.firingError(DEFAULT_FIRING_ERROR)
			.defaultProjectile(Items.ARROW.getDefaultStack())
			.launchableProjectiles(ImmutableList.of(
					Items.ARROW,
					Items.SPECTRAL_ARROW,
					Items.TIPPED_ARROW))
			.build();
	public static final RangedWeaponComponent CROSSBOW = builder()
			.maxSpeed(CROSSBOW_MAX_PROJECTILE_SPEED)
			.maxDamage(CROSSBOW_MAX_DAMAGE)
			.maxUseTicks(CROSSBOW_MAX_USE_TICKS)
			.maxPullTicks(CROSSBOW_MAX_USE_TICKS)
			.firingError(DEFAULT_FIRING_ERROR)
			.defaultProjectile(Items.ARROW.getDefaultStack())
			.launchableProjectiles(ImmutableList.of(
					Items.ARROW,
					Items.SPECTRAL_ARROW,
					Items.TIPPED_ARROW,
					Items.FIREWORK_ROCKET))
			.build();
	public static final Codec<RangedWeaponComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
					Codec.FLOAT.fieldOf("max_speed").forGetter(RangedWeaponComponent::maxSpeed),
					Codec.FLOAT.fieldOf("max_damage").forGetter(RangedWeaponComponent::maxDamage),
					Codec.INT.fieldOf("max_use_ticks").forGetter(RangedWeaponComponent::maxUseTicks),
					Codec.INT.fieldOf("max_pull_ticks").forGetter(RangedWeaponComponent::maxPullTicks),
					Codec.FLOAT.fieldOf("firing_error").forGetter(RangedWeaponComponent::firingError),
					ItemStack.CODEC.fieldOf("default_projectile").forGetter(RangedWeaponComponent::defaultProjectile),
					Registries.ITEM.getCodec().listOf().fieldOf("launchable_projectiles").forGetter(RangedWeaponComponent::launchableProjectiles))
			.apply(builder, RangedWeaponComponent::new));
	public static final PacketCodec<RegistryByteBuf, RangedWeaponComponent> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);
	
	/**
	 * 构建方法<br>
	 * 允许以可选参数的形式创建组件
	 *
	 * @return 远程武器组件构建器
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	/**
	 * 获取发射物类型<br>
	 * 实现此方法以解决原版方法默认返回 {@link net.minecraft.item.Items#ARROW} 的问题
	 *
	 * @param entity 判断实体
	 * @param stack  远程武器
	 * @return 发射物
	 */
	public static ItemStack getProjectileType(LivingEntity entity, ItemStack stack) {
		RangedWeaponComponent component = stack.get(RANGED_WEAPON);
		if (component == null) return ItemStack.EMPTY;
		if (entity instanceof HostileEntity hostile) {
			if (!(stack.getItem() instanceof RangedWeaponItem ranged)) return ItemStack.EMPTY;
			Predicate<ItemStack> predicate = ranged.getHeldProjectiles();
			ItemStack itemStack = RangedWeaponItem.getHeldProjectile(hostile, predicate);
			return itemStack.isEmpty() ? component.defaultProjectile : itemStack;
		} else if (entity instanceof PlayerEntity player) {
			if (!(stack.getItem() instanceof RangedWeaponItem ranged)) return ItemStack.EMPTY;
			Predicate<ItemStack> predicate = ranged.getHeldProjectiles();
			ItemStack itemStack = RangedWeaponItem.getHeldProjectile(player, predicate);
			if (!itemStack.isEmpty()) return itemStack;
			predicate = ranged.getProjectiles();
			
			for (int count = 0; count < player.getInventory().size(); ++count) {
				ItemStack itemStack2 = player.getInventory().getStack(count);
				if (predicate.test(itemStack2)) return itemStack2;
			}
			
			return player.getAbilities().creativeMode ? component.defaultProjectile : ItemStack.EMPTY;
		}
		return ItemStack.EMPTY;
	}
	
	/**
	 * 获取“快速装填”刻数
	 *
	 * @param ticks 原始刻数
	 * @param stack 物品堆栈
	 * @return 有“快速装填”附魔的刻数
	 */
	public static int getQuickTicks(int ticks, ItemStack stack) {
		// 设置“快速装填”效果
		int quickChargeLevel = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
		return quickChargeLevel == 0 ? ticks : ticks - ticks / 5 * quickChargeLevel;
	}
	
	/**
	 * 获取发射物索引以供 JSON 渲染使用
	 *
	 * @return 索引
	 */
	public float getProjectileIndex(LivingEntity entity, ItemStack stack) {
		if (entity.getActiveItem() == stack) return getProjectileIndex(RangedWeaponComponent.getProjectileType(entity, stack));
		ChargedProjectilesComponent component = stack.get(CHARGED_PROJECTILES);
		if (component == null || component.isEmpty()) return 0;
		return getProjectileIndex(component.getProjectiles().getFirst());
	}
	
	/**
	 * 获取发射物索引以供 JSON 渲染使用
	 *
	 * @param projectile 发射物
	 * @return 索引
	 */
	public float getProjectileIndex(ItemStack projectile) {
		return Float.parseFloat("0." + Math.max(0, launchableProjectiles.indexOf(projectile.getItem())));
	}
	
	/**
	 * 修正的发射物基础伤害，使其能符合设置的最大伤害
	 *
	 * @return 修正的发射物伤害
	 */
	public float adjustedProjectileDamage() {
		return maxDamage / maxSpeed;
	}
	
	/**
	 * 获取最大发射物速度
	 *
	 * @param stack 物品堆栈
	 * @return 最大发射物速度
	 */
	public float getMaxProjectileSpeed(ItemStack stack) {
		ChargedProjectilesComponent component = stack.getOrDefault(CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
		return component.contains(Items.FIREWORK_ROCKET) ? maxSpeed / 2 : maxSpeed;
	}
	
	/**
	 * 远程武器组件构建器
	 */
	public static class Builder {
		private float maxSpeed = BOW_MAX_PROJECTILE_SPEED;
		private float maxDamage = BOW_MAX_DAMAGE;
		private int maxUseTicks = BOW_MAX_USE_TICKS;
		private int maxPullTicks = BOW_MAX_PULL_TICKS;
		private float firingError = DEFAULT_FIRING_ERROR;
		private ItemStack defaultProjectile = Items.ARROW.getDefaultStack();
		private List<Item> launchableProjectiles = ImmutableList.of(
				Items.ARROW,
				Items.SPECTRAL_ARROW,
				Items.TIPPED_ARROW);
		
		private Builder() {
		}
		
		public Builder maxSpeed(float maxSpeed) {
			this.maxSpeed = maxSpeed;
			return this;
		}
		
		public Builder maxDamage(float maxDamage) {
			this.maxDamage = maxDamage;
			return this;
		}
		
		public Builder maxUseTicks(int maxUseTicks) {
			this.maxUseTicks = maxUseTicks;
			return this;
		}
		
		public Builder maxPullTicks(int maxPullTicks) {
			this.maxPullTicks = maxPullTicks;
			return this;
		}
		
		public Builder firingError(float firingError) {
			this.firingError = firingError;
			return this;
		}
		
		public Builder defaultProjectile(ItemStack defaultProjectile) {
			this.defaultProjectile = defaultProjectile;
			return this;
		}
		
		public Builder launchableProjectiles(List<Item> launchableProjectiles) {
			this.launchableProjectiles = launchableProjectiles;
			return this;
		}
		
		/**
		 * 构建方法
		 *
		 * @return 远程武器组件
		 */
		public RangedWeaponComponent build() {
			return new RangedWeaponComponent(maxSpeed, maxDamage, maxUseTicks, maxPullTicks, firingError, defaultProjectile, launchableProjectiles);
		}
	}
}
