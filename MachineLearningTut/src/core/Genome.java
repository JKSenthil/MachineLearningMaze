package core;

import java.util.ArrayList;

public class Genome {
	
	private ArrayList<Integer> dir;
	private double fitness;
	
	public Genome(){
		fitness = 0;
		dir = new ArrayList<Integer>();
	}
	
	public Genome(int numOfBits){
		this();
		for(int i = 0; i<numOfBits; i++){
			double r = Math.random();
			if(r<0.5)
				dir.add(0);
			else 
				dir.add(1);
		}
	}
	
	public void setDir(ArrayList<Integer> dir){
		this.dir = dir;
	}
	
	public ArrayList<Integer> getDir(){
		return dir;
	}
	
	public void setFitness(double fitness){
		this.fitness = fitness;
	}
	
	public double getFitness(){
		return fitness;
	}
}