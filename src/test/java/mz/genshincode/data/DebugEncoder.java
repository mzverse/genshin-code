package mz.genshincode.data;

import mz.genshincode.data.asset.*;
import mz.genshincode.shit.Graph;
import mz.genshincode.shit.GraphEncoder;
import mz.genshincode.shit.Node;

/**
 * 调试编码器
 */
public class DebugEncoder {
    public static void main(java.lang.String[] args) {
        // 创建一个简单的图
        Graph graph = new Graph("TestGraph");
        Node node1 = graph.addNode("Test.Node1");
        Node node2 = graph.addNode("Test.Node2");
        graph.flow(node1, node2);
        
        System.out.println("Graph created:");
        System.out.println("  Name: " + graph.getGraphName());
        System.out.println("  Nodes: " + graph.getAllNodes().size());
        
        // 尝试编码
        try {
            AssetBundle bundle = GraphEncoder.encode(graph);
            System.out.println("\nAssetBundle created:");
            System.out.println("  Primary resource: " + bundle.getPrimaryResourceCount());
            System.out.println("  Export tag: " + bundle.getExportTag());
            System.out.println("  Engine version: " + bundle.getEngineVersion());
            
            for (Asset resource : bundle.getPrimaryResourceList()) {
                System.out.println("\nPrimary resource:");
                System.out.println("  Internal name: " + resource.getInternalName());
                System.out.println("  Has identity: " + resource.hasIdentity());
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
                            System.out.println("  Nodes count: " + nodeGraph.getNodesCount());
                        }
                    }
                }
            }
            
            byte[] data = bundle.toByteArray();
            System.out.println("\nEncoded data size: " + data.length);
            
        } catch (Exception e) {
            System.out.println("Error encoding: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
