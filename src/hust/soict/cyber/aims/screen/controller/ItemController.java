package hust.soict.cyber.aims.screen.controller;

import hust.soict.cyber.aims.cart.Cart;
import hust.soict.cyber.aims.media.Media;
import hust.soict.cyber.aims.media.Playable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ItemController {
    private Cart cart;

    private Media media;

    @FXML
    private Button btnAddToCart;

    @FXML
    private Button btnPlay;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblCost;

    public ItemController(Cart cart){
        this.cart = cart;
    }

    public void setData(Media media){
        this.media = media;
        lblTitle.setText(media.getTitle());
        lblCost.setText(media.getCost() + " $");
        if (media instanceof Playable){
            btnPlay.setVisible(true);
        }
        else{
            btnPlay.setVisible(false);
            HBox.setMargin(btnAddToCart, new Insets(0, 0, 0, 60));
        }
    }

    @FXML
    void btnAddToCartClicked(ActionEvent event) {
        cart.addMedia(media);
    }

    @FXML
    void btnPlayClicked(ActionEvent event) {
        Playable mediaPlayable = (Playable) media;
        mediaPlayable.play();
    }

}
