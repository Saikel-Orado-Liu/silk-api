## 1.0.4+1.20.6---2024/11/02

### 功能：

- 新增数据组件 "silk-api:adjust_fov_while_hold"
- 新增数据组件 "silk-api:adjust_fov_while_use"
- 新增数据组件 "silk-api:modify_move_while_hold"
- 新增数据组件 "silk-api:modify_move_while_use"
- 新增数据组件 "silk-api:projectile_container"
- 新增数据组件 "silk-api:shoot_projectiles"
- 新增数据组件 "silk-api:ranged_weapon"
- 新增数据组件 "silk-api:inherent_status_effects"
- 新增数据组件 "silk-api:custom_entity_hurt"

### 更改：

- 将 Armor 接口更新为 ArmorHelper 并优化其方法。
- 将 Tool 接口更新为 ToolHelper 并优化其方法。
- 优化了远程武器物品的相关实现使其更易编写。
- 将 Bow 类更名为 BowLikeItem。
- 将 CrossBow 类更名为 CrossbowLikeItem。
- 删除了 RangedExpansion，BowExpansion 与 CrossbowExpansion 这三个远程武器拓展接口。
- 删除物品属性系统并将其替换为新版的物品数组组件。

## 1.0.3---2024/10/19

### 更改：

- 将无法读取更新日志的提示消息更改为“Changelog does not exist!”。

### 修复：

- 修复了模组的内置组资源包无法正确读取资源和覆盖资源的问题。
- 删除了意外存在的 silk-generate 的内置资源包

## 1.0.2---2024/10/14

### 功能：

- 现支持 0.95.3+1.20.4 及以上版本的 Fabric API。

### 更改：

- 减少了在没有存档时创建可升级世界时的生成初始化时间。

### 修复：

- 修复了模组选项卡中更新日志大概率无法被读取的问题。

## 1.0.1---2024/10/06

### 修复：

- 修复了世界升级器在创建第一个世界时，创建线程一直被堵塞的问题。
- 修复了 API 无法获取 night-config:core 中源代码的问题。
- 修复了 API 自带的资源包丢失未被编译到 Jar 发布文件中的问题。
- 修复了 API 在用户环境中无法正确的读取 Minecraft 代码文件的问题。

## 1.0.0---2024/09/25

### 功能：

- 逻辑端
	- 重构了 API 架构，使 API 更加先进与健壮，同时修复多个已知漏洞。

## 0.3.1---2024/03/04

### 修复：

- 逻辑端
	- 修复了 ChunkStorageData 中 getBiome() 返回 null 导致游戏崩溃的问题。
- 客户端
	- 修复了 1.20 ~ 1.20.1 版本中与世界升级相关的屏幕没有背景的问题。
	- 修复了 1.20 ~ 1.20.1 版本中需要升级的世界加载会崩溃的问题。
	- 修复了一个模组中的所有世界生成器共用一个名字的问题。
	- 修复了有概率升级世界使用其他世界生成器升级方法的问题。

## 0.3.0---2024/02/25

### 功能：

- 重构代码为和 Fabric API 一样的多项目结构模组，极大的方便了开发和适配新版本的速度。并且依然保留的对旧版代码的支持。
- 由于 Minecraft 即将迎来 1.20.5 版本，1.20.2 版本将停止更新。

## 0.2.1---2024/01/27

### 修复：

- 逻辑端
	- 修复了 UpgradeChunkGenerator 中由 getCodec() 重名导致的问题。
- 客户端
	- 修复了 UpgradeWarningScreenMixin$ShowScreen 中 getSession() 混入在客户端失败导致的 remap 崩溃问题。

## 0.2.0---2024/01/24

### 功能：

- 逻辑端
	- 添加了“世界升级系统”，可注册世界升级器让玩家能升级世界
	- 添加了区块生成器解编码器注册器（ChunkGeneratorCodecRegistry）
	- 添加了可自定义区块生成器接口（CustomChunkGenerator）
	- 添加了可修改的原版噪声区块生成器（SilkNoiseChunkGenerator）

### 更改：

- 逻辑端
	- 添加了 SilkVanillaBiomeParameters 的方法，使其更易使用。

- 客户端
	- 修复了 WorldPresetCustomButtonCallbackMixin 的按钮显示问题，现在世界预设的自定义按钮可以正确的覆盖默认按钮了。

## 0.1.3---2023/12/16

### 功能：

- 逻辑端
	- 添加了伤害类型数据生成器。

### 更改：

- 逻辑端
	- 修改了 ModDataGeneration 与 SilkWorldGenerator 的方法，使其更易懂.

- 客户端
	- 模组日志使用 Markdown 文本格式，添加标题与列表格式支持，推荐只使用标题与列表格式编写日志。

v0.1.2 2023/12/09

## 0.1.2 漏洞修复

- 客户端
	- 再次修复模组日志读取被 API 日志覆盖的问题，并修改日志路径和日志模板。

v0.1.1 2023/12/08

## 0.1.1 漏洞修复

- 逻辑端
	- 修复 Minecraft 原版物品只能附魔「耐久」的严重问题
- 客户端
	- 修复模组日志读取被 API 日志覆盖的问题

v1.20.2-0.1.0 2023/12/01
1.「丝绸开发库」正式发布！