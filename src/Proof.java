import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

/** Represents the current state of a proof.
 *  i.e. will not maintain a history of statements invoked. Leave that for some ProofRecorder class.
 */
public class Proof {
    public ArrayList<Formula> formulas; // 0-indexed
    public Stack<Integer> assumptionIndexes; // 0-indexed

    public Proof() {
        formulas = new ArrayList<>();
        assumptionIndexes = new Stack<>();
    }



    public int getLastFormulaIndex() {
        return formulas.size() - 1;
    }
    public Formula getLastFormula() throws IllegalStateException {
        if (formulas.isEmpty())
            throw new IllegalStateException("No formulas in proof");

        return formulas.get( getLastFormulaIndex() );
    }
    public boolean hasAssumptions() {
        return !assumptionIndexes.isEmpty();
    }



    public void makeAssumption(Formula assumption) throws IllegalStateException {
        formulas.add( assumption );
        assumptionIndexes.add( getLastFormulaIndex() );
    }

    public void dischargeAssumption() throws IllegalStateException {
        if (assumptionIndexes.isEmpty())
            throw new IllegalStateException("No assumptions to discharge");

        // Grab the relevant formulas
        Formula conclusion = getLastFormula();
        int assumptionIndex = assumptionIndexes.pop();
        Formula premise = formulas.get( assumptionIndex );

        // Remove everything between the premise and conclusion, inclusive
        while ( formulas.size() > assumptionIndex )
            formulas.remove( getLastFormulaIndex() );

        // Append the new deduced implication to the proof
        formulas.add( new FormulaImplies(premise, conclusion) );
    }

    public void applyInferenceRule(
            InferenceRule rule,
            ArrayList<Integer> premiseIndexes,
            Map<String, Formula> substitutionMap) throws IllegalStateException {

        if (rule.premises.size() != premiseIndexes.size())
            throw new IllegalStateException("Number of premises supplied (" + premiseIndexes.size() + ") do not match with number of premises in the rule (" + rule.premises.size() + ")");

        // Substitute the premises in the rule for how they will actually appear in the proof
        ArrayList<Formula> rulePremises = new ArrayList<>();
        for (Formula f : rule.premises)
            rulePremises.add( f.getSubstitutedFormula(substitutionMap) );

        // Verify that the supplied premise indices refer to formulas in the proof that are the same as the premises required by the inference rule
        for (int n = 0; n < premiseIndexes.size(); ++n) {
            int proofPremiseIndex = premiseIndexes.get(n)-1; // -1 to convert from 1-indexed (as read by the user) to 0-indexed (as used by the proof)
            Formula proofPremise = formulas.get(proofPremiseIndex);
            Formula rulePremise = rulePremises.get(n);

            if ( !proofPremise.equals(rulePremise) )
                throw new IllegalStateException("Premise " + proofPremise + " in proof does not match premise " + rulePremise + " in rule");
        }

        // Accept the rule application, and append the rule's conclusion to the proof
        formulas.add( rule.conclusion.getSubstitutedFormula(substitutionMap) );
    }
}
