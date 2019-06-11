/**
 * The application entry point.
 */
public class Main {
	public static void main(String[] args) {
		Solver solver = new Solver();
		solver.solve(8);
		System.out.println("Found " + solver.solutions.size() + " solutions in " + solver.elapsed + " after considering " + solver.boards + " boards.");
	}
}