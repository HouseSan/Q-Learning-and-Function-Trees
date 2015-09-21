package functions;

import java.util.LinkedList;
import java.util.List;

import data.DefaultTypes;
import data.Type;

public class LessThanFunction extends Function {

	private static List<Type> inputs = defaultInputs();

	public LessThanFunction(LinkedList<Function> inputFunctions) {
		super(inputs, DefaultTypes.booleanType, inputFunctions);
	}

	private static List<Type> defaultInputs() {
		List<Type> inputs = new LinkedList<Type>();
		inputs.add(DefaultTypes.integerType);
		inputs.add(DefaultTypes.integerType);
		return inputs;
	}

	@Override
	public int baseNumInputs() {
		return 2;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Boolean f(List functionInputs) {
		return ((Integer) functionInputs.get(0)) < (Integer) (functionInputs.get(1));
	}

	@Override
	public LessThanFunction copy() {
		return new LessThanFunction(this.inputFunctions);
	}

	@Override
	public Type getBaseType() {
		return DefaultTypes.booleanType;
	}

	@Override
	protected String functionName() {
		return "LessThan";
	}

}
