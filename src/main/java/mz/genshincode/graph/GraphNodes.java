package mz.genshincode.graph;

import mz.genshincode.GenshinType;
import mz.genshincode.data.asset.Identifier;
import mz.genshincode.data.asset.PinSignature;

import java.util.Optional;

public interface GraphNodes
{
    interface Exec
    {
        static Statement1_0<String> print()
        {
            return new Statement1_0<>(identifierServer(1), identifierServer(1), GenshinType.STRING);
        }
    }
    interface Event
    {
        interface Entity
        {
            static Trigger2<GenshinType.Entity, GenshinType.Guid> onCreate()
            {
                return new Trigger2<>(identifierServer(71), identifierServer(71), GenshinType.ENTITY, GenshinType.GUID);
            }
        }
    }
    interface Calc
    {
        interface Cast
        {
            static Expr1<String, Integer> intToString()
            {
                return new Expr1<>(identifierServer(180), identifierServer(182), GenshinType.selected(2, GenshinType.STRING), GenshinType.selected(0, GenshinType.INT));
            }
            static Expr1<String, GenshinType.Entity> entityToString()
            {
                return new Expr1<>(identifierServer(180), identifierServer(183), GenshinType.selected(2, GenshinType.STRING), GenshinType.selected(1, GenshinType.ENTITY));
            }
        }
    }
    interface Server
    {
        interface Exec
        {
            interface Local
            {
                static Statement2_0<GenshinType.Server.Local, String> setString()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2674), GenshinType.Server.LOCAL, GenshinType.selected(2, GenshinType.STRING));
                }
            }
        }
        interface Query
        {
            interface Local
            {
                static Node<String> getString()
                {
                    return new Node<>(identifierServer(2656), 2, GenshinType.STRING);
                }
                class Node<T> extends GraphNode
                {
                    Pin<T> inInit;
                    Pin<GenshinType.Server.Local> outLocal;
                    Pin<T> outValue;
                    public Node(Identifier idKernel, GenshinType<T> type)
                    {
                        super(identifierServer(18), idKernel);
                        this.inInit = this.addPin(new PinDefinition<>(PinSignature.Kind.IN_PARAM, 0, Optional.of(type)));
                        this.outLocal = this.addPin(new PinDefinition<>(PinSignature.Kind.OUT_PARAM, 0, Optional.of(GenshinType.Server.LOCAL)));
                        this.outValue = this.addPin(new PinDefinition<>(PinSignature.Kind.OUT_PARAM, 1, Optional.of(type)));
                    }
                    public Node(Identifier idKernel, int selected, GenshinType<T> type)
                    {
                        this(idKernel, GenshinType.selected(selected, type));
                    }
                    public Pin<T> getInInit()
                    {
                        return inInit;
                    }
                    public Pin<GenshinType.Server.Local> getOutLocal()
                    {
                        return outLocal;
                    }
                    public Pin<T> getOutValue()
                    {
                        return outValue;
                    }
                }
            }
        }
    }
    interface Client
    {
    }

    static Identifier identifierServer(long id)
    {
        return Identifier.newBuilder()
            .setSource(Identifier.Source.SYSTEM_DEFINED)
            .setCategory(Identifier.Category.SERVER_BASIC)
            .setKind(Identifier.AssetKind.SYS_CALL_STUB)
            .setRuntimeId(id)
            .build();
    }

    class Expr0<O> extends GraphNode
    {
        Pin<O> out;
        public Expr0(Identifier idShell, Identifier idKernel, GenshinType<O> out)
        {
            super(idShell, idKernel);
            this.out = this.addPin(new PinDefinition<>(PinSignature.Kind.OUT_PARAM, 0, Optional.of(out)));
        }
        public Pin<O> getOut()
        {
            return out;
        }
    }
    class Expr1<O, I0> extends Expr0<O>
    {
        Pin<I0> in0;
        public Expr1(Identifier idShell, Identifier idKernel, GenshinType<O> out, GenshinType<I0> in0)
        {
            super(idShell, idKernel, out);
            this.in0 = this.addPin(new PinDefinition<>(PinSignature.Kind.IN_PARAM, 0, Optional.of(in0)));
        }
        public Pin<I0> getIn0()
        {
            return in0;
        }
    }
    class Expr2<O, I0, I1> extends Expr1<O, I0>
    {
        Pin<I1> in1;
        public Expr2(Identifier idShell, Identifier idKernel, GenshinType<O> out, GenshinType<I0> in0, GenshinType<I1> in1)
        {
            super(idShell, idKernel, out, in0);
            this.in1 = this.addPin(new PinDefinition<>(PinSignature.Kind.IN_PARAM, 1, Optional.of(in1)));
        }
        public Pin<I1> getIn1()
        {
            return in1;
        }
    }

    class Statement0_0 extends GraphNode
    {
        Pin<Void> flowIn, flowOut;
        public Statement0_0(Identifier idShell, Identifier idKernel)
        {
            super(idShell, idKernel);
            this.flowIn = this.addPin(new PinDefinition<>(PinSignature.Kind.IN_FLOW, 0, Optional.empty()));
            this.flowOut = this.addPin(new PinDefinition<>(PinSignature.Kind.OUT_FLOW, 0, Optional.empty()));
        }
        public Pin<Void> getFlowIn()
        {
            return flowIn;
        }
        public Pin<Void> getFlowOut()
        {
            return flowOut;
        }
    }
    class Statement1_0<I0> extends Statement0_0
    {
        Pin<I0> in0;
        public Statement1_0(Identifier idShell, Identifier idKernel, GenshinType<I0> in0)
        {
            super(idShell, idKernel);
            this.in0 = this.addPin(new PinDefinition<>(PinSignature.Kind.IN_PARAM, 0, Optional.of(in0)));
        }
        public Pin<I0> getIn0()
        {
            return in0;
        }
    }
    class Statement2_0<I0, I1> extends Statement1_0<I0>
    {
        Pin<I1> in1;
        public Statement2_0(Identifier idShell, Identifier idKernel, GenshinType<I0> in0, GenshinType<I1> in1)
        {
            super(idShell, idKernel, in0);
            this.in1 = this.addPin(new PinDefinition<>(PinSignature.Kind.IN_PARAM, 1, Optional.of(in1)));
        }
        public Pin<I1> getIn1()
        {
            return in1;
        }
    }

    class Trigger0 extends GraphNode
    {
        Pin<Void> flow;
        public Trigger0(Identifier idShell, Identifier idKernel)
        {
            super(idShell, idKernel);
            this.flow = this.addPin(new PinDefinition<>(PinSignature.Kind.OUT_FLOW, 0, Optional.empty()));
        }
        public Pin<Void> getFlow()
        {
            return flow;
        }
    }
    class Trigger1<D0> extends Trigger0
    {
        Pin<D0> data0;
        public Trigger1(Identifier idShell, Identifier idKernel, GenshinType<D0> data0)
        {
            super(idShell, idKernel);
            this.data0 = this.addPin(new PinDefinition<>(PinSignature.Kind.OUT_PARAM, 0, Optional.of(data0)));
        }
        public Pin<D0> getData0()
        {
            return data0;
        }
    }
    class Trigger2<D0, D1> extends Trigger1<D0>
    {
        Pin<D1> data1;
        public Trigger2(Identifier idShell, Identifier idKernel, GenshinType<D0> data0, GenshinType<D1> data1)
        {
            super(idShell, idKernel, data0);
            this.data1 = this.addPin(new PinDefinition<>(PinSignature.Kind.OUT_PARAM, 1, Optional.of(data1)));
        }
        public Pin<D1> getData1()
        {
            return data1;
        }
    }
}
