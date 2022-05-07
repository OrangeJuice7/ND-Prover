# Natural Deduction Prover
A project with the vague goal of doing something with natural deduction proving.

In its current state, it is a proof verifier for classical propositional logic statements. All test cases provide tautological formulas and correct proofs. However, the verifier can also detect incorrect proofs. Just load the program up, and it will auto-verify all the test cases so that you can see the proving in action.

Ideas for the future:
* Fully automate proof generation, given a starting formula.
* Expand the logic to first-order logic and perhaps some other custom logics.

## Design
A `Formula` is a standard propositional logic formula.

An `InferenceRule` consists of a name, a list of premises (`Formula`s), and a conclusion (also a `Formula`). If the premise of an `InferenceRule` is satisfied by known formulas, then one can introduce its conclusion to the pool of knowledge. Notably, the propositional atoms in an inference rule have to be uniformly substituted for formulas whenever the rule is invoked, in order to be useful.

A `Proof` consists of a list of `Formula`s, representing what knowledge is known (by assumption or deduction). Unlike the more traditional sense, a `Proof` in this project tracks only the current state of knowledge, and not the entire history. Specifically, when a subproof is discharged, all the formulas in it are deleted from the `Proof`'s current working knowledge.

A `ProofInstruction` specifies what action to take on a `Proof` to update it and generate a new formula. There are 3 possible actions:
* Assume a formula.
* Discharge an assumption.
* Apply an inference rule.

A `ProblemInstance` consists of a `Formula` to prove, and a proposed sequence of `ProofInstruction`s that supposedly tells how to manipulate a fresh `Proof` to arrive at the desired `Formula`.

The `Solver` is the proof verifier, executing the given `ProofInstruction`s on the `Proof`.

A `SolutionInstance` simply contains a boolean that specifies whether the proof is correct or not. It is of auxiliary use.

### Test cases
The first line of each test case specifies a formula, in prefix form. ASCII-friendly characters are used in place of more standard logical symbols:
* `~` stands for negation (NOT, ¬).
* `&` stands for conjunction (AND, ∧).
* `|` stands for disjunction (OR, ∨).
* `>` stands for implication (→).

Each subsequent line in the test case is a proof instruction.
* An assumption starts with the keyword `assume`, followed by the `Formula` to assume, in prefix form.
* A discharge uses the keyword `discharge`, and takes no parameters.
* A rule application starts with the keyword `rule`.
	* Next is the rule name, which consists of two characters: the logical symbol (~, &, |, >), then either I or E which stands for "introduction" or "elimination" respectively.
	* Next is a sequence of *n* numbers, where *n* is the number of premises the specified rule has. The *i*-th number specifies the position of the formula in the `Proof` to use as the *i*-th premise for the rule. The formulas in the `Proof` are 1-indexed. Also remember that any formulas in a discharged subproof are not counted.
	* Finally, the substitutions for the rule have to be specified. The atom is first specified, then the prefix form of the formula to be substituted in is given. This can be repeated as many times as needed to specify all the atoms.
	* For example, consider line 7 of test case 4: `rule >E 4 5 p q q r`. The `>E` rule is implication elimination, which takes in two premises `p` and `p → q` and deduces `q`. The next two numbers (`4` and `5`) specify which lines in the proof correspond to the premises required by the rule; in this case formula 4 is `q` and formula 5 is `q → r`. The next character `p` tells us that we are going to specify the formula to be substituted in `p`'s place in the rule, and we give the formula `q`. Similarly, the next character `q` (the second one) tells the prover that we want to substitute `q` in the rule for the formula `r`. In other words, the `p q q r` part of the line can be understood as `p ↤ q` and `q ↤ r`.
For more examples, check the test cases.

## Test Case Problems
Recall that natural deduction in classical propositional logic consists of the following inference rules:
1. `{p → (q ∧ ¬q)} ⊢ ¬p` *(¬ Intro.)*
1. `{p, ¬p} ⊢ q` *(¬ Elim.)* Also known as the principle of explosion.
1. `{¬¬p} ⊢ p` *(¬¬ Elim.)*
1. `{p, q} ⊢ p ∧ q` *(∧ Intro.)*
1. `{p ∧ q} ⊢ p` *(∧ Elim. 1)*
1. `{p ∧ q} ⊢ q` *(∧ Elim. 2)*
1. `{p} ⊢ p ∨ q` *(∨ Intro. 1)*
1. `{q} ⊢ p ∨ q` *(∨ Intro. 2)*
1. `{p ∨ q, p → r, q → r} ⊢ r` *(∨ Elim.)*
1. `(p ⊢ q) ⊢ p → q` *(→ Intro.)* Not an invocable inference rule. It is instead represented by the assumption-discharge system.
1. `{p, p → q} ⊢ q` *(→ Elim.)* Also known as modus ponens.

Some of the test case formulas are non-trivial to prove in natural deduction, and may be of interest to the reader. As wise man Gentzen once said: [if a formula is provable then it is provable without any lemmas](https://en.wikipedia.org/wiki/Cut-elimination_theorem), so everything can be proven independently (or at least, you can reprove a result for use as a statement in another result). Of course, each test case already includes a model proof. The test cases are as follows:
1. `p → p` *(Identity / Reflexivity)* A trivial formula that does not need any inference rules to prove.
2. `(p ∧ q) → q` *(Conjunction Elimination)* A trivial formula to test applying an inference rule.
3. `(p ∧ (p → q)) → q` *(Modus Ponens)* Requires multiply rule applications to unpack the conjunction.
4. `(p → (q → r)) → ((p → q) → (q → r))` *(Lukasiewicz's second axiom)* Requires several nested assumptions.
5. `p → (q → p)` *(Lukasiewicz's first axiom)* Demonstrates how to copy a formula without an explicit copy rule.
6. `(p → q) → (¬p ∨ q)` *(Law of Material Implication, part 1)* Not provable in intuitionistic logic, which means you have to use double negation elimination in some way. Even knowing that, the proof won't be obvious.
7. `(¬p ∨ q) → (p → q)` *(Law of Material Implication, part 2)*
8. `((p → q) → p) → p` *(Peirce's law)* Not provable in intuitionistic logic.
9. `p ∨ ¬p` *(Law of Excluded Middle)* Not provable in intuitionistic logic.
10. `(p → q) → (¬q → ¬p)` *(Contraposition)*
11. `(p → (q → r)) → (q → (p → r))` *(Permutation/Exchange Rule)*
12. `¬(p ∧ q) → (¬p ∨ ¬q)` *(De Morgan duality, part 1)* Not provable in intuitionistic logic.
13. `¬(p ∨ q) → (¬p ∧ ¬q)` *(De Morgan duality, part 2)*

# Last Words
Do reach out to me if you have any questions about this project.
