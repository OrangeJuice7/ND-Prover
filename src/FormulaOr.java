import java.util.Map;
import java.util.Objects;

public class FormulaOr extends FormulaBinary {
    public FormulaOr(Formula subf1, Formula subf2) {
        super(subf1, subf2);
    }

    public String getOpSymbol() {
        return "|";
    }

    @Override
    public Formula getSubstitutedFormula(Map<String, Formula> substitutionMap) {
        return new FormulaOr(
                subf1.getSubstitutedFormula(substitutionMap),
                subf2.getSubstitutedFormula(substitutionMap) );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof FormulaOr))
            return false;

        FormulaOr f = (FormulaOr) obj;

        return f.subf1.equals(this.subf1)
                && f.subf2.equals(this.subf2);
    }
    @Override
    public int hashCode() {
        return Objects.hash(getOpSymbol(), subf1.hashCode(), subf2.hashCode());
    }
}
