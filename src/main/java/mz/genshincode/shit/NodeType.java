package mz.genshincode.shit;

/**
 * 节点类型系统
 * 表示节点图中引脚和变量的数据类型
 */
public class NodeType {
    public enum Type {
        BASE,       // 基础类型 (Int, Float, Bool, String, Vector, etc.)
        LIST,       // 列表类型 (List<T>)
        DICT,       // 字典类型 (Map<K, V>)
        STRUCT,     // 结构体类型
        ENUM,       // 枚举类型
        VARIANT     // 可变类型 (用于泛型节点)
    }

    public enum BaseType {
        INT,        // 整数
        FLOAT,      // 浮点数
        BOOL,       // 布尔值
        STRING,     // 字符串
        VECTOR,     // 三维向量
        ENTITY,     // 实体引用
        GUID,       // 全局唯一ID
        CONFIG,     // 配置表引用
        PREFAB,     // 预制体引用
        FACTION     // 阵营
    }

    private final Type type;
    private final BaseType baseType;
    private final NodeType itemType;      // for LIST: 元素类型
    private final NodeType keyType;       // for DICT: 键类型
    private final NodeType valueType;     // for DICT: 值类型
    private final Long structId;          // for STRUCT: 结构体ID
    private final Long enumId;            // for ENUM: 枚举ID

    private NodeType(Type type, BaseType baseType, NodeType itemType,
                    NodeType keyType, NodeType valueType, Long structId, Long enumId) {
        this.type = type;
        this.baseType = baseType;
        this.itemType = itemType;
        this.keyType = keyType;
        this.valueType = valueType;
        this.structId = structId;
        this.enumId = enumId;
    }

    // ========== 静态工厂方法 ==========

    /**
     * 创建基础类型
     */
    public static NodeType base(BaseType baseType) {
        return new NodeType(Type.BASE, baseType, null, null, null, null, null);
    }

    /**
     * 创建列表类型
     */
    public static NodeType list(NodeType itemType) {
        return new NodeType(Type.LIST, null, itemType, null, null, null, null);
    }

    /**
     * 创建字典类型
     */
    public static NodeType dict(NodeType keyType, NodeType valueType) {
        return new NodeType(Type.DICT, null, null, keyType, valueType, null, null);
    }

    /**
     * 创建结构体类型
     */
    public static NodeType struct(long structId) {
        return new NodeType(Type.STRUCT, null, null, null, null, structId, null);
    }

    /**
     * 创建枚举类型
     */
    public static NodeType enumType(long enumId) {
        return new NodeType(Type.ENUM, null, null, null, null, null, enumId);
    }

    /**
     * 创建可变类型（用于泛型节点）
     */
    public static NodeType variant() {
        return new NodeType(Type.VARIANT, null, null, null, null, null, null);
    }

    // ========== Getter 方法 ==========

    public Type getType() {
        return type;
    }

    public BaseType getBaseType() {
        return baseType;
    }

    public NodeType getItemType() {
        return itemType;
    }

    public NodeType getKeyType() {
        return keyType;
    }

    public NodeType getValueType() {
        return valueType;
    }

    public Long getStructId() {
        return structId;
    }

    public Long getEnumId() {
        return enumId;
    }

    // ========== 工具方法 ==========

    /**
     * 检查是否为列表类型
     */
    public boolean isList() {
        return type == Type.LIST;
    }

    /**
     * 检查是否为字典类型
     */
    public boolean isDict() {
        return type == Type.DICT;
    }

    /**
     * 检查是否为结构体类型
     */
    public boolean isStruct() {
        return type == Type.STRUCT;
    }

    /**
     * 检查是否为可变类型
     */
    public boolean isVariant() {
        return type == Type.VARIANT;
    }

    /**
     * 获取类型的字符串表示
     */
    public String toString() {
        switch (type) {
            case BASE:
                return baseType.toString();
            case LIST:
                return "L<" + itemType + ">";
            case DICT:
                return "D<" + keyType + "," + valueType + ">";
            case STRUCT:
                return "S[" + structId + "]";
            case ENUM:
                return "E[" + enumId + "]";
            case VARIANT:
                return "Variant";
            default:
                return "Unknown";
        }
    }

    /**
     * 类型相等性检查
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NodeType other = (NodeType) obj;
        if (type != other.type) return false;
        if (type == Type.BASE) {
            return baseType == other.baseType;
        }
        if (type == Type.LIST) {
            return itemType.equals(other.itemType);
        }
        if (type == Type.DICT) {
            return keyType.equals(other.keyType) && valueType.equals(other.valueType);
        }
        if (type == Type.STRUCT) {
            return structId.equals(other.structId);
        }
        if (type == Type.ENUM) {
            return enumId.equals(other.enumId);
        }
        return true;
    }

    public int hashCode() {
        int result = type.hashCode();
        if (type == Type.BASE) {
            result = 31 * result + baseType.hashCode();
        } else if (type == Type.LIST) {
            result = 31 * result + (itemType != null ? itemType.hashCode() : 0);
        } else if (type == Type.DICT) {
            result = 31 * result + (keyType != null ? keyType.hashCode() : 0);
            result = 31 * result + (valueType != null ? valueType.hashCode() : 0);
        } else if (type == Type.STRUCT) {
            result = 31 * result + (structId != null ? structId.hashCode() : 0);
        } else if (type == Type.ENUM) {
            result = 31 * result + (enumId != null ? enumId.hashCode() : 0);
        }
        return result;
    }
}