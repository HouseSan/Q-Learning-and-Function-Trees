package functions;

import java.util.LinkedList;
import java.util.List;

import data.DefaultTypes;
import data.Type;


public class AddFunction extends Function {

	private static List<Type> inputs = defaultInputs();

	public AddFunction(LinkedList<Function> inputFunctions) {
		super(inputs, data.DefaultTypes.integerType, inputFunctions);
	}

	@Override
	public int baseNumInputs() {
		return 2;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer f(List functionInputs) {
		return ((Integer) functionInputs.get(0)) +  (Integer) functionInputs.get(1);
	}

	@Override
	public AddFunction copy() {
		return new AddFunction(this.inputFunctions);
	}
	
	private static List<Type> defaultInputs() {
		List<Type> inputs = new LinkedList<Type>();
		inputs.add(data.DefaultTypes.integerType);
		inputs.add(data.DefaultTypes.integerType);
		return inputs;
	}

	@Override
	public Type getBaseType() {
		return DefaultTypes.integerType;
	}

	@Override
	protected String functionName() {
		return "Add";
	}
	

}
