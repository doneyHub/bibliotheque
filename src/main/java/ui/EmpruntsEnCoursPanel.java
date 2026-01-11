package ui;

import dao.EmpruntDAO;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import model.Emprunt;

public class EmpruntsEnCoursPanel extends BorderPane {

    public EmpruntsEnCoursPanel() {

        setPadding(new Insets(20));

        TableView<Emprunt> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Emprunt, Integer> cId =
                new TableColumn<>("ID");
        cId.setCellValueFactory(d ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        d.getValue().getIdEmprunt()));

        TableColumn<Emprunt, String> cDate =
                new TableColumn<>("Date emprunt");
        cDate.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        d.getValue().getDateEmprunt().toString()));

        TableColumn<Emprunt, String> cRetour =
                new TableColumn<>("Retour prévu");
        cRetour.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        d.getValue().getDateRetourPrevue().toString()));

        table.getColumns().addAll(cId, cDate, cRetour);

        table.setItems(FXCollections.observableArrayList(
                new EmpruntDAO().getEmpruntsEnCours()
        ));

        setCenter(table);
    }
}
