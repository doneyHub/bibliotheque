package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainFrame extends Application {

    @Override
    public void start(Stage stage) {

        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-font-size: 14px;");

        Tab tabAccueil = new Tab("Accueil", new AccueilPanel());
        Tab tabLivres = new Tab("Livres", new LivrePanel());
        Tab tabUtilisateurs = new Tab("Utilisateurs", new UtilisateurPanel());
        Tab tabEmprunts = new Tab("Emprunts", new EmpruntPanel());

        tabAccueil.setClosable(false);
        tabLivres.setClosable(false);
        tabUtilisateurs.setClosable(false);
        tabEmprunts.setClosable(false);

        tabPane.getTabs().addAll(
                tabAccueil,
                tabLivres,
                tabUtilisateurs,
                tabEmprunts
        );

        Scene scene = new Scene(tabPane, 900, 600);
        stage.setTitle("📚 Gestion de Bibliothèque");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
