package com.divyesh.tests.db;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ParaBank's public demo instance does not expose its production database for external
 * connections, so these tests run against a local, self-contained H2 database seeded with data
 * that mirrors the shape of ParaBank's customer/account schema. They demonstrate the JDBC
 * validation layer of the framework rather than validating the live application's real data.
 */
public class AccountDbTest {

    private static final String JDBC_URL =
            "jdbc:h2:mem:parabank_test;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:db/schema.sql'";

    private Connection connection;

    @BeforeClass(alwaysRun = true)
    public void openConnection() throws SQLException {
        connection = DriverManager.getConnection(JDBC_URL, "sa", "");
    }

    @AfterClass(alwaysRun = true)
    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test(description = "Selecting an account by id returns its seeded balance")
    public void selectAccountById_returnsExpectedBalance() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT balance FROM accounts WHERE id = ?")) {
            statement.setInt(1, 1001);
            try (ResultSet resultSet = statement.executeQuery()) {
                Assert.assertTrue(resultSet.next(), "Account 1001 should exist");
                Assert.assertEquals(resultSet.getBigDecimal("balance"), new BigDecimal("500.00"),
                        "Account 1001 should have its seeded balance");
            }
        }
    }

    @Test(description = "Selecting accounts by customer id returns every account belonging to that customer")
    public void selectAccountsByCustomerId_returnsExpectedCount() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id FROM accounts WHERE customer_id = ?")) {
            statement.setInt(1, 1);
            try (ResultSet resultSet = statement.executeQuery()) {
                int count = 0;
                while (resultSet.next()) {
                    count++;
                }
                Assert.assertEquals(count, 2, "Customer 1 should have exactly two seeded accounts");
            }
        }
    }

    @Test(description = "Updating an account balance is reflected in a subsequent select")
    public void updateAccountBalance_reflectsInSubsequentSelect() throws SQLException {
        try (PreparedStatement update = connection.prepareStatement(
                "UPDATE accounts SET balance = balance - ? WHERE id = ?")) {
            update.setBigDecimal(1, new BigDecimal("50.00"));
            update.setInt(2, 2001);
            int rowsUpdated = update.executeUpdate();
            Assert.assertEquals(rowsUpdated, 1, "Exactly one account row should be updated");
        }

        try (PreparedStatement select = connection.prepareStatement(
                "SELECT balance FROM accounts WHERE id = ?")) {
            select.setInt(1, 2001);
            try (ResultSet resultSet = select.executeQuery()) {
                Assert.assertTrue(resultSet.next(), "Account 2001 should still exist after the update");
                Assert.assertEquals(resultSet.getBigDecimal("balance"), new BigDecimal("700.00"),
                        "Account 2001's balance should reflect the debit");
            }
        }
    }
}
