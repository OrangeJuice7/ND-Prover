public class Solver {
    private Logger log;

    public Solver(Logger log) {
        if (log == null)
            throw new IllegalArgumentException("`log` is null");
        this.log = log;
    }



    // Do natural deduction
    private SolutionInstance solveNoStats(ProblemInstance problem) {
        SolutionInstance sol = new SolutionInstance();
        Proof proof = new Proof();

        try {
            for (ProofInstruction instruction : problem.proofInstructions) {
                instruction.applyToProof(proof);
                log.print(LogLevel.STATUS, "%d) %s%s (%s)\n",
                        proof.formulas.size(), // line number, as understood by the proof
                        ". ".repeat( proof.assumptionIndexes.size() ), // indentation
                        proof.getLastFormula().toString(), // most recently added formula in the proof
                        instruction.getInstructionString() ); // the instruction that gave this formula
            }

            if (proof.hasAssumptions())
                throw new Exception("There are undischarged assumptions");

            if (!proof.getLastFormula().equals(problem.formula))
                throw new Exception("Conclusion is the wrong formula");
        } catch (Exception e) {
            log.print(LogLevel.WARNING, e.toString() + "\n");
            sol.isTautological = false;
            return sol;
        }

        sol.isTautological = true;
        return sol;
    }

    public SolutionInstance solve(ProblemInstance problem) {
        long startTime = System.nanoTime();

        SolutionInstance sol = solveNoStats(problem);

        long duration = System.nanoTime() - startTime;
        sol.timeTaken = duration*.000000001f;

        return sol;
    }
}
