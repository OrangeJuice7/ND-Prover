import java.util.Map;
import java.util.Objects;

public class FormulaNeg extends Formula {
    public Formula subf;

    public FormulaNeg(Formula subf) {
        this.subf = subf;
    }

    public String getOpSymbol() {
        return "~";
    }

    @Override
    public Formula getSubstitutedFormula(Map<String, Formula> substitutionMap) {
        return new FormulaNeg( subf.getSubstitutedFormula(substitutionMap) );
    }

    @Override
    public String toString() {
        String subfstr = subf.toString();
        if (subf instanceof FormulaBinary)
            subfstr = "(" + subfstr + ")";
        return getOpSymbol() + subfstr;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof FormulaNeg))
            return false;

        FormulaNeg f = (FormulaNeg) obj;

        return f.subf.equals(this.subf);
    }
    @Override
    public int hashCode() {
        return Objects.hash(getOpSymbol(), subf.hashCode());
    }
}
