package tron;

import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {
    static int myX0 = 0;
    static int myX1 = 0;
    static int myY0 = 0;
    static int myY1 = 0;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int height = 20;
        int width = 30;
        Node[][] map = new Node[width][height]; //creating a map variable with rectangular width, height
        
        for (int i = 0; i < width; i++) //creating a map where every "square" is a node
        {
        	for (int j = 0; j < height; j++)
        	{
        		map[i][j] = new Node(i,j);        	
        	}
        }
        	
        // game loop
        while (true) //insert logic for how to move
        {
            int N = in.nextInt(); // total number of players (2 to 4).
            int P = in.nextInt(); // your player number (0 to 3).
            for (int i = 0; i < N; i++) { //creates lightcycle for each players and sets their position as "owner"
            	//current position
                int X0 = in.nextInt(); // starting X coordinate of lightcycle (or -1)
                int Y0 = in.nextInt(); // starting Y coordinate of lightcycle (or -1) 
                //new position
                int X1 = in.nextInt(); // starting X coordinate of lightcycle (can be the same as X0 if you play before this player)
                int Y1 = in.nextInt(); // starting Y coordinate of lightcycle (can be the same as Y0 if you play before this player)
                //which grids the player occupies
                map[X1][Y1].setOwner(i);
                map[X0][Y0].setOwner(i);
                if (i==P) { //setting "my" x and y
                    myX1 = X1;
                    myY1 = Y1;
                }
            }
            //System.err.println(X1);
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            boolean moved = false;
            //Our logic for moving around
            //Start: what happens if lightcycle is at the edge
            if(myX1 < width-1)
            {
            	if(map[myX1+1][myY1].getOwner() == -1)
            	{
            		System.out.println("RIGHT"); // A single line with UP, DOWN, LEFT or RIGHT
            		moved = true;
            	}
            }
            if(myX1 > 0 && !moved)
            {
            	if (map[myX1-1][myY1].getOwner() == -1)
            	{
            		System.out.println("LEFT");
            		moved = true;
            	}
            }
            if(myY1 < height-1 && !moved)
            {
            	if(map[myX1][myY1+1].getOwner() == -1)
            	{
            		System.out.println("DOWN");
            		moved = true;
            	}
            }
            if(myY1 > 0 && !moved)
            {
            	if(map[myX1][myY1-1].getOwner() == -1)
            	{
            		System.out.println("UP");
            		moved = true;
            	}
            }
            //End: Edges
        }
    }
}


class Node implements Comparable<Node>{
	
    private String name;
    private ArrayList<Node> children;
    private int distance = Integer.MAX_VALUE;
    private double cost = Integer.MAX_VALUE;
    private Node parent;
    private int owner;
    
    int index;
    int x;
    int y;
    
    public Node(String name, ArrayList<Node> children)
    {
        this.name = name;
        this.children = children;
    }
    public Node(String name, ArrayList<Node> children, int index)
    {
        this.name = name;
        this.children = children;
        this.index = index;
    }
    public Node(int x, int y)
    {
        this.name = "(" + x + "," + y + ")";
        this.x = x;
        this.y = y;
        this.owner = -1; //-1 means there is no owner of this node
        }

    public ArrayList<Node> getChildren()
    {
        return this.children;
    }

    public void setChildren(ArrayList<Node> children)
    {
        this.children = children;
    }
    
    public int getDistance()
    {
    	return this.distance;
    }
    public void setDistance(int dist) 
    {
    	this.distance = dist;
    }
    public Node getParent()
    {
    	return this.parent;
    }
    public void setParent(Node node)
    {
    	this.parent = node;
    }

    
    public String toString()
    {
        return this.name;
    }
   
    public double getCost() {
    	return this.cost;
    }
    
    public void setCost(double cost)
    {
    	this.cost = cost;
    }
    public void setOwner(int owner)
    {
    	this.owner = owner;
    }
    public int getOwner()
    {
    	return this.owner;
    }
    
    
    public int compareTo(Node node)
    {
		return Double.compare(this.cost, node.cost);
    }
}