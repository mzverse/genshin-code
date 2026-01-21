package mz.genshincode.data;

import mz.genshincode.shit.NodeType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 类型系统测试
 */
class NodeTypeTest {

    @Test
    void testBaseTypes() {
        NodeType intType = NodeType.base(NodeType.BaseType.INT);
        assertEquals(NodeType.Type.BASE, intType.getType());
        assertEquals(NodeType.BaseType.INT, intType.getBaseType());
        assertEquals("INT", intType.toString());

        NodeType floatType = NodeType.base(NodeType.BaseType.FLOAT);
        assertEquals(NodeType.BaseType.FLOAT, floatType.getBaseType());

        NodeType boolType = NodeType.base(NodeType.BaseType.BOOL);
        assertEquals(NodeType.BaseType.BOOL, boolType.getBaseType());

        NodeType stringType = NodeType.base(NodeType.BaseType.STRING);
        assertEquals(NodeType.BaseType.STRING, stringType.getBaseType());

        NodeType vectorType = NodeType.base(NodeType.BaseType.VECTOR);
        assertEquals(NodeType.BaseType.VECTOR, vectorType.getBaseType());
    }

    @Test
    void testListTypes() {
        NodeType intListType = NodeType.list(NodeType.base(NodeType.BaseType.INT));
        assertTrue(intListType.isList());
        assertEquals("L<INT>", intListType.toString());
        assertEquals(NodeType.base(NodeType.BaseType.INT), intListType.getItemType());

        NodeType stringListType = NodeType.list(NodeType.base(NodeType.BaseType.STRING));
        assertEquals("L<STRING>", stringListType.toString());
    }

    @Test
    void testDictTypes() {
        NodeType dictType = NodeType.dict(
                NodeType.base(NodeType.BaseType.STRING),
                NodeType.base(NodeType.BaseType.INT)
        );
        assertTrue(dictType.isDict());
        assertEquals("D<STRING,INT>", dictType.toString());
        assertEquals(NodeType.base(NodeType.BaseType.STRING), dictType.getKeyType());
        assertEquals(NodeType.base(NodeType.BaseType.INT), dictType.getValueType());
    }

    @Test
    void testStructTypes() {
        NodeType structType = NodeType.struct(12345L);
        assertTrue(structType.isStruct());
        assertEquals("S[12345]", structType.toString());
        assertEquals(12345L, structType.getStructId());
    }

    @Test
    void testEnumTypes() {
        NodeType enumType = NodeType.enumType(67890L);
        assertTrue(enumType.getType() == NodeType.Type.ENUM);
        assertEquals("E[67890]", enumType.toString());
        assertEquals(67890L, enumType.getEnumId());
    }

    @Test
    void testVariantTypes() {
        NodeType variantType = NodeType.variant();
        assertTrue(variantType.isVariant());
        assertEquals("Variant", variantType.toString());
    }

    @Test
    void testTypeEquality() {
        NodeType intType1 = NodeType.base(NodeType.BaseType.INT);
        NodeType intType2 = NodeType.base(NodeType.BaseType.INT);
        NodeType floatType = NodeType.base(NodeType.BaseType.FLOAT);

        assertEquals(intType1, intType2);
        assertNotEquals(intType1, floatType);

        NodeType listType1 = NodeType.list(NodeType.base(NodeType.BaseType.INT));
        NodeType listType2 = NodeType.list(NodeType.base(NodeType.BaseType.INT));
        assertEquals(listType1, listType2);

        NodeType dictType1 = NodeType.dict(
                NodeType.base(NodeType.BaseType.STRING),
                NodeType.base(NodeType.BaseType.INT)
        );
        NodeType dictType2 = NodeType.dict(
                NodeType.base(NodeType.BaseType.STRING),
                NodeType.base(NodeType.BaseType.INT)
        );
        assertEquals(dictType1, dictType2);
    }
}