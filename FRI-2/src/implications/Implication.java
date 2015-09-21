package implications;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import functions.*;
import data.*;

public class Implication {
	
	private static final boolean DEBUG = true;

	// the list of functions, each of which outputs one part of the returned GameObject
	private List<Function> functions;
	// contains the list of inputTypes for each function
	private List<List<Type>> inputTypesLists;
	// contains # of Values needed for each internal Function
	private List<Integer> numInputsList;
	// contains the list of types in the output object
	private List<Type> outputTypes;
	// the FcnObjects that are input into the function
	private List<FcnObject> inputObjectModels;
	// type of this Implication; used for the returned GameObject
	private Type returnObjectType;
	// list of the Types of the Values in the returned GameObject
	private LinkedList<Type> dataTypes;
//	private Boolean output;
	
	public Implication(List<Function> functions, List<FcnObject> inputObjectModels, Type superType, LinkedList<Type> dataTypes) {
		this.functions = functions;
		this.inputTypesLists = new LinkedList<List<Type>>();
		this.outputTypes = new LinkedList<Type>();
		this.inputObjectModels = inputObjectModels;
		this.numInputsList = new LinkedList<Integer>();
		this.dataTypes = dataTypes;
		for (Function f : functions) {
			List<Type> inputTypes = new LinkedList<Type>();
			f.fillInputTypeList(inputTypes);
			this.inputTypesLists.add(inputTypes);
			this.numInputsList.add(inputTypes.size());
			this.outputTypes.add(f.getOutputType());
		}
//		output = false;
		if (DEBUG) {
			int neededInputs = 0;
			int totalInputs = 0;
			for (List<Type> inputL : this.inputTypesLists) {
				for (Type t : inputL) {
					neededInputs++;
				}
			}
			for (FcnObject fObj : this.inputObjectModels) {
				totalInputs++;
				assert (fObj != null) : "Implication: InputFcnObject null";
				//TODO: make sure fObj.valueIndex is within bounds and correct type
			}
			assert (neededInputs == totalInputs) : "Implication: input quantity mismatch";
		}
		this.returnObjectType = new Type(superType);
	}
	
	public void fillWithResults(List<GameObject> outputObjects, List<GameObject> givenObjects) {
		fillResultsRecursive(outputObjects, givenObjects, new LinkedList<Value>(), new LinkedList<FcnObject>());
	}
	
	private void fillResultsRecursive(List<GameObject> outputObjects, List<GameObject> givenObjects,
			List<Value> fcnInputValues, List<FcnObject> usedModels) {
		// base case
		if (inputObjectModels.isEmpty())
			addResult(fcnInputValues, outputObjects);
		else {
			// add value from previously used object if object already used
			Iterator<FcnObject> fObjIt = usedModels.iterator();
			Iterator<Value> inputValIt = fcnInputValues.iterator();
			FcnObject nextUsedFcnObj;
			Value nextUsedInputVal;
			for (int i = 0; i < usedModels.size(); i++) {
				nextUsedFcnObj = fObjIt.next();
				nextUsedInputVal = inputValIt.next();
				if (inputObjectModels.get(0).isSameObject(nextUsedFcnObj)) {
					fcnInputValues.add(nextUsedInputVal.getParentObject().getObjectData().get(inputObjectModels.get(0).getValueIndex()));
					usedModels.add(inputObjectModels.remove(0));
					// recursive call
					fillResultsRecursive(outputObjects, givenObjects, fcnInputValues, usedModels);
					// add stuff back after recursive call and then return
					fcnInputValues.remove(fcnInputValues.size() - 1);
					inputObjectModels.add(0, usedModels.remove(usedModels.size() - 1));
					return;
				}
			}
			// else search through givenObjects
			// true if there was a matching object
			boolean worked = false;
			Iterator<GameObject> gameObjIt = givenObjects.iterator();
			GameObject nextGameObj;
			for (int i = 0; i < givenObjects.size(); i++) {
				nextGameObj = gameObjIt.next();
				if (inputObjectModels.get(0).matchesObject(nextGameObj)) {
					worked = true;
					gameObjIt.remove();
					fcnInputValues.add(nextGameObj.getObjectData().get(inputObjectModels.get(0).getValueIndex()));
					usedModels.add(inputObjectModels.remove(0));
					// recursive call
					fillResultsRecursive(outputObjects, givenObjects, fcnInputValues, usedModels);
					// add stuff back after recursive call and continue where left off
					inputObjectModels.add(0, usedModels.remove(usedModels.size() - 1));
					givenObjects.add(i, fcnInputValues.remove(fcnInputValues.size() - 1).getParentObject());
				}
			}
			// if no matching object found, send in null for default operation
			if (!worked) {
				fcnInputValues.add(null);
				usedModels.add(inputObjectModels.remove(0));
				// recursive call
				fillResultsRecursive(outputObjects, givenObjects, fcnInputValues, usedModels);
				// add stuff back after recursive call and return
				fcnInputValues.remove(fcnInputValues.size() - 1);
				inputObjectModels.add(0, usedModels.remove(usedModels.size() - 1));
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addResult(List<Value> fcnInputValues, List<GameObject> outputObjects) {
		List<List<Value>> inputValuesLists = new LinkedList<List<Value>>();
		List<Value> currentInputsList = new LinkedList<Value>();
		int i = 0;
		Iterator<Integer> numInputsIt = this.numInputsList.iterator();
		int numInputs = numInputsIt.next();
		for (Value v : fcnInputValues) {
			while (i >= numInputs) {
				i = 0;
				inputValuesLists.add(currentInputsList);
				currentInputsList = new LinkedList<Value>();
				numInputs = numInputsIt.next();
			}
			currentInputsList.add(v);
		}
		inputValuesLists.add(currentInputsList);
		// stores the values needed by the GameObject to make Values
		LinkedList objectDataRaw = new LinkedList();
		Iterator<List<Value>> inputValuesListsIt = inputValuesLists.iterator();
		Iterator<Function> functionIt = this.functions.iterator();
		List<Value> currentInputs;
		Function currentFunction;
		// false if any of the functions returned null
		boolean stillValid = true;
		while (functionIt.hasNext() && stillValid) {
			currentInputs = inputValuesListsIt.next();
			currentFunction = functionIt.next();
			Object result = currentFunction.compute(currentInputs);
			objectDataRaw.add(result);
			stillValid = result != null;
		}
		if (stillValid) {
			GameObject result = new GameObject(returnObjectType, objectDataRaw, dataTypes);
			outputObjects.add(result);
		}
	}
	
}
