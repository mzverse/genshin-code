package mz.genshincode.shit;

import java.util.*;

/**
 * 节点实例
 * 表示节点图中的一个节点，包含节点定义、引脚值、连接关系等信息
 */
public class Node {
    /** 节点在图中的唯一索引 */
    private final int nodeIndex;

    /** 节点的唯一标识符 (用于查找节点定义) */
    private final String identifier;

    /** 节点的系统类型 */
    private final ResourceClass system;

    /** 节点的 X 坐标 */
    private float x;

    /** 节点的 Y 坐标 */
    private float y;

    /** 附加到节点的注释 */
    private String comment;

    /** 输入引脚的值存储 (PinIdentifier -> TypedValue) */
    private final Map<String, TypedValue> pinValues;

    /** 数据流输入连接 (PinIdentifier -> Connection) */
    private final Map<String, Connection> dataFrom;

    /** 数据流输出连接 (PinIdentifier -> Set<Connection>) */
    private final Map<String, Set<Connection>> dataTo;

    /** 控制流输入连接 (PinIdentifier -> Set<Connection>) */
    private final Map<String, Set<Connection>> flowFrom;

    /** 控制流输出连接 (PinIdentifier -> List<Connection>)，保持顺序 */
    private final Map<String, List<Connection>> flowTo;

    public Node(int nodeIndex, String identifier, ResourceClass system) {
        this.nodeIndex = nodeIndex;
        this.identifier = identifier;
        this.system = system;
        this.x = 0;
        this.y = 0;
        this.comment = null;
        this.pinValues = new HashMap<>();
        this.dataFrom = new HashMap<>();
        this.dataTo = new HashMap<>();
        this.flowFrom = new HashMap<>();
        this.flowTo = new HashMap<>();
    }

    // ========== Getter 方法 ==========

    public int getNodeIndex() {
        return nodeIndex;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ResourceClass getSystem() {
        return system;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getComment() {
        return comment;
    }

    public Map<String, TypedValue> getPinValues() {
        return Collections.unmodifiableMap(pinValues);
    }

    public Map<String, Connection> getDataFrom() {
        return Collections.unmodifiableMap(dataFrom);
    }

    public Map<String, Set<Connection>> getDataTo() {
        return Collections.unmodifiableMap(dataTo);
    }

    public Map<String, Set<Connection>> getFlowFrom() {
        return Collections.unmodifiableMap(flowFrom);
    }

    public Map<String, List<Connection>> getFlowTo() {
        return Collections.unmodifiableMap(flowTo);
    }

    // ========== Setter 方法 ==========

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 设置引脚值
     * @param pinIdentifier 引脚标识符
     * @param value 类型化值
     */
    public void setPinValue(String pinIdentifier, TypedValue value) {
        pinValues.put(pinIdentifier, value);
    }

    /**
     * 设置引脚值（使用索引）
     * @param pinIndex 引脚索引
     * @param value 类型化值
     */
    public void setPinValue(int pinIndex, TypedValue value) {
        setPinValue("Pin" + pinIndex, value);
    }

    // ========== 连接管理 ==========

    /**
     * 连接引脚到另一个节点的引脚
     * @param pin 当前节点的引脚标识符
     * @param withNode 目标节点
     * @param withPin 目标节点的引脚标识符
     * @return 创建的连接对象，失败时返回 null
     */
    public Connection connectPinWith(String pin, Node withNode, String withPin) {
        Connection conn = new Connection(this, pin, withNode, withPin, Connection.Type.DATA);
        return addConnection(conn);
    }

    /**
     * 连接控制流引脚到另一个节点的引脚
     * @param pin 当前节点的引脚标识符
     * @param withNode 目标节点
     * @param withPin 目标节点的引脚标识符
     * @param insertPos 插入位置（可选，仅用于控制流连接）
     * @return 创建的连接对象，失败时返回 null
     */
    public Connection connectFlowPinWith(String pin, Node withNode, String withPin, Integer insertPos) {
        Connection conn = new Connection(this, pin, withNode, withPin, Connection.Type.FLOW);
        return addConnection(conn);
    }

    /**
     * 添加连接
     */
    private Connection addConnection(Connection conn) {
        if (conn.getType() == Connection.Type.DATA) {
            // 数据流连接
            conn.getToNode().dataFrom.put(conn.getToPin(), conn);
            conn.getFromNode().dataTo.computeIfAbsent(conn.getFromPin(), k -> new HashSet<>()).add(conn);
        } else {
            // 控制流连接
            conn.getToNode().flowFrom.computeIfAbsent(conn.getToPin(), k -> new HashSet<>()).add(conn);
            List<Connection> list = conn.getFromNode().flowTo.computeIfAbsent(conn.getFromPin(), k -> new ArrayList<>());
            list.add(conn);
        }
        return conn;
    }

    /**
     * 断开指定输入数据引脚的连接
     */
    public boolean disconnectDataInAt(String pinIdentifier) {
        Connection conn = dataFrom.remove(pinIdentifier);
        if (conn != null) {
            conn.getFromNode().dataTo.get(conn.getFromPin()).remove(conn);
            return true;
        }
        return false;
    }

    /**
     * 断开指定输出控制流引脚的连接
     */
    public boolean disconnectFlowOutAt(String pinIdentifier, int index) {
        List<Connection> conns = flowTo.get(pinIdentifier);
        if (conns != null && index >= 0 && index < conns.size()) {
            Connection conn = conns.remove(index);
            conn.getToNode().flowFrom.get(conn.getToPin()).remove(conn);
            // 如果列表为空，删除 Map 中的键
            if (conns.isEmpty()) {
                flowTo.remove(pinIdentifier);
            }
            return true;
        }
        return false;
    }

    /**
     * 获取所有连接
     */
    public List<Connection> getAllConnections() {
        List<Connection> all = new ArrayList<>();
        all.addAll(dataFrom.values());
        dataTo.values().forEach(set -> all.addAll(set));
        flowFrom.values().forEach(set -> all.addAll(set));
        flowTo.values().forEach(list -> all.addAll(list));
        return all;
    }

    @Override
    public String toString() {
        return "Node{" +
                "nodeIndex=" + nodeIndex +
                ", identifier='" + identifier + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
