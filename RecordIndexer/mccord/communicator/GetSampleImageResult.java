package communicator;

// TODO: Auto-generated Javadoc
/**
 * The Class GetSampleImageResult.
 */
public class GetSampleImageResult {
	
	/** The url. */
	protected String url;
	
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		sb.append(url);
		sb.append("\n");
		
		return sb.toString();
	}

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
}
