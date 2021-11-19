//Rohaan Ahmad and Miles Vilke
//rahmad3@u.rochester.edu mvilke@u.rochester.edu
//29 April 2021
//CSC 171
//Project 3
//
//Flourishes: a,c,g

import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Hashtable;
import java.util.Random;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOfLife extends JFrame{
	protected JPanel controlPanel;
	protected JPanel viewPanel;
	public JButton button1;
	public JButton button2;
	public JButton button3;
	public int size = 20; //size of grid
	JLabel label = new JLabel();
	int[][] array = new int[size][size];
	protected Timer timer;
	public int conwayCounter = 0; //counts number of times simulation has been run
	JSlider slider;
	JSlider slider2;
	public int speedNumber = 1000; //holds value of slider
	
	public GameOfLife() {
		super("Game Of Life");
		
		//creates frame with two panels
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setSize(1000,800);
		controlPanel = new JPanel();
		viewPanel = new viewPanel();
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS)); //lets buttons display nicely at the top
		
		//set size of panel to look nice, this makes the dimensions work out very nicely
		viewPanel.setPreferredSize(new Dimension(500, 474));
		
		
		
		//creates slider for speed
		slider = new JSlider(JSlider.HORIZONTAL, 0, 10, 0);
		slider.setMinorTickSpacing(0);
		slider.setMajorTickSpacing(5);
		
		//labels sliders for speed
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(0,new JLabel("Slow") );
		labelTable.put(5,new JLabel("Fast") );
		labelTable.put(10,new JLabel("Extreme") );
		slider.setLabelTable(labelTable);
		JLabel l = new JLabel("", JLabel.CENTER);
		l.setText("|Speed|->");
		controlPanel.add(l);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		
		
		//adds sliders for speed
		controlPanel.add(slider);
		sliderClass s = new sliderClass();
		slider.addChangeListener(s);
		
		//buttons created
		button1 = new JButton("Randomize");
		button2 = new JButton("Start");
		button3 = new JButton("Stop");
		
		//maps buttons to listener
		ActionListener buttonListener = new buttonListener();
		button1.addActionListener(buttonListener);
		button2.addActionListener(buttonListener);
		button3.addActionListener(buttonListener);
		controlPanel.add(button1);
		controlPanel.add(button2);
		controlPanel.add(button3);
		
		//creates grid changing slider
		slider2 = new JSlider(JSlider.HORIZONTAL, 0, 20, 20);
		slider2.setMinorTickSpacing(0);
		slider2.setMajorTickSpacing(10);
		controlPanel.add(slider2);
		Hashtable<Integer, JLabel> labelTable2 = new Hashtable<Integer, JLabel>();
		labelTable2.put(0,new JLabel("0") );
		labelTable2.put(20,new JLabel("20") );
		labelTable2.put(40,new JLabel("40") );
		slider2.setLabelTable(labelTable2);
		JLabel p = new JLabel("", JLabel.CENTER);
		p.setText("<-|Grid Size|");
		controlPanel.add(p);
		slider2.setPaintLabels(true);
		slider2.setPaintTicks(true);
		
		//adds sliders for grid
		sliderGrid g = new sliderGrid();
		slider2.addChangeListener(g);
				
		//defines label and adds it
		label.setText("Count: 0");
		controlPanel.add(label);

		//adds to panels
		this.add(controlPanel);
		this.add(viewPanel);
	}
	class viewPanel extends JPanel{
		public void paintComponent(Graphics g) {
			//initialize variables
			int h = viewPanel.getHeight();
			int w = viewPanel.getWidth();
			
			int holdX = 0;
			int holdY = 0;
		
			//color grid
			for(int i = 0; i < size; i++) {
				for(int j = 0; j < size; j++) {
					if(array[i][j] == 1) {
						Color green = new Color(0,255,0);
						g.setColor(green);
						g.fillRect((j)*(w/size), (i)*(h/size),(w/size),(h/size));					
					}
					else{
						Color red = new Color(255,0,0);
						g.setColor(red);
						g.fillRect((j)*(w/size), (i)*(h/size),(w/size),(h/size));
					}
				}
			}
			Color blue = new Color(0, 0, 255);
			g.setColor(blue);
			
			//draw vertical lines
			holdX = 0;
			holdY = 0;
			for(int i = 0; i<=size; i++) {
				g.drawLine(holdX, 0, holdX, h);
				holdX += w/size;
			}
			
			//draw horizontal lines
			for(int j = 0; j<=size; j++) {
				g.drawLine(0, holdY, w, holdY);
				holdY += h/size;
			}
			
			
		}
	}
	
	//buttonListener made 
	public class buttonListener implements ActionListener{

		public void actionPerformed(ActionEvent click) {
			if(click.getSource() == button1){
				generateGrid(); //generates a new grid
				conwayCounter=0;
				label.setText("Count: 0");
				
			}
			else if (click.getSource() == button2) {
				timer = new Timer(speedNumber, new buttonListener()); //adds timer after start is hit so time sets based on slider
				timer.start(); //causes timer to start
				
			}
			else if (click.getSource() == button3){
				timer.stop(); //causes timer to stop
			}
			else {
				//the timer event defaults to this else statement, so all code in here runs once per second after timer starts until timer stops
				int[][] holdArray = new int[size][size];
				int count = 0; // counts adjacent "alive" boxes
				for(int i = 0; i<size; i++) {
					for(int j = 0; j<size; j++) {
						count = 0;
						//extra conditionals prevent out of bounds of array error
						if(i!=size-1) {
							if(array[i+1][j]==1) {
								count++; //checks cell above
							}
							if(j!=size-1){
								if(array[i+1][j+1]==1){
									count++; //checks cell to the diagonal above and right
								}
							}
							if(j!=0){
								if(array[i+1][j-1] == 1){
									count++; //checks cell to diagonal above and left
								}
							}
						}
						if(i!=0) {
							if(array[i-1][j] == 1) {
								count++; //checks cell below
							}
							if(j!=size-1){
								if(array[i-1][j+1]==1){
									count++; //checks cell to the diagonal down and right
								}
							}
							if(j!=0){
								if(array[i-1][j-1] == 1){
									count++; //checks cell to diagonal down and left
								}
							}
						}
						if(j!=size-1) {
							if(array[i][j+1] == 1) {
								count++; //checks cell to the right
							}
						}
						if(j!=0) {
							if(array[i][j-1] == 1) {
								count++; //checks cell to the left
							}
						}	
						
						//determining new array, stored temporarily in hold array
						if(array[i][j] == 1){ //cell alive
							if(count<2 || count>3) {
								holdArray[i][j]=0; //simulates conditions for a live cell to die
							}
							else{
								holdArray[i][j] = 1; //simulates live cell continuing to live
							}
						}
						else{//cell dead
							if(count == 3){
								holdArray[i][j]=1; //simulated cell coming to life
							}
							else {
								holdArray[i][j]=0; //simulates cell not coming to life
							}
						}
						
						
					}
				}
				
				//sets values stored in holdArray to the global array
				for(int i = 0; i < size; i++) {
					for(int j = 0; j < size; j++) {
						array[i][j]=holdArray[i][j];
					}
				}
				
				//adds to conwayCoutner to check how many times it was run
				conwayCounter++;
				label.setText("Count: " + conwayCounter);
				controlPanel.add(label);
				//repaint
				repaint();
			}
		}
	}
	
	//slider class
	public class sliderClass extends JFrame implements ChangeListener{
		public void stateChanged(ChangeEvent e) {
			if(slider.getValue() == 0) {
			speedNumber = 1200; //the conditional prevents errors since no division by 0
			}
			else {
				speedNumber = 1000/slider.getValue(); //makes timer faster as slider is increased
			}
		}
	}
	public class sliderGrid extends JFrame implements ChangeListener{
		public void stateChanged(ChangeEvent e){
			size = slider2.getValue();
		}
	}
	
	//method to generate Grid (for randomize button)
	public void generateGrid(){
		//store values in an array 0 = dead 1 = alive
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				Random random = new Random();
				//puts random number (either 0 or 1) in each index of array
				array[i][j] = random.nextInt(2);
			}
		}
		
		//adds timer after grid generated so time sets based on slider
		if(conwayCounter != 0) {
			timer.stop();
		}
		timer = new Timer(speedNumber, new buttonListener());
		repaint();
		
		
	}	
	public static void main(String[]args) {	
		GameOfLife frame1 = new GameOfLife();
		frame1.setVisible(true);	
	}
}
