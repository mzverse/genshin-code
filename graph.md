## 监听事件

```kotlin
on(EventType) { event ->
    // do sth.
}
```
其中`EventType`是事件类型，`event`中包含事件提供的各参数（表达式）

## 常量

通过`const(v)`创建
```kotlin
log(const("Hello World")) // 打印到日志
```

## 控制流

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
val begin = const(114)
val end = const(514)
For(begin, end) { i -> // i 是循环变量
    log(i) // 打印到日志
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
    val loop1 = this // 标记外层循环
    For(const(0), const(514)) { j ->
        If(j eq const(200)) {
            loop1.Break // 退出外层循环
        }
    }
}
```