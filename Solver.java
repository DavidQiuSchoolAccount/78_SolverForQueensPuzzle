import java.util.*;

/**
 * Solves the n-queens problem with recursive backtracking.
 *
 * To run the algorithm, call solve(). Then, inspect the results in "solutions", "boards", and "elapsed".
 */
public class Solver {
	/**
	 * Contains all solutions. Each solution is in the format of a Board.queens array.
	 */
	public List<int[]> solutions = new ArrayList<>();

	/**
	 * The runtime of the algorithm, in seconds.
	 */
	public double elapsed;

	/**
	 * The number of boards considered. Includes illegal positions.
	 */
	public long boards;

	/**
	 * The Board that is being operated on.
	 */
	private Board board;

	/**
	 * Executes the solving algorithm.
	 *
	 * @param size The size of the board.
	 */
	public void solve(int size) {
		long start = System.nanoTime();
		solutions.clear();
		board = new Board(size);
		boards = 0;

		_solve();
		elapsed = (System.nanoTime() - start) / 1000000000.0;
	}

	/**
	 * The heart of the recursive algorithm. This method requires "boards" to contain a position where ranks 0 to
	 * the current rank are filled with a valid configuration of queens. It then solves the current rank by making
	 * attempts to place a queen in all possible files in that rank. Some of those positions will result in a valid
	 * setup; the algorithm will continue searching from there.
	 */
	private void _solve() {
		if(board.isSolved()) {
			solutions.add(board.getQueensArray());
			return;
		}

		for(int i = 0; i < board.getSize(); i++) {
			boards++;
			boolean valid = board.place(i);
			if(valid) {
				_solve();
				board.undo();
			}
		}
	}
}