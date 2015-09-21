package functions;

import java.util.Iterator;
import java.util.LinkedList;

import data.Type;
import data.VariableMarker;

public class SpecificFunction {
	
	private static final boolean DEBUG = true;
	
	private LinkedList<VariableMarker> variableMarkers;
	private Function internalFunction;

	public SpecificFunction(Function internalFunction, LinkedList<VariableMarker> variableMarkers) {
		if (DEBUG) {
			if (internalFunction.getTotalInputs() != variableMarkers.size())
				throw new IllegalArgumentException(
						"SpecificFunction: variableMarkers size does not match internalFunction inputs."
						+ " functionInputs: " + internalFunction.getTotalInputs() + " markers: " + variableMarkers.size());
			Iterator<VariableMarker> markerIt = variableMarkers.iterator();
			Iterator<Type> inputTypesIt = internalFunction.inputTypes.iterator();
			while (markerIt.hasNext()) {
				if (!markerIt.next().getType().isSub(inputTypesIt.next()))
					throw new IllegalArgumentException(
							"SpecificFunction: variableMarkers of invalid type for function inputs");
			}
		}
		this.internalFunction = internalFunction;
		this.variableMarkers = variableMarkers;
	}

	
	public final String toString() {
		String result = recursiveToString(this.internalFunction, this.variableMarkers.iterator());
		return result;
	}
	
	private final String recursiveToString(Function currentFunction, Iterator<VariableMarker> variableMarkersIt) {
		String result = currentFunction.functionName() + " ( ";
		for (Function f : currentFunction.inputFunctions) {
			//base case
			if (f == null) {
				VariableMarker marker = variableMarkersIt.next();
				result += "x " + marker.getType().getTypeTag() + ' ' + marker.getMarkerIndex() + ' ';
			} else
				result += recursiveToString(f, variableMarkersIt) + ' ';
			result += ", ";
		}
		result += ')';
		return result;
	}

}
