package com.kbtu.university.cli;

import com.kbtu.university.model.user.User;

import java.util.Scanner;

public interface Menu {

    void run(User user, Scanner scanner);
}
