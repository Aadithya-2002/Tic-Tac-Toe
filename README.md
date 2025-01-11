## Tic-Tac-Toe AI Agents: Value Iteration, Policy Iteration, and Q-Learning
This project implements three AI agents—Value Iteration, Policy Iteration, and Q-Learning—to play Tic-Tac-Toe on a 3x3 grid. Each agent learns strategies based on Markov Decision Processes (MDPs) and Reinforcement Learning principles. The agents are tested against opponents with different play styles (Random, Aggressive, Defensive), and the results are analyzed.

## Table of Contents
1. Introduction
2. Features
3. Installation
4. How to Run
5. Agent Details
* Value Iteration Agent
* Policy Iteration Agent
* Q-Learning Agent
6. Testing and Results
7. Contributing

## Introduction
This project demonstrates AI-based solutions to Tic-Tac-Toe using:

* Value Iteration: A dynamic programming technique that iteratively calculates the value of states.
* Policy Iteration: Alternates between policy evaluation and policy improvement until convergence.
* Q-Learning: A reinforcement learning algorithm that trains an agent through exploration and exploitation.
The goal is to design agents that can compete effectively and learn optimal strategies.

## Features
* Implements three AI strategies: Value Iteration, Policy Iteration, and Q-Learning.
* Provides an interactive testing environment to play against AI agents.
* Supports evaluation against Random, Aggressive, and Defensive opponent agents.
* Generates comprehensive reports for each agent's performance.

## Installation
1. Clone the repository:

```bash

git clone https://github.com/your-username/tictactoe-ai-agents.git
cd tictactoe-ai-agents
```
2. Build the project using Maven:

```bash
mvn clean install
```
3. Ensure Java (JDK 8 or later) and Maven are installed.

## How to Run
# Playing Against an AI Agent
Run the following command to play against a specified agent:

```bash
java -cp target/classes ticTacToe.Game -x <x-agent> -o <o-agent> -s <starter>
```
* x-agent: The agent playing as X (vi, pi, ql, or human).
* o-agent: The agent playing as O (random, aggressive, defensive, or human).
* starter: Specifies who starts the game (x or o).
Examples:
* X as Value Iteration agent vs. O as Random agent:
```bash
java -cp target/classes ticTacToe.Game -x vi -o random -s x
```
* X as Q-Learning agent vs. O as Aggressive agent:
```bash
java -cp target/classes ticTacToe.Game -x ql -o aggressive -s o
```
## Agent Details
# Value Iteration Agent
* File: ValueIterationAgent.java
* Key Methods:
  * iterate(): Performs value iteration over a set number of steps.
  * extractPolicy(): Derives an optimal policy based on state values.
* Testing:
  * Command: java -cp target/classes ticTacToe.Game -x vi -o random -s x
  * Results documented in vi-agent-report.pdf.
# Policy Iteration Agent
* File: PolicyIterationAgent.java
* Key Methods:
  * initRandomPolicy(): Initializes a random policy.
  * evaluatePolicy(): Evaluates the policy until state values converge.
  * improvePolicy(): Updates the policy to maximize expected rewards.
* Testing:
  * Command: java -cp target/classes ticTacToe.Game -x pi -o aggressive -s x
  * Results documented in pi-agent-report.pdf.
# Q-Learning Agent
* File: QLearningAgent.java
* Key Methods:
  * train(): Trains the agent using an ε-greedy strategy and updates Q-values.
  * extractPolicy(): Extracts the learned policy from the Q-Table.
* Testing:
  * Command: java -cp target/classes ticTacToe.Game -x ql -o defensive -s x
  * Results documented in ql-agent-report.pdf.
  
## Testing and Results
Each agent is tested against:

* Random, Aggressive, and Defensive opponents.
*Performance metrics:
  * Wins, Losses, Draws over 50 games for each opponent.
* Reports:
  * Results for each agent are summarized in PDFs (vi-agent-report.pdf, pi-agent-report.pdf, ql-agent-report.pdf).

Contributing
We welcome contributions to enhance the project. To contribute:

1. Fork the repository.
2. Create a feature branch:
```bash
git checkout -b feature-name
```
3. Commit your changes:
```bash
git commit -m "Add feature-name"
```
4. Push to your fork:
```bash
git push origin feature-name
```
5. Create a pull request.
