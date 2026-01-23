# GenshinCode 项目文档

## 项目概述

GenshinCode 是一个基于 Java/Kotlin 的代码生成项目，旨在使用编程方式生成原神千星奇域（Miliastra Wonderland）的节点图数据。

**核心目标：**
- 使用代码（Java 或 Kotlin）替代图形化节点编辑器，以更高效、可维护的方式创建节点图
- 提供更高阶的抽象来生成节点图（不可逆过程）
- 避免使用图形化节点图编辑器（对程序员来说效率低下且难以维护）

**技术栈：**
- **语言：** Java 8 / Kotlin 2.2.0
- **构建工具：** Gradle 8.x（使用 Kotlin DSL）
- **序列化：** Protocol Buffers 3.25.1
- **测试框架：** JUnit 5.10.0

**项目结构：**
```
GenshinCode/
├── src/
│   ├── main/
│   │   ├── java/mz/genshincode/
│   │   │   ├── Main.java                   # 主入口类
│   │   │   ├── data/
│   │   │   │   ├── GenshinData.java        # 抽象基类，定义数据保存格式
│   │   │   │   ├── GenshinDataAssets.java  # AssetBundle 数据实现
│   │   │   │   └── asset/
│   │   │   │       └── AssetsGenerator.java # 资产生成器
│   │   │   └── graph/
│   │   │       ├── GraphNode.java          # 图节点类
│   │   │       └── NodeGraph.java          # 节点图类
│   │   └── proto/mz/genshincode/data/
│   │       └── asset.proto                 # Protocol Buffers 数据结构定义
│   └── test/
├── build.gradle.kts                        # Gradle 构建配置
├── settings.gradle.kts                     # Gradle 项目设置
├── gradlew / gradlew.bat                   # Gradle Wrapper 脚本
└── README.md                               # 项目说明文件
```

## 构建和运行

### 前置要求
- Java Development Kit (JDK) 8 或更高版本
- Gradle（项目包含 Gradle Wrapper，无需单独安装）

### 构建命令

```bash
# 编译项目（包括生成 Protocol Buffers 代码）
.\gradlew.bat build

# 清理构建产物
.\gradlew.bat clean

# 编译并运行测试
.\gradlew.bat test

# 生成 Protocol Buffers 代码
.\gradlew.bat generateProto
```

### 运行项目

由于这是一个库项目，具体运行方式取决于使用场景。通常需要：

1. 创建继承自 `GenshinData` 的子类
2. 实现 `encode()` 和 `getType()` 方法
3. 调用 `save(File)` 方法保存到 `.gia` 文件

## 开发约定

### 代码风格
- **包命名：** 使用 `mz.genshincode` 作为基础包名
- **类命名：** 使用 PascalCase（大驼峰）
- **方法命名：** 使用 camelCase（小驼峰）
- **常量命名：** 使用 UPPER_SNAKE_CASE

### Protocol Buffers 约定

**核心数据结构：**
- `AssetBundle`：顶层资产容器，包含所有生成的资源
- `Asset`：资源物理容器，定义资源的类型和载荷
- `Identifier`：资源标识符系统，包含来源域、服务域和类型域

**资源类型分类：**
- `Asset.Type`：业务类型枚举（OBJECT、CREATION、ENTITY、SKILL、NODE_GRAPH 等）
- `ServerTypeId`：服务器端逻辑类型 ID（用于后端逻辑运算）
- `ClientTypeId`：客户端表现类型 ID（用于 UI 渲染）

**节点图系统：**
- `NodeGraphData`：逻辑容器，包含节点实例、接口映射、变量池等
- `NodeInterface`：复合节点接口定义，定义对外签名
- `NodeInstance`：节点实例，包含外壳定义、内核实现和引脚参数
- `Pin`：引脚实例，定义引脚签名、值和连接关系

### 文件格式

生成的 `.gia` 文件格式由 `GenshinData.save()` 方法定义：

```
[总长度-4字节][schema_version-4字节][head_tag-4字节][type-4字节][data_length-4字节][data_data...][tail_tag-4字节]
```

- **head_tag:** `0x0326`
- **tail_tag:** `0x0679`
- **schema_version:** `1`
- **type:** 资源类型（AssetBundle 为 4）

### 引用项目

本项目的 `asset.proto` 数据结构参考自：
- [Genshin-Impact-Miliastra-Wonderland-Code-Node-Editor-Pack](https://github.com/Wu-Yijun/Genshin-Impact-Miliastra-Wonderland-Code-Node-Editor-Pack)

如果只想以 TypeScript 方式无损编辑节点图，可以参考上述项目。

## 关键概念

### 资源标识符（Identifier）

资源标识符由三个域组成：

1. **Source（来源域）：**
   - `USER_DEFINED` (10000)：用户资产
   - `SYSTEM_DEFINED` (10001)：系统内置

2. **Category（服务域）：**
   - `SERVER_BASIC` (20000)：后端基础
   - `SERVER_STATUS` (20003)：后端状态
   - `CLIENT_FILTER` (20001)：前端过滤器
   - `CLIENT_SKILL` (20002)：前端技能

3. **AssetKind（类型域）：**
   - `CUSTOM_GRAPH` (21001)：普通节点图
   - `COMPOSITE_GRAPH` (21002)：复合图
   - `SYS_CALL_STUB` (22000)：系统调用存根

### 节点图类型

- **ENTITY_NODE_GRAPH**：实体节点图
- **SKILL_NODE_GRAPH**：技能节点图
- **BOOLEAN_FILTER_GRAPH**：布尔过滤器节点图
- **INTEGER_FILTER_GRAPH**：整数过滤器节点图
- **ITEM_NODE_GRAPH**：道具节点图

### 数据类型系统

项目使用两套类型系统：

**服务器端类型（ServerTypeId）：**
- 基础类型：S_ENTITY(1), S_GUID(2), S_INT(3), S_BOOL(4), S_FLOAT(5), S_STRING(6), S_VECTOR(12)
- 列表类型：S_GUID_LIST(7), S_INT_LIST(8), S_BOOL_LIST(9), S_FLOAT_LIST(10), S_STRING_LIST(11), S_ENTITY_LIST(13)
- 复杂类型：S_STRUCT(25), S_STRUCT_LIST(26), S_DICT(27)

**客户端类型（ClientTypeId）：**
- 采用更紧凑的编号方式（List = Base + 1）
- 不支持 Map/Struct 类型

## 核心类说明

### GenshinData.java
抽象基类，定义了 `.gia` 文件的基本格式和保存方法。包含以下类型：
- PROJECT(1)
- LEVEL(2)
- ASSETS(3) 
- RUNTIME(4)

### GenshinDataAssets.java
资产数据类，用于将 AssetBundle 对象保存为 `.gia` 文件。

### AssetsGenerator.java
资产生成器，负责生成 AssetBundle 并分配 GUID。

### NodeGraph.java 和 GraphNode.java
节点图相关类，用于构建节点图结构。

## 测试

项目使用 JUnit 5 进行测试。测试类应放置在 `src/test/java/` 目录下。

运行测试：
```bash
.\gradlew.bat test
```

查看测试报告：
```bash
.\gradlew.bat test --info
```

## 版本信息

- **引擎版本：** 6.3.0
- **Protocol Buffers 版本：** 3.25.1
- **项目版本：** 1.0-SNAPSHOT

## 注意事项

1. 本项目生成的节点图数据是**不可逆的**，无法反向转换为代码
2. Protocol Buffers 定义文件（`asset.proto`）是数据结构的核心，修改后需要重新生成代码
3. 节点图的生成过程是单向的：代码 → 节点图数据
4. 系统内置节点使用 `SYSTEM_DEFINED` 来源，用户自定义节点使用 `USER_DEFINED` 来源

## TODO

- [ ] 完善 NodeGraph 的 generateAssets 方法
- [ ] 添加更多示例代码
- [ ] 完善文档
- [ ] 添加单元测试
- [ ] 提供更高阶的 API 抽象