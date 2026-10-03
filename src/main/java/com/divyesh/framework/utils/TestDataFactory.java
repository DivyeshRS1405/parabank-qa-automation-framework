package com.divyesh.framework.utils;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static String uniqueUsername() {
        return "qa" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 10);
    }

    public static String defaultPassword() {
        return "Passw0rd!23";
    }

    public static String randomSsn() {
        return "%d-%d-%d".formatted(
                ThreadLocalRandom.current().nextInt(100, 999),
                ThreadLocalRandom.current().nextInt(10, 99),
                ThreadLocalRandom.current().nextInt(1000, 9999));
    }

    public static String randomPhoneNumber() {
        return "555-%d".formatted(ThreadLocalRandom.current().nextInt(1000000, 9999999));
    }

    public static RegistrationData newRegistrationData() {
        return new RegistrationData(
                "Jordan",
                "Reyes",
                "742 Evergreen Terrace",
                "Springfield",
                "IL",
                "62704",
                randomPhoneNumber(),
                randomSsn(),
                uniqueUsername(),
                defaultPassword());
    }

    public static PayeeData newPayeeData() {
        return new PayeeData(
                "Springfield Power and Light",
                "100 Power Plant Way",
                "Springfield",
                "IL",
                "62704",
                randomPhoneNumber(),
                String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999)));
    }

    public record RegistrationData(
            String firstName,
            String lastName,
            String street,
            String city,
            String state,
            String zipCode,
            String phoneNumber,
            String ssn,
            String username,
            String password) {
    }

    public record PayeeData(
            String name,
            String street,
            String city,
            String state,
            String zipCode,
            String phoneNumber,
            String accountNumber) {
    }
}
