package com.company;

public class Main {

    public static void main(String[] args) {
		Graph graph = new Graph();
		graph.add("Alexandria");
		graph.add("Blacksburg");
		graph.add("Charlottesville");
		graph.add("Danville");
		graph.add("Fredericksburg");
		graph.add("Harrisonburg");
		graph.add("Lynchburg");
		graph.add("Newport News");
		graph.add("Richmond");
		graph.add("Roanoke");
		graph.add("Virginia Beach");

		graph.addEdge("Alexandria", "Fredericksburg", 50);
		graph.addEdge("Fredericksburg", "Richmond", 60);
		graph.addEdge("Richmond", "Charlottesville", 70);
		graph.addEdge("Richmond", "Lynchburg", 110);
		graph.addEdge("Richmond", "Danville", 145);
		graph.addEdge("Richmond", "Newport News", 70);
		graph.addEdge("Newport News", "Virginia Beach", 35);
		graph.addEdge("Virginia Beach", "Danville", 210);
		graph.addEdge("Danville", "Lynchburg", 70);
		graph.addEdge("Lynchburg", "Charlottesville", 70);
		graph.addEdge("Lynchburg", "Roanoke", 65);
		graph.addEdge("Roanoke", "Blacksburg", 40);
		graph.addEdge("Blacksburg", "Harrisonburg", 140);
		graph.addEdge("Harrisonburg", "Alexandria", 135);
		graph.addEdge("Harrisonburg", "Charlottesville", 50);

		graph.addEdge("Fredericksburg","Alexandria", 50);
		graph.addEdge( "Richmond","Fredericksburg",  60);
		graph.addEdge( "Charlottesville","Richmond",  70);
		graph.addEdge( "Lynchburg","Richmond",  110);
		graph.addEdge( "Danville","Richmond",  145);
		graph.addEdge( "Newport News","Richmond",  70);
		graph.addEdge( "Virginia Beach","Newport News",  35);
		graph.addEdge( "Danville","Virginia Beach",  210);
		graph.addEdge("Lynchburg","Danville",  70);
		graph.addEdge( "Charlottesville","Lynchburg",  70);
		graph.addEdge( "Roanoke","Lynchburg",  65);
		graph.addEdge( "Blacksburg","Roanoke",  40);
		graph.addEdge( "Harrisonburg","Blacksburg",  140);
		graph.addEdge( "Alexandria","Harrisonburg", 135);
		graph.addEdge( "Charlottesville","Harrisonburg", 50);
		System.out.println(graph);
		graph.printShortestPath("Alexandria","Virginia Beach");
		graph.allReachable("Alexandria");
	}
}
