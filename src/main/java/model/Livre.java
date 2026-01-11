package model;

public class Livre {
    private int idLivre;
    private String titre;
    private String auteur;
    private String isbn;
    private int quantiteTotale;
    private int quantiteDisponible;

    public Livre(int idLivre, String titre, String auteur, String isbn, int quantiteTotale, int quantiteDisponible) {
        this.idLivre = idLivre;
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.quantiteTotale = quantiteTotale;
        this.quantiteDisponible = quantiteDisponible;
    }
    public int getIdLivre() { return idLivre; }
    public String getTitre() { return titre; }
    public String getAuteur() { return auteur; }
    public String getIsbn() { return isbn; }
    public int getQuantiteTotale() { return quantiteTotale; }
    public int getQuantiteDisponible() { return quantiteDisponible; }
}