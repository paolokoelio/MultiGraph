package es.um.multigraph.core.test;

import javax.xml.parsers.ParserConfigurationException;

import es.um.multigraph.decision.model.adapt.BayesianAdapter;
import es.um.multigraph.utils.FileUtils;
import es.um.multigraph.utils.ImportAG;
import es.um.multigraph.utils.ParseAG;

public class MainTest {

	public static void main(String[] args) {
		try {
			
			ImportAG bs = new ImportAG();
			FileUtils fl = new FileUtils();
			fl.readFile("files/AttackGraph.xml");

			bs.setFile(fl);
			bs.importAG();
			ParseAG ps = new ParseAG(bs.getNodes(), bs.getEdges());
			ps.parseAG();
			BayesianAdapter adapter = new BayesianAdapter();
			adapter.setMyEdges(ps.getMyEdges());
			adapter.setMyNodes(ps.getMyNodes());
			adapter.convertAG();
			adapter.getMyBayesianNodes();
			adapter.getMyBayesianEdges();

		} catch (ParserConfigurationException e) {

			e.printStackTrace();
		}

	}

}
