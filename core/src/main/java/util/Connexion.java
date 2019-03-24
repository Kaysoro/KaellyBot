package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import data.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConfig;

public class Connexion {
	private final static Logger LOG = LoggerFactory.getLogger(Connexion.class);
	private static Connexion instance = null;
	private Connection connection = null;
	private Statement statement = null;

	public void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + Constants.database);

			statement = connection.createStatement();
			LOG.info("Connexion à " + Constants.database + " avec succès");

			SQLiteConfig config = new SQLiteConfig();  
			config.enforceForeignKeys(true);  
			connection = DriverManager.getConnection("jdbc:sqlite:" + Constants.database,config.toProperties());

		} catch (ClassNotFoundException e) {
			Reporter.report(e);
            LOG.error("Librairie SQLite non trouvé.");
		} catch (SQLException e) {
			Reporter.report(e);
            LOG.error("Erreur lors de la connexion à la base de données");
		}
	}

	public void close() {
		try {
			connection.close();
			statement.close();
            LOG.info("Fermeture de la connexion");
		} catch (SQLException e) {
			Reporter.report(e);
            LOG.error("Erreur lors de la fermeture de la connexion");
		}
	}
	
	public synchronized static Connexion getInstance(){
		if (instance == null) {
			instance = new Connexion();
			instance.connect();
		}
		return instance;
	}

	public ResultSet query(String requet) {
		ResultSet resultat = null;
		try {
			resultat = statement.executeQuery(requet);
		} catch (SQLException e) {
			Reporter.report(e);
			LOG.error("Erreur dans la requête : " + requet);
		}
		return resultat;
	}

	public Connection getConnection(){
		return connection;
	}
}
