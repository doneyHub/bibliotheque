package ui;
import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;
import model.TopUtilisateur;
import service.EmpruntService;

public class StatistiquesBarChartPanel extends BorderPane {
    public StatistiquesBarChartPanel() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Utilisateurs les plus actifs");
        xAxis.setLabel("Utilisateur");
        yAxis.setLabel("Nombre d’emprunts");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        EmpruntService service = new EmpruntService();

        for (TopUtilisateur u : service.getUtilisateursLesPlusActifs()) {
            series.getData().add(new XYChart.Data<>(u.getNom(), u.getNombreEmprunts()));
        }
        chart.getData().add(series);
        setCenter(chart);
    }
}