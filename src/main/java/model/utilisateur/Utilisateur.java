package model.utilisateur;

public abstract class Utilisateur {

    protected int idUtilisateur;
    protected String nom;
    protected String prenom;
    protected String matricule;

    public Utilisateur(int idUtilisateur, String nom, String prenom, String matricule) {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getMatricule() {
        return matricule;
    }

    public abstract int getNombreMaxEmprunts();
}
