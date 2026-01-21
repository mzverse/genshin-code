package mz.genshincode.data;

import java.io.File;
import java.io.IOException;

import mz.genshincode.data.asset.*;

/**
 * 图示例代码
 * 演示如何使用 Graph API 创建节点图并保存为 .gia 文件
 */
public class GraphExample {

    public static void main(String[] args) throws IOException
    {
        new GenshinDataAssets(
        AssetBundle.newBuilder()
            .setMode(AssetBundle.Mode.CLASSIC)
            .addAsset(Asset.newBuilder()
                .setIdentity(ResourceLocator.newBuilder()
                    .setServiceDomain(ResourceLocator.Category.SERVER_NODE_GRAPH)
                    .setAssetGuid(1073741825L)
                    )
                .setInternalName("test")
                .setResourceClass(Asset.Type.ENTITY_NODE_GRAPH)
                .setGraphData(NodeGraphContainer.newBuilder()
                    .setInner(NodeGraphContainer.InnerWrapper.newBuilder()
                        .setGraph(NodeGraph.newBuilder()
                            .setIdentity(ResourceLocator.newBuilder()
                                .setSourceDomain(ResourceLocator.Origin.USER_DEFINED)
                                .setServiceDomain(ResourceLocator.Category.SERVER_BASIC)
                                .setKind(ResourceLocator.AssetKind.CUSTOM_GRAPH)
                                .setRuntimeId(1073741825L)
                            )
                            .setDisplayName("test")
                        )
                    )
                )
            .build()).build()).save(new File("output.gia"));
//        // 创建一个实体节点图
//        Graph graph = new Graph("MyFirstGraph");
//
//        // 添加节点
//        Node triggerNode = graph.addNode("Trigger.Tab.OnTabSelect");
//        Node getVarNode = graph.addNode("Query.CustomVariable.GetVariable");
//        Node branchNode = graph.addNode("Control.General.Branch");
//        Node logNode = graph.addNode("Debug.Log");
//
//        // 设置节点位置
//        triggerNode.setPosition(100, 100);
//        getVarNode.setPosition(300, 100);
//        branchNode.setPosition(500, 100);
//        logNode.setPosition(700, 100);
//
//        // 设置引脚值
//        TypedValue varNameValue = new TypedValue(
//                NodeType.base(NodeType.BaseType.STRING),
//                "PlayerLevel"
//        );
//        getVarNode.setPinValue("varName", varNameValue);
//
//        // 连接控制流
//        graph.flow(triggerNode, getVarNode);
//        graph.flow(getVarNode, branchNode);
//        graph.flow(branchNode, logNode);
//
//        // 连接数据流
//        graph.connect(getVarNode, branchNode, 0, "cond");
//
//        // 设置分支节点的参数
//        TypedValue trueValue = new TypedValue(
//                NodeType.base(NodeType.BaseType.BOOL),
//                true
//        );
//        branchNode.setPinValue("Case1", trueValue);
//
//        // 添加注释
//        graph.addComment("这是一个触发器节点", 100, 50);
//        graph.addNodeComment(branchNode, "这是一个分支节点");
//
//        // 添加图变量
//        graph.addGraphVariable(
//                "playerLevel",
//                NodeType.base(NodeType.BaseType.INT),
//                10,
//                true
//        );
//
//        graph.addGraphVariable(
//                "itemList",
//                NodeType.list(NodeType.base(NodeType.BaseType.INT)),
//                new Integer[]{1, 2, 3, 4, 5},
//                false
//        );
//
//        // 打印图信息
//        System.out.println("Graph created:");
//        System.out.println("  System: " + graph.getSystem());
//        System.out.println("  Name: " + graph.getGraphName());
//        System.out.println("  ID: " + graph.getGraphId());
//        System.out.println("  Nodes: " + graph.getAllNodes().size());
//        System.out.println("  Variables: " + graph.getGraphVariables().size());
//
//        // 保存到文件
//        try {
//            File outputFile = new File("output.gia");
//            GenshinDataAsset.saveGraph(graph, outputFile);
//            System.out.println("\nGraph saved to: " + outputFile.getAbsolutePath());
//        } catch (IOException e) {
//            System.err.println("Failed to save graph: " + e.getMessage());
//            e.printStackTrace();
//        }
    }
}