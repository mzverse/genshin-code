package mz.genshincode.data;

import mz.genshincode.data.asset.*;

import java.util.stream.Collectors;

/**
 * 简单的 Protobuf 测试
 */
public class SimpleProtobufTest {
    public static void main(java.lang.String[] args) {
        // 创建一个简单的 AssetBundle
        AssetBundle.Builder builder = AssetBundle.newBuilder();
        
        // 创建一个 ResourceEntry
        Asset.Builder resourceBuilder = Asset.newBuilder();
        
        // 设置必需字段
        ResourceLocator.Builder locatorBuilder = ResourceLocator.newBuilder();
        locatorBuilder.setSourceDomain(ResourceLocator.Origin.USER_DEFINED);
        locatorBuilder.setServiceDomain(ResourceLocator.Category.SERVER_BASIC);
        locatorBuilder.setKind(ResourceLocator.AssetKind.CUSTOM_GRAPH);
        locatorBuilder.setRuntimeId(12345);
        
        resourceBuilder.setIdentity(locatorBuilder.build());
        resourceBuilder.setInternalName("Test");
        resourceBuilder.setResourceClass(Asset.Type.ENTITY_NODE_GRAPH);
        
        // 设置图数据
        NodeGraphContainer.Builder graphContainerBuilder = NodeGraphContainer.newBuilder();
        NodeGraphContainer.InnerWrapper.Builder innerWrapperBuilder = NodeGraphContainer.InnerWrapper.newBuilder();
        
        NodeGraph.Builder graphBuilder = NodeGraph.newBuilder();
        graphBuilder.setIdentity(locatorBuilder.build());
        graphBuilder.setDisplayName("Test Graph");
        
        innerWrapperBuilder.setGraph(graphBuilder.build());
        graphContainerBuilder.setInner(innerWrapperBuilder.build());
        
        resourceBuilder.setGraphData(graphContainerBuilder.build());
        
        builder.addPrimaryResource(resourceBuilder.build());
        builder.setExportTag("test-export");
        builder.setEngineVersion("6.2.0");
        
        // 编码
        AssetBundle bundle = builder.build();
        byte[] data = bundle.toByteArray();
        
        System.out.println("Encoded data size: " + data.length);
        System.out.println("First 20 bytes: ");
        for (int i = 0; i < Math.min(20, data.length); i++) {
            System.out.printf("%02x ", data[i]);
        }
        System.out.println();
        
        // 尝试解码
        try {
            AssetBundle decoded = AssetBundle.parseFrom(data);
            System.out.println("Decoded successfully!");
            System.out.println("Primary resource: " + decoded.getPrimaryResourceList().stream().map(Asset::getInternalName).collect(Collectors.toList()));
        } catch (Exception e) {
            System.out.println("Decode failed: " + e.getMessage());
        }
    }
}