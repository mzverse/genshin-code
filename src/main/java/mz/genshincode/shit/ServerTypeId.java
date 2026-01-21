package mz.genshincode.shit;

/**
 * 服务器端类型ID
 * 用于后端逻辑运算、数据存储、结构体定义、黑板变量
 */
public enum ServerTypeId {
    SERVER_UNKNOWN(0),
    S_ENTITY(1),
    S_GUID(2),
    S_INT(3),
    S_BOOL(4),
    S_FLOAT(5),
    S_STRING(6),
    S_GUID_LIST(7),
    S_INT_LIST(8),
    S_BOOL_LIST(9),
    S_FLOAT_LIST(10),
    S_STRING_LIST(11),
    S_VECTOR(12),
    S_ENTITY_LIST(13),
    S_ENUM_ITEM(14),
    S_LOCAL_VAR_REF(16),
    S_VECTOR_LIST(15),
    S_FACTION(17),
    S_ENUM_LIST(18),
    S_CONFIG(20),
    S_PREFAB(21),
    S_CONFIG_LIST(22),
    S_PREFAB_LIST(23),
    S_FACTION_LIST(24),
    S_STRUCT(25),
    S_STRUCT_LIST(26),
    S_DICT(27),
    S_VAR_SNAPSHOT_REF(28);

    private final int value;

    ServerTypeId(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ServerTypeId fromValue(int value) {
        for (ServerTypeId type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        return SERVER_UNKNOWN;
    }

    /**
     * 将 NodeType 转换为 ServerTypeId
     */
    public static ServerTypeId fromNodeType(NodeType type) {
        switch (type.getType()) {
            case BASE:
                switch (type.getBaseType()) {
                    case INT: return S_INT;
                    case FLOAT: return S_FLOAT;
                    case BOOL: return S_BOOL;
                    case STRING: return S_STRING;
                    case VECTOR: return S_VECTOR;
                    case ENTITY: return S_ENTITY;
                    case GUID: return S_GUID;
                    case CONFIG: return S_CONFIG;
                    case PREFAB: return S_PREFAB;
                    case FACTION: return S_FACTION;
                    default: return SERVER_UNKNOWN;
                }
            case LIST:
                switch (type.getItemType().getType()) {
                    case BASE:
                        switch (type.getItemType().getBaseType()) {
                            case INT: return S_INT_LIST;
                            case FLOAT: return S_FLOAT_LIST;
                            case BOOL: return S_BOOL_LIST;
                            case STRING: return S_STRING_LIST;
                            case ENTITY: return S_ENTITY_LIST;
                            case VECTOR: return S_VECTOR_LIST;
                            case GUID: return S_GUID_LIST;
                            case FACTION: return S_FACTION_LIST;
                            case CONFIG: return S_CONFIG_LIST;
                            case PREFAB: return S_PREFAB_LIST;
                            default: return SERVER_UNKNOWN;
                        }
                    case STRUCT:
                        return S_STRUCT_LIST;
                    default:
                        return SERVER_UNKNOWN;
                }
            case DICT:
                return S_DICT;
            case STRUCT:
                return S_STRUCT;
            case ENUM:
                return S_ENUM_ITEM;
            default:
                return SERVER_UNKNOWN;
        }
    }
}