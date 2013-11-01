package communicator;

// TODO: Auto-generated Javadoc
/**
 * The Class DownloadFileResult.
 */
public class DownloadFileResult {
	
	/** The url. */
	protected String url;

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 *
	 * @param url the new url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		sb.append(url);
		sb.append("\n");
		
		return sb.toString();
	}
}
