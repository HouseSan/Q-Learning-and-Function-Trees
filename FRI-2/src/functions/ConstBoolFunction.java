package functions;

import java.util.LinkedList;
import java.util.List;

import data.DefaultTypes;
import data.Type;
import data.Value;

public class ConstBoolFunction extends Function {
	
	private static List<Type> inputs = defaultInputs();
	
	//the constant to be returned by this
	private Boolean constant;

	public ConstBoolFunction(LinkedList<Function> inputFunctions, Value constValue) {
		super(inputs, DefaultTypes.booleanType, inputFunctions, false);
		if (this.DEBUG && !constValue.getType().isSub(DefaultTypes.booleanType))
			throw new IllegalArgumentException("ConstBoolFunction: constValue of invalid type");
		this.constant = (Boolean) constValue.getValue();
	}

	public ConstBoolFunction(LinkedList<Function> inputFunctions, Boolean constant) {
		super(inputs, DefaultTypes.booleanType, inputFunctions, false);
		this.constant = constant;
	}

	private static List<Type> defaultInputs() {
		return new LinkedList<Type>();
	}

	@Override
	public int baseNumInputs() {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Boolean f(List functionInputs) {
		return constant;
	}

	@Override
	public ConstBoolFunction copy() {
		return new ConstBoolFunction(this.inputFunctions, this.constant);
	}

	@Override
	public Type getBaseType() {
		return DefaultTypes.booleanType;
	}

	@Override
	protected String functionName() {
		return "" + constant;
	}

}
