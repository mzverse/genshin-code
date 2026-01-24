package mz.genshincode.data

import mz.genshincode.Main
import mz.genshincode.data.asset.AssetBundle
import mz.genshincode.data.asset.AssetsGenerator
import mz.genshincode.graph.GraphNodes
import mz.genshincode.graph.NodeGraph
import mz.genshincode.graph.dsl.NodeGraph
import mz.genshincode.graph.dsl.asString
import mz.genshincode.graph.dsl.event.entity.EventEntityCreate
import mz.genshincode.graph.dsl.log
import org.junit.jupiter.api.Test
import java.io.File

class TestGraph {
    @Test
    fun test() {
        val graph = NodeGraph {
            on(EventEntityCreate) { event ->
                log(event.entity.asString())
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
