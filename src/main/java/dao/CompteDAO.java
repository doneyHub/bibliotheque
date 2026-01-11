package dao;
import model.Compte;
import java.sql.*;
public class CompteDAO {

    public Compte authentifier(String login, String mdp) {

        String sql = """
        SELECT * FROM comptes
        WHERE login = ? AND mot_de_passe = ?
    """;

        try (Connection c = ConnexionBD.getConnexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, login);
            ps.setString(2, mdp);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Compte(
                        rs.getInt("id_compte"),
                        rs.getString("login"),
                        rs.getString("role")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}