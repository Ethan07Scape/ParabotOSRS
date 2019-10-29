package org.osrs.min.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.osrs.min.api.interactive.Players;
import org.osrs.min.api.wrappers.Player;
import org.osrs.min.api.wrappers.Tile;

import javax.swing.*;

public class DebuggerUI {
    private JFrame frame;
    private JFXPanel jfxPanel = new JFXPanel();
    private Button load, clear;
    private ComboBox<String> entityBox;
    private TextField nameText;
    private TableView tableView;
    private TableColumn<Integer, Entity> idColumn, indexColumn, amountColumn, distanceColumn;
    private TableColumn<String, Entity> nameColumn;
    private TableColumn<Tile, Entity> locationColumn;

    public DebuggerUI() {
        loadDebugUI();
    }

    /**
     * @author Ethan
     */
    public static void main(String[] args) {
        new DebuggerUI();
    }

    private void loadDebugUI() {
        Platform.runLater(() -> {
            frame = new JFrame("Entity debugger");
            jfxPanel = new JFXPanel();
            nameText = new TextField();
            nameText.setPromptText("Search by name?");

            entityBox = new ComboBox<>();
            entityBox.getItems().addAll(
                    "Players"
            );
            entityBox.setPromptText("Choose");

            load = new Button("Search/All");
            load.setOnAction(e -> loadEntites());

            clear = new Button("Clear");
            clear.setOnAction(e -> clear());

            tableView = new TableView();
            setColumns();
            tableView.getColumns().addAll(indexColumn, nameColumn, idColumn, amountColumn, distanceColumn, locationColumn);


            HBox hBox = new HBox();
            hBox.getChildren().addAll(entityBox, nameText, load, clear);
            hBox.setSpacing(25);

            VBox layout = new VBox(5);
            layout.setPadding(new Insets(0, 0, 10, 0));

            layout.getChildren().addAll(tableView, hBox);

            jfxPanel.setScene(new Scene(layout, 475, 300));
            SwingUtilities.invokeLater(() -> {
                frame.add(jfxPanel);
                frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            });
        });
    }

    private void setColumns() {
        nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setMaxWidth(150);

        idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setMaxWidth(50);

        indexColumn = new TableColumn<>("Index");
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("index"));
        indexColumn.setMaxWidth(50);

        amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setMinWidth(50);
        amountColumn.setMaxWidth(100);

        distanceColumn = new TableColumn<>("Distance");
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        distanceColumn.setMaxWidth(75);

        locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        locationColumn.setMinWidth(150);

    }

    public void setVisible() {
        frame.setVisible(true);
    }

    private void clear() {
        if (!tableView.getItems().isEmpty())
            tableView.getItems().clear();
        clearText();

    }

    private void clearText() {
        if (!nameText.getText().isEmpty())
            nameText.clear();
    }

    private void loadEntites() {

        String comboValue = entityBox.getValue();

        if (comboValue.toLowerCase().equals("players")) {
            tableView.setItems(getPlayers());
        }
        clearText();

    }

    private ObservableList<Entity> getPlayers() {
        ObservableList<Entity> players = FXCollections.observableArrayList();
        if (nameText.getText().isEmpty()) {
            for (Player p : Players.getNearest()) {
                if (p != null) {
                    players.add(new Entity(p.getIndex(), p.getIndex(), 1, p.getName(), p.getLocation(), p.getLocation().distanceTo()));
                }
            }
        } else {
            final String name = nameText.getText();
            for (Player p : Players.getNearest(p -> p != null && p.getName().toLowerCase().contains(name.toLowerCase()) || p != null && p.getName().toLowerCase().equals(name.toLowerCase()))) {
                if (p != null) {
                    players.add(new Entity(p.getIndex(), p.getIndex(), 1, p.getName(), p.getLocation(), p.getLocation().distanceTo()));
                }
            }
        }
        return players;
    }
}




