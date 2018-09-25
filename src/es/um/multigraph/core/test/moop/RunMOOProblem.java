package es.um.multigraph.core.test.moop;

import java.io.IOException;

import org.moeaframework.Executor;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

import es.um.multigraph.decision.model.moop.MOOProblem;

public class RunMOOProblem {

	public static void main(String[] args) throws IOException {
		NondominatedPopulation result = new Executor().withProblemClass(MOOProblem.class).withAlgorithm("NSGAII")
				.withProperty("withReplacement", true).withMaxEvaluations(4).distributeOnAllCores().run();

		Plot plot = new Plot();

		for (int i = 0; i < result.size(); i++) {
			Solution solution = result.get(i);
			double[] objectives = solution.getObjectives();
			objectives[1] = -solution.getObjective(1);

			System.out.println("Solution " + (i + 1) + ":");
			System.out.println("SCC min: " + objectives[0]); // SSC
			System.out.println("LG max: " + objectives[1]); // LG
			System.out.println("Binary String: " + solution.getVariable(0));

		}

//		String[] argv = {"", ""};
//		new LaunchDiagnosticTool();
//		try {
//			LaunchDiagnosticTool.main(argv);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		plot.add("NSGAII", result).setXLabel("Security control cost (SCC)").setYLabel("Expected loss/gain (LG)").show();

	}

}