import java.util.ArrayList;
import java.util.Scanner;

/** Problem:
 *  Given a propositional logic formula and a proposed natural deduction proof of it, determine if the proof is valid.
 */
public class ProblemInstance {
    public Formula formula;
    public ArrayList<ProofInstruction> proofInstructions;

    public ProblemInstance() {
        formula = null;
        proofInstructions = null;
    }

    public void loadFromInput(Scanner input) throws Exception {
        formula = Formula.parseFromPrefixString( input.nextLine() );

        proofInstructions = new ArrayList<>();
        while ( input.hasNextLine() )
            proofInstructions.add( ProofInstruction.parseFromString( input.nextLine() ) );
    }

    /**
     *  Do a basic check to see if this problem has the correct format. Throws an Exception if not.
     */
    public void validate() throws Exception {}

    public void print(Logger log, LogLevel level) {
        log.print(level, "Formula: %s\n", formula.toString());
    }
}
