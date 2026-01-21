package mz.genshincode.data;

import mz.genshincode.shit.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;

/**
 * 图测试
 */
class GraphTest {

    @Test
    void testCreateGraph() {
        Graph graph = new Graph("TestGraph");
        assertEquals("TestGraph", graph.getGraphName());
        assertEquals(ResourceClass.ENTITY_NODE_GRAPH, graph.getSystem());
        assertTrue(graph.getGraphId() > 0);
        assertTrue(graph.getNodes().isEmpty());
    }

    @Test
    void testAddNode() {
        Graph graph = new Graph("TestGraph");
        Node node1 = graph.addNode("Test.Node1");
        Node node2 = graph.addNode("Test.Node2");

        assertEquals(2, graph.getNodes().size());
        assertEquals(0, node1.getNodeIndex());
        assertEquals(1, node2.getNodeIndex());
        assertEquals("Test.Node1", node1.getIdentifier());
        assertEquals("Test.Node2", node2.getIdentifier());
    }

    @Test
    void testNodePosition() {
        Graph graph = new Graph("TestGraph");
        Node node = graph.addNode("Test.Node");

        node.setPosition(100.5f, 200.5f);
        assertEquals(100.5f, node.getX());
        assertEquals(200.5f, node.getY());
    }

    @Test
    void testNodeComment() {
        Graph graph = new Graph("TestGraph");
        Node node = graph.addNode("Test.Node");

        node.setComment("This is a test comment");
        assertEquals("This is a test comment", node.getComment());
    }

    @Test
    void testPinValues() {
        Graph graph = new Graph("TestGraph");
        Node node = graph.addNode("Test.Node");

        TypedValue intValue = new TypedValue(
                NodeType.base(NodeType.BaseType.INT),
                42
        );
        node.setPinValue("input", intValue);

        TypedValue retrievedValue = node.getPinValues().get("input");
        assertNotNull(retrievedValue);
        assertEquals(42, retrievedValue.getInt());
    }

    @Test
    void testGraphComment() {
        Graph graph = new Graph("TestGraph");

        GraphComment comment = graph.addComment("This is a floating comment", 50.0f, 100.0f);

        assertEquals(1, graph.getComments().size());
        assertEquals("This is a floating comment", comment.getContent());
        assertEquals(50.0f, comment.getX());
        assertEquals(100.0f, comment.getY());
    }

    @Test
    void testGraphVariable() {
        Graph graph = new Graph("TestGraph");

        GraphVariable var = graph.addGraphVariable(
                "testVar",
                NodeType.base(NodeType.BaseType.INT),
                100,
                true
        );

        assertEquals(1, graph.getGraphVariables().size());
        assertEquals("testVar", var.getName());
        assertEquals(100, var.getValue().getInt());
        assertTrue(var.isExposed());

        GraphVariable retrievedVar = graph.getGraphVariable("testVar");
        assertNotNull(retrievedVar);
        assertEquals(var, retrievedVar);
    }

    @Test
    void testFlowConnection() {
        Graph graph = new Graph("TestGraph");
        Node node1 = graph.addNode("Test.Node1");
        Node node2 = graph.addNode("Test.Node2");

        Connection conn = graph.flow(node1, node2);

        assertNotNull(conn);
        assertEquals(node1, conn.getFromNode());
        assertEquals(node2, conn.getToNode());
        assertEquals(Connection.Type.FLOW, conn.getType());

        assertEquals(1, node1.getFlowTo().size());
        assertEquals(1, node2.getFlowFrom().size());
    }

    @Test
    void testDataConnection() {
        Graph graph = new Graph("TestGraph");
        Node node1 = graph.addNode("Test.Node1");
        Node node2 = graph.addNode("Test.Node2");

        Connection conn = graph.connect(node1, node2, "output", "input");

        assertNotNull(conn);
        assertEquals(node1, conn.getFromNode());
        assertEquals(node2, conn.getToNode());
        assertEquals(Connection.Type.DATA, conn.getType());

        assertEquals(1, node1.getDataTo().size());
        assertEquals(1, node2.getDataFrom().size());
    }

    @Test
    void testDisconnect() {
        Graph graph = new Graph("TestGraph");
        Node node1 = graph.addNode("Test.Node1");
        Node node2 = graph.addNode("Test.Node2");

        Connection conn = graph.flow(node1, node2);
        assertNotNull(conn);

        graph.disconnect(conn);

        // 检查 flowTo 是否为空
        assertEquals(0, node1.getFlowTo().size());

        // 检查 flowFrom 中的所有 Set 是否为空
        assertTrue(node2.getFlowFrom().values().stream().allMatch(Set::isEmpty));
    }

    @Test
    void testGetAllConnections() {
        Graph graph = new Graph("TestGraph");
        Node node1 = graph.addNode("Test.Node1");
        Node node2 = graph.addNode("Test.Node2");
        Node node3 = graph.addNode("Test.Node3");

        graph.flow(node1, node2);
        graph.connect(node1, node3, "output", "input");

        assertEquals(1, graph.getFlowConnections().size());
        assertEquals(1, graph.getDataConnections().size());
    }

    @Test
    void testComplexGraph() {
        Graph graph = new Graph("ComplexGraph");

        // 创建多个节点
        Node trigger = graph.addNode("Trigger.Test");
        Node branch = graph.addNode("Control.Branch");
        Node action1 = graph.addNode("Action.Test1");
        Node action2 = graph.addNode("Action.Test2");

        // 设置位置
        trigger.setPosition(100, 100);
        branch.setPosition(300, 100);
        action1.setPosition(500, 50);
        action2.setPosition(500, 150);

        // 连接
        graph.flow(trigger, branch);
        graph.flow(branch, action1);
        graph.flow(branch, action2);

        // 添加变量
        graph.addGraphVariable("counter", NodeType.base(NodeType.BaseType.INT), 0, true);
        graph.addGraphVariable("items", NodeType.list(NodeType.base(NodeType.BaseType.INT)), new Integer[]{1, 2, 3}, false);

        // 添加注释
        graph.addComment("Main trigger", 100, 50);
        graph.addNodeComment(branch, "Branch based on condition");

        // 验证
        assertEquals(4, graph.getNodes().size());
        assertEquals(2, graph.getGraphVariables().size());
        assertEquals(1, graph.getComments().size());
        assertEquals(3, graph.getFlowConnections().size());
    }
}