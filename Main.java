// package Timer;

public class Main {
	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 2346;
		Server ts = new Server(host, port);
		ts.open();

		System.out.println("Serveur initialisé, addr : "+ host+",port : "+port);
	}

}
