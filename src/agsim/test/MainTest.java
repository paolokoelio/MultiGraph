package agsim.test;
import javax.xml.parsers.ParserConfigurationException;

import agsim.model.BayesianAdapter;
import agsim.utils.FileUtils;
import agsim.utils.ImportAG;

public class MainTest {

	public static void main(String[] args) {
		try {
			ImportAG bs = new ImportAG();
			FileUtils fl = new FileUtils();
			fl.readFile("files/AttackGraph.xml");
			BayesianAdapter adapter = new BayesianAdapter();
			
			bs.setFile(fl);
			bs.parseAG();
			adapter.convertAG(bs.getNodes(), bs.getEdges());

			
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
		}

	}

}
