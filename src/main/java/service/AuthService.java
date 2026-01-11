package service;

import dao.CompteDAO;
import model.Compte;

public class AuthService {

    private static Compte utilisateurConnecte;
    private final CompteDAO dao = new CompteDAO();

    public boolean login(String login, String mdp) {
        utilisateurConnecte = dao.authentifier(login, mdp);
        return utilisateurConnecte != null;
    }

    public static Compte getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public static void logout() {
        utilisateurConnecte = null;
    }
}
