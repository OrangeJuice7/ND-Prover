import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ProofInstructionApplyRule extends ProofInstruction {
    public InferenceRule rule;
    public ArrayList<Integer> premiseIndexes;
    public Map<String, Formula> substitutionMap; // substitute the propositions in the rule for the formula specified here

    public ProofInstructionApplyRule(InferenceRule rule, ArrayList<Integer> premiseIndexes, Map<String, Formula> substitutionMap) {
        this.rule = rule;
        this.premiseIndexes = premiseIndexes;
        this.substitutionMap = substitutionMap;
    }

    @Override
    public void applyToProof(Proof proof) throws IllegalStateException {
        proof.applyInferenceRule(rule, premiseIndexes, substitutionMap);
    }

    @Override
    public String getInstructionString() {
        return rule.name
                + " from premises " + premiseIndexes.toString()
                + " with substitutions " + substitutionMap.toString();
    }

    public static ProofInstructionApplyRule parseFromScanner(Scanner input) throws Exception {
        if (!input.hasNext())
            throw new Exception("input does not specify a valid inference rule name");

        String ruleName = input.next();
        if (!InferenceRule.rules.containsKey(ruleName))
            throw new Exception("Specified inference rule name \"" + ruleName + "\" does not refer to a valid rule");

        InferenceRule rule = InferenceRule.rules.get(ruleName);

        ArrayList<Integer> premiseIndexes = new ArrayList<>();
        for (int i = 0; i < rule.premises.size(); ++i) {
            premiseIndexes.add( input.nextInt() );
        }

        Map<String, Formula> substitutionMap = new HashMap<>();
        while (input.hasNext()) {
            String atomName = input.next();
            Formula formula = Formula.parseFromScanner(input);
            substitutionMap.put(atomName, formula);
        }

        return new ProofInstructionApplyRule( rule, premiseIndexes, substitutionMap );
    }
}
