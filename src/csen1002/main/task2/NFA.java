package csen1002.main.task2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * Write your info here
 * 
 * @name Ahmed Tamer
 * @id 43-2117
 * @labNumber 11
 */
public class NFA {
	/**
	 * NFA constructor
	 * 
	 * @param description is the string describing a NFA
	 */

	HashMap<Integer, State> NFAStates;
	HashMap<Integer, DFAState> DFAStates;
	DFAState currentState;
	int numberOfStates;
	int currentIndex;

	public NFA(String description) {
		// TODO Write Your Code Here
		this.numberOfStates = 0;
		this.currentIndex = 0;
		String[] parts = description.split("#");
		String zeroTransData = parts[0];
		String oneTransData = parts[1];
		String epsilonTransData = parts[2];
		String acceptStatesData = parts[3];

		String[] zeroTransitions = zeroTransData.split(";");
		String[] oneTransitions = oneTransData.split(";");
		String[] epsilonTransitions = epsilonTransData.split(";");
		String[] acceptStates = acceptStatesData.split(",");

		HashMap<Integer, State> map = new HashMap<Integer, State>();

		for (int i = 0; i < zeroTransitions.length; i++) {
			String[] trans = zeroTransitions[i].split(",");

			State state = map.get(Integer.parseInt(trans[0]));
			State state2 = map.get(Integer.parseInt(trans[1]));

			if (state == null) {
				state = new State(Integer.parseInt(trans[0]));
				map.put(Integer.parseInt(trans[0]), state);
			}

			if (state2 == null) {
				state2 = new State(Integer.parseInt(trans[1]));
				map.put(Integer.parseInt(trans[1]), state2);
			}

			state = map.get(Integer.parseInt(trans[0]));
			state.zeroTransition.add(Integer.parseInt(trans[1]));
		}

		for (int i = 0; i < oneTransitions.length; i++) {
			String[] trans = oneTransitions[i].split(",");

			State state = map.get(Integer.parseInt(trans[0]));
			State state2 = map.get(Integer.parseInt(trans[1]));

			if (state == null) {
				state = new State(Integer.parseInt(trans[0]));
				map.put(Integer.parseInt(trans[0]), state);
			}

			if (state2 == null) {
				state2 = new State(Integer.parseInt(trans[1]));
				map.put(Integer.parseInt(trans[1]), state2);
			}

			state = map.get(Integer.parseInt(trans[0]));
			state.oneTransition.add(Integer.parseInt(trans[1]));
		}

		for (int i = 0; i < epsilonTransitions.length; i++) {
			String[] trans = epsilonTransitions[i].split(",");

			State state = map.get(Integer.parseInt(trans[0]));
			State state2 = map.get(Integer.parseInt(trans[1]));

			if (state == null) {
				state = new State(Integer.parseInt(trans[0]));
				map.put(Integer.parseInt(trans[0]), state);
			}

			if (state2 == null) {
				state2 = new State(Integer.parseInt(trans[1]));
				map.put(Integer.parseInt(trans[1]), state2);
			}

			state = map.get(Integer.parseInt(trans[0]));
			state.epsilonTransitions.add(Integer.parseInt(trans[1]));

		}

		for (int i = 0; i < acceptStates.length; i++) {
			State state = map.get(Integer.parseInt(acceptStates[i]));
			state.isAcceptState = true;

		}

		this.NFAStates = map;
		this.DFAStates = new HashMap<Integer, DFAState>();

		this.changeToDFA();

//		this.displayDFA();
//		
//		System.out.println();
//		
//		this.displayNFA();
	}

	public void changeToDFA() {
		State state = NFAStates.get(0);
		DFAState dfaState = new DFAState(this.numberOfStates);
		dfaState.stateNumbers.add(state.stateNumber);

//		S<DFAState> stack = new Stack()<DFAState>();
		ArrayList<DFAState> stack = new ArrayList<DFAState>();
		stack.add(dfaState);

		while (!stack.isEmpty()) {

			dfaState = stack.remove(0);
			HashSet<Integer> dfaStateNumbers = dfaState.stateNumbers;

			while (true) {
				boolean hasChanged = false;

				HashSet<Integer> temp = new HashSet<Integer>();

				for (int stateNumber : dfaStateNumbers) {
					State checkState = NFAStates.get(stateNumber);
					temp.add(stateNumber);
					for (int epsilonTrans : checkState.epsilonTransitions) {
						if (!dfaStateNumbers.contains(epsilonTrans)) {
//							newStateData.add(epsilonTrans);
							temp.add(epsilonTrans);

							hasChanged = true;
						}
					}

				}

				dfaStateNumbers = temp;

				if (!hasChanged) {
					break;
				}
			}

			dfaState.stateNumbers = dfaStateNumbers;
			HashSet<Integer> newStateData = new HashSet<Integer>();

			this.DFAStates.put(this.currentIndex, dfaState);

			// Loop for zero transition
			for (int dfaStateNumber : dfaStateNumbers) {
				state = NFAStates.get(dfaStateNumber);

				for (int zeroTrans : state.zeroTransition) {
					newStateData.add(zeroTrans);
				}
//				newStateData.add(state.stateNumber);

//				for(int epsilonTrans: state.epsilonTransitions) {
//					newStateData.add(epsilonTrans);
//				}

				while (true) {
					boolean hasChanged = false;

					HashSet<Integer> temp = new HashSet<Integer>();

					for (int stateNumber : newStateData) {
						State checkState = NFAStates.get(stateNumber);
						temp.add(stateNumber);
						for (int epsilonTrans : checkState.epsilonTransitions) {
							if (!newStateData.contains(epsilonTrans)) {
//								newStateData.add(epsilonTrans);
								temp.add(epsilonTrans);

								hasChanged = true;
							}
						}

					}

					newStateData = temp;

					if (!hasChanged) {
						break;
					}
				}
//				DFAState newStateOne = new DFAState();
//				newStateOne.setStateNumbers(newStateData);;
//				dfaState.zeroTransition = newStateOne.stateID;
//				
//				stack.add(newStateOne);

			}

//			dfaState.stateNumbers = newStateData;

			int sID = -1;
			DFAState reqState = null;
			for (DFAState x : DFAStates.values()) {
				if (x.isEqual(newStateData)) {
					reqState = x;
				}

			}

			if (reqState == null) {
				for (DFAState stackDFA : stack) {

					if (stackDFA.isEqual(newStateData)) {
						reqState = stackDFA;
					}
				}
			}

			if (reqState == null) {
				this.numberOfStates++;
				DFAState newStateZero = new DFAState(this.numberOfStates);
				newStateZero.setStateNumbers(newStateData);
				;
				dfaState.zeroTransition = newStateZero.stateID;

				stack.add(newStateZero);
			} else {
//				DFAState TransitionToState = DFAStates.get(sID);
				dfaState.zeroTransition = reqState.stateID;
			}

			newStateData = new HashSet<Integer>();

			// Loop for one transition
			for (int dfaStateNumber : dfaStateNumbers) {
				state = NFAStates.get(dfaStateNumber);

				for (int oneTrans : state.oneTransition) {
					newStateData.add(oneTrans);
				}

				while (true) {
					boolean hasChanged = false;
					HashSet<Integer> temp = new HashSet<Integer>();

					for (int stateNumber : newStateData) {
						State checkState = NFAStates.get(stateNumber);
						temp.add(stateNumber);

						for (int epsilonTrans : checkState.epsilonTransitions) {
							if (!newStateData.contains(epsilonTrans)) {
//								newStateData.add(epsilonTrans);
								temp.add(epsilonTrans);

								hasChanged = true;
							}
						}

					}

					newStateData = temp;

					if (!hasChanged) {
						break;
					}
				}

			}

//			sID = -1;
			reqState = null;
			for (DFAState x : DFAStates.values()) {
				if (x.isEqual(newStateData)) {
//					sID = x.stateID;
					reqState = x;
				}
			}

			if (reqState == null) {
				for (DFAState stackDFA : stack) {
//					if(stackDFA.stateNumbers.size() != newStateData.size()) {
//						break;
//					}

					if (stackDFA.isEqual(newStateData)) {
//						sID = stackDFA.stateID;
						reqState = stackDFA;
					}
				}
			}

			if (reqState == null) {
				this.numberOfStates++;
				DFAState newStateOne = new DFAState(this.numberOfStates);
				newStateOne.setStateNumbers(newStateData);
				;
				dfaState.oneTransition = newStateOne.stateID;

				stack.add(newStateOne);
			} else {
//				DFAState TransitionToState = DFAStates.get(sID);
				dfaState.oneTransition = reqState.stateID;
			}

			boolean isAccept = false;
			for (int num : dfaState.stateNumbers) {
				State checkAcceptState = NFAStates.get(num);
				if (checkAcceptState.isAcceptState) {
					isAccept = true;
					break;
				}

			}
			dfaState.isAcceptState = isAccept;
			this.DFAStates.replace(this.currentIndex, dfaState);
			this.currentIndex++;
		}

		this.currentState = this.DFAStates.get(0);
	}

	public void displayNFA() {
		for (State value : NFAStates.values()) {
			System.out.println(value);
			System.out.println();
		}
	}

	public void displayDFA() {
		for (DFAState value : DFAStates.values()) {
			System.out.println(value);
			System.out.println();
		}
	}

	/**
	 * Returns true if the string is accepted by the NFA and false otherwise.
	 * 
	 * @param input is the string to check by the NFA.
	 * @return if the string is accepted or not.
	 */
	public boolean run(String input) {
		// TODO Write Your Code Here
//		return false;
		for (int i = 0; i < input.length(); i++) {
			int x = Integer.parseInt("" + input.charAt(i));
			int transition;

			if (x == 0) {
				transition = this.currentState.zeroTransition;
			} else {
				transition = this.currentState.oneTransition;
			}

			this.currentState = this.DFAStates.get(transition);
		}

		if (this.currentState.isAcceptState) {
			return true;
		}

		return false;
	}

	public static void main(String[] args) {
//		NFA x = new NFA("0,0;1,2;3,3#0,0;0,1;2,3;3,3#1,2#3");
		NFA x = new NFA("2,3#4,5;7,8#0,1;0,7;1,2;1,4;3,6;5,6;6,1;6,7#8");
//		NFA x = new NFA("2,3#4,5;7,8#0,1;0,7;1,2;1,4;3,6;5,6;6,1;6,7#8");

//		x.displayDFA();
		System.out.println(x.run("01010101"));
	}
}

class State {

	int stateNumber;
	ArrayList<Integer> zeroTransition;
	ArrayList<Integer> oneTransition;
	ArrayList<Integer> epsilonTransitions;

	boolean isAcceptState;

	public State(int stateNumber) {
		this.stateNumber = stateNumber;
		this.zeroTransition = new ArrayList<Integer>();
		this.oneTransition = new ArrayList<Integer>();
		this.epsilonTransitions = new ArrayList<Integer>();
		this.isAcceptState = false;

		this.epsilonTransitions.add(stateNumber);
	}

	public String toString() {

		String str = "State: " + this.stateNumber + "\nZeroTrans: ";

		for (int i = 0; i < this.zeroTransition.size(); i++) {
			str += this.zeroTransition.get(i) + ", ";
		}

		str += "\nOneTrans: ";

		for (int i = 0; i < this.oneTransition.size(); i++) {
			str += this.oneTransition.get(i) + ", ";
		}

		str += "\nEpsilonTrans: ";

		for (int i = 0; i < this.epsilonTransitions.size(); i++) {
			str += this.epsilonTransitions.get(i) + ", ";
		}

		return str;
	}

}

class DFAState {

//	ArrayList<Integer> stateNumbers;
	HashSet<Integer> stateNumbers;
	boolean isAcceptState;

	int zeroTransition;
	int oneTransition;

//	static int id = 0;
	int stateID;

	public DFAState(int id) {
		this.stateNumbers = new HashSet<Integer>();
		this.isAcceptState = false;
		stateID = id;
	}

	public void setStateNumbers(HashSet<Integer> stateNumbers) {
		this.stateNumbers = stateNumbers;
	}

	public boolean isEqual(HashSet<Integer> set) {

		if (set.size() != this.stateNumbers.size()) {
			return false;
		}

		for (int x : set) {
			if (!this.stateNumbers.contains(x)) {
//				System.out.println(false);
				return false;
			}
		}

		return true;
	}

	public String toString() {

		String str = "ID: " + this.stateID + "\nState: ";
		for (int i : this.stateNumbers) {
			str += i + ", ";
		}

		str += "\nZeroTrans: " + this.zeroTransition;

		str += "\nOneTrans: " + this.oneTransition;

		str += "\nIsAccept: " + this.isAcceptState;

		return str;
	}

}