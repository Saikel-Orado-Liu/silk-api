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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.SimpleBlockFeature;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.api.callback.SimpleBlockFeatureCreateCallback;

/**
 * <p><b style="color:FFC800"><font size="+1">设置简单块地物创建回调</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@Mixin(SimpleBlockFeature.class)
abstract class SimpleBlockFeatureCreateMixin implements WorldView {
	@Inject(method = "generate", at = @At("RETURN"), cancellable = true)
	private void generate(FeatureContext<SimpleBlockFeatureConfig> context, CallbackInfoReturnable<Boolean> cir) {
		SimpleBlockFeatureConfig simpleBlockFeatureConfig = context.getConfig();
		StructureWorldAccess world = context.getWorld();
		BlockPos pos = context.getOrigin();
		BlockState state = simpleBlockFeatureConfig.toPlace().get(context.getRandom(), pos);
		SimpleBlockFeatureCreateCallback.ReturnValue returnValue = SimpleBlockFeatureCreateCallback.EVENT.invoker().interact(world, pos, state, cir.getReturnValue());
		cir.setReturnValue(returnValue.returnBoolean());
	}
}
