package client;

public class Client {

	public static void main(String[] args) {
		if(args.length != 2)
			throw new RuntimeException("invalid number of args passed to client: " + args.length);
		
		String host = args[0];
		int port = Integer.parseInt(args[1]);

	}

}
