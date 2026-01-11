package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import service.NotificationService;

public class NotificationsPanel extends BorderPane {

    public NotificationsPanel() {
        setPadding(new Insets(20));

        Label titre = new Label("🔔 Notifications de Retard");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #e67e22;");

        ListView<String> list = new ListView<>();
        list.getItems().addAll(new NotificationService().getNotifications());

        // Style simple si la liste est vide
        if (list.getItems().isEmpty()) {
            list.getItems().add("Aucune notification : Tous les emprunts sont à l'heure !");
        }

        setTop(new VBox(10, titre));
        setCenter(list);
    }
}