package service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import dao.EmpruntDAO;
import model.Emprunt;

import java.io.FileOutputStream;
import java.util.List;

public class ExportPDFService {

    public boolean exporterPDF(String chemin) {

        List<Emprunt> emprunts = new EmpruntDAO().getHistorique();

        if (emprunts == null || emprunts.isEmpty()) {
            System.out.println("Aucun emprunt à exporter");
            return false;
        }

        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(chemin));
            document.open();

            Font titreFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 11);

            Paragraph titre = new Paragraph("Historique des emprunts", titreFont);
            titre.setAlignment(Element.ALIGN_CENTER);
            titre.setSpacingAfter(20);
            document.add(titre);

            for (Emprunt e : emprunts) {

                Paragraph p = new Paragraph();
                p.setFont(normalFont);

                p.add("Emprunt # : " + e.getIdEmprunt() + "\n");
                p.add("Utilisateur : " + e.getUtilisateur().getIdUtilisateur() + "\n");
                p.add("Livre : " + e.getLivre().getIdLivre() + "\n");
                p.add("Date : " + e.getDateEmprunt() + "\n");
                p.add(
                        "Retour : " +
                                (e.getDateRetourEffective() == null
                                        ? "EN COURS"
                                        : e.getDateRetourEffective()) +
                                "\n"
                );
                p.add("Pénalité : " + e.getPenalite() + " jour(s)\n");

                p.setSpacingAfter(15);
                document.add(p);
            }

            document.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
