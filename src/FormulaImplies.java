import java.util.Map;
import java.util.Objects;

public class FormulaImplies extends FormulaBinary {
    public FormulaImplies(Formula subf1, Formula subf2) {
        super(subf1, subf2);
    }

    public String getOpSymbol() {
        return "->";
    }

    @Override
    public Formula getSubstitutedFormula(Map<String, Formula> substitutionMap) {
        return new FormulaImplies(
                subf1.getSubstitutedFormula(substitutionMap),
                subf2.getSubstitutedFormula(substitutionMap) );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof FormulaImplies))
            return false;

        FormulaImplies f = (FormulaImplies) obj;

        return f.subf1.equals(this.subf1)
                && f.subf2.equals(this.subf2);
    }
    @Override
    public int hashCode() {
        return Objects.hash(getOpSymbol(), subf1.hashCode(), subf2.hashCode());
    }
}
