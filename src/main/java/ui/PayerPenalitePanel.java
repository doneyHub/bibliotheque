package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import service.EmpruntService;

public class PayerPenalitePanel extends BorderPane {

    private final EmpruntService service = new EmpruntService();

    public PayerPenalitePanel() {
        setPadding(new Insets(30));

        Label titre = new Label("💳 Régularisation des Pénalités");
        titre.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox card = new VBox(15);
        card.setPadding(new Insets(30));
        card.setMaxWidth(400);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        TextField txtId = new TextField();
        txtId.setPromptText("ID Emprunt (ex: 12)");
        txtId.setStyle("-fx-font-size: 14px;");

        Button btn = new Button("✔ Confirmer le paiement");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-font-size: 14px;");

        Label badge = new Label();
        badge.setVisible(false);

        btn.setOnAction(e -> {
            badge.setVisible(true);
            try {
                int id = Integer.parseInt(txtId.getText());
                boolean ok = service.payerPenalite(id);

                if (ok) {
                    styleBadge(badge, "✔ Pénalité réglée avec succès", "#27ae60");
                } else {
                    styleBadge(badge, "❌ Aucune pénalité impayée pour cet ID", "#e74c3c");
                }
            } catch (Exception ex) {
                styleBadge(badge, "❌ ID invalide", "#c0392b");
            }
        });

        card.getChildren().addAll(new Label("Entrez l'ID de l'emprunt concerné :"), txtId, btn, badge);

        VBox container = new VBox(25, titre, card);
        container.setAlignment(Pos.TOP_CENTER);

        setCenter(container);
    }

    private void styleBadge(Label l, String txt, String color) {
        l.setText(txt);
        l.setTextFill(Color.WHITE);
        l.setStyle("-fx-background-radius: 5; -fx-padding: 8; -fx-background-color: " + color + ";");
    }
}