package data;

import java.util.Iterator;
import java.util.LinkedList;

//Note:  A new GameObject should be made for every object each frame.
//Do not edit ones from previous frames.
public class GameObject {

	//TODO: use the idCounter?
//	private static int idCounter;
	
	// type of this GameObject
	private Type type;
	// where all of the data for this object type is stored
	private LinkedList<Value> objectData;

	/**
	 * Note: Deepcopy not made of objectData
	 * @param type the type of this GameObject
	 * @param objectData data which will be stored in this. Deepcopy not made.
	 */
	public GameObject(Type type, LinkedList<Value> objectData) {
		this.type = type;
		this.objectData = objectData;
	}

	public GameObject(Type type, LinkedList<Object> objectDataRaw, LinkedList<Type> dataTypes) {
		this.type = type;
		if (objectDataRaw.size() != dataTypes.size())
			throw new IllegalArgumentException("GameObject: inputObjects and inputTypes quantity mismatch");
		this.objectData = new LinkedList<Value>();
		Iterator<Object> dataRawIt = objectDataRaw.iterator();
		Iterator<Type> dataTypesIt = dataTypes.iterator();
		Object nextData;
		Type dataType;
		while (dataRawIt.hasNext()) {
			nextData = dataRawIt.next();
			dataType = dataTypesIt.next();
			this.objectData.add(new Value(dataType, nextData, this));
		}
	}
	
	public LinkedList<Value> getObjectData() {
		return objectData;
	}
	
	public Type getType() {
		return type;
	}
	
}
