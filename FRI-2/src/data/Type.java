package data;

import java.util.ArrayList;
import java.util.List;

//import java.util.HashMap;
//import java.util.Map;

public class Type {
//	private static Map<Integer, Integer> typeToSuper = new HashMap<Integer, Integer>();
	private static Integer numTypes = 0;

	// the type ID of this value
//	private Integer type;
	// the Types that are a subset of this Type; something more specific than this
	private ArrayList<Type> subTypes;
	// the Types that this Type is a subset of; something more general than this
	private ArrayList<Type> superTypes;
	//the number specific to this Type
	private int typeTag;
//	private Type superType;
	//TODO: add list of related cause/effect pointers?
	
	/**
	 * Adds each superType to the list of superTypes; adds this to each super's subTypes
	 * Creates new lists; does not rely on the given lists
	 * @param superTypes
	 */
	public Type(List<Type> superTypes) {
		this.superTypes = new ArrayList<Type>();
		if (superTypes != null)
			for (Type superT : superTypes) {
				this.superTypes.add(superT);
				// add this type to the subTypes of each superType
				superT.subTypes.add(this);
			}
//		this.superType = superType;
		this.subTypes = new ArrayList<Type>();
//		this.type = getNewTypeID();
		this.typeTag = ++numTypes;
	}

	public Type(Type superType) {
		this.superTypes = new ArrayList<Type>();
		if (superType != null) {
			this.superTypes.add(superType);
			superType.subTypes.add(this);
		}
//		this.superType = superType;
		this.subTypes = new ArrayList<Type>();
//		this.type = getNewTypeID();
		this.typeTag = ++numTypes;
	}
	
	/**
	 * WARNING: not to be used regularly.  For reformation of saved SpecificFunctions only.
	 * Types created with the same specifications as another Type will not be considered equal, so avoid deceptive "duplicates".
	 * @param superTypes superTypes of this Type.
	 * @param typeTag the typeTag value of this Type.  Does not increment numTypes.
	 */
	public Type(List<Type> superTypes, int typeTag) {
		this.superTypes = new ArrayList<Type>();
		if (superTypes != null)
			for (Type superT : superTypes) {
				this.superTypes.add(superT);
				// add this type to the subTypes of each superType
				superT.subTypes.add(this);
			}
//		this.superType = superType;
		this.subTypes = new ArrayList<Type>();
//		this.type = getNewTypeID();
		this.typeTag = typeTag;
	}
	
	/**
	 * Returns true if this is a subtype of otherType
	 * @param otherType another type
	 * @return true if this is a subtype of otherType / otherType is a supertype of this
	 */
	public boolean isSub(Type otherType) {
		if (this == otherType)
			return true;
		for (Type supT : superTypes) {
			if (supT == otherType || (supT != null && supT.isSub(otherType)))
				return true;
		}
		return false;
	}
	
	public ArrayList<Type> getSuperTypes() {
		return superTypes;
	}

/*	public Type getSuperType() {
		return superType;
	}
*/
	
	public ArrayList<Type> getSubTypes() {
		return subTypes;
	}
	
	public int getTypeTag() {
		return typeTag;
	}
	
	/**
	 * Updates numTypes, typeToSuper, and returns the new TypeID
	 * @param superType
	 * @return
	 */
/*	private static int getNewTypeID(Type superType) {
		++numTypes;
		typeToSuper.put(numTypes, superType);
		return numTypes;
	}
*/
}
