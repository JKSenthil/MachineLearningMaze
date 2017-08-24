package examples.maze;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import core.GeneticAlgorithm;

public class Maze extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	public static final double CROSSOVER_RATE = 0.7;
	public static final double MUTATION_RATE = 0.001;
	public static final int POPULATION_SIZE = 140;
	public static final int CHROMOSOME_LENGTH = 70;
	
	public static final int BLOCK_SIZE = 30;
	public static final int BLOCK_WIDTH = 15;
	public static final int BLOCK_HEIGHT = 10;
	
	public static final int START_COORD_X = 13;
	public static final int START_COORD_Y = 7;
	public static final int END_COORD_X = 0;
	public static final int END_COORD_Y = 3;
	
	private JPanel panel;
	private int[] map= {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1, 
						1,0,1,0,0,0,0,0,1,1,1,0,0,0,1, 
						8,0,0,0,0,0,0,0,1,1,1,0,0,0,1, 
						1,0,0,0,1,1,1,0,0,1,0,0,0,0,1, 
						1,0,0,0,1,1,1,0,0,0,0,0,1,0,1, 
						1,1,0,0,1,1,1,0,0,0,0,0,1,0,1, 
						1,0,0,0,0,1,0,0,0,0,1,1,1,0,1, 
						1,0,1,1,0,0,0,1,0,0,0,0,0,0,1, 
						1,0,1,1,0,0,0,1,0,0,0,0,0,0,1, 
						1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
	
	private int playerX = 0;
	private int playerY = 0;
	
	private static GeneticAlgorithm ga;
	
	public Maze(){
		panel = new JPanel(){
		
			private static final long serialVersionUID = 1L;
			
			@Override
			public void paintComponent(Graphics g) {
				draw(g);
				super.paintComponent(g);
			}
		};
		setPreferredSize(new Dimension(BLOCK_WIDTH*BLOCK_SIZE, BLOCK_HEIGHT*BLOCK_SIZE));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		add(panel);
		pack();
		setVisible(true);
		
		ga = new GeneticAlgorithm(CROSSOVER_RATE, MUTATION_RATE,
				POPULATION_SIZE, CHROMOSOME_LENGTH, this);
	}

	private void draw(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.RED);
		if(ga != null)
			g.drawString("Generation " + ga.getGeneration(), 10, 20);
		
		for(int x = 0; x<BLOCK_WIDTH; x++){
			for(int y = 0; y<BLOCK_HEIGHT; y++){
				g.setColor(Color.BLACK);
				int index = x + y * BLOCK_WIDTH;
				if(map[index] == 1) g.drawRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
				if(map[index] == 8){
					g.setColor(Color.RED);
					g.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
				}
				if(map[index] == 5){
					g.setColor(Color.BLUE);
					g.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
				}
			}
		}
		
		if(playerX != 0 && playerY != 0){
			g.setColor(Color.GREEN);
			g.fillRect(playerX * BLOCK_SIZE, playerY * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
		}
		
		g.dispose();
	}
	
	//returns fitness value
	public double testRoute(ArrayList<Integer> dir){
		int currentX = START_COORD_X;
		int currentY = START_COORD_Y;
		for(Integer i : dir){
			int index;
			if(i == 0){
				index = currentX + (currentY-1) * BLOCK_WIDTH;
				if(map[index]!=1)
					currentY--;
			}else if(i == 1){
				index = currentX + (currentY+1) * BLOCK_WIDTH;
				if(map[index]!=1)
					currentY++;
			}else if(i == 2){
				index = (currentX+1) + currentY * BLOCK_WIDTH;
				if(map[index]!=1)
					currentX++;
			}else if(i == 3){
				index = (currentX-1) + currentY * BLOCK_WIDTH;
				if(map[index]!=1)
					currentX--;
			}
			playerX = currentX;
			playerY = currentY;
			repaint();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		playerX = 0;
		playerY = 0;
		int diffX = Math.abs(currentX - END_COORD_X);
		int diffY = Math.abs(currentY - END_COORD_Y);
		
		return 1/(double) (diffX+diffY+1);
	}

	public static void main(String[] args) {
		new Maze();
		while(true){
			ga.epoch();
		}
	}

}