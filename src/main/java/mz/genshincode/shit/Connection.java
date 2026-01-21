package mz.genshincode.shit;

/**
 * 节点连接
 * 表示节点之间的连接关系，可以是数据流连接或控制流连接
 */
public class Connection {
    public enum Type {
        DATA,   // 数据流连接
        FLOW    // 控制流连接
    }

    private final Node fromNode;
    private final String fromPin;
    private final Node toNode;
    private final String toPin;
    private final Type type;

    public Connection(Node fromNode, String fromPin, Node toNode, String toPin, Type type) {
        this.fromNode = fromNode;
        this.fromPin = fromPin;
        this.toNode = toNode;
        this.toPin = toPin;
        this.type = type;
    }

    public Node getFromNode() {
        return fromNode;
    }

    public String getFromPin() {
        return fromPin;
    }

    public Node getToNode() {
        return toNode;
    }

    public String getToPin() {
        return toPin;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "from=" + fromNode.getNodeIndex() + "." + fromPin +
                ", to=" + toNode.getNodeIndex() + "." + toPin +
                ", type=" + type +
                '}';
    }
}