import java.util.Scanner;

public abstract class ProofInstruction {
    public abstract void applyToProof(Proof proof) throws IllegalStateException;

    public abstract String getInstructionString();

    public static ProofInstruction parseFromScanner(Scanner input) throws Exception {
        if (!input.hasNext())
            throw new IllegalArgumentException("Scanner is empty");

        return switch (input.next().toLowerCase()) {
            case "assume" -> ProofInstructionAssumption.parseFromScanner(input);
            case "discharge" -> new ProofInstructionDischarge();
            case "rule" -> ProofInstructionApplyRule.parseFromScanner(input);
            default -> throw new IllegalArgumentException("Unrecognized proof instruction identifier");
        };
    }
    public static ProofInstruction parseFromString(String str) throws Exception {
        return parseFromScanner( new Scanner(str) );
    }
}
