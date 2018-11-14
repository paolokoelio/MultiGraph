package es.um.multigraph.decision.poolsappasitmoop.moop;

import java.util.ArrayList;
import java.util.List;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variable;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

public class MOOProblem extends AbstractProblem {

	public MOOProblem(int numberOfVariables, int numberOfObjectives) {
		super(numberOfVariables, numberOfObjectives);
	}

	/**
	 * The number of vulnerability attributes (nodes that are of type vulExists).
	 */
	public int nvulns = 2;

	/**
	 * The number of countermeasure attributes.
	 */
	public int ncms = 2;

	/**
	 * Entry {@code ssc[i][j]} is the cost per counter measure {@code i}.
	 */
	public double[] scc = { 0.5, 0.5 };

	/**
	 * Entry {@code lg[i][j]} is the value of an attribute/node, if counter measure
	 * is not applied lg[i][0] or it is applied lg[i][1] {@code i}. These values are
	 * pre-computed: {@code double v = [1 - pr[i]]*gain[i] - pr[i]*loss[i]};
	 */
	public double[][] lg = { { -0.088, 0.2 }, { -0.104, 0.4 }, { 0, 0.7 }, { 0.2, 0.4 } };

	/**
	 * Entry {@code constraint} is an applicable constraint to the problem (e.g. a
	 * max budget).
	 */
	public static int constraint = 10;

	/**
	 * Stores the solutions (acceptable security plans).
	 */
	private List<List<String>> secPlans;

	/**
	 * Ordered CMs' IDs (w.r.t. to SSCs)
	 */
	private String[] cmIds;
	/**
	 * Results of MOOP solution.
	 */
	NondominatedPopulation result;

	public void evaluate(Solution solution) {
		boolean[] t = EncodingUtils.getBinary(solution.getVariable(0));
		double f = 0;
		double g = 0;

// 		calculate the profits and weights for the knapsacks
		for (int i = 0; i < ncms; i++) {
			if (t[i]) {
				f += scc[i];
			}
		}

//		calculate the loss-gain sum LG
		for (int i = 0; i < nvulns; i++) { // for every attribute/vuln

			if (t[i])
				g += lg[i][1];
			else
				g += lg[i][0];

//			System.out.println(g);
		}

// check if SCC exceedes the budget
//		for (int j = 0; j < ncms; j++) {
//			if (f[j] <= constraint[j]) {
//				f[j] = 0.0;
//			} else {
//				f[j] = f[j] - constraint[j];
//			}
//		}

// 		minimization of SSC
		solution.setObjective(0, f);
//		maximization of LG
		g = -g;
		solution.setObjective(1, g);

//		in case we have limited budget (optional)
//		solution.setConstraints(f);

	}

	public Solution newSolution() {
		Solution solution = new Solution(1, 2, 0);
		solution.setVariable(0, EncodingUtils.newBinary(ncms));
		return solution;
	}

	public List<List<String>> resolve() {
		this.result = new Executor().withProblem(this).withAlgorithm("NSGAII").withProperty("withReplacement", true)
				.withMaxEvaluations(100).distributeOnAllCores().run();

		this.secPlans = new ArrayList<List<String>>();

		List<String> secPlan = null;
		char[] solString = null;

		for (int i = 0; i < this.result.size(); i++) {
			Solution solution = this.result.get(i);
			double[] objectives = solution.getObjectives();
			objectives[1] = -solution.getObjective(1);

			Variable sol = solution.getVariable(0);

			System.out.println("Solution " + (i + 1) + ":" + " SCC min: " + objectives[0] + " LG max: " + objectives[1]
					+ " Binary String: " + sol);

			solString = sol.toString().toCharArray();
			secPlan = new ArrayList<>();

			for (int j = 0; j < solString.length; j++)
				if (solString[j] == '1')
					secPlan.add(this.cmIds[j]);

			secPlans.add(secPlan);

		}
		return secPlans;
	}

	public void setNvulns(int nvulns) {
		this.nvulns = nvulns;
	}

	public void setNcms(int ncms) {
		this.ncms = ncms;
	}

	public void setScc(double[] scc) {
		this.scc = scc;
	}

	public void setLg(double[][] lg) {
		this.lg = lg;
	}

	public void setCmIds(String[] cmIds) {
		this.cmIds = cmIds;
	}

	public NondominatedPopulation getResult() {
		return result;
	}

	public String getPropsRepresentation() {
		return "moop: " + "nvluns: " + this.nvulns + "; nCMs " + this.ncms + "; SCC " + this.scc.length + "; LGs "
				+ this.lg.length + "\n";
	}

}
