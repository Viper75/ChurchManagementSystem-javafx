package org.viper75.churchmgt.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import org.viper75.churchmgt.model.Currency;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class TreeTVController implements Initializable {

    @FXML private TreeTableView<Currency> currencyTreeTableView;
    @FXML private TreeTableColumn<Currency, String> currencyStringTreeTableColumn;
    @FXML private TreeTableColumn<Currency, BigDecimal> currencyBigDecimalTreeTableColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
