package ui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import dao.EmpruntDAO;
import model.Emprunt;
import service.ExportService;
import service.ExportPDFService;
import service.ExportTXTService;
import service.EmpruntService;

import java.io.File;

public class HistoriqueEmpruntPanel extends BorderPane {

    public HistoriqueEmpruntPanel() {

        /* =========================
           TABLE
        ========================= */
        TableView<Emprunt> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Emprunt, Integer> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getIdEmprunt()));

        TableColumn<Emprunt, String> cDate = new TableColumn<>("Date emprunt");
        cDate.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDateEmprunt().toString()));

        TableColumn<Emprunt, Integer> cPenalite = new TableColumn<>("Pénalité");
        cPenalite.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getPenalite()));

        TableColumn<Emprunt, String> cStatut = new TableColumn<>("Statut");
        cStatut.setCellValueFactory(d -> {
            Emprunt e = d.getValue();
            if (e.getDateRetourEffective() == null) return new SimpleStringProperty("EN COURS");
            if (e.getPenalite() > 0) return new SimpleStringProperty("RETARD (" + e.getPenalite() + "j)");
            return new SimpleStringProperty("CLOTURÉ");
        });

        /* =========================
           CELL FACTORY STATUT
        ========================= */
        cStatut.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }
                Circle badge = new Circle(6);
                String tooltip;
                if (item.startsWith("RETARD")) {
                    badge.setFill(Color.RED);
                    tooltip = "Retour en retard – pénalité appliquée";
                } else if (item.equals("EN COURS")) {
                    badge.setFill(Color.ORANGE);
                    tooltip = "Emprunt non retourné";
                } else {
                    badge.setFill(Color.GREEN);
                    tooltip = "Emprunt clôturé sans retard";
                }
                Label label = new Label(item);
                label.setTooltip(new Tooltip(tooltip));
                HBox box = new HBox(6, badge, label);
                box.setAlignment(Pos.CENTER_LEFT);
                setGraphic(box);
            }
        });
        cStatut.setComparator((a, b) -> ordreStatut(a) - ordreStatut(b));
        table.getColumns().addAll(cId, cDate, cPenalite, cStatut);

        /* =========================
           DONNÉES + FILTRE
        ========================= */
        FilteredList<Emprunt> filteredData = new FilteredList<>(
                FXCollections.observableArrayList(new EmpruntService().getHistorique()),
                p -> true
        );
        table.setItems(filteredData);

        ComboBox<String> filterBox = new ComboBox<>();
        filterBox.getItems().addAll("TOUS", "EN COURS", "RETARD", "CLOTURÉ");
        filterBox.setValue("TOUS");

        filterBox.setOnAction(e -> filteredData.setPredicate(emprunt -> {
            String statut;
            if (emprunt.getDateRetourEffective() == null) statut = "EN COURS";
            else if (emprunt.getPenalite() > 0) statut = "RETARD";
            else statut = "CLOTURÉ";
            return filterBox.getValue().equals("TOUS") || statut.equals(filterBox.getValue());
        }));

        table.getSortOrder().add(cStatut);
        table.getSortOrder().addListener((ListChangeListener<TableColumn<Emprunt, ?>>) c -> table.refresh());

        /* =========================
           EXPORT
        ========================= */
        Button btnCSV = new Button("⬇ Export CSV");
        Button btnPDF = new Button("⬇ Export PDF");
        Button btnTXT = new Button("⬇ Export TXT"); // Nouveau bouton TXT

        btnCSV.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        btnPDF.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white;");
        btnTXT.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;"); // couleur verte pour TXT

        ExportService csvService = new ExportService();
        ExportPDFService pdfService = new ExportPDFService();
        ExportTXTService txtService = new ExportTXTService(); // Nouveau service TXT

        btnCSV.disableProperty().bind(Bindings.isEmpty(filteredData));
        btnPDF.disableProperty().bind(Bindings.isEmpty(filteredData));
        btnTXT.disableProperty().bind(Bindings.isEmpty(filteredData));

        btnCSV.setOnAction(e -> exporter(
                "Exporter en CSV",
                "historique.csv",
                f -> csvService.exporterCSV(f.getAbsolutePath())
        ));

        btnPDF.setOnAction(e -> exporter(
                "Exporter en PDF",
                "historique.pdf",
                f -> pdfService.exporterPDF(f.getAbsolutePath())
        ));

        btnTXT.setOnAction(e -> exporter(
                "Exporter en TXT",
                "historique.txt",
                f -> txtService.exporterTXT(f.getAbsolutePath())
        ));

        /* =========================
           TOOLBAR
        ========================= */
        ToolBar toolBar = new ToolBar(
                btnCSV,
                btnPDF,
                btnTXT, // ajouter le bouton TXT
                new Separator(),
                new Label("Filtrer :"),
                filterBox
        );
        toolBar.setPadding(new Insets(8));

        setTop(toolBar);
        setCenter(table);
    }

    /* =========================
       MÉTHODES UTILES
    ========================= */
    private int ordreStatut(String s) {
        if (s.startsWith("EN COURS")) return 1;
        if (s.startsWith("RETARD")) return 2;
        return 3;
    }

    private void exporter(String titre, String nomFichier, java.util.function.Consumer<File> action) {
        FileChooser fc = new FileChooser();
        fc.setTitle(titre);
        fc.setInitialFileName(nomFichier);
        File f = fc.showSaveDialog(getScene().getWindow());
        if (f != null) action.accept(f);
    }
}
