package functions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import data.DefaultTypes;
import data.Type;
import data.VariableMarker;

/**
 * A class for reading functions from a String pointed to by a Scanner
 */
public class FunctionReader {
	
	// used to avoid errors involving new types "identical" to default types not being recognized as being the default types.
	private static final boolean startWithDefaultTypes = true;

	/**
	 * A recursive method to return the Function specified by the String pointed to by scan.
	 * Note that this doesn't check to make sure the function or syntax is valid.
	 * Behavior is undefined if syntax is invalid.
	 * @param scan the Scanner pointing to the Function that is to be read
	 * @return the Function specified by the String pointed to by scan
	 */
	public static Function interpretString(Scanner scan) {
		LinkedList<Function> inputFunctions;
		String nextWord = scan.next();
		switch (nextWord) {
			case "x":
				//get rid of variable type tag
				scan.next();
				//get rid of number of variable
				scan.next();
				//return null function, as that's how an input is stored in an inputFunction list
				return null;
			case "true":
				//get rid of parentheses
				scan.next();
				scan.next();
				return new ConstBoolFunction(new LinkedList<Function>(), (Boolean) true);
			case "false":
				//get rid of parentheses
				scan.next();
				scan.next();
				return new ConstBoolFunction(new LinkedList<Function>(), (Boolean) false);
			case "Add":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(interpretString(scan));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new AddFunction(inputFunctions);
			case "And":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(interpretString(scan));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new AndFunction(inputFunctions);
			case "Divide":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(interpretString(scan));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new DivideFunction(inputFunctions);
			case "Equal":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(interpretString(scan));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new EqualFunction(inputFunctions);
			case "Exists":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 1; i++) {
					inputFunctions.add(interpretString(scan));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new ExistsFunction(inputFunctions);
			case "GreaterThan":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(interpretString(scan));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new GreaterThanFunction(inputFunctions);
			case "IfElse":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 3; i++) {
					inputFunctions.add(interpretString(scan));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new IfElseFunction(inputFunctions);
			case "IfThen":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(interpretString(scan));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new IfThenFunction(inputFunctions);
			case "LessThan":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(interpretString(scan));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new LessThanFunction(inputFunctions);
			case "Modulo":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(interpretString(scan));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new ModuloFunction(inputFunctions);
			case "Multiply":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(interpretString(scan));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new MultiplyFunction(inputFunctions);
			case "Not":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 1; i++) {
					inputFunctions.add(interpretString(scan));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new NotFunction(inputFunctions);
			case "Or":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(interpretString(scan));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new OrFunction(inputFunctions);
			case "Subtract":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(interpretString(scan));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new SubtractFunction(inputFunctions);
				
			default:
				//it's a constant Integer Function
				//get rid of parentheses
				scan.next();
				scan.next();
				return new ConstIntFunction(new LinkedList<Function>(), Integer.parseInt(nextWord));
		}
	}
	
	/**
	 * Method to return a String representation of a list of SpecificFunctions
	 * @param specificFunctions the list of SpecificFunctions to create a String representation of
	 * @return the String representation of specificFunctions
	 */
	public static String specificFunctionListToString(List<SpecificFunction> specificFunctions) {
		String result = "";
		for (SpecificFunction sF : specificFunctions)
			result += sF.toString() + '\n';
		return result;
	}
	
	/**
	 * Method to return the list of StringFunctions specified by the String scanned by scan.
	 * @param scan the Scanner that reads the String that contains the data for the SpecificFunctions
	 * @param typeMap the Map<Integer, Type> with typeTags as keys and Types as values,
	 * identical to those used in the String representation of the SpecificFunctions.
	 * @return the list of StringFunctions specified by the String scanned by scan.
	 */
	public static LinkedList<SpecificFunction> stringToSpecificFunctionList(Scanner scan, Map<Integer, Type> typeMap) {
		LinkedList<SpecificFunction> result = new LinkedList<SpecificFunction>();
		while (scan.hasNext()) {
			LinkedList<VariableMarker> variableMarkers = new LinkedList<VariableMarker>();
			Function internalFunction = stringToSpecificFunction(scan, variableMarkers, typeMap);
			result.add(new SpecificFunction(internalFunction, variableMarkers));
		}
		return result;
	}
	
	/**
	 * Returns the SpecificFunction specified by scan.
	 * @param scan Scanner that reads the String that represents the SpecificFunction.
	 * @param typeMap had typeTags as keys and Types as values.
	 * @return the SpecificFunction specified by scan.
	 */
	public static SpecificFunction stringToSpecificFunction(Scanner scan,
			Map<Integer, Type> typeMap) {
		LinkedList<VariableMarker> variableMarkers = new LinkedList<VariableMarker>();
		Function internalFunction = stringToSpecificFunction(scan, variableMarkers, typeMap);
		return new SpecificFunction(internalFunction, variableMarkers);
	}

	/**
	 * Recursive method to return the Function specified by scan, while filling in VariableMarkers used as well.
	 * @param scan Scanner that reads the String that represents the SpecificFunction.
	 * @param variableMarkers the List of VariableMarkers that will be filled as this method runs.
	 * @param typeMap had typeTags as keys and Types as values.
	 * @return the Function contained in the SpecificFunction.
	 */
	private static Function stringToSpecificFunction(Scanner scan,
			LinkedList<VariableMarker> variableMarkers, Map<Integer, Type> typeMap) {
		LinkedList<Function> inputFunctions;
		String nextWord = scan.next();
		switch (nextWord) {
			case "x":
				//get variable type tag
				int typeTag = Integer.parseInt(scan.next());
				Type varType = typeMap.get((Integer) typeTag);
				//get marker index of variable
				int markerIndex = Integer.parseInt(scan.next());
				variableMarkers.add(new VariableMarker(varType, markerIndex));
				//return null function, as that's how an input is stored in an inputFunction list
				return null;
			case "true":
				//get rid of parentheses
				scan.next();
				scan.next();
				return new ConstBoolFunction(new LinkedList<Function>(), (Boolean) true);
			case "false":
				//get rid of parentheses
				scan.next();
				scan.next();
				return new ConstBoolFunction(new LinkedList<Function>(), (Boolean) false);
			case "Add":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(stringToSpecificFunction(scan, variableMarkers, typeMap));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new AddFunction(inputFunctions);
			case "And":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(stringToSpecificFunction(scan, variableMarkers, typeMap));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new AndFunction(inputFunctions);
			case "Divide":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(stringToSpecificFunction(scan, variableMarkers, typeMap));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new DivideFunction(inputFunctions);
			case "Equal":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(stringToSpecificFunction(scan, variableMarkers, typeMap));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new EqualFunction(inputFunctions);
			case "Exists":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 1; i++) {
					inputFunctions.add(stringToSpecificFunction(scan, variableMarkers, typeMap));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new ExistsFunction(inputFunctions);
			case "GreaterThan":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(stringToSpecificFunction(scan, variableMarkers, typeMap));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new GreaterThanFunction(inputFunctions);
			case "IfElse":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 3; i++) {
					inputFunctions.add(stringToSpecificFunction(scan, variableMarkers, typeMap));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new IfElseFunction(inputFunctions);
			case "IfThen":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(stringToSpecificFunction(scan, variableMarkers, typeMap));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new IfThenFunction(inputFunctions);
			case "LessThan":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(stringToSpecificFunction(scan, variableMarkers, typeMap));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new LessThanFunction(inputFunctions);
			case "Modulo":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(stringToSpecificFunction(scan, variableMarkers, typeMap));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new ModuloFunction(inputFunctions);
			case "Multiply":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(stringToSpecificFunction(scan, variableMarkers, typeMap));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new MultiplyFunction(inputFunctions);
			case "Not":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 1; i++) {
					inputFunctions.add(stringToSpecificFunction(scan, variableMarkers, typeMap));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new NotFunction(inputFunctions);
			case "Or":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(stringToSpecificFunction(scan, variableMarkers, typeMap));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new OrFunction(inputFunctions);
			case "Subtract":
				inputFunctions = new LinkedList<Function>();
				//get rid of parenthesis at beginning of function
				scan.next();
				for (int i = 0; i < 2; i++) {
					inputFunctions.add(stringToSpecificFunction(scan, variableMarkers, typeMap));
					//get rid of comma after element
					scan.next();
				}
				//get rid of parenthesis at end of function
				scan.next();
				return new SubtractFunction(inputFunctions);
				
			default:
				//it's a constant Integer Function
					//get rid of parentheses
					scan.next();
					scan.next();
					return new ConstIntFunction(new LinkedList<Function>(), Integer.parseInt(nextWord));
		}
	}
	
	/**
	 * Returns a typeMap with typeTags as keys and Types as values.
	 * Types in the Map will equal types previously created.
	 * @param types the list of Types to be mapped.
	 * @return a typeMap with typeTags as keys and Types as values.
	 */
	public static Map<Integer, Type> getTypeMap(List<Type> types) {
		Map<Integer, Type> typeMap = new HashMap<Integer, Type>();
		for (Type t : types) {
			typeMap.put(t.getTypeTag(), t);
		}
		return typeMap;
	}
	
	/**
	 * WARNING: Types created in the returned Map will not equal Types previously made.
	 * Use startWithDefaultTypes=true to compensate for this.
	 * Returns a Map with typeTags as keys and Types as values.
	 * Can be used to regenerate saved SpecificFunctions.
	 * @param scan a Scanner that points to the String that contains a Type's data on each line.
	 * Must specify superTypes before subTypes.
	 * In the format: [type tag] [super type tag 1] [super type tag 2] ... [super type tag n]
	 * [type tag 2] [super type tag 1] ... [super type tag m]
	 * ...
	 * @return Map with typeTags as keys and Types as values.
	 */
	public static Map<Integer, Type> getTypeMap(Scanner scan) {
		LinkedList<Integer> subTypes = new LinkedList<Integer>();
		LinkedList<LinkedList<Integer>> superTypes = new LinkedList<LinkedList<Integer>>();
		Scanner lineScanner;
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			lineScanner = new Scanner(line);
			if (lineScanner.hasNext()) {
				LinkedList<Integer> currentSuperTypes = new LinkedList<Integer>();
				subTypes.add(lineScanner.nextInt());
				while (lineScanner.hasNext())
					currentSuperTypes.add(lineScanner.nextInt());
				superTypes.add(currentSuperTypes);
			}
			lineScanner.close();
		}
		return getTypeMap(subTypes, superTypes);
	}
	
	/**
	 * WARNING: Types created in the returned Map will not equal Types previously made.
	 * Use startWithDefaultTypes=true to compensate for this.
	 * @param subTypes list of Types to include in the map.  SuperTypes of these will also be included.
	 * @param superTypes the list of superTypes for each Type in subTypes.
	 * @return Map with typeTags as keys and Types as values.
	 */
	public static Map<Integer, Type> getTypeMap(LinkedList<Integer> subTypes, LinkedList<LinkedList<Integer>> superTypes) {
		Map<Integer, Type> typeMap = new HashMap<Integer, Type>();
		if (startWithDefaultTypes)
			//populate typeMap with default types so they are not overridden.
			//TODO: these may not be in order such that superTypes come first
			for (Type t : DefaultTypes.getDefaultTypes())
				typeMap.put(t.getTypeTag(), t);
		Iterator<Integer> subTypeTagsIt = subTypes.iterator();
		Iterator<LinkedList<Integer>> superTypesListIt = superTypes.iterator();
		while (subTypeTagsIt.hasNext()) {
			Integer nextTag = subTypeTagsIt.next();
			LinkedList<Integer> superTypesList = superTypesListIt.next();
			//make sure it hasn't already been added, just in case.
			if (!typeMap.containsKey(nextTag)) {
				LinkedList<Type> currentSuperTypes = new LinkedList<Type>();
				for (Integer i : superTypesList)
					currentSuperTypes.add(typeMap.get(i));
				typeMap.put(nextTag, new Type(currentSuperTypes, nextTag));
			}
		}
		return typeMap;
	}

	/**
	 * Returns a String representation of all the types given.
	 * Will be in order so that superTypes occur before their subTypes.
	 * All types directly given and their superTypes appear first in exactly one line.
	 * Format:  [typeTag] [superTypeTag 1] [superTypeTag 2] ...
	 * [typeTag 2] [superTypeTag 1] ...
	 * ...
	 * @param types all the types that are to be saved
	 * @return the String representation of each Type and superType provided
	 */
	public static String typeListToString(List<Type> types) {
		return typeListToString(types, new HashMap<Integer, Type>());
	}
	
	/**
	 * Recursive method to return a String representation of all the types given.
	 * @param types all the types that are to be saved
	 * @param typeMap the map of all typeTags and their Types that have been added already
	 * @return the String representation of each Type and superType provided
	 */
	private static String typeListToString(List<Type> types, Map<Integer, Type> typeMap) {
		String result = "";
		for (Type t : types) {
			result += typeListToString(t.getSuperTypes(), typeMap);
			if (!typeMap.containsKey(t.getTypeTag())) {
				typeMap.put(t.getTypeTag(), t);
				result += t.getTypeTag() + " ";
				for (Type superT : t.getSuperTypes())
					result += superT.getTypeTag() + " ";
				result += '\n';
			}
		}
		return result;
	}
	
}
