package service;

import dao.LivreDAO;
import model.Livre;

import java.util.ArrayList;

public class LivreService {

    private final LivreDAO livreDAO = new LivreDAO();

    public void ajouterLivre(Livre livre) {
        livreDAO.ajouterLivre(livre);
    }

    public Livre getLivreParId(int id) {
        return livreDAO.getLivreParId(id);
    }

    public ArrayList<Livre> getTousLesLivres() {
        return livreDAO.getTousLesLivres();
    }
}
