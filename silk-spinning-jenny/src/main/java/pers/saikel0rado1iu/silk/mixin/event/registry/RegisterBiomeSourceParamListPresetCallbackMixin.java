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

package pers.saikel0rado1iu.silk.mixin.event.registry;

import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import pers.saikel0rado1iu.silk.api.event.registry.RegisterBiomeSourceParamListPresetCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>{@link RegisterBiomeSourceParamListPresetCallback} 混入</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.0.0
 */
@Mixin(MultiNoiseBiomeSourceParameterList.Preset.class)
abstract class RegisterBiomeSourceParamListPresetCallbackMixin {
    @SuppressWarnings("unchecked")
    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE",
                                             target = "L java/util/stream/Stream;of([L java/lang/Object;)L java/util/stream/Stream;"))
    private static <T> T[] register(T[] byId) {
        List<T> presets = new ArrayList<>(List.of(byId));
        RegisterBiomeSourceParamListPresetCallback.EVENT.invoker()
                                                        .add((List<MultiNoiseBiomeSourceParameterList.Preset>) presets);
        return (T[]) presets.toArray(new Object[0]);
    }
}
