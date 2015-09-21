package data;

public class FcnValue {

	// type of GameObject this FcnValue stands for
	private Type valType;
	// TODO: not used; should be used to distinguish some FcnValues from one another
	private int id;
	
	FcnValue(Type objType, int id) {
		this.valType = objType;
		this.id = id;
	}
	
	/**
	 * Used to see if a Value can be represented by this FcnValue
	 * @param val the Value to compare to this FcnValue
	 * @return
	 */
	public boolean matchesObject(Value val) {
		return this.valType.equals(val.getType());
	}
	
	public Type getType() {
		return valType;
	}
	
	public int getID() {
		return id;
	}
}
