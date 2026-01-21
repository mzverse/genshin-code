package mz.genshincode.shit;

import mz.genshincode.data.GenshinDataAssets;
import mz.genshincode.data.asset.*;

/**
 * 调试编码器
 */
public class DebugEncoder {
    public static void main(java.lang.String[] args) {
        // 创建一个简单的图
        Graph graph = new Graph("TestGraph");
        Node node1 = graph.addNode("Arithmetic.General.Entry");
        Node node2 = graph.addNode("Debug.General.Log");
        
        // 设置节点位置
        node1.setPosition(0, 0);
        node2.setPosition(200, 0);
        
        graph.flow(node1, node2);
        
        System.out.println("Graph created:");
        System.out.println("  Name: " + graph.getGraphName());
        System.out.println("  Nodes: " + graph.getAllNodes().size());
        
        // 尝试编码
        try {
            AssetBundle bundle = GraphEncoder.encode(graph);
            System.out.println("\nAssetBundle created:");
            System.out.println("  Primary resource: " + bundle.getAssetCount());
            System.out.println("  Export tag: " + bundle.getExportInfo());
            System.out.println("  Engine version: " + bundle.getEngineVersion());
            
            for (Asset resource : bundle.getAssetList()) {
                System.out.println("\nPrimary resource:");
                System.out.println("  Internal name: " + resource.getName());
                System.out.println("  Has identity: " + resource.hasId());
                System.out.println("  Has graph data: " + resource.hasGraphData());
                
                if (resource.hasGraphData()) {
                    NodeGraphContainer container = resource.getGraphData();
                    System.out.println("\nGraph container:");
                    System.out.println("  Has inner: " + container.hasInner());
                    
                    if (container.hasInner()) {
                        NodeGraphContainer.InnerWrapper inner = container.getInner();
                        System.out.println("\nInner wrapper:");
                        System.out.println("  Has graph: " + inner.hasGraph());
                        
                        if (inner.hasGraph()) {
                            NodeGraph nodeGraph = inner.getGraph();
                            System.out.println("\nNode graph:");
                            System.out.println("  Display name: " + nodeGraph.getDisplayName());
                            System.out.println("  Nodes count: " + nodeGraph.getNodeCount());
                        }
                    }
                }
            }
            
            byte[] data = bundle.toByteArray();
                    System.out.println("\nEncoded data size: " + data.length);
                    
                    // 保存到文件
                    java.io.File outputFile = new java.io.File("output.gia");
//                    GenshinDataAssets.saveGraph(AssetBundle.Mode.CLASSIC, graph, outputFile);
                    System.out.println("\nSaved to: " + outputFile.getAbsolutePath());
                    System.out.println("File size: " + outputFile.length() + " bytes");            
        } catch (Exception e) {
            System.out.println("Error encoding: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
