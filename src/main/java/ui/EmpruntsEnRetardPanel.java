package ui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import service.EmpruntService;
import model.Emprunt;

public class EmpruntsEnRetardPanel extends BorderPane {

    public EmpruntsEnRetardPanel() {
        setPadding(new Insets(20));

        Label titre = new Label("⚠ Liste des retards");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #c0392b;");

        TableView<Emprunt> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Emprunt, Integer> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getIdEmprunt()));

        TableColumn<Emprunt, String> cLivre = new TableColumn<>("Livre");
        cLivre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getLivre().getTitre()));

        TableColumn<Emprunt, String> cUser = new TableColumn<>("Utilisateur");
        cUser.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getUtilisateur().getNom()));

        TableColumn<Emprunt, String> cDate = new TableColumn<>("Retour Prévu");
        cDate.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDateRetourPrevue().toString()));
        cDate.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        table.getColumns().addAll(cId, cLivre, cUser, cDate);

        table.setItems(FXCollections.observableArrayList(new EmpruntService().getEmpruntsEnRetard()));

        setTop(new VBox(10, titre));
        setCenter(table);
    }
}