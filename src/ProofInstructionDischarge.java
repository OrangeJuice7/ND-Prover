import java.util.Scanner;

public class ProofInstructionDischarge extends ProofInstruction {
    @Override
    public void applyToProof(Proof proof) throws IllegalStateException {
        proof.dischargeAssumption();
    }

    @Override
    public String getInstructionString() {
        return "discharge";
    }
}
