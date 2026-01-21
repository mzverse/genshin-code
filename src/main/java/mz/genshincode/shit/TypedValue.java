package mz.genshincode.shit;

/**
 * 类型化值
 * 表示节点图中引脚或变量的值，包含类型信息和实际值
 */
public class TypedValue {
    private final NodeType type;
    private final Object value;

    public TypedValue(NodeType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public NodeType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    /**
     * 获取整数值
     */
    public Integer getInt() {
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return null;
    }

    /**
     * 获取浮点数值
     */
    public Float getFloat() {
        if (value instanceof Float) {
            return (Float) value;
        }
        return null;
    }

    /**
     * 获取布尔值
     */
    public Boolean getBool() {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return null;
    }

    /**
     * 获取字符串值
     */
    public String getString() {
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    /**
     * 获取向量值 [x, y, z]
     */
    public float[] getVector() {
        if (value instanceof float[]) {
            return (float[]) value;
        }
        return null;
    }

    /**
     * 获取ID值（用于 Entity、GUID、Config、Prefab 等）
     */
    public Long getId() {
        if (value instanceof Long) {
            return (Long) value;
        }
        return null;
    }

    /**
     * 获取枚举值
     */
    public Long getEnum() {
        if (value instanceof Long) {
            return (Long) value;
        }
        return null;
    }

    /**
     * 获取列表值
     */
    public Object[] getList() {
        if (value instanceof Object[]) {
            return (Object[]) value;
        }
        return null;
    }

    /**
     * 获取字典值 [[key, value], ...]
     */
    public Object[][] getDict() {
        if (value instanceof Object[][]) {
            return (Object[][]) value;
        }
        return null;
    }

    /**
     * 获取结构体值 [field1, field2, ...]
     */
    public Object[] getStruct() {
        if (value instanceof Object[]) {
            return (Object[]) value;
        }
        return null;
    }

    /**
     * 检查值是否已设置
     */
    public boolean isValueSet() {
        return value != null;
    }

    @Override
    public String toString() {
        return "TypedValue{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }
}