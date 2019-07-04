package es.um.multigraph.decision.poolsappasitmoop.moop;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variable;

import es.um.multigraph.utils.FileUtils;

public class MOOPUtils {

	private Map<String, Double> exlg;
	private Map<String, Double> exlgAfter;
//	private Map<String,double[]> nodeLG;
	private double[][] lgs;
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_HEADER = "plan,node,cost";
	private static final String PATH_SOL = "files/solutions/";

	/**
	 * Ordered Nodes's IDs (w.r.t. to LGs).
	 */
	private String[] ids;

	/**
	 * Stores the solutions (acceptable security plans).
	 */
	private List<List<String>> secPlans;

	/**
	 * Ordered CMs' IDs (w.r.t. to SSCs).
	 */
	private String[] cmIds;
	/**
	 * Results of MOOP solution.
	 */
	private NondominatedPopulation result;

	/**
	 * The expected loss/gain for every node. External nodes are set to 0 as per
	 * paper. A positive values signifies a gain, while a negative signifies a loss.
	 *
	 * @return All nodes' expectedLossGain with Double[1] and without Double[0] CMs.
	 */
	public void genNodesLGs() {
		Map<String, double[]> nodeLG = new LinkedHashMap<>();
		this.lgs = new double[this.exlg.size()][2];
		this.ids = new String[this.exlg.size()];

		int i = 0;
		for (Iterator<String> it = this.exlg.keySet().iterator(); it.hasNext();) {
			String id = it.next();

			ids[i] = id;
			lgs[i][0] = this.exlg.get(id);
			lgs[i][1] = this.exlgAfter.get(id);

			nodeLG.put(id, lgs[i]);
			i++;
		}
	}

	public List<List<String>> resolve(MOOProblem moop, String alg, int maxEval, String[][]... props) {
		if (!(props.length == 0))
			throw new UnsupportedOperationException("Not supported yet.");

		this.result = new Executor().withProblem(moop).withAlgorithm(alg).withProperty("withReplacement", true)
				.withMaxEvaluations(maxEval).distributeOnAllCores().run();

		this.secPlans = new ArrayList<List<String>>();

		List<String> secPlan = null;
		char[] solString = null;

		for (int i = 0; i < this.result.size(); i++) {
			Solution solution = this.result.get(i);
			double[] objectives = solution.getObjectives();
			objectives[1] = -solution.getObjective(1);

			Variable sol = solution.getVariable(0);

			System.out.println("Sol " + (i + 1) + ":" + " SCC min: " + objectives[0] + " LG max: " + objectives[1]
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

	public void writeCSV(String path, List<String> plan) {

		String base_path = PATH_SOL;
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		path = base_path + path + "_" + dateFormat.format(date) + ".csv";

		FileUtils fileUtils = new FileUtils();
		FileWriter writer = fileUtils.getWriter(path);

		try {
			// Write the CSV file header
			writer.append(FILE_HEADER.toString());

			// Add a new line separator after the header
			writer.append(NEW_LINE_SEPARATOR);

			for (Iterator<String> iterator = plan.iterator(); iterator.hasNext();)
				writer.append(iterator.next());

		} catch (Exception e) {
			System.out.println("Error in Writer");
			e.printStackTrace();
		} finally {

			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing Writer");
				e.printStackTrace();
			}

		}
	}

	public void setLGs(Map<String, Double> exlg) {
		this.exlg = exlg;
	}

	public void setLGsAfter(Map<String, Double> exlgAfter) {
		this.exlgAfter = exlgAfter;

	}

	public double[][] getLgs() {
		return lgs;
	}

	public String[] getIds() {
		return ids;
	}

	public Map<String, double[]> getNodesLGs() {

		return null;
	}

	public void setCmIds(String[] cmIds) {
		this.cmIds = cmIds;
	}

	public NondominatedPopulation getResult() {
		return result;
	}

}
