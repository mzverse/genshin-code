package mz.genshincode.shit;

import mz.genshincode.data.asset.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 图编码器
 * 将 Graph 对象编码为 Protobuf 格式的 AssetBundle
 */
public class GraphEncoder {

    /**
     * 将 Graph 编码为 AssetBundle
     */
    public static AssetBundle encode(Graph graph) {
        AssetBundle.Builder builder = AssetBundle.newBuilder();

        // 构建主资源
        Asset primaryResource = buildPrimaryResource(graph);
        builder.addAsset(primaryResource);

        // 设置导出标签
        String exportTag = buildExportTag(graph);
        builder.setExportInfo(exportTag);

        // 设置引擎版本
        builder.setEngineVersion("6.3.0");

        return builder.build();
    }

    /**
     * 构建主资源
     */
    public static Asset buildPrimaryResource(Graph graph) {
        Asset.Builder builder = Asset.newBuilder();

        // 设置资源标识符
        Identifier identity = buildIdentifier(graph);
        builder.setId(identity);

        // 设置内部名称
        builder.setName(graph.getGraphName());

        // 设置资源类
        builder.setType(Asset.Type.forNumber(graph.getSystem().getValue()));

        // 设置引用列表（根据使用的节点）
        List<Identifier> references = buildReferences(graph);
        for (Identifier ref : references) {
            builder.addReference(ref);
        }

        // 构建节点图数据
        NodeGraphContainer graphContainer = buildNodeGraphContainer(graph);
        builder.setGraphData(graphContainer);

        return builder.build();
    }

    /**
     * 构建资源标识符
     */
    private static Identifier buildIdentifier(Graph graph) {
        Identifier.Builder builder = Identifier.newBuilder();

        // 根据系统类型设置标识符
        switch (graph.getSystem()) {
            case ENTITY_NODE_GRAPH:
            case ITEM_NODE_GRAPH:
            case STATUS_NODE_GRAPH:
                // 实体节点图 - 使用 SERVER_NODE_GRAPH 作为 service_domain
                builder.setCategory(Identifier.Category.forNumber(5)); // SERVER_NODE_GRAPH
                builder.setGuid(graph.getGraphId());
                break;
            case SKILL_NODE_GRAPH:
            case CLASS_NODE_GRAPH:
                // 技能/职业节点图
                builder.setCategory(Identifier.Category.forNumber(20002)); // CLIENT_SKILL
                builder.setGuid(graph.getGraphId());
                break;
            case BOOLEAN_FILTER_GRAPH:
            case INTEGER_FILTER_GRAPH:
                // 过滤器节点图
                builder.setCategory(Identifier.Category.forNumber(20001)); // CLIENT_FILTER
                builder.setGuid(graph.getGraphId());
                break;
            default:
                builder.setCategory(Identifier.Category.forNumber(5)); // SERVER_NODE_GRAPH
                builder.setGuid(graph.getGraphId());
        }

        return builder.build();
    }

    /**
     * 构建节点图容器
     */
    private static NodeGraphContainer buildNodeGraphContainer(Graph graph) {
        NodeGraphContainer.Builder containerBuilder = NodeGraphContainer.newBuilder();
        NodeGraphContainer.InnerWrapper.Builder innerWrapperBuilder = NodeGraphContainer.InnerWrapper.newBuilder();
        NodeGraph.Builder graphBuilder = NodeGraph.newBuilder();

        // 设置节点图标识符
        Identifier graphIdentity = buildGraphLocator(graph);
        graphBuilder.setId(graphIdentity);

        // 设置显示名称
        graphBuilder.setDisplayName(graph.getGraphName());

        // 构建节点列表
        List<NodeInstance> nodes = buildNodes(graph);
        graphBuilder.addAllNode(nodes);

        // 构建注释列表
        List<Annotation> annotations = buildAnnotations(graph);
        graphBuilder.addAllComment(annotations);

        // 构建图变量列表
        List<mz.genshincode.data.asset.GraphVariable> graphVars = buildGraphVariables(graph);
        graphBuilder.addAllBlackboard((Iterable<? extends mz.genshincode.data.asset.GraphVariable>) graphVars);

        // 设置接口映射（空列表）
        graphBuilder.addAllPortMapping(new ArrayList<>());

        // 构建完整对象
        innerWrapperBuilder.setGraph(graphBuilder.build());
        containerBuilder.setInner(innerWrapperBuilder.build());

        return containerBuilder.build();
    }

    /**
     * 构建节点图标识符
     */
    private static Identifier buildGraphLocator(Graph graph) {
        Identifier.Builder builder = Identifier.newBuilder();
        builder.setSource(Identifier.Source.forNumber(10000)); // USER_DEFINED
        builder.setCategory(Identifier.Category.forNumber(5)); // SERVER_NODE_GRAPH
        builder.setKind(Identifier.AssetKind.forNumber(21001)); // CUSTOM_GRAPH
        builder.setRuntimeId(graph.getGraphId());
        return builder.build();
    }

    /**
     * 构建引用列表
     */
    private static List<Identifier> buildReferences(Graph graph) {
        List<Identifier> references = new ArrayList<>();
        
        // 收集所有使用的节点 ID
        java.util.Set<Long> usedNodeIds = new java.util.HashSet<>();
        for (Node node : graph.getAllNodes()) {
            long nodeId = getNodeIdFromIdentifier(node.getIdentifier());
            usedNodeIds.add(nodeId);
        }
        
        // 为每个使用的节点创建引用
        for (Long nodeId : usedNodeIds) {
            Identifier.Builder refBuilder = Identifier.newBuilder();
            refBuilder.setSource(Identifier.Source.forNumber(10001)); // SYSTEM_DEFINED
            refBuilder.setCategory(Identifier.Category.forNumber(20000)); // SERVER_BASIC
            refBuilder.setKind(Identifier.AssetKind.forNumber(22000)); // SYS_CALL_STUB
            refBuilder.setRuntimeId(nodeId);
            references.add(refBuilder.build());
        }
        
        return references;
    }

    /**
     * 构建节点列表
     */
    private static List<NodeInstance> buildNodes(Graph graph) {
        List<NodeInstance> nodes = new ArrayList<>();
        for (Node node : graph.getAllNodes()) {
            nodes.add(buildNode(node));
        }
        return nodes;
    }

    /**
     * 构建单个节点
     */
    private static NodeInstance buildNode(Node node) {
        NodeInstance.Builder builder = NodeInstance.newBuilder();

        // 设置节点索引
        builder.setIndex(node.getNodeIndex());

        // 设置外壳引用（UI 定义）
        Identifier shellRef = buildShellRef(node);
        builder.setShellRef(shellRef);

        // 设置内核引用（逻辑实现）
        Identifier kernelRef = buildKernelRef(node);
        builder.setKernelRef(kernelRef);

        // 构建引脚列表
        List<Pin> pins = buildPins(node);
        builder.addAllPin(pins);

        // 设置位置
        builder.setXPos(node.getX());
        builder.setYPos(node.getY());

        // 设置注释
        if (node.getComment() != null) {
            Annotation comment = Annotation.newBuilder()
                    .setText(node.getComment())
                    .build();
            builder.setAttachedComment(comment);
        }

        // 设置结构体依赖（空列表）
        builder.addAllUsingStructs(new ArrayList<>());

        return builder.build();
    }

    /**
     * 构建外壳引用
     */
    private static Identifier buildShellRef(Node node) {
        Identifier.Builder builder = Identifier.newBuilder();
        builder.setSource(Identifier.Source.forNumber(10001)); // SYSTEM_DEFINED
        builder.setCategory(Identifier.Category.forNumber(20000)); // SERVER_BASIC
        builder.setKind(Identifier.AssetKind.forNumber(22000)); // SYS_CALL_STUB
        builder.setRuntimeId(getNodeIdFromIdentifier(node.getIdentifier()));
        return builder.build();
    }

    /**
     * 构建内核引用
     */
    private static Identifier buildKernelRef(Node node) {
        Identifier.Builder builder = Identifier.newBuilder();
        builder.setSource(Identifier.Source.forNumber(10001)); // SYSTEM_DEFINED
        builder.setCategory(Identifier.Category.forNumber(20000)); // SERVER_BASIC
        builder.setKind(Identifier.AssetKind.forNumber(22000)); // SYS_CALL_STUB
        builder.setRuntimeId(getNodeIdFromIdentifier(node.getIdentifier()));
        return builder.build();
    }

    /**
     * 从节点标识符获取节点 ID
     */
    private static long getNodeIdFromIdentifier(java.lang.String identifier) {
        return NodeIds.getNodeId(identifier);
    }

    /**
     * 构建引脚列表
     */
    private static List<Pin> buildPins(Node node) {
        List<Pin> pins = new ArrayList<>();

        // 这里简化实现，实际项目中应该根据节点定义来构建引脚
        // 包括输入引脚、输出引脚、控制流引脚等

        return pins;
    }

    /**
     * 构建注释列表
     */
    private static List<Annotation> buildAnnotations(Graph graph) {
        List<Annotation> annotations = new ArrayList<>();
        for (GraphComment comment : graph.getComments()) {
            Annotation.Builder builder = Annotation.newBuilder();
            builder.setText(comment.getContent());
            builder.setXPos(comment.getX());
            builder.setYPos(comment.getY());
            annotations.add(builder.build());
        }
        return annotations;
    }

    /**
     * 构建图变量列表
     */
    private static List<mz.genshincode.data.asset.GraphVariable> buildGraphVariables(Graph graph) {
        List<mz.genshincode.data.asset.GraphVariable> graphVars = new ArrayList<>();
        for (Map.Entry<java.lang.String, GraphVariable> entry : graph.getGraphVariables().entrySet()) {
            GraphVariable var = entry.getValue();
            mz.genshincode.data.asset.GraphVariable.Builder builder = mz.genshincode.data.asset.GraphVariable.newBuilder();

            builder.setVarName(var.getName());
            builder.setBaseType(mz.genshincode.data.asset.ServerTypeId.forNumber(ServerTypeId.fromNodeType(var.getType()).getValue()));
            builder.setStorageValue(buildTypedValue(var.getValue()));
            builder.setIsPublic(var.isExposed());

            // 设置容器类型（如果有）
            if (var.getType().isDict()) {
                builder.setContainerKeyType(mz.genshincode.data.asset.ServerTypeId.forNumber(ServerTypeId.fromNodeType(var.getType().getKeyType()).getValue()));
                builder.setContainerValueType(mz.genshincode.data.asset.ServerTypeId.forNumber(ServerTypeId.fromNodeType(var.getType().getValueType()).getValue()));
            } else {
                builder.setContainerKeyType(mz.genshincode.data.asset.ServerTypeId.forNumber(6));
                builder.setContainerValueType(mz.genshincode.data.asset.ServerTypeId.forNumber(6));
            }

            // 设置结构体 ID（如果有）
            if (var.getType().getStructId() != null) {
                builder.setSchemaRefId(var.getType().getStructId());
            }

            graphVars.add(builder.build());
        }
        return graphVars;
    }

    /**
     * 构建类型化值
     */
    private static mz.genshincode.data.asset.TypedValue buildTypedValue(TypedValue value) {
        mz.genshincode.data.asset.TypedValue.Builder builder = mz.genshincode.data.asset.TypedValue.newBuilder();

        if (value == null || !value.isValueSet()) {
            builder.setIsValueSet(0);
            return builder.build();
        }

        builder.setIsValueSet(1);

        // 根据类型设置值
        switch (value.getType().getType()) {
            case BASE:
                switch (value.getType().getBaseType()) {
                    case INT:
                        builder.setValInt(mz.genshincode.data.asset.Int.newBuilder().setInt(value.getInt()).build());
                        builder.setWidget(mz.genshincode.data.asset.TypedValue.WidgetType.NUMBER_INPUT);
                        break;
                    case FLOAT:
                        builder.setValFloat(mz.genshincode.data.asset.Float.newBuilder().setFloat(value.getFloat()).build());
                        builder.setWidget(mz.genshincode.data.asset.TypedValue.WidgetType.DECIMAL_INPUT);
                        break;
                    case BOOL:
                        builder.setValEnum(mz.genshincode.data.asset.Enum.newBuilder().setEnum(value.getBool() ? 1 : 0).build());
                        builder.setWidget(mz.genshincode.data.asset.TypedValue.WidgetType.ENUM_PICKER);
                        break;
                    case STRING:
                        builder.setValString(mz.genshincode.data.asset.Str.newBuilder().setStr(value.getString()).build());
                        builder.setWidget(mz.genshincode.data.asset.TypedValue.WidgetType.TEXT_INPUT);
                        break;
                    case VECTOR:
                        float[] vec = value.getVector();
                        builder.setValVector(mz.genshincode.data.asset.Vector.newBuilder()
                                .setVec(mz.genshincode.data.asset.Vector.Vec.newBuilder()
                                        .setX(vec[0])
                                        .setY(vec[1])
                                        .setZ(vec[2]))
                                .build());
                        builder.setWidget(mz.genshincode.data.asset.TypedValue.WidgetType.VECTOR_GROUP);
                        break;
                    case ENTITY:
                    case GUID:
                    case CONFIG:
                    case PREFAB:
                    case FACTION:
                        builder.setValId(mz.genshincode.data.asset.Id.newBuilder().setId(value.getId()).build());
                        builder.setWidget(mz.genshincode.data.asset.TypedValue.WidgetType.ID_INPUT);
                        break;
                }
                break;
            case LIST:
                builder.setWidget(mz.genshincode.data.asset.TypedValue.WidgetType.LIST_GROUP);
                mz.genshincode.data.asset.ListStorage.Builder listBuilder = mz.genshincode.data.asset.ListStorage.newBuilder();
                Object[] list = value.getList();
                if (list != null) {
                    for (Object item : list) {
                        TypedValue itemValue =
                                new TypedValue(value.getType().getItemType(), item);
                        listBuilder.addElements(buildTypedValue(itemValue));
                    }
                }
                builder.setValList(listBuilder.build());
                break;
            case DICT:
                builder.setWidget(mz.genshincode.data.asset.TypedValue.WidgetType.MAP_GROUP);
                mz.genshincode.data.asset.MapStorage.Builder mapBuilder = mz.genshincode.data.asset.MapStorage.newBuilder();
                Object[][] dict = value.getDict();
                if (dict != null) {
                    for (Object[] pair : dict) {
                        TypedValue keyValue =
                                new TypedValue(value.getType().getKeyType(), pair[0]);
                        TypedValue valueValue =
                                new TypedValue(value.getType().getValueType(), pair[1]);
                        mz.genshincode.data.asset.MapPairStorage.Builder pairBuilder = mz.genshincode.data.asset.MapPairStorage.newBuilder();
                        pairBuilder.setKey(buildTypedValue(keyValue));
                        pairBuilder.setValue(buildTypedValue(valueValue));
                        // MapPairStorage 需要包装为 TypedValue
                        mz.genshincode.data.asset.TypedValue.Builder pairValueBuilder = mz.genshincode.data.asset.TypedValue.newBuilder();
                        pairValueBuilder.setWidget(mz.genshincode.data.asset.TypedValue.WidgetType.MAP_PAIR_ITEM);
                        pairValueBuilder.setValPair(pairBuilder.build());
                        pairValueBuilder.setIsValueSet(1);
                        mapBuilder.addPairs(pairValueBuilder.build());
                    }
                }
                builder.setValMap(mapBuilder.build());
                break;
            case STRUCT:
                builder.setWidget(mz.genshincode.data.asset.TypedValue.WidgetType.STRUCT_BLOCK);
                mz.genshincode.data.asset.StructStorage.Builder structBuilder = mz.genshincode.data.asset.StructStorage.newBuilder();
                Object[] struct = value.getStruct();
                if (struct != null) {
                    for (Object field : struct) {
                        // 简化实现，实际应该根据结构体定义来构建字段
                        structBuilder.addFields(buildTypedValue(
                                new TypedValue(NodeType.base(NodeType.BaseType.INT), field)));
                    }
                }
                builder.setValStruct(structBuilder.build());
                break;
        }

        return builder.build();
    }

    /**
     * 构建导出标签
     */
    private static java.lang.String buildExportTag(Graph graph) {
        long timestamp = System.currentTimeMillis() / 1000;
        java.lang.String fileName = graph.getGraphName().replaceAll("[^a-zA-Z0-9_.]", "_").toLowerCase();
        return java.lang.String.format("%d-%d-%d-\\%s", graph.getUid(), timestamp, graph.getFileId(), fileName);
    }
}
