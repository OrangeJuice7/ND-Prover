import java.util.Scanner;

public class SolutionInstance {
    public boolean isTautological;

    // Statistics
    float timeTaken; // in seconds

    public void loadFromInput(Scanner input) {
        isTautological = input.nextBoolean();
    }

    /**
     *  Do a basic check to see if this solution has the correct format for the problem given.
     *  Does not guarantee that this solution actually satisfies the problem.
     */
    public void validate(ProblemInstance problem) throws Exception {}

    /**
     *  Checks that this solution is the same as the given solution.
     *  If there is a difference, this throws an exception specifying where the difference is.
     */
    public void compare(SolutionInstance other) throws Exception {
        if (this.isTautological != other.isTautological)
            throw new Exception("Conclusion is incorrect");
    }

    public void print(Logger log, LogLevel level) {
        log.print(level, "Is tautological: %b\n", isTautological);
    }
    public void printTimeTaken(Logger log, LogLevel level) {
        log.print(level, "Time taken: %.4f s\n", timeTaken);
    }
}
