package agsim.test;

import javax.xml.parsers.ParserConfigurationException;

import agsim.model.BayesianAdapter;
import agsim.utils.FileUtils;
import agsim.utils.ImportAG;
import agsim.utils.ParseAG;

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
