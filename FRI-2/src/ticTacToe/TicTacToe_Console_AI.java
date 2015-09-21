package ticTacToe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe_Console_AI {
	
	public static final double didNotMoveReward = -200.;
	public static final double winReward = 100.;
	public static final double neutralMoveReward = -1.;
	public static final double tieReward = 0.;
	public static final int x = 10;
	public static final int y = 11;
	
	private static Random rand = new Random();
	private static Scanner scan = new Scanner(System.in);

	private int turn, game, wins, losses, player;
	private int xPos, yPos;  // position of cursor
	private boolean gameEnd;
	private int[][] board;
	private boolean justWon;
	private boolean justTied;
	private boolean didNotMove;
	
	public static void main(String[] args) {
		playerVPlayer();
	}
	
	/**
	 * For testing purposes; random moves vs random moves. Continue with enter.
	 */
	public static void randomVRandom() {
		TicTacToe_Console_AI game = new TicTacToe_Console_AI();
		while (true) {
			scan.nextLine();
			double score = game.doRandomMove();
			game.printBoard();
			System.out.println("Score: " + score);
			if (game.justWon) {
				System.out.println("Won!");
			} else if (game.justTied) {
				System.out.println("Tied.");
			}
		}
	}

	/**
	 * For testing purposes. Player versus player. Enter 1-9 to make a move.
	 */
	public static void playerVPlayer() {
		TicTacToe_Console_AI game = new TicTacToe_Console_AI();
		while (true) {
			game.printBoard();
			double score = game.doMove(scan.nextInt());
			System.out.println("Score: " + score);
			if (game.justWon) {
				System.out.println("Won!");
			} else if (game.justTied) {
				System.out.println("Tied.");
			}
			System.out.println();
		}
	}
	
	public TicTacToe_Console_AI() {
		board = getNewBoard();
		turn = 2;
		game = 1;
		wins = 0;
		losses = 0;
		player = 1;
		gameEnd = false;
		justWon = false;
		justTied = false;
		didNotMove = false;
	}
	
	public double doRandomMove() {
		int totalLeft = 0;
		for (int i = 0; i < 9; i++)
			if (board[i / 3][i % 3] == i + 1)
				totalLeft++;
		if (totalLeft == 0)
			throw new IllegalStateException("doRandomMove(): no moves left!");
		int randInt = rand.nextInt(totalLeft);
		int index = 0;
		while (true) {
			if (board[index / 3][index % 3] == index + 1) {
				randInt--;
				if (randInt == -1)
					break;
			}
			index++;
		}
		return doMove(index + 1);
	}
	
	/**
	 * Does a move if able, and returns a fitness value to match
	 * @param move value 1 through 9 to indicate what space to place a piece on
	 * @return the fitness value for the move made
	 */
	public double doMove(int move) {
		this.justWon = false;
		this.justTied = false;
		this.didNotMove = false;
		//if you failed to move
		int xCord = (move - 1) % 3;
		int yCord = (move - 1) / 3;
		if (move < 1 || move > 9 || board[yCord][xCord] != move) {
			this.didNotMove = true;
			return didNotMoveReward;
		}
		//else do the move / update the board
		int playerValue;
		if (player == 1)
			playerValue = x;
		else
			playerValue = y;
		board[yCord][xCord] = playerValue;
		//update player
		this.player++;
		if (this.player == 3)
			this.player = 1;
		//check if a win
		if (justWon(playerValue, xCord, yCord)) {
			//reset the board because someone won
			this.minorReset();
			this.justWon = true;
			return winReward;
		}
		//else
		//check number of moves left
		int tally = 0;
		for (int i = 0; i < 9; i++)
			if (board[i / 3][i % 3] == i + 1)
				tally++;
		//if no moves left (tie), reset
		if (tally == 0) {
			this.minorReset();
			this.justTied = true;
		}
		return neutralMoveReward;
	}
	
	/**
	 * Return true if the player specified won
	 * @param player either x or y constants
	 * @param xCord the coordinate player just placed a piece on
	 * @param yCord the coordinate player just placed a piece on
	 * @return true if player won last turn
	 */
	private boolean justWon(int player, int xCord, int yCord) {
		boolean rowMatch = true;
		boolean colMatch = true;
		boolean downRightMatch = true;
		boolean upRightMatch = true;
		for (int i = 0; i < 3; i++) {
			rowMatch = rowMatch && (board[yCord][i] == player);
			colMatch = colMatch && (board[i][xCord] == player);
			downRightMatch = downRightMatch && (board[i][i] == player);
			upRightMatch = upRightMatch && (board[2 - i][i] == player);
		}
		return rowMatch || colMatch || downRightMatch || upRightMatch;
	}
	
	public boolean justWon() {
		return this.justWon;
	}
	
	public boolean justTied() {
		return this.justTied;
	}
	
	public boolean didNotMove() {
		return this.didNotMove;
	}

	/** resets for new game and resets statistics*/
	public void totalReset() {
		board = getNewBoard();
		turn = 2;
		game = 1;
		wins = 0;
		losses = 0;
		player = 1;
		gameEnd = false;
		justWon = false;
		justTied = false;
		didNotMove = false;
	}
	
	/** resets for a new game, but keeps statistics*/
	public void minorReset() {
		board = getNewBoard();
		turn = 2;
		game = 1;
		wins = 0;
		losses = 0;
		player = 1;
		gameEnd = false;
		justWon = false;
		justTied = false;
		didNotMove = false;
	}
	
	public void printBoard() {
		System.out.println("Game: " + game + ", turn: " + (turn / 2));
		for (int[] a : board)
			System.out.println(Arrays.toString(a));
		System.out.println();
	}
	
	private static int[][] getNewBoard() {
		return new int[][] {
				{1, 2, 3},
				{4, 5, 6},
				{7, 8, 9}};
	}
	
	public int[][] getBoard() {
		return this.board;
	}
}
