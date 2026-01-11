package dao;
import model.utilisateur.*;
import java.sql.*;
import java.util.ArrayList;
public class UtilisateurDAO {

    // ===============================
    // AJOUT UTILISATEUR
    // ===============================
    public void ajouterUtilisateur(Utilisateur utilisateur) {

        String sql = """
        INSERT INTO utilisateurs(nom, prenom, matricule, type_utilisateur)
        VALUES (?, ?, ?, ?)
    """;

        try (Connection c = ConnexionBD.getConnexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, utilisateur.getNom());
            ps.setString(2, utilisateur.getPrenom());
            ps.setString(3, utilisateur.getMatricule());
            ps.setString(4,
                    utilisateur instanceof Etudiant ? "ETUDIANT" : "ENSEIGNANT");

            ps.executeUpdate();
            System.out.println("✔ Utilisateur ajouté");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ===============================
    // UTILISATEUR PAR ID
    // ===============================
    public Utilisateur getUtilisateurParId(int id) {

        String sql = "SELECT * FROM utilisateurs WHERE id_utilisateur = ?";

        try (Connection c = ConnexionBD.getConnexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String type = rs.getString("type_utilisateur");

                if ("ETUDIANT".equals(type)) {
                    return new Etudiant(
                            id,
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("matricule")
                    );
                } else {
                    return new Enseignant(
                            id,
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("matricule")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ===============================
    // TOUS LES UTILISATEURS
    // ===============================
    public ArrayList<Utilisateur> getTousLesUtilisateurs() {

        ArrayList<Utilisateur> liste = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs";

        try (Connection c = ConnexionBD.getConnexion();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id_utilisateur");
                String type = rs.getString("type_utilisateur");

                if ("ETUDIANT".equals(type)) {
                    liste.add(new Etudiant(
                            id,
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("matricule")
                    ));
                } else {
                    liste.add(new Enseignant(
                            id,
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("matricule")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }

    public ArrayList<Utilisateur> rechercher(String motCle) {

        ArrayList<Utilisateur> liste = new ArrayList<>();

        String sql = """
            SELECT * FROM utilisateurs
            WHERE nom LIKE ?
               OR matricule LIKE ?
        """;

        try (Connection c = ConnexionBD.getConnexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            String mc = "%" + motCle + "%";
            ps.setString(1, mc);
            ps.setString(2, mc);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_utilisateur");
                String type = rs.getString("type_utilisateur");

                Utilisateur u =
                        "ETUDIANT".equals(type)
                                ? new Etudiant(id,
                                rs.getString("nom"),
                                rs.getString("prenom"),
                                rs.getString("matricule"))
                                : new Enseignant(id,
                                rs.getString("nom"),
                                rs.getString("prenom"),
                                rs.getString("matricule"));

                liste.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return liste;
    }

    public void modifierUtilisateur(Utilisateur u) {

        String sql = """
            UPDATE utilisateurs
            SET nom = ?, prenom = ?, matricule = ?, type_utilisateur = ?
            WHERE id_utilisateur = ?
        """;

        try (Connection c = ConnexionBD.getConnexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getMatricule());
            ps.setString(4,
                    u instanceof Etudiant ? "ETUDIANT" : "ENSEIGNANT");
            ps.setInt(5, u.getIdUtilisateur());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void supprimerUtilisateur(int id) {

        String sql = "DELETE FROM utilisateurs WHERE id_utilisateur = ?";

        try (Connection c = ConnexionBD.getConnexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}