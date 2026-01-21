package mz.genshincode.shit;

/**
 * 图变量
 * 表示节点图级别的全局变量，可以在多个节点间共享数据
 */
public class GraphVariable {
    private final String name;
    private final NodeType type;
    private final TypedValue value;
    private final boolean exposed;

    public GraphVariable(String name, NodeType type, TypedValue value, boolean exposed) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.exposed = exposed;
    }

    public String getName() {
        return name;
    }

    public NodeType getType() {
        return type;
    }

    public TypedValue getValue() {
        return value;
    }

    public boolean isExposed() {
        return exposed;
    }

    @Override
    public String toString() {
        return "GraphVariable{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", value=" + value +
                ", exposed=" + exposed +
                '}';
    }
}