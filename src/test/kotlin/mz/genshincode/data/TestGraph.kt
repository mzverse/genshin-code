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
                    Switch(event.guid.asString()) {
                        Case("awa")
                            log("awa")
                            Break
                        Case("qwq")
                        Case("pwq")
                            log("qwq")
                            Break
                    }
                    log("finish")
                }
            }
        }.save(File("test.gia"))

        Main.println("test.gia")
    }
}
