package patterns;
import java.util.ArrayList;
import java.util.Random;

public class FunctionTree {

	// static fields for storing basic nodes
	private static final FunctionNode[] basicFunctionNodes = initializeBasicNodes();
	private static VariableNode[] variableNodes = {};
	private static int numInputs = 0;
	private static double[] inputs;
	private static boolean inputsReady = false;
	public static Random rand = new Random();
	
	// private fields for storing tree data
	private Node headerNode;
	private int localNumInputs;

	/**
	 * Constructor
	 * @param numInputs must be > 0
	 */
	public FunctionTree(int numInputs) {
		this(numInputs, variableNodes[0]);
	}

	/**
	 * Constructor
	 * @param numInputs must be > 0, headerNode != null
	 */
	public FunctionTree(int numInputs, Node headerNode) {
		assert numInputs > 0 && headerNode != null : "FunctionTree: numInputs out of bounds";
		if (numInputs != FunctionTree.numInputs)
			throw new IllegalStateException("FunctionTree constructor: FunctionTree.numInputs does not match local numInputs");
		this.headerNode = headerNode;
		this.localNumInputs = numInputs;
	}
	
	public static void setNumInputs(int numInputs) {
		FunctionTree.numInputs = numInputs;
		variableNodes = initializeVariableNodes(numInputs);
		inputsReady = false;
	}

	/**
	 * Used before each generation to update the inputs for all FunctionTree's
	 * @param inputs the array to copy to FunctionTree.inputs
	 */
	public static void updateInputs(double[] inputs) {
		assert inputs.length == numInputs : "updateInputs: inputs.lenght != numInputs";
		FunctionTree.inputs = new double[numInputs];
		for (int i = 0; i < numInputs; i++)
			FunctionTree.inputs[i] = inputs[i];
		inputsReady = true;
	}

	/**
	 * Returns the functionTree's result given the current static input
	 * @pre inputsReady
	 * @return the functionTree's result given the current static input
	 */
	public double fire() {
		if (!inputsReady || localNumInputs != numInputs)
			throw new IllegalStateException("FunctionTree fire: inputs not initialized"
					+ "or numInputs does not match static numInputs");
		return headerNode.fire();
	}
	
	public Node getNodeAtLength(int depth) {
		return headerNode.getNodeAtLength(depth);
	}
	
	public int getLength() {
		if (headerNode == null)
			return 0;
		return headerNode.getLength();
	}
	
	public static int getNumBasicNodes() {
		return basicFunctionNodes.length;
	}
	
	/**
	 * Returns a deep copy of the basic node with random variableNode subNodes
	 * [each deep copies themselves] of the index given
	 * @param index the index of the basicNode desired
	 * @param prevNode the node that the result's prevNode should be set to; what will come before the result
	 * @param prevNodeIndex the index of the resulting node's position in its prevNode's subNode array
	 * @return a deep copy of the basic node desired, with random subNodex
	 */
	public static FunctionNode getBasicNode(int index, FunctionNode prevNode, int prevNodeIndex) {
		if (index < 0 || index >= basicFunctionNodes.length)
			throw new IllegalArgumentException("getBasicNode: index out of bounds");
		FunctionNode result = basicFunctionNodes[index].clone(prevNode, prevNodeIndex);
		for (int i = 0; i < result.subNodes.length; i++) {
			Node tempNode = variableNodes[rand.nextInt(numInputs)].clone(result, i);
			result.subNodes[i] = tempNode;
		}
		return result;
	}
	
	public static double[] getInputs() {
		if (!inputsReady)
			throw new IllegalStateException("getInputs: !inputsReady");
		return FunctionTree.inputs;
	}
	
	public static int getNumInputs() {
		return FunctionTree.numInputs;
	}
	
	public void setHeaderNode(Node newHeader) {
		if (newHeader == null)
			throw new IllegalArgumentException("setHeaderNode: newHeader cannot be null");
		headerNode = newHeader;
	}
	
	private static VariableNode[] initializeVariableNodes(int numInputs) {
		VariableNode[] result = new VariableNode[numInputs];
		for (int i = 0; i < numInputs; i++)
			result[i] = new VariableNode(i, null, -1);
		return result;
	}
	
	private static FunctionNode[] initializeBasicNodes() {
//		final int NUM_OF_BASIC_NODES = 8;
		ArrayList<FunctionNode> nodeList = new ArrayList<FunctionNode>();
//		FunctionNode[] result = new FunctionNode[NUM_OF_BASIC_NODES];
		Node[] blanks = new Node[2];
		for (int i = 0; i < blanks.length; i++)
			blanks[i] = new VariableNode(0, null, -1);
		nodeList.add(new AddNode(blanks, null, -1));
		nodeList.add(new SubtractNode(blanks, null, -1));
		nodeList.add(new MultiplyNode(blanks, null, -1));
		nodeList.add(new DivideNode(blanks, null, -1));
//		nodeList.add(new MaxNode(blanks, null, -1));
//		nodeList.add(new MinNode(blanks, null, -1));
		blanks = new Node[1];
		for (int i = 0; i < blanks.length; i++)
			blanks[i] = new VariableNode(0, null, -1);
//		nodeList.add(new SineNode(blanks, null, -1));
//		nodeList.add(new CosineNode(blanks, null, -1));
		nodeList.add(new AbsNode(blanks, null, -1));
		
		return nodeList.toArray(new FunctionNode[nodeList.size()]);
	}
	
	public FunctionTree clone() {
		return new FunctionTree(localNumInputs, headerNode.clone(null, -1));
	}
	
	public String toString() {
		return headerNode.toString();
	}
	
}
