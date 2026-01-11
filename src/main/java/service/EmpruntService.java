package service;
import dao.*;
import model.*;
import model.utilisateur.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class EmpruntService {
    private final EmpruntDAO empruntDAO = new EmpruntDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private final LivreDAO livreDAO = new LivreDAO();

    public ResultatEmprunt emprunterLivre(int idUtilisateur, int idLivre) {
        Utilisateur u = utilisateurDAO.getUtilisateurParId(idUtilisateur);
        if (u == null) return ResultatEmprunt.UTILISATEUR_INEXISTANT;
        Livre l = livreDAO.getLivreParId(idLivre);
        if (l == null) return ResultatEmprunt.LIVRE_INEXISTANT;
        if (empruntDAO.utilisateurAPenaliteNonPayee(idUtilisateur)) return ResultatEmprunt.PENALITE_NON_REGLEE;
        if (l.getQuantiteDisponible() <= 0) return ResultatEmprunt.LIVRE_INDISPONIBLE;

        int nb = empruntDAO.compterEmpruntsActifs(idUtilisateur);
        if (u instanceof Etudiant && nb >= 3) return ResultatEmprunt.LIMITE_ETUDIANT_ATTEINTE;
        if (u instanceof Enseignant && nb >= 5) return ResultatEmprunt.LIMITE_ENSEIGNANT_ATTEINTE;

        empruntDAO.ajouterEmprunt(idUtilisateur, idLivre, LocalDate.now(), LocalDate.now().plusDays(14));
        livreDAO.decrementerStock(idLivre);
        return ResultatEmprunt.SUCCES;
    }

    public int retournerLivre(int idEmprunt) {
        Connection c = null;
        try {
            c = ConnexionBD.getConnexion();
            c.setAutoCommit(false);
            Emprunt e = empruntDAO.getEmpruntParId(idEmprunt);
            if (e == null || e.getDateRetourEffective() != null) return -1;

            LocalDate retour = LocalDate.now();
            int penalite = 0;
            if (retour.isAfter(e.getDateRetourPrevue())) {
                penalite = (int) ChronoUnit.DAYS.between(e.getDateRetourPrevue(), retour);
            }
            empruntDAO.cloturerEmprunt(c, idEmprunt, retour, penalite);
            livreDAO.incrementerStock(c, e.getLivre().getIdLivre());
            c.commit();
            return penalite;
        } catch (Exception e) {
            try{if(c!=null)c.rollback();}catch(Exception ex){}
            return -1;
        }
    }

    public boolean payerPenalite(int id) { return empruntDAO.payerPenalite(id); }
    public List<Emprunt> getHistorique() { return empruntDAO.getHistorique(); }
    public ArrayList<Emprunt> getEmpruntsEnCours() { return empruntDAO.getEmpruntsEnCours(); }
    public ArrayList<Emprunt> getEmpruntsEnRetard() { return empruntDAO.getEmpruntsEnRetard(); }
    public ArrayList<TopUtilisateur> getUtilisateursLesPlusActifs() { return empruntDAO.getUtilisateursLesPlusActifs(); }
    public int getNbEmpruntsActifs() { return empruntDAO.compterEmpruntsActifs(0); } // 0 = tous (si modif DAO) ou utiliser size()
}