package service;
import dao.LivreDAO;
import dao.ReservationDAO;
import model.Livre;

public class ReservationService {
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final LivreDAO livreDAO = new LivreDAO();

    public boolean reserverLivre(int idU, int idL) {
        Livre l = livreDAO.getLivreParId(idL);
        // On ne peut réserver que si le stock est épuisé (logique de réservation)
        if (l != null && l.getQuantiteDisponible() > 0) return false;

        reservationDAO.ajouterReservation(idU, idL);
        return true;
    }
}