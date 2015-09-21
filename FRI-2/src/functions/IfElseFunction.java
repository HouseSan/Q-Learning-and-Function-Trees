package functions;

import java.util.LinkedList;
import java.util.List;

import data.DefaultTypes;
import data.Type;
import data.Value;

public class IfElseFunction extends Function {

	private static List<Type> inputs = defaultInputs();

	public IfElseFunction(LinkedList<Function> inputFunctions) {
		super(inputs, DefaultTypes.booleanType, inputFunctions);
	}

	private static List<Type> defaultInputs() {
		List<Type> inputs = new LinkedList<Type>();
		inputs.add(DefaultTypes.booleanType);
		inputs.add(DefaultTypes.integerType);
		inputs.add(DefaultTypes.integerType);
		return inputs;
	}

	@Override
	public int baseNumInputs() {
		return 3;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer f(List functionInputs) {
		return ((Boolean) functionInputs.get(0)) ? (Integer) functionInputs.get(1) : (Integer) functionInputs.get(2);
	}

	@Override
	public IfElseFunction copy() {
		return new IfElseFunction(this.inputFunctions);
	}

	@Override
	public Type getBaseType() {
		return DefaultTypes.integerType;
	}
	
	@Override
	public boolean returnDefault(List<Value> functionInputs) {
			if (functionInputs.get(0) == null)
				return true;
		return false;
	}

	@Override
	protected String functionName() {
		return "IfElse";
	}

}
