package ballotApp;

import java.util.Arrays;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Ballot {
	
	public Candidate[] candidates;
	
	// tracks year group of current election
	private int yearGroup;

	private Scanner scanner;
	
	public Ballot() {
		scanner = new Scanner(System.in);
	}
	
	private String getLine(String prompt) {
		boolean badInput = false;
		String line;
		
		do {
			badInput = false;
			
			System.out.print(prompt);
			line = scanner.nextLine();
			
			if (line.isEmpty()) {
				System.out.println("ERROR: nothing entered");
				badInput = true;
			}
		} while (badInput);
		
		return line;
	}
	
	/**
	 * Try to get a positive integer from stdin.
	 * 
	 * If an integer is not entered,
	 * @return	the number entered. If an integer is not entered,
	 * 			return -2. If it is not positive, return -1.
	 */
	private int getPositiveInt() {		
		int num;
		
		try {
			num = new Scanner(System.in).nextInt();
		} catch (InputMismatchException e) {
			return -2;
		}
		
		if (num < 0) { return -1; } else { return num; }
	}
	
	/**
	 * Force a positive integer to be entered and return it.
	 * 
	 * @param prompt	The string to display before asking for input.
	 * 					No newlines nor spaces are appended.
	 * @return			The (valid) integer entered.
	 */
	private int promptPositiveInt(String prompt) {		
		boolean badInput;
		int num;
		
		do {
			badInput = false;
			
			System.out.print(prompt);
			num = this.getPositiveInt();
			
			if (num == -2) {
				System.out.println("ERROR: not an integer");
				badInput = true;
			} else if (num == -1) {
				System.out.println("ERROR: not a positive integer");
				badInput = true;
			}
		} while (badInput);
		
		return num;
	}
	
	/**
	 * Set the candidates for the next election.
	 * 
	 * Note: this does not *add* candidates to the current list.
	 * The list of candidates is rewritten (as this would
	 * be used after every election to do another one).
	 */
	public void setCandidates(int numOfCandidates) {
		this.candidates = new Candidate[numOfCandidates];
		
		this.yearGroup = this.promptPositiveInt("Candidate year group: ");
		
		for (int i = 0; i < numOfCandidates; i++) {
			// prompt for name
			String name = this.getLine("Candidate " + (i + 1) + "/" + numOfCandidates + " name: ");
			
			this.candidates[i] = new Candidate(name, yearGroup);
			this.candidates[i].prefs = new int[numOfCandidates];
		}
	}
	
	private String paperise(String string) {
		return "| " + string;
	}
	
	public void makeVote() {
		/** Do a ballot paper. */
		
		// some fun ascii art
		String ballotPaperArt = ""
				+ "\n------------------------------------"
				+ "\n|           BALLOT PAPER"
				+ "\n|             YEAR " + this.yearGroup
				+ "\n|";
		System.out.println(ballotPaperArt);
		this.voteSystem2();
	}
	
	/**
	 * Vote using input type 1.
	 * 
	 * I guess this method isn't entirely clear, but I prefer it.
	 * This input method asks for your preferences in order and takes
	 * input in the form of a number which corresponds to a candidate
	 * on the ballot paper. It's a bit backwards and requires some
	 * slightly different thinking in the programming, but isn't that
	 * what you'd do IRL: mark your first preference first, followed by
	 * your second and third... ? ¯\_(ツ)_/¯
	 */
	public void voteSystem1() {
		// print candidate names & associated number
		for (int i = 0; i < this.candidates.length; i++) {
			System.out.println(this.paperise((i + 1) + ". [ ] " + this.candidates[i].name));
		}
		System.out.println(this.paperise(""));
		
		// instructions because not the clearest input system immediately
		System.out.println(this.paperise("Please enter the number of the associated candidate for each of your preferences."));
		System.out.println(this.paperise(""));
		
		// Maps choices to candidates based on the order of this.candidates.
		// Position is points, value is candidate.
		// i.e. given {4, 2, 1, 3, 5}, the 4th candidate on the paper would be the favourite.
		// we load them into an array to give them a chance to change their mind later
		int[] choices = new int[this.candidates.length];
		
		for (int i = 0; i < this.candidates.length; i++) {
			choices[i] = this.promptPositiveInt("| Preference " + (i + 1) + ": ") - 1;
		}
		
		// TODO: show the current state
		// TODO: later: could be all ascii art-ified :D
		
		// TODO: check whether they're okay with
		
		for (int i = 0; i < this.candidates.length; i++) {
			this.candidates[choices[i]].prefs[i] += 1;
			//System.out.println(this.candidates[choices[i]].name + " ranked as " + (i + 1));
		}
	}
	
	/**
	 * Vote using input type 2.
	 * 
	 * Input type 1 feels easier to do IMHO, but I'm a programmer, so
	 * I shouldn't assume to know anything about my clients ;)
	 * This input method prints each candidate and asks for your
	 * preference.
	 */
	public void voteSystem2() {
		// print candidate names
		for (int i = 0; i < this.candidates.length; i++) {
			System.out.println(this.paperise("[ ] " + this.candidates[i].name));
		}
		System.out.println(this.paperise(""));
		
		System.out.println(this.paperise("Please enter your preference for each of the candidates."));
		System.out.println(this.paperise(""));
		
		// Position is candidate, value is their rank.
		// i.e. given {4, 2, 1, 3, 5}, the 4th candidate on the paper
		// would be the 3rd ranked.
		// we load them into an array to give them a chance to change
		// their mind later
		int[] choices = new int[this.candidates.length];
		
		for (int i = 0; i < this.candidates.length; i++) {
			choices[i] = this.promptPositiveInt(this.paperise("Cand. " + (i + 1) + ", " + this.candidates[i].name + ": ")) - 1;
		}
		
		// TODO: show the current state
		// TODO: later: could be all ascii art-ified :D
		
		// TODO: check whether they're okay with
		
		for (int i = 0; i < this.candidates.length; i++) {
			this.candidates[i].prefs[choices[i]] += 1;
			//System.out.println(this.candidates[i].name + " ranked as " + (choices[i] + 1));
		}
	}
	
	public void printResults() {
		for (int i = 0; i < this.candidates.length; i++) {
			System.out.println(this.candidates[i].name + " points: " + this.candidates[i].totalPoints());
		}
	}
	
	public void endElection() {
		// TODO: functionise??
		// sort by point score, low -> high
		Arrays.sort(this.candidates, Collections.reverseOrder());
		// because we're only finding the winner, only check the first
		// 2 for being identical
		if (this.candidates[0].compareTo(this.candidates[1]) == 0) {
			System.out.println("Draw! Re-election time!");
			return;
		}
		this.printResults();
		System.out.println("The winner for Year " + this.yearGroup + " is... " + this.candidates[0].name + ", with " + this.candidates[0].totalPoints() + " points!");
	}
	
	private void runTests() {
		this.setCandidates(5);
		this.makeVote();
		this.makeVote();
		this.endElection();
	}
	
	public static void main(String[] args) {
		Ballot test = new Ballot();
		test.runTests();
	}

}
