package mz.genshincode.graph;

import mz.genshincode.Side;
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
        Pin<T> result = this.new Pin<>(definition);
        this.pins.add(result);
        return result;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public class Pin<T>
    {
        PinDefinition<T> definition;
        Optional<T> value;
        List<Pin<?>> connections;
        public Pin(PinDefinition<T> definition, Optional<T> value, List<Pin<?>> connections)
        {
            this.definition = definition;
            this.value = value;
            this.connections = connections;
        }
        public Pin(PinDefinition<T> definition)
        {
            this(definition, Optional.empty(), new ArrayList<>());
        }
        public GraphNode getNode()
        {
            return GraphNode.this;
        }

        public Optional<GenshinType<T>> getType()
        {
            return this.definition.type;
        }

        public void setValue(T value)
        {
            this.value = Optional.of(value);
        }
        public void removeValue()
        {
            this.value = Optional.empty();
        }

        public List<Pin<?>> getConnections()
        {
            return this.connections;
        }

        public void connect(Pin<T> that)
        {
            this.connect0(that);
            that.connect0(this);
        }
        public void connectAll(List<Pin<T>> those)
        {
            those.forEach(this::connect);
        }
        void connect0(Pin<T> pin)
        {
            this.connections.add(pin);
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

    NodeInstance encode(Side side, Map<GraphNode, Integer> ids)
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
                for(Pin<?> connection : pin.connections)
                {
                    data.addConnection(NodeConnection.newBuilder()
                        .setTargetNodeIndex(ids.get(connection.getNode()))
                        .setTargetPinShell(connection.definition.signShell)
                        .setTargetPinKernel(connection.definition.signKernel)
                    );
                }
                builder.addPin(data);
            });
        }
        return builder.build();
    }
}
