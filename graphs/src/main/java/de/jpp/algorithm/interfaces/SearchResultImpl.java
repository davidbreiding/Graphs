package de.jpp.algorithm.interfaces;

import de.jpp.model.interfaces.Edge;

import java.util.*;
import java.util.function.BiConsumer;

import static de.jpp.algorithm.interfaces.NodeStatus.CLOSED;

public class SearchResultImpl<N, A> implements ObservableSearchResult<N, A> {

    List<BiConsumer<N, SearchResult<N, A>>> onOpen;
    List<BiConsumer<N, SearchResult<N, A>>> onClose;

    Map<N, NodeStatus> statusMap;
    Map<N, NodeInformation<N, A>> infoMap;


    public SearchResultImpl() {
        onOpen = new ArrayList<>();
        onClose = new ArrayList<>();
        statusMap = new HashMap<>();
        infoMap = new HashMap<>();
    }

    @Override
    public void addNodeOpenedListener(BiConsumer<N, SearchResult<N, A>> onOpen) {
        this.onOpen.add(onOpen);
    }

    @Override
    public void removeNodeOpenedListener(BiConsumer<N, SearchResult<N, A>> onOpen) {
        this.onOpen.remove(onOpen);
    }

    @Override
    public void addNodeClosedListener(BiConsumer<N, SearchResult<N, A>> onClose) {
        this.onClose.add(onClose);
    }

    @Override
    public void removeNodeClosedListener(BiConsumer<N, SearchResult<N, A>> onClose) {
        this.onClose.remove(onClose);
    }

    @Override
    public NodeStatus getNodeStatus(N node) {
        return statusMap.get(node);
    }

    @Override
    public Optional<Edge<N, A>> getPredecessor(N node) {
        try {
            NodeInformation<N, A> nodeInformation = infoMap.get(node);

            Edge<N, A> edge = nodeInformation.getPredecessor();

            return Optional.of(edge);
        } catch (NullPointerException n) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<N> getAllKnownNodes() {
        return statusMap.keySet();
    }

    @Override
    public Collection<N> getAllOpenNodes() {
        Collection<N> nCollection = new ArrayList<>();
        for (N node : statusMap.keySet()) {
            if (statusMap.get(node).equals(NodeStatus.OPEN)) {
                nCollection.add(node);
            }
        }
        return nCollection;
    }

    @Override
    public void setClosed(N node) {
        if (statusMap.keySet().contains(node)) {
            statusMap.remove(node);
        }

        for (BiConsumer<N, SearchResult<N, A>> consumer : onClose) {
            consumer.accept(node, this);
        }
        statusMap.put(node, CLOSED);
    }

    @Override
    public void setOpen(N node) {

        for (BiConsumer<N, SearchResult<N, A>> consumer : onOpen) {
            consumer.accept(node, this);
        }
        statusMap.put(node, NodeStatus.OPEN);
    }

    @Override
    public void clear() {
        statusMap.clear();
        infoMap.clear();
    }

    @Override
    public Optional<List<Edge<N, A>>> getPathTo(N dest) {

        List<Edge<N, A>> list = new ArrayList<>();

        N node = dest;

        try {
            while (getPredecessor(node).isPresent()) {
                list.add(getPredecessor(node).get());
                node = getPredecessor(node).get().getStart();
            }

            if (list.size() > 0) {
                Collections.reverse(list);
                return Optional.of(list);
            } else {
                return Optional.empty();
            }
        } catch (NullPointerException n) {
            Collections.reverse(list);
            return Optional.of(list);
        }
    }


    public void close(N node, NodeInformation<N, A> nodeInformation) {
        infoMap.put(node, nodeInformation);
        for (BiConsumer<N, SearchResult<N, A>> consumer : onClose) {
            consumer.accept(node, this);
        }
    }

    public void open(N node, NodeInformation<N, A> nodeInformation) {
        infoMap.put(node, nodeInformation);
        for (BiConsumer<N, SearchResult<N, A>> consumer : onOpen) {
            consumer.accept(node, this);
        }
    }

    public NodeInformation<N, A> getInformation(N node) {
        return infoMap.get(node);
    }

    public Map<N, NodeStatus> getStatusMap() {
        return statusMap;
    }
}



