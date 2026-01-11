package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainFrame extends Application {

    @Override
    public void start(Stage stage) {
        // 1. Ecran de connexion
        LoginPanel loginPanel = new LoginPanel(() -> showMainUI(stage));
        Scene scene = new Scene(loginPanel, 1280, 800);

        // 2. CSS Global (Style High Level)
        String css = """
            .root { -fx-font-family: 'Segoe UI', sans-serif; -fx-background-color: #f0f2f5; }
            
            /* ONGLETS */
            .tab-pane .tab-header-area .tab-header-background { -fx-background-color: transparent; }
            .tab { -fx-background-color: white; -fx-background-radius: 5 5 0 0; -fx-padding: 8 15; }
            .tab:selected { -fx-background-color: #2c3e50; -fx-text-fill: white; }
            .tab-label { -fx-text-fill: #7f8c8d; -fx-font-weight: bold; }
            .tab:selected .tab-label { -fx-text-fill: white; }
            
            /* TABLEAUX */
            .table-view { -fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 0); }
            .table-view .column-header-background { -fx-background-color: #ecf0f1; }
            .table-view .column-header .label { -fx-text-fill: #2c3e50; -fx-font-weight: bold; }
            
            /* BOUTONS */
            .button { -fx-cursor: hand; -fx-font-weight: bold; }
        """;
        scene.getStylesheets().add("data:text/css," + css.replaceAll("\n", ""));

        stage.setTitle("Système de Gestion de Bibliothèque Universitaire (Complet)");
        stage.setScene(scene);
        stage.show();
    }

    private void showMainUI(Stage stage) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // ==========================================
        // CRÉATION DES ONGLETS (Respect strict PDF)
        // ==========================================

        // 1. DASHBOARD
        Tab tabAccueil = new Tab("📊 Tableau de bord", new AccueilPanel(tabPane));

        // 2. MODULES OBLIGATOIRES
        Tab tabLivres = new Tab("📘 Livres", new LivrePanel());
        Tab tabUsers = new Tab("👤 Utilisateurs", new UtilisateurPanel());
        Tab tabEmprunt = new Tab("➕ Emprunter", new EmpruntPanel());
        Tab tabRetour = new Tab("↩ Retourner", new RetourEmpruntPanel());

        // 3. SUIVI & GESTION
        Tab tabEnCours = new Tab("⏳ En cours", new EmpruntsEnCoursPanel());
        Tab tabHist = new Tab("📜 Historique", new HistoriqueEmpruntPanel());

        // 4. MODULES 2.2 & 2.3 (BONUS & SPÉCIFIQUES)
        Tab tabRetards = new Tab("⚠ Retards", new EmpruntsEnRetardPanel());
        Tab tabPenalites = new Tab("💳 Pénalités", new PayerPenalitePanel());
        Tab tabReserv = new Tab("📅 Réservations", new ReservationPanel()); // Bonus
        Tab tabNotifs = new Tab("🔔 Notifications", new NotificationsPanel()); // Bonus
        Tab tabStats = new Tab("📈 Statistiques", new StatistiquesBarChartPanel());

        // Ajout dans l'ordre logique
        tabPane.getTabs().addAll(
                tabAccueil,     // 0
                tabLivres,      // 1
                tabUsers,       // 2
                tabEmprunt,     // 3
                tabRetour,
                tabEnCours,
                tabRetards,     // Spécifique PDF
                tabPenalites,   // Spécifique PDF
                tabReserv,      // Bonus
                tabNotifs,      // Bonus
                tabHist,
                tabStats
        );

        stage.getScene().setRoot(tabPane);
    }

    public static void main(String[] args) {
        launch(args);
    }
}