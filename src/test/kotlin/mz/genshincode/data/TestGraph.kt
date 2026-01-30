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
                    log(const(-8) shr const(2))
                }
            }
        }.save(File("test.gia"))

        Main.println("test.gia")
    }
}
