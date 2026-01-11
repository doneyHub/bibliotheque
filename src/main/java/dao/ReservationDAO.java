package dao;

import java.sql.*;
public class ReservationDAO {

    public void ajouterReservation(int idU, int idL) {

        String sql = """
        INSERT INTO reservations(id_utilisateur, id_livre, date_reservation)
        VALUES (?, ?, CURRENT_DATE)
    """;

        try (Connection c = ConnexionBD.getConnexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idU);
            ps.setInt(2, idL);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}