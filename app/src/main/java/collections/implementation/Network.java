package collections.implementation;

import collections.exceptions.EmptyCollectionException;
import collections.exceptions.UnknownPathException;
import collections.interfaces.NetWorkADT;
import collections.interfaces.UnorderedListADT;

import java.util.Iterator;

/**
 * Network class that implements the NetWorkADT interface.
 * @param <T> The type of the elements in the network.
 */
public class Network<T> extends MatrixGraph<T> implements NetWorkADT<T> {
    /**
     * The default capacity of the network.
     */
    protected final int DEFAULT_CAPACITY = 10;

    /**
     * The adjacency matrix of the network.
     */
    private double[][] adjMatrix;

    /**
     * Creates an empty network.
     */
    public Network() {
        super();
        this.adjMatrix = new double[this.DEFAULT_CAPACITY][this.DEFAULT_CAPACITY];
    }

    /**
     * Adds the specified vertex to this graph, expanding the capacity of the graph if necessary.
     * @param vertex the vertex to be added to this graph
     */
    @Override
    public void addVertex(T vertex) {
        if (this.numVertices + 1 >= this.adjMatrix.length) {
            this.expandMatrix();
        }

        super.addVertex(vertex);
    }

    /**
     * Adds an edge between the two specified vertices of this graph.
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     * @param weight the weight of the edge
     */
    @Override
    public void addEdge(T vertex1, T vertex2, double weight) {
        if (weight < 0.0D) {
            throw new IllegalArgumentException("The weight cannot be under the default.");
        } else {
            super.addEdge(vertex1, vertex2);
            this.setEdgeWeight(vertex1, vertex2, weight);
        }
    }

    /**
     * Return an array containing the vertices with the specified weight.
     * @param weight the weight of the edge
     * @param visited the array used to mark the visited vertices
     * @return an array containing the vertices with the specified weight
     */
    protected int[] getEdgeWithWeightOf(double weight, boolean[] visited) {
        int[] edge = new int[2];
        for (int i = 0; i < numVertices; i++)
            for (int j = 0; j < numVertices; j++)
                if ((adjMatrix[i][j] == weight) && (visited[i] ^ visited[j])) {
                    edge[0] = i;
                    edge[1] = j;
                    return edge;
                }

        // Will only get to here if a valid edge is not found
        edge[0] = -1;
        edge[1] = -1;
        return edge;
    }

    /**
     * Sets the weight of the edge between the two specified vertices.
     * @param firstVertex the first vertex
     * @param secondVertex the second vertex
     * @param weight the weight of the edge
     */
    public void setEdgeWeight(T firstVertex, T secondVertex, double weight) {
        if (weight < 0.0D) {
            throw new IllegalArgumentException("The weight cannot be under the default.");
        }

        int first = this.getIndex(firstVertex);
        int second = this.getIndex(secondVertex);

        if (secondVertex.equals("exterior") || firstVertex.equals("exterior") || secondVertex.equals("entrada") || firstVertex.equals("entrada")) {
            this.adjMatrix[first][second] = 0;
            this.adjMatrix[second][first] = 0;
        } else {
            this.adjMatrix[first][second] = weight;
        }

    }

    /**
     * Returns the weight of the edge between the two specified vertices.
     * @param firstVertex the first vertex
     * @param secondVertex the second vertex
     * @return the weight of the edge between the two specified vertices
     */
    public double getEdgeWeight(T firstVertex, T secondVertex) {
        int first = this.getIndex(firstVertex);
        int second = this.getIndex(secondVertex);

        return this.adjMatrix[first][second];
    }

    /**
     * Expands the capacity of the matrix by creating a new matrix and copying the contents of the old matrix to it.
     */
    private void expandMatrix() {
        double[][] tempMatrix = new double[this.numVertices * 2][this.numVertices * 2];

        for (int i = 0; i < this.numVertices; ++i) {
            for (int j = 0; j < this.numVertices; ++j) {
                tempMatrix[i][j] = this.adjMatrix[i][j];
            }
        }

        this.adjMatrix = tempMatrix;
    }

    /**
     * Returns an iterator that returns the shortest path between the two specified vertices.
     * @param startVertex the starting vertex
     * @param targetVertex the ending vertex
     * @return an iterator that returns the shortest path between the two specified vertices
     * @throws UnknownPathException the exception thrown when the path is unknown
     * @throws EmptyCollectionException the exception thrown when the collection is empty
     */
    public Iterator<T> iteratorShortestWeight(T startVertex, T targetVertex) throws UnknownPathException, EmptyCollectionException {
        return shortestPathWeight(startVertex, targetVertex).iterator();
    }

    /**
     * Returns the shortest path between the two specified vertices.
     * @param vertex1 the starting vertex
     * @param vertex2 the ending vertex
     * @return the shortest path between the two specified vertices
     * @throws UnknownPathException the exception thrown when the path is unknown
     * @throws EmptyCollectionException the exception thrown when the collection is empty
     */
    @Override
    public ArrayUnorderedList<T> shortestPathWeight(T vertex1, T vertex2) throws EmptyCollectionException, UnknownPathException {
        PriorityQueue<Pair<T>> priorityQueue = new PriorityQueue<>();
        UnorderedListADT<T> verticesFromPossiblePath = new ArrayUnorderedList<>();
        ArrayUnorderedList<T> result = new ArrayUnorderedList<>();
        Pair<T> startPair = new Pair<>(null, vertex1, 0.0);

        priorityQueue.addElement(startPair, (int) startPair.cost);

        while (!priorityQueue.isEmpty()) {
            Pair<T> pair = priorityQueue.removeNext();
            T vertex = pair.vertex;
            double minCost = pair.cost;

            if (vertex.equals(vertex2)) {
                Pair<T> finalPair = pair;

                while (finalPair != null) {
                    result.addToFront(finalPair.vertex);
                    finalPair = finalPair.previous;
                }

                return result;
            }

            verticesFromPossiblePath.addToRear(vertex);

            for (int i = 0; i < numVertices; i++) {
                if (super.adjMatrix[getIndex(vertex)][i] && !verticesFromPossiblePath.contains(vertices[i])) {
                    double minCostToVertex = minCost + adjMatrix[getIndex(vertex)][i];
                    Pair<T> tmpPair = new Pair<>(pair, vertices[i], minCostToVertex);
                    priorityQueue.addElement(tmpPair, (int) tmpPair.cost);
                }
            }
        }

        throw new UnknownPathException("Path doesn't exist");
    }

    /**
     * Returns the minimum spanning tree of the network.
     * @return the minimum spanning tree of the network
     */
    @SuppressWarnings("unchecked")
    public Network<T> mstNetwork() {
        int x, y;
        int index;
        double weight;
        int[] edge = new int[2];
        LinkedHeap<Double> minHeap = new LinkedHeap<>();
        Network<T> resultGraph = new Network<>();
        if (isEmpty() || !isConnected())
            return resultGraph;
        resultGraph.adjMatrix = new double[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++)
            for (int j = 0; j < numVertices; j++)
                resultGraph.adjMatrix[i][j] = Double.POSITIVE_INFINITY;
        resultGraph.vertices = (T[]) (new Object[numVertices]);
        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++)
            visited[i] = false;

        edge[0] = 0;
        resultGraph.vertices[0] = this.vertices[0];
        resultGraph.numVertices++;
        visited[0] = true;

        // Add all edges, which are adjacent to the starting vertex, to the heap
        for (int i = 0; i < numVertices; i++)
            minHeap.addElement(adjMatrix[0][i]);
        while ((resultGraph.size() < this.size()) && !minHeap.isEmpty()) {
            // Get the edge with the smallest weight that has exactly one vertex already in the resultGraph
            do {
                try {
                    weight = minHeap.removeMin();
                    edge = getEdgeWithWeightOf(weight, visited);
                } catch (EmptyCollectionException e) {
                    System.out.println(e.getMessage());
                }
            } while (!indexIsValid(edge[0]) || !indexIsValid(edge[1]));
            x = edge[0];
            y = edge[1];
            if (!visited[x])
                index = x;
            else
                index = y;
            // Add the new edge and vertex to the resultGraph
            resultGraph.vertices[index] = this.vertices[index];
            visited[index] = true;
            resultGraph.numVertices++;
            resultGraph.adjMatrix[x][y] = this.adjMatrix[x][y];
            resultGraph.adjMatrix[y][x] = this.adjMatrix[y][x];

            // Add all edges, that are adjacent to the newly added vertex, to the heap
            for (int i = 0; i < numVertices; i++) {
                if (!visited[i] && (this.adjMatrix[i][index] < Double.POSITIVE_INFINITY)) {
                    edge[0] = index;
                    edge[1] = i;
                    minHeap.addElement(adjMatrix[index][i]);
                }
            }
        }
        return resultGraph;
    }

    /**
     * Returns the string representation of the network.
     * @return the string representation of the network
     */
    public String toString() {
        if (numVertices == 0)
            return "Graph is empty";

        String result = super.toString();

        //Print the weights of the edges
        result += "\n\nWeights of Edges";
        result += "\n----------------\n";
        result += "index\tweight\n\n";

        for (int i = 0; i < numVertices; i++) {
            for (int j = numVertices - 1; j > i; j--) {
                if (super.adjMatrix[i][j]) {
                    result += i + " to " + j + "\t";
                    result += adjMatrix[i][j] + "\n";
                }
            }
        }

        result += "\n";
        return result;
    }
}

