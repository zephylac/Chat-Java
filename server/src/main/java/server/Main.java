package server;

public class Main {
	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 2346;
		Server srv = new Server(host, port);
		srv.open();

		System.out.println("Serveur initialisé, addr : "+ host+",port : "+port);
	}

}
