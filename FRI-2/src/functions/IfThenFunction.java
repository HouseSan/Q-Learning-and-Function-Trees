package functions;

import java.util.LinkedList;
import java.util.List;

import data.DefaultTypes;
import data.Type;

public class IfThenFunction extends Function {

	private static List<Type> inputs = defaultInputs();

	public IfThenFunction(LinkedList<Function> inputFunctions) {
		super(inputs, DefaultTypes.booleanType, inputFunctions);
	}

	private static List<Type> defaultInputs() {
		List<Type> inputs = new LinkedList<Type>();
		inputs.add(DefaultTypes.booleanType);
		inputs.add(DefaultTypes.integerType);
		return inputs;
	}

	@Override
	public int baseNumInputs() {
		return 2;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer f(List functionInputs) {
		return ((Boolean) functionInputs.get(0)) ? (Integer) functionInputs.get(1) : (Integer) null;
	}

	@Override
	public IfThenFunction copy() {
		return new IfThenFunction(this.inputFunctions);
	}

	@Override
	public Type getBaseType() {
		return DefaultTypes.integerType;
	}

	@Override
	protected String functionName() {
		return "IfThen";
	}

}
