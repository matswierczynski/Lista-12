package com.company;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Directed, weighted graph representation
 */

/**Constructor of an empty graph*/
public class Graph {
    private int nrOfVertices;
    private int nrOfEdges;
    private final LinkedList<Vertex> verticesList;

    Graph() {
        nrOfVertices = 0;
        nrOfEdges = 0;
        verticesList = new LinkedList<>();
    }

    /**
     * Add edge by names of vertices, if vertex doesn't exist create a new one
     */
    void addEdge(String v, String u, int weight) {
        Vertex ver1, ver2;
        if (weight < 1)
            throw new IllegalArgumentException();
        if (!contains(v)) {
            ver1 = add(v);
            nrOfVertices++;
        } else
            ver1 = get(v);
        if (!contains(u)) {
            ver2 = add(u);
            nrOfVertices++;
        } else
            ver2 = get(u);
        ver1.addEdge(ver2.getNr(), weight);
        nrOfEdges++;
    }

    /**
     * Add vertex by name, nr of vertex is created automatically
     */
    @SuppressWarnings("unchecked for duplicates")
    Vertex add(String name) {
        Vertex vertex = new Vertex(nrOfVertices, name);
        verticesList.add(vertex);
        nrOfVertices++;
        return vertex;
    }

    /**
     * Check is graph contains vertex of given number
     */
    @Contract(pure = true)
    private boolean contains(int v) {
        return validateVertex(v);
    }

    /**
     * Check for containnig vertex of given name
     */
    private boolean contains(String s) {
        return validateVertex(s);
    }

    /**
     * Return vertex of given number
     */
    @Nullable
    private Vertex get(int nr) {
        if (validateVertex(nr))
            return verticesList.get(nr);
        return null;
    }

    /**
     * Return vertex of given name
     */
    @Nullable
    private Vertex get(String s) {
        for (Vertex v : verticesList)
            if (v.getData().equals(s))
                return v;
        return null;
    }

    /**
     * Check if vertex of given number is correct graph number
     */
    @Contract(pure = true)
    private boolean validateVertex(int v) {
        return (v >= 0 && v < nrOfVertices);
    }

    /**
     * Check if vertex of given name is correct graph vertex
     */
    private boolean validateVertex(String s) {
        for (Vertex v : verticesList)
            if (v.getData().equals(s))
                return true;
        return false;
    }

    /**
     * Find shortest path between two cities - int version
     */
    private DijkstraAlgorithm shortestPath(int begin, int end) {
        if (!validateVertex(begin) || !validateVertex(end))
            throw new IllegalArgumentException();
        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(this);
        dijkstraAlgorithm.Dijkstra(verticesList.get(begin));
        return dijkstraAlgorithm;

    }

    /**
     * Find shortest path between two cities - String version
     */
    private DijkstraAlgorithm shortestPath(String begin, String end) {
        if (!validateVertex(begin) || !validateVertex(end))
            throw new IllegalArgumentException();
        int from = get(begin).getNr();
        int to = get(end).getNr();
        return shortestPath(from, to);
    }

    /**
     * Print shortest path - String version
     */
    void printShortestPath(String begin, String end) {
        DijkstraAlgorithm da = shortestPath(begin, end);
        int from = get(begin).getNr();
        int to = get(end).getNr();
        System.out.print(da.getMinPath(from, to));
    }

    /**
     * Find all cities reachable from given vertex number
     */
    private void allReachable(int nr) {
        DijkstraAlgorithm da = shortestPath(nr, nrOfVertices - 1 - nr);
        System.out.println("\n\n Wszystkie miasta osiągalne z " + verticesList.get(nr) + ":");
        for (Vertex x : verticesList) {
            if (x.getDijkstraWeight() != Integer.MAX_VALUE && x.getDijkstraWeight() != 0)
                System.out.println(x + " " + x.getDijkstraWeight() + " km ");
        }
    }

    /**
     * Print all reachable cities from given name
     */
    void allReachable(String begin) {
        int from = get(begin).getNr();
        allReachable(from);
    }


    @Override
    public String toString() {
        for (Vertex v : verticesList) {
            System.out.print(v + v.getAdjList());
        }
        return "Directed weighted graph";
    }

    /**
     * Private class for representing vertex of a graph
     * Contains Vertex nr ,name, nr of edges to other vertices
     * and Dijkstra's algorithm weight.
     * Adjacency list is given as a TreeMap - Key is
     * a nr of vertex to which we can travel and a Value
     * is a weight of a path
     */
    private class Vertex implements Comparable<Vertex> {
        private final int nr;
        private final String data;
        private int edges;
        private int DijkstraWeight;
        private final TreeMap<Integer, Integer> adj;

        /**
         * Create empty vertex
         */
        Vertex(int nr, String data) {
            this.nr = nr;
            this.data = data;
            edges = 0;
            adj = new TreeMap<>();
        }

        /**
         * Add edge between  this vertex and other vertex with given weight
         */
        void addEdge(int vertex, int weight) {
            if (vertex < 0 || weight < 1) throw new IllegalArgumentException();
            adj.put(vertex, weight);
            edges++;
        }


        @Override
        public int compareTo(Vertex o) {
            return Integer.compare(this.DijkstraWeight, o.getDijkstraWeight());
        }

        @Override
        public boolean equals(Object obj) {
            Vertex ver = (Vertex) obj;
            return getData().equals(ver.getData());
        }

        @Override
        public String toString() {
            String s = " Miasto: " + data;
            return s;
        }


        /**
         * Getters
         */
        int getDijkstraWeight() {
            return DijkstraWeight;
        }

        String getAdjList() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n Możemy się dostać bezpośrednio do: \n");
            adj.forEach((key, value) -> {
                stringBuilder.append(verticesList.get(key));
                stringBuilder.append(" ");
                stringBuilder.append(value);
                stringBuilder.append(" km\n");
            });
            stringBuilder.append("\n");
            return stringBuilder.toString();
        }

        int getNr() {
            return nr;
        }

        String getData() {
            return data;
        }

        public int getEdges() {
            return edges;
        }

        TreeMap<Integer, Integer> getAdj() {
            return adj;
        }

        /**
         * Setter
         */
        void setDijkstraWeight(int dijkstraWeight) {
            DijkstraWeight = dijkstraWeight;
        }
    }

    /**Private class used for finding a shortest path between two vertices
     * Contains graph, Priority Queue of all vertices to compute
     * shortest path and an array of predecessors of given vertices
     * on the shortest path. By travelling from the destination city to its
     * predecessors we finally reach the beginning city - if the road exists
     * in the graph
     * */
    private class DijkstraAlgorithm{
        private final Graph graph;
        private final PriorityQueue<Vertex> queue;
        private final int [] pred;

        /**Creates an empty Dijkstra's algorithm class instance*/
         DijkstraAlgorithm(Graph g){
            graph=g;
            queue=new PriorityQueue<>();
            pred=new int[graph.nrOfVertices];
        }

        /**Set the Dijkstra's weight od all vertices fo infinite,
         * @param s root
         * set the root weight to 0 - arrival city
         */
        private  void initializeSingleSource(int s){
            for (Vertex v : verticesList){
                v.setDijkstraWeight(Integer.MAX_VALUE);
            }
            verticesList.get(s).setDijkstraWeight(0);
        }

        /**Check whether it is possible to reach given city
         * on shortest path than given in its Dijkstra's weight
          * @param u root
         * @param v city on root adjacency's list
         * @param weight weight of a path between root and adjacency's list city
         */
        private void relax(int u, int v, int weight){
            if (verticesList.get(u).getDijkstraWeight()+weight>=0) {
                if (verticesList.get(v).getDijkstraWeight() >
                        verticesList.get(u).getDijkstraWeight() + weight) {
                    verticesList.get(v).setDijkstraWeight
                            (verticesList.get(u).getDijkstraWeight() + weight);
                    pred[v] = u;
                    queue.remove(verticesList.get(v));
                    queue.add(verticesList.get(v));
                }
            }
        }

        /** Dijkstra's algorithm. Add all vertices to the priority queue
         * search for the the smallest based on dijkstra's weight
         * poll it from the queue and RELAX
         * @param v
         */
         void Dijkstra(Vertex v){
            initializeSingleSource(v.getNr());
            queue.addAll(verticesList);
            while (!queue.isEmpty()){
                Vertex vertex=queue.poll();
                for (Map.Entry<Integer,Integer> entry : vertex.getAdj().entrySet()){
                    Integer other=entry.getKey();
                    Integer weight=entry.getValue();
                    relax(vertex.getNr(),other,weight);
                }
            }

        }

        /**Build the mininum path and return it as a String*/
         String getMinPath(int begin, int end){
            StringBuilder s=new StringBuilder();
            s=shortestPath(begin,end,s);
            s.append("Jesteś u celu");
            return s.toString();
        }

        /**Recursively add cities on the path traversal and the length
         * between them
         * @param begin start city
         * @param end arrival city
         * @param s builded path
         * @return path as a String
         */
        private StringBuilder shortestPath(int begin, int end, StringBuilder s) {
            if (end!=begin)
                shortestPath(begin,pred[end],s);
            s.append(verticesList.get(end));
            s.append(" (");
            s.append(verticesList.get(end).getDijkstraWeight());
            s.append(") km\n ");
            s.append("          ||\n           \\/ \n");
            return s;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder=new StringBuilder();
            for (Vertex ver : verticesList){
                stringBuilder.append(ver);
                stringBuilder.append(" Waga: ");
                stringBuilder.append(ver.DijkstraWeight);
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }
    }
    
}
