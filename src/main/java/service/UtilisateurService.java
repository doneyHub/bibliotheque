package service;
import dao.UtilisateurDAO;
import model.utilisateur.Utilisateur;
import java.util.ArrayList;

public class UtilisateurService {
    private final UtilisateurDAO dao = new UtilisateurDAO();
    public void ajouterUtilisateur(Utilisateur u) { dao.ajouterUtilisateur(u); }
    public Utilisateur getUtilisateurParId(int id) { return dao.getUtilisateurParId(id); }
    public ArrayList<Utilisateur> getTousLesUtilisateurs() { return dao.getTousLesUtilisateurs(); }
    public ArrayList<Utilisateur> rechercher(String m) { return dao.rechercher(m); }
    public void modifier(Utilisateur u) { dao.modifierUtilisateur(u); }
    public void supprimer(int id) { dao.supprimerUtilisateur(id); }
}