package mz.genshincode.shit;

import java.util.*;

/**
 * 节点图
 * 用于创建、编辑和管理节点图
 */
public class Graph {
    /** 节点图系统类型 */
    private final ResourceClass system;

    /** 节点图名称 */
    private String graphName;

    /** 用户 ID */
    private int uid;

    /** 节点图 ID */
    private final int graphId;

    /** 文件 ID */
    private int fileId;

    /** 节点索引计数器 */
    private int nextNodeIndex;

    /** 节点映射 (nodeIndex -> Node) */
    private final Map<Integer, Node> nodes;

    /** 注释集合 */
    private final Set<GraphComment> comments;

    /** 图变量映射 (name -> GraphVariable) */
    private final Map<String, GraphVariable> graphVariables;

    /**
     * 创建一个新的节点图
     * @param system 节点图系统类型
     * @param graphName 节点图名称
     */
    public Graph(ResourceClass system, String graphName) {
        this.system = system;
        this.graphName = graphName;
        this.uid = generateRandomInt(9, 201);
        this.graphId = generateGraphId(system);
        this.fileId = this.graphId;
        this.nextNodeIndex = 0;
        this.nodes = new LinkedHashMap<>();
        this.comments = new HashSet<>();
        this.graphVariables = new HashMap<>();
    }

    /**
     * 创建实体节点图（便捷构造函数）
     */
    public Graph(String graphName) {
        this(ResourceClass.ENTITY_NODE_GRAPH, graphName);
    }

    // ========== Getter 方法 ==========

    public ResourceClass getSystem() {
        return system;
    }

    public String getGraphName() {
        return graphName;
    }

    public int getUid() {
        return uid;
    }

    public int getGraphId() {
        return graphId;
    }

    public int getFileId() {
        return fileId;
    }

    public Map<Integer, Node> getNodes() {
        return Collections.unmodifiableMap(nodes);
    }

    public Set<GraphComment> getComments() {
        return Collections.unmodifiableSet(comments);
    }

    public Map<String, GraphVariable> getGraphVariables() {
        return Collections.unmodifiableMap(graphVariables);
    }

    // ========== Setter 方法 ==========

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    // ========== 节点管理 ==========

    /**
     * 添加节点到图中
     * @param identifier 节点标识符
     * @return 创建的节点对象
     */
    public Node addNode(String identifier) {
        int index = nextNodeIndex++;
        Node node = new Node(index, identifier, system);
        nodes.put(index, node);
        return node;
    }

    /**
     * 添加现有节点到图中
     * @param node 节点对象
     * @return 添加的节点对象
     */
    public Node addNode(Node node) {
        if (nodes.containsKey(node.getNodeIndex())) {
            throw new IllegalArgumentException("Node index " + node.getNodeIndex() + " already exists in graph");
        }
        nodes.put(node.getNodeIndex(), node);
        nextNodeIndex = Math.max(nextNodeIndex, node.getNodeIndex() + 1);
        return node;
    }

    /**
     * 根据索引获取节点
     */
    public Node getNode(int index) {
        return nodes.get(index);
    }

    /**
     * 获取所有节点
     */
    public Collection<Node> getAllNodes() {
        return nodes.values();
    }

    // ========== 注释管理 ==========

    /**
     * 添加独立注释到节点图
     * @param content 注释内容
     * @param x 注释的 X 坐标
     * @param y 注释的 Y 坐标
     * @return 创建的注释对象
     */
    public GraphComment addComment(String content, float x, float y) {
        GraphComment comment = new GraphComment(content, x, y);
        comments.add(comment);
        return comment;
    }

    /**
     * 添加节点注释
     * @param node 目标节点
     * @param content 注释内容
     */
    public void addNodeComment(Node node, String content) {
        node.setComment(content);
    }

    // ========== 图变量管理 ==========

    /**
     * 添加图变量
     * @param name 变量名称
     * @param type 变量类型
     * @param value 变量初始值
     * @param exposed 是否暴露给外部
     * @return 创建的图变量对象
     */
    public GraphVariable addGraphVariable(String name, NodeType type, Object value, boolean exposed) {
        if (graphVariables.containsKey(name)) {
            throw new IllegalArgumentException("Graph variable '" + name + "' already exists");
        }
        TypedValue typedValue = new TypedValue(type, value);
        GraphVariable var = new GraphVariable(name, type, typedValue, exposed);
        graphVariables.put(name, var);
        return var;
    }

    /**
     * 获取图变量
     */
    public GraphVariable getGraphVariable(String name) {
        return graphVariables.get(name);
    }

    // ========== 连接管理 ==========

    /**
     * 创建控制流连接
     * @param from 源节点
     * @param to 目标节点
     * @return 创建的连接对象
     */
    public Connection flow(Node from, Node to) {
        return flow(from, to, "FlowOut", "FlowIn");
    }

    /**
     * 创建控制流连接（指定引脚）
     * @param from 源节点
     * @param to 目标节点
     * @param fromArg 源节点的流引脚标识符
     * @param toArg 目标节点的流引脚标识符
     * @return 创建的连接对象
     */
    public Connection flow(Node from, Node to, String fromArg, String toArg) {
        return from.connectFlowPinWith(fromArg, to, toArg, null);
    }

    /**
     * 创建数据流连接
     * @param from 源节点
     * @param to 目标节点
     * @param fromArg 源节点的引脚标识符（索引或名称）
     * @param toArg 目标节点的引脚标识符（索引或名称）
     * @return 创建的连接对象
     */
    public Connection connect(Node from, Node to, Object fromArg, Object toArg) {
        String fromPin = fromArg instanceof String ? (String) fromArg : "Pin" + fromArg;
        String toPin = toArg instanceof String ? (String) toArg : "Pin" + toArg;
        return from.connectPinWith(fromPin, to, toPin);
    }

    /**
     * 断开连接
     */
    public void disconnect(Connection conn) {
        if (conn.getType() == Connection.Type.DATA) {
            conn.getToNode().disconnectDataInAt(conn.getToPin());
        } else {
            List<Connection> flowList = conn.getFromNode().getFlowTo().get(conn.getFromPin());
            if (flowList != null) {
                int index = flowList.indexOf(conn);
                if (index >= 0) {
                    conn.getFromNode().disconnectFlowOutAt(conn.getFromPin(), index);
                }
            }
        }
    }

    /**
     * 获取所有数据流连接
     */
    public List<Connection> getDataConnections() {
        List<Connection> connections = new ArrayList<>();
        for (Node node : nodes.values()) {
            connections.addAll(node.getDataFrom().values());
        }
        return connections;
    }

    /**
     * 获取所有控制流连接
     */
    public List<Connection> getFlowConnections() {
        List<Connection> connections = new ArrayList<>();
        for (Node node : nodes.values()) {
            for (List<Connection> list : node.getFlowTo().values()) {
                connections.addAll(list);
            }
        }
        return connections;
    }

    // ========== 工具方法 ==========

    /**
     * 生成随机整数
     */
    private static int generateRandomInt(int digits, int max) {
        Random random = new Random();
        int min = (int) Math.pow(10, digits - 1);
        if (max <= min) {
            max = min + 100; // 确保 max > min
        }
        return min + random.nextInt(max - min);
    }

    /**
     * 根据系统类型生成节点图 ID
     */
    private static int generateGraphId(ResourceClass system) {
        Random random = new Random();
        int base = 0;
        switch (system) {
            case ENTITY_NODE_GRAPH:
            case ITEM_NODE_GRAPH:
            case STATUS_NODE_GRAPH:
                base = 0x40000000; // Server 范围: 1073741824
                break;
            case SKILL_NODE_GRAPH:
            case CLASS_NODE_GRAPH:
                base = 0x40800000; // Client 范围: 1082130432
                break;
            case BOOLEAN_FILTER_GRAPH:
            case INTEGER_FILTER_GRAPH:
                base = 0x40800000; // Client 范围
                break;
            default:
                base = 0x40000000; // 默认 Server 范围
        }
        // 生成 1-10000 之间的随机偏移
        return base + 1 + random.nextInt(10000);
    }

    @Override
    public String toString() {
        return "Graph{" +
                "system=" + system +
                ", graphName='" + graphName + '\'' +
                ", graphId=" + graphId +
                ", nodes=" + nodes.size() +
                '}';
    }
}
