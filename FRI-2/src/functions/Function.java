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
public abstract class Function {
	
	//pseudo-static variables
	public final boolean DEBUG = true;
	
	protected int valNumber = 1;

	// type of object which input values must be a subtype of (or equal Type)
	protected List<Type> inputTypes;
	protected Type outputType;
	protected Object defaultOutput;
	// Includes a Function in each element where a lower function should be used for input,
	// and null where a given Input is to be used.
	// Should be length given by numInputs
	protected LinkedList<Function> inputFunctions;
	
	/**
	 * 
	 * @param inputTypes list of Types that are inputed into this Function
	 * @param outputType the Type that is output by this Function
	 * @param inputFunctions list of Functions where inputs should be functions,
	 * and null where inputs will be passed to compute()
	 * @param defaultOutput the value to be output by this Function if an input is null (meaning no match found)
	 */
	public Function(List<Type> inputTypes, Type outputType, LinkedList<Function> inputFunctions, Object defaultOutput) {
		if (DEBUG) {
			if (inputTypes == null)
				throw new IllegalArgumentException("Function(): inputTypes == null");
			for (Type t : inputTypes)
				if (t == null)
					throw new IllegalArgumentException("Function(): an inputType is null");
			if (inputFunctions == null || inputFunctions.size() != baseNumInputs())
				throw new IllegalArgumentException("Function(): invalid inputFunctions");
		}
		this.defaultOutput = defaultOutput;
		this.inputTypes = inputTypes;
		this.outputType = outputType;
		this.inputFunctions = inputFunctions;
	}

	public Function(List<Type> inputTypes, Type outputType, LinkedList<Function> inputFunctions) {
		this(inputTypes, outputType, inputFunctions, null);
	}
	
	/**
	 * returns the Output value returned by this Function tree
	 * @param valueInputs a List of the Values which contain the inputs for this Function tree.
	 * Note that it should contain multiple types iff lower functions in the tree are of a different Input type.
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final Object compute(List<Value> valueInputs) {
		if (DEBUG)
			checkAssertions(valueInputs);
		List functionInputs = new LinkedList();
		int i = 0;
		Iterator<Value> givenInputs = valueInputs.iterator();
		// assert inputFunctions has numInputs elements.
		for (Function f : inputFunctions) {
			if (f != null) {
				int lowerInputs = f.getTotalInputs();
				functionInputs.add(f.compute(valueInputs.subList(i, i + lowerInputs)));
				i += lowerInputs;
				for (; lowerInputs > 0; lowerInputs--)
					givenInputs.next();
			} else {
				//TODO: assert givenInputs.next().getValue() is of type Input
				// this is because that Value should have supertype type, which has InputType Input
				functionInputs.add(givenInputs.next().getValue());
				i++;
			}
		}
		if (returnDefault(functionInputs))
			return defaultOutput;
		return f(functionInputs);
	}

	/**
	 * The number of inputs needed to make this method work if there are no subFunctions
	 * @return the minimum number of inputs needed to make this type of Function work
	 */
	public abstract int baseNumInputs();
	
	public boolean returnDefault(List<Value> functionInputs) {
		for (Object input : functionInputs)
			if (input == null)
				return true;
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	public abstract Object f(List functionInputs);

	/**
	 * Method to get the list of Types required to run this entire function
	 * @param result
	 */
	public final void fillInputTypeList(List<Type> result) {
		Iterator<Type> inputTypeIt = inputTypes.iterator();
		for (Function f : inputFunctions) {
			if (f != null) {
				f.fillInputTypeList(result);
				inputTypeIt.next();
			} else
				result.add(inputTypeIt.next());
		}
	}
	
	/**
	 * Recursive method to get the total # of inputs needed for this Function
	 * @return the number of inputs required to make this exact Function work
	 */
	public final int getTotalInputs() {
		int result = 0;
		for (Function f : inputFunctions) {
			if (f != null) {
				result += f.getTotalInputs();
			} else
				result++;
		}
		return result;
	}
	
	/**
	 * Returns true if there are no inputs to this function, meaning the return value is constant
	 * @return true if there are no inputs to this function, meaning the return value is constant
	 */
	public final boolean isConstant() {
		return getTotalInputs() == 0;
	}
	
	public final LinkedList<Function> getInputFunctions() {
		return inputFunctions;
	}
	
	/**
	 * Assuming inputFunctions is correct, checks to make sure the given input list complements the function tree
	 * @param valueInputs
	 */
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
	public abstract Function copy();
	
	public List<Type> getInputTypes() {
		return inputTypes;
	}
	
	public Type getOutputType() {
		return outputType;
	}
	
	public abstract Type getBaseType();
	
	/**
	 * Format:
	 * Function: [functionName] ( [input] , [input] , ... , [input] , )
	 * Variable: x [type tag] [number of this type; actually variable number here but fixed in SpecificFunction]
	 * Constant Function: [value] ( )
	 */
	public final String toString() {
		String result = recursiveToString(this.inputTypes.iterator());
		valNumber = 1;
		return result;
	}
	
	protected final String recursiveToString(Iterator<Type> inputTypesIt) {
		String result = functionName() + " ( ";
		for (Function f : inputFunctions) {
			//base case
			if (f == null)
				result += "x " + inputTypesIt.next().getTypeTag() + ' ' + valNumber++ + ' ';
			else
				result += f.recursiveToString(inputTypesIt) + ' ';
			result += ", ";
		}
		result += ')';
		return result;
	}
	
	protected abstract String functionName();
	
}
