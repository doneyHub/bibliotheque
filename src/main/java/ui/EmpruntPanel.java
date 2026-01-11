package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import service.EmpruntService;
import service.ResultatEmprunt;

public class EmpruntPanel extends BorderPane {

    private final EmpruntService service = new EmpruntService();

    public EmpruntPanel() {
        setPadding(new Insets(30));

        // TITRE
        Label titre = new Label("📖 Nouvel emprunt");
        titre.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // CARTE FORMULAIRE
        VBox card = new VBox(15);
        card.setPadding(new Insets(25));
        card.setMaxWidth(420);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #dddddd;");

        TextField txtUser = new TextField();
        txtUser.setPromptText("ID Utilisateur");
        txtUser.setTooltip(new Tooltip("Identifiant visible dans la liste utilisateurs"));

        TextField txtLivre = new TextField();
        txtLivre.setPromptText("ID Livre");
        txtLivre.setTooltip(new Tooltip("Identifiant visible dans la liste livres"));

        Button btn = new Button("📚 Valider l’emprunt");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10;");

        Label badge = new Label();
        badge.setVisible(false);

        // ACTION
        btn.setOnAction(e -> {
            badge.setVisible(true);
            try {
                int idUtilisateur = Integer.parseInt(txtUser.getText().trim());
                int idLivre = Integer.parseInt(txtLivre.getText().trim());

                ResultatEmprunt resultat = service.emprunterLivre(idUtilisateur, idLivre);

                switch (resultat) {
                    case SUCCES:
                        styleBadge(badge, "✔ Emprunt validé", "#27ae60");
                        break;
                    case UTILISATEUR_INEXISTANT:
                        styleBadge(badge, "❌ Utilisateur inexistant", "#c0392b");
                        break;
                    case LIVRE_INEXISTANT:
                        styleBadge(badge, "❌ Livre inexistant", "#c0392b");
                        break;
                    case LIVRE_INDISPONIBLE:
                        styleBadge(badge, "❌ Livre indisponible (Stock 0)", "#e74c3c");
                        break;
                    case LIMITE_ETUDIANT_ATTEINTE:
                        styleBadge(badge, "❌ Limite étudiant atteinte (3)", "#f39c12");
                        break;
                    case LIMITE_ENSEIGNANT_ATTEINTE:
                        styleBadge(badge, "❌ Limite enseignant atteinte (5)", "#f39c12");
                        break;
                    case PENALITE_NON_REGLEE:
                        styleBadge(badge, "⚠ Pénalité non réglée", "#e67e22");
                        break;
                    default:
                        styleBadge(badge, "❌ Erreur inconnue", "#c0392b");
                }
            } catch (NumberFormatException ex) {
                styleBadge(badge, "❌ ID invalide (entrez un nombre)", "#c0392b");
            }
        });

        card.getChildren().addAll(txtUser, txtLivre, btn, badge);

        VBox container = new VBox(20, titre, card);
        container.setAlignment(Pos.TOP_CENTER);
        setCenter(container);
    }

    private void styleBadge(Label label, String texte, String couleur) {
        label.setText(texte);
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-background-radius: 15; -fx-font-weight: bold; -fx-padding: 6 12 6 12; -fx-background-color: " + couleur + ";");
    }
}