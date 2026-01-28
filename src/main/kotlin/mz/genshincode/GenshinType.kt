package mz.genshincode

import mz.genshincode.data.asset.*
import mz.genshincode.data.asset.Enum
import mz.genshincode.data.asset.Vec.Value
import java.util.stream.Collectors

class Entity private constructor()
class ServerLocal private constructor()

data class Vector(var x: Float, var y: Float, var z: Float)

@Suppress("ClassName")
sealed interface GenshinType<T> {
    fun encode(side: Side, value: T?): TypedValue
    fun getTypeId(side: Side): Int

    fun unwrap() = this

    object BOOLEAN: Basic<Boolean>(
        ServerTypeId.S_BOOLEAN,
        ClientTypeId.C_BOOLEAN,
        TypedValue.WidgetType.ENUM_PICKER
    ) {
        override fun encode0(builder: TypedValue.Builder, value: Boolean) {
            builder.valEnum = Enum.newBuilder().setValue((if (value) 1L else 0L)).build()
        }
    }
    object INT: Basic<Int>(
        ServerTypeId.S_INT,
        ClientTypeId.C_INT,
        TypedValue.WidgetType.NUMBER_INPUT
    ) {
        override fun encode0(builder: TypedValue.Builder, value: Int) {
            builder.valInt = mz.genshincode.data.asset.Int.newBuilder().setValue(value).build()
        }
    }
    object STRING: Basic<String>(
        ServerTypeId.S_STRING,
        ClientTypeId.C_STRING,
        TypedValue.WidgetType.TEXT_INPUT
    ) {
        override fun encode0(builder: TypedValue.Builder, value: String) {
            builder.valString = Str.newBuilder().setValue(value).build()
        }
    }
    object ENTITY: Basic<Entity>(ServerTypeId.S_ENTITY, ClientTypeId.C_ENTITY) {
        override fun encode0(builder: TypedValue.Builder, value: Entity) {}
    }
    object FLOAT: Basic<Float>(
        ServerTypeId.S_FLOAT,
        ClientTypeId.C_FLOAT,
        TypedValue.WidgetType.DECIMAL_INPUT
    ) {
        override fun encode0(builder: TypedValue.Builder, value: Float) {
            builder.valFloat = Flt.newBuilder().setValue(value).build()
        }
    }
    object VECTOR: Basic<Vector>(
        ServerTypeId.S_VECTOR,
        ClientTypeId.C_VECTOR,
        TypedValue.WidgetType.VECTOR_GROUP
    ) {
        override fun encode0(builder: TypedValue.Builder, value: Vector) {
            builder.valVector = Vec.newBuilder().setValue(Value.newBuilder().setX(value.x).setY(value.y).setZ(value.z)).build()
        }
    }
    object GUID: BasicId<Guid>(
        ServerTypeId.S_GUID,
        ClientTypeId.C_GUID
    )
    object CONFIG: BasicId<Config>(
        ServerTypeId.S_CONFIG,
        ClientTypeId.C_CONFIG
    )
    object PREFAB: BasicId<Prefab>(
        ServerTypeId.S_PREFAB,
        ClientTypeId.C_PREFAB
    )
    object FACTION: BasicId<Faction>(
        ServerTypeId.S_FACTION,
        ClientTypeId.C_FACTION
    )

    object INT_LIST: ListType<Int>(ServerTypeId.S_INT_LIST, ClientTypeId.C_INT_LIST, INT)
    object STRING_LIST: ListType<String>(ServerTypeId.S_STRING_LIST, ClientTypeId.C_STRING_LIST, STRING)
    object ENTITY_LIST: ListType<Entity>(ServerTypeId.S_ENTITY_LIST, ClientTypeId.C_ENTITY_LIST, ENTITY)
    object GUID_LIST: ListType<Guid>(ServerTypeId.S_GUID_LIST, ClientTypeId.C_GUID_LIST, GUID)
    object FLOAT_LIST: ListType<Float>(ServerTypeId.S_FLOAT_LIST, ClientTypeId.C_FLOAT_LIST, FLOAT)
    object VECTOR_LIST: ListType<Vector>(ServerTypeId.S_VECTOR_LIST, ClientTypeId.C_VECTOR_LIST, VECTOR)
    object BOOLEAN_LIST: ListType<Boolean>(ServerTypeId.S_BOOLEAN_LIST, ClientTypeId.C_BOOLEAN_LIST, BOOLEAN)
    object CONFIG_LIST: ListType<Config>(ServerTypeId.S_CONFIG_LIST, ClientTypeId.C_CONFIG_LIST, CONFIG)

    interface Server {
        object LOCAL: Basic<ServerLocal>(ServerTypeId.S_LOCAL_VAR_REF, ClientTypeId.CLIENT_UNKNOWN)

        object PREFAB_LIST: ListType<Prefab>(ServerTypeId.S_PREFAB_LIST, ClientTypeId.CLIENT_UNKNOWN, PREFAB)
        object FACTION_LIST: ListType<Faction>(ServerTypeId.S_FACTION_LIST, ClientTypeId.CLIENT_UNKNOWN, FACTION)
    }

    interface Client {

    }

    data class Selected<T>(val index: Int, val impl: GenshinType<T>) : GenshinType<T> {
        override fun encode(side: Side, value: T?): TypedValue {
            return TypedValue.newBuilder().apply {
                widget = TypedValue.WidgetType.TYPE_SELECTOR
                isSet = true
                valPoly = PolymorphicValue.newBuilder().apply {
                    chosenTypeIndex = index
                    actualValue = impl.encode(side, value)
                }.build()
            }.build()
        }

        override fun getTypeId(side: Side) =
            impl.getTypeId(side)

        override fun unwrap() = impl
    }

    sealed class Basic<T>(val serverType: ServerTypeId, val clientType: ClientTypeId, val widgetType: TypedValue.WidgetType): GenshinType<T> {
        constructor(serverType: ServerTypeId, clientType: ClientTypeId): this(serverType, clientType, TypedValue.WidgetType.UNKNOWN)

        open fun encode0(builder: TypedValue.Builder, value: T): Unit = throw UnsupportedOperationException()

        override fun encode(side: Side, value: T?): TypedValue {
            val builder = TypedValue.newBuilder()
                .setWidget(widgetType)
                .setIsSet(value != null)
                .setType(
                    TypeDefinition.newBuilder()
                        .setBackend(if (side == Side.SERVER) TypeDefinition.Backend.SERVER else TypeDefinition.Backend.CLIENT)
                        .setServerSide(
                            TypeDefinition.ServerType.newBuilder()
                                .setTypeTagValue(this.getTypeId(side))
                        )
                )
            if(value != null)
                encode0(builder, value)
            return builder.build()
        }

        override fun getTypeId(side: Side): Int = when (side) {
            Side.SERVER -> serverType.getNumber()
            Side.CLIENT -> clientType.getNumber()
        }
    }
    sealed class BasicId<T: IdBased>(serverType: ServerTypeId, clientType: ClientTypeId):
        Basic<T>(serverType, clientType, TypedValue.WidgetType.ID_INPUT) {
        override fun encode0(builder: TypedValue.Builder, value: T) {
            builder.valId = Id.newBuilder().setValue(value.id).build()
        }
    }

    sealed class ListType<T>(val serverType: ServerTypeId, val clientType: ClientTypeId, val elementType: GenshinType<T>): GenshinType<List<T>> {
        override fun encode(side: Side, value: List<T>?): TypedValue {
            val builder = TypedValue.newBuilder()
                .setWidget(TypedValue.WidgetType.LIST_GROUP)
                .setIsSet(value != null)
                .setType(
                    TypeDefinition.newBuilder()
                        .setBackend(if (side == Side.SERVER) TypeDefinition.Backend.SERVER else TypeDefinition.Backend.CLIENT)
                        .setServerSide(
                            TypeDefinition.ServerType.newBuilder()
                                .setTypeTagValue(this.getTypeId(side))
                        )
                )
            if(value != null)
                builder.setValList(ListStorage.newBuilder().addAllElement(value.stream().map { i ->
                    TypedValue.newBuilder(
                        elementType.encode(
                            side, i
                        )
                    ).build()
                }.collect(Collectors.toList())))
            return builder.build()
        }

        override fun getTypeId(side: Side): Int = when (side) {
            Side.SERVER -> serverType.getNumber()
            Side.CLIENT -> clientType.getNumber()
        }
    }
}