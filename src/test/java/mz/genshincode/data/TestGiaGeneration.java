package mz.genshincode.data;

import mz.genshincode.data.asset.AssetBundle;
import mz.genshincode.shit.Graph;
import mz.genshincode.shit.Node;

import java.io.File;

/**
 * 测试生成 .gia 文件
 */
public class TestGiaGeneration {
    public static void main(String[] args) throws Exception {
        // 创建一个简单的图
        Graph graph = new Graph("TestGraph");
        
        // 添加两个节点
        Node node1 = graph.addNode("Test.Node1");
        Node node2 = graph.addNode("Test.Node2");
        
        // 连接节点
        graph.flow(node1, node2);
        
        // 保存到文件
        File outputFile = new File("output.gia");
        GenshinDataAssets.saveGraph(AssetBundle.Mode.CLASSIC, graph, outputFile);
        
        System.out.println("Generated .gia file: " + outputFile.getAbsolutePath());
        System.out.println("File size: " + outputFile.length() + " bytes");
    }
}