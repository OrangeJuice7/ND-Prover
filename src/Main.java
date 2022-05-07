import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.function.Function;

public class Main {
    public static Logger mainLog = new Logger(LogLevel.STATUS);

    private static Scanner getScannerFromFile(String filename) throws FileNotFoundException {
        File inputFile = new File(filename);
        return new Scanner(inputFile);
    }
    private static Scanner getScannerFromUser(String fileDesc) {
        // Declare a Scanner object to read input
        Scanner input = new Scanner(System.in);

        // Prompt for an input file. Keep trying until a correct file is specified
        while (true) {
            try {
                System.out.println("Specify " + fileDesc + " filename: ");
                return getScannerFromFile( input.next() );
            } catch(FileNotFoundException e) {
                System.out.println("File not found. (Mistyped filename?)");
                System.out.println(e);
                //e.printStackTrace(); // More verbose
            }
        }
    }

    private static void loadProblemFromFile(ProblemInstance problem, String filename) throws Exception {
        Scanner fileInput = getScannerFromFile(filename);
        problem.loadFromInput(fileInput);
    }
    private static void loadProblemFromUser(ProblemInstance problem) throws Exception {
        Scanner fileInput = getScannerFromUser("problem");
        problem.loadFromInput(fileInput);
    }

    private static void loadSolutionFromFile(SolutionInstance sol, String filename) throws Exception {
        Scanner fileInput = getScannerFromFile(filename);
        sol.loadFromInput(fileInput);
    }
    private static void loadSolutionFromUser(SolutionInstance sol) {
        Scanner fileInput = getScannerFromUser("solution");
        sol.loadFromInput(fileInput);
    }



    /** Tests whether the given solver can produce the correct solution for the given problem.
     *  @param problem The problem. Test will be rejected if this is null.
     *  @param solver The solver. Test will be rejected if this is null.
     *  @param correctSolution The correct solution. Can be null; then the solver's solution will not compare against this.
     *  @return The solution that the solver gave. May be null if there is some diagnostic error.
     */
    public static SolutionInstance testProblem(
            ProblemInstance problem,
            Solver solver,
            SolutionInstance correctSolution) {

        // Validate the parameters
        if (problem == null) {
            mainLog.print(LogLevel.WARNING, "WARNING: `problem` is null. Skipping test\n");
            return null;
        }
        if (solver == null) {
            mainLog.print(LogLevel.WARNING, "WARNING: `solver` is null. Skipping test\n");
            return null;
        }

        // Check problem for errors
        try {
            problem.validate();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        // Print problem stats
        problem.print(mainLog, LogLevel.STATUS);

        // Solve
        mainLog.print(LogLevel.STATUS, "Solving...\n");
        SolutionInstance sol = solver.solve(problem);

        // Print solution
        sol.print(mainLog, LogLevel.STATUS);
        sol.printTimeTaken(mainLog, LogLevel.STATUS);

        // Check solution for errors
        mainLog.print(LogLevel.STATUS,"Checking solution...\n");
        try {
            sol.validate(problem);
            if (correctSolution != null)
                sol.compare(correctSolution);

            mainLog.print(LogLevel.STATUS,"Correct\n");
        } catch (Exception e) {
            System.out.println(e);
        }

        return sol;
    }

    public static void batchTestFromFiles(
            Solver solver,
            final int NUM_OF_TEST_CASES,
            Function<Integer,String> problemFilenameGenerator,
            Function<Integer,String> solutionFilenameGenerator) {

        ProblemInstance problem = new ProblemInstance();
        SolutionInstance correctSolution = new SolutionInstance();

        float totalTimeTaken = 0f;

        mainLog.print(LogLevel.STATUS,"Checking test cases...\n");

        for (int i = 1; i <= NUM_OF_TEST_CASES; ++i) {
            mainLog.print(LogLevel.STATUS,"\nRunning test case %d/%d\n", i, NUM_OF_TEST_CASES);

            String problemFilename = problemFilenameGenerator.apply(i);
            String solutionFilename = solutionFilenameGenerator.apply(i);

            try {
                loadProblemFromFile(problem, problemFilename);
                loadSolutionFromFile(correctSolution, solutionFilename);
            } catch (Exception e) {
                System.out.println(e);
                continue;
            }

            SolutionInstance sol = testProblem(problem, solver, correctSolution);
            totalTimeTaken += sol.timeTaken;
        }

        float avgTimeTaken = totalTimeTaken / NUM_OF_TEST_CASES;

        mainLog.print(LogLevel.STATUS,"\nBatch test completed.\n");
        mainLog.print(LogLevel.STATUS,"Total time taken: %.4f s\n", totalTimeTaken);
        mainLog.print(LogLevel.STATUS,"Avg time taken: %.4f s\n", avgTimeTaken);
        mainLog.print(LogLevel.STATUS,"\n");
    }



    private enum ProgramMode {
        DIRECT_INPUT,
        LOAD_FROM_USER
    }
    public static void main(String[] args) {
        final ProgramMode PROGRAM_MODE = ProgramMode.LOAD_FROM_USER;

        System.out.println("--== AUTOMATIC ND PROVER ==--");

        System.out.println("Inference rules:");
        for (InferenceRule rule : InferenceRule.rules.values()) {
            System.out.println( rule.getDisplayString() );
        }

        //mainLog.setLevel(LogLevel.WARNING);
        batchTestFromFiles( new Solver(mainLog),
                10,
                i -> "tests/in/" + i + ".in",
                i -> "tests/out/" + i + ".out");

        switch (PROGRAM_MODE) {
            case DIRECT_INPUT -> System.out.println("Mode: Input problem directly");
            case LOAD_FROM_USER -> System.out.println("Mode: Load problem from user-specified file");
        }

        // Load/generate problem
        ProblemInstance problem = new ProblemInstance();
        try {
            switch (PROGRAM_MODE) {
                case DIRECT_INPUT:
                    problem.loadFromInput( new Scanner(System.in) );
                    break;
                case LOAD_FROM_USER:
                    loadProblemFromUser(problem); // tests/in/1.in
                    break;
            }
        } catch (Exception e) {
            System.out.println(e);
            return;
        }

        // Load solution if applicable
        SolutionInstance correctSolution = null;
        if (PROGRAM_MODE == ProgramMode.LOAD_FROM_USER) {
            correctSolution = new SolutionInstance();
            loadSolutionFromUser(correctSolution); // tests/out/1.out
        }

        // Test the problem
        mainLog.setLevel(LogLevel.STATUS);
        testProblem(problem, new Solver(mainLog), correctSolution);
    }
}
