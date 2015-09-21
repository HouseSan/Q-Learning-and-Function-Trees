package tests;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import ticTacToe.TicTacToe_Console_AI;
import data.*;
import functions.*;
import ai.QLearner;

public class Test {

	public static Random rand = new Random();
	public static Scanner scan = new Scanner(System.in);
	
	public static int numRepetitions = 80000000;
	
	public static void main(String[] args) {
//		ticTacToeCoevolutionTest();
		ticTacToeCoevolutionTest();
/*
		Type coordinate = new Type(DefaultTypes.integerType);
		Type xCoord = new Type(coordinate);
		Type yCoord = new Type(coordinate);
		
		
		Function twoInThisRow;
*/		
	}
	
	public static void qLearnerStateTest() {
		boolean[] states = {false, false, false};
		QLearner agent = new QLearner(states, 3);
		ArrayList<ArrayList<Boolean>> stateActionList = agent.getStateActionList();
		System.out.println("Test");
	}
	
	public static void ticTacToeCoevolutionTest() {
		TicTacToe_Console_AI game = new TicTacToe_Console_AI();
		boolean[] inputs = new boolean[15];
		QLearner agent = new QLearner(inputs, 4);
		QLearner agent2 = new QLearner(inputs, 4);
		Double lastFitness = null;
		Double lastFitness2 = null;
		boolean moveFirst = true;
		boolean myMove = moveFirst;
		double score = 0;
		int wins = 0;
		int losses = 0;
		int ties = 0;
		int scoreWeight = 100;
		int tally = 0;
		int wrapArounds = 0;
		boolean newGame = true;
		while (tally < numRepetitions) {
			if (myMove) {
				ArrayList<Boolean> state = getCompressedState(game.getBoard());
//				ArrayList<Boolean> state = getCompressedState2(game.getBoard(), myMove);
				int move = boolsToInt(agent.getMoves(state, lastFitness));
				game.doMove(move);
				tally++;
				if (game.didNotMove()) {
					lastFitness = TicTacToe_Console_AI.didNotMoveReward;
					//update score and tallies
					score = ((score * scoreWeight) + lastFitness) / ++scoreWeight;
					if (scoreWeight % 1000000 == 0) {
						System.out.println("Turns: " + tally + " Score: " + score);
						int total = wins + losses + ties;
//						System.out.println("Wins: " + (wins / (double) total) + " Losses: " + (losses / (double) total) + " Ties: " + (ties / (double) total));
						System.out.println("Wins: " + wins + " Losses: " + losses + " Ties: " + ties);
						scoreWeight = 100;
						wins = losses = ties = 0;
					}
				}
			} else {
				ArrayList<Boolean> state = getCompressedState(game.getBoard());
//				ArrayList<Boolean> state = getCompressedState2(game.getBoard(), !myMove);
				int move = boolsToInt(agent2.getMoves(state, lastFitness2));
				game.doMove(move);
				if (game.didNotMove())
					lastFitness2 = TicTacToe_Console_AI.didNotMoveReward;
			}
			//if game's over, switch first players
			if (game.justTied() || game.justWon()) {
				if (game.justWon()) {
					//if the opponent won, update reward
					if (!myMove) {
						lastFitness = TicTacToe_Console_AI.didNotMoveReward;
						lastFitness2 = TicTacToe_Console_AI.winReward;
						losses++;
					}
					//if player won, update reward
					else {
						lastFitness = TicTacToe_Console_AI.winReward;
						lastFitness2 = TicTacToe_Console_AI.didNotMoveReward;
						wins++;
					}
						
				} else {
					//tied
					lastFitness2 = lastFitness = TicTacToe_Console_AI.tieReward;
					ties++;
				}
				//update score and tallies
				score = ((score * scoreWeight) + lastFitness) / ++scoreWeight;
				if (scoreWeight % 1000000 == 0) {
					System.out.println("Turns: " + tally + " Score: " + score);
					int total = wins + losses + ties;
//					System.out.println("Wins: " + (wins / (double) total) + " Losses: " + (losses / (double) total) + " Ties: " + (ties / (double) total));
					System.out.println("Wins: " + wins + " Losses: " + losses + " Ties: " + ties);
					scoreWeight = 100;
					wins = losses = ties = 0;
				}
				//mark that a new game will start
				newGame = true;				
				moveFirst = !moveFirst;
				myMove = moveFirst;
			//if the game didn't end. And someone actually moved.
			} else if (!game.didNotMove()) {
				//TODO: update CompressedTest to look like this [make sure !game.didNotMove()].
				//only update fitness if not a new game
				if (!newGame) {
					//if game didn't end and opponent moved
					if (!myMove) {
						lastFitness = TicTacToe_Console_AI.neutralMoveReward;
						//update score and tallies
						/*
						score = ((score * scoreWeight) + lastFitness) / ++scoreWeight;
						tally++;
						if (tally % 1000000 == 0) {
							System.out.println("Turns: " + tally + " Score: " + score);
	//						System.out.println("Highest Q-Value: " + agent.getHighestQValue());
							scoreWeight = 100;
						}
						*/
					} else {
						//if game didn't end and player moved AND not a new game
						lastFitness2 = TicTacToe_Console_AI.neutralMoveReward;
					}
				//otherwise if it was a newGame, mark it as no longer a new game.
				} else
					newGame = false;
				//if a move was made, switch the player
				myMove = !myMove;
			}
			if (tally == -1)
				wrapArounds++;
		}
		System.out.println("Success! Score: " + score + ". Turns: " + tally);
		
		//play with the created agent when done
		while (true) {
			if (myMove) {
				ArrayList<Boolean> state = getCompressedState(game.getBoard());
//				ArrayList<Boolean> state = getCompressedState2(game.getBoard(), myMove);
				int move = boolsToInt(agent.getMoves(state, lastFitness));
				game.doMove(move);
				if (game.didNotMove()) {
					lastFitness = TicTacToe_Console_AI.didNotMoveReward;
				}
			} else {
				boolean worked = false;
				while (!worked) {
					worked = true;
					game.printBoard();
					System.out.println("Your move? " + "(" + !myMove + ")");
					try {
						String answer = scan.nextLine();
						int move = Integer.parseInt(answer);
						game.doMove(move);
					} catch(Exception e) {
						worked = false;
					}
				}
			}
			//if game's over, switch first players
			if (game.justTied() || game.justWon()) {
				if (game.justWon()) {
					//if the opponent won, update reward
					if (!myMove) {
						System.out.println("You won!");
						lastFitness = TicTacToe_Console_AI.didNotMoveReward;
					}
					//if player won, update reward
					else {
						System.out.println("You lost.");
						lastFitness = TicTacToe_Console_AI.winReward;
					}
						
				} else {
					System.out.println("Tie game.");
					lastFitness = TicTacToe_Console_AI.tieReward;
				}
				scan.nextLine();
				newGame = true;
				moveFirst = !moveFirst;
				myMove = moveFirst;
			} else if (!game.didNotMove()) {
				if (!newGame) {
					if (!myMove) {
						lastFitness = TicTacToe_Console_AI.neutralMoveReward;
					}
				} else
					newGame = false;
				//otherwise if a move was made, switch the player
				myMove = !myMove;
			}
		}
	}

	/**
	 * Squeezes the board into a state of 15 booleans instead of 18
	 * @param board the 3x3 gameboard from TicTacToe_Console_AI
	 * @return 15 boolean list representation of the board
	 */
	private static ArrayList<Boolean> getCompressedState2(int[][] board, boolean myMove) {
		if (myMove) {
			//get integer value for state
			int value = 0;
			for (int i = 0; i < 9; i++) {
				value *= 3;
				int piece = board[i / 3][i % 3];
				if (piece == i + 1)
					value += 1;
				if (piece == TicTacToe_Console_AI.x)
					value += 2;
				else value += 0;
			}
			//decode into boolean
			ArrayList<Boolean> state = new ArrayList<Boolean>();
			for (int i = 0; i < 15; i++) {
				int s = value % 2;
				value /= 2;
				state.add(s == 0);
			}
			return state;
		} else {
			//get integer value for state
			int value = 0;
			for (int i = 0; i < 9; i++) {
				value *= 3;
				int piece = board[i / 3][i % 3];
				if (piece == i + 1)
					value += 1;
				if (piece == TicTacToe_Console_AI.x)
					value += 0;
				else value += 2;
			}
			//decode into boolean
			ArrayList<Boolean> state = new ArrayList<Boolean>();
			for (int i = 0; i < 15; i++) {
				int s = value % 2;
				value /= 2;
				state.add(s == 0);
			}
			return state;
		}
	}


	/**
	 * Squeezes the board into a state of 15 booleans instead of 18
	 * @param board the 3x3 gameboard from TicTacToe_Console_AI
	 * @return 15 boolean list representation of the board
	 */
	private static ArrayList<Boolean> getCompressedState(int[][] board) {
		//get integer value for state
		int value = 0;
		for (int i = 0; i < 9; i++) {
			value *= 3;
			int piece = board[i / 3][i % 3];
			if (piece == i + 1)
				value += 1;
			if (piece == TicTacToe_Console_AI.x)
				value += 2;
			//else += 0
		}
		//decode into boolean
		ArrayList<Boolean> state = new ArrayList<Boolean>();
		for (int i = 0; i < 15; i++) {
			int s = value % 2;
			value /= 2;
			state.add(s == 0);
		}
		return state;
	}
	
	public static void ticTacToeCompressedTest() {
		TicTacToe_Console_AI game = new TicTacToe_Console_AI();
		boolean[] inputs = new boolean[15];
		QLearner agent = new QLearner(inputs, 4);
		Double lastFitness = null;
		boolean moveFirst = true;
		boolean myMove = moveFirst;
		double score = 0;
		int wins = 0;
		int losses = 0;
		int ties = 0;
		int scoreWeight = 100;
		int tally = 0;
		int wrapArounds = 0;
		boolean newGame = true;
		while (tally < numRepetitions) {
			if (myMove) {
				ArrayList<Boolean> state = getCompressedState(game.getBoard());
//				ArrayList<Boolean> state = getCompressedState2(game.getBoard(), myMove);
				int move = boolsToInt(agent.getMoves(state, lastFitness));
				game.doMove(move);
				tally++;
				if (game.didNotMove()) {
					lastFitness = TicTacToe_Console_AI.didNotMoveReward;
					//update score and tallies
					score = ((score * scoreWeight) + lastFitness) / ++scoreWeight;
					if (scoreWeight % 1000000 == 0) {
						System.out.println("Turns: " + tally + " Score: " + score);
						int total = wins + losses + ties;
//						System.out.println("Wins: " + (wins / (double) total) + " Losses: " + (losses / (double) total) + " Ties: " + (ties / (double) total));
						System.out.println("Wins: " + wins + " Losses: " + losses + " Ties: " + ties);
						scoreWeight = 100;
						wins = losses = ties = 0;
					}
				}
			} else {
				game.doRandomMove();
			}
			//if game's over, switch first players
			if (game.justTied() || game.justWon()) {
				if (game.justWon()) {
					//if the opponent won, update reward
					if (!myMove) {
						lastFitness = TicTacToe_Console_AI.didNotMoveReward;
						losses++;
					}
					//if player won, update reward
					else {
						lastFitness = TicTacToe_Console_AI.winReward;
						wins++;
					}
						
				} else {
					//tied
					lastFitness = TicTacToe_Console_AI.tieReward;
					ties++;
				}
				//update score and tallies
				score = ((score * scoreWeight) + lastFitness) / ++scoreWeight;
				if (scoreWeight % 1000000 == 0) {
					System.out.println("Turns: " + tally + " Score: " + score);
					int total = wins + losses + ties;
//					System.out.println("Wins: " + (wins / (double) total) + " Losses: " + (losses / (double) total) + " Ties: " + (ties / (double) total));
					System.out.println("Wins: " + wins + " Losses: " + losses + " Ties: " + ties);
					scoreWeight = 100;
					wins = losses = ties = 0;
				}
				//mark that a new game will start
				newGame = true;				
				moveFirst = !moveFirst;
				myMove = moveFirst;
			//if the game didn't end. And someone actually moved.
			} else if (!game.didNotMove()) {
				//TODO: update CompressedTest to look like this [make sure !game.didNotMove()].
				//only update fitness if not a new game
				if (!newGame) {
					//if game didn't end and opponent moved
					if (!myMove)
						lastFitness = TicTacToe_Console_AI.neutralMoveReward;
				//otherwise if it was a newGame, mark it as no longer a new game.
				} else
					newGame = false;
				//if a move was made, switch the player
				myMove = !myMove;
			}
			if (tally == -1)
				wrapArounds++;
		}
		System.out.println("Success! Score: " + score + ". Turns: " + tally);
		
		//play with the created agent when done
		while (true) {
			if (myMove) {
				ArrayList<Boolean> state = getCompressedState(game.getBoard());
//				ArrayList<Boolean> state = getCompressedState2(game.getBoard(), myMove);
				int move = boolsToInt(agent.getMoves(state, lastFitness));
				game.doMove(move);
				if (game.didNotMove()) {
					lastFitness = TicTacToe_Console_AI.didNotMoveReward;
				}
			} else {
				boolean worked = false;
				while (!worked) {
					worked = true;
					game.printBoard();
					System.out.println("Your move? " + "(" + !myMove + ")");
					try {
						String answer = scan.nextLine();
						int move = Integer.parseInt(answer);
						game.doMove(move);
					} catch(Exception e) {
						worked = false;
					}
				}
			}
			//if game's over, switch first players
			if (game.justTied() || game.justWon()) {
				if (game.justWon()) {
					//if the opponent won, update reward
					if (!myMove) {
						System.out.println("You won!");
						lastFitness = TicTacToe_Console_AI.didNotMoveReward;
					}
					//if player won, update reward
					else {
						System.out.println("You lost.");
						lastFitness = TicTacToe_Console_AI.winReward;
					}
						
				} else {
					System.out.println("Tie game.");
					lastFitness = TicTacToe_Console_AI.tieReward;
				}
				scan.nextLine();
				newGame = true;
				moveFirst = !moveFirst;
				myMove = moveFirst;
			} else if (!game.didNotMove()) {
				if (!newGame) {
					if (!myMove) {
						lastFitness = TicTacToe_Console_AI.neutralMoveReward;
					}
				} else
					newGame = false;
				//otherwise if a move was made, switch the player
				myMove = !myMove;
			}
		}
	}
	
	public static void ticTacToeTest() {
		TicTacToe_Console_AI game = new TicTacToe_Console_AI();
		boolean[] inputs = new boolean[18];
		QLearner agent = new QLearner(inputs, 4);
		Double lastFitness = null;
		boolean moveFirst = true;
		boolean myMove = true;
		double score = 0;
		int scoreWeight = 100;
		int tally = 0;
		while (tally < 1000000) {
			if (myMove) {
				ArrayList<Boolean> state = getState(game.getBoard());
				int move = boolsToInt(agent.getMoves(state, lastFitness));
				game.doMove(move);
				if (game.didNotMove()) {
					lastFitness = TicTacToe_Console_AI.didNotMoveReward;
					//update score and tallies
					score = ((score * scoreWeight) + lastFitness) / ++scoreWeight;
					tally++;
					if (tally % 1000 == 0) {
						System.out.println("Turns: " + tally + " Score: " + score);
						scoreWeight = 100;
					}
				}
			} else {
				game.doRandomMove();
			}
			//if game's over, switch first players
			if (game.justTied() || game.justWon()) {
				if (game.justWon()) {
					//if the opponent won, update reward
					if (!myMove)
						lastFitness = -TicTacToe_Console_AI.winReward;
					//if player won, update reward
					else
						lastFitness = TicTacToe_Console_AI.winReward;
						
				} else
					lastFitness = TicTacToe_Console_AI.neutralMoveReward;
				//update score and tallies
				score = ((score * scoreWeight) + lastFitness) / ++scoreWeight;
				tally++;
				if (tally % 1000 == 0) {
					System.out.println("Turns: " + tally + " Score: " + score);
					scoreWeight = 100;
				}
				
				moveFirst = !moveFirst;
				myMove = moveFirst;
			} else {
				//if opponent didn't win and player didn't do anything wrong
				if (!myMove) {
					lastFitness = TicTacToe_Console_AI.neutralMoveReward;
					//update score and tallies
					/*
					score = ((score * scoreWeight) + lastFitness) / ++scoreWeight;
					tally++;
					if (tally % 1000 == 0) {
						System.out.println("Turns: " + tally + " Score: " + score);
						scoreWeight = 100;
					}
					*/
				}
				//otherwise if a move was made, switch the player
				if (!game.didNotMove())
					myMove = !myMove;
			}
		}
		System.out.println("Success! Score: " + score + ". Turns: " + tally);
		
		//play with the created agent
		while (true) {
			if (myMove) {
				ArrayList<Boolean> state = getState(game.getBoard());
				int move = boolsToInt(agent.getMoves(state, lastFitness));
				game.doMove(move);
				if (game.didNotMove()) {
					lastFitness = TicTacToe_Console_AI.didNotMoveReward;
				}
			} else {
				game.printBoard();
				System.out.println("Your move? " + "(" + !myMove + ")");
				game.doMove(scan.nextInt());
			}
			//if game's over, switch first players
			if (game.justTied() || game.justWon()) {
				if (game.justWon()) {
					//if the opponent won, update reward
					if (!myMove) {
						System.out.println("You won!");
						lastFitness = -TicTacToe_Console_AI.winReward;
					}
					//if player won, update reward
					else {
						System.out.println("You lost.");
						lastFitness = TicTacToe_Console_AI.winReward;
					}
						
				} else {
					System.out.println("Tie game.");
					lastFitness = TicTacToe_Console_AI.neutralMoveReward;
				}
				scan.nextLine();
				moveFirst = !moveFirst;
				myMove = moveFirst;
			} else {
				if (!myMove) {
					lastFitness = TicTacToe_Console_AI.neutralMoveReward;
				}
				//otherwise if a move was made, switch the player
				if (!game.didNotMove())
					myMove = !myMove;
			}
		}
	}
	
	public static void ticTacToeHumanTest() {
		TicTacToe_Console_AI game = new TicTacToe_Console_AI();
		boolean[] inputs = new boolean[18];
		QLearner agent = new QLearner(inputs, 4);
		Double lastFitness = null;
		boolean moveFirst = true;
		boolean myMove = true;
		double score = 0;
		int scoreWeight = 100;
		int tally = 0;
		while (score < 50.) {
			if (myMove) {
				ArrayList<Boolean> state = getState(game.getBoard());
				game.printBoard();
				System.out.println("Your move?");
				int move = scan.nextInt();
				game.doMove(move);
				if (game.didNotMove()) {
					lastFitness = TicTacToe_Console_AI.didNotMoveReward;
					//update score and tallies
					score = ((score * scoreWeight) + lastFitness) / ++scoreWeight;
					tally++;
					System.out.println("Turns: " + tally + " Score: " + score);
					scoreWeight = 100;
				}
			} else {
				game.doRandomMove();
			}
			//if game's over, switch first players
			if (game.justTied() || game.justWon()) {
				if (game.justWon()) {
					//if the opponent won, update reward
					if (!myMove)
						lastFitness = -TicTacToe_Console_AI.winReward;
					//if player won, update reward
					else
						lastFitness = TicTacToe_Console_AI.winReward;
						
				} else
					lastFitness = TicTacToe_Console_AI.neutralMoveReward;
				//update score and tallies
				score = ((score * scoreWeight) + lastFitness) / ++scoreWeight;
				tally++;
				System.out.println("Turns: " + tally + " Score: " + score);
				scoreWeight = 100;
				
				moveFirst = !moveFirst;
				myMove = moveFirst;
			} else {
				if (!myMove) {
					lastFitness = TicTacToe_Console_AI.neutralMoveReward;
					//update score and tallies
					/*
					score = ((score * scoreWeight) + lastFitness) / ++scoreWeight;
					tally++;
					System.out.println("Turns: " + tally + " Score: " + score);
					scoreWeight = 100;
					*/
				}
				//otherwise if a move was made, switch the player
				if (!game.didNotMove())
					myMove = !myMove;
			}
		}
		System.out.println("Success! Score: " + score + ". Turns: " + tally);
	}
	
	private static ArrayList<Boolean> getState(int[][] board) {
		ArrayList<Boolean> state = new ArrayList<Boolean>(18);
		for (int i = 0; i < 9; i++) {
			int x = i % 3;
			int y = i / 3;
			boolean b1 = (board[y][x] == i + 1);
			boolean b2 = (board[y][x] == TicTacToe_Console_AI.x);
			boolean b3 = (board[y][x] == TicTacToe_Console_AI.y);
			boolean input1;
			boolean input2;
			if (b1) {
				input1 = true;
				input2 = true;
			} else if (b2) {
				input1 = true;
				input2 = false;
			} else {
				// (b3 == true)
				input1 = false;
				input2 = true;
			}
			state.add(input1);
			state.add(input2);
		}
		return state;
	}
	
	private static int boolsToInt(List<Boolean> bList) {
		int value = 0;
		for (Boolean b : bList) {
			value *= 2;
			if (b)
				value++;
		}
		return value;
	}
	
	public static void qLearnerTest() {
		
		LinkedList<Function> sumInputFunctions = new LinkedList<Function>();
		sumInputFunctions.add(null);
		sumInputFunctions.add(null);
		Function addF = new AddFunction(sumInputFunctions);
		
		Type sumObjectType = new Type(DefaultTypes.integerType);
		LinkedList<Object> sumObjectData = new LinkedList<Object>();
		sumObjectData.add((Integer) 7);
		sumObjectData.add((Integer) 0);
		LinkedList<Type> sumObjectDataTypes = new LinkedList<Type>();
		sumObjectDataTypes.add(DefaultTypes.integerType);
		sumObjectDataTypes.add(DefaultTypes.integerType);
		
		GameObject sumObject = new GameObject(sumObjectType, sumObjectData, sumObjectDataTypes);
		Value sevenValue;
		sevenValue = sumObject.getObjectData().getFirst();

		LinkedList<Function> sevenInputFunctions = new LinkedList<Function>();
		Function sevenF = new ConstIntFunction(sevenInputFunctions, sevenValue);
		
		LinkedList<Function> greaterInputFunctions = new LinkedList<Function>();
		greaterInputFunctions.add(addF);
		greaterInputFunctions.add(sevenF);
		Function greaterF = new GreaterThanFunction(greaterInputFunctions);
		Type greaterObjectType = new Type(DefaultTypes.booleanType);
		LinkedList<Type> greaterObjectDataTypes = new LinkedList<Type>();
		greaterObjectDataTypes.add(DefaultTypes.integerType);
		ArrayList<Function> qLearnerInputs = new ArrayList<Function>();
		qLearnerInputs.add(greaterF);
		
		QLearner qLearner = new QLearner(qLearnerInputs, 1);
		LinkedList<Object> greaterObjectData = new LinkedList<Object>();
		ArrayList<Boolean> gameState = new ArrayList<Boolean>(1);
		LinkedList<Value> inputs;
		List<Boolean> keyPresses;
		
		int numTrials = 0;
		int weightTrials = 100;
		double avgScore = 0.0;
		Double lastFitness = null;
		while (avgScore < 0.70) {
			
			greaterObjectData.clear();
			int r1 = rand.nextInt(10) + 1;
			int r2 = 0;
			greaterObjectData.add((Integer) r1);
			greaterObjectData.add((Integer) r2);
			greaterObjectDataTypes.clear();
			greaterObjectDataTypes.add(DefaultTypes.integerType);
			greaterObjectDataTypes.add(DefaultTypes.integerType);
			
			GameObject greaterObject = new GameObject(greaterObjectType, greaterObjectData, greaterObjectDataTypes);
			inputs = greaterObject.getObjectData();
			gameState.clear();
			gameState.add(false);
//			for (Value v : greaterObject.getObjectData())
//				inputs.add(v);
			Boolean result = (Boolean) greaterF.compute(inputs);
			if (result != null)
				gameState.set(0, result);
			Boolean resultReal = r1 + r2 > 7;
			if (!result.equals(resultReal))
				throw new IllegalStateException("GreaterF broken. " + r1 + " + " + r2 + " > 7: " + result + ". Expected: " + resultReal);
//			System.out.println("" + result + ": ");
			keyPresses = qLearner.getMoves(gameState, lastFitness);
			Boolean expected = r1 + r2 > 6;
			Boolean response = keyPresses.get(0);
			if (expected.equals(response))
				lastFitness = 1.0;
			else
				lastFitness = -1.0;
			avgScore = (avgScore * weightTrials + lastFitness) / (weightTrials + 1);
			numTrials++;
			weightTrials++;
			if (weightTrials % 1000 == 0) {
				weightTrials = 100;
				System.out.println("avgScore: " + avgScore + " Response: " + response);
			}
		}
		System.out.println("numTrials:" + numTrials + " avgScore: " + avgScore);
		System.out.println("greaterF: ");
		String greaterFString = greaterF.toString();
		System.out.println(greaterFString);
		Scanner functionStringScanner = new Scanner(greaterFString);
		Function newGreaterF = FunctionReader.interpretString(functionStringScanner);
		String newGreaterFString = newGreaterF.toString();
		System.out.println("new greaterF: ");
		System.out.println(newGreaterFString);
		LinkedList<VariableMarker> greaterFMarkers = new LinkedList<VariableMarker>();
		Type redIntegerType = new Type(DefaultTypes.integerType);
		greaterFMarkers.add(new VariableMarker(redIntegerType, 1));
		greaterFMarkers.add(new VariableMarker(redIntegerType, 2));
		SpecificFunction specificGreaterF = new SpecificFunction(greaterF, greaterFMarkers);
		String specificFString = specificGreaterF.toString();
		System.out.println("specificGreaterF: ");
		System.out.println(specificFString);
		functionStringScanner = new Scanner(specificFString);
		LinkedList<Type> allTypes = DefaultTypes.getDefaultTypes();
		allTypes.add(redIntegerType);
		Map<Integer, Type> typeMap = FunctionReader.getTypeMap(allTypes);
		System.out.println("new specificGreaterF: ");
		System.out.println(FunctionReader.stringToSpecificFunction(functionStringScanner, typeMap));
		String typesString = FunctionReader.typeListToString(allTypes);
		System.out.println("types list: ");
		System.out.println(typesString);
		functionStringScanner = new Scanner(typesString);
		List<Type> newTypes = new LinkedList<Type>();
		Map<Integer, Type> newTypeMap = FunctionReader.getTypeMap(functionStringScanner);
		for (Type t : newTypeMap.values())
			newTypes.add(t);
		String newTypesString = FunctionReader.typeListToString(newTypes);
		System.out.println("new types list: ");
		System.out.println(newTypesString);
		functionStringScanner = new Scanner(specificFString);
		SpecificFunction newSpecificGreaterF = FunctionReader.stringToSpecificFunction(functionStringScanner, newTypeMap);
		System.out.println("new new specificGreaterF: ");
		System.out.println(newSpecificGreaterF.toString());
	}
	
	public static void sumTest() {
		LinkedList<Function> sumInputFunctions = new LinkedList<Function>();
		sumInputFunctions.add(null);
		sumInputFunctions.add(null);
		Function addF = new AddFunction(sumInputFunctions);
		Type sumObjectType = new Type(DefaultTypes.integerType);
		LinkedList<Object> sumObjectData = new LinkedList<Object>();
		sumObjectData.add((Integer) 2);
		sumObjectData.add((Integer) 7);
		LinkedList<Type> sumObjectDataTypes = new LinkedList<Type>();
		sumObjectDataTypes.add(DefaultTypes.integerType);
		sumObjectDataTypes.add(DefaultTypes.integerType);
		
		GameObject sumObject = new GameObject(sumObjectType, sumObjectData, sumObjectDataTypes);
		Integer result = (Integer) addF.compute(sumObject.getObjectData());
		System.out.println("add 2 + 7 result: " + result);
	}
	
	public static void equalTest() {
		LinkedList<Function> equalInputFunctions = new LinkedList<Function>();
		equalInputFunctions.add(null);
		equalInputFunctions.add(null);
		Function equalF = new EqualFunction(equalInputFunctions);
		Type equalObjectType = new Type(DefaultTypes.booleanType);
		LinkedList<Object> equalObjectData = new LinkedList<Object>();
		equalObjectData.add((Integer) 7);
		equalObjectData.add((Integer) 7);
		LinkedList<Type> equalObjectDataTypes = new LinkedList<Type>();
		equalObjectDataTypes.add(DefaultTypes.integerType);
		equalObjectDataTypes.add(DefaultTypes.integerType);
		
		GameObject equalObject = new GameObject(equalObjectType, equalObjectData, equalObjectDataTypes);
		Boolean result = (Boolean) equalF.compute(equalObject.getObjectData());
		System.out.println("7 == 7 result: " + result);
	}
	
	public static void existsTest() {
		LinkedList<Function> sumInputFunctions = new LinkedList<Function>();
		sumInputFunctions.add(null);
		Function addF = new ExistsFunction(sumInputFunctions);
		Type sumObjectType = new Type((Type) null);
		LinkedList<Object> sumObjectData = new LinkedList<Object>();
		sumObjectData.add((Integer) null);
		LinkedList<Type> sumObjectDataTypes = new LinkedList<Type>();
		sumObjectDataTypes.add(DefaultTypes.integerType);
		
		GameObject sumObject = new GameObject(sumObjectType, sumObjectData, sumObjectDataTypes);
		Boolean result = (Boolean) addF.compute(sumObject.getObjectData());
		System.out.println("null exists result: " + result);
	}
	
	public static void fcnChainTest() {

		LinkedList<Function> sumInputFunctions = new LinkedList<Function>();
		sumInputFunctions.add(null);
		sumInputFunctions.add(null);
		Function addF = new AddFunction(sumInputFunctions);
		Type sumObjectType = new Type(DefaultTypes.integerType);
		LinkedList<Object> sumObjectData = new LinkedList<Object>();
		sumObjectData.add((Integer) 2);
		sumObjectData.add((Integer) 5);
		LinkedList<Type> sumObjectDataTypes = new LinkedList<Type>();
		sumObjectDataTypes.add(DefaultTypes.integerType);
		sumObjectDataTypes.add(DefaultTypes.integerType);
		
		GameObject sumObject = new GameObject(sumObjectType, sumObjectData, sumObjectDataTypes);
		
		LinkedList<Function> equalInputFunctions = new LinkedList<Function>();
		equalInputFunctions.add(addF);
		equalInputFunctions.add(null);
		Function equalF = new EqualFunction(equalInputFunctions);
		Type equalObjectType = new Type(DefaultTypes.booleanType);
		LinkedList<Object> equalObjectData = new LinkedList<Object>();
		equalObjectData.add((Integer) 7);
		LinkedList<Type> equalObjectDataTypes = new LinkedList<Type>();
		equalObjectDataTypes.add(DefaultTypes.integerType);
		
		GameObject equalObject = new GameObject(equalObjectType, equalObjectData, equalObjectDataTypes);
		LinkedList<Value> inputs = sumObject.getObjectData();
		for (Value v : equalObject.getObjectData())
			inputs.add(v);
		Boolean result = (Boolean) equalF.compute(inputs);
		System.out.println("(2 + 5) == 7 result: " + result);
	}
	
	public static void ifElseTest() {
		LinkedList<Function> equalInputFunctions = new LinkedList<Function>();
		equalInputFunctions.add(null);
		equalInputFunctions.add(null);
		equalInputFunctions.add(null);
		Function equalF = new IfElseFunction(equalInputFunctions);
		Type equalObjectType = new Type(DefaultTypes.integerType);
		LinkedList<Object> equalObjectData = new LinkedList<Object>();
		equalObjectData.add((Boolean) false);
		equalObjectData.add((Integer) 2);
		equalObjectData.add((Integer) 7);
		LinkedList<Type> equalObjectDataTypes = new LinkedList<Type>();
		equalObjectDataTypes.add(DefaultTypes.booleanType);
		equalObjectDataTypes.add(DefaultTypes.integerType);
		equalObjectDataTypes.add(DefaultTypes.integerType);
		
		GameObject equalObject = new GameObject(equalObjectType, equalObjectData, equalObjectDataTypes);
		Integer result = (Integer) equalF.compute(equalObject.getObjectData());
		System.out.println("true ? 2 : 7 result: " + result);
	}
}
