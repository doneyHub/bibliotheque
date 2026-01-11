package ui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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

    private final TextField txtNom = new TextField();
    private final TextField txtPrenom = new TextField();
    private final TextField txtMatricule = new TextField();
    private final ChoiceBox<String> type = new ChoiceBox<>();

    public UtilisateurPanel() {
        setPadding(new Insets(20));

        // Formulaire
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        txtNom.setPromptText("Nom");
        txtPrenom.setPromptText("Prénom");
        txtMatricule.setPromptText("Matricule");

        type.getItems().addAll("ETUDIANT", "ENSEIGNANT");
        type.setValue("ETUDIANT");

        Button btnAjouter = new Button("➕ Ajouter");
        Button btnModifier = new Button("✏ Modifier");
        Button btnSupprimer = new Button("🗑 Supprimer");

        TextField recherche = new TextField();
        recherche.setPromptText("Rechercher (nom ou matricule)");

        // ================= ACTIONS =================
        btnAjouter.setOnAction(e -> {
            // Vérification stricte
            if (txtNom.getText().isBlank() || txtPrenom.getText().isBlank() || txtMatricule.getText().isBlank()) {
                alerte("Veuillez remplir le Nom, le Prénom et le Matricule.");
                return;
            }

            // ID = 0 : C'est le signal pour dire à la BDD "Génère l'ID toi-même"
            service.ajouterUtilisateur(creerUtilisateur(0));
            charger();
            viderChamps();
        });

        btnModifier.setOnAction(e -> {
            Utilisateur sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                alerte("Veuillez sélectionner un utilisateur dans la liste.");
                return;
            }
            if (txtNom.getText().isBlank() || txtMatricule.getText().isBlank()) {
                alerte("Les champs ne peuvent pas être vides.");
                return;
            }
            // Ici on garde l'ID existant
            service.modifier(creerUtilisateur(sel.getIdUtilisateur()));
            charger();
            viderChamps();
        });

        btnSupprimer.setOnAction(e -> {
            Utilisateur sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                service.supprimer(sel.getIdUtilisateur());
                charger();
            }
        });

        recherche.textProperty().addListener((obs, o, n) ->
                table.setItems(FXCollections.observableArrayList(service.rechercher(n)))
        );

        // ================= TABLE =================
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Utilisateur, Integer> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getIdUtilisateur()));

        TableColumn<Utilisateur, String> cNom = new TableColumn<>("Nom");
        cNom.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNom()));

        TableColumn<Utilisateur, String> cPrenom = new TableColumn<>("Prénom");
        cPrenom.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPrenom()));

        TableColumn<Utilisateur, String> cMatricule = new TableColumn<>("Matricule");
        cMatricule.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getMatricule()));

        TableColumn<Utilisateur, String> cType = new TableColumn<>("Type");
        cType.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue() instanceof Etudiant ? "ETUDIANT" : "ENSEIGNANT"
        ));

        table.getColumns().addAll(cId, cNom, cPrenom, cMatricule, cType);

        // Remplissage auto du formulaire au clic
        table.getSelectionModel().selectedItemProperty().addListener((obs, o, u) -> {
            if (u != null) {
                txtNom.setText(u.getNom());
                txtPrenom.setText(u.getPrenom());
                txtMatricule.setText(u.getMatricule());
                type.setValue(u instanceof Etudiant ? "ETUDIANT" : "ENSEIGNANT");
            }
        });

        charger();

        form.addRow(0, txtNom, txtPrenom, txtMatricule, type);
        form.addRow(1, btnAjouter, btnModifier, btnSupprimer);
        form.addRow(2, recherche);

        setTop(form);
        setCenter(table);
    }

    private Utilisateur creerUtilisateur(int id) {
        if (type.getValue().equals("ETUDIANT")) {
            return new Etudiant(id, txtNom.getText(), txtPrenom.getText(), txtMatricule.getText());
        } else {
            return new Enseignant(id, txtNom.getText(), txtPrenom.getText(), txtMatricule.getText());
        }
    }

    private void charger() {
        table.setItems(FXCollections.observableArrayList(service.getTousLesUtilisateurs()));
    }

    private void viderChamps() {
        txtNom.clear(); txtPrenom.clear(); txtMatricule.clear();
    }

    private void alerte(String message) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}