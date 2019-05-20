package es.um.multigraph.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import Utilities.FileAccess;

public class GoalReader {

	FileReader reader;

	public GoalReader() {

	}

	public Set<String> readGoals(String path) {
		Set<String> goals = new HashSet<String>();
		FileAccess fa = new FileAccess();
		String file = path + "goal.txt";
		File tempFile = new File(file);
		if(!tempFile.exists())
			return this.readGoalsFromAG(path);
		BufferedReader bf = fa.createBufferedReader(file);
		if (bf == null) {
			return null;
		}
		String buffer = "";
		while (buffer != null && (buffer = fa.readFromFile(bf)) != null && buffer.length() != 0) {

//	            String[] bsplit = buffer.split(",");
//	            Initial p = new Initial();
//	            p.name = bsplit[0].replaceAll(" ", "").toLowerCase();
//	            p.value = Double.parseDouble(bsplit[1].replaceAll(" ", ""));
			goals.add(buffer);
		}

		return goals;
	}

	public Set<String> readGoalsFromAG(String path) {
		String file = path + "VERTICES.CSV";
		Set<String> goals = new HashSet<String>();
		FileAccess fa = new FileAccess();
		BufferedReader bf = fa.createBufferedReader(file);
		if (bf == null) {
			return null;
		}
		String buffer = "";
		while (buffer != null) {

			try {
				buffer = bf.readLine();
				if (buffer == null)
					break;
				String[] buffersplit = buffer.split(",");
				String newGoal = "";
				if(buffersplit[1].contains("execCode"))
					newGoal = buffersplit[0];
				else
					continue;
//				for (int i = 1; i < buffersplit.length - 2; ++i) {
//					newGoal = i < buffersplit.length - 2 ? newGoal + buffersplit[i] + "," : newGoal + buffersplit[i];
//				}
//				String bodyPlain = newGoal.substring(1, newGoal.length() - 2);
				goals.add(newGoal);
			} catch (IOException exp) {
				System.out.println("Could not read VERTICES.CSV.");
			}

//            goals.add(buffer);
		}

		return goals;
	}

}
