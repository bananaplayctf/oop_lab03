package hust.soict.cyber.aims.screen;

import hust.soict.cyber.aims.cart.Cart;
import hust.soict.cyber.aims.media.Media;
import hust.soict.cyber.aims.media.Playable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class CartScreenController {
    private Cart cart;

    @FXML
    private TableView<Media> tblMedia;
    @FXML
    private TableColumn<Media, String> colMediaTitle;
    @FXML
    private TableColumn<Media, String> colMediaCategory;
    @FXML
    private TableColumn<Media, Float> colMediaCost;
    @FXML
    private Button btnPlay;
    @FXML
    private Button btnRemove;
    @FXML
    private TextField tfFilter;
    @FXML
    private RadioButton radioBtnFilterId;
    @FXML
    private RadioButton radioBtnFilterTitle;
    @FXML
    private Label lblTotalCost;
    @FXML
    private Button btnPlaceOrder;

    private FilteredList<Media> filteredData;

    public CartScreenController(Cart cart) {
        this.cart = cart;
    }

    @FXML
    private void initialize() {
        colMediaTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colMediaCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colMediaCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        filteredData = new FilteredList<>(cart.getItemsOrdered(), p -> true);
        tblMedia.setItems(filteredData);

        btnPlay.setVisible(false);
        btnRemove.setVisible(false);

        tblMedia.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateButtonBar(newValue);
            }
        });

        tfFilter.textProperty().addListener((observable, oldValue, newValue) -> showFilteredMedia(newValue));

        cart.getItemsOrdered().addListener((ListChangeListener<Media>) change -> updateTotalCost());

        updateTotalCost();
    }

    private void updateButtonBar(Media media) {
        btnRemove.setVisible(true);
        btnPlay.setVisible(media instanceof Playable);
    }

    @FXML
    void btnRemovePressed(ActionEvent event) {
        Media media = tblMedia.getSelectionModel().getSelectedItem();
        if (media != null) {
            cart.removeMedia(media);
        }
    }

    @FXML
    void btnPlayPressed(ActionEvent event) {
        Media media = tblMedia.getSelectionModel().getSelectedItem();
        if (media instanceof Playable) {
            ((Playable) media).play();
        }
    }

    @FXML
    void btnPlaceOrderPressed(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Placed");
        alert.setHeaderText(null);
        alert.setContentText("Your order has been placed successfully!");
        alert.showAndWait();

        cart.clear();
    }

    private void showFilteredMedia(String filter) {
        if (filter == null || filter.isEmpty()) {
            filteredData.setPredicate(p -> true);
        } else {
            if (radioBtnFilterId.isSelected()) {
                try {
                    int id = Integer.parseInt(filter);
                    filteredData.setPredicate(media -> media.getId() == id);
                } catch (NumberFormatException e) {
                    filteredData.setPredicate(p -> false);
                }
            } else if (radioBtnFilterTitle.isSelected()) {
                filteredData.setPredicate(media -> media.getTitle().toLowerCase().contains(filter.toLowerCase()));
            }
        }
    }

    private void updateTotalCost() {
        float totalCost = cart.totalCost();
        lblTotalCost.setText(String.format("%.2f $", totalCost));
    }
}