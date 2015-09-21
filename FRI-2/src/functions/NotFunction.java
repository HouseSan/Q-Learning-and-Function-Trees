package functions;

import java.util.LinkedList;
import java.util.List;

import data.DefaultTypes;
import data.Type;

public class NotFunction extends Function {

	private static List<Type> inputs = defaultInputs();

	public NotFunction(LinkedList<Function> inputFunctions) {
		super(inputs, DefaultTypes.booleanType, inputFunctions);
	}

	private static List<Type> defaultInputs() {
		List<Type> inputs = new LinkedList<Type>();
		inputs.add(DefaultTypes.booleanType);
		return inputs;
	}

	@Override
	public int baseNumInputs() {
		return 1;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Boolean f(List functionInputs) {
		return !(Boolean) functionInputs.get(0);
	}

	@Override
	public NotFunction copy() {
		return new NotFunction(this.inputFunctions);
	}

	@Override
	public Type getBaseType() {
		return DefaultTypes.booleanType;
	}

	@Override
	protected String functionName() {
		return "Not";
	}

}
