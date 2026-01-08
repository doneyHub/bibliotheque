package ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import service.EmpruntService;

import java.sql.SQLException;

public class EmpruntPanel extends GridPane {

    private final EmpruntService empruntService = new EmpruntService();

    public EmpruntPanel() {

        setPadding(new Insets(30));
        setHgap(20);
        setVgap(20);
        setStyle("-fx-background-color: #ffffff;");

        Label titre = new Label("📖 Gestion des emprunts");
        titre.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label lblUtilisateur = new Label("ID Utilisateur :");
        TextField txtUtilisateur = new TextField();
        txtUtilisateur.setPromptText("ex: 1");

        Label lblLivre = new Label("ID Livre :");
        TextField txtLivre = new TextField();
        txtLivre.setPromptText("ex: 10");

        Button btnEmprunter = new Button("📚 Emprunter");
        btnEmprunter.setStyle("""
            -fx-background-color: #2ecc71;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 8 20;
        """);

        Label message = new Label();

        btnEmprunter.setOnAction(e -> {

            message.setText("");

            if (txtUtilisateur.getText().isEmpty() || txtLivre.getText().isEmpty()) {
                message.setText("❌ Tous les champs sont obligatoires");
                message.setStyle("-fx-text-fill: red;");
                return;
            }

            try {
                int idUtilisateur = Integer.parseInt(txtUtilisateur.getText());
                int idLivre = Integer.parseInt(txtLivre.getText());

                boolean ok = empruntService.emprunterLivre(idUtilisateur, idLivre);

                if (ok) {
                    message.setText("✔ Emprunt effectué avec succès");
                    message.setStyle("-fx-text-fill: green;");
                } else {
                    message.setText("❌ Emprunt refusé (limite atteinte ou livre indisponible)");
                    message.setStyle("-fx-text-fill: red;");
                }

            } catch (NumberFormatException ex) {
                message.setText("❌ Les IDs doivent être des nombres");
                message.setStyle("-fx-text-fill: red;");

            } catch (SQLException ex) {
                message.setText("❌ Erreur base de données");
                message.setStyle("-fx-text-fill: red;");
                ex.printStackTrace();
            }
        });

        add(titre, 0, 0, 2, 1);
        add(lblUtilisateur, 0, 1);
        add(txtUtilisateur, 1, 1);
        add(lblLivre, 0, 2);
        add(txtLivre, 1, 2);
        add(btnEmprunter, 1, 3);
        add(message, 1, 4);
    }
}
