package com.pluralsight;

import com.pluralsight.model.Transaction;
import com.pluralsight.service.ReportService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountingApp {

    // Console Colors
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RESET = "\u001B[0m";

    //allows user input
    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
      HomeMenu.homeMenu();
    }

}



