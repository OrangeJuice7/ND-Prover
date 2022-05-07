import java.util.Map;
import java.util.Scanner;

public abstract class Formula {
    public abstract Formula getSubstitutedFormula(Map<String, Formula> substitutionMap);

    /** Symbols:
     *  ~ : not
     *  & : and
     *  | : or
     *  > : implies
     *  (unused) = : iff
     *  (unused) ^ : xor
     */
    public static Formula parseFromScanner(Scanner input) {
        if (!input.hasNext())
            throw new IllegalArgumentException("Scanner is empty");

        String token = input.next();
        switch (token) {
            case "~": { // NOT
                Formula subf = parseFromScanner(input);
                return new FormulaNeg(subf);
            }
            case "&": { // AND
                Formula subf1 = parseFromScanner(input);
                Formula subf2 = parseFromScanner(input);
                return new FormulaAnd(subf1, subf2);
            }
            case "|": { // OR
                Formula subf1 = parseFromScanner(input);
                Formula subf2 = parseFromScanner(input);
                return new FormulaOr(subf1, subf2);
            }
            case ">": { // IMPLIES
                Formula subf1 = parseFromScanner(input);
                Formula subf2 = parseFromScanner(input);
                return new FormulaImplies(subf1, subf2);
            }
            default: // Assume it's a proposition
                return new FormulaAtom(token);
        }
    }
    public static Formula parseFromPrefixString(String prefixString) {
        return parseFromScanner( new Scanner(prefixString) );
    }

    //public String getPrefixForm() {}
}
