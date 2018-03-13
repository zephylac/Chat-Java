import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.Serializable;


public	class UserData implements Serializable {

	private String username;
	private Color color;

	private  static  final  long serialVersionUID = 5318518731L;

	public UserData(String username, Color color){
		this.username = username;
		this.color = color;
	}

	public void setColor(Color color){
		this.color = color;
	}

	public Color getColor(){
		return color;
	}

	public String getUsername(){
		return username;
	}

	public String toString(){
		return "nom : " + username + ", couleur : " + color.toString();
	}

	private	void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {

			double red = ois.readDouble();
			double green = ois.readDouble();
			double blue = ois.readDouble();
			double opacity = ois.readDouble();

			this.username = ois.readUTF() ;
			this.color = new Color(red,green,blue,opacity);
	}

	private	void writeObject(ObjectOutputStream oos) throws IOException {

			oos.writeDouble(color.getRed()) ;
			oos.writeDouble(color.getGreen()) ;
			oos.writeDouble(color.getBlue()) ;
			oos.writeDouble(color.getOpacity()) ;

			oos.writeUTF(username) ;

	}
}
