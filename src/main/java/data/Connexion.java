package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import controler.ReadyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConfig;

public class Connexion {
	private final static Logger LOG = LoggerFactory.getLogger(Connexion.class);
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
			LOG.info("Connexion à " + DBPath + " avec succès");

			SQLiteConfig config = new SQLiteConfig();  
			config.enforceForeignKeys(true);  
			connection = DriverManager.getConnection("jdbc:sqlite:" + DBPath,config.toProperties());

		} catch (ClassNotFoundException e) {
            LOG.error("Librairie SQLite non trouvé.");
		} catch (SQLException e) {
            LOG.error("Erreur lors de la connexion à la base de données");
		}
	}

	public void close() {
		try {
			connection.close();
			statement.close();
            LOG.info("Fermeture de la connexion");
		} catch (SQLException e) {
            LOG.error("Erreur lors de la fermeture de la connexion");
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
			LOG.error("Erreur dans la requête : " + requet);
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
