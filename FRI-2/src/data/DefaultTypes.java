package data;

import java.util.LinkedList;

public class DefaultTypes {

	public static final int numDefaultTypes = 3;
	public static final Type thingType = new Type((Type) null);
	public static final Type integerType = new Type(thingType);
	public static final Type booleanType = new Type(thingType);
//	public static final Type constIntObjType = new Type(thingType);
//	public static final Type constBoolObjType = new Type(thingType);
//	public static final Type constIntType = new Type(integerType);
//	public static final Type constBoolType = new Type(booleanType);
//	static Type idType = getTypeList(integerType);
	
//	public static final Type booleanFunctionType = new Type(booleanType);
//	public static final Type integerFunctionType = new Type(integerType);
	
	/**
	 * WARNING: These should be kept in order of superTypes first, subTypes last.
	 * Returns the list of all default types used. Used in [recursive] FunctionReader.getTypeMap().
	 * @return
	 */
	public static LinkedList<Type> getDefaultTypes() {
		LinkedList<Type> result = new LinkedList<Type>();
		result.add(thingType);
		result.add(integerType);
		result.add(booleanType);
		return result;
	}

}
