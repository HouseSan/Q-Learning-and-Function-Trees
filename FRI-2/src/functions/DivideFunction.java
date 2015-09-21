package functions;

import java.util.LinkedList;
import java.util.List;

import data.DefaultTypes;
import data.Type;

public class DivideFunction extends Function {
	
	private static List<Type> inputs = defaultInputs();

	public DivideFunction(LinkedList<Function> inputFunctions) {
		super(inputs, DefaultTypes.integerType, inputFunctions);
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
	public Integer f(List functionInputs) {
		return ((Integer) functionInputs.get(0)) / (Integer) functionInputs.get(1);
	}

	@Override
	public DivideFunction copy() {
		return new DivideFunction(this.inputFunctions);
	}

	@Override
	public Type getBaseType() {
		return DefaultTypes.integerType;
	}

	@Override
	protected String functionName() {
		return "Divide";
	}

}
