package mz.genshincode.data

import mz.genshincode.Main
import mz.genshincode.data.asset.AssetBundle
import mz.genshincode.data.asset.AssetsGenerator
import mz.genshincode.graph.GraphNodes
import mz.genshincode.graph.NodeGraph
import org.junit.jupiter.api.Test
import java.io.File

class TestGraph {
    @Test
    fun test() {
        val graph = NodeGraph()
        val nodeEvent = GraphNodes.Event.Entity.onCreate()
        val nodeForInt = GraphNodes.Exec.forInt()
        val nodeCast = GraphNodes.Calc.Cast.intToString()
        val nodePrint = GraphNodes.Exec.print()
        graph.addNode(nodeEvent)
        graph.addNode(nodeForInt)
        graph.addNode(nodeCast)
        graph.addNode(nodePrint)

        graph.connect(nodeEvent, nodeEvent.flow, nodeForInt, nodeForInt.flowIn)
        graph.connect(nodeForInt, nodeForInt.flowBody, nodePrint, nodePrint.flowIn)

        graph.connect(nodeForInt, nodeForInt.outValue, nodeCast, nodeCast.in0)
        graph.connect(nodeCast, nodeCast.out, nodePrint, nodePrint.in0)

        nodeForInt.inBegin.setValue(114)
        nodeForInt.inEnd.setValue(514)

        graph.autoLayout()
        val generator = AssetsGenerator()
        generator.setMode(AssetBundle.Mode.OVERLIMIT)
        graph.generateAssets(generator)
        generator.toData().save(File("test.gia"))

        Main.println("test.gia")
    }
}
