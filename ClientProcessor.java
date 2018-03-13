// package Timer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.lang.ClassNotFoundException;


import javafx.scene.paint.Color;

public class ClientProcessor implements Runnable{


	private Socket sock;
	private ObjectOutputStream writer = null;
	private ObjectInputStream reader = null;
	private PrintWriter writerString;
	private BufferedInputStream readerString;
	private List<String> message;
	private Map<UserData, PrintWriter> userString;
	private UserData user;

	public ClientProcessor(Socket pSock, List<String> message, Map<UserData, PrintWriter> userString){
		sock = pSock;
		this.message = message;
		this.userString = userString;

		try {
			writer = new ObjectOutputStream(sock.getOutputStream());
			reader = new ObjectInputStream(sock.getInputStream());

			writerString = new PrintWriter(sock.getOutputStream(), true);
			readerString = new BufferedInputStream(sock.getInputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connexionUser(){
		try{
			// String response = (String)(reader.readObject());
			String response = read();
			System.out.println("Server : Login recu de " + response);

			UserData temp = getUserDataFromString(response);
			if(temp != null){
				writerString.write("LOGIN : KO");
				writerString.flush();
				System.out.println("Server : LOGIN 	: KO " + response);
			}
			else{
				this.user = new UserData(response, Color.BLACK);
				userString.put(user, writerString);
				response = "";

				writerString.write("LOGIN : OK");
				writerString.flush();
				System.out.println("Server : LOGIN 	: OK " + response);

				updateAll("USERS");
				updateOne(user,"MESSAGES");
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void deconnexionUser(){
		try{
			System.out.println("Server : Logout recu de " + user.getUsername());
			userString.remove(user);

			writerString.write("LOGOUT : OK");
			writerString.flush();
			System.out.println("Server : LOGOUT : OK " + user.getUsername());

			writer = null;
			reader = null;
			sock.close();

			updateAll("USERS");

		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void updateAll(String msg){
		System.out.println("Server : Envoie de requete d'update a tous les utilisateurs sur " + msg);
		for(Map.Entry<UserData,PrintWriter> u : userString.entrySet()){
			//System.out.println("Server : Envoie requete d'update ->" + u.getKey() + " sur " + msg);
			updateOne(u.getKey(), msg);
		}
	}

	public void updateOne(UserData userData, String msg){
			userString.get(userData).write(msg);
			userString.get(userData).flush();
			System.out.println("Server : " + msg + " : " + userData);
	}

	//Le traitement lancé dans un thread séparé
	public void run(){
		int nb;
		ArrayList<String> ArrayMessageToSend;
		ArrayList<UserData> ArrayUserToSend;

		//tant que la connexion est active, on traite les demandes
		connexionUser();

		while(!sock.isClosed()){
			try {
				//On attend la demande du client
				String response = read();

				System.out.println("RESPONSE : " + response);

				String toSend = "";
				// PM:@john:message
				if (response.startsWith("PM:") ){
					String cutResponse = response.substring(4);
					String[] splitted = cutResponse.split(":");
					String sendTo = splitted[0];
					String text = splitted[1];

					UserData sendToData= getUserDataFromString(sendTo);

					System.out.println("Serveur : PM recu du client " + user.getUsername() + "@" + sendTo + " : " + text);
					message.add("@"+sendTo+ " from "+ user.getUsername()+ " : " + text);
					updateOne(user,"MESSAGES");
					updateOne(sendToData,"MESSAGES");
				}
				else if (response.startsWith("MSG:") ){
					String cutResponse = response.substring(4);
					message.add(user.getUsername() + " : " + cutResponse);
					System.out.println("Serveur : MSG recu du client "+ user.getUsername() +" : "+ cutResponse);
					updateAll("MESSAGES");
				}
				else if (response.startsWith("COLOR:")){
					String cutResponse = response.substring(6);
					userString.remove(user);
					user = new UserData(user.getUsername(), checkColor(cutResponse));
					//user.setColor(checkColor(cutResponse));
					userString.put(user, writerString);
					System.out.println("Serveur : Couleur recu du client "+ user.getUsername() +" : "+ checkColor(cutResponse).toString());
					updateAll("USERS");
				}
				else{

					switch(response.toUpperCase()){
						case "LOGOUT": deconnexionUser();break;
						case "USERS" :
							//nb = reader.readInt();

							ArrayUserToSend = new ArrayList<UserData>(userString.keySet());
							writer.writeObject(ArrayUserToSend);
							writer.flush();
							System.out.println("Server : envoie array : " + ArrayUserToSend + " -> " + user.getUsername());
							break;

						case "MESSAGES":
							nb = reader.readInt();

							ArrayList<String> temp = filterArray(user.getUsername());
							ArrayMessageToSend = new ArrayList<>(temp.subList(nb,temp.size()));
							writer.writeObject(ArrayMessageToSend);
							writer.flush();
							System.out.println("Server : envoie array : " + ArrayMessageToSend + " -> " + user.getUsername());
							break;

						default :toSend = "Commande inconnu !";break;
					}
				}
			}catch(SocketException e){
				System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// User has bee disconnected (lost connection?, remote closed?)
		System.out.println("Server : username : socket ferme");
		userString.remove(user);
		updateAll("USERS");

	}

	//Méthode pour lire les réponses du serveur
	private String read() throws IOException{
		try{
			String response = "";
			int stream;
			byte[] b = new byte[4096];
			stream = readerString.read(b);
			if(stream == -1){
				sock.close();
				return "";
			}
			else{
				response = new String(b, 0, stream);
				return response;
			}
		}catch(IOException e){
			e.printStackTrace();
			return "";
		}
	}

	private ArrayList<String> filterArray(String user){
		ArrayList<String> response = new ArrayList<>();
		for(String m : message){
			if (m.startsWith("@")){
				String cutResponse = m.substring(1);
				String[] splitted = cutResponse.split(" from ",2);
				if(splitted[0].equals(user) || splitted[1].equals(user)){
					response.add(m);
				}
			}
			else{
				response.add(m);
			}
		}
		return response;
	}

	private Color checkColor(String color){
		switch(color.toUpperCase()){
			case "RED" : return Color.RED;
			case "ORANGE" : return Color.ORANGE;
			case "BLACK" : return Color.BLACK;
			case "YELLOW" : return Color.YELLOW;
			default : return Color.YELLOW;
		}
	}

	private UserData getUserDataFromString(String user){
		for(Map.Entry<UserData,PrintWriter> u : userString.entrySet()){
			if(u.getKey().getUsername().equals(user)){
				return u.getKey();
			}
		}
		return null;
	}

}
