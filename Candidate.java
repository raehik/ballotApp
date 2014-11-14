package ballotApp;

public class Candidate implements Comparable<Candidate> {

	public String name;
	public int yearGroup;
	
	// we have to store each preference in a separate variable to
	// compare in case of a tie
	// pref 1 (most favourite) = prefs[0] etc.
	// (array is programatically way easier to use than separate vars, see Ballot.setVotes())
	public int[] prefs;
	
	public Candidate(String name, int yearGroup) {
		this.name = name;
		this.yearGroup = yearGroup;
	}
	
	/**
	 * Return the candidate's total points.
	 * 
	 * Total number of points is equal to the sum of the value of each preference:
	 *     preference 1 (most favourite) = 1 point
	 *     preference 5 (least favourite) = 5 points
	 *     so e.g. {2, 4, 0} = 1(2) + 2(4) + 3(0) = 2 + 8 = 10
	 * A lower point total point value is better.
	 * 
	 * @return The candidate's total points.
	 */
	public int totalPoints() {
		// Return the total points of the candidate.
		

		
		int total = 0;
		
		for (int i = 0; i < prefs.length; i++) {
			total += prefs[i] * (i + 1);
		}
		
		return total;
	}

	@Override
	/**
	 * Helper function used by Arrays.sort() to order Candidates.
	 * 
	 * The specification says that a candidate beats another if they
	 * have a lower point score. If they are equal, the candidate with
	 * the most 1st preference votes wins. If *they* are equal, it's
	 * down to the 2nd preferences votes, and so on.
	 * 
	 * We return 0 *if and only if* the candidates' scores are truly
	 * identical (i.e. exact same votes).
	 */
	public int compareTo(Candidate otherCandidate) {
		if (this.totalPoints() > otherCandidate.totalPoints()) { return 1; }
		if (this.totalPoints() < otherCandidate.totalPoints()) { return -1; }
		// point score is equal -- begin checking separate preference votes
		else {
			for (int i = 0; i < this.prefs.length; i++) {
				if (this.prefs[i] > otherCandidate.prefs[i]) { return 1; }
				if (this.prefs[i] > otherCandidate.prefs[i]) { return -1; }
				// if this level of preference votes was equal,
				// continue to next level
				// TODO: recursion instead? but we need to check total
				//       points first, so the recursion would be in a
				//       helper function
				else continue;
			}
			// if we got to the end of the preference rankings, the
			// candidates are exactly the same
			return 0;
		}
	}
}
