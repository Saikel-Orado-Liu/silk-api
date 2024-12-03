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

package pers.saikel0rado1iu.silk.test.base;

import net.fabricmc.api.ModInitializer;
import pers.saikel0rado1iu.silk.test.base.common.collect.MultiWayTreeTest;
import pers.saikel0rado1iu.silk.test.base.common.noise.PerlinNoiseTest;
import pers.saikel0rado1iu.silk.test.base.common.noise.WhiteNoiseTest;
import pers.saikel0rado1iu.silk.test.base.common.util.*;

/**
 * 测试
 */
public final class Test implements ModInitializer {
    @Override
    public void onInitialize() {
        MultiWayTreeTest.test();
        PerlinNoiseTest.test();
        WhiteNoiseTest.test();
        JarUtilTest.test();
        MathUtilTest.test();
        PlayerUtilTest.test();
        SpawnUtilTest.test();
        TickUtilTest.test();
    }
}
