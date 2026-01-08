package dao;

import model.Emprunt;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmpruntDAO {

    // ========================================
    // Champ pour accéder aux livres
    // ========================================
    private LivreDAO livreDAO;

    // ========================================
    // Constructeur
    // ========================================
    public EmpruntDAO() {
        this.livreDAO = new LivreDAO();
    }

    // ========================================
    // 1. AJOUT D’UN EMPRUNT (OBJET)
    // ========================================
    public void ajouterEmprunt(Emprunt emprunt) {

        String sql = """
            INSERT INTO emprunts(id_utilisateur, id_livre, date_emprunt, date_retour_prevue)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection connexion = ConnexionBD.getConnexion();
             PreparedStatement ps = connexion.prepareStatement(sql)) {

            ps.setInt(1, emprunt.getUtilisateur().getIdUtilisateur());
            ps.setInt(2, emprunt.getLivre().getIdLivre());
            ps.setDate(3, Date.valueOf(emprunt.getDateEmprunt()));
            ps.setDate(4, Date.valueOf(emprunt.getDateRetourPrevue()));

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ========================================
    // 2. AJOUT D’UN EMPRUNT (PAR IDENTIFIANTS)
    // ========================================
    public void ajouterEmprunt(int idUtilisateur,
                               int idLivre,
                               LocalDate dateEmprunt,
                               LocalDate dateRetourPrevue) {

        String sql = """
            INSERT INTO emprunts(id_utilisateur, id_livre, date_emprunt, date_retour_prevue)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection connexion = ConnexionBD.getConnexion();
             PreparedStatement ps = connexion.prepareStatement(sql)) {

            ps.setInt(1, idUtilisateur);
            ps.setInt(2, idLivre);
            ps.setDate(3, Date.valueOf(dateEmprunt));
            ps.setDate(4, Date.valueOf(dateRetourPrevue));

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ========================================
    // 3. COMPTER LES EMPRUNTS ACTIFS
    // ========================================
    public int compterEmpruntsActifs(int idUtilisateur) {

        String sql = """
            SELECT COUNT(*)
            FROM emprunts
            WHERE id_utilisateur = ?
            AND date_retour_effective IS NULL
        """;

        try (Connection connexion = ConnexionBD.getConnexion();
             PreparedStatement ps = connexion.prepareStatement(sql)) {

            ps.setInt(1, idUtilisateur);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ========================================
    // 4. ENREGISTRER LE RETOUR D’UN EMPRUNT
    // ========================================
    public void enregistrerRetour(int idEmprunt,
                                  LocalDate dateRetourEffective,
                                  int penalite) {

        String sql = """
            UPDATE emprunts
            SET date_retour_effective = ?, penalite = ?
            WHERE id_emprunt = ?
        """;

        try (Connection connexion = ConnexionBD.getConnexion();
             PreparedStatement ps = connexion.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(dateRetourEffective));
            ps.setInt(2, penalite);
            ps.setInt(3, idEmprunt);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ========================================
    // 5. LISTE DE TOUS LES EMPRUNTS
    // ========================================
    public ArrayList<Emprunt> getTousLesEmprunts() {

        ArrayList<Emprunt> liste = new ArrayList<>();
        String sql = "SELECT * FROM emprunts";

        try (Connection connexion = ConnexionBD.getConnexion();
             Statement st = connexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Emprunt emprunt = new Emprunt(
                        rs.getInt("id_emprunt"),
                        null,   // utilisateur reconstruit dans le service
                        null,   // livre reconstruit dans le service
                        rs.getDate("date_emprunt").toLocalDate(),
                        rs.getDate("date_retour_prevue").toLocalDate()
                );

                liste.add(emprunt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }

    // ========================================
    // 6. RECUPERER UN EMPRUNT PAR ID
    // ========================================
    public Emprunt getEmpruntParId(int idEmprunt) {

        String sql = "SELECT * FROM emprunts WHERE id_emprunt = ?";

        try (Connection c = ConnexionBD.getConnexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idEmprunt);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Emprunt(
                        rs.getInt("id_emprunt"),
                        null,
                        livreDAO.getLivreParId(rs.getInt("id_livre")), // ✅ corrigé
                        rs.getDate("date_emprunt").toLocalDate(),
                        rs.getDate("date_retour_prevue").toLocalDate()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ========================================
// CLÔTURER UN EMPRUNT (RETOUR)
// ========================================
    public void cloturerEmprunt(int idEmprunt,
                                LocalDate dateRetourEffective,
                                int penalite) {

        String sql = """
        UPDATE emprunts
        SET date_retour_effective = ?, penalite = ?
        WHERE id_emprunt = ?
    """;

        try (Connection connexion = ConnexionBD.getConnexion();
             PreparedStatement ps = connexion.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(dateRetourEffective));
            ps.setInt(2, penalite);
            ps.setInt(3, idEmprunt);

            ps.executeUpdate();
            System.out.println("✔ Emprunt clôturé");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
