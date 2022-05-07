import java.util.Scanner;

public class ProofInstructionAssumption extends ProofInstruction {
    public Formula formulaToAssume;

    public ProofInstructionAssumption(Formula formulaToAssume) {
        this.formulaToAssume = formulaToAssume;
    }

    @Override
    public void applyToProof(Proof proof) throws IllegalStateException {
        proof.makeAssumption(formulaToAssume);
    }

    @Override
    public String getInstructionString() {
        //return "assume " + formulaToAssume.toString();
        return "assumption";
    }

    public static ProofInstructionAssumption parseFromScanner(Scanner input) {
        return new ProofInstructionAssumption( Formula.parseFromScanner(input) );
    }
}
