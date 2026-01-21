package mz.genshincode.data;

import mz.genshincode.shit.Connection;
import mz.genshincode.shit.Graph;
import mz.genshincode.shit.Node;
import org.junit.jupiter.api.Test;

/**
 * 调试测试
 */
class DebugTest {

    @Test
    void testDisconnectDebug() {
        Graph graph = new Graph("TestGraph");
        Node node1 = graph.addNode("Test.Node1");
        Node node2 = graph.addNode("Test.Node2");

        Connection conn = graph.flow(node1, node2);

        System.out.println("Before disconnect:");
        System.out.println("  node1.getFlowTo().size() = " + node1.getFlowTo().size());
        System.out.println("  node1.getFlowTo() = " + node1.getFlowTo());
        System.out.println("  node2.getFlowFrom().size() = " + node2.getFlowFrom().size());
        System.out.println("  node2.getFlowFrom() = " + node2.getFlowFrom());
        System.out.println("  conn = " + conn);
        System.out.println("  conn.getFromPin() = " + conn.getFromPin());
        System.out.println("  conn.getToPin() = " + conn.getToPin());

        graph.disconnect(conn);

        System.out.println("After disconnect:");
        System.out.println("  node1.getFlowTo().size() = " + node1.getFlowTo().size());
        System.out.println("  node1.getFlowTo() = " + node1.getFlowTo());
        System.out.println("  node2.getFlowFrom().size() = " + node2.getFlowFrom().size());
        System.out.println("  node2.getFlowFrom() = " + node2.getFlowFrom());
    }
}