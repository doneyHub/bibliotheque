package service;

import dao.EmpruntDAO;
import model.Emprunt;

import java.io.FileWriter;
import java.util.List;

public class ExportService {

    private final EmpruntDAO dao = new EmpruntDAO();

    // =========================
    // EXPORT CSV
    // =========================
    public boolean exporterCSV(String chemin) {

        List<Emprunt> emprunts = dao.getHistorique();

        try (FileWriter fw = new FileWriter(chemin)) {

            fw.write("ID;Utilisateur;Livre;DateEmprunt;DateRetour;Penalite\n");

            for (Emprunt e : emprunts) {

                fw.write(
                        e.getIdEmprunt() + ";" +
                                e.getUtilisateur().getIdUtilisateur() + ";" +
                                e.getLivre().getIdLivre() + ";" +
                                e.getDateEmprunt() + ";" +
                                e.getDateRetourEffective() + ";" +
                                e.getPenalite() + "\n"
                );
            }

            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
