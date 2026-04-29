package com.kbtu.university.cli;

import com.kbtu.university.factory.UserFactory;
import com.kbtu.university.model.user.Admin;
import com.kbtu.university.model.user.User;
import com.kbtu.university.storage.DataStorage;

import java.io.IOException;
import java.util.Scanner;

public class AdminMenu implements Menu {

    @Override
    public void run(User user, Scanner scanner) {
        Admin admin = (Admin) user;
        DataStorage db = DataStorage.getInstance();

        while (true) {
            System.out.println();
            System.out.println("Admin menu");
            System.out.println("1. List users");
            System.out.println("2. Add student");
            System.out.println("3. Remove user by id");
            System.out.println("4. View log");
            System.out.println("5. Save state to file");
            System.out.println("6. Load state from file");
            System.out.println("0. Logout");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("0")) {
                admin.logout();
                return;
            } else if (choice.equals("1")) {
                for (User u : admin.viewUsers()) {
                    System.out.println("  " + u);
                }
            } else if (choice.equals("2")) {
                System.out.print("Login: ");
                String login = scanner.nextLine().trim();
                System.out.print("Password: ");
                String password = scanner.nextLine().trim();
                System.out.print("Major: ");
                String major = scanner.nextLine().trim();
                System.out.print("Year (1-7): ");
                int year = parseInt(scanner.nextLine().trim(), 1);
                try {
                    UserFactory.createStudent(login, password, major, year);
                    System.out.println("Created");
                } catch (RuntimeException e) {
                    System.out.println("Failed: " + e.getMessage());
                }
            } else if (choice.equals("3")) {
                System.out.print("User id: ");
                String id = scanner.nextLine().trim();
                admin.removeUser(id);
                System.out.println("Removed");
            } else if (choice.equals("4")) {
                for (String line : admin.viewLog()) {
                    System.out.println("  " + line);
                }
            } else if (choice.equals("5")) {
                System.out.print("File [university.dat]: ");
                String f = scanner.nextLine().trim();
                if (f.isEmpty()) f = "university.dat";
                try {
                    db.save(f);
                    System.out.println("Saved");
                } catch (IOException e) {
                    System.out.println("Save failed: " + e.getMessage());
                }
            } else if (choice.equals("6")) {
                System.out.print("File [university.dat]: ");
                String f = scanner.nextLine().trim();
                if (f.isEmpty()) f = "university.dat";
                try {
                    DataStorage.load(f);
                    System.out.println("Loaded");
                    return;
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Load failed: " + e.getMessage());
                }
            } else {
                System.out.println("Unknown option");
            }
        }
    }

    private static int parseInt(String s, int fallback) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
