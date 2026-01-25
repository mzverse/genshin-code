package mz.genshincode.data

import mz.genshincode.Main
import mz.genshincode.data.asset.AssetBundle
import mz.genshincode.data.asset.AssetsGenerator
import mz.genshincode.graph.gen.If
import mz.genshincode.graph.gen.NodeGraph
import mz.genshincode.graph.gen.asString
import mz.genshincode.graph.gen.const
import mz.genshincode.graph.gen.eq
import mz.genshincode.graph.gen.event.entity.EventEntityCreate
import mz.genshincode.graph.gen.guid
import mz.genshincode.graph.gen.log
import mz.genshincode.graph.gen.on
import org.junit.jupiter.api.Test
import java.io.File

class TestGraph {
    @Test
    fun test() {
        val graph = NodeGraph {
            on(EventEntityCreate) { event ->
                If(event.guid eq guid(114514L)) {
                    log(event.entity.asString())
                } Else {
                    log(event.guid.asString())
                }
            }
        }

        graph.autoLayout()
        val generator = AssetsGenerator()
        generator.setMode(AssetBundle.Mode.OVERLIMIT)
        graph.generateAssets(generator)
        generator.toData().save(File("test.gia"))

        Main.println("test.gia")
    }
}
