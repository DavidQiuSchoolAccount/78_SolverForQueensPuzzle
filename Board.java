import java.util.*;

/**
 * Represents a chessboard for the n-queens puzzle.
 */
public class Board implements Cloneable {
	/**
	 * The size of the board. This is both the number of ranks and the number of files on the board.
	 */
	private int size;

	/**
	 * The current rank. The next queen will be added in this rank.
	 */
	private int rank = 0;

	/**
	 * Whether the board is in a conflict state - that is, two queens attack each other.
	 */
	private boolean conflict = false;;

	/**
	 * Indexes the file of each queen by rank. In other words, if queens[rank] == file, there is a queen
	 * located at (rank, file). Both rank and file are zero-indexed. Rank goes from top to bottom and
	 * file goes from left to right. This array is only valid for indicies below the current rank.
	 *
	 * For example, if queens is [5, 3, 0] and rank is 3, there are queens at (0, 5), (1, 3), and (2, 0).
	 */
	private int[] queens;

	/**
	 * Since each file can only contain one queen, this boolean array marks whether a file has already been
	 * filled. It is a method of efficient conflict checking.
	 */
	private boolean[] files;

	/**
	 * The diagonal arrays. A "left diagonal" moves from up-left to down-right, and a "right diagonal" moves
	 * from up-right to down-left. Since each "left diagonal" and each "right diagonal" can only contain one
	 * queen, this also allows efficient conflict checking.
	 */
	private boolean[] leftDiagonals;
	private boolean[] rightDiagonals;

	/**
	 * Constructs an empty board of a given size.
	 * @param size The number of ranks (and files) of the board.
	 */
	public Board(int size) {
		if(size < 0) { throw new IllegalArgumentException("Invalid size!"); }
		this.size = size;
		queens = new int[size];
		files = new boolean[size];

		int diagonalsCount = Math.max(size * 2 - 1, 0);
		leftDiagonals  = new boolean[diagonalsCount];
		rightDiagonals = new boolean[diagonalsCount];
	}

	/**
	 * Constructs a Board as a copy of another Board.
	 * @param other The other Board.
	 */
	public Board(Board other) {
		size = other.size;
		rank = other.rank;
		conflict = other.conflict;
		queens = other.queens.clone();
		files = other.files.clone();
		leftDiagonals = other.leftDiagonals.clone();
		rightDiagonals = other.rightDiagonals.clone();
	}

	/**
	 * Adds a new queen to the current rank in the given file.
	 * @param file A file number, from 0 to getSize() - 1.
	 */
	public void add(int file) {
		if(file >= size) { throw new IllegalArgumentException("Invalid file number!"); }
		else if(rank >= size) { throw new IllegalStateException("Cannot add a queen when the board is full!"); }
		else if(conflict) { throw new IllegalStateException("Cannot add a queen when the board is in a conflict state!"); }

		int leftDiagonal  = getLeftDiagonal(rank, file);
		int rightDiagonal = getRightDiagonal(rank, file);

		if(files[file] || leftDiagonals[leftDiagonal] || rightDiagonals[rightDiagonal]) {
			conflict = true;
		}

		queens[rank] = file;
		files[file] = leftDiagonals[leftDiagonal] = rightDiagonals[rightDiagonal] = true;
		rank++;
	}

	/**
	 * Removes the most recently added queen. The state of the board is reset as if the insertion never occurred.
	 */
	public void undo() {
		if(rank <= 0) { throw new IllegalStateException("No moves to undo!"); }

		int file = queens[rank];
		int leftDiagonal  = getLeftDiagonal(rank, file);
		int rightDiagonal = getRightDiagonal(rank, file);

		files[file] = leftDiagonals[leftDiagonal] = rightDiagonals[rightDiagonal] = false;
		conflict = false;
		rank--;
	}

	@Override
	public String toString() {
		String result = "";
		if(size <= 0) { result += "No board.\n"; }
		else {
			String rowSeparator = "\n+";
			for(int i = 0; i < size; i++) {
				rowSeparator += "---+";
			} rowSeparator += "\n";

			result += "Board:" + rowSeparator;
			for(int i = 0; i < size; i++) {
				int file = queens[i];
				result += "|";

				for(int j = 0; j < size; j++) {
					result += (i < rank && j == file) ? " Q |" : "   |";
				} result += rowSeparator;
			}
		}

		result += "Size: " + size + "\n";
		result += "Current rank: " + rank + "\n";
		result += (isSolved() ? "SOLVED!" : "Unsolved.") + "\n";
		result += (conflict ? "CONFLICT!" : "No conflicts.") + "\n";
		return result;
	}

	@Override
	public Board clone() {
		return new Board(this);
	}

	/**
	 * Returns whether the current board state is a solution. All ranks must be filled, and there cannot
	 * be any conflicts.
	 */
	public boolean isSolved() {
		return rank >= size && !conflict;
	}

	/**
	 * Returns whether the last-added queen introduced a conflict.
	 */
	public boolean hasConflict() {
		return conflict;
	}

	/**
	 * Returns a copy of the internal queens array.
	 */
	public int[] getQueensArray() {
		return queens.clone();
	}

	/**
	 * Returns the current rank – the one the next queen will be added to.
	 */
	public int getCurrentRank() {
		return rank;
	}

	/**
	 * Returns the size of the board.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Calculates the "left diagonal number" of a square. The numbers are arranged like so:
	 *
	 *     0 1 2 3
	 *   +---------+
	 * 0 | 3 2 1 0 |
	 * 1 | 4 3 2 1 |
	 * 2 | 5 4 3 2 |
	 * 3 | 6 5 4 3 |
	 *   +---------+
	 */
	private int getLeftDiagonal(int rank, int file) {
		return rank - file + size - 1;
	}

	/**
	 * Calculates the "right diagonal number" of a square. The numbers are arranged like so:
	 *
	 *     0 1 2 3
	 *   +---------+
	 * 0 | 0 1 2 3 |
	 * 1 | 1 2 3 4 |
	 * 2 | 2 3 4 5 |
	 * 3 | 3 4 5 6 |
	 *   +---------+
	 */
	private int getRightDiagonal(int rank, int file) {
		return rank + file;
	}
}