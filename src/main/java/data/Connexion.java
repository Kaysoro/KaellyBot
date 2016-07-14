package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.sqlite.SQLiteConfig;

public class Connexion {
	private static String DBPath = "Chemin aux base de donnée SQLite";
	private static Connexion instance = null;
	private Connection connection = null;
	private Statement statement = null;

	private Connexion(String dBPath) {
		DBPath = dBPath;
	}

	public void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + DBPath);

			statement = connection.createStatement();
			System.out.println("Connexion à " + DBPath + " avec succès");

			SQLiteConfig config = new SQLiteConfig();  
			config.enforceForeignKeys(true);  
			connection = DriverManager.getConnection("jdbc:sqlite:" + DBPath,config.toProperties());

		} catch (ClassNotFoundException notFoundException) {
			notFoundException.printStackTrace();
			System.out.println("Erreur de connexion");
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			System.out.println("Erreur de connexion");
		}
	}

	public void close() {
		try {
			connection.close();
			statement.close();
			System.out.println("Fermeture de la connexion");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Connexion getInstance(){
		if (instance == null)
			instance = new Connexion(Connexion.DBPath);
		return instance;
	}

	public ResultSet query(String requet) {
		ResultSet resultat = null;
		try {
			resultat = statement.executeQuery(requet);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Erreur dans la requête : " + requet);
		}
		return resultat;

	}

	public Connection getConnection(){
		return connection;
	}
	
	public static void setDBPath(String DBPath){
		Connexion.DBPath = DBPath;
	}
}
