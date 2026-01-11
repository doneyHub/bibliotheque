package dao;
import model.Livre;
import java.sql.*;
import java.util.ArrayList;
public class LivreDAO {

    // ========================================
    // AJOUT D’UN LIVRE
    // ========================================
    public void ajouterLivre(Livre livre) {

        String sql = """
    INSERT INTO livres(titre, auteur, isbn, quantite_totale, quantite_disponible)
    VALUES (?, ?, ?, ?, ?)
""";

        Connection connexion = ConnexionBD.getConnexion();
        if (connexion == null) {
            System.out.println("❌ Connexion BD indisponible");
            return;
        }

        try (PreparedStatement ps = connexion.prepareStatement(sql)) {

            ps.setString(1, livre.getTitre());
            ps.setString(2, livre.getAuteur());
            ps.setString(3, livre.getIsbn());
            ps.setInt(4, livre.getQuantiteTotale());
            ps.setInt(5, livre.getQuantiteDisponible());

            ps.executeUpdate();
            System.out.println("✔ Livre ajouté");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ========================================
    // RÉCUPÉRER UN LIVRE PAR ID
    // ========================================
    public Livre getLivreParId(int idLivre) {

        String sql = "SELECT * FROM livres WHERE id_livre = ?";

        try (Connection connexion = ConnexionBD.getConnexion();
             PreparedStatement ps = connexion.prepareStatement(sql)) {

            ps.setInt(1, idLivre);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Livre(
                        rs.getInt("id_livre"),
                        rs.getString("titre"),
                        rs.getString("auteur"),
                        rs.getString("isbn"),
                        rs.getInt("quantite_totale"),
                        rs.getInt("quantite_disponible")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ========================================
    // LISTE DE TOUS LES LIVRES
    // ========================================
    public ArrayList<Livre> getTousLesLivres() {

        ArrayList<Livre> liste = new ArrayList<>();
        String sql = "SELECT * FROM livres";

        try (Connection connexion = ConnexionBD.getConnexion();
             Statement st = connexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                liste.add(new Livre(
                        rs.getInt("id_livre"),
                        rs.getString("titre"),
                        rs.getString("auteur"),
                        rs.getString("isbn"),
                        rs.getInt("quantite_totale"),
                        rs.getInt("quantite_disponible")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }

    // ========================================
    // DIMINUER LE STOCK (EMPRUNT)
    // ========================================
    public boolean decrementerStock(int idLivre) {

        String sql = """
        UPDATE livres
        SET quantite_disponible = quantite_disponible - 1
        WHERE id_livre = ?
        AND quantite_disponible > 0
    """;

        try (Connection connexion = ConnexionBD.getConnexion();
             PreparedStatement ps = connexion.prepareStatement(sql)) {

            ps.setInt(1, idLivre);
            int rows = ps.executeUpdate(); // nombre de lignes affectées
            return rows > 0;               // true si succès, false sinon

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ========================================
    // AUGMENTER LE STOCK (RETOUR)
    // ========================================
    public boolean incrementerStock(int idLivre) {

        String sql = """
        UPDATE livres
        SET quantite_disponible = quantite_disponible + 1
        WHERE id_livre = ?
    """;

        try (Connection c = ConnexionBD.getConnexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idLivre);
            int rows = ps.executeUpdate();
            return rows > 0;  // true si succès, false sinon

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean decrementerStock(Connection c, int idLivre) throws SQLException {

        String sql = """
    UPDATE livres
    SET quantite_disponible = quantite_disponible - 1
    WHERE id_livre = ?
    AND quantite_disponible > 0
""";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idLivre);
            return ps.executeUpdate() == 1;
        }
    }

    public void incrementerStock(Connection c, int idLivre) throws SQLException {

        String sql = """
    UPDATE livres
    SET quantite_disponible = quantite_disponible + 1
    WHERE id_livre = ?
""";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idLivre);
            ps.executeUpdate();
        }
    }

    public ArrayList<Livre> rechercher(String motCle) {

        ArrayList<Livre> liste = new ArrayList<>();

        String sql = """
    SELECT * FROM livres
    WHERE titre LIKE ?
       OR auteur LIKE ?
       OR isbn LIKE ?
""";

        try (Connection c = ConnexionBD.getConnexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            String mc = "%" + motCle + "%";
            ps.setString(1, mc);
            ps.setString(2, mc);
            ps.setString(3, mc);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                liste.add(new Livre(
                        rs.getInt("id_livre"),
                        rs.getString("titre"),
                        rs.getString("auteur"),
                        rs.getString("isbn"),
                        rs.getInt("quantite_totale"),
                        rs.getInt("quantite_disponible")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return liste;
    }

    public void modifierLivre(Livre l) {

        String sql = """
    UPDATE livres
    SET titre = ?, auteur = ?, isbn = ?,
        quantite_totale = ?, quantite_disponible = ?
    WHERE id_livre = ?
""";

        try (Connection c = ConnexionBD.getConnexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, l.getTitre());
            ps.setString(2, l.getAuteur());
            ps.setString(3, l.getIsbn());
            ps.setInt(4, l.getQuantiteTotale());
            ps.setInt(5, l.getQuantiteDisponible());
            ps.setInt(6, l.getIdLivre());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void supprimerLivre(int idLivre) {

        String sql = "DELETE FROM livres WHERE id_livre = ?";

        try (Connection c = ConnexionBD.getConnexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idLivre);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}