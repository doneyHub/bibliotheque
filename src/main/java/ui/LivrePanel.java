package ui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.Livre;
import service.LivreService;

public class LivrePanel extends BorderPane {

    private final LivreService service = new LivreService();
    private final TableView<Livre> table = new TableView<>();

    private final TextField txtTitre = new TextField();
    private final TextField txtAuteur = new TextField();
    private final TextField txtIsbn = new TextField();
    private final TextField txtQt = new TextField();

    public LivrePanel() {
        setPadding(new Insets(20));

        // ================= FORMULAIRE =================
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        txtTitre.setPromptText("Titre");
        txtAuteur.setPromptText("Auteur");
        txtIsbn.setPromptText("ISBN");
        txtQt.setPromptText("Quantité totale");

        Button btnAjouter = new Button("➕ Ajouter");
        Button btnModifier = new Button("✏ Modifier");
        Button btnSupprimer = new Button("🗑 Supprimer");

        TextField recherche = new TextField();
        recherche.setPromptText("Rechercher (titre, auteur, ISBN)");

        // ================= ACTIONS =================

        // --- BOUTON AJOUTER ---
        btnAjouter.setOnAction(e -> {
            // Vérification stricte : tous les champs texte doivent être remplis
            if (txtTitre.getText().isBlank() || txtAuteur.getText().isBlank()
                    || txtIsbn.getText().isBlank() || txtQt.getText().isBlank()) {
                alerte("Tous les champs (Titre, Auteur, ISBN, Quantité) sont obligatoires.");
                return;
            }

            try {
                int qt = Integer.parseInt(txtQt.getText().trim());

                // ID = 0 : On laisse MySQL gérer l'auto-incrément (ex: 16, 17...)
                // Au départ, Quantité Dispo = Quantité Totale
                Livre l = new Livre(0, txtTitre.getText(), txtAuteur.getText(), txtIsbn.getText(), qt, qt);

                service.ajouterLivre(l);
                charger();     // Rafraîchir la table
                viderChamps(); // Nettoyer le formulaire

            } catch (NumberFormatException ex) {
                alerte("La quantité doit être un nombre entier valide.");
            }
        });

        // --- BOUTON MODIFIER ---
        btnModifier.setOnAction(e -> {
            Livre sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                alerte("Veuillez sélectionner un livre à modifier.");
                return;
            }

            if (txtTitre.getText().isBlank() || txtQt.getText().isBlank()) {
                alerte("Les champs ne peuvent pas être vides.");
                return;
            }

            try {
                int nouvelleQtTotale = Integer.parseInt(txtQt.getText().trim());

                // On garde l'ID existant et le stock disponible actuel
                // (Sauf si on voulait une logique complexe de recalcul du stock dispo, ici on reste simple)
                Livre l = new Livre(
                        sel.getIdLivre(),
                        txtTitre.getText(),
                        txtAuteur.getText(),
                        txtIsbn.getText(),
                        nouvelleQtTotale,
                        sel.getQuantiteDisponible()
                );

                service.modifier(l);
                charger();
                viderChamps();

            } catch (NumberFormatException ex) {
                alerte("Quantité invalide.");
            }
        });

        // --- BOUTON SUPPRIMER ---
        btnSupprimer.setOnAction(e -> {
            Livre sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                service.supprimer(sel.getIdLivre());
                charger();
                viderChamps();
            } else {
                alerte("Veuillez sélectionner un livre à supprimer.");
            }
        });

        // --- RECHERCHE ---
        recherche.textProperty().addListener((obs, o, n) ->
                table.setItems(FXCollections.observableArrayList(service.rechercher(n)))
        );

        // ================= TABLE (COLONNES TYPÉES) =================
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Livre, Integer> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getIdLivre()));
        cId.setPrefWidth(50);

        TableColumn<Livre, String> cTitre = new TableColumn<>("Titre");
        cTitre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitre()));

        TableColumn<Livre, String> cAuteur = new TableColumn<>("Auteur");
        cAuteur.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getAuteur()));

        TableColumn<Livre, String> cIsbn = new TableColumn<>("ISBN");
        cIsbn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getIsbn()));

        TableColumn<Livre, Integer> cDispo = new TableColumn<>("Dispo / Total");
        cDispo.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getQuantiteDisponible()));

        // Affichage personnalisé "3 / 5"
        cDispo.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    Livre l = getTableRow().getItem();
                    setText(l.getQuantiteDisponible() + " / " + l.getQuantiteTotale());

                    // Petit bonus visuel : rouge si stock 0
                    if (l.getQuantiteDisponible() == 0) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        table.getColumns().addAll(cId, cTitre, cAuteur, cIsbn, cDispo);

        // Remplir le formulaire au clic sur une ligne
        table.getSelectionModel().selectedItemProperty().addListener((obs, o, l) -> {
            if (l != null) {
                txtTitre.setText(l.getTitre());
                txtAuteur.setText(l.getAuteur());
                txtIsbn.setText(l.getIsbn());
                txtQt.setText(String.valueOf(l.getQuantiteTotale()));
            }
        });

        // Chargement initial
        charger();

        // Disposition du formulaire
        form.addRow(0, txtTitre, txtAuteur, txtIsbn, txtQt);
        form.addRow(1, btnAjouter, btnModifier, btnSupprimer);
        form.addRow(2, recherche);

        setTop(form);
        setCenter(table);
    }

    private void charger() {
        table.setItems(FXCollections.observableArrayList(service.getTousLesLivres()));
    }

    private void viderChamps() {
        txtTitre.clear();
        txtAuteur.clear();
        txtIsbn.clear();
        txtQt.clear();
    }

    private void alerte(String message) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}