package data;

public class FcnObject {

	// type of GameObject this FcnObject stands for
	private Type objType;
	// TODO: not used; should be used to distinguish some FcnObjects from one another
//	private int id;
	// stores the index of the desired Value in the GameObject
	//TODO: CHANGE THIS TO TYPE OF INPUT FIELD, NOT INDEX OF INPUT FIELD
	private int valueIndex;
	
	FcnObject(Type objType, int valueIndex) {
		this.objType = objType;
//		this.id = id;
		this.valueIndex = valueIndex;
	}
	
	/**
	 * Used to see if a GameObject can be represented by this FcnObject
	 * @param object the GameObject to compare to this FcnObject
	 * @return
	 */
	public boolean matchesObject(GameObject object) {
		return this.objType.equals(object.getType());
	}
	
	public Type getType() {
		return objType;
	}
	
	public int getValueIndex() {
		return valueIndex;
	}
	
	public boolean isSameObject(FcnObject other) {
		return (this.objType == other.objType);
	}
	
/*	
	public int getID() {
		return id;
	}
*/
	
}
