package model;

import model.utilisateur.Utilisateur;
import java.time.LocalDate;

public class Emprunt {

    private int idEmprunt;
    private Utilisateur utilisateur;
    private Livre livre;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    private int penalite;

    public Emprunt(int idEmprunt, Utilisateur utilisateur, Livre livre,
                   LocalDate dateEmprunt, LocalDate dateRetourPrevue) {
        this.idEmprunt = idEmprunt;
        this.utilisateur = utilisateur;
        this.livre = livre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public int getIdEmprunt() { return idEmprunt; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public Livre getLivre() { return livre; }
    public LocalDate getDateEmprunt() { return dateEmprunt; }
    public LocalDate getDateRetourPrevue() { return dateRetourPrevue; }

    public LocalDate getDateRetourEffective() { return dateRetourEffective; }
    public void setDateRetourEffective(LocalDate d) { this.dateRetourEffective = d; }

    public int getPenalite() { return penalite; }
    public void setPenalite(int p) { this.penalite = p; }
}
