package com.example.historian;

import com.example.historian.models.account.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import java.util.List;

import com.example.historian.utils.StageManager;

import java.io.IOException;

public class AccountManagementController {
    @FXML
    private Button exitButton;

    @FXML
    private ListView<Account> accountsListView;
    private IAccountDAO accountDAO;
    @FXML
    private VBox accountContainer;
    public AccountManagementController() {
        accountDAO = new MockAccountDAO();
        accountDAO.addAccount(new Account("admin", "password", AccountPrivilege.ADMIN));
    }

    private ListCell<Account> renderCell(ListView<Account> accountsListView) {
        return new ListCell<>() {
            private void onContactSelected(MouseEvent mouseEvent) {
                ListCell<Account> clickedCell = (ListCell<Account>) mouseEvent.getSource();
                Account selectedAccount = clickedCell.getItem();
                if (selectedAccount != null) selectAccount(selectedAccount);
            }
            @Override
            protected void updateItem(Account account, boolean empty) {
                super.updateItem(account, empty);
                if (empty || account == null || account.getUsername() == null) {
                    setText(null);
                    super.setOnMouseClicked(this::onAccountSelected);
                } else {
                    setText(account.getUsername() + " (" + account.getAccountPrivilege() + ") ");
                }
            }
        };
    }

    private void syncAccounts() {
        accountsListView.getItems().clear();
        List<Account> accounts = accountDAO.getAllAccounts();
        boolean hasAccounts = !accounts.isEmpty();
        if (hasAccounts) {
            accountsListView.getItems().addAll(accounts);
        }
        accountContainer.setVisible(hasAccounts);
    }

    @FXML
    public void initialize() {
        accountsListView.setCellFactory(this::renderCell);
        syncAccounts();
        accountsListView.getSelectionModel().selectFirst();
        Account firstAccount = accountsListView.getSelectionModel().getSelectedItem();
        if (firstAccount != null) {
            selectAccount(firstAccount);
        }
    }

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;

    private void selectAccount(Account account) {
        accountsListView.getSelectionModel().select(account);
        usernameTextField.setText(account.getUsername());
        passwordTextField.setText(account.get());
    }

    @FXML
    protected void onExitButtonClick() throws IOException {
        StageManager.switchScene("database-view.fxml");
    }
}
