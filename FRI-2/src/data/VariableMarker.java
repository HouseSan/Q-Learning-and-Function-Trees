package data;

public class VariableMarker {

	//type that this stands for
	private Type type;
	//the int marker to distinguish this marker from another Marker of the same type.
	private int markerIndex;
	
	public VariableMarker(Type type, int markerIndex) {
		this.type = type;
		this.markerIndex = markerIndex;
	}
	
	public boolean equals(VariableMarker other) {
		return this.type == other.type && this.markerIndex == other.markerIndex;
	}
	
	public Type getType() {
		return type;
	}
	
	public int getMarkerIndex() {
		return markerIndex;
	}
}
