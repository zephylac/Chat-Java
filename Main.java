// package Timer;

public class Main {
	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 2346;
		TimeServer ts = new TimeServer(host, port);
		ts.open();

		System.out.println("Serveur initialisé.");

		//ClientConnexion c1 = new ClientConnexion(host, port, "José");
		//ClientConnexion c2 = new ClientConnexion(host, port, "Rogé");

		//c1.envoieMessage("Salut Roge");
		//c2.envoieMessage("Salut Jose");
		//c1.seDeconnecter();
	}

}
