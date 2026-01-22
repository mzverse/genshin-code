package mz.genshincode.graph;

import mz.genshincode.data.asset.AssetsGenerator;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NodeGraph
{
    Set<GraphNode> nodes;

    public NodeGraph()
    {
        this.nodes = new HashSet<>();
    }

    public void generateAssets(AssetsGenerator generator)
    {
        List<GraphNode> list = new ArrayList<>(this.nodes);
        Map<GraphNode, Integer> ids = IntStream.range(0, list.size()).boxed().collect(Collectors.toMap(list::get, Function.identity()));
        // TODO
    }
}
