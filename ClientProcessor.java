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

import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ClientProcessor implements Runnable{


	private Socket sock;
	private ObjectOutputStream writer = null;
	private ObjectInputStream reader = null;
	private PrintWriter writerString;
	private BufferedInputStream readerString;
	private List<Text> message;
	private Map<Text, PrintWriter> user;
	private Text username;
	private Color color;

	public ClientProcessor(Socket pSock, List<Text> message, Map<Text, PrintWriter> user){
		sock = pSock;
		this.message = message;
		this.user = user;
		this.color = Color.BLACK;
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
			String response = read();
			System.out.println("Server : Login recu de " + response);

			username = new Text(response);
			username.setFill(color);
			username.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));

			// On verifie qu'une personne avec le meme pseudo n'existe pas
			if(checkUser(response) != null){
				writerString.write("LOGIN : KO");
				writerString.flush();
				System.out.println("Server : LOGIN 	: KO " + username);
			}
			else{

				user.put(username, writerString);
				response = "";

				writerString.write("LOGIN : OK");
				writerString.flush();
				System.out.println("Server : LOGIN 	: OK " + username);

				updateAll("USERS");
				updateOne(username,"MESSAGES");
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void deconnexionUser(){
		try{
			System.out.println("Server : Logout recu de " + username);
			user.remove(username);

			writerString.write("LOGOUT : OK");
			writerString.flush();
			System.out.println("Server : LOGOUT : OK " + username);

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
		for(Map.Entry<Text,PrintWriter> u : user.entrySet()){
			//System.out.println("Server : Envoie requete d'update ->" + u.getKey() + " sur " + msg);
			updateOne(u.getKey(), msg);
		}
	}


	public void updateOne(Text userData, String msg){
			user.get(userData).write(msg);
			user.get(userData).flush();
			System.out.println("Server : " + msg + " : " + userData);
	}



	//Le traitement lancé dans un thread séparé
	public void run(){
		int nb;
		ArrayList<Text> ArrayToSend;

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

					Text sendToText = checkUser(sendTo);

					if(sendToText != null){

						System.out.println("Serveur : PM recu du client " + username + "@" + sendTo + " : " + text);

						Text messageText = new Text(text);
						messageText.setFill(Color.BLACK);
						messageText.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));

						Text combined = new Text("@"+sendToText+ " from "+ username+ " : " + messageText);
						combined.setFill(Color.BLACK);
						combined.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));

						message.add(combined);
						updateOne(username,"MESSAGES");

						updateOne(sendToText,"MESSAGES");
					}
				}
				else if (response.startsWith("MSG:") ){
					String cutResponse = response.substring(4);

					Text messageText = new Text(cutResponse);
					messageText.setFill(Color.BLACK);
					messageText.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));

					Text combined = new Text(username + " : " + messageText + "\n");
					combined.setFill(Color.BLACK);
					combined.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));

					message.add(combined);

					System.out.println("Serveur : MSG recu du client "+ username +" : "+ cutResponse);
					updateAll("MESSAGES");
				}
				// else if (response.startsWith("COLOR:")){
				// 	String cutResponse = response.substring(6);
				// 	if(checkColor(cutResponse)){
				//
				// 	}
				// }
				else{

					switch(response.toUpperCase()){
						case "LOGOUT": deconnexionUser();break;
						case "USERS" :
							nb = reader.readInt();

							ArrayToSend = new ArrayList<Text>(user.keySet());
							writer.writeObject(ArrayToSend);
							writer.flush();
							System.out.println("Server : envoie array : " + ArrayToSend + " -> " + username);
							break;

						case "MESSAGES":
							nb = reader.readInt();

							ArrayList<Text> temp = filterArray(username);
							ArrayToSend = new ArrayList<>(temp.subList(nb,temp.size()));
							writer.writeObject(ArrayToSend);
							writer.flush();
							System.out.println("Server : envoie array : " + ArrayToSend + " -> " + username);
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
		user.remove(username);
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

	private ArrayList<Text> filterArray(Text user){
		ArrayList<Text> response = new ArrayList<>();
		for(Text t : message){
			String m = t.getText();
			if (m.startsWith("@")){
				String cutResponse = m.substring(1);
				String[] splitted = cutResponse.split(" from ",2);
				if(splitted[0].equals(user) || splitted[1].equals(user)){
					response.add(t);
				}
			}
			else{
				response.add(t);
			}
		}
		return response;
	}

	private Text checkUser(String username){
		for(Map.Entry<Text,PrintWriter> u : user.entrySet()){
			if(u.getKey().getText().equals(username)){
				return u.getKey();
			}
		}
		return null;
	}
}
