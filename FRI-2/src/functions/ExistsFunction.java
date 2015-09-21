package functions;

import java.util.LinkedList;
import java.util.List;

import data.DefaultTypes;
import data.Type;

public class ExistsFunction extends Function {
	
	private static List<Type> inputs = defaultInputs();

	public ExistsFunction(LinkedList<Function> inputFunctions) {
		super(inputs, DefaultTypes.booleanType, inputFunctions, false);
	}

	private static List<Type> defaultInputs() {
		List<Type> inputs = new LinkedList<Type>();
		inputs.add(DefaultTypes.thingType);
		return inputs;
	}

	@Override
	public int baseNumInputs() {
		return 1;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Boolean f(List functionInputs) {
		return true;
	}

	@Override
	public ExistsFunction copy() {
		return new ExistsFunction(this.inputFunctions);
	}

	@Override
	public Type getBaseType() {
		return DefaultTypes.booleanType;
	}

	@Override
	protected String functionName() {
		return "Exists";
	}

}
