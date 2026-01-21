package mz.genshincode.shit;

/**
 * 资源类枚举
 * 定义节点图的业务类型
 */
public enum ResourceClass {
    UNKNOWN_TYPE(0, "UNKNOWN"),
    OBJECT(1, "OBJECT"),
    CREATION(2, "CREATION"),
    OBJECT_ENTITY(3, "OBJECT_ENTITY"),
    CREATION_ENTITY(4, "CREATION_ENTITY"),
    TERRAIN_ENTITY(5, "TERRAIN_ENTITY"),
    PRESET_POINT(6, "PRESET_POINT"),
    UNIT_STATUS(7, "UNIT_STATUS"),
    SKILL(8, "SKILL"),
    ENTITY_NODE_GRAPH(9, "ENTITY_NODE_GRAPH"),
    BOOLEAN_FILTER_GRAPH(10, "BOOLEAN_FILTER_GRAPH"),
    SKILL_NODE_GRAPH(11, "SKILL_NODE_GRAPH"),
    COMPOSITE_NODE_DECL(12, "COMPOSITE_NODE_DECL"),
    CAMERA(13, "CAMERA"),
    SIGNAL_NODE_DECL(14, "SIGNAL_NODE_DECL"),
    UI_CONTROL(15, "UI_CONTROL"),
    SKILL_RESOURCE(16, "SKILL_RESOURCE"),
    CLASS(17, "CLASS"),
    PLAYER_TEMPLATE(18, "PLAYER_TEMPLATE"),
    CHARACTER_TEMPLATE(19, "CHARACTER_TEMPLATE"),
    INTERFACE_LAYOUT(20, "INTERFACE_LAYOUT"),
    UI_CONTROL_GROUP(21, "UI_CONTROL_GROUP"),
    STATUS_NODE_GRAPH(22, "STATUS_NODE_GRAPH"),
    CLASS_NODE_GRAPH(23, "CLASS_NODE_GRAPH"),
    GLOBAL_TIMER(24, "GLOBAL_TIMER"),
    PROJECTILE(25, "PROJECTILE"),
    ITEM(26, "ITEM"),
    DECORATION(28, "DECORATION"),
    STRUCTURE(29, "STRUCTURE"),
    SHOP_TEMPLATE(30, "SHOP_TEMPLATE"),
    CURRENCY(35, "CURRENCY"),
    LEVEL_STRUCTURE(37, "LEVEL_STRUCTURE"),
    PATH(38, "PATH"),
    SHIELD(39, "SHIELD"),
    ENTITY_DEPLOYMENT_GROUP(43, "ENTITY_DEPLOYMENT_GROUP"),
    UNIT_TAG(44, "UNIT_TAG"),
    SCAN_TAG(45, "SCAN_TAG"),
    ITEM_NODE_GRAPH(46, "ITEM_NODE_GRAPH"),
    INTEGER_FILTER_GRAPH(47, "INTEGER_FILTER_GRAPH"),
    LIGHT_SOURCE(48, "LIGHT_SOURCE"),
    ENVIRONMENT_CONFIGURATION(49, "ENVIRONMENT_CONFIGURATION");

    private final int value;
    private final String name;

    ResourceClass(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static ResourceClass fromValue(int value) {
        for (ResourceClass rc : values()) {
            if (rc.value == value) {
                return rc;
            }
        }
        return UNKNOWN_TYPE;
    }

    public static ResourceClass fromName(String name) {
        for (ResourceClass rc : values()) {
            if (rc.name.equalsIgnoreCase(name)) {
                return rc;
            }
        }
        return UNKNOWN_TYPE;
    }

    @Override
    public String toString() {
        return name;
    }
}