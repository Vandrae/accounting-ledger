package com.pluralsight;

public class HomeMenu {

    //First menu user sees
    public static void homeMenu() {
        boolean appRunning = true;
        while (appRunning) {
            //Display Home Screen
            System.out.println(" ");
            System.out.println("Home Screen");
            System.out.println(" ");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Pick an option from the menu above: ");
            String menuSelection = input.nextLine().toUpperCase();
            System.out.println("-------------------------------------");
            System.out.println(" ");

            switch(menuSelection){
                case "D" -> makeDeposit();
                case "P" -> makePayment();
                case "L" -> ledgerMenu();
                case "X" -> {
                    System.out.println("Thank you! Goodbye");
                    appRunning = false;
                }
            }

        }

    }



}
