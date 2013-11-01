package communicator;

// TODO: Auto-generated Javadoc
/**
 * The Class SubmitBatchResult.
 */
public class SubmitBatchResult {
	
	/** The success. */
	protected String result;
	
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		sb.append(result);
		sb.append("\n");
		
		return sb.toString();
	}
	
	public boolean equals(Object o){
		if(o instanceof SubmitBatchResult){
			SubmitBatchResult r = (SubmitBatchResult)o;
			return (r.toString().equals(this.toString()));
//			}
		}
		return false;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	
}
