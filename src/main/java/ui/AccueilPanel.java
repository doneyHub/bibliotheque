package ui;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import service.StatistiqueService;

public class AccueilPanel extends BorderPane {

    private final StatistiqueService stats = new StatistiqueService();

    public AccueilPanel(TabPane tabPane) {
        initInterface(tabPane);
    }

    private void initInterface(TabPane tabPane) {
        setPadding(new Insets(20));
        // Fond légèrement grisé pour faire ressortir les éléments
        setStyle("-fx-background-color: #f4f6f8;");

        // =====================
        // 1. KPI CARDS (Ton ancien modèle que tu aimes)
        // =====================
        VBox cardLivres = carteKPI("📚 Livres", stats.totalLivres(), "#3498db");
        VBox cardUtilisateurs = carteKPI("👤 Utilisateurs", stats.totalUtilisateurs(), "#8e44ad");
        VBox cardEmprunts = carteKPI("🔁 En cours", stats.empruntsEnCours(), "#27ae60");
        VBox cardRetards = carteKPI("⚠ Retards", stats.retards(), "#e74c3c");

        HBox kpis = new HBox(20, cardLivres, cardUtilisateurs, cardEmprunts, cardRetards);
        kpis.setAlignment(Pos.CENTER);

        // =====================
        // 2. NOUVEAUX BOUTONS DE NAVIGATION (Centrés et Modernes)
        // =====================
        HBox navigation = new HBox(30);
        navigation.setAlignment(Pos.CENTER);
        navigation.setPadding(new Insets(30, 0, 30, 0)); // Espace en haut et en bas

        if (tabPane != null) {
            // On cible les onglets : 1=Livres, 2=Utilisateurs, 3=Emprunts
            VBox btnLivres = creerBoutonAction("📘 Gérer les Livres", "Stock & Ajout", "#2980b9", () -> tabPane.getSelectionModel().select(1));
            VBox btnUsers  = creerBoutonAction("👤 Gérer Utilisateurs", "Inscriptions", "#8e44ad", () -> tabPane.getSelectionModel().select(2));
            VBox btnEmprunt= creerBoutonAction("📖 Nouvel Emprunt", "Prêter un livre", "#27ae60", () -> tabPane.getSelectionModel().select(3));

            navigation.getChildren().addAll(btnLivres, btnUsers, btnEmprunt);
        }

        // =====================
        // 3. GRAPHIQUES (Ton ancien modèle)
        // =====================
        PieChart pie = new PieChart();
        pie.getData().addAll(
                new PieChart.Data("Retards", stats.retards()),
                new PieChart.Data("Clôturés", stats.empruntsClotures()),
                new PieChart.Data("En cours", stats.empruntsEnCours())
        );
        pie.setTitle("Répartition des emprunts");
        pie.setLabelsVisible(true);

        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();
        BarChart<String, Number> bar = new BarChart<>(x, y);
        bar.setTitle("État des emprunts");
        bar.setLegendVisible(false);

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.getData().add(new XYChart.Data<>("En cours", stats.empruntsEnCours()));
        serie.getData().add(new XYChart.Data<>("Retards", stats.retards()));
        serie.getData().add(new XYChart.Data<>("Clôturés", stats.empruntsClotures()));
        bar.getData().add(serie);

        HBox charts = new HBox(20, pie, bar);
        charts.setAlignment(Pos.CENTER);
        HBox.setHgrow(pie, Priority.ALWAYS);
        HBox.setHgrow(bar, Priority.ALWAYS);

        // =====================
        // ASSEMBLAGE
        // =====================
        VBox topContainer = new VBox(10, kpis, navigation);
        topContainer.setAlignment(Pos.CENTER);

        setTop(topContainer);
        setCenter(charts);
    }

    // =====================
    // ANCIEN STYLE KPI (Celui que tu aimes)
    // =====================
    private VBox carteKPI(String titre, int valeur, String couleurHex) {
        Label t = new Label(titre);
        t.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label v = new Label(String.valueOf(valeur));
        v.setStyle("-fx-font-size: 28px; -fx-text-fill: white; -fx-font-weight: bold;");

        VBox box = new VBox(10, t, v);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(15));
        box.setPrefWidth(200);

        // Style simple et propre
        box.setStyle("-fx-background-color: " + couleurHex + "; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");

        return box;
    }

    // =====================
    // NOUVEAU STYLE BOUTONS (Ceux que tu veux garder)
    // =====================
    private VBox creerBoutonAction(String titre, String sousTitre, String couleurBordure, Runnable action) {
        VBox btn = new VBox(5);
        btn.setPadding(new Insets(15, 30, 15, 30));
        btn.setAlignment(Pos.CENTER);
        btn.setCursor(Cursor.HAND);
        btn.setMinWidth(220);

        // Style Bouton "Flat" avec bordure colorée gauche
        btn.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: " + couleurBordure + "; " +
                "-fx-border-width: 0 0 0 6; " + // Bordure gauche épaisse
                "-fx-border-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label lblTitre = new Label(titre);
        lblTitre.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblTitre.setTextFill(Color.web("#2c3e50"));

        Label lblSous = new Label(sousTitre);
        lblSous.setFont(Font.font("Segoe UI", 12));
        lblSous.setTextFill(Color.GRAY);

        btn.getChildren().addAll(lblTitre, lblSous);

        btn.setOnMouseClicked(e -> action.run());

        // Animation au survol
        ScaleTransition st = new ScaleTransition(Duration.millis(200), btn);
        btn.setOnMouseEntered(e -> {
            st.setToX(1.05); st.setToY(1.05); st.playFromStart();
            btn.setStyle(btn.getStyle() + "-fx-background-color: #fcfcfc;");
        });
        btn.setOnMouseExited(e -> {
            st.setToX(1.0); st.setToY(1.0); st.playFromStart();
            btn.setStyle(btn.getStyle().replace("-fx-background-color: #fcfcfc;", ""));
        });

        return btn;
    }
}