package functions;

import java.util.LinkedList;
import java.util.List;

import data.DefaultTypes;
import data.Type;

public class OrFunction extends Function {

	private static List<Type> inputs = defaultInputs();

	public OrFunction(LinkedList<Function> inputFunctions) {
		super(inputs, DefaultTypes.booleanType, inputFunctions);
	}

	private static List<Type> defaultInputs() {
		List<Type> inputs = new LinkedList<Type>();
		inputs.add(DefaultTypes.booleanType);
		inputs.add(DefaultTypes.booleanType);
		return inputs;
	}

	@Override
	public int baseNumInputs() {
		return 2;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Boolean f(List functionInputs) {
		return ((Boolean) functionInputs.get(0)) || (Boolean) functionInputs.get(1);
	}

	@Override
	public OrFunction copy() {
		return new OrFunction(this.inputFunctions);
	}

	@Override
	public Type getBaseType() {
		return DefaultTypes.booleanType;
	}

	@Override
	protected String functionName() {
		return "Or";
	}

}
