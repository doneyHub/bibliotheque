package model;

public class TopUtilisateur {
    private String nom;
    private String prenom;
    private int nombreEmprunts;

    public TopUtilisateur(String nom, String prenom, int nombreEmprunts) {
        this.nom = nom;
        this.prenom = prenom;
        this.nombreEmprunts = nombreEmprunts;
    }

    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public int getNombreEmprunts() { return nombreEmprunts; }

    @Override
    public String toString() { return nom + " " + prenom; }
}