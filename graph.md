## 监听事件

使用`on(eventType) { event -> ... }`，其中`eventType`是事件类型，`event`包含事件的各参数

```kotlin
on(EventEntityCreate) { event -> // 监听实体创建事件
    // do sth.
    log(event.entity) // 打印事件实体到日志
}
```

或者直接调用`eventType.subscribe`，如果你喜欢的话

```kotlin
EventEntityCreate.subscribe { event -> // 监听实体创建事件
    // do sth.
    log(event.entity) // 打印事件实体到日志
}
```

## 作用域

每次只有一个触发器（事件）会执行，期间运行过的节点才有值，否则为默认值（全0）

此周期结束后所有节点的值（包括局部变量）都会清空，请勿跨事件直接读取值

## 常量

通过`const(v)`创建
```kotlin
log(const("Hello World")) // 打印到日志
```

## 局部变量

> [!IMPORTANT]
> 请注意，局部变量的作用域是当前触发器（事件）

使用`local(init)`创建，其中`init`是初始值表达式。当然你需要使用*Kotlin*级别的`val`进行标记

```kotlin
val myLocal = local(const(0))
```

局部变量也是静态强类型的，取决于你调用的`local`方法（`init`参数）

使用`get`得到变量的值，使用`set`设置

```kotlin
val myLocal = local(const(114)) // 创建局部变量
log(myLocal.get()) // 114
myLocal.set(const(514)) // 设置局部变量的值
log(myLocal.get()) // 514
```

## 控制流

> [!NOTE]
> 为避免和关键字冲突，控制流标识符均使用大驼峰

条件控制使用`If`，后可接`Else`
```kotlin
If(condition) {
    // do then
}
```
```kotlin
If(condition) {
    // do then
} Else {
    // do else
}
```

### 有限循环

使用`For`遍历左闭右开的整数区间；闭区间则使用`ForClosed`，这直接对应“有限循环”节点

```kotlin
val begin = const(114) // 包含
val end = const(514) // 不包含
For(begin, end) { i -> // i 是循环变量
    log(i) // 打印到日志
}
```
```kotlin
val begin = const(114)
val end = const(514) // 这次都包含
ForClosed(begin, end) { i -> // 因为是ForClosed
    log(i)
}
```

退出循环使用`Break`，默认退出所在最内层的循环————就像其它编程语言一样

```kotlin
For(const(114), const(514)) { i ->
    log(i)
    If(i eq const(200)) {
        Break
    }
}
```

退出外层循环：需要在外层循环标记`this`，退出时使用其`Break`

```kotlin
For(const(0), const(114)) { i ->
    val outer = this // 标记外层循环
    For(const(0), const(514)) { j ->
        If(j eq const(200)) {
            outer.Break // 退出外层循环
        }
    }
}
```

或使用*Kotlin*的标记

```kotlin
For(const(0), const(114)) outer@{ i -> // 在外层标记
    For(const(0), const(514)) { j ->
        If(j eq const(200)) {
            this@outer.Break // 退出外层循环
        }
    }
}
```

类似地，使用`Continue`跳到下一轮循环

```kotlin
For(const(0), const(514)) { i ->
    If(i eq const(114)) {
        Continue // 等于114就跳过
    }
    log(i) // 不会打印114
}
```

### 无限循环

使用`Loop`进行无限循环（其实是42亿次，因为官方不允许真正的死循环）

```kotlin
Loop {
    log("endless")
}
```

在此基础上加上条件得到“while循环”，使用`While({ con }) { /* do sth. */ }`

示例：做一个C风格的“for循环”

```kotlin
val i = local(const(0)) // int i = 0
While({ i.get() lt const(10) }) { // while(i < 10)
    log(i.get())
    i++ // Idea会警告NeverRead，即使无副作用这里也不该警告的，这完全是Idea的问题
}
```

### for循环

在While的基础上增加更新语句即得“for循环”：
```kotlin
var i = local(const(0))
For({ i.get() lt const(10) }, { i++ }) {
    log(i.get())
}
```

相比传统的C-Style的三段式for循环，由于作用域限制，init语句被提升到循环外了

没事，我们还有自动创建变量的版本，只需把初始值表达式添加到首个参数：
```kotlin
For(const(0), { it.get() lt const(10) }, { it.inc() }) {
    If(it.get() eq const(5)) {
        Continue
    }
    log(it.get())
}
```
后续通过lambda的参数访问它，当然它默认叫`it`，不喜欢的话你也可以手动指定：
```kotlin
For(
    const(0), // 初始值表达式，自动创建局部变量
    { it.get() lt const(10) }, // 循环条件
    { it.inc() } // 更新，此处不能使用i++，因为kotlin的形参不可变
) { i -> // 显式声明形参i
    If(i.get() eq const(5)) {
        Continue
    }
    log(i.get())
}
```

特别地，for循环的`Continue`不会跳过update语句，就像其它编程语言一样

### 自定义跳转（实验性）

> [!IMPORTANT]
> 注意，受节点图限制，你只能向后跳转而不能反之（不允许回路）
>  
> 你也不能跳入或跳出循环，因为循环是特殊的控制流

在kotlin的公共作用域创建`Label`，然后`go`和`place`

```kotlin
val l1 = Label() // 跳转标记
If(const(114) lt const(514)) {
    l1.go() // 跳转
}
log("Hello")
l1.place() // 到这里
log("World")
```


## 布尔（比较、关系操作符、条件）

*Kotlin*可不支持这样重载运算符，所以我们使用中缀函数，有点像*Bash*或*PowerShell*

**相同类型**的表达式才能进行比较：**大小**的比较支持**整数**和**浮点数**表达式，**相等**比较支持大部分**非列表类型**

```kotlin
val a = const(114)
val b = const(514)
a eq b // == equal
a ne b // != not equal
a lt b // <  less than
a gt b // >  greater than
a le b // <= less than or equal
a ge b // >= greater than or equal
```

完整示例：
```kotlin
on(EventEntityCreate) { event -> // 监听实体创建事件
    If(event.guid eq guid(114514L)) { // 判断guid是否等于114514L
        log("Congratulation!!!")
    }
}
```

布尔表达式支持逻辑非：`!expr`（`ne`就是这样实现的）

```kotlin
val a = const(114)
val b = const(514)
val expr = a eq b
If(!expr) {
    log("not")
}
```

## 运算

目前支持整数和浮点数的加和减，以及布尔值的逻辑非
```kotlin
val a = const(114)
val b = const(514)
log(a + b) // 628
log(a - b) // -400
```

特别地，**整型局部变量**支持自增`inc()`和自减`dec()`
```kotlin
val i = local(const(0))
i.set(i.get() + const(114))
log(i.get()) // 114
i.inc()
log(i.get()) // 115
```
对于自增，操作符只允许`++i`或独立语句，自减同理，一般只建议写独立语句
```kotlin
var i = local(const(114)) // 需要var，即使它实际不变
log((++i).get()) // 自增然后打印
log(i++.get()) // 逻辑错误！！！实际上只支持++i，不要这样写！
++i // 有时会警告变量不再使用——这是Idea的问题——此时还是用i.inc()吧
i++ // 单语句用哪个都一样
```

## 类型转换

使用"as"开头的方法，如`asString`
```kotlin
val a = const(114514)
log(a.asString()) // 114514
log(a) // 其实log有自动asString()的版本
```