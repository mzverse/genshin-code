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
                    For(const(0), const(114)) { i ->
                        val loop1 = this
                        For(const(0), const(514)) { j ->
                            log(i)
                            If(j eq const(200)) {
                                loop1.Break
                            } Else {
                                log("false")
                            }
                            log(j)
                        }
                    }
                    Loop {
                    }
                }
            }
        }.save(File("test.gia"))

        Main.println("test.gia")
    }
}
