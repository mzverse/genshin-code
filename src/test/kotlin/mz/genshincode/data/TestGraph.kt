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
                    val i = local(const(0))
                    While({ i.get() lt const(10) }) {
                        log(i.get())
                        i.increment()
                    }
                }
            }
        }.save(File("test.gia"))

        Main.println("test.gia")
    }
}
