package core;

import java.util.ArrayList;

import examples.maze.Maze;

public class GeneticAlgorithm {
	
	private ArrayList<Genome> genomes;
	private int populationSize;
	
	private double crossoverRate;
	private double mutationRate;
	
	private int chromosomeLength;
	private int fittestGenome;
	
	private double bestFitnessScore;
	private double totalFitnessScore;
	
	private int generation;
	
	private boolean isBusy;
	
	private Maze maze;
	
	public GeneticAlgorithm(double crossoverRate, double mutationRate,
			int populationSize, int numOfBits, Maze maze){
		this.crossoverRate = crossoverRate;
		this.mutationRate = mutationRate;
		this.populationSize = populationSize;
		this.chromosomeLength = numOfBits;
		this.totalFitnessScore = 0;
		this.generation = 0;
		this.maze = maze;
		isBusy = false;
		genomes = new ArrayList<Genome>();
		createStartPopulation();
	}
	
	private void mutate(Genome baby){
		for(int i = 0; i<baby.getDir().size(); i++){
			if(Math.random() < mutationRate){
				if(baby.getDir().get(i) == 1)
					baby.getDir().set(i, 0);
				else
					baby.getDir().set(i, 1);
			}
		}
	}
	
	//might be problematic??
	private void crossOver(Genome mom, Genome dad, Genome baby1, Genome baby2){
		if(Math.random() > crossoverRate){
			baby1.setDir(mom.getDir());
			baby2.setDir(dad.getDir());
			return;
		}
		int point = (int) (Math.random()*chromosomeLength);
		for(int i = 0; i < point; i++){
			baby1.getDir().add(mom.getDir().get(i));
			baby2.getDir().add(dad.getDir().get(i));
		}
		for(int i = point; i < mom.getDir().size(); i++){
			baby1.getDir().add(dad.getDir().get(i));
			baby2.getDir().add(mom.getDir().get(i));
		}
	}
	
	private Genome RouletteWheelSelection(){
		double slice = Math.random() * totalFitnessScore;
		double total = 0;
		int selectedGenome = 0;
		for(int i = 0; i < populationSize; i++){
			total += genomes.get(i).getFitness();
			if(total > slice){
				selectedGenome = i;
				break;
			}
		}
		return genomes.get(selectedGenome);
	}
	
	private void updateFitnessScores(){
		totalFitnessScore = 0;
		bestFitnessScore = 0;
		for(Genome g : genomes){
			g.setFitness(maze.testRoute(decode(g.getDir())));
			totalFitnessScore += g.getFitness();
			if(g.getFitness() > bestFitnessScore)
				bestFitnessScore = g.getFitness();
		}
	}
	
	//decodes vector of ints into directions
	private ArrayList<Integer> decode(ArrayList<Integer> dir){
		ArrayList<Integer> decoded = new ArrayList<Integer>();
		for(int i = 0; i<dir.size(); i += 2){
			String code = "" + dir.get(i) + "" + dir.get(i+1);
			if(code.equals("00"))
				decoded.add(0);
			else if(code.equals("01"))
				decoded.add(1);
			else if(code.equals("10"))
				decoded.add(2);
			else if(code.equals("11"))
				decoded.add(3);
		}
		return decoded;
	}
	
	private void createStartPopulation(){
		for(int i = 1; i <= populationSize; i++)
			genomes.add(new Genome(chromosomeLength));
	}
	
	public void epoch(){
		updateFitnessScores();
		
		int numOfBabies = 0;
		ArrayList<Genome> babyGenomes = new ArrayList<Genome>();
		
		while(numOfBabies < populationSize){
			Genome mom = RouletteWheelSelection();
			Genome dad = RouletteWheelSelection();
			Genome baby1 = new Genome();
			Genome baby2 = new Genome();

			crossOver(mom, dad, baby1, baby2);
			mutate(baby1);
			mutate(baby2);
			
			babyGenomes.add(baby1);
			babyGenomes.add(baby2);
			numOfBabies += 2;
		}
		
		genomes = babyGenomes;
		generation++;
	}
	
	public int getGeneration(){
		return generation;
	}
	
	public int getFittestGenome(){
		return fittestGenome;
	}
	
	public boolean isStarted(){
		return isBusy;
	}
	
	public void stop(){
		isBusy = false;
	}
}