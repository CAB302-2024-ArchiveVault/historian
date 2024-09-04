package com.example.historian;

import com.example.historian.models.account.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import java.util.List;
import java.util.EnumSet;
import java.util.Set;

import com.example.historian.utils.StageManager;

import java.io.IOException;

public class AccountManagementController {
    private Boolean isResetPasswordMode = false;
    private Boolean isCreateAccountMode = false;

    @FXML
    private ListView<Account> accountsListView;
    private final IAccountDAO accountDAO;

    @FXML
    private VBox accountContainer;

    @FXML
    private ComboBox<AccountPrivilege> privilegeComboBox;

    public AccountManagementController() {
        accountDAO = new MockAccountDAO();
        accountDAO.addAccount(new Account("admin", "password", AccountPrivilege.ADMIN));
    }

    private ListCell<Account> renderCell(ListView<Account> accountsListView) {
        return new ListCell<>() {
            private void onAccountSelected(MouseEvent mouseEvent) {
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
        Set<AccountPrivilege> availablePrivileges = EnumSet.complementOf(EnumSet.of(AccountPrivilege.DATABASE_OWNER, AccountPrivilege.VIEWER));
        ObservableList<AccountPrivilege> privileges = FXCollections.observableArrayList(availablePrivileges);
        privilegeComboBox.setItems(privileges);
    }

    @FXML
    private TextField usernameTextField;
    @FXML
    private Label databaseOwnerLabel;
    @FXML
    private Button deleteAccountButton;

    @FXML
    private Button resetPasswordButton;
    @FXML
    private TextField newPasswordTextField;

    private void setResetPasswordMode(Boolean isMode) {
        isResetPasswordMode = isMode;
        resetPasswordButton.setVisible(!isMode);
        newPasswordTextField.setVisible(isMode);
    }

    private void setCreateAccountMode(Boolean isMode) {
        isCreateAccountMode = isMode;
        deleteAccountButton.setDisable(isMode);
        usernameTextField.setEditable(isMode);
        setResetPasswordMode(isMode);
    }

    private void setDatabaseOwnerMode(Boolean isMode) {
        databaseOwnerLabel.setVisible(isMode);
        privilegeComboBox.setVisible(!isMode);
        deleteAccountButton.setDisable(isMode);
    }

    private void selectAccount(Account account) {
        setResetPasswordMode(false);
        setCreateAccountMode(false);
        accountsListView.getSelectionModel().select(account);
        usernameTextField.setText(account.getUsername());
        if (account.getAccountPrivilege() == AccountPrivilege.DATABASE_OWNER) {
            setDatabaseOwnerMode(true);
        } else {
            setDatabaseOwnerMode(false);
            privilegeComboBox.getSelectionModel().select(account.getAccountPrivilege());
        }
    }

    @FXML
    private void onResetPasswordShow() {
        Account selectedAccount = accountsListView.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            setResetPasswordMode(true);
            newPasswordTextField.setText("P@ssw0rd");
            newPasswordTextField.requestFocus();
        }
    }

    @FXML
    private void onEditConfirm() {
        if (isCreateAccountMode) {
            String newUsername = usernameTextField.getText();
            String newPassword = newPasswordTextField.getText();
            AccountPrivilege newAccountPrivilege = privilegeComboBox.getSelectionModel().getSelectedItem();
            Account newAccount = new Account(newUsername, newPassword, newAccountPrivilege);
            accountDAO.addAccount(newAccount);
            syncAccounts();

            accountsListView.getSelectionModel().select(newAccount);
            Account selectedAccount = accountsListView.getSelectionModel().getSelectedItem();
            if (selectedAccount != null) {
                selectAccount(selectedAccount);
            }

        } else {
            Account selectedAccount = accountsListView.getSelectionModel().getSelectedItem();
            if (selectedAccount != null) {
                if (selectedAccount.getAccountPrivilege() != AccountPrivilege.DATABASE_OWNER) {
                    selectedAccount.setAccountPrivilege(privilegeComboBox.getSelectionModel().getSelectedItem());
                }
                if (isResetPasswordMode) {
                    selectedAccount.resetPassword(newPasswordTextField.getText());
                }
                accountDAO.updateAccount(selectedAccount);
                syncAccounts();
                selectAccount(selectedAccount);
            }
        }

        setResetPasswordMode(false);
        setCreateAccountMode(false);
    }

    @FXML
    private void onDeleteAccount() {
        Account selectedAccount = accountsListView.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            accountDAO.removeAccount(selectedAccount);
            usernameTextField.setText("");
            syncAccounts();
            accountsListView.getSelectionModel().selectFirst();
            Account firstAccount = accountsListView.getSelectionModel().getSelectedItem();
            selectAccount(firstAccount);
        }

        setResetPasswordMode(false);
    }

    @FXML
    private void onEditCancel() {
        Account selectedAccount = accountsListView.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            selectAccount(selectedAccount);
        } else {
            accountsListView.getSelectionModel().selectFirst();
            Account firstAccount = accountsListView.getSelectionModel().getSelectedItem();
            selectAccount(firstAccount);
        }

        setResetPasswordMode(false);
        setCreateAccountMode(false);
    }

    @FXML
    private void onCreateAccount() {
        setCreateAccountMode(true);
        setDatabaseOwnerMode(false);
        accountsListView.getSelectionModel().clearSelection();
        usernameTextField.setText("New_Username");
        usernameTextField.requestFocus();
        privilegeComboBox.getSelectionModel().select(AccountPrivilege.MEMBER);
        newPasswordTextField.setText("P@ssw0rd");
    }

    @FXML
    protected void onExitButtonClick() throws IOException {
        StageManager.switchScene("database-view.fxml");
    }
}
