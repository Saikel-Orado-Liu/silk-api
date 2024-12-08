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

package pers.saikel0rado1iu.silk.test.modpass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.saikel0rado1iu.silk.api.modpass.ModBasicData;
import pers.saikel0rado1iu.silk.impl.SilkModPass;

/**
 * Test {@link ModBasicData}
 */
public interface ModDataTest {
    /**
     * 日志
     */
    Logger LOGGER = LoggerFactory.getLogger("SilkModPass/TestingModBasicData");

    /**
     * 测试
     */
    static void test() {
        LOGGER.info("SilkModPass.INSTANCE.id() = {}", SilkModPass.INSTANCE.id());
        LOGGER.info("SilkModPass.INSTANCE.debugName() = {}",
                SilkModPass.INSTANCE.debugName());
        LOGGER.info("SilkModPass.INSTANCE.mod() = {}", SilkModPass.INSTANCE.mod());
        LOGGER.info("SilkModPass.INSTANCE.logger() = {}", SilkModPass.INSTANCE.logger());
        LOGGER.info("SilkModPass.INSTANCE.name() = {}", SilkModPass.INSTANCE.name());
        LOGGER.info("SilkModPass.INSTANCE.description() = {}",
                SilkModPass.INSTANCE.description());
        LOGGER.info("SilkModPass.INSTANCE.version() = {}",
                SilkModPass.INSTANCE.version());
        LOGGER.info("SilkModPass.INSTANCE.slug() = {}", SilkModPass.INSTANCE.slug());
        LOGGER.info("SilkModPass.INSTANCE.authors() = {}",
                SilkModPass.INSTANCE.authors());
        LOGGER.info("SilkModPass.INSTANCE.licenses() = {}",
                SilkModPass.INSTANCE.licenses());
        LOGGER.info("SilkModPass.INSTANCE.icon() = {}", SilkModPass.INSTANCE.icon());
        LOGGER.info("SilkModPass.INSTANCE.link(ModData.LinkType.HOMEPAGE) = {}",
                SilkModPass.INSTANCE.link(ModBasicData.LinkType.HOMEPAGE));
        LOGGER.info("SilkModPass.INSTANCE.link(ModData.LinkType.SOURCES) = {}",
                SilkModPass.INSTANCE.link(ModBasicData.LinkType.SOURCES));
        LOGGER.info("SilkModPass.INSTANCE.link(ModData.LinkType.ISSUES) = {}",
                SilkModPass.INSTANCE.link(ModBasicData.LinkType.ISSUES));
        LOGGER.info("SilkModPass.INSTANCE.link(ModData.LinkType.COMMUNITY) = {}",
                SilkModPass.INSTANCE.link(ModBasicData.LinkType.COMMUNITY));
        LOGGER.info("SilkModPass.INSTANCE.link(ModData.LinkType.SUPPORT) = {}",
                SilkModPass.INSTANCE.link(ModBasicData.LinkType.SUPPORT));
    }
}
