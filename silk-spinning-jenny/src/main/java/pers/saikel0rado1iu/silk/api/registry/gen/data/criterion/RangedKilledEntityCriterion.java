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

package pers.saikel0rado1iu.silk.api.registry.gen.data.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

/**
 * <h2 style="color:FFC800">远程武器击杀实体标准</h2>
 * <p style="color:FFC800">远程武器是通过发射弹射物中储存的物品 NBT 来判断的，此 NBT 需要开发使用 {@link RangedKilledEntityCriterion#putRangedNbt(Entity, ItemStack)} 手动添加到弹射物中。{@link Entity#writeNbt(NbtCompound)} 已被注入，使可以记录参数中 NBT</p>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public class RangedKilledEntityCriterion extends AbstractCriterion<RangedKilledEntityCriterion.Conditions> {
	private static final Identifier ID = new Identifier(Silk.DATA.getId(), "ranged_killed_entity");
	
	@SilkApi
	public static void putRangedNbt(Entity projectile, ItemStack ranged) {
		NbtCompound nbtCompound = new NbtCompound();
		NbtCompound nbt = new NbtCompound();
		nbt.putString("id", Registries.ITEM.getId(ranged.getItem()).toString());
		nbt.put("nbt", ranged.getNbt());
		nbtCompound.put("fromRanged", nbt);
		projectile.writeNbt(nbtCompound);
	}
	
	@Override
	protected Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate lootContextPredicate, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		LootContextPredicate target = EntityPredicate.contextPredicateFromJson(jsonObject, "target", advancementEntityPredicateDeserializer);
		ItemPredicate ranged = ItemPredicate.fromJson(jsonObject.get("ranged"));
		EntityPredicate projectile = EntityPredicate.fromJson(jsonObject.get("projectile"));
		NumberRange.IntRange killed = NumberRange.IntRange.fromJson(jsonObject.get("killed"));
		return new Conditions(lootContextPredicate, target, ranged, projectile, killed);
	}
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@SilkApi
	public void trigger(ServerPlayerEntity player, Entity entity, DamageSource damageSource) {
		trigger(player, entity, damageSource, 1);
	}
	
	@SilkApi
	public void trigger(ServerPlayerEntity player, Entity entity, DamageSource damageSource, int killed) {
		LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, entity);
		trigger(player, conditions -> conditions.matches(player, lootContext, damageSource.getSource(), killed));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate ranged;
		private LootContextPredicate target;
		private EntityPredicate projectile;
		private NumberRange.IntRange killed;
		private int count = 0;
		
		public Conditions(LootContextPredicate player, LootContextPredicate target, ItemPredicate ranged, EntityPredicate projectile, NumberRange.IntRange killed) {
			super(ID, player);
			this.target = target;
			this.ranged = ranged;
			this.projectile = projectile;
			this.killed = killed;
		}
		
		@SilkApi
		public static Conditions ranged(ItemPredicate ranged) {
			return new Conditions(LootContextPredicate.EMPTY, EntityPredicate.asLootContextPredicate(EntityPredicate.ANY), ranged, EntityPredicate.ANY, NumberRange.IntRange.ANY);
		}
		
		@SilkApi
		public static Conditions ranged(ItemConvertible ranged) {
			ItemPredicate itemPredicates = ItemPredicate.Builder.create().items(ranged.asItem()).build();
			return ranged(itemPredicates);
		}
		
		@SilkApi
		public Conditions target(EntityPredicate target) {
			this.target = EntityPredicate.asLootContextPredicate(target);
			return this;
		}
		
		@SilkApi
		public Conditions projectile(EntityPredicate projectile) {
			this.projectile = projectile;
			return this;
		}
		
		@SilkApi
		public Conditions killed(NumberRange.IntRange killed) {
			this.killed = killed;
			return this;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("target", target.toJson(predicateSerializer));
			jsonObject.add("ranged", ranged.toJson());
			jsonObject.add("projectile", projectile.toJson());
			jsonObject.add("killed", killed.toJson());
			return jsonObject;
		}
		
		public boolean matches(ServerPlayerEntity player, LootContext killedEntityContext, Entity projectile, int count) {
			if (!target.test(killedEntityContext)) return false;
			if (projectile == null) return false;
			NbtCompound nbtCompound = projectile.writeNbt(new NbtCompound()).getCompound("fromRanged");
			String[] id = nbtCompound.getString("id").split(":");
			ItemStack stack = new ItemStack("".equals(id[0]) ? Items.AIR : Registries.ITEM.get(new Identifier(id[0], id[1])));
			stack.setNbt(nbtCompound.getCompound("nbt"));
			boolean hasRanged = this.ranged.test(stack);
			if (!hasRanged) return false;
			if (!this.projectile.test(player, projectile)) return false;
			return killed.test(this.count += count);
		}
	}
}