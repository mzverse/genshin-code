package mz.genshincode.data;

import mz.genshincode.GenshinType;
import mz.genshincode.Main;
import mz.genshincode.data.asset.AssetBundle;
import mz.genshincode.data.asset.AssetsGenerator;
import mz.genshincode.graph.GraphNodes;
import mz.genshincode.graph.NodeGraph;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class TestGraph
{
    @Test
    void test() throws IOException
    {
        NodeGraph graph = new NodeGraph();
        GraphNodes.Trigger2<GenshinType.Entity, GenshinType.Guid> nodeEvent = GraphNodes.Event.Entity.onCreate();
        GraphNodes.Expr1<String, GenshinType.Entity> nodeCast = GraphNodes.Calc.General.Cast.entityToString();
        GraphNodes.Statement1_0<String> nodePrint = GraphNodes.Exec.General.print();
        graph.addNode(nodeEvent);
        graph.addNode(nodeCast);
        graph.addNode(nodePrint);
        graph.connect(nodeEvent, nodeEvent.getFlow(), nodePrint, nodePrint.getFlowIn());
        graph.connect(nodeEvent, nodeEvent.getData0(), nodeCast, nodeCast.getIn0());
        graph.connect(nodeCast, nodeCast.getOut(), nodePrint, nodePrint.getIn0());

        graph.autoLayout();
        AssetsGenerator generator = new AssetsGenerator();
        generator.setMode(AssetBundle.Mode.OVERLIMIT);
        graph.generateAssets(generator);
        generator.toData().save(new File("test.gia"));

        Main.println("test.gia");
    }
}
