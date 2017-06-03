package com.company;
import java.util.*;

/**
 * Created by Mati on 2017-06-01.
 */
public class Graph {
    private int nrOfVertices;
    private int nrOfEdges;
    private LinkedList<Vertex> verticesList;

    public Graph(){
        nrOfVertices=0;
        nrOfEdges=0;
        verticesList=new LinkedList<>();
    }

    public void addEdge(int v, int u, int weight){
        if (u<0 || u>nrOfVertices || v<0 || v>nrOfVertices || weight<1)
            throw new IllegalArgumentException();
        if(!contains(v))
            add("Not defined");
        Vertex ver = get(v);
        ver.addEdge(u,weight);
        nrOfEdges++;
    }

    public void addEdge(String v, String u, int weight){
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

    public Vertex add(String name){
        Vertex vertex=new Vertex(nrOfVertices,name);
        verticesList.add(vertex);
        nrOfVertices++;
        return vertex;
    }

    public boolean contains(int v){
        if (validateVertex(v))
           return true;
        return false;
    }

    public boolean contains(String s){
        if (validateVertex(s))
            return true;
        return false;
    }

    public Vertex get(int nr){
        if (validateVertex(nr))
            return verticesList.get(nr);
        return null;
    }

    public Vertex get(String s){
        for (Vertex v : verticesList)
            if (v.getData().equals(s))
                return v;
        return null;
    }

    private boolean validateVertex(int v){
        if (v<0 || v>=nrOfVertices)
            return false;
        return true;
    }

    private boolean validateVertex(String s){
        for (Vertex v : verticesList)
            if (v.getData().equals(s))
                return true;
        return false;
    }

    public Vertex remove(int nr){
        for (Vertex ver : verticesList) {
            if (ver.getNr() == nr) {
                verticesList.remove(ver);
                nrOfVertices--;
                nrOfEdges-=ver.getEdges();
                return ver;
            }
        }
         return null;
    }

    public void removeEdge(int v,int u){
        Vertex ver = get(v);
        if (ver==null)
            return;
        ver.removeEdge(u);
        nrOfEdges--;
    }

    public DijkstraAlgorithm shortestPath(int begin, int end){
        if (!validateVertex(begin) || !validateVertex(end))
            throw new IllegalArgumentException();
        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(this);
        dijkstraAlgorithm.Dijkstra(verticesList.get(begin));
        return dijkstraAlgorithm;

    }

    public void printShortestPath(int begin, int end){
        DijkstraAlgorithm da = shortestPath(begin,end);
        System.out.print(da.getMinPath(begin, end));
    }

    public void printShortestPath(String begin, String end){
        DijkstraAlgorithm da = shortestPath(begin,end);
        int from=get(begin).getNr();
        int to=get(end).getNr();
        System.out.print(da.getMinPath(from, to));
    }


    public DijkstraAlgorithm shortestPath (String begin, String end){
        if (!validateVertex(begin) || !validateVertex(end))
            throw new IllegalArgumentException();
        int from=get(begin).getNr();
        int to=get(end).getNr();
        return shortestPath(from,to);
    }

    public void allReachable(int nr){
        DijkstraAlgorithm da = shortestPath(nr,nrOfVertices-1-nr);
        System.out.println("\n\n Wszystkie miasta osiągalne z "+verticesList.get(nr)+":");
        for (Vertex x : verticesList){
           if (x.getDijkstraWeight()!=Integer.MAX_VALUE && x.getDijkstraWeight()!=0)
               System.out.println(x+" "+x.getDijkstraWeight()+" km ");
        }
    }

    public void allReachable(String begin){
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
        private int nr;
        private String data;
        private int edges;
        private int DijkstraWeight;
        private TreeMap<Integer, Integer> adj;

        public Vertex(int nr,String data){
            this.nr=nr;
            this.data=data;
            edges=0;
            adj=new TreeMap<>();
        }

        public void addEdge(int vertex, int weight){
            if (vertex<0 || weight<1) throw new IllegalArgumentException();
            adj.put(vertex,weight);
            edges++;
        }

        public void removeEdge(int u){
            adj.remove(u);
        }

        public boolean isEdge(int vertex){
            return adj.get(vertex)!=null;
        }

        public int getWeight(int vertex){
            return adj.get(vertex);
        }

        public String getName(){
            return data;
        }

        public int getDijkstraWeight() {
            return DijkstraWeight;
        }

        public void setDijkstraWeight(int dijkstraWeight) {
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
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(" Miasto: ");
            stringBuilder.append(data);
            return stringBuilder.toString();
        }

        public String getVertexNr(){
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("\nNr wierzchołka: ");
            stringBuilder.append(nr);
            return stringBuilder.toString();
        }

        public String getAdjList() {
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

        public int getNr() {
            return nr;
        }

        public String getData() {
            return data;
        }

        public int getEdges() {
            return edges;
        }

        public TreeMap<Integer, Integer> getAdj() {
            return adj;
        }
    }

    private class DijkstraAlgorithm{
        private Graph graph;
        private PriorityQueue<Vertex> queue;
        private int [] pred;
        private ArrayList<Integer> visited;

        public DijkstraAlgorithm(Graph g){
            graph=g;
            queue=new PriorityQueue<>();
            pred=new int[graph.nrOfVertices];
            visited=new ArrayList<>();
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

        public void Dijkstra(Vertex v){
            initializeSingleSource(v.getNr());
            for (Vertex vex : verticesList)
            queue.add(vex);
            while (!queue.isEmpty()){
                Vertex vertex=queue.poll();
                for (Map.Entry<Integer,Integer> entry : vertex.getAdj().entrySet()){
                    Integer other=entry.getKey();
                    Integer weight=entry.getValue();
                    relax(vertex.getNr(),other,weight);
                }
            }

        }

        public String getMinPath(int begin, int end){
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
