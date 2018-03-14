import java.io.IOException;
import java.lang.ClassNotFoundException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class ClientAttente implements Runnable{

	private Socket sock = null;
	private ObjectOutputStream writer = null;
	private ObjectInputStream reader = null;
	private PrintWriter writerString;
	private BufferedInputStream readerString;
	private ObservableList<String> other;
	private ObservableList<String> message;
	private ObservableList<UserData> user;
	private String name;
	private int status;

		public ClientAttente(Socket sock, String name,ObservableList<String> other, ObservableList<String> message, ObservableList<UserData> user, ObjectOutputStream writer, ObjectInputStream reader,PrintWriter writerString, BufferedInputStream readerString){
		this.sock = sock;
		this.name = name;

		this.other = other;
		this.message = message;
		this.user = user;

		this.writer = writer;
		this.reader = reader;

		this.writerString = writerString;
		this.readerString = readerString;

		this.status = 1;
	}

	public void run(){
		while(!sock.isClosed()){
			try {
				/* Le client attend une reponse */
				System.out.println(name + " : Attend un message du serveur : ");
				String response = read();
				System.out.println(name + " : Recoit un message du serveur : " + response);
				process(response);
				response = "";
			} catch (IOException e) {
					e.printStackTrace();
			}
		}
		// User has bee disconnected (lost connection?, remote closed?)
		System.out.println("Socket closed, lost connection?");
		switch(status){
			case 0: other.add("!DISCONNECT");break;
			case 1: other.add("!LOST");break;
		}
		user.clear();
		writer = null;
		reader = null;
		writerString = null;
		readerString = null;

	}

	private void process(String str){
		switch(str){
			case "USERSMESSAGES":
				processBoth();
				break;
			case "MESSAGESUSERS":
				processBoth();
				break;
			case "USERS" :
				processUser();
				break;
			case "MESSAGES" :
				processMessage();
				break;
			case "LOGOUT : OK" :
				System.out.println(name + ": Le serveur a bien coupe la connexion");
				status  = 0;
				writer = null;
				reader = null;
				writerString = null;
				readerString = null;

				try{
					sock.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				user.clear();
				message.clear();
				other.clear();

				break;
			case "LOGOUT : KO" :
				status = 1;
				System.out.println(name + ": Le serveur n'a pas bien coupe la connexion");
				user.clear();
				message.clear();
				other.clear();
				break;
		}
	}


	//Method to read string array (messages)
	private ArrayList<String> readMessageArray() throws IOException{
		ArrayList<String> list = new ArrayList<String>();

		try{
			Object object = reader.readObject();
			list = (ArrayList<String>) object;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(name + " : On lit une array : "+ list);
		return list;
	}

	//Method to read UserData array (user)
	private ArrayList<UserData> readUserArray() throws IOException{
		ArrayList<UserData> list = new ArrayList<UserData>();

		try{
			Object object = reader.readObject();
			list = (ArrayList<UserData>) object;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(name + " : On lit une array : "+ list);
		return list;
	}

	//Method to read string since readUTF() not working well
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

	private void processUser(){
		try{
			System.out.println(name + " : USERS");
			writerString.write("USERS");
			writerString.flush();

			//System.out.println(name + " : Users -> on envoie la taille de notre liste d'user, taille : " + user.size());
			//writer.writeInt(user.size());
			//writer.flush();

			user.setAll(readUserArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processMessage(){
		try{
			System.out.println(name + " : MESSAGES");
			writerString.write("MESSAGES");
			writerString.flush();

			System.out.println(name + " : Messages -> on envoie la taille de notre liste de message, taille : " + message.size());
			writer.writeInt(message.size());
			writer.flush();

			ArrayList<String> test = readMessageArray();
			message.addAll(test);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processBoth(){
		processUser();
		processMessage();
	}
}
