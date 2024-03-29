package api.interfaces;

import java.util.Iterator;

import api.exceptions.NotPlaceInstanceException;
import collections.implementation.ArrayUnorderedList;
import collections.interfaces.GraphADT;

/**
 * Contract of a graph with paths of a game between portals and connectors.
 *
 * @param <T> type of locals in graph.
 */
public interface IPathGameGraphADT<T> extends GraphADT<T> {

    /**
     * Returns the local with the given index.
     * @param index index of local.
     * @return local with the given index.
     */
    ILocal get(int index);

    /**
     * Gets the number of connectors in graph.
     * @return the number of connectors in graph.
     */
    public int getNumberOfConnectores();

    /**
     * Gets the number of portals in graph.
     * @return the number of portals in graph.
     */
    public int getNumberOfPortals();


    /**
     * Gets the connectores on graph.
     * @return iterator of connectores.
     */
    Iterator<IConnector> getConnectores();

    /**
     * Gets the Portals on graph.
     * @return iterator of portals.
     */
    Iterator<IPortal> getPortals();

    /**
     * Gets the paths existences on the graph
     * @return iterator with those paths
     */
    Iterator<IRoute<ILocal>> getRoutes();

    /**
     * Shortest path between two points.
     * @param source starting point, starting point
     * @param destiny Point of arrival, place where you want to go
     * @return iterator with the path.
     * @throws NotPlaceInstanceException if start point is not {@link ILocal local} instance.
     */
    Iterator<ILocal> shortestPathBetweenTwoPoints(T source, T destiny) throws NotPlaceInstanceException;

    /**
     * Shortest path considering crossing only through portals.
     * @param source starting point, starting point
     * @param destiny Point of arrival, place where you want to go
     * @return iterator with the path.
     * @throws NotPlaceInstanceException if start point is not {@link ILocal local} instance.
     */
    Iterator<ILocal> shortestPathWithOnlyPortals(T source, T destiny) throws NotPlaceInstanceException;

    /**
     * Shortest path between two locals with crossing only by connectors.
     * @param source starting point, starting point
     * @param destiny Point of arrival, place where you want to go
     * @return iterator with the path.
     * @throws NotPlaceInstanceException if start point is not {@link ILocal local} instance.
     */
    Iterator<ILocal> shortestPathWithOnlyConnectors(T source, T destiny)throws NotPlaceInstanceException;

    /**
     * Shortest path between two locals crossing through at least one connector.
     * @param source starting point, starting point
     * @param destiny Point of arrival, place where you want to go
     * @return iterator with the path.
     * @throws NotPlaceInstanceException if start point is not {@link ILocal local} instance.
     */
    Iterator<ILocal> shortestPathAtleastOneConnector(T source, T destiny) throws NotPlaceInstanceException;

    /**
     * Gets the neighbours of a local.
     * @param vertex local to get neighbours.
     * @return iterator with the neighbours.
     * @throws NotPlaceInstanceException if vertex is not {@link ILocal local} instance.
     */
    ArrayUnorderedList<ILocal> getNeighbours(T vertex) throws NotPlaceInstanceException;

    /**
     * Exports the shortest path between two points to a json file.
     * @param source starting point
     * @param destiny destiny point
     * @param fileName name of the file
     * @throws NotPlaceInstanceException if start point is not {@link ILocal local} instance.
     */
    void exportShortestPathBetweenTwoPoints(T source, T destiny, String fileName) throws NotPlaceInstanceException;

    /**
     * Exports the shortest path between two points into a json file going only through portals.
     * @param source starting point
     * @param destiny destiny point
     * @param fileName name of the file
     * @throws NotPlaceInstanceException if source or destiny is not a {@link ILocal local} instance.
     */
    void exportShortestPathWithOnlyPortals(T source, T destiny, String fileName) throws NotPlaceInstanceException;

    /**
     * Exports the shortest path between two points into a json file going only through connectors.
     * @param source starting point
     * @param destiny destiny point
     * @param fileName name of the file
     * @throws NotPlaceInstanceException if source or destiny is not a {@link ILocal local} instance.
     */
    void exportShortestPathWithOnlyConnectors(T source, T destiny, String fileName) throws NotPlaceInstanceException;

    /**
     * Exports the shortest path between two points into a json file going through at least one connector.
     * @param source starting point
     * @param destiny destiny point
     * @param fileName name of the file
     * @throws NotPlaceInstanceException if source or destiny is not a {@link ILocal local} instance.
     */
    void exportShortestPathAtleastOneConnector(T source, T destiny, String fileName) throws NotPlaceInstanceException;
}