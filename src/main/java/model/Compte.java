package model;

public class Compte {
    private int id;
    private String login;
    private String role;

    public Compte(int id, String login, String role) {
        this.id = id;
        this.login = login;
        this.role = role;
    }
    public String getLogin() { return login; }
}