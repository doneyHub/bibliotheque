package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AccueilPanel extends VBox {

    public AccueilPanel() {

        setSpacing(25);
        setAlignment(Pos.CENTER);

        Label titre = new Label("📚 Système de Gestion de Bibliothèque");
        titre.getStyleClass().add("label-title");

        Label description = new Label(
                "Projet Java – Architecture MVC / DAO\n\n" +
                        "• Gestion des livres\n" +
                        "• Gestion des utilisateurs\n" +
                        "• Gestion des emprunts"
        );
        description.getStyleClass().add("label-text");

        getChildren().addAll(titre, description);
    }
}
