public abstract class FormulaBinary extends Formula {
    public Formula subf1;
    public Formula subf2;

    public FormulaBinary(Formula subf1, Formula subf2) {
        this.subf1 = subf1;
        this.subf2 = subf2;
    }

    public abstract String getOpSymbol();

    @Override
    public String toString() {
        String subf1str = subf1.toString();
        if (subf1 instanceof FormulaBinary)
            subf1str = "(" + subf1str + ")";
        String subf2str = subf2.toString();
        if (subf2 instanceof FormulaBinary)
            subf2str = "(" + subf2str + ")";

        return subf1str + getOpSymbol() + subf2str;
    }
}
