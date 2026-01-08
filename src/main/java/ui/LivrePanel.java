package ui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.Livre;
import service.LivreService;

public class LivrePanel extends BorderPane {

    private final LivreService livreService = new LivreService();
    private final TableView<Livre> table = new TableView<>();

    public LivrePanel() {

        setPadding(new Insets(20));

        // ===== FORMULAIRE =====
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        TextField titre = new TextField();
        titre.setPromptText("Titre");

        TextField auteur = new TextField();
        auteur.setPromptText("Auteur");

        TextField isbn = new TextField();
        isbn.setPromptText("ISBN");

        TextField quantite = new TextField();
        quantite.setPromptText("Quantité");

        Button ajouter = new Button("➕ Ajouter");
        ajouter.getStyleClass().add("button-success");

        Label msg = new Label();

        ajouter.setOnAction(e -> {
            try {
                Livre livre = new Livre(
                        0,
                        titre.getText(),
                        auteur.getText(),
                        isbn.getText(),
                        Integer.parseInt(quantite.getText()),
                        Integer.parseInt(quantite.getText())
                );
                livreService.ajouterLivre(livre);
                chargerLivres();
                msg.setText("✔ Livre ajouté");
            } catch (Exception ex) {
                msg.setText("❌ Erreur de saisie");
            }
        });

        form.addRow(0, titre, auteur, isbn, quantite, ajouter);
        form.add(msg, 0, 1, 5, 1);

        // ===== TABLE =====
        TableColumn<Livre, String> cTitre = new TableColumn<>("Titre");
        cTitre.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getTitre()));

        TableColumn<Livre, String> cAuteur = new TableColumn<>("Auteur");
        cAuteur.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getAuteur()));

        TableColumn<Livre, Integer> cDispo = new TableColumn<>("Disponible");
        cDispo.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getQuantiteDisponible()));

        table.getColumns().addAll(cTitre, cAuteur, cDispo);
        chargerLivres();

        setTop(form);
        setCenter(table);
    }

    private void chargerLivres() {
        table.setItems(FXCollections.observableArrayList(
                livreService.getTousLesLivres()
        ));
    }
}
