import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class tictactoe extends JFrame{
	public static int[][] board = new int[3][3];
	public static int bestMoveIndexI;
	public static int bestMoveIndexJ;
	public static int score;
	protected JPanel controlPanel;
	public String a = "";
	public String b = "";
	public String c = "";
	public String d = "";
	public String e = "";
	public String f = "";
	public String g = "";
	public String h = "";
	public String i = "";
	public static int count = 0;
	public static JButton button1;
	public static JButton button2;
	public static JButton button3;
	public static JButton button4;
	public static JButton button5;
	public static JButton button6;
	public static JButton button7;
	public static JButton button8;
	public static JButton button9;
	public static JButton button0;
	public static String winner;
	
	protected JPanel viewPanel;
	public tictactoe(String firstOrSecond) {
		super("GUI Lab");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setSize(500,500);
		controlPanel = new JPanel();
		viewPanel = new JPanel();

		
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		controlPanel.setBounds(1,1,500,500);
		button0 = new JButton("Reset");
		viewPanel.add(button0);
		viewPanel.setPreferredSize(new Dimension(10,10));
		
		for(int i = 0; i<3; i++) {
			for(int j = 0; j<3; j++) {
				board[i][j]=0;
			}
		}
		ActionListener buttonListener = new buttonListener();
		
		
		button1 = new JButton(a);
		button1.setFont(new Font("Arial", Font.PLAIN, 150));
		button1.setPreferredSize(new Dimension(150,150));
		button1.addActionListener(buttonListener);
		
		button2 = new JButton(b);
		button2.setFont(new Font("Arial", Font.PLAIN, 150));
		button2.setPreferredSize(new Dimension(150,150));
		button2.addActionListener(buttonListener);
		
		button3 = new JButton(c);
		button3.setPreferredSize(new Dimension(150,150));
		button3.setFont(new Font("Arial", Font.PLAIN, 150));
		button3.addActionListener(buttonListener);
		
		button4 = new JButton(d);
		button4.setPreferredSize(new Dimension(150,150));
		button4.setFont(new Font("Arial", Font.PLAIN, 150));
		button4.addActionListener(buttonListener);
		
		button5 = new JButton(e);
		button5.setPreferredSize(new Dimension(150,150));
		button5.setFont(new Font("Arial", Font.PLAIN, 150));
		button5.addActionListener(buttonListener);
		
		button6 = new JButton(f);
		button6.setPreferredSize(new Dimension(150,150));
		button6.setFont(new Font("Arial", Font.PLAIN, 150));
		button6.addActionListener(buttonListener);
		
		button7 = new JButton(g);
		button7.setPreferredSize(new Dimension(150,150));
		button7.setFont(new Font("Arial", Font.PLAIN, 150));
		button7.addActionListener(buttonListener);
		
		button8 = new JButton(h);
		button8.setPreferredSize(new Dimension(150,150));
		button8.setFont(new Font("Arial", Font.PLAIN, 150));
		button8.addActionListener(buttonListener);
		
		button9 = new JButton(i);
		button9.setPreferredSize(new Dimension(150,150));
		button9.setFont(new Font("Arial", Font.PLAIN, 150));
		button9.addActionListener(buttonListener);
		
		
		controlPanel.add(button1);
		controlPanel.add(button2);
		controlPanel.add(button3);
		controlPanel.add(button4);
		controlPanel.add(button5);
		controlPanel.add(button6);
		controlPanel.add(button7);
		controlPanel.add(button8);
		controlPanel.add(button9);
		
		if(firstOrSecond.equals("second")) {
			computerMove();
			count++;
		}
		
		this.add(controlPanel);
		
}
	public class buttonListener implements ActionListener{

		public void actionPerformed(ActionEvent click) {
			if(click.getSource() == button1 && board[0][0]==0){
				if(count%2 == 0) {
					button1.setText("x");
					count++;
					board[0][0]=1;
					if(!checkWinner().equals("Player 1")) {
						computerMove();
					}
					
				}
				else {
					button1.setText("o");
					count++;
					board[0][0]=-1;
					if(!checkWinner().equals("Player 1")) {
						computerMove();
					}
				}
				checkWinner();
				printWinner();
			}
			if(click.getSource() == button2 && board[0][1]==0){
				if(count%2 == 0) {
					button2.setText("x");
					count++;
					board[0][1]=1;
					if(!checkWinner().equals("Player 1")) {
						computerMove();
					}
				}
				else {
					button2.setText("o");
					count++;
					board[0][1]=-1;
					if(!checkWinner().equals("Player 1")) {
						computerMove();
					}
				}
				checkWinner();
				printWinner();
			}
			if(click.getSource() == button3 && board[0][2]==0){
				if(count%2 == 0) {
					button3.setText("x");
					count++;
					board[0][2]=1;
					if(!checkWinner().equals("Player 1")) {
						computerMove();
					}
				}
				else {
					button3.setText("o");
					count++;
					board[0][2]=-1;
					if(!checkWinner().equals("Player 1")) {
						computerMove();
					}
				}
				checkWinner();
				printWinner();
			}
			if(click.getSource() == button4 && board[1][0]==0){
				if(count%2 == 0) {
					button4.setText("x");
					count++;
					board[1][0]=1;
					if(!checkWinner().equals("Player 1")) {
						computerMove();
					}
				}
				else {
					button4.setText("o");
					count++;
					board[1][0]=-1;
					if(!checkWinner().equals("Player 1")) {
						computerMove();
					}
				}
				checkWinner();
				printWinner();
			}
			if(click.getSource() == button5 && board[1][1]==0){
				if(count%2 == 0) {
					button5.setText("x");
					count++;
					board[1][1]=1;
					if(!checkWinner().equals("Player 1")) {
						computerMove();
					}
				}
				else {
					button5.setText("o");
					count++;
					board[1][1]=-1;
					if(!checkWinner().equals("Player 1")) {
						computerMove();
					}
				}
				checkWinner();	
				printWinner();
			}
			if(click.getSource() == button6 && board[1][2]==0){
				if(count%2 == 0) {
					button6.setText("x");
					count++;
					board[1][2]=1;
					if(!checkWinner().equals("Player 1")) {
						computerMove();
					}
				}
				else {
					button6.setText("o");
					count++;
					board[1][2]=-1;
					if(!checkWinner().equals("Player 1")) {
						computerMove();
					}
				}
				checkWinner();
				printWinner();
			}
			if(click.getSource() == button7 && board[2][0]==0){
				if(count%2 == 0) {
					button7.setText("x");
					count++;
					board[2][0]=1;
					if(!checkWinner().equals("Player 1")) {
						computerMove();
					}
				}
				else {
					button7.setText("o");
					count++;
					board[2][0]=-1;
					if(!checkWinner().equals("Player 1")) {
						computerMove();
					}
				}
				checkWinner();
				printWinner();
			}
			if(click.getSource() == button8 && board[2][1]==0){
				if(count%2 == 0) {
					button8.setText("x");
					count++;
					board[2][1]=1;
					if(!checkWinner().equals("Player 1")) {
						computerMove();
					}
				}
				else {
					button8.setText("o");
					count++;
					board[2][1]=-1;
					if(!checkWinner().equals("Player 1")) {
						computerMove();
					}
				}
				checkWinner();
				printWinner();
			}
			if(click.getSource() == button9 && board[2][2]==0){
				if(count%2 == 0) {
					button9.setText("x");
					count++;
					board[2][2]=1;
					if(!checkWinner().equals("Player 1")) {
						computerMove();
					}
				}
				else {
					button9.setText("o");
					count++;
					board[2][2]=-1;
					if(!checkWinner().equals("Player 1")) {
						computerMove();
					}
				}
				checkWinner();
				printWinner();
			}
		}

	}
	public static void printBoard() {
		for(int i = 0; i<3; i++) {
			for(int j = 0; j<3; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}
	public static String checkWinner() {
		winner = "null";
		if(board[0][0]==1){
			if(board[0][1]==1 && board[0][2]==1) {
				winner = "Player 1";
			}
			if(board[1][0]==1 && board[2][0]==1) {
				winner = "Player 1";
			}
			if(board[1][1]==1 && board[2][2]==1) {
				winner = "Player 1";
			}
		}
		if(board[0][1]==1) {
			if(board[1][1]==1&&board[2][1]==1) {
				winner = "Player 1";
			}
		}
		if(board[0][2]==1) {
			if(board[1][1]==1 && board[2][0]==1) {
				winner = "Player 1";
			}
			if(board[1][2]==1 && board[2][2]==1) {
				winner = "Player 1";
			}
		}
		if(board[1][0]==1){
			if(board[1][1]==1 && board[1][2]==1) {
				winner = "Player 1";
			}
		}
		if(board[2][0]==1) {
			if(board[2][1]==1 && board[2][2]==1) {
				winner = "Player 1";
			}
		}
		if(board[0][0]==-1){
			if(board[0][1]==-1 && board[0][2]==-1) {
				winner = "Player 2";
			}
			if(board[1][0]==-1 && board[2][0]==-1) {
				winner = "Player 2";
			}
			if(board[1][1]==-1 && board[2][2]==-1) {
				winner = "Player 2";
			}
		}
		if(board[0][1]==-1) {
			if(board[1][1]==-1&&board[2][1]==-1) {
				winner = "Player 2";
			}
		}
		if(board[0][2]==-1) {
			if(board[1][1]==-1 && board[2][0]==-1) {
				winner = "Player 2";
			}
			if(board[1][2]==-1 && board[2][2]==-1) {
				winner = "Player 2";
			}
		}
		if(board[1][0]==-1){
			if(board[1][1]==-1 && board[1][2]==-1) {
				winner = "Player 2";
			}
		}
		if(board[2][0]==-1) {
			if(board[2][1]==-1 && board[2][2]==-1) {
				winner = "Player 2";

			}
		}
		int counter = 0;
		for(int i = 0; i<3; i++) {
			for(int j = 0; j<3; j++) {
				if(board[i][j]==0) {
					counter++;
				}
			}
		}
		if(counter==0 && winner!= "Player 1" && winner != "Player 2") {
			winner = "Tie";
		}
		
		
		return(winner);
		//return("Player 2");
		//return("Tie");
	}
	public static void printWinner() {
		
		if(winner.equals("Tie")) {
			System.out.println(winner);
		}
		else if(winner.equals("Player 1") || winner.equals("Player 2")) {
		System.out.println("The winner is " + winner);
		}
	}
	public static void computerMove() {
		int bestScore = Integer.MIN_VALUE;
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if(board[i][j]==0) {
					board[i][j]=-1;	
					int score = minimax(board, 0, false);
					board[i][j]=0;
					if(score > bestScore) {
						bestScore = score;
						bestMoveIndexI = i;
						bestMoveIndexJ = j;
					}
					
					
				}
			}
		}
		goComputer();
	}
	public static void goComputer() {
		if(bestMoveIndexI == 0) {
			if (bestMoveIndexJ == 0) {
				button1.setText("o");
				count++;
				board[bestMoveIndexI][bestMoveIndexJ]=-1;
			}
			if (bestMoveIndexJ == 1) {
				button2.setText("o");
				count++;
				board[bestMoveIndexI][bestMoveIndexJ]=-1;
			}
			if (bestMoveIndexJ == 2) {
				button3.setText("o");
				count++;
				board[bestMoveIndexI][bestMoveIndexJ]=-1;
			}
		}
		if(bestMoveIndexI == 1) {
			if (bestMoveIndexJ == 0) {
				button4.setText("o");
				count++;
				board[bestMoveIndexI][bestMoveIndexJ]=-1;
			}
			if (bestMoveIndexJ == 1) {
				button5.setText("o");
				count++;
				board[bestMoveIndexI][bestMoveIndexJ]=-1;
			}
			if (bestMoveIndexJ == 2) {
				button6.setText("o");
				count++;
				board[bestMoveIndexI][bestMoveIndexJ]=-1;
			}
		}
		if(bestMoveIndexI == 2) {
			if (bestMoveIndexJ == 0) {
				button7.setText("o");
				count++;
				board[bestMoveIndexI][bestMoveIndexJ]=-1;
			}
			if (bestMoveIndexJ == 1) {
				button8.setText("o");
				count++;
				board[bestMoveIndexI][bestMoveIndexJ]=-1;
			}
			if (bestMoveIndexJ == 2) {
				button9.setText("o");
				count++;
				board[bestMoveIndexI][bestMoveIndexJ]=-1;
			}
		}
	}
	public static int minimax(int[][] board, int depth, boolean isMax) {
		checkWinner();
		if(winner.equals("Player 2")) {
			int score = 1;
			return score;
		}
		if(winner.equals("Player 1")) {
			int score = -1;
			return score;
		}
		if(winner.equals("Tie")) {
			int score = 0;
			return score;
		}
		if(isMax) {
			int bestScore = Integer.MIN_VALUE;
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					if(board[i][j]==0) {
						board[i][j]=-1;	
						int score = minimax(board, depth+1, false);
						board[i][j]=0;
						bestScore = Math.max(score, bestScore);
							
						
					}
				}
			}
			return bestScore;
		}
		else {
			int bestScore = Integer.MAX_VALUE;
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					if(board[i][j]==0) {
						board[i][j]=1;	
						int score = minimax(board, depth+1, true);
						board[i][j]=0;
						bestScore = Math.min(score, bestScore);
							
					}
				}
			}
			return bestScore;
		}

	}
	public static void main(String[]args) {
		System.out.println("Would you like to go first or second?");
		Scanner scnr = new Scanner(System.in);
		String firstOrSecond = scnr.next();
		
		tictactoe frame1 = new tictactoe(firstOrSecond);
		frame1.setVisible(true);
	}
	
}
