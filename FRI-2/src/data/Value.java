package data;

//import java.util.HashMap;
//import java.util.Map;

// a class for values, which are ints
public class Value {
	// type of this Value; descriptive to the AI
	private Type type;
	// value stored in this Value
	private Object value;
	// the object that this Value belongs to
	private GameObject parentObject;
	
	public Value(Type type, Object value, GameObject parentObject) {
		this.type = type;
		this.value = value;
		this.parentObject = parentObject;
	}
	
	public Type getType() {
		return type;
	}
	
	public Object getValue() {
		return value;
	}
	
	public GameObject getParentObject() {
		return parentObject;
	}
	
	/**
	 * True if this Value shares the same parent GameObject as other
	 * @param other the Value to check its parent against this; must not be null
	 * @return true if this Value has the same parent GameObject as other
	 */
	public boolean SharesParent(Value other) {
		return this.parentObject == other.parentObject;
	}
}
