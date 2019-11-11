package org.osrs.min.ui.debugger;

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
import org.osrs.min.api.data.Bank;
import org.osrs.min.api.data.Inventory;
import org.osrs.min.api.data.grandexchange.GrandExchangeOffers;
import org.osrs.min.api.interactive.GroundItems;
import org.osrs.min.api.interactive.Npcs;
import org.osrs.min.api.interactive.Players;
import org.osrs.min.api.interactive.SceneObjects;
import org.osrs.min.api.packets.outgoing.PacketWriter;
import org.osrs.min.api.wrappers.*;

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
                    "Players",
                    "Npcs",
                    "Objects",
                    "Inventory",
                    "Bank",
                    "GroundItems",
                    "GE Offers",
                    "Testing"
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

        final String comboValue = entityBox.getValue();

        if (comboValue.toLowerCase().equals("players")) {
            tableView.setItems(getPlayers());
        } else if (comboValue.toLowerCase().equals("npcs")) {
            tableView.setItems(getNpcs());
        } else if (comboValue.toLowerCase().equals("objects")) {
            tableView.setItems(getObjects());
        } else if (comboValue.toLowerCase().equals("inventory")) {
            tableView.setItems(getInventory());
        } else if (comboValue.toLowerCase().equalsIgnoreCase("bank")) {
            tableView.setItems(getBank());
        } else if (comboValue.toLowerCase().equals("grounditems")) {
            tableView.setItems(getGroundItems());
        } else if (comboValue.toLowerCase().equals("ge offers")) {
            tableView.setItems(getOffers());
        } else if (comboValue.toLowerCase().equals("testing")) {
            PacketWriter.getInstance().sendRandomFocus();
        }
        clearText();

    }

    private ObservableList<Entity> getOffers() {
        ObservableList<Entity> offers = FXCollections.observableArrayList();
        if (nameText.getText().isEmpty()) {
            for (GrandExchangeOffer g : GrandExchangeOffers.getOffers()) {
                if (g != null) {
                    offers.add(new Entity(g.getIndex(), g.getItemId(), g.getItemQuantity(), g.getType().name(), null, -1));
                }
            }
        } else {

        }
        return offers;
    }

    private ObservableList<Entity> getNpcs() {
        ObservableList<Entity> npcs = FXCollections.observableArrayList();
        if (nameText.getText().isEmpty()) {
            for (NPC n : Npcs.getNearest()) {
                if (n != null) {
                    npcs.add(new Entity(n.getIndex(), n.getId(), 1, n.getName(), n.getLocation(), n.distanceTo()));
                }
            }
        } else {
            final String name = nameText.getText();
            for (NPC n : Npcs.getNearest(n -> n != null && n.getName().toLowerCase().contains(name.toLowerCase()) || n != null && n.getName().toLowerCase().equals(name.toLowerCase()))) {
                if (n != null) {
                    npcs.add(new Entity(n.getIndex(), n.getId(), 1, n.getName(), n.getLocation(), n.getLocation().distanceTo()));
                }
            }
        }
        return npcs;
    }

    private ObservableList<Entity> getInventory() {
        ObservableList<Entity> items = FXCollections.observableArrayList();
        if (nameText.getText().isEmpty()) {
            for (Item i : Inventory.getAllItems()) {
                if (i != null) {
                    items.add(new Entity(i.getHash(), i.getId(), i.getStackSize(), i.getName(), null, -1));
                }
            }
        } else {
            final String name = nameText.getText();
            for (Item i : Inventory.getAllItems(n -> n != null && n.getName().toLowerCase().contains(name.toLowerCase()) || n != null && n.getName().toLowerCase().equals(name.toLowerCase()))) {
                if (i != null) {
                    items.add(new Entity(i.getIndex(), i.getId(), i.getStackSize(), i.getName(), null, -1));
                }
            }
        }
        return items;
    }

    private ObservableList<Entity> getBank() {
        ObservableList<Entity> items = FXCollections.observableArrayList();
        if (nameText.getText().isEmpty()) {
            for (Item i : Bank.getItems()) {
                if (i != null) {
                    items.add(new Entity(i.getIndex(), i.getId(), i.getStackSize(), i.getName(), null, -1));
                }
            }
        } else {
            final String name = nameText.getText();
            for (Item i : Bank.getItems(n -> n != null && n.getName().toLowerCase().contains(name.toLowerCase()) || n != null && n.getName().toLowerCase().equals(name.toLowerCase()))) {
                if (i != null) {
                    items.add(new Entity(i.getIndex(), i.getId(), i.getStackSize(), i.getName(), null, -1));
                }
            }
        }
        return items;
    }

    private ObservableList<Entity> getObjects() {
        ObservableList<Entity> objects = FXCollections.observableArrayList();
        if (nameText.getText().isEmpty()) {
            for (SceneObject n : SceneObjects.getNearest()) {
                if (n != null) {
                    objects.add(new Entity((int) n.getUID(), n.getId(), 1, n.getName(), n.getLocation(), n.distanceTo()));
                }
            }
        } else {
            final String name = nameText.getText();
            for (SceneObject n : SceneObjects.getNearest(n -> n != null && n.getName().toLowerCase().contains(name.toLowerCase()) || n != null && n.getName().toLowerCase().equals(name.toLowerCase()))) {
                if (n != null) {
                    objects.add(new Entity((int) n.getUID(), n.getId(), 1, n.getName(), n.getLocation(), n.distanceTo()));
                }
            }
        }
        return objects;
    }

    private ObservableList<Entity> getGroundItems() {
        ObservableList<Entity> objects = FXCollections.observableArrayList();
        if (nameText.getText().isEmpty()) {
            for (GroundItem g : GroundItems.getNearest()) {
                if (g != null) {
                    objects.add(new Entity(-1, g.getId(), g.getStackSize(), g.getName(), g.getLocation(), g.distanceTo()));
                }
            }
        } else {
            final String name = nameText.getText();
            for (GroundItem g : GroundItems.getNearest(n -> n != null && n.getName().toLowerCase().contains(name.toLowerCase()) || n != null && n.getName().toLowerCase().equals(name.toLowerCase()))) {
                if (g != null) {
                    objects.add(new Entity(-1, g.getId(), g.getStackSize(), g.getName(), g.getLocation(), g.distanceTo()));
                }
            }
        }
        return objects;
    }
    private ObservableList<Entity> getPlayers() {
        ObservableList<Entity> players = FXCollections.observableArrayList();
        if (nameText.getText().isEmpty()) {
            for (Player p : Players.getNearest()) {
                if (p != null) {
                    players.add(new Entity(p.getIndex(), -1, 1, p.getName(), p.getLocation(), p.getLocation().distanceTo()));
                }
            }
        } else {
            final String name = nameText.getText();
            for (Player p : Players.getNearest(p -> p != null && p.getName().toLowerCase().contains(name.toLowerCase()) || p != null && p.getName().toLowerCase().equals(name.toLowerCase()))) {
                if (p != null) {
                    players.add(new Entity(p.getIndex(), -1, 1, p.getName(), p.getLocation(), p.getLocation().distanceTo()));
                }
            }
        }
        return players;
    }
}




