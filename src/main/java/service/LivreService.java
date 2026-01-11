package service;
import dao.LivreDAO;
import model.Livre;
import java.util.ArrayList;

public class LivreService {
    private final LivreDAO dao = new LivreDAO();
    public void ajouterLivre(Livre l) { dao.ajouterLivre(l); }
    public ArrayList<Livre> getTousLesLivres() { return dao.getTousLesLivres(); }
    public ArrayList<Livre> rechercher(String m) { return dao.rechercher(m); }
    public void modifier(Livre l) { dao.modifierLivre(l); }
    public void supprimer(int id) { dao.supprimerLivre(id); }
}