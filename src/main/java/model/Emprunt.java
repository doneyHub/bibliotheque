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
    private boolean penalitePayee;

    public Emprunt(int idEmprunt, Utilisateur utilisateur, Livre livre,
                   LocalDate dateEmprunt, LocalDate dateRetourPrevue,
                   LocalDate dateRetourEffective, int penalite, boolean penalitePayee) {
        this.idEmprunt = idEmprunt;
        this.utilisateur = utilisateur;
        this.livre = livre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = dateRetourEffective;
        this.penalite = penalite;
        this.penalitePayee = penalitePayee;
    }

    // Constructeur simplifié pour création
    public Emprunt(int idEmprunt, Utilisateur utilisateur, Livre livre, LocalDate dateEmprunt, LocalDate dateRetourPrevue) {
        this(idEmprunt, utilisateur, livre, dateEmprunt, dateRetourPrevue, null, 0, false);
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
    public boolean isPenalitePayee() { return penalitePayee; }
    public void setPenalitePayee(boolean p) { this.penalitePayee = p; }
}