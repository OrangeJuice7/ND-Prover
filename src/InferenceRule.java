import java.util.ArrayList;
import java.util.HashMap;

public class InferenceRule {
    public String name;
    public ArrayList<Formula> premises;
    public Formula conclusion;

    public InferenceRule(String name, ArrayList<Formula> premises, Formula conclusion) {
        this.name = name;
        this.premises = premises;
        this.conclusion = conclusion;
    }

    public static HashMap<String, InferenceRule> rules;
    static {
        rules = new HashMap<>();

        addRule( new InferenceRule("~I",
                new ArrayList<>(){{
                    add( Formula.parseFromPrefixString("> p & q ~ q") );
                }},
                Formula.parseFromPrefixString("~ p")
        ) );
        addRule( new InferenceRule("~E",
                new ArrayList<>(){{
                    add( Formula.parseFromPrefixString("p") );
                    add( Formula.parseFromPrefixString("~ p") );
                }},
                Formula.parseFromPrefixString("q")
        ) );
        addRule( new InferenceRule("~~E",
                new ArrayList<>(){{
                    add( Formula.parseFromPrefixString("~ ~ p") );
                }},
                Formula.parseFromPrefixString("p")
        ) );

        addRule( new InferenceRule("&I",
                new ArrayList<>(){{
                    add( Formula.parseFromPrefixString("p") );
                    add( Formula.parseFromPrefixString("q") );
                }},
                Formula.parseFromPrefixString("& p q")
        ) );
        addRule( new InferenceRule("&E1",
                new ArrayList<>(){{
                    add( Formula.parseFromPrefixString("& p q") );
                }},
                Formula.parseFromPrefixString("p")
        ) );
        addRule( new InferenceRule("&E2",
                new ArrayList<>(){{
                    add( Formula.parseFromPrefixString("& p q") );
                }},
                Formula.parseFromPrefixString("q")
        ) );

        addRule( new InferenceRule("|I1",
                new ArrayList<>(){{
                    add( Formula.parseFromPrefixString("p") );
                }},
                Formula.parseFromPrefixString("| p q")
        ) );
        addRule( new InferenceRule("|I2",
                new ArrayList<>(){{
                    add( Formula.parseFromPrefixString("q") );
                }},
                Formula.parseFromPrefixString("| p q")
        ) );
        addRule( new InferenceRule("|E",
                new ArrayList<>(){{
                    add( Formula.parseFromPrefixString("| p q") );
                    add( Formula.parseFromPrefixString("> p r") );
                    add( Formula.parseFromPrefixString("> q r") );
                }},
                Formula.parseFromPrefixString("r")
        ) );

        addRule( new InferenceRule(">E",
                new ArrayList<>(){{
                    add( Formula.parseFromPrefixString("p") );
                    add( Formula.parseFromPrefixString("> p q") );
                }},
                Formula.parseFromPrefixString("q")
        ) );
    }
    public static void addRule(InferenceRule rule) {
        rules.put(rule.name, rule);
    }

    public String getDisplayString() {
        return name + " : " + premises.toString() + " |- " + conclusion.toString();
    }
}
