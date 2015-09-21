package ai;

import java.util.ArrayList;
import java.util.List;

import functions.*;

/**
 * Q-Learning algorithm.
 */
//TODO: implement true Q-Learning w/ back-propagation.
public class QLearnerOld {
	
	public static final boolean DEBUG = true;
	public static final int recursiveLoops = 3;
	public static double learningRate = 0.2;
	public static double rememberingRate = 1.0 - learningRate;

	//NOTE: numStates is the number of state bools, NOT including keyInput bools.
	//This includes constant inputs, which are listed as null in this.states.
	private int numStates;
	//NOTE: numStateBools is the number of states with the same input bools; different state bools.
	//Equal to 2^(numStates - number of boolean constants)
	private int numStateCombos;
	//NOTE: numInputs is the number of keyInput bools, NOT including state bools.
	private int numInputs;
	//NOTE: numInputCombos is the number of states with the same state bools; different input bools.
	//Equal to 2^(numInputs)
	private int numInputCombos;
	//NOTE: states.size() == numStateBools * numInputCombos
	//list of states, given by boolean values.  All true or false or null (if should be ignored because it's constant)
	private ArrayList<ArrayList<Boolean>> states;
	//list of Q-values for each state.
	private ArrayList<Double> values;
	//holds a list of tally lists. Tallies store how many times that gamestate led to another set of gamestates.
	private ArrayList<ArrayList<Integer>> nextStateTallies;
	//index of the last state that was used for keyInput
	private Integer stateLastUsed;
//	private boolean qValuesUpdated;
	//just used as a global variable in recursiveQValue() and read in getMoves().
	private int bestOption;
	
	public QLearnerOld(ArrayList<Function> boolFunctions, int numKeyInputs) {
		this.stateLastUsed = null;
//		this.qValuesUpdated = true;
		this.numInputs = numKeyInputs;
		int numCombos = 1;
		for (int i = numInputs; i > 0; i--)
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
		recursiveStateSetup(state, 0, this.numInputs, this.states, this.values);
		talliesSetup();
	}
	
	private void talliesSetup() {
		nextStateTallies = new ArrayList<ArrayList<Integer>>();
		int stopIndex = states.size();
		for (int i = 0; i < stopIndex; i++) {
			ArrayList<Integer> tallies = new ArrayList<Integer>();
			for (int k = this.numStateCombos; k > 0; k--) {
				tallies.add(new Integer(0));
			}
			nextStateTallies.add(tallies);
		}
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
		boolean matchEnded = true;
		int firstOptionIndex = -this.numInputCombos;
		int nextTallyIndex = -1;
		do {
			firstOptionIndex += this.numInputCombos;
			nextTallyIndex++;
			ArrayList<Boolean> stateRecord = states.get(firstOptionIndex);
			matchEnded = true;
			for (int bIndex = 0; bIndex < stateFcnValues.size() && matchEnded; bIndex++) {
				Boolean boolRecord = stateRecord.get(bIndex);
				matchEnded = (boolRecord == null || boolRecord.equals(stateFcnValues.get(bIndex)));
			}
			//break loop if a match found
		} while (!matchEnded);
		if (this.stateLastUsed != null)
			updateQValues(lastFitness, nextTallyIndex);
//		int stopByThisIndex = bestOptionIndex + this.numStateCombos;
//		Double bestOptionValue = values.get(bestOptionIndex);
		this.bestOption = 0;
		//update this.bestOption to be the index of the best option
		recursiveQValue(firstOptionIndex, QLearnerOld.recursiveLoops);
/*		Double bestOptionValue = recursiveQValue(bestOptionIndex, this.recursiveLoops);
		int i = bestOptionIndex + 1;
		for (; i < stopByThisIndex; i++) {
//			Double newOption = values.get(i);
			Double newOption = recursiveQValue(i, this.recursiveLoops);
			if (newOption > bestOptionValue) {
				bestOptionIndex = i;
				bestOptionValue = newOption;
			}
		}
		//best option found
*/
//		this.stateLastUsed = bestOptionIndex;
		this.stateLastUsed = this.bestOption;
//		qValuesUpdated = false;
		ArrayList<Boolean> bestState = states.get(this.bestOption);
		// get the key inputs that the AI will output
		List<Boolean> keyPresses = bestState.subList(this.numStates, bestState.size());
		return keyPresses;
	}
	
	/**
	 * Updates the appropriate Q Value, as well as the tallies
	 * @param lastFitness the fitness given to the last move.
	 * @param nextTallyIndex the 
	 */
	private void updateQValues(Double lastFitness, int nextTallyIndex) {
//		if (qValuesUpdated)
//			throw new IllegalStateException("QLearner updateQValues: qValues updated; call getMoves before using");
		//increment the correct tally
		ArrayList<Integer> tallies = nextStateTallies.get(this.stateLastUsed);
		tallies.set(nextTallyIndex, tallies.get(nextTallyIndex) + 1);
		//update the correct state value
		values.set(stateLastUsed, values.get(stateLastUsed) * rememberingRate + lastFitness * learningRate);
//		values.set(stateLastUsed, recursiveQValue(stateLastUsed, QLearner.recursiveLoops));
//		qValuesUpdated = true;
	}
	

/*
	private Double recursiveQValue(int tallyIndex, int valueIndex, int recursiveLoopsLeft) {
		//base case
		Double total = values.get(valueIndex);
		if (recursiveLoopsLeft != 0) {
			ArrayList<Integer> tallies = this.nextStateTallies.get(valueIndex);
			int totalTallies = 0;
			for (Integer tally : tallies)
				totalTallies += tally;
			Double max = Double.NEGATIVE_INFINITY;
			for (int i = tallies.size() - 1; i >= 0; i--) {
				Integer tally = tallies.get(i);
				if (!tally.equals(0)) {
					Double  recursiveQValue(, recursiveLoopsLeft - 1) * tally / totalTallies;
				}
			}
			total += max;
		}
		return total;
	}
*/

	/**
	 * Returns the highest average value for the branch, and updates this.bestOption to reflect this.
	 * this.bestOption will be the index of the state in this.states which contains the desired action.
	 * @param tallyIndex the index of the desired 
	 * @param valueIndex the index of the desired nextStateTallies element and Value element.
	 * @param recursiveLoopsLeft the number of times this function should recursively call itself
	 * @return the biggest weighted Q-Value for this option branch
	 */
	private Double recursiveQValue(int valueIndex, int recursiveLoopsLeft) {
		Double maxReward = Double.NEGATIVE_INFINITY;
		int bestIndex = -1;
		for (int option = valueIndex + this.numInputCombos - 1; option >= valueIndex; option--) {
			Double currentReward = values.get(option);
			//base case recursiveLoopsLeft == 0
			if (recursiveLoopsLeft != 0) {
				ArrayList<Integer> tallies = this.nextStateTallies.get(option);
				int totalTallies = 0;
				for (Integer tally : tallies)
					totalTallies += tally;
				if (totalTallies != 0) {
					for (int i = tallies.size() - 1; i >= 0; i--) {
						Integer tally = tallies.get(i);
						if (!tally.equals(0)) {
							//recursive call
							currentReward += recursiveQValue(i * this.numInputCombos, recursiveLoopsLeft - 1) * tally / totalTallies;
						}
					}
				} else {
					//in case this state/input combination was never explored, assume best future results possible.
					//NOTE: assumes 1.0 is higher than the highest fitness possible.
					//won't break otherwise, but might not explore new options
					//if an option that consistently returns higher than this is found.
					currentReward += (double) recursiveLoopsLeft;
				}
			}
			if (currentReward > maxReward) {
				bestIndex = option;
				maxReward = currentReward;
			}
		}
		this.bestOption = bestIndex;
		return maxReward;
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
			}
		}
	}
	
	/**
	 * Helper for recursiveStateSetup. Adds in the keyInput booleans and actually adds the state lists.
	 * @param state the state to be added. When first called will not include keyInput values.
	 * @param numKeyInputs the number of keyInputs allowed to the AI.
	 */
	private static void recursiveStateFinisher(ArrayList<Boolean> state, int numKeyInputs,
			ArrayList<ArrayList<Boolean>> states, ArrayList<Double> values) {
		//base case
		if (numKeyInputs == 0) {
			ArrayList<Boolean> copy = new ArrayList<Boolean>();
			for (Boolean b : state)
				copy.add(b);
			states.add(copy);
			values.add((Double) 1.0);
		} else {
			state.add(true);
			recursiveStateFinisher(state, numKeyInputs - 1, states, values);
			state.set(state.size() - 1, false);
			recursiveStateFinisher(state, numKeyInputs - 1, states, values);
			state.remove(state.size() - 1);
		}
	}
	
}
