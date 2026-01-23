package mz.genshincode.graph;

import mz.genshincode.GenshinType;
import mz.genshincode.data.asset.Identifier;
import mz.genshincode.data.asset.PinSignature;

import java.util.List;
import java.util.Optional;

public interface GraphNodes
{
    interface Exec
    {
        static Statement1_0<String> print()
        {
            return new Statement1_0<>(identifierServer(1), identifierServer(1), GenshinType.STRING);
        }
        static ForInt forInt()
        {
            return new ForInt();
        }
        static Statement0_0 breakLoop()
        {
            return new Statement0_0(identifierServer(6), identifierServer(6));
        }
        static Statement1_0<GenshinType.Entity> forwardEvent()
        {
            return new Statement1_0<>(identifierServer(190), identifierServer(190), GenshinType.ENTITY);
        }
        class ForInt extends GraphNode
        {
            Pin<Void> flowIn;
            Pin<Void> flowBreak;
            Pin<Void> flowBody;
            Pin<Void> flowOut;
            Pin<Integer> inBegin, inEnd, outValue;
            ForInt()
            {
                super(identifierServer(5), identifierServer(5));
                this.flowIn = this.addPin(new PinDefinition<>(PinSignature.Kind.IN_FLOW, 0, Optional.empty()));
                this.flowBreak = this.addPin(new PinDefinition<>(PinSignature.Kind.IN_FLOW, 1, Optional.empty()));
                this.flowBody = this.addPin(new PinDefinition<>(PinSignature.Kind.OUT_FLOW, 0, Optional.empty()));
                this.flowOut = this.addPin(new PinDefinition<>(PinSignature.Kind.OUT_FLOW, 1, Optional.empty()));

                this.inBegin = this.addPin(new PinDefinition<>(PinSignature.Kind.IN_PARAM, 0, Optional.of(GenshinType.INT)));
                this.inEnd = this.addPin(new PinDefinition<>(PinSignature.Kind.IN_PARAM, 1, Optional.of(GenshinType.INT)));
                this.outValue = this.addPin(new PinDefinition<>(PinSignature.Kind.OUT_PARAM, 0, Optional.of(GenshinType.INT)));
            }
            public Pin<Void> getFlowIn()
            {
                return flowIn;
            }
            public Pin<Void> getFlowBreak()
            {
                return flowBreak;
            }
            public Pin<Void> getFlowBody()
            {
                return flowBody;
            }
            public Pin<Void> getFlowOut()
            {
                return flowOut;
            }
            public Pin<Integer> getInBegin()
            {
                return inBegin;
            }
            public Pin<Integer> getInEnd()
            {
                return inEnd;
            }
            public Pin<Integer> getOutValue()
            {
                return outValue;
            }
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
            static Expr1<Boolean, Integer> intToBool()
            {
                return new Expr1<>(identifierServer(180), identifierServer(180), GenshinType.selected(0, GenshinType.BOOL), GenshinType.selected(0, GenshinType.INT));
            }
            static Expr1<Float, Integer> intToFloat()
            {
                return new Expr1<>(identifierServer(180), identifierServer(181), GenshinType.selected(1, GenshinType.FLOAT), GenshinType.selected(0, GenshinType.INT));
            }
            static Expr1<String, Integer> intToString()
            {
                return new Expr1<>(identifierServer(180), identifierServer(182), GenshinType.selected(2, GenshinType.STRING), GenshinType.selected(0, GenshinType.INT));
            }
            static Expr1<String, GenshinType.Entity> entityToString()
            {
                return new Expr1<>(identifierServer(180), identifierServer(183), GenshinType.selected(2, GenshinType.STRING), GenshinType.selected(1, GenshinType.ENTITY));
            }
            static Expr1<String, GenshinType.Guid> guidToString()
            {
                return new Expr1<>(identifierServer(180), identifierServer(184), GenshinType.selected(2, GenshinType.STRING), GenshinType.selected(2, GenshinType.GUID));
            }
            static Expr1<Integer, Boolean> boolToInt()
            {
                return new Expr1<>(identifierServer(180), identifierServer(185), GenshinType.selected(3, GenshinType.INT), GenshinType.selected(3, GenshinType.BOOL));
            }
            static Expr1<String, Boolean> boolToString()
            {
                return new Expr1<>(identifierServer(180), identifierServer(186), GenshinType.selected(2, GenshinType.STRING), GenshinType.selected(3, GenshinType.BOOL));
            }
            static Expr1<Integer, Float> floatToInt()
            {
                return new Expr1<>(identifierServer(180), identifierServer(187), GenshinType.selected(3, GenshinType.INT), GenshinType.selected(4, GenshinType.FLOAT));
            }
            static Expr1<String, Float> floatToString()
            {
                return new Expr1<>(identifierServer(180), identifierServer(188), GenshinType.selected(2, GenshinType.STRING), GenshinType.selected(4, GenshinType.FLOAT));
            }
            static Expr1<String, GenshinType.Vector> vectorToString()
            {
                return new Expr1<>(identifierServer(180), identifierServer(189), GenshinType.selected(2, GenshinType.STRING), GenshinType.selected(5, GenshinType.VECTOR));
            }
            static Expr1<String, GenshinType.Faction> factionToString()
            {
                return new Expr1<>(identifierServer(180), identifierServer(255), GenshinType.selected(2, GenshinType.STRING), GenshinType.selected(6, GenshinType.FACTION));
            }
        }
    }
    interface Server
    {
        interface Exec
        {
            interface Local
            {
                static Statement2_0<GenshinType.Server.Local, Boolean> setBool()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(19), GenshinType.Server.LOCAL, GenshinType.selected(0, GenshinType.BOOL));
                }
                static Statement2_0<GenshinType.Server.Local, Integer> setInt()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(21), GenshinType.Server.LOCAL, GenshinType.selected(1, GenshinType.INT));
                }
                static Statement2_0<GenshinType.Server.Local, String> setString()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2674), GenshinType.Server.LOCAL, GenshinType.selected(2, GenshinType.STRING));
                }
                static Statement2_0<GenshinType.Server.Local, GenshinType.Entity> setEntity()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2675), GenshinType.Server.LOCAL, GenshinType.selected(3, GenshinType.ENTITY));
                }
                static Statement2_0<GenshinType.Server.Local, GenshinType.Guid> setGuid()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2676), GenshinType.Server.LOCAL, GenshinType.selected(4, GenshinType.GUID));
                }
                static Statement2_0<GenshinType.Server.Local, Float> setFloat()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2677), GenshinType.Server.LOCAL, GenshinType.selected(5, GenshinType.FLOAT));
                }
                static Statement2_0<GenshinType.Server.Local, GenshinType.Vector> setVector()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2678), GenshinType.Server.LOCAL, GenshinType.selected(6, GenshinType.VECTOR));
                }
                static Statement2_0<GenshinType.Server.Local, List<Integer>> setIntList()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2679), GenshinType.Server.LOCAL, GenshinType.selected(7, GenshinType.INT_LIST));
                }
                static Statement2_0<GenshinType.Server.Local, List<String>> setStringList()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2680), GenshinType.Server.LOCAL, GenshinType.selected(8, GenshinType.STRING_LIST));
                }
                static Statement2_0<GenshinType.Server.Local, List<GenshinType.Entity>> setEntityList()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2681), GenshinType.Server.LOCAL, GenshinType.selected(9, GenshinType.ENTITY_LIST));
                }
                static Statement2_0<GenshinType.Server.Local, List<GenshinType.Guid>> setGuidList()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2682), GenshinType.Server.LOCAL, GenshinType.selected(10, GenshinType.GUID_LIST));
                }
                static Statement2_0<GenshinType.Server.Local, List<Float>> setFloatList()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2683), GenshinType.Server.LOCAL, GenshinType.selected(11, GenshinType.FLOAT_LIST));
                }
                static Statement2_0<GenshinType.Server.Local, List<GenshinType.Vector>> setVectorList()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2684), GenshinType.Server.LOCAL, GenshinType.selected(12, GenshinType.VECTOR_LIST));
                }
                static Statement2_0<GenshinType.Server.Local, List<Boolean>> setBoolList()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2685), GenshinType.Server.LOCAL, GenshinType.selected(13, GenshinType.BOOL_LIST));
                }
                static Statement2_0<GenshinType.Server.Local, GenshinType.Config> setConfig()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2686), GenshinType.Server.LOCAL, GenshinType.selected(14, GenshinType.CONFIG));
                }
                static Statement2_0<GenshinType.Server.Local, GenshinType.Prefab> setPrefab()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2687), GenshinType.Server.LOCAL, GenshinType.selected(15, GenshinType.PREFAB));
                }
                static Statement2_0<GenshinType.Server.Local, List<GenshinType.Config>> setConfigList()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2688), GenshinType.Server.LOCAL, GenshinType.selected(16, GenshinType.CONFIG_LIST));
                }
                static Statement2_0<GenshinType.Server.Local, List<GenshinType.Prefab>> setPrefabList()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2689), GenshinType.Server.LOCAL, GenshinType.selected(17, GenshinType.Server.PREFAB_LIST));
                }
                static Statement2_0<GenshinType.Server.Local, GenshinType.Faction> setFaction()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2690), GenshinType.Server.LOCAL, GenshinType.selected(18, GenshinType.FACTION));
                }
                static Statement2_0<GenshinType.Server.Local, List<GenshinType.Faction>> setFactionList()
                {
                    return new Statement2_0<>(identifierServer(19), identifierServer(2691), GenshinType.Server.LOCAL, GenshinType.selected(19, GenshinType.Server.FACTION_LIST));
                }
            }
        }
        interface Query
        {
            interface Local
            {
                static Node<Boolean> getBool()
                {
                    return new Node<>(identifierServer(18), 0, GenshinType.BOOL);
                }
                static Node<Integer> getInt()
                {
                    return new Node<>(identifierServer(20), 1, GenshinType.INT);
                }
                static Node<String> getString()
                {
                    return new Node<>(identifierServer(2656), 2, GenshinType.STRING);
                }
                static Node<GenshinType.Entity> getEntity()
                {
                    return new Node<>(identifierServer(2657), 3, GenshinType.ENTITY);
                }
                static Node<GenshinType.Guid> getGuid()
                {
                    return new Node<>(identifierServer(2658), 4, GenshinType.GUID);
                }
                static Node<Float> getFloat()
                {
                    return new Node<>(identifierServer(2659), 5, GenshinType.FLOAT);
                }
                static Node<GenshinType.Vector> getVector()
                {
                    return new Node<>(identifierServer(2660), 6, GenshinType.VECTOR);
                }
                static Node<List<Integer>> getIntList()
                {
                    return new Node<>(identifierServer(2661), 7, GenshinType.INT_LIST);
                }
                static Node<List<String>> getStringList()
                {
                    return new Node<>(identifierServer(2662), 8, GenshinType.STRING_LIST);
                }
                static Node<List<GenshinType.Entity>> getEntityList()
                {
                    return new Node<>(identifierServer(2663), 9, GenshinType.ENTITY_LIST);
                }
                static Node<List<GenshinType.Guid>> getGuidList()
                {
                    return new Node<>(identifierServer(2664), 10, GenshinType.GUID_LIST);
                }
                static Node<List<Float>> getFloatList()
                {
                    return new Node<>(identifierServer(2665), 11, GenshinType.FLOAT_LIST);
                }
                static Node<List<GenshinType.Vector>> getVectorList()
                {
                    return new Node<>(identifierServer(2666), 12, GenshinType.VECTOR_LIST);
                }
                static Node<List<Boolean>> getBoolList()
                {
                    return new Node<>(identifierServer(2667), 13, GenshinType.BOOL_LIST);
                }
                static Node<GenshinType.Config> getConfig()
                {
                    return new Node<>(identifierServer(2668), 14, GenshinType.CONFIG);
                }
                static Node<GenshinType.Prefab> getPrefab()
                {
                    return new Node<>(identifierServer(2669), 15, GenshinType.PREFAB);
                }
                static Node<List<GenshinType.Config>> getConfigList()
                {
                    return new Node<>(identifierServer(2670), 16, GenshinType.CONFIG_LIST);
                }
                static Node<List<GenshinType.Prefab>> getPrefabList()
                {
                    return new Node<>(identifierServer(2671), 17, GenshinType.Server.PREFAB_LIST);
                }
                static Node<GenshinType.Faction> getFaction()
                {
                    return new Node<>(identifierServer(2672), 18, GenshinType.FACTION);
                }
                static Node<List<GenshinType.Faction>> getFactionList()
                {
                    return new Node<>(identifierServer(2673), 19, GenshinType.Server.FACTION_LIST);
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
