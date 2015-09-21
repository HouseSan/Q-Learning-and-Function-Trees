package ai;

import java.util.ArrayList;
import java.util.List;

import functions.*;

/**
 * Q-Learning algorithm.
 */
//TODO: implement true Q-Learning w/ back-propagation.
public class QLearner {
	
	public static final boolean DEBUG = true;
	//what the q values are initialized to; runs slower if this is high but more explorative
	public static final double initialQValue = 10000.0;
	//the factor that future rewards are multiplied by before back propagation.
	//closer to 1 means the agent will be willing to wait for rewards more.
	public static final double discountFactor = 0.9;
	public static double learningRate = 0.4;

	//NOTE: numStates is the number of state bools, NOT including keyInput bools.
	//This includes constant inputs, which are listed as null in this.states.
	private int numStates;
	//NOTE: numStateBools is the number of states with the same input bools; different state bools.
	//Equal to 2^(numStates - number of boolean constants)
	private int numStateCombos;
	//NOTE: numInputs is the number of keyInput bools, NOT including state bools.
	private int numKeys;
	//NOTE: numInputCombos is the number of states with the same state bools; different input bools.
	//Equal to 2^(numInputs)
	private int numInputCombos;
	//NOTE: states.size() == numStateBools * numInputCombos
	//list of states, given by boolean values.  All true or false or null (if should be ignored because it's constant)
	private ArrayList<ArrayList<Boolean>> states;
	//list of Q-values for each state.
	private ArrayList<Double> values;
	//index of the last state that was used for keyInput
	private Integer stateActionLastUsed;
//	private boolean qValuesUpdated;
	//used for indexing quickly
	private ArrayList<Boolean> usefulStates;
	
	public QLearner(ArrayList<Function> boolFunctions, int numKeyInputs) {
		this.stateActionLastUsed = null;
//		this.qValuesUpdated = true;
		this.numKeys = numKeyInputs;
		int numCombos = 1;
		for (int i = numKeys; i > 0; i--)
			numCombos *= 2;
		this.numInputCombos = numCombos;
		this.numStates = boolFunctions.size();
		this.states = new ArrayList<ArrayList<Boolean>>();
		this.values = new ArrayList<Double>();
		this.numStateCombos = 1;
		ArrayList<Boolean> state = new ArrayList<Boolean>();
		for (Function f : boolFunctions) {
			if (f.isConstant()) {
				state.add(null);
			} else {
				state.add((Boolean) true);
				numStateCombos *= 2;
			}
		}
		this.usefulStates = new ArrayList<Boolean>();
		for (Boolean b : state)
			usefulStates.add(b);
		for (int i = numKeyInputs; i > 0; i--)
			state.add(true);
		recursiveStateSetup(state, 0, this.numKeys, this.states, this.values);
	}
	
	public QLearner(boolean[] boolFunctions, int numKeyInputs) {
		this.stateActionLastUsed = null;
//		this.qValuesUpdated = true;
		this.numKeys = numKeyInputs;
		int numCombos = 1;
		for (int i = numKeys; i > 0; i--)
			numCombos *= 2;
		this.numInputCombos = numCombos;
		this.numStates = boolFunctions.length;
		this.states = new ArrayList<ArrayList<Boolean>>();
		this.values = new ArrayList<Double>();
		this.numStateCombos = 1;
		ArrayList<Boolean> state = new ArrayList<Boolean>();
		for (Boolean b : boolFunctions) {
			state.add(true);
			numStateCombos *= 2;
		}
		this.usefulStates = new ArrayList<Boolean>();
		for (Boolean b : state)
			usefulStates.add(b);
		for (int i = numKeyInputs; i > 0; i--)
			state.add(true);
		recursiveStateSetup(state, 0, this.numKeys, this.states, this.values);
	}

	/**
	 * Returns the optimal list of button presses given the current state.
	 * Also sets stateLastUsed.
	 * @param stateFcnValues list of Boolean values to represent the current gamestate.
	 * Must be of same size as this QLearner's lists.
	 * @param lastFitness the fitness given to the last choice made by the AI. Ignored if no choice made yet.
	 * @return the optimal list of button presses given the current state
	 */
	public List<Boolean> getMoves(ArrayList<Boolean> stateFcnValues, Double lastFitness) {
		if (DEBUG && stateFcnValues.size() != this.numStates)
			throw new IllegalArgumentException("QLearner getMoves: stateFcnValues of wrong size");
//		if (!qValuesUpdated)
//			throw new IllegalStateException("QLearner getMoves: qValues not updated; call updateQValues before using");
//		boolean matchEnded = true;
//		int firstOptionIndex = -this.numInputCombos;
		/*
		do {
			firstOptionIndex += this.numInputCombos;
			ArrayList<Boolean> stateRecord = states.get(firstOptionIndex);
			matchEnded = true;
			for (int bIndex = 0; bIndex < stateFcnValues.size() && matchEnded; bIndex++) {
				Boolean boolRecord = stateRecord.get(bIndex);
				matchEnded = (boolRecord == null || boolRecord.equals(stateFcnValues.get(bIndex)));
			}
			//break loop if a match found
		} while (!matchEnded);
		*/
		//get index of first state-action pair that matches the current state
		int firstOptionIndex = 0;
		int length = stateFcnValues.size();
		for (int i = 0; i < length; i++)
			if (this.usefulStates.get(i) != null) {
				firstOptionIndex *= 2;
				if (!stateFcnValues.get(i))
					firstOptionIndex++;
			}
		firstOptionIndex *= this.numInputCombos;
		/*
		if (DEBUG) {
			boolean matchEnded = true;
			ArrayList<Boolean> stateRecord = states.get(firstOptionIndex);
			for (int bIndex = 0; bIndex < stateFcnValues.size() && matchEnded; bIndex++) {
				Boolean boolRecord = stateRecord.get(bIndex);
				matchEnded = (boolRecord == null || boolRecord.equals(stateFcnValues.get(bIndex)));
			}
			if (!matchEnded)
				throw new IllegalStateException("QLearner getMoves(): incorrect index found.\nFunction:\n" + stateFcnValues + "\nState:\n" + stateRecord);
		}
		*/
		if (this.stateActionLastUsed != null)
			updateQValues(lastFitness, firstOptionIndex);
//		int stopByThisIndex = bestOptionIndex + this.numStateCombos;
//		Double bestOptionValue = values.get(bestOptionIndex);
		int bestOption = firstOptionIndex;
		double bestValue = Double.NEGATIVE_INFINITY;
		//update this.bestOption to be the index of the best option
		for (int i = firstOptionIndex + this.numInputCombos - 1; i >= firstOptionIndex; i--) {
			double currentValue = values.get(i);
			if (currentValue > bestValue) {
				bestOption = i;
				bestValue = currentValue;
			}
		}
		//best option found
		this.stateActionLastUsed = bestOption;
		ArrayList<Boolean> bestState = states.get(bestOption);
		// get the key inputs that the AI will output
		List<Boolean> keyPresses = bestState.subList(this.numStates, bestState.size());
		return keyPresses;
	}
	
	/**
	 * Updates the appropriate Q Value, as well as the tallies
	 * @param lastFitness the fitness given to the last move.
	 * @param stateIndex the index of the first state-action pair with the correct state
	 */
	private void updateQValues(Double lastFitness, int stateIndex) {
//		if (qValuesUpdated)
//			throw new IllegalStateException("QLearner updateQValues: qValues updated; call getMoves before using");
		//calculate new Q value
		double oldValue = values.get(this.stateActionLastUsed);
		//calculate the maximum estimated q value for the next turn
		double maxQ = Double.NEGATIVE_INFINITY;
		for (int i = stateIndex + this.numInputCombos - 1; i >= stateIndex; i--) {
			double currentQ = values.get(i);
			if (currentQ > maxQ)
				maxQ = currentQ;
		}
		double newQ = oldValue + learningRate * (lastFitness - oldValue + discountFactor * maxQ);
		//update the correct state value
		values.set(this.stateActionLastUsed, newQ);
	}
	
	public ArrayList<ArrayList<Boolean>> getStateActionList() {
		return states;
	}
	
	public Double getHighestQValue() {
		Double highest = Double.NEGATIVE_INFINITY;
		for (Double d : this.values)
			if (d > highest)
				highest = d;
		return highest;
	}
	
	/**
	 * Fills this.states and this.values with entries based on given state,
	 * @param state the list of Boolean values, all true or null,
	 * which define which of the state parameters should be considered.
	 * Null indicates the value does not influence the state.
	 * NOTE: ordered such that states with everything the same
	 * except keyInput values are in a row, for later optimization
	 */
	private static void recursiveStateSetup(ArrayList<Boolean> state, int index, int numInputs,
			ArrayList<ArrayList<Boolean>> states, ArrayList<Double> values) {
		//base case
		if (index == state.size()) {
			recursiveStateFinisher(state, numInputs, states, values);
		} else {
			recursiveStateSetup(state, index + 1, numInputs, states, values);
			Boolean b = state.get(index);
			if (b != null) {
				state.set(index, false);
				recursiveStateSetup(state, index + 1, numInputs, states, values);
				state.set(index, true);
			}
		}
	}
	
	/**
	 * Helper for recursiveStateSetup. Adds in the keyInput booleans and actually adds the state lists and value lists.
	 * @param state the state to be added. When first called will not include keyInput values.
	 * @param numKeyInputs the number of keyInputs allowed to the AI.
	 */
	private static void recursiveStateFinisher(ArrayList<Boolean> state, int numKeyInputs,
			ArrayList<ArrayList<Boolean>> states, ArrayList<Double> values) {
		//base case
//		if (numKeyInputs == 0) {
			ArrayList<Boolean> copy = new ArrayList<Boolean>(state.size());
			for (Boolean b : state)
				copy.add(b);
			states.add(copy);
			values.add((Double) initialQValue);
		/*
		} else {
			state.add(true);
			recursiveStateFinisher(state, numKeyInputs - 1, states, values);
			state.set(state.size() - 1, false);
			recursiveStateFinisher(state, numKeyInputs - 1, states, values);
			state.remove(state.size() - 1);
		}
		*/
	}
	
}
