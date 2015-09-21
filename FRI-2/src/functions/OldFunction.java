package functions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import data.Type;
import data.Value;

/*
 * Input is the type of Object that is input into the Function
 * Output is the type of Object that is returned from the Function
 * type is the Type object which input values must be a subtype of (or equal Type)
 */
public abstract class OldFunction<Input, Output> {
	
	public final boolean DEBUG = true;

	// type of object which input values must be a subtype of (or equal Type)
	protected Type inputType;
	protected Type outputType;
	protected Output defaultOutput;
	// Includes a Function in each element where a lower function should be used for input,
	// and null where a given Input is to be used.
	// Should be length given by numInputs
	protected LinkedList<OldFunction<?, Input>> inputFunctions;
	
	/**
	 * 
	 * @param inputType
	 * @param inputFunctions list of Functions where inputs should be functions,
	 * and null where inputs will be passed to compute()
	 */
	public OldFunction(Type inputType, Type outputType, LinkedList<OldFunction<?, Input>> inputFunctions, Output defaultOutput) {
		if (DEBUG) {
			assert inputType != null : "Function(): type == null";
			assert inputFunctions != null && inputFunctions.size() == numInputs() :
				"Function(): invalid inputFunctions";
		}
		this.defaultOutput = defaultOutput;
		this.inputType = inputType;
		this.outputType = outputType;
		this.inputFunctions = inputFunctions;
	}

	public OldFunction(Type inputType, Type outputType, LinkedList<OldFunction<?, Input>> inputFunctions) {
		this(inputType, outputType, inputFunctions, null);
	}
	
	/**
	 * returns the Output value returned by this Function tree
	 * @param valueInputs a List of the Values which contain the inputs for this Function tree.
	 * Note that it should contain multiple types iff lower functions in the tree are of a different Input type.
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final Output compute(List<Value> valueInputs) {
		if (DEBUG)
			checkAssertions(valueInputs);
		List<Input> functionInputs = new LinkedList<Input>();
		int i = 0;
		Iterator<Value> givenInputs = valueInputs.iterator();
		// assert inputFunctions has numInputs elements.
		for (OldFunction<?, Input> f : inputFunctions) {
			if (f != null)
				functionInputs.add(f.compute(valueInputs.subList(i, valueInputs.size())));
			else
				// assert givenInputs.next().getValue() is of type Input
				// this is because that Value should have supertype type, which has InputType Input
				functionInputs.add((Input) givenInputs.next().getValue());
		}
		for (Input input : functionInputs)
			if (input == null)
				return defaultOutput;
		return f(functionInputs);
	}
	
	public abstract int numInputs();
	
	public abstract Output f(List<Input> functionInputs);

	/**
	 * Method to get the list of Types required to run this entire function
	 * @param result
	 */
	public final void fillInputTypeList(List<Type> result) {
		for (OldFunction<?, Input> f : inputFunctions) {
			if (f != null)
				f.fillInputTypeList(result);
			else
				result.add(inputType);
		}
	}
	
	public final LinkedList<OldFunction<?, Input>> getInputFunctions() {
		return inputFunctions;
	}
	
	/**
	 * Assuming inputFunctions is correct, checks to make sure the given input list complements the function tree
	 * @param valueInputs
	 */
	@SuppressWarnings("rawtypes")
	public final void checkAssertions(List<Value> valueInputs) {
		List<Type> expectedTypes = new LinkedList<Type>();
		fillInputTypeList(expectedTypes);
		if (valueInputs.size() != expectedTypes.size())
			throw new IllegalStateException("Function.checkAssertions: valueInputs wrong size");
		Iterator<Value> valuesIt = valueInputs.iterator();
		for (Type t : expectedTypes) {
			if (!valuesIt.next().getType().isSub(t))
				throw new IllegalStateException("Function.checkAssertions: Type mismatch");
		}
	}
	
	/**
	 * Returns a deepcopy of the current Function, without deepcopying its fields
	 * @return a new Function of this type which shares the same parameters
	 */
	public abstract OldFunction<Input, Output> copy();
	
	public Type getInputType() {
		return inputType;
	}
	
	public Type getOutputType() {
		return outputType;
	}
	
}
