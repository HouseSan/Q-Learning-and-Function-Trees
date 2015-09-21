package patterns;

public abstract class Node {
	private FunctionNode prevNode;
	private int prevNodeIndex;
	
/*	public Node() {
		this(null, -1);
	}
*/	
	public Node(FunctionNode prevNode, int prevNodeIndex) {
		this.prevNode = prevNode;
		this.prevNodeIndex = prevNodeIndex;
	}
	
	public abstract double fire();
	
//	public Node clone() {
//		return clone(null, -1);
//	}
	
	public abstract Node clone(FunctionNode prevNode, int prevNodeIndex);
	// returns the node at depth below this one, where 0 is this, 1 is any subNode of this, etc
	public abstract Node getNodeAtLength(int depth);
	public abstract int getLength();
	
	/**
	 * A method to get the node above this one.
	 * @return the node that this one is contained within; null if this is a root node
	 */
	public FunctionNode getPrev() {
		return prevNode;
	}
	
	public int getPrevIndex() {
		return prevNodeIndex;
	}
	
	/**
	 * Replaces this node in the previous node's slot with a given Node
	 * @param replacement the node to replace this
	 * @return true if replaced, false if getPrev() == null, which indicates this is a root node
	 */
	public boolean setPrev(Node replacement) {
		if (prevNode == null)
			return false;
		// else
		prevNode.subNodes[prevNodeIndex] = replacement.clone(prevNode, prevNodeIndex);
		// this no longer belongs to prevNode; update fields accordingly
		prevNode = null;
		prevNodeIndex = -1;
		return true;
	}
	
	public abstract String toString();
}


abstract class FunctionNode extends Node {
	
	public Node[] subNodes;
	
	public FunctionNode(Node[] subNodes) {
		this(subNodes, null, -1);
	}
	
	public FunctionNode(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		super(prevNode, prevNodeIndex);
		int numNodes = initializeNumNodes();
		assert (numNodes > 0 && subNodes.length == numNodes) : "FunctionNode: 0 < subNodes.length == numNodes";
		Node[] result = new Node[numNodes];
//		if (subNodes != null)
		for (int i = 0; i < numNodes; i++)
			result[i] = subNodes[i].clone(this, i);
		this.subNodes= result;
	}
	
	public String toString() {
		String result = methodName() + '(' + subNodes[0].toString();
		for (int i = 1; i < subNodes.length; i++)
			result += ", " + subNodes[i].toString();
		result += " )";
		return result;
	}
	
	/**
	 * Used like a private static int method. Used in FunctionNode constructor.
	 * @return the number of sub-nodes for this class
	 */
	public abstract int initializeNumNodes();
	public abstract String methodName();
	
	public Node getNodeAtLength(int depth) {
		if (depth == 0)
			return this;
		// recursive case
		int randInt = FunctionTree.rand.nextInt(subNodes.length);
		return subNodes[randInt].getNodeAtLength(depth - 1);
	}
	
	// makes an array of subNodes which are copies of its subNodes,
	// and uses its constructor to make a new one to return
	@Override
	public FunctionNode clone(FunctionNode prevNode, int prevNodeIndex) {
		Node[] result = new Node[subNodes.length];
		for (int i = 0; i < subNodes.length; i++)
			result[i] = subNodes[i].clone(this, i);
		return pseudoConstructor(result, prevNode, prevNodeIndex);
	}

	public abstract FunctionNode pseudoConstructor(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex);
	
	public int getLength() {
		int max = 0;
		for (Node n : subNodes)
			max = Math.max(max, n.getLength());
		return max + 1;
	}
}

class AddNode extends FunctionNode {
	
	public AddNode(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		super(subNodes, prevNode, prevNodeIndex);
	}

	@Override
	public double fire() {
		return subNodes[0].fire() + subNodes[1].fire();
	}

	@Override
	public int initializeNumNodes() {
		return 2;
	}

	@Override
	public FunctionNode pseudoConstructor(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		return new AddNode(subNodes, prevNode, prevNodeIndex);
	}

	@Override
	public String methodName() {
		return "Add";
	}
}

class SubtractNode extends FunctionNode {
	public SubtractNode(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		super(subNodes, prevNode, prevNodeIndex);
	}

	@Override
	public double fire() {
		return subNodes[0].fire() - subNodes[1].fire();
	}

	@Override
	public int initializeNumNodes() {
		return 2;
	}

	@Override
	public FunctionNode pseudoConstructor(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		return new SubtractNode(subNodes, prevNode, prevNodeIndex);
	}

	@Override
	public String methodName() {
		return "Subtract";
	}
}

class MultiplyNode extends FunctionNode {
	public MultiplyNode(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		super(subNodes, prevNode, prevNodeIndex);
	}

	@Override
	public double fire() {
		return subNodes[0].fire() * subNodes[1].fire();
	}

	@Override
	public int initializeNumNodes() {
		return 2;
	}

	@Override
	public FunctionNode pseudoConstructor(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		return new MultiplyNode(subNodes, prevNode, prevNodeIndex);
	}

	@Override
	public String methodName() {
		return "Multiply";
	}
}

class DivideNode extends FunctionNode {
	public DivideNode(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		super(subNodes, prevNode, prevNodeIndex);
	}

	@Override
	public double fire() {
		return subNodes[0].fire() / subNodes[1].fire();
	}

	@Override
	public int initializeNumNodes() {
		return 2;
	}

	@Override
	public FunctionNode pseudoConstructor(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		return new DivideNode(subNodes, prevNode, prevNodeIndex);
	}

	@Override
	public String methodName() {
		return "Divide";
	}
}

class MaxNode extends FunctionNode {
	public MaxNode(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		super(subNodes, prevNode, prevNodeIndex);
	}

	@Override
	public double fire() {
		return Math.max(subNodes[0].fire(), subNodes[1].fire());
	}

	@Override
	public int initializeNumNodes() {
		return 2;
	}

	@Override
	public FunctionNode pseudoConstructor(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		return new MaxNode(subNodes, prevNode, prevNodeIndex);
	}

	@Override
	public String methodName() {
		return "Max";
	}
}

class MinNode extends FunctionNode {
	public MinNode(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		super(subNodes, prevNode, prevNodeIndex);
	}

	@Override
	public double fire() {
		return Math.min(subNodes[0].fire(), subNodes[1].fire());
	}

	@Override
	public int initializeNumNodes() {
		return 2;
	}

	@Override
	public FunctionNode pseudoConstructor(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		return new MinNode(subNodes, prevNode, prevNodeIndex);
	}

	@Override
	public String methodName() {
		return "Min";
	}
}

// returns the absolute value
class AbsNode extends FunctionNode {
	public AbsNode(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		super(subNodes, prevNode, prevNodeIndex);
	}

	@Override
	public double fire() {
		return Math.abs(subNodes[0].fire());
	}

	@Override
	public int initializeNumNodes() {
		return 1;
	}

	@Override
	public FunctionNode pseudoConstructor(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		return new AbsNode(subNodes, prevNode, prevNodeIndex);
	}

	@Override
	public String methodName() {
		return "Abs";
	}
}

class SineNode extends FunctionNode {
	public SineNode(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		super(subNodes, prevNode, prevNodeIndex);
	}

	@Override
	public double fire() {
		return Math.sin(subNodes[0].fire());
	}

	@Override
	public int initializeNumNodes() {
		return 1;
	}

	@Override
	public FunctionNode pseudoConstructor(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		return new SineNode(subNodes, prevNode, prevNodeIndex);
	}

	@Override
	public String methodName() {
		return "Sine";
	}
}

class CosineNode extends FunctionNode {
	public CosineNode(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		super(subNodes, prevNode, prevNodeIndex);
	}

	@Override
	public double fire() {
		return Math.cos(subNodes[0].fire());
	}

	@Override
	public int initializeNumNodes() {
		return 1;
	}

	@Override
	public FunctionNode pseudoConstructor(Node[] subNodes, FunctionNode prevNode, int prevNodeIndex) {
		return new CosineNode(subNodes, prevNode, prevNodeIndex);
	}

	@Override
	public String methodName() {
		return "Cosine";
	}
}


// For returning variables in the tree's list of variables.
// Currently only returns one of the inputs.
class VariableNode extends Node {
	
	private int variableNum;
	
//	public VariableNode(int variableNum) {
//		this(variableNum, null, -1);
//	}
	
	public VariableNode(int variableNum, FunctionNode prevNode, int prevNodeIndex) {
		super(prevNode, prevNodeIndex);
		assert (variableNum >= 0 && variableNum < FunctionTree.getNumInputs()) : "variableNode constructor: variableNum out of bounds";
		this.variableNum = variableNum;
	}
	
	public double fire() {
		assert (variableNum < FunctionTree.getNumInputs()) : "variableNode fire: variableNum out of bounds";
		return FunctionTree.getInputs()[variableNum];
	}
	
	// Deep copy is necessary because of prevNode and prevNodeIndex
//	public VariableNode clone() {
//		return new VariableNode(variableNum, null, -1);
//	}

	@Override
	public Node clone(FunctionNode prevNode, int prevNodeIndex) {
		return new VariableNode(variableNum, prevNode, prevNodeIndex);
	}
	
	public int getLength() {
		return 1;
	}

	@Override
	public Node getNodeAtLength(int depth) {
		return this;
	}
	
	@Override
	public String toString() {
		return "[variable " + variableNum + ']';
	}
	
}