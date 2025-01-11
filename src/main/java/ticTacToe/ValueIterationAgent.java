package ticTacToe;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Value Iteration Agent, only very partially implemented. The methods to implement are: 
 * (1) {@link ValueIterationAgent#iterate}
 * (2) {@link ValueIterationAgent#extractPolicy}
 * 
 * You may also want/need to edit {@link ValueIterationAgent#train} - feel free to do this, but you probably won't need to.
 * @author ae187
 *
 */
public class ValueIterationAgent extends Agent {

	/**
	 * This map is used to store the values of states
	 */
	Map<Game, Double> valueFunction=new HashMap<Game, Double>();
	
	/**
	 * the discount factor
	 */
	double discount=0.9;
	
	/**
	 * the MDP model
	 */
	TTTMDP mdp=new TTTMDP();
	
	/**
	 * the number of iterations to perform - feel free to change this/try out different numbers of iterations
	 */
	int k=10;
	
	
	/**
	 * This constructor trains the agent offline first and sets its policy
	 */
	public ValueIterationAgent()
	{
		super();
		mdp=new TTTMDP();
		this.discount=0.9;
		initValues();
		train();
	}
	
	
	/**
	 * Use this constructor to initialise your agent with an existing policy
	 * @param p
	 */
	public ValueIterationAgent(Policy p) {
		super(p);
		
	}

	public ValueIterationAgent(double discountFactor) {
		
		this.discount=discountFactor;
		mdp=new TTTMDP();
		initValues();
		train();
	}
	
	/**
	 * Initialises the {@link ValueIterationAgent#valueFunction} map, and sets the initial value of all states to 0 
	 * (V0 from the lectures). Uses {@link Game#inverseHash} and {@link Game#generateAllValidGames(char)} to do this. 
	 * 
	 */
	public void initValues()
	{
		
		List<Game> allGames=Game.generateAllValidGames('X');//all valid games where it is X's turn, or it's terminal.
		for(Game g: allGames)
			this.valueFunction.put(g, 0.0);
		
		
		
	}
	
	
	
	public ValueIterationAgent(double discountFactor, double winReward, double loseReward, double livingReward, double drawReward)
	{
		this.discount=discountFactor;
		mdp=new TTTMDP(winReward, loseReward, livingReward, drawReward);
	}
	
	/**
	 
	
	/*
	 * Performs {@link #k} value iteration steps. After running this method, the {@link ValueIterationAgent#valueFunction} map should contain
	 * the (current) values of each reachable state. You should use the {@link TTTMDP} provided to do this.
	 * 
	 *
	 */
	public void iterate()
	{
		// Perform the specified number of iterations
		for (int i = 0; i < k; i++){
			// Temporary map to store the updated values
			Map<Game,Double> newValues = new HashMap<>(valueFunction);
			
			// Update values for each state in the value function
			for (Game state: valueFunction.keySet()){
				if (state.isTerminal()){ // Terminal state should have a value of 0
					newValues.put(state, 0.0);
					continue;
				}
				double maxActionValue=Double.NEGATIVE_INFINITY;

				// Iterate over all possible actions from the current state
				for (Move action : state.getPossibleMoves()){
					double actionValue = 0.0;

					// Get transitions for this action
					List<TransitionProb> transitions = mdp.generateTransitions(state, action);

					// Calculate the expected action value
					for (TransitionProb transition : transitions){
						double reward = transition.outcome.localReward;
						Game nextState = transition.outcome.sPrime;
						double prob = transition.prob;

						// Bellman update rule
						actionValue += prob * (reward + discount * valueFunction.get(nextState));
					}

					// Tracking the maximum value across all actions
					maxActionValue = Math.max(maxActionValue, actionValue);
				}

				// Update the new value for the state
				newValues.put(state, maxActionValue);
			}

			// Updating the value function with new computed values
			valueFunction = newValues;
		}
	}
	
	/**This method should be run AFTER the train method to extract a policy according to {@link ValueIterationAgent#valueFunction}
	 * You will need to do a single step of expectimax from each game (state) key in {@link ValueIterationAgent#valueFunction} 
	 * to extract a policy.
	 * 
	 * @return the policy according to {@link ValueIterationAgent#valueFunction}
	 */

	public Policy extractPolicy()
	{
		Policy policy=new Policy();

		// Generating a policy by choosing the best action for ech state
		for (Game state: valueFunction.keySet()){
			if (state.isTerminal()) continue;

		Move bestAction  = null;
		double bestActionValue = Double.NEGATIVE_INFINITY;

		// Finding the best action by comparing action values
			for (Move action: state.getPossibleMoves()){
				double actionValue = 0.0;


				// Calculate the action value based on the possible transitions
				List<TransitionProb> transitions = mdp.generateTransitions(state, action);
				for (TransitionProb transition : transitions){
					double reward = transition.outcome.localReward;
					Game nextState = transition.outcome.sPrime;
					double prob = transition.prob;

					// Bellman equation for action value
					actionValue += prob * (reward + discount * valueFunction.get(nextState));
				}

				// Choosing the action with the highest expected value
				if (actionValue > bestActionValue){
					bestActionValue = actionValue;
					bestAction = action;
				}
			}
			// Set the best action for this state in the policy
			policy.setMove(state, bestAction);

		}
		return policy;
	}
	
	/**
	 * This method solves the mdp using your implementation of {@link ValueIterationAgent#extractPolicy} and
	 * {@link ValueIterationAgent#iterate}. 
	 */
	public void train()
	{
		/**
		 * First run value iteration
		 */
		this.iterate();
		/**
		 * now extract policy from the values in {@link ValueIterationAgent#valueFunction} and set the agent's policy 
		 *  
		 */
		
		super.policy=extractPolicy();
		
		if (this.policy==null)
		{
			System.out.println("Unimplemented methods! First implement the iterate() & extractPolicy() methods");
			//System.exit(1);
		} else {
			System.out.println("Policy set successfull");

		}
		
		
		
	}

	public static void main(String a[]) throws IllegalMoveException
	{
		//Test method to play the agent against a human agent.
		ValueIterationAgent agent=new ValueIterationAgent();
		HumanAgent d=new HumanAgent();
		
		Game g=new Game(agent, d, d);
		g.playOut();
		
		
		

		
		
	}
}
