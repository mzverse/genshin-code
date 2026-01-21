本项目旨在使用代码生成原神千星奇域的节点图(等)数据

节点图的编辑方式也许确实对新手比较友好，但对程序员来说就是一坨大的。最低效且难以维护的开发方式之一

如果你只想以代码(TypeScript)的方式无损编辑节点图，可以参照另一个项目：[Genshin-Impact-Miliastra-Wonderland-Code-Node-Editor-Pack](https://github.com/Wu-Yijun/Genshin-Impact-Miliastra-Wonderland-Code-Node-Editor-Pack)

（本项目的gia.proto数据结构也是来源于此项目）

但我们不满足于此，本项目使用更高阶的方式生成节点图（不可逆），使用的语言是Java或Kotlin

# 文件结构

千星奇域中导出的文件格式均为`*.gi?`，使用简单二进制格式封装其中的proto msg，更进一步的解析全部使用proto

资产文件为`*.gia`格式(_Genshin Impact Assets_)，其中包含导出的所有资产及其引用（依赖）的所有资产，暂时我们只考虑这种格式

整个msg称为`AssetBundle`，包含基本信息以及若干资产。

每个资产在此文件中有唯一GUID，一般为导出时资产的GUID，只用于表示资产之引用（依赖）关系，导入后实际的GUID可能不同

## AssetBundle

asset
: 若干个`Asset`，名义上保存的资产

dependency
: 若干个`Asset`，所有被依赖的资产，例如在节点图中使用了其它的复合节点图

mode
: `Mode`表示关卡模式，经典模式或超限模式，默认为超限模式

## Asset

表示一个资产

id
: `Identifier` 资产的id

reference
: TODO

name
: 资产名称

type
: 资产类型

payload
: `oneof` 取决于类型。TODO

## Identifier

资产的标识符

source
: 资产来源（系统内置 / 用户自定义）

category
: 一级作用域。TODO

kind
: 二级作用域。TODO

guid
: 在此作用域中的唯一id，`0L`是缺省值（默认）

runtime_id
: TODO，意义不明
