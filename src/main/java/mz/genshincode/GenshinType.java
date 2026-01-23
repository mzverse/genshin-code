package mz.genshincode;

import mz.genshincode.data.asset.*;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

public interface GenshinType<T>
{
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    TypedValue encode(GenshinSide side, Optional<T> value);
    int getTypeId(GenshinSide side);

    GenshinType<Integer> INT = common(ServerTypeId.S_INT, ClientTypeId.C_INT, TypedValue.WidgetType.NUMBER_INPUT, (builder, it) -> builder.setValInt(Int.newBuilder().setValue(it)));
    GenshinType<String> STRING = common(ServerTypeId.S_STRING, ClientTypeId.C_STRING, TypedValue.WidgetType.TEXT_INPUT, (builder, it) -> builder.setValString(Str.newBuilder().setValue(it)));
    GenshinType<Guid> GUID = common(ServerTypeId.S_GUID, ClientTypeId.C_GUID, TypedValue.WidgetType.ID_INPUT, (builder, it) -> builder.setValId(Id.newBuilder().setValue(it.getValue())));
    GenshinType<Entity> ENTITY = common(ServerTypeId.S_ENTITY, ClientTypeId.C_ENTITY, TypedValue.WidgetType.UNKNOWN, (builder, it) -> Util.valueThrow(new UnsupportedOperationException()));

    final class Guid
    {
        long value;
        public Guid(long value)
        {
            this.value = value;
        }
        public long getValue()
        {
            return this.value;
        }
    }
    final class Entity
    {
        private Entity()
        {
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
    static <T> GenshinType<T> common(ServerTypeId server, ClientTypeId client, TypedValue.WidgetType widgetType, BiConsumer<TypedValue.Builder, T> setter)
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
