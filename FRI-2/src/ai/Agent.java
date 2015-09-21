package ai;

//import implications.*;
import functions.*;
import data.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A learning AI agent */
public class Agent {

//	private List<Relation> relations;
	
	private static final Function oneFunction = defineOneFunction();
	private static final Function trueFunction = defineTrueFunction();
	
	private ArrayList<Function> intKeeperFunctions;		//these aren't changed, only added to; return Integer
	private ArrayList<Function> boolKeeperFunctions;	//these aren't changed, only added to; return Boolean
	private ArrayList<Function> intTempFunctions;		//these are mutated but not evaluated; Integer
	private ArrayList<Function> boolTempFunctions;		//these are mutated but not evaluated; Boolean
	private ArrayList<Function> trainingFunctions;		//these are evaluated; whatever type is looked for
	private Type targetType;							//the type returned by elements of TrainingFunctions
	private QLearner qLearner;							//used to control the AI's moves
	private ArrayList<Boolean> inputs;
	
	public static void main(String[] args) {
		
	}
	
	public Agent(ArrayList<Function> intTempFunctions, ArrayList<Function> boolTempFunctions,
			ArrayList<Function> trainingFunctions, Type targetType, ArrayList<Boolean> inputs) {
		//TODO: fill this
		this.intKeeperFunctions = defaultIntKeepers();
		this.boolKeeperFunctions = defaultBoolKeepers();
		this.intTempFunctions = intTempFunctions;
		this.boolTempFunctions = boolTempFunctions;
		this.trainingFunctions = trainingFunctions;
		this.targetType = targetType;
		this.inputs = inputs;
		this.qLearner = new QLearner(boolKeeperFunctions, inputs);
	}
	
	private static ArrayList<Function> defaultIntKeepers() {
		ArrayList<Function> defaultIntKeepers = new ArrayList<Function>();
		
		defaultIntKeepers.add(Agent.oneFunction);
		
		LinkedList<Function> inputFunctions = new LinkedList<Function>();
		inputFunctions.add(Agent.oneFunction);
		inputFunctions.add(Agent.oneFunction);
		defaultIntKeepers.add(new AddFunction(inputFunctions));
		
		inputFunctions = new LinkedList<Function>();
		inputFunctions.add(Agent.oneFunction);
		inputFunctions.add(Agent.oneFunction);
		defaultIntKeepers.add(new DivideFunction(inputFunctions));
		
		inputFunctions = new LinkedList<Function>();
		inputFunctions.add(Agent.oneFunction);
		inputFunctions.add(Agent.oneFunction);
		defaultIntKeepers.add(new EqualFunction(inputFunctions));
		
		inputFunctions = new LinkedList<Function>();
		inputFunctions.add(Agent.trueFunction);
		inputFunctions.add(Agent.oneFunction);
		defaultIntKeepers.add(new IfElseFunction(inputFunctions));
		
		inputFunctions = new LinkedList<Function>();
		inputFunctions.add(Agent.trueFunction);
		inputFunctions.add(Agent.oneFunction);
		inputFunctions.add(Agent.oneFunction);
		defaultIntKeepers.add(new IfThenFunction(inputFunctions));
		
		inputFunctions = new LinkedList<Function>();
		inputFunctions.add(Agent.oneFunction);
		inputFunctions.add(Agent.oneFunction);
		defaultIntKeepers.add(new ModuloFunction(inputFunctions));
		
		inputFunctions = new LinkedList<Function>();
		inputFunctions.add(Agent.oneFunction);
		inputFunctions.add(Agent.oneFunction);
		defaultIntKeepers.add(new MultiplyFunction(inputFunctions));
		
		inputFunctions = new LinkedList<Function>();
		inputFunctions.add(Agent.oneFunction);
		inputFunctions.add(Agent.oneFunction);
		defaultIntKeepers.add(new SubtractFunction(inputFunctions));
		
		return defaultIntKeepers;
	}
	
	private static ArrayList<Function> defaultBoolKeepers() {
		ArrayList<Function> defaultBoolKeepers = new ArrayList<Function>();
		
		defaultBoolKeepers.add(Agent.trueFunction);
		
		LinkedList<Function> inputFunctions = new LinkedList<Function>();
		inputFunctions.add(Agent.trueFunction);
		defaultBoolKeepers.add(new NotFunction(inputFunctions));
		
		inputFunctions = new LinkedList<Function>();
		inputFunctions.add(Agent.trueFunction);
		inputFunctions.add(Agent.trueFunction);
		defaultBoolKeepers.add(new AndFunction(inputFunctions));
		
		inputFunctions = new LinkedList<Function>();
		inputFunctions.add(Agent.oneFunction);
		inputFunctions.add(Agent.oneFunction);
		defaultBoolKeepers.add(new EqualFunction(inputFunctions));
		
		inputFunctions = new LinkedList<Function>();
		inputFunctions.add(Agent.trueFunction);
		defaultBoolKeepers.add(new ExistsFunction(inputFunctions));
		
		inputFunctions = new LinkedList<Function>();
		inputFunctions.add(Agent.trueFunction);
		inputFunctions.add(Agent.trueFunction);
		defaultBoolKeepers.add(new GreaterThanFunction(inputFunctions));
		
		inputFunctions = new LinkedList<Function>();
		inputFunctions.add(Agent.trueFunction);
		inputFunctions.add(Agent.trueFunction);
		defaultBoolKeepers.add(new LessThanFunction(inputFunctions));
		
		inputFunctions = new LinkedList<Function>();
		inputFunctions.add(Agent.trueFunction);
		inputFunctions.add(Agent.trueFunction);
		defaultBoolKeepers.add(new OrFunction(inputFunctions));
		
		return defaultBoolKeepers;
	}
	
	private static Function defineOneFunction() {
		LinkedList<Function> emptyFunctionList = new LinkedList<Function>();
		
		LinkedList<Object> oneObjectData = new LinkedList<Object>();
		LinkedList<Type> oneObjectDataTypes = new LinkedList<Type>();
		oneObjectData.add((Integer) 1);
		oneObjectDataTypes.add(DefaultTypes.constIntType);
		GameObject oneObject = new GameObject(DefaultTypes.constIntObjType, oneObjectData, oneObjectDataTypes);
		Value oneValue = oneObject.getObjectData().getFirst();
		Function oneFunction = new ConstIntFunction(emptyFunctionList, oneValue);
		return oneFunction;
	}
	
	private static Function defineTrueFunction() {
		LinkedList<Function> emptyFunctionList = new LinkedList<Function>();
		
		LinkedList<Object> trueObjectData = new LinkedList<Object>();
		LinkedList<Type> trueObjectDataTypes = new LinkedList<Type>();
		trueObjectData.add((Boolean) true);
		trueObjectDataTypes.add(DefaultTypes.constBoolType);
		GameObject oneObject = new GameObject(DefaultTypes.constBoolObjType, trueObjectData, trueObjectDataTypes);
		Value trueValue = oneObject.getObjectData().getFirst();
		Function trueFunction = new ConstBoolFunction(emptyFunctionList, trueValue);
		return trueFunction;
	}
	
}
