package com.company;
import java.util.*;

/**
 * Directed, weighted grapg representation
 */
public class Graph {
    private int nrOfVertices;
    private int nrOfEdges;
    private final LinkedList<Vertex> verticesList;

     Graph(){
        nrOfVertices=0;
        nrOfEdges=0;
        verticesList=new LinkedList<>();
    }


     void addEdge(String v, String u, int weight){
        Vertex ver1,ver2;
        if (weight<1)
            throw new IllegalArgumentException();
        if(!contains(v)) {
            ver1 = add(v);
            nrOfVertices++;
        }
        else
            ver1=get(v);
        if (!contains(u)) {
            ver2 = add(u);
            nrOfVertices++;
        }
        else
            ver2=get(u);
        ver1.addEdge(ver2.getNr(),weight);
        nrOfEdges++;
    }

     Vertex add(String name){
        Vertex vertex=new Vertex(nrOfVertices,name);
        verticesList.add(vertex);
        nrOfVertices++;
        return vertex;
    }

    private boolean contains(int v){
        return validateVertex(v);
    }

    private boolean contains(String s) {
        return validateVertex(s);
    }

    private Vertex get(int nr){
        if (validateVertex(nr))
            return verticesList.get(nr);
        return null;
    }

    private Vertex get(String s){
        for (Vertex v : verticesList)
            if (v.getData().equals(s))
                return v;
        return null;
    }

    private boolean validateVertex(int v){
        return (v<0 || v>=nrOfVertices);
    }

    private boolean validateVertex(String s){
        for (Vertex v : verticesList)
            if (v.getData().equals(s))
                return true;
        return false;
    }


    private DijkstraAlgorithm shortestPath(int begin, int end){
        if (!validateVertex(begin) || !validateVertex(end))
            throw new IllegalArgumentException();
        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(this);
        dijkstraAlgorithm.Dijkstra(verticesList.get(begin));
        return dijkstraAlgorithm;

    }


    void printShortestPath(String begin, String end){
        DijkstraAlgorithm da = shortestPath(begin,end);
        int from=get(begin).getNr();
        int to=get(end).getNr();
        System.out.print(da.getMinPath(from, to));
    }


    private DijkstraAlgorithm shortestPath (String begin, String end){
        if (!validateVertex(begin) || !validateVertex(end))
            throw new IllegalArgumentException();
        int from=get(begin).getNr();
        int to=get(end).getNr();
        return shortestPath(from,to);
    }

    private void allReachable(int nr){
        DijkstraAlgorithm da = shortestPath(nr,nrOfVertices-1-nr);
        System.out.println("\n\n Wszystkie miasta osiągalne z "+verticesList.get(nr)+":");
        for (Vertex x : verticesList){
           if (x.getDijkstraWeight()!=Integer.MAX_VALUE && x.getDijkstraWeight()!=0)
               System.out.println(x+" "+x.getDijkstraWeight()+" km ");
        }
    }

     void allReachable(String begin){
        int from = get(begin).getNr();
        allReachable(from);
    }



    @Override
    public String toString() {
        for (Vertex v : verticesList) {
            System.out.print(v+v.getAdjList());
        }
        return "\n";
    }

    private class Vertex implements Comparable<Vertex>{
        private final int nr;
        private final String data;
        private int edges;
        private int DijkstraWeight;
        private final TreeMap<Integer, Integer> adj;

         Vertex(int nr,String data){
            this.nr=nr;
            this.data=data;
            edges=0;
            adj=new TreeMap<>();
        }

        void addEdge(int vertex, int weight){
            if (vertex<0 || weight<1) throw new IllegalArgumentException();
            adj.put(vertex,weight);
            edges++;
        }



         int getDijkstraWeight() {
            return DijkstraWeight;
        }

         void setDijkstraWeight(int dijkstraWeight) {
            DijkstraWeight = dijkstraWeight;
        }

        @Override
        public int compareTo(Vertex o) {
            return Integer.compare(this.DijkstraWeight,o.getDijkstraWeight());
        }

        @Override
        public boolean equals(Object obj) {
            Vertex ver = (Vertex)obj;
            return getData().equals(ver.getData());
        }

        @Override
        public String toString(){
            String s=" Miasto: "+data;
            return s;
        }



         String getAdjList() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n Możemy się dostać bezpośrednio do: \n");
            adj.forEach((key,value)->{
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
    }

    private class DijkstraAlgorithm{
        private final Graph graph;
        private final PriorityQueue<Vertex> queue;
        private final int [] pred;

         DijkstraAlgorithm(Graph g){
            graph=g;
            queue=new PriorityQueue<>();
            pred=new int[graph.nrOfVertices];
        }

        private  void initializeSingleSource(int s){
            for (Vertex v : verticesList){
                v.setDijkstraWeight(Integer.MAX_VALUE);
            }
            verticesList.get(s).setDijkstraWeight(0);
        }

        private void relax(int u, int v, int weight){
            if (verticesList.get(v).getDijkstraWeight()>
                    verticesList.get(u).getDijkstraWeight()+weight){
                verticesList.get(v).setDijkstraWeight
                        (verticesList.get(u).getDijkstraWeight()+weight);
                pred[v]=u;
                queue.remove(verticesList.get(v));
                queue.add(verticesList.get(v));
            }
        }

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

         String getMinPath(int begin, int end){
            StringBuilder s=new StringBuilder();
            s=shortestPath(begin,end,s);
            s.append("Jesteś u celu");
            return s.toString();
        }

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
