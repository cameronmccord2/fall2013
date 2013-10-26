package importer;

/**
 * The Class Importer.
 */
public class Importer {

	/**
	 * The main method.
	 *
	 * @param args the first and only argument should be the path to the archive
	 */
	public static void main(String[] args) {
		if(args.length != 1){
			throw new RuntimeException("wrong number of inputs, got: " + args.length);
		}else{
			DataImporter importer = new DataImporter();
			importer.importArchive(args[0]);
		}
	}

}
