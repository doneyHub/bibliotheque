package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import service.EmpruntService;

public class RetourEmpruntPanel extends BorderPane {

    private final EmpruntService service = new EmpruntService();

    public RetourEmpruntPanel() {
        setPadding(new Insets(30));

        Label titre = new Label("🔁 Retour d’un emprunt");
        titre.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        VBox card = new VBox(15);
        card.setPadding(new Insets(25));
        card.setMaxWidth(420);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #dddddd; -fx-border-radius: 12;");

        TextField txtId = new TextField();
        txtId.setPromptText("ID Emprunt");

        Button btn = new Button("✔ Valider le retour");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10;");

        Label badge = new Label();
        badge.setVisible(false);
        badge.setPadding(new Insets(6, 12, 6, 12));
        badge.setStyle("-fx-background-radius: 15;");

        btn.setOnAction(e -> {
            badge.setVisible(true);

            if (txtId.getText().isBlank()) {
                styleBadge(badge, "❌ ID emprunt obligatoire", "#c0392b");
                return;
            }

            try {
                int id = Integer.parseInt(txtId.getText().trim());
                // Retourne : -1 (Erreur), 0 (OK), >0 (Pénalité en jours)
                int p = service.retournerLivre(id);

                if (p == -1) {
                    styleBadge(badge, "❌ Emprunt introuvable ou déjà clos", "#e74c3c");
                } else if (p == 0) {
                    styleBadge(badge, "✔ Retour effectué (Pas de pénalité)", "#27ae60");
                } else {
                    int montant = p * 100; // 100 FCFA par jour
                    styleBadge(badge, "⚠ Pénalité : " + p + " j → " + montant + " FCFA", "#f39c12");
                }
            } catch (NumberFormatException ex) {
                styleBadge(badge, "❌ ID invalide", "#c0392b");
            }
        });

        card.getChildren().addAll(txtId, btn, badge);

        VBox container = new VBox(20, titre, card);
        container.setAlignment(Pos.TOP_CENTER);
        setCenter(container);
    }

    private void styleBadge(Label l, String txt, String color) {
        l.setText(txt);
        l.setTextFill(Color.WHITE);
        l.setStyle("-fx-background-radius: 15; -fx-font-weight: bold; -fx-padding: 6 12 6 12; -fx-background-color: " + color + ";");
    }
}