package service;

import dao.EmpruntDAO;
import model.Emprunt;

import java.io.FileWriter;
import java.util.List;

public class ExportTXTService {

    public boolean exporterTXT(String chemin) {

        List<Emprunt> emprunts = new EmpruntDAO().getHistorique();

        try (FileWriter fw = new FileWriter(chemin)) {

            fw.write("HISTORIQUE DES EMPRUNTS\n");
            fw.write("======================\n\n");

            for (Emprunt e : emprunts) {

                fw.write(
                        "Emprunt # : " + e.getIdEmprunt() + "\n" +
                                "Utilisateur : " + e.getUtilisateur().getIdUtilisateur() + "\n" +
                                "Livre : " + e.getLivre().getIdLivre() + "\n" +
                                "Date : " + e.getDateEmprunt() + "\n" +
                                "Retour : " + e.getDateRetourEffective() + "\n" +
                                "Pénalité : " + e.getPenalite() + " jour(s)\n" +
                                "-----------------------------\n"
                );
            }

            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
