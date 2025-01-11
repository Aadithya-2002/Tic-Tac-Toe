package ticTacToe;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
/**
 * A policy iteration agent. You should implement the following methods:
 * (1) {@link PolicyIterationAgent#evaluatePolicy}: this is the policy evaluation step from your lectures
 * (2) {@link PolicyIterationAgent#improvePolicy}: this is the policy improvement step from your lectures
 * (3) {@link PolicyIterationAgent#train}: this is a method that should runs/alternate (1) and (2) until convergence. 
 * 
 * NOTE: there are two types of convergence involved in Policy Iteration: Convergence of the Values of the current policy, 
 * and Convergence of the current policy to the optimal policy.
 * The former happens when the values of the current policy no longer improve by much (i.e. the maximum improvement is less than 
 * some small delta). The latter happens when the policy improvement step no longer updates the policy, i.e. the current policy 
 * is already optimal. The algorithm should stop when this happens.
 * 
 * @author ae187
 *
 */
public class PolicyIterationAgent extends Agent {

	/**
	 * This map is used to store the values of states according to the current policy (policy evaluation). 
	 */
	HashMap<Game, Double> policyValues=new HashMap<Game, Double>();
	
	/**
	 * This stores the current policy as a map from {@link Game}s to {@link Move}. 
	 */
	HashMap<Game, Move> curPolicy=new HashMap<Game, Move>();
	
	double discount=0.9;
	
	/**
	 * The mdp model used, see {@link TTTMDP}
	 */
	TTTMDP mdp;
	
	/**
	 * loads the policy from file if one exists. Policies should be stored in .pol files directly under the project folder.
	 */
	public PolicyIterationAgent() {
		super();
		this.mdp=new TTTMDP();
		initValues();
		initRandomPolicy();
		train();
		
		
	}
	
	
	/**
	 * Use this constructor to initialise your agent with an existing policy
	 * @param p
	 */
	public PolicyIterationAgent(Policy p) {
		super(p);
		
	}

	/**
	 * Use this constructor to initialise a learning agent with default MDP paramters (rewards, transitions, etc) as specified in 
	 * {@link TTTMDP}
	 * @param discountFactor
	 */
	public PolicyIterationAgent(double discountFactor) {
		
		this.discount=discountFactor;
		this.mdp=new TTTMDP();
		initValues();
		initRandomPolicy();
		train();
	}
	/**
	 * Use this constructor to set the various parameters of the Tic-Tac-Toe MDP
	 * @param discountFactor
	 * @param winningReward
	 * @param losingReward
	 * @param livingReward
	 * @param drawReward
	 */
	public PolicyIterationAgent(double discountFactor, double winningReward, double losingReward, double livingReward, double drawReward)
	{
		this.discount=discountFactor;
		this.mdp=new TTTMDP(winningReward, losingReward, livingReward, drawReward);
		initValues();
		initRandomPolicy();
		train();
	}
	/**
	 * Initialises the {@link #policyValues} map, and sets the initial value of all states to 0 
	 * (V0 under some policy pi ({@link #curPolicy} from the lectures). Uses {@link Game#inverseHash} and {@link Game#generateAllValidGames(char)} to do this. 
	 * 
	 */
	public void initValues()
	{
		List<Game> allGames=Game.generateAllValidGames('X');//all valid games where it is X's turn, or it's terminal.
		for(Game g: allGames)
			this.policyValues.put(g, 0.0);
		
	}
	
	/**
	 *  You should implement this method to initially generate a random policy, i.e. fill the {@link #curPolicy} for every state. Take care that the moves you choose
	 *  for each state ARE VALID. You can use the {@link Game#getPossibleMoves()} method to get a list of valid moves and choose 
	 *  randomly between them. 
	 */
	public void initRandomPolicy()
	{
		Random random = new Random();
		// Generating all valid game states for the 'X' player
		List<Game> allGames=Game.generateAllValidGames('X');

		for (Game state: allGames){
			if (!state.isTerminal()){ // Setting policies for non-terminal states
				// Getting all possible moves from the state
				List<Move> possibleMoves = state.getPossibleMoves();
				// Choosing a random
				Move randomMove = possibleMoves.get(random.nextInt(possibleMoves.size()));
				// Setting a random move as the policy for this state
				curPolicy.put(state, randomMove);
			}
		}
	}
	
	
	/**
	 * Performs policy evaluation steps until the maximum change in values is less than {@code delta}, in other words
	 * until the values under the currrent policy converge. After running this method, 
	 * the {@link PolicyIterationAgent#policyValues} map should contain the values of each reachable state under the current policy. 
	 * You should use the {@link TTTMDP} {@link PolicyIterationAgent#mdp} provided to do this.
	 *
	 * @param delta
	 */
	protected void evaluatePolicy(double delta)
	{
		boolean converged;

		do{
			converged=true;
			// Copying current policy Values
			HashMap<Game, Double> newValues =new HashMap<>(policyValues);

			for (Game state: policyValues.keySet()){
				if (state.isTerminal()){  // Terminal state have a value of 0.0
					newValues.put(state, 0.0);
					continue;
				}

				// Getting the current action for this state in the policy
				Move action = curPolicy.get(state);
				double actionValue= 0.0;

				// Getting possible transitions for the action and calculating the expected value for the action
				List<TransitionProb> transitions = mdp.generateTransitions(state, action);

				for (TransitionProb transition: transitions){
					double reward = transition.outcome.localReward; // Reward received for the transition
					Game nextState = transition.outcome.sPrime; // Next state after the transition
					double prob = transition.prob; // Probability of the transition

					// Calculating expected value based on transition probability, reward and next value
					actionValue += prob * (reward + discount * policyValues.get(nextState));
				}

				// Checking if the value difference for this state is greater than delta to determine convergence
				if (Math.abs(actionValue - policyValues.get(state))>delta){
					converged=false;
				}

				// Updating the new value for the state
				newValues.put(state, actionValue);
			}

			// Updating policy with newly computed values
			policyValues = newValues;

		} while (!converged); // Repeat until values converge within the threshold delta
		
		
	}
		
	
	
	/**This method should be run AFTER the {@link PolicyIterationAgent#evaluatePolicy} train method to improve the current policy according to 
	 * {@link PolicyIterationAgent#policyValues}. You will need to do a single step of expectimax from each game (state) key in {@link PolicyIterationAgent#curPolicy} 
	 * to look for a move/action that potentially improves the current policy. 
	 * 
	 * @return true if the policy improved. Returns false if there was no improvement, i.e. the policy already returned the optimal actions.
	 */
	protected boolean improvePolicy()
	{
		boolean policyImproved = false;

		for (Game state: policyValues.keySet()){
			if (state.isTerminal()) continue; // Skipping terminal state as do not require policy updates

			Move bestMove = null;
			double bestValue = Double.NEGATIVE_INFINITY;

			// Iterating over all possible moves from the current state to find the best move
			for (Move action : state.getPossibleMoves()){
				double actionValue = 0.0;

				// Calculating the expected value of the action by iterating over possible transitions
				List<TransitionProb> transitions = mdp.generateTransitions(state, action);

				for (TransitionProb transition: transitions){
					double reward = transition.outcome.localReward; // Reward for the transition
					Game nextState = transition.outcome.sPrime; // Resulting state of the transition
					double prob = transition.prob; // Probability of the transition

					// Accumulate the expected value based on transition probability, reward and discounted next state value
					actionValue += prob * (reward + discount * policyValues.get(nextState));

				}

				// Selecting the move with the highest expected value as the optimal action
				if (actionValue > bestValue){
					bestValue = actionValue;
					bestMove = action;
				}
			}

			// If the best move is different from the current policy, update the policy and mark it as improved
			if (bestMove != null && !bestMove.equals(curPolicy.get(state))){
				curPolicy.put(state, bestMove);
				policyImproved = true;
			}
		}
		return policyImproved;
	}
	
	/**
	 * The (convergence) delta
	 */
	double delta=0.1;
	
	/**
	 * This method should perform policy evaluation and policy improvement steps until convergence (i.e. until the policy
	 * no longer changes), and so uses your 
	 * {@link PolicyIterationAgent#evaluatePolicy} and {@link PolicyIterationAgent#improvePolicy} methods.
	 */
	public void train()
	{
		boolean policyStable;

		do {
			// Evaluating the current policy by calculating state value
			evaluatePolicy(delta);
			// Improving the policy and checking if it's stable
			policyStable= !improvePolicy();
		} while (policyStable); //Repeat until the policy does not change, indicating convergence

		// Set the final policy based on the improved policy map
		Policy finalPolicy = new Policy();
		for (Game state: curPolicy.keySet()){
			finalPolicy.setMove(state, curPolicy.get(state));
		}
		// Assigning the policy to the agent
		this.policy = finalPolicy;
	}
	
	public static void main(String[] args) throws IllegalMoveException
	{
		/**
		 * Test code to run the Policy Iteration Agent agains a Human Agent.
		 */
		PolicyIterationAgent pi=new PolicyIterationAgent();
		
		HumanAgent h=new HumanAgent();
		
		Game g=new Game(pi, h, h);
		
		g.playOut();
		
		
	}
	

}
