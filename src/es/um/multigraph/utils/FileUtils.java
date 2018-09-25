/**
 * 
 */
package es.um.multigraph.utils;
import java.io.File;

/**
 * 
 * Manage file I/O
 * @author julien
 *
 */
public class FileUtils {

	private File inputFile;
	
	public void readFile(String path) {
		
		try {
			this.inputFile = new File(path);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public File getFile() {
		return this.inputFile;
	}
}
