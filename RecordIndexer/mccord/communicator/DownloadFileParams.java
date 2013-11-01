package communicator;

// TODO: Auto-generated Javadoc
/**
 * The Class DownloadFileParams.
 */
public class DownloadFileParams {
	
	/** The filename. */
	protected String filename;

	/**
	 * Gets the filename.
	 *
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Sets the filename.
	 *
	 * @param filename the new filename
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		sb.append(filename);
		sb.append("\n");
		
		return sb.toString();
	}
}
