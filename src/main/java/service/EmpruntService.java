package service;

import dao.EmpruntDAO;
import dao.LivreDAO;
import dao.UtilisateurDAO;
import model.Livre;
import model.utilisateur.Utilisateur;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class EmpruntService {

    private final EmpruntDAO empruntDAO = new EmpruntDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private final LivreDAO livreDAO = new LivreDAO();

    public boolean emprunterLivre(int idUtilisateur, int idLivre) throws SQLException {

        Utilisateur utilisateur = utilisateurDAO.getUtilisateurParId(idUtilisateur);
        Livre livre = livreDAO.getLivreParId(idLivre);

        if (utilisateur == null || livre == null) return false;
        if (livre.getQuantiteDisponible() <= 0) return false;

        int empruntsActifs = empruntDAO.compterEmpruntsActifs(idUtilisateur);
        if (empruntsActifs >= utilisateur.getNombreMaxEmprunts()) return false;

        LocalDate aujourdHui = LocalDate.now();
        empruntDAO.ajouterEmprunt(idUtilisateur, idLivre,
                aujourdHui, aujourdHui.plusDays(14));

        livreDAO.decrementerStock(idLivre);
        return true;
    }

    public int calculerPenalite(LocalDate dateRetourPrevue,
                                LocalDate dateRetourEffective) {

        if (dateRetourEffective.isAfter(dateRetourPrevue)) {
            return (int) ChronoUnit.DAYS.between(
                    dateRetourPrevue,
                    dateRetourEffective
            );
        }
        return 0;
    }

    public void retournerLivre(int idEmprunt) {

        var emprunt = empruntDAO.getEmpruntParId(idEmprunt);

        if (emprunt == null) {
            throw new IllegalArgumentException("Emprunt introuvable");
        }

        LocalDate dateRetour = LocalDate.now();

        int penalite = calculerPenalite(
                emprunt.getDateRetourPrevue(),
                dateRetour
        );

        empruntDAO.cloturerEmprunt(
                idEmprunt,
                dateRetour,
                penalite
        );

        // remettre le livre en stock
        livreDAO.incrementerStock(
                emprunt.getLivre().getIdLivre()
        );
    }


}
