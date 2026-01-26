本项目旨在使用代码生成原神千星奇域的节点图(等)数据

节点图的编辑方式也许确实对新手比较友好，但对程序员来说就是一坨大的。最低效且难以维护的开发方式之一

如果你只想以代码(TypeScript)的方式无损编辑节点图，可以参照另一个项目：[Genshin-Impact-Miliastra-Wonderland-Code-Node-Editor-Pack](https://github.com/Wu-Yijun/Genshin-Impact-Miliastra-Wonderland-Code-Node-Editor-Pack)
（本项目的gia.proto数据结构也是来源于此项目）

但我们不满足于此，本项目使用更高阶的方式生成节点图（不可逆），使用的语言是Java或Kotlin

因为我说白了这节点图的架构本身就是一坨屎，你直接用代码表示也无济于事，必须采用更优雅的封装

### 项目正在积极开发中，尽情期待

## 层级

1. proto格式和文件级别的封装（`mz.genshincode.data`）

    这部分代码大部分来自[Genshin-Impact-Miliastra-Wonderland-Code-Node-Editor-Pack](https://github.com/Wu-Yijun/Genshin-Impact-Miliastra-Wonderland-Code-Node-Editor-Pack)

2. Java面向对象的封装，但大致保留原始结构

3. Kotlin dsl生成节点图

4. TODO: 将Java编译为节点图

## Kotlin DSL

示例
```kotlin
GenshinDataAssets {
    mode = AssetBundle.Mode.OVERLIMIT
    graph {
        on(EventEntityCreate) { event ->
            If(event.guid eq guid(114514L)) {
                log(event.entity)
            } Else {
                log(event.guid)
            }
        }
    }
}.save(File("test.gia"))
```

使用`GenshinDataAssets {}`构造资源，其中使用`graph {}`构造节点图，最后`save`到文件

节点图详见[graph.md](graph.md)

开发交流：
- QQ: 763416502
- Discord: https://discord.gg/Ssd4FTSKuq