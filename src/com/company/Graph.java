package com.company;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

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
        if (u<0 || weight<1)
            throw new IllegalArgumentException();
        if(!contains(v))
            add(v,"Not defined");
        Vertex ver = get(v);
        ver.addEdge(u,weight);
        nrOfEdges++;
    }

    public void add(int v,String name){
        if (v<0)
            throw new IllegalArgumentException();
        verticesList.add(new Vertex(v,name));
        nrOfVertices++;
    }

    private boolean contains(int v){
        if (v<0)
            throw new IllegalArgumentException();
        boolean contains=false;
        for (Vertex ver : verticesList)
            if (ver.getNr()==v)
                contains=true;
        return contains;
    }

    public Vertex get(int nr){
        for (Vertex v : verticesList)
            if (v.getNr()==nr)
                return v;
        return null;
    }

    public Vertex remove(int nr){
        for (Vertex ver : verticesList) {
            if (ver.getNr() == nr) {
                verticesList.remove(ver);
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
    }



    private class Vertex{
        private int nr;
        private String data;
        private int edges;
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

        @Override
        public String toString() {
            return "Nr wierzchołka: "+nr+" Miasto: "+data;
        }

        public int getNr() {
            return nr;
        }

        public String getData() {
            return data;
        }
    }
    
}
