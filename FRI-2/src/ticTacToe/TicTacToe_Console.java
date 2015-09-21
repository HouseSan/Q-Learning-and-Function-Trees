package ticTacToe;

import java.util.Scanner;

public class TicTacToe_Console {

	private static int turn, game, wins, losses, player;
	private static int xPos, yPos;  // position of cursor
	private static boolean gameEnd;
	private static char[][] board;
	private static Scanner scan = new Scanner(System.in);
	
	public TicTacToe_Console() {
		board = getNewBoard();
		turn = 2;
		game = 1;
		wins = 0;
		losses = 0;
		player = 1;
		gameEnd = false;
	}
	
	public static void main(String[] args) {
		humanVsHuman();
	}
	
	public static void humanVsHuman() {
		System.out.println("Human vs Human game start");
		while ((!gameEnd) && (turn - 2) < 9) {
			updateBoard(getHumanInput());
		}
	}
	
	public static void getHumanInput() {
		printBoard();
		System.out.println("");
		System.out.println("Player " + player + ", what is your choice?");
		String inputString = scan.next();
		if (inputString.length() == 1) {
			char answer = inputString.charAt(0);
			if (answer == 'x' || answer == 'o')
				return 
		}
	}

	/** resets for new game and resets statistics*/
	public static void totalReset() {
		board = getNewBoard();
		turn = 2;
		game = 1;
		wins = 0;
		losses = 0;
		gameEnd = false;
	}
	
	/** resets for a new game, but keeps statistics*/
	public static void minorReset() {
		board = getNewBoard();
		turn = 2;
		game = 1;
		wins = 0;
		losses = 0;
		gameEnd = false;
	}
	
	public static void printBoard() {
		System.out.println("Game: " + game + ", turn: " + (turn / 2));
		System.out.println(board);
		System.out.println();
	}
	
	public static char[][] getNewBoard() {
		return new char[][] {
				{'1', '2', '3'},
				{'6', '5', '4'},
				{'7', '8', '9'}};
	}
}
