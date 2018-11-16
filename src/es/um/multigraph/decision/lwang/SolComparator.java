package es.um.multigraph.decision.lwang;

import java.util.Comparator;
import java.util.List;

public class SolComparator<T extends Comparable<T>> implements Comparator<List<MyNode>> {

	@Override
	public int compare(List<MyNode> sol1, List<MyNode> sol2) {

		Double o1Cost = 0d;
		Double o2Cost = 0d;

		for (MyNode n : sol1) {
			o1Cost += n.getCost();
		}

		for (MyNode n : sol2) {
			o2Cost += n.getCost();
		}

		if (o1Cost < o2Cost) {
			return -1;
		} else if (o1Cost > o2Cost) {
			return 1;
		} else {
			return 0;
		}

	}

}
