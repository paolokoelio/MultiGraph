/**
 * 
 */
package es.um.multigraph.utils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * Manage file I/O
 * @author Pavlo Burda
 *
 */
public class FileUtils {

	private File inputFile;
	private File outFile;
	private FileWriter writer;
	
	public void readFile(String path) {
		
		try {
			this.inputFile = new File(path);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void writeFile(String path, String content) {
		
	    writer = null;
	    try {
	    	this.outFile = new File(path);
	        writer = new FileWriter(this.outFile);
	        writer.write(content);
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (writer != null) try { writer.close(); } catch (IOException ignore) {}
	    }
	    System.out.printf("File is located at %s%n", this.outFile.getAbsolutePath());
		
	}
	
	public File getFile() {
		return this.inputFile;
	}
	
	public FileWriter getWriter(String path) {
		this.outFile = new File(path);
		
	    try {
	        this.writer = new FileWriter(this.outFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    } 
//	    finally {
//	        if (writer != null) try { writer.close(); } catch (IOException ignore) {}
//	    }
		return this.writer;
	}
}
