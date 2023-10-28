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

package pers.saikel0rado1iu.silk.mixin.api.callback;

import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.api.callback.InfectMushroomCallback;

/**
 * <p><b style="color:FFC800"><font size="+1">设置菌类成长感染回调</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@Mixin(MushroomPlantBlock.class)
abstract class InfectMushroomMixin {
	@Inject(method = "trySpawningBigMushroom", at = @At(value = "INVOKE",
			target = "L net/minecraft/world/gen/feature/ConfiguredFeature;generate(L net/minecraft/world/StructureWorldAccess;L net/minecraft/world/gen/chunk/ChunkGenerator;L net/minecraft/util/math/random/Random;L net/minecraft/util/math/BlockPos;)Z"),
			cancellable = true)
	private void trySpawningBigMushroom(ServerWorld world, BlockPos pos, BlockState state, Random random, CallbackInfoReturnable<Boolean> cir) {
		ActionResult result = InfectMushroomCallback.EVENT.invoker().interact(world, pos, state, random, (MushroomPlantBlock) (Object) this);
		if (result == ActionResult.SUCCESS) cir.setReturnValue(true);
	}
}
