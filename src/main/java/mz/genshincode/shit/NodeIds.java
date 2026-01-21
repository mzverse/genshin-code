package mz.genshincode.shit;

/**
 * 节点 ID 常量
 * 从原神千星奇域节点定义中提取
 */
public class NodeIds {
    // 基础节点
    public static final long ENTRY_NODE = 1; // 入口节点
    public static final long COMMENT_NODE = 1088; // 注释节点
    public static final long DATA_TYPE_CONVERSION = 180; // 数据类型转换
    public static final long LOG = 1088; // 日志节点（与注释节点共用）
    
    // 算术运算
    public static final long ADD = 100;
    public static final long SUBTRACT = 101;
    public static final long MULTIPLY = 102;
    public static final long DIVIDE = 103;
    public static final long MODULO = 104;
    
    // 比较运算
    public static final long EQUAL = 180;
    public static final long NOT_EQUAL = 181;
    public static final long GREATER_THAN = 182;
    public static final long LESS_THAN = 183;
    public static final long GREATER_EQUAL = 184;
    public static final long LESS_EQUAL = 185;
    
    // 逻辑运算
    public static final long AND = 200;
    public static final long OR = 201;
    public static final long NOT = 202;
    
    // 控制流
    public static final long BRANCH = 300;
    public static final long SEQUENCE = 301;
    
    /**
     * 从节点标识符获取节点 ID
     */
    public static long getNodeId(String identifier) {
        switch (identifier) {
            case "Entry":
            case "Arithmetic.General.Entry":
                return ENTRY_NODE;
            case "Comment":
            case "Debug.General.Log":
                return LOG;
            case "Arithmetic.General.Convert_Type":
            case "Arithmetic.General.Equal":
                return DATA_TYPE_CONVERSION;
            case "Arithmetic.General.Add":
                return ADD;
            case "Arithmetic.General.Subtract":
                return SUBTRACT;
            case "Arithmetic.General.Multiply":
                return MULTIPLY;
            case "Arithmetic.General.Divide":
                return DIVIDE;
            case "Control.General.Branch":
                return BRANCH;
            default:
                // 默认返回哈希码（应该避免这种情况）
                return (long) identifier.hashCode() & 0xFFFFFFFFL;
        }
    }
}