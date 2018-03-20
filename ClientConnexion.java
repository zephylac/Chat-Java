import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.ArrayList;
import java.lang.ClassNotFoundException;
import java.net.ConnectException;

public class ClientConnexion{


	private Socket connexion = null;
	private ObjectOutputStream writer = null;
	private ObjectInputStream reader = null;

	private PrintWriter writerString;
	private BufferedInputStream readerString;

	private String name;
	private String host;
	private int port;
	private Thread t1;
	private boolean estConnecte;

	private ObservableList<String> other;
	private ObservableList<String> message;
	private ObservableList<UserData> user;

	public ClientConnexion(){

		List<String> l1  = new ArrayList<String>();
		List<String> l2  = new ArrayList<String>();
		List<UserData> l3  = new ArrayList<UserData>();

		estConnecte = false;
		this.other = FXCollections.observableList(l1);
		this.message = FXCollections.observableList(l2);
		this.user = FXCollections.observableList(l3);
	}

	// Client send message
	public void envoieMessage(String str){
			if(str.startsWith("@")){
				String[] temp = str.split(" ");
				writerString.write("PM:"+temp[0]+":"+temp[1]);
				writerString.flush();
				System.out.println(name + " : Envoie message prive a "+temp[0] + ": " + temp[1]);
			}
			else if(str.equals("!color")){
				other.add("!COLOR");
				System.out.println(name + " : liste couleur");
			}
			else if(str.equals("!smiley")){
				other.add("!SMILEY");
				System.out.println(name + " : liste smiley");
			}
			else if(str.equals("!help")){
				other.add("!COLOR");
				other.add("!SMILEY");
				System.out.println(name + " : liste help");
			}
			else if(str.startsWith("!color")){
				String temp = str.substring(7);
				writerString.write("COLOR:"+temp);
				writerString.flush();
				System.out.println(name + " : Couleur : "+temp);
				}
			else if(str.equals("!potato")){
				writerString.write("POTATO");
				writerString.flush();
				System.out.println(name + " : potato");
			}
			else if(str.equals("!denis")){
				writerString.write("DENIS");
				writerString.flush();
				System.out.println(name + " : denis");
			}
			else if(str.equals("!pepe")){
				writerString.write("PEPE");
				writerString.flush();
				System.out.println(name + " : pepe");
			}
			else if(str.equals("!nyan")){
				writerString.write("NYAN");
				writerString.flush();
				System.out.println(name + " : nyan");
			}
			else{
				writerString.write("MSG:"+str);
				writerString.flush();
				System.out.println(name + " : Envoie du message = " + str);
			}
	}

	/* The client is trying to connect to the server,
	 * return true if connection is authorised, false if not
	 */
	public int seConnecter(String host, int port, String name){
		this.name = name;
		this.host = host;
		this.port = port;

		try {
			connexion = new Socket(host, port);
			estConnecte = true;
		} catch (UnknownHostException e) {
			estConnecte = false;
		} catch (IOException e) {
			estConnecte = false;
		}
		if(estConnecte){

			try{
				writer = new ObjectOutputStream(connexion.getOutputStream());
				reader = new ObjectInputStream(connexion.getInputStream());

				writerString = new PrintWriter(connexion.getOutputStream(), true);
				readerString = new BufferedInputStream(connexion.getInputStream());

				System.out.println(name + ": Tentative de login au server");
				writerString.write(name);
				writerString.flush();

				String response = read();
				switch(response){
					case "LOGIN : OK" :
						t1 = new Thread(new ClientAttente(connexion,name,other,message,user,writer,reader,writerString,readerString));
						t1.start();
						System.out.println(name + ": Le serveur a autorise le login");
						estConnecte = true;
						System.out.println(name + ": Lancement de l'attente msg");
						message.clear();
						other.clear();
						other.add("!CONNECT");
						return 0;
					case "LOGIN : KO" :
						System.out.println(name + ": Le serveur n'a pas autorise le login");
						estConnecte = false;
						return 1;
					case "LOGIN : AE" :
						System.out.println(name + ": Le serveur n'a pas autorise le login car une personne de ce nom existe deja");
						estConnecte = false;
						return 2;

				}
			} catch (IOException e) {
				estConnecte = false;
				e.printStackTrace();
			}
		}
		return 1;
	}

	/* The client is trying to disconnect from the Server
	 * Return true if it has disconnected successfully, false if not
	*/
	public boolean seDeconnecter(){
		System.out.println(name + ": Tentative de deconnection au serveur");
		writerString.write("LOGOUT");
		writerString.flush();
		return true;
	}

	// Return boolean for connect Status
	public boolean getEstConnecte(){
		return estConnecte;
	}

	//Return other list
	public ObservableList<String> getOther(){
		return other;
	}

	//Return message list
	public ObservableList<String> getMessage(){
		return message;
	}

	//Return user list
	public ObservableList<UserData> getUser(){
		return user;
	}

	// Method to read string since readUTF() doesn't work well
	private String read() throws IOException{
		try{
			String response = "";
			int stream;
			byte[] b = new byte[4096];
			stream = readerString.read(b);
			response = new String(b, 0, stream);
			return response;
		}catch(IOException e){
			e.printStackTrace();
		}
		return "";
	}


}
