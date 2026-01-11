package service;
import dao.*;
import java.util.List;

public class StatistiqueService {
    private final EmpruntDAO eDao = new EmpruntDAO();
    private final LivreDAO lDao = new LivreDAO();
    private final UtilisateurDAO uDao = new UtilisateurDAO();

    public int totalLivres() { return lDao.getTousLesLivres().size(); }
    public int totalUtilisateurs() { return uDao.getTousLesUtilisateurs().size(); }
    public int empruntsEnCours() { return eDao.getEmpruntsEnCours().size(); }
    public int retards() { return eDao.countRetards(); }
    public int empruntsClotures() { return eDao.getHistorique().size() - empruntsEnCours(); }
}