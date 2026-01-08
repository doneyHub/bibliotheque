package service;

import dao.UtilisateurDAO;
import model.utilisateur.Utilisateur;

import java.util.ArrayList;

public class UtilisateurService {

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public void ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateurDAO.ajouterUtilisateur(utilisateur);
    }

    public Utilisateur getUtilisateurParId(int id) {
        return utilisateurDAO.getUtilisateurParId(id);
    }

    public ArrayList<Utilisateur> getTousLesUtilisateurs() {
        return utilisateurDAO.getTousLesUtilisateurs();
    }
}
