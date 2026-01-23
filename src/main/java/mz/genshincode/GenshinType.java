package mz.genshincode;

import mz.genshincode.data.asset.*;
import mz.genshincode.data.asset.Enum;

import java.lang.Float;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public interface GenshinType<T>
{
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    TypedValue encode(GenshinSide side, Optional<T> value);
    int getTypeId(GenshinSide side);

    GenshinType<Boolean> BOOL = basic(ServerTypeId.S_BOOL, ClientTypeId.C_BOOL, TypedValue.WidgetType.ENUM_PICKER, (builder, it) -> builder.setValEnum(Enum.newBuilder().setValue(it ? 1 : 0)));
    GenshinType<Integer> INT = basic(ServerTypeId.S_INT, ClientTypeId.C_INT, TypedValue.WidgetType.NUMBER_INPUT, (builder, it) -> builder.setValInt(Int.newBuilder().setValue(it)));
    GenshinType<String> STRING = basic(ServerTypeId.S_STRING, ClientTypeId.C_STRING, TypedValue.WidgetType.TEXT_INPUT, (builder, it) -> builder.setValString(Str.newBuilder().setValue(it)));
    GenshinType<Entity> ENTITY = basic(ServerTypeId.S_ENTITY, ClientTypeId.C_ENTITY);
    GenshinType<Float> FLOAT = basic(ServerTypeId.S_FLOAT, ClientTypeId.C_FLOAT, TypedValue.WidgetType.DECIMAL_INPUT, (builder, it) -> builder.setValFloat(Flt.newBuilder().setValue(it)));
    GenshinType<Vector> VECTOR = basic(ServerTypeId.S_VECTOR, ClientTypeId.C_VECTOR, TypedValue.WidgetType.VECTOR_GROUP, (builder, it) -> builder.setValVector(it.toData()));
    GenshinType<Guid> GUID = basic(ServerTypeId.S_GUID, ClientTypeId.C_GUID, TypedValue.WidgetType.ID_INPUT, IdBased.encoder());
    GenshinType<Config> CONFIG = basic(ServerTypeId.S_CONFIG, ClientTypeId.C_CONFIG, TypedValue.WidgetType.ID_INPUT, IdBased.encoder());
    GenshinType<Prefab> PREFAB = basic(ServerTypeId.S_PREFAB, ClientTypeId.C_PREFAB, TypedValue.WidgetType.ID_INPUT, IdBased.encoder());
    GenshinType<Faction> FACTION = basic(ServerTypeId.S_FACTION, ClientTypeId.C_FACTION, TypedValue.WidgetType.ID_INPUT, IdBased.encoder());

    GenshinType<List<Integer>> INT_LIST = basic(ServerTypeId.S_INT_LIST, ClientTypeId.C_INT_LIST, TypedValue.WidgetType.LIST_GROUP, encoderList(INT));
    GenshinType<List<String>> STRING_LIST = basic(ServerTypeId.S_STRING_LIST, ClientTypeId.C_STRING_LIST, TypedValue.WidgetType.LIST_GROUP, encoderList(STRING));
    GenshinType<List<Entity>> ENTITY_LIST = basic(ServerTypeId.S_ENTITY_LIST, ClientTypeId.C_ENTITY_LIST, TypedValue.WidgetType.LIST_GROUP, (builder, it) -> Util.valueThrow(new UnsupportedOperationException()));
    GenshinType<List<Guid>> GUID_LIST = basic(ServerTypeId.S_GUID_LIST, ClientTypeId.C_GUID_LIST, TypedValue.WidgetType.LIST_GROUP, encoderList(GUID));
    GenshinType<List<Float>> FLOAT_LIST = basic(ServerTypeId.S_FLOAT_LIST, ClientTypeId.C_FLOAT_LIST, TypedValue.WidgetType.LIST_GROUP, encoderList(FLOAT));
    GenshinType<List<Vector>> VECTOR_LIST = basic(ServerTypeId.S_VECTOR_LIST, ClientTypeId.C_VECTOR_LIST, TypedValue.WidgetType.LIST_GROUP, encoderList(VECTOR));
    GenshinType<List<Boolean>> BOOL_LIST = basic(ServerTypeId.S_BOOL_LIST, ClientTypeId.C_BOOL_LIST, TypedValue.WidgetType.LIST_GROUP, encoderList(BOOL));
    GenshinType<List<Config>> CONFIG_LIST = basic(ServerTypeId.S_CONFIG_LIST, ClientTypeId.C_CONFIG_LIST, TypedValue.WidgetType.LIST_GROUP, encoderList(CONFIG));

    interface Server
    {
        GenshinType<Local> LOCAL = basic(ServerTypeId.S_LOCAL_VAR_REF, ClientTypeId.CLIENT_UNKNOWN);

        GenshinType<List<Prefab>> PREFAB_LISt = basic(ServerTypeId.S_PREFAB_LIST, ClientTypeId.CLIENT_UNKNOWN, TypedValue.WidgetType.LIST_GROUP, encoderList(PREFAB));
        GenshinType<List<Faction>> FACTION_LIST = basic(ServerTypeId.S_FACTION_LIST, ClientTypeId.CLIENT_UNKNOWN, TypedValue.WidgetType.LIST_GROUP, encoderList(FACTION));

        final class Local
        {
            private Local()
            {
            }
        }
    }
    interface Client
    {
        // TODO
    }

    static <T> BiConsumer<TypedValue.Builder, List<T>> encoderList(GenshinType<T> elementType)
    {
        return (builder, it) -> encodeList(builder, it, elementType);
    }
    static <T> void encodeList(TypedValue.Builder builder, List<T> value, GenshinType<T> elementType)
    {
        builder.setValList(ListStorage.newBuilder().addAllElement(value.stream().map(i -> TypedValue.newBuilder(elementType.encode(GenshinSide.SERVER, Optional.of(i)))
            .clearWidget()
            .clearIsSet()
            .clearType()
            .build()).collect(Collectors.toList())));
    }

    abstract class IdBased
    {
        long id;
        IdBased(long id)
        {
            this.id = id;
        }
        public long getId()
        {
            return id;
        }
        static <T extends IdBased> BiConsumer<TypedValue.Builder, T> encoder()
        {
            return (builder, value) -> builder.setValId(Id.newBuilder().setValue(value.getId()));
        }
    }
    final class Guid extends IdBased
    {
        public Guid(long id)
        {
            super(id);
        }
    }
    final class Config extends IdBased
    {
        public Config(long id)
        {
            super(id);
        }
    }
    final class Prefab extends IdBased
    {
        public Prefab(long id)
        {
            super(id);
        }
    }
    final class Faction extends IdBased
    {
        public Faction(long id)
        {
            super(id);
        }
    }
    final class Entity
    {
        private Entity()
        {
        }
    }
    final class Vector
    {
        float x, y, z;
        public Vector(float x, float y, float z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        public float getX()
        {
            return x;
        }
        public float getY()
        {
            return y;
        }
        public float getZ()
        {
            return z;
        }
        Vec toData()
        {
            return Vec.newBuilder().setValue(Vec.Value.newBuilder().setX(this.x).setY(this.y).setZ(this.z)).build();
        }
    }

    static <T> Selected<T> selected(int index, GenshinType<T> impl)
    {
        return new Selected<>(index, impl);
    }
    class Selected<T> implements GenshinType<T>
    {
        int index;
        GenshinType<T> impl;
        public Selected(int index, GenshinType<T> impl)
        {
            this.index = index;
            this.impl = impl;
        }

        @Override
        public TypedValue encode(GenshinSide side, Optional<T> value)
        {
            return TypedValue.newBuilder()
                .setWidget(TypedValue.WidgetType.TYPE_SELECTOR)
                .setIsSet(true)
                .setValPoly(PolymorphicValue.newBuilder()
                    .setChosenTypeIndex(this.index)
                    .setActualValue(this.impl.encode(side, value))
                )
                .build();
        }

        @Override
        public int getTypeId(GenshinSide side)
        {
            return this.impl.getTypeId(side);
        }

        @Override
        public boolean equals(Object o)
        {
            if(!(o instanceof GenshinType.Selected))
                return false;
            Selected<?> that = (Selected<?>) o;
            return index == that.index && Objects.equals(impl, that.impl);
        }
        @Override
        public int hashCode()
        {
            return Objects.hash(index, impl);
        }
    }
    static <T> GenshinType<T> basic(ServerTypeId server, ClientTypeId client)
    {
        return basic(server, client, TypedValue.WidgetType.UNKNOWN, (builder, it) -> Util.valueThrow(new UnsupportedOperationException()));
    }
    static <T> GenshinType<T> basic(ServerTypeId server, ClientTypeId client, TypedValue.WidgetType widgetType, BiConsumer<TypedValue.Builder, T> setter)
    {
        return new GenshinType<T>()
        {
            @Override
            public TypedValue encode(GenshinSide side, Optional<T> value)
            {
                TypedValue.Builder builder = TypedValue.newBuilder()
                    .setWidget(widgetType)
                    .setIsSet(value.isPresent())
                    .setType(TypeDefinition.newBuilder()
                        .setBackend(side == GenshinSide.SERVER ? TypeDefinition.Backend.SERVER : TypeDefinition.Backend.CLIENT)
                        .setServerSide(TypeDefinition.ServerType.newBuilder()
                            .setTypeTagValue(this.getTypeId(side))
                        )
                    );
                value.ifPresent(it -> setter.accept(builder, it));
                return builder.build();
            }
            @Override
            public int getTypeId(GenshinSide side)
            {
                switch(side)
                {
                    case SERVER:
                        return server.getNumber();
                    case CLIENT:
                        return client.getNumber();
                    default:
                        throw new UnsupportedOperationException();
                }
            }
        };
    }
}
