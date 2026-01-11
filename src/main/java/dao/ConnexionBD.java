package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBD {

    // ✅ J'ai mis "biblio" ici comme tu l'as demandé
    private static final String URL = "jdbc:mysql://localhost:3306/biblio?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String USER = "root";

    // ⚠ ATTENTION : Si tu es sur XAMPP (Windows), laisse vide "".
    // Si tu es sur MAMP (Mac) ou Workbench, mets "root".
    private static final String PASSWORD = "root";

    public static Connection getConnexion() {
        try {
            Connection c = DriverManager.getConnection(URL, USER, PASSWORD);
            // Ce message s'affichera dans la console d'IntelliJ si ça marche
            System.out.println("--- Connexion à 'biblio' OK ---");
            return c;
        } catch (SQLException e) {
            System.err.println("❌ ERREUR DE CONNEXION SQL !");
            System.err.println("Vérifie : 1. Le nom de la base (biblio ?)");
            System.err.println("          2. Le mot de passe (root ou vide ?)");
            System.err.println("          3. Si WAMP/XAMPP est allumé.");
            e.printStackTrace();
            return null;
        }
    }
}