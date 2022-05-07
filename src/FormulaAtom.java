import java.util.Map;
import java.util.Objects;

public class FormulaAtom extends Formula {
    public String name;

    public FormulaAtom(String name) {
        this.name = name;
    }

    @Override
    public Formula getSubstitutedFormula(Map<String, Formula> substitutionMap) {
        return substitutionMap.getOrDefault(this.name, this);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof FormulaAtom))
            return false;

        FormulaAtom f = (FormulaAtom) obj;

        return f.name.equals(this.name);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
