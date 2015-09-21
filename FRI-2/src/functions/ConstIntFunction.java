package functions;

import java.util.LinkedList;
import java.util.List;

import data.DefaultTypes;
import data.Type;
import data.Value;

public class ConstIntFunction extends Function {
	
	private static List<Type> inputs = defaultInputs();
	
	//the constant to be returned by this
	private Integer constant;

	public ConstIntFunction(LinkedList<Function> inputFunctions, Value constValue) {
		super(inputs, DefaultTypes.integerType, inputFunctions, false);
		if (this.DEBUG && !constValue.getType().isSub(DefaultTypes.integerType))
			throw new IllegalArgumentException("ConstIntFunction: constValue of invalid type");
		this.constant = (Integer) constValue.getValue();
	}

	public ConstIntFunction(LinkedList<Function> inputFunctions, Integer constant) {
		super(inputs, DefaultTypes.integerType, inputFunctions, false);
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
	public Integer f(List functionInputs) {
		return constant;
	}

	@Override
	public ConstIntFunction copy() {
		return new ConstIntFunction(this.inputFunctions, this.constant);
	}

	@Override
	public Type getBaseType() {
		return DefaultTypes.integerType;
	}

	@Override
	protected String functionName() {
		return "" + constant;
	}

}
