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

package pers.saikel0rado1iu.silk.test.spore;

import pers.saikel0rado1iu.silk.api.spore.EntityUtil;
import pers.saikel0rado1iu.silk.impl.SilkSpore;

/**
 * EntityUtilTest
 */
public class EntityUtilTest {
	/**
	 * 测试
	 */
	static void test() {
		SilkSpore.getInstance().logger().error("EntityUtil.POS_SHIFTING: " + EntityUtil.POS_SHIFTING);
		SilkSpore.getInstance().logger().error("EntityUtil.PROJECTILE_BOX: " + EntityUtil.PROJECTILE_BOX);
		SilkSpore.getInstance().logger().error("EntityUtil.PROJECTILE_MAX_TRACKING_RANGE: " + EntityUtil.PROJECTILE_MAX_TRACKING_RANGE);
		SilkSpore.getInstance().logger().error("EntityUtil.PROJECTILE_TRACKING_TICK_INTERVAL: " + EntityUtil.PROJECTILE_TRACKING_TICK_INTERVAL);
	}
}
