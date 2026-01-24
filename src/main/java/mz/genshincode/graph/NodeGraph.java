package mz.genshincode.graph;

import mz.genshincode.Util;
import mz.genshincode.data.asset.*;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NodeGraph
{
    String name = "todo";
    Set<GraphNode> nodes;

    public NodeGraph()
    {
        this.nodes = new HashSet<>();
    }

    public void addNode(GraphNode value)
    {
        this.nodes.add(value);
    }

    public void generateAssets(AssetsGenerator generator)
    {
        long guid = generator.allocateGuid();

        List<GraphNode> list = new ArrayList<>(Collections.singleton(null));
        list.addAll(this.nodes);
        Map<GraphNode, Integer> ids = IntStream.range(1, list.size()).boxed().collect(Collectors.toMap(list::get, Function.identity()));

        Asset.Builder builder = Asset.newBuilder();
        builder.setId(Identifier.newBuilder()
            .setCategory(Identifier.Category.SERVER_NODE_GRAPH)
            .setGuid(guid)
        );
        builder.setName(this.name);
        builder.setType(Asset.Type.ENTITY_NODE_GRAPH);
        builder.setGraphData(NodeGraphContainer.newBuilder()
            .setInner(NodeGraphContainer.InnerWrapper.newBuilder()
                .setGraph(NodeGraphData.newBuilder()
                    .setId(Identifier.newBuilder()
                        .setSource(Identifier.Source.USER_DEFINED)
                        .setCategory(Identifier.Category.SERVER_BASIC)
                        .setKind(Identifier.AssetKind.CUSTOM_GRAPH)
                        .setRuntimeId(guid)
                    )
                    .setDisplayName(this.name)
                    .addAllNode(ids.keySet().stream().map(it -> it.encode(generator.getSide(), ids)).collect(Collectors.toList()))
                )
            )
        );
        generator.addAsset(builder.build());
    }

    public void autoLayout()
    { // TODO
        Map<GraphNode, Point2D.Double> positions = new HashMap<>();
        Random random = ThreadLocalRandom.current();
        for(GraphNode node : this.nodes)
        {
            positions.put(node, new Point2D.Double(random.nextDouble(), random.nextDouble()));
        }
        double length = 100;
        double strengthX = 0.02, strength1 = 1000, strength2 = 0.01;
        int time = 10000;
        double timeStep = 1;
        while(time --> 0)
        {
            Map<GraphNode, Point2D.Double> forces = new HashMap<>();
            positions.forEach((n, p) ->
            {
                Point2D.Double force = new Point2D.Double();
                for(GraphNode.Pin<?> pin : n.pins)
                {
                    if(pin.getConnections().isEmpty())
                        continue;
                    switch(pin.definition.signShell.getKind())
                    {
                        case OUT_FLOW:
                        case OUT_PARAM:
                            force.x -= strengthX;
                            break;
                        case IN_FLOW:
                        case IN_PARAM:
                            force.x += strengthX;
                            break;
                        default: // TODO
                            break;
                    }
                }
                for(Point2D.Double p1 : positions.values())
                {
                    if(p1 == p)
                        continue;
                    double dx = p.x - p1.x, dy = p.y - p1.y;
                    double disSq = dx * dx + dy * dy, dis = Math.sqrt(disSq);
                    double norm = strength1 / disSq;
                    force.x += norm * dx / dis;
                    force.y += norm * dy / dis;
                }
                for(Point2D.Double p1 : Util.iterable(n.pins.stream()
                    .map(GraphNode.Pin::getConnections).flatMap(Collection::stream)
                    .map(GraphNode.Pin::getNode)
                    .map(positions::get)))
                {
                    double dx = p.x - p1.x, dy = p.y - p1.y;
                    double disSq = dx * dx + dy * dy, dis = Math.sqrt(disSq);
                    double norm = strength2 / (length - dis);
                    force.x += norm * dx / dis;
                    force.y += norm * dy / dis;
                }
                forces.put(n, force);
            });
            forces.forEach((n, f) ->
            {
                double maxForce = 100.;
                if(Math.abs(f.x) > maxForce)
                    f.x = maxForce * Math.signum(f.x);
                if(Math.abs(f.y) > maxForce)
                    f.y = maxForce * Math.signum(f.y);
                Point2D.Double p = positions.get(n);
                p.x += f.x * timeStep;
                p.y += f.y * timeStep;
            });
        }

        positions.forEach((n, p) -> n.setPosition(new Point2D.Float((float) p.x, (float) p.y)));
    }
}
