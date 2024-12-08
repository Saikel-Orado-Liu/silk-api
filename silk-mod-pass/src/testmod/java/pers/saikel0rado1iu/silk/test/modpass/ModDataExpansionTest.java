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

import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import pers.saikel0rado1iu.silk.api.modpass.ModExtendedData;
import pers.saikel0rado1iu.silk.api.modpass.pack.ModDataPack;
import pers.saikel0rado1iu.silk.api.modpass.pack.ModResourcesPack;
import pers.saikel0rado1iu.silk.impl.SilkApiBase;
import pers.saikel0rado1iu.silk.impl.SilkModPass;

/**
 * Test {@link ModExtendedData}
 */
public interface ModDataExpansionTest {
    /**
     * 日志
     */
    Logger LOGGER = LoggerFactory.getLogger("SilkModPass/TestingModExtendedData");

    /**
     * 测试
     */
    static void test() {
        LOGGER.info("SilkModPass.INSTANCE.themeColor() = {}",
                SilkModPass.INSTANCE.themeColor());
        LOGGER.info("SilkModPass.INSTANCE.i18nName() = {}",
                SilkModPass.INSTANCE.i18nName());
        LOGGER.info("SilkModPass.INSTANCE.i18nSummary() = {}",
                SilkModPass.INSTANCE.i18nSummary());
        LOGGER.info("SilkModPass.INSTANCE.i18nDescription() = {}",
                SilkModPass.INSTANCE.i18nDescription());
        SilkModPass.INSTANCE.writeFormatLog(Level.DEBUG, SilkApiBase.INSTANCE,
                "SilkApiBase/TestingModExtendedData");
        SilkModPass.INSTANCE.writeFormatLog(Level.INFO, SilkApiBase.INSTANCE,
                "SilkApiBase/TestingModExtendedData");
        SilkModPass.INSTANCE.writeFormatLog(Level.WARN, SilkApiBase.INSTANCE,
                "SilkApiBase/TestingModExtendedData");
        SilkModPass.INSTANCE.writeFormatLog(Level.ERROR, SilkApiBase.INSTANCE,
                "SilkApiBase/TestingModExtendedData");
        SilkModPass.INSTANCE.writeFormatLog(Level.TRACE, SilkApiBase.INSTANCE,
                "SilkApiBase/TestingModExtendedData");
        LOGGER.info("DataPack.create() = {}", ModDataPack.createSimple("pack",
                ResourcePackActivationType.ALWAYS_ENABLED, SilkModPass.INSTANCE));
        LOGGER.info("DataPack.getDescKey() = {}",
                ModDataPack.getDescKey(SilkModPass.INSTANCE));
        LOGGER.info("ResourcePack.create() = {}", ModResourcesPack.createSimple("pack",
                ResourcePackActivationType.ALWAYS_ENABLED, SilkModPass.INSTANCE));
        LOGGER.info("ResourcePack.getDescKey() = {}",
                ModResourcesPack.getDescKey(SilkModPass.INSTANCE));
        LOGGER.info("SilkModPass.INSTANCE.email() = {}", SilkModPass.INSTANCE.email());
        LOGGER.info("SilkModPass.INSTANCE.irc() = {}", SilkModPass.INSTANCE.irc());
        LOGGER.info("SilkModPass.INSTANCE.contributors() = {}",
                SilkModPass.INSTANCE.contributors());
        LOGGER.info("SilkModPass.INSTANCE.nested() = {}", SilkModPass.INSTANCE.nested());
        LOGGER.info("SilkModPass.INSTANCE.depends() = {}",
                SilkModPass.INSTANCE.depends());
        LOGGER.info("SilkModPass.INSTANCE.recommends() = {}",
                SilkModPass.INSTANCE.recommends());
        LOGGER.info("SilkModPass.INSTANCE.suggests() = {}",
                SilkModPass.INSTANCE.suggests());
        LOGGER.info("SilkModPass.INSTANCE.breaks() = {}", SilkModPass.INSTANCE.breaks());
        LOGGER.info("SilkModPass.INSTANCE.conflicts() = {}",
                SilkModPass.INSTANCE.conflicts());
        LOGGER.info("SilkModPass.INSTANCE.jar() = {}", SilkModPass.INSTANCE.jar());
        LOGGER.info("SilkModPass.INSTANCE.jarPath() = {}",
                SilkModPass.INSTANCE.jarPath());
        LOGGER.info("SilkModPass.INSTANCE.jarName() = {}",
                SilkModPass.INSTANCE.jarName());
    }
}
