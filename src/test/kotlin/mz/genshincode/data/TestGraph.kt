package mz.genshincode.data

import mz.genshincode.Main
import mz.genshincode.data.asset.AssetBundle
import mz.genshincode.data.asset.AssetsGenerator
import mz.genshincode.graph.gen.*
import mz.genshincode.graph.gen.event.entity.EventEntityCreate
import org.junit.jupiter.api.Test
import java.io.File

class TestGraph {
    @Test
    fun test() {
        val graph = NodeGraph {
            on(EventEntityCreate) { event ->
                ForInt(const(0), const(114)) { i ->
                    val loop1 = loop
                    ForInt(const(0), const(514)) { j ->
                        If(j eq const(200)) {
                            Break(loop1)
                        }
                    }
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
