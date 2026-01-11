package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import service.ReservationService;

public class ReservationPanel extends BorderPane {

    private final ReservationService service = new ReservationService();

    public ReservationPanel() {
        setPadding(new Insets(30));

        Label titre = new Label("📅 Réservation de Livre");
        titre.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        VBox card = new VBox(15);
        card.setPadding(new Insets(30));
        card.setMaxWidth(400);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        TextField txtUser = new TextField();
        txtUser.setPromptText("ID Utilisateur");

        TextField txtLivre = new TextField();
        txtLivre.setPromptText("ID Livre");

        Button btn = new Button("Réserver");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");

        Label info = new Label("NB: Réservation possible uniquement si stock = 0.");
        info.setStyle("-fx-font-style: italic; -fx-text-fill: gray; -fx-font-size: 11px;");

        Label badge = new Label();
        badge.setVisible(false);

        btn.setOnAction(e -> {
            badge.setVisible(true);
            try {
                int idU = Integer.parseInt(txtUser.getText());
                int idL = Integer.parseInt(txtLivre.getText());

                if (service.reserverLivre(idU, idL)) {
                    styleBadge(badge, "✔ Réservation enregistrée", "#27ae60");
                } else {
                    styleBadge(badge, "❌ Impossible : Le livre est encore disponible ou introuvable", "#e74c3c");
                }
            } catch (Exception ex) {
                styleBadge(badge, "❌ ID invalide", "#c0392b");
            }
        });

        card.getChildren().addAll(new Label("Saisir les IDs :"), txtUser, txtLivre, btn, info, badge);

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