package dao;

import model.Emprunt;
import model.Livre;
import model.TopUtilisateur;
import model.utilisateur.Enseignant;
import model.utilisateur.Etudiant;
import model.utilisateur.Utilisateur;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmpruntDAO {

    // Méthode utilitaire pour mapper le ResultSet complet avec les JOINs
    // Cela évite les NullPointerExceptions dans l'interface
    private Emprunt mapRow(ResultSet rs) throws SQLException {
        // Reconstruction Utilisateur
        Utilisateur u;
        if ("ETUDIANT".equals(rs.getString("type_utilisateur"))) {
            u = new Etudiant(rs.getInt("id_utilisateur"), rs.getString("nom_u"), rs.getString("prenom_u"), rs.getString("matricule"));
        } else {
            u = new Enseignant(rs.getInt("id_utilisateur"), rs.getString("nom_u"), rs.getString("prenom_u"), rs.getString("matricule"));
        }

        // Reconstruction Livre
        Livre l = new Livre(rs.getInt("id_livre"), rs.getString("titre"), rs.getString("auteur"), rs.getString("isbn"), 0, 0);

        return new Emprunt(
                rs.getInt("id_emprunt"),
                u,
                l,
                rs.getDate("date_emprunt").toLocalDate(),
                rs.getDate("date_retour_prevue").toLocalDate(),
                rs.getDate("date_retour_effective") != null ? rs.getDate("date_retour_effective").toLocalDate() : null,
                rs.getInt("penalite"),
                rs.getBoolean("penalite_payee")
        );
    }

    // Requete de base avec JOIN pour tout récupérer
    private static final String SQL_SELECT_FULL = """
        SELECT e.*, 
               u.nom as nom_u, u.prenom as prenom_u, u.matricule, u.type_utilisateur,
               l.titre, l.auteur, l.isbn
        FROM emprunts e
        JOIN utilisateurs u ON e.id_utilisateur = u.id_utilisateur
        JOIN livres l ON e.id_livre = l.id_livre
    """;

    public void ajouterEmprunt(int idUtilisateur, int idLivre, LocalDate dateEmprunt, LocalDate dateRetourPrevue) {
        String sql = "INSERT INTO emprunts(id_utilisateur, id_livre, date_emprunt, date_retour_prevue) VALUES (?, ?, ?, ?)";
        try (Connection c = ConnexionBD.getConnexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            ps.setInt(2, idLivre);
            ps.setDate(3, Date.valueOf(dateEmprunt));
            ps.setDate(4, Date.valueOf(dateRetourPrevue));
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void ajouterEmprunt(Connection c, int idUtilisateur, int idLivre, LocalDate dateEmprunt, LocalDate dateRetourPrevue) throws SQLException {
        String sql = "INSERT INTO emprunts(id_utilisateur, id_livre, date_emprunt, date_retour_prevue) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            ps.setInt(2, idLivre);
            ps.setDate(3, Date.valueOf(dateEmprunt));
            ps.setDate(4, Date.valueOf(dateRetourPrevue));
            ps.executeUpdate();
        }
    }

    public int compterEmpruntsActifs(int idUtilisateur) {
        String sql = "SELECT COUNT(*) FROM emprunts WHERE id_utilisateur = ? AND date_retour_effective IS NULL";
        try (Connection c = ConnexionBD.getConnexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { return 0; }
    }

    public ArrayList<Emprunt> getHistorique() {
        ArrayList<Emprunt> liste = new ArrayList<>();
        String sql = SQL_SELECT_FULL + " ORDER BY date_emprunt DESC";
        try (Connection c = ConnexionBD.getConnexion(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) liste.add(mapRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return liste;
    }

    public ArrayList<Emprunt> getEmpruntsEnCours() {
        ArrayList<Emprunt> liste = new ArrayList<>();
        String sql = SQL_SELECT_FULL + " WHERE date_retour_effective IS NULL ORDER BY date_emprunt DESC";
        try (Connection c = ConnexionBD.getConnexion(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) liste.add(mapRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return liste;
    }

    public ArrayList<Emprunt> getEmpruntsEnRetard() {
        ArrayList<Emprunt> liste = new ArrayList<>();
        String sql = SQL_SELECT_FULL + " WHERE date_retour_effective IS NULL AND date_retour_prevue < CURRENT_DATE";
        try (Connection c = ConnexionBD.getConnexion(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) liste.add(mapRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return liste;
    }

    public Emprunt getEmpruntParId(int id) {
        String sql = SQL_SELECT_FULL + " WHERE id_emprunt = ?";
        try (Connection c = ConnexionBD.getConnexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public void cloturerEmprunt(Connection c, int idEmprunt, LocalDate dateRetour, int penalite) throws SQLException {
        String sql = "UPDATE emprunts SET date_retour_effective = ?, penalite = ? WHERE id_emprunt = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(dateRetour));
            ps.setInt(2, penalite);
            ps.setInt(3, idEmprunt);
            ps.executeUpdate();
        }
    }

    public boolean payerPenalite(int idEmprunt) {
        String sql = "UPDATE emprunts SET penalite_payee = TRUE WHERE id_emprunt = ? AND penalite > 0";
        try (Connection c = ConnexionBD.getConnexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idEmprunt);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    public boolean utilisateurAPenaliteNonPayee(int idUtilisateur) {
        String sql = "SELECT COUNT(*) FROM emprunts WHERE id_utilisateur = ? AND penalite > 0 AND penalite_payee = FALSE";
        try (Connection c = ConnexionBD.getConnexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) { return false; }
    }

    public ArrayList<TopUtilisateur> getUtilisateursLesPlusActifs() {
        ArrayList<TopUtilisateur> liste = new ArrayList<>();
        String sql = """
            SELECT u.nom, u.prenom, COUNT(e.id_emprunt) as total
            FROM emprunts e
            JOIN utilisateurs u ON e.id_utilisateur = u.id_utilisateur
            GROUP BY u.id_utilisateur
            ORDER BY total DESC
            LIMIT 10
        """;
        try (Connection c = ConnexionBD.getConnexion(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new TopUtilisateur(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getInt("total")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return liste;
    }

    // Stats simples
    public int countEmpruntsActifs() { return count("SELECT COUNT(*) FROM emprunts WHERE date_retour_effective IS NULL"); }
    public int countRetards() { return count("SELECT COUNT(*) FROM emprunts WHERE date_retour_effective IS NULL AND date_retour_prevue < CURRENT_DATE"); }
    public int sumPenalites() { return count("SELECT COALESCE(SUM(penalite),0) FROM emprunts"); }

    private int count(String sql) {
        try (Connection c = ConnexionBD.getConnexion(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) { return 0; }
    }
}