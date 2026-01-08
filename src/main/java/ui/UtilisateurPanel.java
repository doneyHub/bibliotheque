package ui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.utilisateur.*;
import service.UtilisateurService;

public class UtilisateurPanel extends BorderPane {

    private final UtilisateurService service = new UtilisateurService();
    private final TableView<Utilisateur> table = new TableView<>();

    public UtilisateurPanel() {

        setPadding(new Insets(20));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        TextField nom = new TextField();
        nom.setPromptText("Nom");

        TextField prenom = new TextField();
        prenom.setPromptText("Prénom");

        TextField matricule = new TextField();
        matricule.setPromptText("Matricule");

        ChoiceBox<String> type = new ChoiceBox<>();
        type.getItems().addAll("ETUDIANT", "ENSEIGNANT");
        type.setValue("ETUDIANT");

        Button ajouter = new Button("➕ Ajouter");
        Label msg = new Label();

        ajouter.setOnAction(e -> {
            try {
                Utilisateur u = type.getValue().equals("ETUDIANT")
                        ? new Etudiant(0, nom.getText(), prenom.getText(), matricule.getText())
                        : new Enseignant(0, nom.getText(), prenom.getText(), matricule.getText());

                service.ajouterUtilisateur(u);
                charger();
                msg.setText("✔ Utilisateur ajouté");
            } catch (Exception ex) {
                msg.setText("❌ Erreur");
            }
        });

        form.addRow(0, nom, prenom, matricule, type, ajouter);
        form.add(msg, 0, 1, 5, 1);

        TableColumn<Utilisateur, String> cNom = new TableColumn<>("Nom");
        cNom.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNom()));

        TableColumn<Utilisateur, String> cType = new TableColumn<>("Type");
        cType.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        d.getValue() instanceof Etudiant ? "ETUDIANT" : "ENSEIGNANT"
                ));

        table.getColumns().addAll(cNom, cType);
        charger();

        setTop(form);
        setCenter(table);
    }

    private void charger() {
        table.setItems(FXCollections.observableArrayList(
                service.getTousLesUtilisateurs()
        ));
    }
}
