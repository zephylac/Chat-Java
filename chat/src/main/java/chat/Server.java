package chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.io.ObjectOutputStream;
import java.io.PrintWriter;

public class Server {
	//On initialise des valeurs par défaut
	private int port = 2345;
	private String host = "127.0.0.1";
	private ServerSocket server = null;
	private boolean isRunning = true;
	private List<String> message = new ArrayList<>();
	private Map<UserData, PrintWriter> userString = new HashMap<>();


	public Server(){
		try {
			server = new ServerSocket(port, 100, InetAddress.getByName(host));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Server(String pHost, int pPort){
		host = pHost;
		port = pPort;

		try {
			server = new ServerSocket(port, 100, InetAddress.getByName(host));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//On lance notre serveur
	public void open(){

		//Toujours dans un thread à part vu qu'il est dans une boucle infinie

		Thread t = new Thread(new Runnable(){
			public void run(){
				while(isRunning == true){
					try {
						//On attend une connexion d'un client
						Socket client = server.accept();
						//Une fois reçue, on la traite dans un thread séparé
						System.out.println("Connexion cliente reçue -> Nouveau thread");
						Thread t = new Thread(new ClientProcessor(client,message, userString));
						t.start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
					server = null;
				}
			}
		});
		t.start();
	}

	public void close(){
		isRunning = false;
	}
}
