package mz.genshincode.data

import mz.genshincode.Main
import mz.genshincode.data.asset.AssetBundle
import mz.genshincode.graph.gen.*
import mz.genshincode.graph.gen.event.entity.EventEntityCreate
import org.junit.jupiter.api.Test
import java.io.File

class TestGraph {
    @Test
    fun test() {
        GenshinDataAssets {
            mode = AssetBundle.Mode.OVERLIMIT
            graph {
                on(EventEntityCreate) { event ->
                    val l1 = Label() // 跳转标记
                    If(const(114) lt const(514)) {
                        l1.go() // 跳转
                    }
                    log("Hello")
                    l1.place() // 到这里
                    log("World")
                    For(const(0), { it.get() lt const(10) }, { it.inc() }) { i ->
                        If(i.get() eq const(5)) {
                            Continue
                        }
                        log(i.get())
                    }
                }
            }
        }.save(File("test.gia"))

        Main.println("test.gia")
    }
}
