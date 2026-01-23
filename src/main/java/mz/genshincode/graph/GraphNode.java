package mz.genshincode.graph;

import mz.genshincode.GenshinSide;
import mz.genshincode.GenshinType;
import mz.genshincode.Util;
import mz.genshincode.data.asset.*;

import java.awt.geom.Point2D;
import java.util.*;

public class GraphNode
{
    Identifier idShell, idKernel;

    Set<Pin<?>> pins;

    public GraphNode(Identifier idShell, Identifier idKernel)
    {
        this.idShell = idShell;
        this.idKernel = idKernel;

        this.pins = new HashSet<>();
    }

    Point2D.Float pos = new Point2D.Float(0.f, 0.f);
    public void setPosition(Point2D.Float value)
    {
        this.pos = value;
    }

    public <T> Pin<T> addPin(PinDefinition<T> definition)
    {
        Pin<T> result = definition.newPin();
        this.pins.add(result);
        return result;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static class Pin<T>
    {
        PinDefinition<T> definition;
        Optional<T> value;
        Set<Map.Entry<GraphNode, Pin<?>>> connections;
        public Pin(PinDefinition<T> definition, Optional<T> value, Set<Map.Entry<GraphNode, Pin<?>>> connections)
        {
            this.definition = definition;
            this.value = value;
            this.connections = connections;
        }
        public Pin(PinDefinition<T> definition)
        {
            this(definition, Optional.empty(), new HashSet<>());
        }

        public void setValue(T value)
        {
            this.value = Optional.of(value);
        }
        public void removeValue()
        {
            this.value = Optional.empty();
        }

        Set<Map.Entry<GraphNode, Pin<?>>> getConnections()
        {
            return this.connections;
        }
        void connection(GraphNode node, Pin<T> pin)
        {
            this.connections.add(new AbstractMap.SimpleEntry<>(node, pin));
        }
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static class PinDefinition<T>
    {
        PinSignature signShell, signKernel;
        Optional<GenshinType<T>> type;

        public PinDefinition(PinSignature signShell, PinSignature signKernel, Optional<GenshinType<T>> type)
        {
            this.signShell = signShell;
            this.signKernel = signKernel;
            this.type = type;
        }
        public PinDefinition(PinSignature sign, Optional<GenshinType<T>> type)
        {
            this(sign, sign, type);
        }
        public PinDefinition(PinSignature.Kind kind, int index, Optional<GenshinType<T>> type)
        {
            this(PinSignature.newBuilder().setKind(kind).setIndex(index).build(), type);
        }

        public Pin<T> newPin()
        {
            return new Pin<>(this);
        }

        @Override
        public boolean equals(Object o)
        {
            if(!(o instanceof PinDefinition))
                return false;
            PinDefinition<?> that = (PinDefinition<?>) o;
            return Objects.equals(signShell, that.signShell) &&
                Objects.equals(signKernel, that.signKernel) && Objects.equals(type, that.type);
        }
        @Override
        public int hashCode()
        {
            return Objects.hash(signShell, signKernel, type);
        }
    }

    NodeInstance encode(GenshinSide side, Map<GraphNode, Integer> ids)
    {
        NodeInstance.Builder builder = NodeInstance.newBuilder();
        builder.setIndex(ids.get(this));
        builder.setShellRef(this.idShell);
        builder.setKernelRef(this.idKernel);
        builder.setXPos(this.pos.x);
        builder.setYPos(this.pos.y);
        for(Pin<?> i : this.pins)
        {
            Util.consume(i, pin ->
            {
                PinData.Builder data = PinData.newBuilder()
                    .setShellSig(pin.definition.signShell)
                    .setKernelSig(pin.definition.signKernel);
                pin.definition.type.ifPresent(type ->
                {
                    data.setType(type.getTypeId(side));
                    data.setValue(type.encode(side, pin.value));
                });
                for(Map.Entry<GraphNode, Pin<?>> connection : pin.connections)
                {
                    data.addConnection(NodeConnection.newBuilder()
                        .setTargetNodeIndex(ids.get(connection.getKey()))
                        .setTargetPinShell(connection.getValue().definition.signShell)
                        .setTargetPinKernel(connection.getValue().definition.signKernel)
                    );
                }
                builder.addPin(data);
            });
        }
        return builder.build();
    }
}
