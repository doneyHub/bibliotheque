import javafx.collections.FXCollections;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.BorderPane;
import service.EmpruntService;

public class StatistiquesPieChartPanel extends BorderPane {

    public StatistiquesPieChartPanel() {

        EmpruntService service = new EmpruntService();

        int retard = service.getEmpruntsEnRetard().size();
        int total = service.getHistorique().size();

        PieChart chart = new PieChart(FXCollections.observableArrayList(
                new PieChart.Data("En retard", retard),
                new PieChart.Data("À jour", total - retard)
        ));

        chart.setTitle("Répartition des emprunts");

        setCenter(chart);
    }
}
