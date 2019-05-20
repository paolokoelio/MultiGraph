package es.um.multigraph.core.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import es.um.multigraph.conf.DBManager;
import es.um.multigraph.decision.model.adapt.BayesianNodeAdapted;
import es.um.multigraph.decision.poolsappasitmoop.BayesianNode;

public class CompteUnc implements Runnable {

	public BayesianNode node;
	public DBManager conn;

	public CompteUnc(BayesianNode node, DBManager db) {
		this.node = node;
		this.conn = db;
	}
	
	private Double LCPD_SQL_searchRecord(String me, String[] nodesID, boolean[] nodesState, boolean prTrue)
			throws SQLException {
		Connection conn = this.conn.getConnection();
		Double result;

		String query = "SELECT " + (prTrue ? "prTrue" : "prFalse") + " FROM lcpd WHERE " + me + "='M'";

		for (String n : nodesID) {
			query += " AND " + n + "=?";
		}

		query += " LIMIT 1";

		// this.update("Looking for " + me + " (" + (prTrue ? "prTrue" : "prFalse") + ")
		// in LCPD table: " + query + " -- " + Arrays.toString(nodesState));
		PreparedStatement ps = conn.prepareStatement(query);

		for (int i = 0; i < nodesState.length; i++) {
			ps.setString(i + 1, nodesState[i] ? "1" : "0");
		}

		ResultSet rs = ps.executeQuery();
		rs.next();
		result = rs.getDouble(1); // RESULT COLUMN ARE COUNTED FROM 1

		return result;
	}

	public Double computeUnconditionalProbability(BayesianNode target, boolean forceRecompute) throws SQLException {

		int SQL = 0;
		int SKIP = 0;
		
		if (!target.getUnconditionalPr().equals(Double.NaN) && !forceRecompute) {
			System.out.println("PrUnconditional(" + target.getID() + ") = " + target.getUnconditionalPr() + " CACHED\n");
			return target.getUnconditionalPr();
		}

		String debug = "PrUnconditional(" + target.getID();

		if (target.isExternal()) {
			debug += ")";
//			 this.log(debug+"\n");
//			 this.log("| Pr(" + target.getID() + "=true) = " + String.format("%.2f",
//			 target.getPriorPr()) + " PRIOR\n"); //COMMENT FOR INLINE
//			 this.log("| SUM += 0,0 -> " + String.format("%.2f",
//			 target.getPriorPr())+"\n"); //COMMENT FOR INLINE
//			 this.log("|-------------------------\nResult\n"); //COMMENT FOR INLINE
			System.out.println(debug + " = " + String.format("%.2f", target.getPriorPr()) + "\n");
			target.setUnconditionalPr(target.getPriorPr());
			return target.getPriorPr();
		}

//		 debug += "|"; //COMMENT FOR INLINE
		Set<BayesianNode> ancestor = target.getAllAncestor();
		List<String> ancestorID = new LinkedList<>();
		List<Boolean> ancestorStates = new LinkedList<>();

		for (Iterator<BayesianNode> it = ancestor.iterator(); it.hasNext();) {
			String tmp = it.next().getID();
			ancestorID.add(tmp);
//			 debug += tmp + " "; //COMMENT FOR INLINE
		}
//		 debug = debug.trim(); //COMMENT FOR INLINE
//		debug += ")"
//				 + "\n" //COMMENT FOR INLINE
//				+ "";
//		 this.log(debug+"\n");

		Double sum_result = 0d;

		int anc_size = (int) ancestorID.size();
		long startTime = 0;
		
		if(anc_size >5) {
//		 System.out.println("PrUnc: Pa[" + target.getID() + "] size: " + ancestorID.size() + ", #cases " + Math.pow(2, ancestorID.size()));
			System.out.println("PrUnc: Pa[" + target.getID() + "] size: " + ancestorID.size() + ", #cases " + Math.pow(2, ancestorID.size()));
		 startTime = System.currentTimeMillis();
		}

		// Generate all possible combinations of parents states
		for (int i = 0; i < Math.pow(2, ancestorID.size()); i++) {
			ancestorStates.clear();

			// this.log("|-------------------------\n");
//			 debug = "| Pr(" + target.getID() + "|"; //COMMENT FOR INLINE
			StringBuilder binary = new StringBuilder(Integer.toBinaryString(i));
			for (int j = binary.length(); j < ancestorID.size(); j++) {
				binary.insert(0, '0');
			}

			for (int pos = 0; pos < ancestorID.size(); pos++) {
				ancestorStates.add(binary.charAt(pos) == '1');
//				 debug += ancestorID.get(pos) + "=" + ancestorStates.get(pos) + " "; //COMMENT
				// FOR INLINE
			}

//			 debug = debug.trim() + ")"; //COMMENT FOR INLINE
//			 this.log(debug+"\n"); //COMMENT FOR INLINE
			Double partialPR = 1d;

			// TARGET NODE
			List<String> parentIDs = new LinkedList<>(target.getParentsIDs());
			boolean[] parentStates = new boolean[parentIDs.size()];

			for (int kk = 0; kk < parentIDs.size(); kk++) {
				parentStates[kk] = ancestorStates.get(ancestorID.indexOf(parentIDs.get(kk)));
			}

			Double tmp_pr = LCPD_SQL_searchRecord(target.getID(), parentIDs.toArray(new String[parentIDs.size()]),
					parentStates, true);
			SQL++;
			partialPR *= tmp_pr;

			// END TARGET NODE
			if (partialPR == 0) {
//				 this.log(debug + " == 0 -> SKIP"); //COMMENT FOR INLINE
//				 this.log("| SUM += 0,0 -> " + String.format("%.2f", sum_result)); //COMMENT
				// FOR INLINE
				continue;
			} else {
//				 this.log(debug + " = " + String.format("%.2f", tmp_pr)); //COMMENT FOR INLINE
			}
			
			// ALL ANCESTORS
			for (Iterator<BayesianNode> itt = target.getAllAncestor().iterator(); itt.hasNext();) {
//				 debug = "|"; //COMMENT FOR INLINE
				BayesianNode p = itt.next();

				boolean pState = ancestorStates.get(ancestorID.indexOf(p.getID()));

				if (p.isExternal()) {
					tmp_pr = pState ? p.getPriorPr() : (1 - p.getPriorPr());
					partialPR *= tmp_pr;
//					 debug += " Pr(" + p.getID() + "=" + pState + ") = " + String.format("%.2f",
//					 tmp_pr) + " PRIOR"; //COMMENT FOR INLINE
//					 this.log(debug); //COMMENT FOR INLINE
					SKIP++;
					continue;
				}

//				 debug += " Pr(" + p.getID() + "=" + pState + "|"; //COMMENT FOR INLINE
				parentIDs = new LinkedList<>(p.getParentsIDs());
				parentStates = new boolean[parentIDs.size()];

				for (int kk = 0; kk < parentIDs.size(); kk++) {
					parentStates[kk] = ancestorStates.get(ancestorID.indexOf(parentIDs.get(kk)));
//					 debug += parentIDs.get(kk) + "=" + parentStates[kk] + " "; //COMMENT FOR
					// INLINE
				}

				tmp_pr = LCPD_SQL_searchRecord(p.getID(), parentIDs.toArray(new String[parentIDs.size()]), parentStates,
						pState);
				SQL++;
				partialPR *= tmp_pr;

				if (partialPR == 0) {
//					 this.log(debug + ") == 0 -> SKIP"); //COMMENT FOR INLINE
					partialPR = 0d;
					SKIP++;
					break;
				} else {
//					 log(debug + ") = " + String.format("%.2f", tmp_pr)); //COMMENT FOR INLINE
				}
			}
			// END ALL ANCESTORS
			
			sum_result += partialPR;
//			 this.log("| SUM += " + String.format("%.2f", partialPR) + " -> " +
//			 String.format("%.2f", sum_result)); //COMMENT FOR INLINE
		}
		
		if(anc_size >5) {
			long endTime = System.currentTimeMillis();
			long t = (endTime-startTime);
			System.out.println("Total elapsed time in computation of "
					+ "PrUnc is :" + t + "ms");
		}

		target.setUnconditionalPr(sum_result);

//		this.log("|-------------------------"); // COMMENT FOR INLINE
		System.out.println(debug + ""
//		 + "Result" //COMMENT FOR INLINE
//				+ ") = " + String.format("%.2f", sum_result) + "\n");
				+ ") = " + String.format("%.2f", sum_result) );
		System.out.println("SQL reads: " + SQL + " SKIPs: " + SKIP +"\n"); 
		return sum_result;
	}
	
	@Override
	public void run() {

		try {
			computeUnconditionalProbability(this.node, true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
