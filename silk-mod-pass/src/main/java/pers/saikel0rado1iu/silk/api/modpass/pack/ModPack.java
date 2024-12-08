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

package pers.saikel0rado1iu.silk.api.modpass.pack;

import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.ResourcePackPosition;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;
import pers.saikel0rado1iu.silk.api.event.registry.RegisterModResourcePackCallback;
import pers.saikel0rado1iu.silk.api.modpass.ModBasicData;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <h2>模组包</h2>
 * 模组包的基础数据
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.2.4
 */
public sealed interface ModPack extends ModPass permits ModPack.Group, ModPack.Simple,
                                                        ModDataPack, ModResourcesPack {
    /** 激活信息 */
    ResourcePackPosition ACTIVATION_INFO = new ResourcePackPosition(true,
            ResourcePackProfile.InsertionPosition.TOP, false);

    /**
     * 包的根目录，数据包目录为 {@code "resourcepacks/packRoot()/..."}
     *
     * @return 根目录名称
     */
    String packRoot();

    /**
     * 包激活类型，参考 {@link ResourcePackActivationType}
     *
     * @return 数据包激活类型
     */
    ResourcePackActivationType type();

    /**
     * 包的唯一标识符
     *
     * @return 标识符
     */
    Identifier id();

    /**
     * 包名称文本
     *
     * @return 名称文本
     */
    Text name();

    /**
     * 注册包
     *
     * @return 是否成功注册
     */
    boolean registry();

    /**
     * <h2>简单资源包</h2>
     * 资源包的简单实现
     *
     * @author <a href="https://github.com/Saikel-Orado-Liu">
     *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
     *         </a>
     * @since 1.2.4
     */
    sealed abstract class Simple implements ModPack
            permits Group, ModDataPack.Simple, ModResourcesPack.Simple {
        protected final String packRoot;
        protected final ResourcePackActivationType type;
        protected final ModPass modPass;

        /**
         * @param packRoot 包的根目录
         * @param type     包激活类型
         * @param modPass  所需的模组通
         */
        protected Simple(String packRoot, ResourcePackActivationType type, ModPass modPass) {
            this.packRoot = packRoot;
            this.type = type;
            this.modPass = modPass;
        }

        @Override
        public String packRoot() {
            return packRoot;
        }

        @Override
        public ResourcePackActivationType type() {
            return type;
        }

        @Override
        public boolean registry() {
            return ResourceManagerHelper.registerBuiltinResourcePack(id(), modData().mod(), name(), type());
        }

        @Override
        public ModBasicData modData() {
            return modPass.modData();
        }
    }

    /**
     * <h2>组资源包</h2>
     * 组资源包会把所有模组容器内统一路径下的包内容识别为一个资源包
     *
     * @author <a href="https://github.com/Saikel-Orado-Liu">
     *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
     *         </a>
     * @since 1.2.4
     */
    sealed abstract class Group extends Simple implements ModPack
            permits ModDataPack.Group, ModResourcesPack.Group {
        protected final String nameKey;
        protected final String descKey;
        protected final List<String> orderList;
        protected final ResourcePackInfo info;
        protected final ResourceType resourceType;

        /**
         * @param packRoot     包的根目录
         * @param nameKey      名称翻译键
         * @param descKey      描述翻译键
         * @param orderList    排序列表
         * @param type         包激活类型
         * @param modPass      所需的模组数据
         * @param resourceType 资源包类型
         */
        protected Group(String packRoot, String nameKey, String descKey, List<String> orderList,
                        ResourcePackActivationType type, ModPass modPass,
                        ResourceType resourceType) {
            super(packRoot, type, modPass);
            this.nameKey = nameKey;
            this.descKey = descKey;
            this.orderList = orderList;
            this.resourceType = resourceType;
            this.info = new ResourcePackInfo(modData().id(), Text.translatable(nameKey),
                    new GroupResourcePackSource(modData()), Optional.empty());
        }

        @Override
        @SuppressWarnings("UnstableApiUsage")
        public boolean registry() {
            AtomicBoolean flag = new AtomicBoolean(false);
            RegisterModResourcePackCallback.EVENT.register((type, consumer) -> {
                if (!resourceType.equals(type)) {
                    return;
                }
                final List<ModResourcePack> defaults = new ArrayList<>();
                final List<ModResourcePack> packs = new ArrayList<>();
                ModResourcePackUtil.appendModResourcePacks(defaults, type, null);
                ModResourcePackUtil.appendModResourcePacks(packs, type,
                        "resourcepacks/" + id().getPath());
                packs.addAll(defaults);
                // 现在排序列表中添加默认模组资源包，然后再添加内置资源包
                final List<String> ordered = Lists.newArrayList();
                defaults.forEach(pack -> ordered.add(pack.getId()));
                ordered.addAll(orderList
                        .stream()
                        .map(name -> name + "_resourcepacks/" + id().getPath())
                        .toList());
                if (packs.isEmpty()) {
                    return;
                }
                ResourcePackProfile profile = ResourcePackProfile.create(info,
                        new GroupResourcePack.Factory(type, packs, ordered, this),
                        type, ACTIVATION_INFO);
                if (profile == null) {
                    return;
                }
                consumer.accept(profile);
                flag.set(true);
            });
            return flag.get();
        }
    }
}
