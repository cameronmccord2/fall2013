package server;

// TODO: Auto-generated Javadoc
/**
 * The Class FailedException.
 */
public class FailedException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5560487194842935810L;

	/**
	 * Instantiates a new failed exception.
	 *
	 * @param message the message
	 */
	public FailedException(String message) {
        super(message);
    }

	public FailedException() {
		super();
	}
}
