@file:Suppress("unused")

package mz.genshincode.graph

import mz.genshincode.Entity
import mz.genshincode.GenshinType
import mz.genshincode.Guid
import mz.genshincode.ServerLocal
import mz.genshincode.data.asset.Identifier
import mz.genshincode.data.asset.PinSignature
import java.util.*

@Suppress("ClassName")
interface GraphNodes {
    interface Server {
        interface Exec {
            interface Local {
                companion object {
                    fun <T> node(idKernel: Long, selected: Int, type: GenshinType<T>): Statement2_0<ServerLocal, T> =
                        Statement2_0(
                            identifierServer(19),
                            identifierServer(idKernel),
                            GenshinType.Server.LOCAL,
                            GenshinType.Selected(selected, type)
                        )

                    fun setBoolean() = node(19, 0, GenshinType.BOOLEAN)
                    fun setInt() = node(21, 1, GenshinType.INT)
                    fun setString() = node(2674, 2, GenshinType.STRING)
                    fun setEntity() = node(2675, 3, GenshinType.ENTITY)
                    fun setGuid() = node(2676, 4, GenshinType.GUID)
                    fun setFloat() = node(2677, 5, GenshinType.FLOAT)
                    fun setVector() = node(2678, 6, GenshinType.VECTOR)
                    fun setIntList() = node(2679, 7, GenshinType.INT_LIST)
                    fun setStringList() = node(2680, 8, GenshinType.STRING_LIST)
                    fun setEntityList() = node(2681, 9, GenshinType.ENTITY_LIST)
                    fun setGuidList() = node(2682, 10, GenshinType.GUID_LIST)
                    fun setFloatList() = node(2683, 11, GenshinType.FLOAT_LIST)
                    fun setVectorList() = node(2684, 12, GenshinType.VECTOR_LIST)
                    fun setBooleanList() = node(2685, 13, GenshinType.BOOLEAN_LIST)
                    fun setConfig() = node(2686, 14, GenshinType.CONFIG)
                    fun setPrefab() = node(2687, 15, GenshinType.PREFAB)
                    fun setConfigList() = node(2688, 16, GenshinType.CONFIG_LIST)
                    fun setPrefabList() = node(2689, 17, GenshinType.Server.PREFAB_LIST)
                    fun setFaction() = node(2690, 18, GenshinType.FACTION)
                    fun setFactionList() = node(2691, 19, GenshinType.Server.FACTION_LIST)
                }
            }

            companion object {
                fun print(): Statement1_0<String> =
                    Statement1_0(identifierServer(1), identifierServer(1), GenshinType.STRING)

                fun forwardEvent(): Statement1_0<Entity> =
                    Statement1_0(identifierServer(190), identifierServer(190), GenshinType.ENTITY)
            }
        }

        interface Event {
            interface Entity {
                companion object {
                    fun onCreate(): Trigger2<mz.genshincode.Entity, Guid> = 
                        Trigger2(
                            identifierServer(71),
                            identifierServer(71),
                            GenshinType.ENTITY,
                            GenshinType.GUID
                        )
                }
            }
        }

        /**
         * 为避免和关键字冲突，控制节点全部使用大驼峰
         */
        @Suppress("FunctionName")
        interface Control {
            companion object {
                fun SwitchInt(countCases: Int): NodeSwitch<Int> =
                    NodeSwitch(3, GenshinType.INT, GenshinType.INT_LIST, 0, countCases)
                fun SwitchString(countCases: Int): NodeSwitch<String> =
                    NodeSwitch(4, GenshinType.STRING, GenshinType.STRING_LIST, 1, countCases)
                fun Break(): Statement0_0 =
                    Statement0_0(identifierServer(6), identifierServer(6))
            }

            class If(): GraphNode(identifierServer(2), identifierServer(2)) {
                val flowIn: Pin<Void> = addPin(PinDefinition(PinSignature.Kind.IN_FLOW, 0, Optional.empty()))
                val flowThen: Pin<Void> = addPin(PinDefinition(PinSignature.Kind.OUT_FLOW, 0, Optional.empty()))
                val flowElse: Pin<Void> = addPin(PinDefinition(PinSignature.Kind.OUT_FLOW, 1, Optional.empty()))
                val inCondition: Pin<Boolean> = addPin(PinDefinition(PinSignature.Kind.IN_PARAM, 0, Optional.of(GenshinType.BOOLEAN)))
            }

            class NodeSwitch<T>(
                id: Long,
                type: GenshinType<T>,
                listType: GenshinType<List<T>>,
                selected: Int,
                countCases: Int
            ): GraphNode(identifierServer(3), identifierServer(id)) {
                val flowIn: Pin<Void> = addPin(PinDefinition(PinSignature.Kind.IN_FLOW, 0, Optional.empty()))
                val flowDefault: Pin<Void> = addPin(PinDefinition(PinSignature.Kind.OUT_FLOW, 0, Optional.empty()))
                val inControlling: Pin<T> = addPin(PinDefinition(PinSignature.Kind.IN_PARAM, 0, Optional.of(GenshinType.Selected(selected, type))))
                val inCases: Pin<List<T>> = addPin(PinDefinition(PinSignature.Kind.IN_PARAM, 1, Optional.of(GenshinType.Selected(selected, listType))))
                val flowCases: MutableList<Pin<Void>> = ArrayList<Pin<Void>>()

                init {
                    for (i in 0..<countCases) this.flowCases.add(
                        addPin(
                            PinDefinition(
                                PinSignature.Kind.OUT_FLOW,
                                1 + i,
                                Optional.empty()
                            )
                        )
                    )
                }

                fun setCases(value: List<T>) =
                    this.inCases.setValue(value)

                fun getFlowCase(i: Int): Pin<Void> =
                    this.flowCases[i]
            }

            class ForClosed internal constructor() : GraphNode(identifierServer(5), identifierServer(5)) {
                val flowIn: Pin<Void> = addPin(PinDefinition(PinSignature.Kind.IN_FLOW, 0, Optional.empty()))
                val flowBreak: Pin<Void> = addPin(PinDefinition(PinSignature.Kind.IN_FLOW, 1, Optional.empty()))
                val flowBody: Pin<Void> = addPin(PinDefinition(PinSignature.Kind.OUT_FLOW, 0, Optional.empty()))
                val flowOut: Pin<Void> = addPin(PinDefinition(PinSignature.Kind.OUT_FLOW, 1, Optional.empty()))
                val inBegin: Pin<Int> = addPin(PinDefinition(PinSignature.Kind.IN_PARAM, 0, Optional.of(GenshinType.INT)))
                val inEnd: Pin<Int> = addPin(PinDefinition(PinSignature.Kind.IN_PARAM, 1, Optional.of(GenshinType.INT)))
                val outValue: Pin<Int> = addPin(PinDefinition(PinSignature.Kind.OUT_PARAM, 0, Optional.of(GenshinType.INT)))
            }
        }

        interface Calc {
            interface IsEqual {
                companion object {
                    fun ofString() = node(14, 0, GenshinType.STRING)
                    fun ofGuid() = node(15, 1, GenshinType.GUID)
                    fun ofEntity() = node(16, 2, GenshinType.ENTITY)
                    fun ofVector() = node(17, 3, GenshinType.VECTOR)
                    fun ofFaction() = node(254, 4, GenshinType.FACTION)
                    fun ofInt() = node(370, 5, GenshinType.INT)
                    fun ofFloat() = node(371, 6, GenshinType.FLOAT)
                    fun ofConfig() = node(581, 7, GenshinType.CONFIG)
                    fun ofPrefab() = node(582, 8, GenshinType.PREFAB)
                    fun ofBoolean() = node(786, 9, GenshinType.BOOLEAN)

                    fun <T> node(id: Long, index: Int, type: GenshinType<T>): Expr2<Boolean, T, T> {
                        return Expr2(
                            identifierServer(14),
                            identifierServer(id),
                            GenshinType.BOOLEAN,
                            GenshinType.Selected(index, type),
                            GenshinType.Selected(index, type)
                        )
                    }
                }
            }

            interface Cast {
                companion object {
                    fun <T, R> node(idKernel: Long, selectedOut: Int, typeOut: GenshinType<R>, selectedIn: Int, typeIn: GenshinType<T>) = Expr1(
                        identifierServer(180),
                        identifierServer(idKernel),
                        GenshinType.Selected(selectedOut, typeOut),
                        GenshinType.Selected(selectedIn, typeIn)
                    )
                    fun intToBool() = node(180, 0, GenshinType.BOOLEAN, 0, GenshinType.INT)
                    fun intToFloat() = node(181, 1, GenshinType.FLOAT, 0, GenshinType.INT)
                    fun intToString() = node(182, 2, GenshinType.STRING, 0, GenshinType.INT)
                    fun entityToString() = node(183, 2, GenshinType.STRING, 1, GenshinType.ENTITY)
                    fun guidToString() = node(184, 2, GenshinType.STRING, 2, GenshinType.GUID)
                    fun boolToInt() = node(185, 3, GenshinType.INT, 3, GenshinType.BOOLEAN)
                    fun boolToString() = node(186, 2, GenshinType.STRING, 3, GenshinType.BOOLEAN)
                    fun floatToInt() = node(187, 3, GenshinType.INT, 4, GenshinType.FLOAT)
                    fun floatToString() = node(188, 2, GenshinType.STRING, 4, GenshinType.FLOAT)
                    fun vectorToString() = node(189, 2, GenshinType.STRING, 5, GenshinType.VECTOR)
                    fun factionToString() = node(255, 2, GenshinType.STRING, 6, GenshinType.FACTION)
                }
            }

            interface Math {
                companion object {
                    fun not(): Expr1<Boolean, Boolean> = Expr1(
                        identifierServer(229),
                        identifierServer(229),
                        GenshinType.BOOLEAN,
                        GenshinType.BOOLEAN
                    )
                    fun addInt() = closedBinarySelected(200, 200, 0, GenshinType.INT)
                    fun addFloat() = closedBinarySelected(200, 201, 1, GenshinType.FLOAT)
                    fun subtractInt() = closedBinarySelected(202, 202, 0, GenshinType.INT)
                    fun subtractFloat() = closedBinarySelected(202, 203, 1, GenshinType.FLOAT)
                    fun lessThanInt() = compareSelected(230, 230, 0, GenshinType.INT)
                    fun lessThanFloat() = compareSelected(230, 235, 1, GenshinType.FLOAT)
                    fun greaterThanInt() = compareSelected(232, 230, 0, GenshinType.INT)
                    fun greaterThanFloat() = compareSelected(232, 237, 1, GenshinType.FLOAT)
                    fun lessThanOrEqualInt() = compareSelected(231, 231, 0, GenshinType.INT)
                    fun lessThanOrEqualFloat() = compareSelected(231, 236, 1, GenshinType.FLOAT)
                    fun greaterThanOrEqualInt() = compareSelected(233, 233, 0, GenshinType.INT)
                    fun greaterThanOrEqualFloat() = compareSelected(233, 238, 1, GenshinType.FLOAT)

                    fun <T> closedBinarySelected(
                        idShell: Long,
                        idKernel: Long,
                        selected: Int,
                        type: GenshinType<T>
                    ): Expr2<T, T, T> = GenshinType.Selected(selected, type).let {
                        Expr2(identifierServer(idShell), identifierServer(idKernel), it, it, it)
                    }
                    fun <T> compareSelected(
                        idShell: Long,
                        idKernel: Long,
                        selected: Int,
                        type: GenshinType<T>
                    ): Expr2<Boolean, T, T> = GenshinType.Selected(selected, type).let {
                        Expr2(identifierServer(idShell), identifierServer(idKernel), GenshinType.BOOLEAN, it, it)
                    }
                }
            }
        }

        interface Query {
            interface Local {
                class Node<T>(idKernel: Identifier, type: GenshinType<T>) :
                    GraphNode(identifierServer(18), idKernel) {
                    val inInit: Pin<T> = addPin(PinDefinition(PinSignature.Kind.IN_PARAM, 0, Optional.of(type)))
                    val outLocal: Pin<ServerLocal> = addPin(PinDefinition(PinSignature.Kind.OUT_PARAM, 0, Optional.of(GenshinType.Server.LOCAL)))
                    val outValue: Pin<T> = addPin(PinDefinition(PinSignature.Kind.OUT_PARAM, 1, Optional.of(type)))

                    constructor(idKernel: Long, selected: Int, type: GenshinType<T>):
                            this(identifierServer(idKernel), GenshinType.Selected(selected, type))
                }

                companion object {
                    fun getBoolean() = Node(18, 0, GenshinType.BOOLEAN)
                    fun getInt() = Node(20, 1, GenshinType.INT)
                    fun getString() = Node(2656, 2, GenshinType.STRING)
                    fun getEntity() = Node(2657, 3, GenshinType.ENTITY)
                    fun getGuid() = Node(2658, 4, GenshinType.GUID)
                    fun getFloat() = Node(2659, 5, GenshinType.FLOAT)
                    fun getVector() = Node(2660, 6, GenshinType.VECTOR)
                    fun getIntList() = Node(2661, 7, GenshinType.INT_LIST)
                    fun getStringList() = Node(2662, 8, GenshinType.STRING_LIST)
                    fun getEntityList() = Node(2663, 9, GenshinType.ENTITY_LIST)
                    fun getGuidList() = Node(2664, 10, GenshinType.GUID_LIST)
                    fun getFloatList() = Node(2665, 11, GenshinType.FLOAT_LIST)
                    fun getVectorList() = Node(2666, 12, GenshinType.VECTOR_LIST)
                    fun getBooleanList() = Node(2667, 13, GenshinType.BOOLEAN_LIST)
                    fun getConfig() = Node(2668, 14, GenshinType.CONFIG)
                    fun getPrefab() = Node(2669, 15, GenshinType.PREFAB)
                    fun getConfigList() = Node(2670, 16, GenshinType.CONFIG_LIST)
                    fun getPrefabList() = Node(2671, 17, GenshinType.Server.PREFAB_LIST)
                    fun getFaction() = Node(2672, 18, GenshinType.FACTION)
                    fun getFactionList() = Node(2673, 19, GenshinType.Server.FACTION_LIST)
                }
            }
        }
    }

    interface Client

    open class Expr0<O>(idShell: Identifier, idKernel: Identifier, out: GenshinType<O>) : GraphNode(idShell, idKernel) {
        val out: Pin<O> = addPin<O>(PinDefinition<O>(PinSignature.Kind.OUT_PARAM, 0, Optional.of<GenshinType<O>>(out)))
    }
    open class Expr1<O, I0>(idShell: Identifier, idKernel: Identifier, out: GenshinType<O>, in0: GenshinType<I0>) :
        Expr0<O>(idShell, idKernel, out) {
        val in0: Pin<I0> = addPin<I0>(PinDefinition<I0>(PinSignature.Kind.IN_PARAM, 0, Optional.of<GenshinType<I0>>(in0)))
    }
    class Expr2<O, I0, I1>(
        idShell: Identifier,
        idKernel: Identifier,
        out: GenshinType<O>,
        in0: GenshinType<I0>,
        in1: GenshinType<I1>
    ): Expr1<O, I0>(idShell, idKernel, out, in0) {
        val in1: Pin<I1> = addPin<I1>(PinDefinition<I1>(PinSignature.Kind.IN_PARAM, 1, Optional.of<GenshinType<I1>>(in1)))
    }

    open class Statement0_0(idShell: Identifier, idKernel: Identifier) : GraphNode(idShell, idKernel) {
        val flowIn: Pin<Void> = addPin(PinDefinition(PinSignature.Kind.IN_FLOW, 0, Optional.empty()))
        val flowOut: Pin<Void> = addPin(PinDefinition(PinSignature.Kind.OUT_FLOW, 0, Optional.empty()))
    }
    open class Statement1_0<I0>(idShell: Identifier, idKernel: Identifier, in0: GenshinType<I0>): Statement0_0(idShell, idKernel) {
        val in0: Pin<I0> = addPin<I0>(PinDefinition<I0>(PinSignature.Kind.IN_PARAM, 0, Optional.of<GenshinType<I0>>(in0)))
    }
    class Statement2_0<I0, I1>(
        idShell: Identifier,
        idKernel: Identifier,
        in0: GenshinType<I0>,
        in1: GenshinType<I1>
    ): Statement1_0<I0>(idShell, idKernel, in0) {
        val in1: Pin<I1> = addPin<I1>(PinDefinition<I1>(PinSignature.Kind.IN_PARAM, 1, Optional.of<GenshinType<I1>>(in1)))
    }

    open class Trigger0(idShell: Identifier, idKernel: Identifier) : GraphNode(idShell, idKernel) {
        val flow: Pin<Void> = addPin(PinDefinition(PinSignature.Kind.OUT_FLOW, 0, Optional.empty()))
    }
    open class Trigger1<O0>(idShell: Identifier, idKernel: Identifier, out0: GenshinType<O0>): Trigger0(idShell, idKernel) {
        val out0: Pin<O0> = addPin(PinDefinition(PinSignature.Kind.OUT_PARAM, 0, Optional.of(out0)))
    }
    class Trigger2<O0, O1>(
        idShell: Identifier,
        idKernel: Identifier,
        data0: GenshinType<O0>,
        out1: GenshinType<O1>
    ) : Trigger1<O0>(idShell, idKernel, data0) {
        val out1: Pin<O1> = addPin(PinDefinition(PinSignature.Kind.OUT_PARAM, 1, Optional.of(out1)))
    }
}

fun identifierServer(id: Long): Identifier =
    Identifier.newBuilder().apply {
        source = Identifier.Source.SYSTEM_DEFINED
        category = Identifier.Category.SERVER_BASIC
        kind = Identifier.AssetKind.SYS_CALL_STUB
        runtimeId = id
    }.build()