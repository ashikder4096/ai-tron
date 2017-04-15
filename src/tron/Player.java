package tron;

import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {
	int totalPlayers, myPNum;
	
    static int myX0 = 0;
    static int myY0 = 0;
    static int myX1 = 0;
    static int myY1 = 0;
    
    static int width, height;
    static Node[][] map;
    
    static AStar astar;
    static Queue<Node> path = new LinkedList<>();
    
    static String mode;
    
    public static void main(String args[]) {
    	int width = 30;
    	int height = 20;
    	createBoard(width, height);
        Scanner in = new Scanner(System.in);
        	
        // game loop
        while (true) {
            int N = in.nextInt(); // total number of players (2 to 4).
            int P = in.nextInt(); // your player number (0 to 3).
            for (int i = 0; i < N; i++) 
            {
                int X0 = in.nextInt(); // starting X coordinate of lightcycle (or -1)
                int Y0 = in.nextInt(); // starting Y coordinate of lightcycle (or -1)
                int X1 = in.nextInt(); // starting X coordinate of lightcycle (can be the same as X0 if you play before this player)
                int Y1 = in.nextInt(); // starting Y coordinate of lightcycle (can be the same as Y0 if you play before this player)
                map[X1][Y1].setOwner(i);
                map[X0][Y0].setOwner(i);
                if (i==P) { //setting "my" x and y
                    myX1 = X1;
                    myY1 = Y1;
                }
            }
        }
    }
    
    public static void createBoard(int width, int height)
    {	
        map = new Node[height][width]; //creating a map variable with rectangular width, height
        
        for (int i = 0; i < height; i++) //creating a map where every "square" is a node
        {
        	for (int j = 0; j < height; j++)
        	{
        		map[i][j] = new Node(i,j);        	
        	}
        }
        
        astar = new AStar(map);
    }
    
    public static boolean edges() //need to fix this
    {
        if(myX1 == width-1)
        {
        	if(map[myX1+1][myY1].getOwner() == -1)
        	{
        		System.out.println("RIGHT"); // A single line with UP, DOWN, LEFT or RIGHT
        		return true;
        	}
        }
        if(myX1 > 0)
        {
        	if (map[myX1-1][myY1].getOwner() == -1)
        	{
        		System.out.println("LEFT");
        		return true;
        	}
        }
        if(myY1 < height-1)
        {
        	if(map[myX1][myY1+1].getOwner() == -1)
        	{
        		System.out.println("DOWN");
        		return true;
        	}
        }
        if(myY1 > 0)
        {
        	if(map[myX1][myY1-1].getOwner() == -1)
        	{
        		System.out.println("UP");
        		return true;
        	}
        }
        return false;
    }
    
    public boolean clearPath() //checks if our desire route was crossed by another player
    {
    	for(Node n : path)
    	{
    		if(n.getOwner() != -1)
    		{
    			return false;
    		}
    	}
    	return true;
    }
    
    public boolean isExitPossible()
    {
		return false;
    }
    
    public void survivalMode()
    {
    	//When in survivalMode
    }
    
    public void attackMode()
    {
    	//When in attack mode
    	//first go to the center to divide the map into four pieces
    	//Find the best optimal path to block the enemy off inside a box using shortest path
    	//Consider the min distance it takes to get to a node for both players
    }
}


class Node implements Comparable<Node>{
    private int posX, posY;
    private double disFromGoal, disFromStart, cost;
    private Node parent;
    private ArrayList<Node> children = new ArrayList<>();
    private int owner;
	
	public Node(int x, int y){
        posX = x;
        posY = y;
        this.owner = -1;
    }

    public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public double getDisFromGoal() {
		return disFromGoal;
	}

	public void setDisFromGoal(double disFromGoal) {
		this.disFromGoal = disFromGoal;
	}

	public double getDisFromStart() {
		return disFromStart;
	}

	public void setDisFromStart(double disFromStart) {
		this.disFromStart = disFromStart;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}
	
    public void setOwner(int owner)
    {
    	this.owner = owner;
    }
    public int getOwner()
    {
    	return this.owner;
    }
    
    public void addChild(Node child)
    {
    	children.add(child);
    }


	public void setChildren(ArrayList<Node> children) {
		this.children = children;
	}

	public ArrayList<Node> getChildren(){
    	return children;
    }
	
//    An auxiliary function which allows
//    us to remove any child nodes from
//    our list of child nodes.
    public void removeChild(Node n){
        children.remove(n);
    }
    
    public String toString()
    {
    	return "(" + posX + " , " + posY + ") Cost: " + getCost();
    }

	public double getCost() {
		// TODO Auto-generated method stub
		return cost;
	}
	
	public double distanceFrom(Node n)
	{
		return Math.sqrt((n.getPosX() - posX)*(n.getPosX() - posX) + (n.getPosY() - posY)*(n.getPosY() - posY));
	}
	
	public void setCost() {
		// TODO Auto-generated method stub
		cost = disFromStart + disFromGoal;
	}

	@Override
	public int compareTo(Node arg0) {
		// TODO Auto-generated method stub
		this.setCost();
		arg0.setCost();
		return (int)Double.compare(this.getCost() , arg0.getCost());
	}

}

class AStar{
	
	PriorityQueue<Node> queue = new PriorityQueue<>();
	ArrayList<Node> explored = new ArrayList<>();
	Node[][] grid; //flip x and y coordinate
	Queue<Node> path = new LinkedList<>();
	Node startNode;
	Node goalNode;
	int minDistance = Integer.MAX_VALUE;
	
	public AStar(Node[][] map) {
		grid = map;
	}
	
	@SuppressWarnings("unused")
	private Queue<Node> getShortestPath(Node startNode, Node goalNode)
	{
		this.startNode = startNode;
		this.goalNode = goalNode;
		//sets up start and goal nodes
		grid[startNode.getPosX()][startNode.getPosY()] = startNode;
		grid[goalNode.getPosX()][goalNode.getPosY()] = goalNode;
		
		startNode.setDisFromStart(0);
		startNode.setDisFromGoal(startNode.distanceFrom(goalNode));
		startNode.setCost();
		
		queue.add(startNode); //will initialize with startNode being added to the queue
		startNode.setParent(null);
		
		search();
		
		return path;
	}
	
	private Queue<Node> getLongestPath(Node startNode, Node goalNode) //Finds the logest path from one point to another
	{
		return path;
	}
	
	public PriorityQueue<Node> getQueue() {
		return queue;
	}

	public void setQueue(PriorityQueue<Node> queue) {
		this.queue = queue;
	}

	public ArrayList<Node> getExplored() {
		return explored;
	}

	public void setExplored(ArrayList<Node> explored) {
		this.explored = explored;
	}

	public Node[][] getGrid() {
		return grid;
	}

	public void setGrid(Node[][] grid) {
		this.grid = grid;
	}
	
	public void search(){
		while(!queue.isEmpty()){
			Node parent = queue.remove(); //takes out the node
			explored.add(parent); //adds the current node to explored
			
			if(parent.equals(goalNode)) //if the goal is found
			{
				populatePath(goalNode); //displays the path
			}
			else
			{
				setNodeChildren(parent);
				for(int i = 0 ; i < parent.getChildren().size() ; i++)
				{
					Node child = parent.getChildren().get(i);
					if(child != null && child.getOwner() == -1) //if it has no owner
					{
						if(!queue.contains(child) && !explored.contains(child))
						{
							child.setParent(parent); //sets node's parent
							child.setDisFromStart(parent.getDisFromStart() + 1);
							child.setDisFromGoal(child.distanceFrom(goalNode));
							child.setCost();
							queue.add(child); //adds to queue
						}
					}
				}
			}
		}
		System.out.println("Path not found");
	}

	private void populatePath(Node goal) {
		minDistance = 0;
		path.clear();
		while(goal.getParent() != null)
		{
			minDistance++;
			path.add(goal);
			goal = goal.getParent();
		}
	}
	
	private void setNodeChildren(Node n) //probably made this more complicated than it has to
	{
		int x = n.getPosX(), y=n.getPosY();
		//edges and their horizontals
		if(n.getPosX() == 0) //Deals with (0,x)
		{
			n.addChild(grid[x + 1][y]); //if(0,0)
			if(n.getPosY() == 0)
			{
				n.addChild(grid[x][y+1]);
			}
			else if(n.getPosY() == grid[x].length-1) //if(0,last)
			{
				n.addChild(grid[x][y - 1]);
			}
			else
			{
				n.addChild(grid[x][y - 1]);
				n.addChild(grid[x][y+1]);
			}
		}
		else if(n.getPosX() == grid.length - 1) //Node [last,x]
		{
			n.addChild(grid[x - 1][y]);
			if(n.getPosY() == 0)
			{
				n.addChild(grid[x][y + 1]);
			}
			else if(n.getPosY() == grid[y].length-1) //if(0,last)
			{
				n.addChild(grid[x][y - 1]);
			}
			else
			{
				n.addChild(grid[x][y-1]);
				n.addChild(grid[x][y+1]);
			}
		}
		else if(n.getPosY() == 0)
		{
			n.addChild(grid[x][y + 1]);
			n.addChild(grid[x - 1][y]);
			n.addChild(grid[x + 1][y]);
		}
		else if(n.getPosY() == grid.length - 1)
		{
			n.addChild(grid[x][y - 1]);
			n.addChild(grid[x - 1][y]);
			n.addChild(grid[x + 1][y]);
		}
		else
		{
			n.addChild(grid[x][y + 1]);
			n.addChild(grid[x][y - 1]);
			n.addChild(grid[x - 1][y]);
			n.addChild(grid[x + 1][y]);
		}
	}
}
