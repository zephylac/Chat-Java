package server;

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

	// User is connecting to the server
	public void connexionUser(){
		try{
			String response = read();
			System.out.println("Server : Login recu de " + response);

			UserData temp = getUserDataFromString(response);
			if(temp != null){
				writerString.write("LOGIN : AE");
				writerString.flush();
				System.out.println("Server : LOGIN 	: AE ");
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

	// User is disconnecting from the server
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

			//updateAll("REMOVEUSERS");
			//updateAll(user.getUsername());
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	/* Method UpdateAll :
	 * Send a message to all users
	 */
	public void updateAll(String msg){
		System.out.println("Server : Envoie de requete d'update a tous les utilisateurs sur " + msg);
		for(Map.Entry<UserData,PrintWriter> u : userString.entrySet()){
			//System.out.println("Server : Envoie requete d'update ->" + u.getKey() + " sur " + msg);
			updateOne(u.getKey(), msg);
		}
	}

	/* Method UpdateOne :
	 * Send a message to a specific
	 */
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

					System.out.println("Server : PM recu du client " + user.getUsername() + "@" + sendTo + " : " + text);
					System.out.println("@"+sendTo + ":Whispering ("+user.getUsername()+") : " + text);
					System.out.println("@"+user.getUsername() + ":Whispering to "+sendTo+ " : " + text);


					message.add("@"+sendTo + ":Whispering ("+user.getUsername()+") : " + text);
					message.add("@"+user.getUsername() + ":Whispering to "+sendTo+ " : " + text);
					updateOne(user,"MESSAGES");
					updateOne(sendToData,"MESSAGES");
				}
				else if (response.startsWith("MSG:") ){
					// Message
					String cutResponse = response.substring(4);
					message.add(user.getUsername() + " : " + cutResponse);
					System.out.println("Server : MSG recu du client "+ user.getUsername() +" : "+ cutResponse);
					updateAll("MESSAGES");
				}
				else if (response.startsWith("COLOR:")){
					// Color
					String cutResponse = response.substring(6);
					userString.remove(user);
					this.user = new UserData(user.getUsername(), checkColor(cutResponse));
					//user.setColor(checkColor(cutResponse));
					userString.put(user, writerString);
					System.out.println("Server : Couleur recu du client "+ user.getUsername() +" : "+ checkColor(cutResponse).toString());
					updateAll("USERS");
				}
				else{

					switch(response.toUpperCase()){
						case "LOGOUT": deconnexionUser();break;
						case "USERS" :

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
						case "POTATO":
							message.add(user.getUsername() + " :!POTATO");
							System.out.println("Server : MSG POTATO recu du client "+ user.getUsername());
							updateAll("MESSAGES");
							break;
						case "DENIS":
							message.add(user.getUsername() + " :!DENIS");
							System.out.println("Server : MSG DENIS recu du client "+ user.getUsername());
							updateAll("MESSAGES");
							break;
						case "NYAN":
							message.add(user.getUsername() + " :!NYAN");
							System.out.println("Server : MSG NYAN recu du client "+ user.getUsername());
							updateAll("MESSAGES");
							break;
						case "PEPE":
							message.add(user.getUsername() + " :!PEPE");
							System.out.println("Server : MSG PEPE recu du client "+ user.getUsername());
							updateAll("MESSAGES");
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
		updateAll("REMOVEUSERS");
		updateAll(user.getUsername());

	}

	//Method to read string since readUTF() has somme problem
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

	//Method to filter the Array for a specified user
	private ArrayList<String> filterArray(String user){
		ArrayList<String> response = new ArrayList<>();
		for(String m : message){
			if(m.startsWith("@")){
				String cutResponse = m.substring(1);
				String[] splitted = cutResponse.split(":",2);
				//If message belongs to user we add it to the list
				if(splitted[0].equals(user)){
					response.add(splitted[1]);
				}
			}
			else {response.add(m);}
		}
		return response;
	}

	/* Takes a String as parameter and return a valid color
	 * Check if it's a valid color if not black will be returned by default
	 */
	private Color checkColor(String color){
		switch(color.toUpperCase()){
			case "RED"    : return Color.RED;
			case "YELLOW" : return Color.YELLOW;
			case "ORANGE" : return Color.ORANGE;
			case "BLACK"  : return Color.BLACK;
			case "BLUE"   : return Color.BLUE;
			case "GREEN"  : return Color.GREEN;
			case "PINK"   : return Color.PINK;
			case "PURPLE" : return Color.PURPLE;
			case "CYAN"   : return Color.CYAN;
			default       : return Color.BLACK;
		}
	}

	// Retrieve userData from string
	private UserData getUserDataFromString(String user){
		for(Map.Entry<UserData,PrintWriter> u : userString.entrySet()){
			if(u.getKey().getUsername().equals(user)){
				return u.getKey();
			}
		}
		return null;
	}

}
