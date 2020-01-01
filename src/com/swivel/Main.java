package com.swivel;

import java.util.Scanner;

public class Main {
    private static Integer Search;
    private static String Selected, Tfields, Ufields, Ofields;
    public static void main(String[] args) {
	// write your code here

        Scanner SelectedMenu = new Scanner (System.in);
        Scanner userMenu = new Scanner (System.in);
        Scanner ticketMenu = new Scanner (System.in);
        Scanner orgMenu = new Scanner (System.in);


        System.out.print("Type 'quit' to exit at any time, Press 'Enter' to continue \n \n \n ");
        System.out.print("Select search options: \n * Press 1 to select Category \n * Type 'all' to view all data \n * Type 'quit' to exit \n \n \n");


        Selected = SelectedMenu.nextLine();
//        System.out.println(Selected);
        if (Selected.equals("1") || Selected.equals("2")) {
//            Integer select = Integer.parseInt(Selected)
            System.out.print("Select Categories 1. Users or 2. Tickets or 3. Organizations \n");
            Search = SelectedMenu.nextInt();
        }else if (Selected.equals("quit")) {
            System.out.println("Exit!..");
            System.exit(0);
//            SelectedMenu.close();
        }else {
            System.out.println(("No Menu found"));
            System.exit(0);

        }
        switch (Search) {
            case 1:
                System.out.print("Enter User field \n");
                users userObject = new users();
                Ufields = userMenu.nextLine();
                userObject.userField(Ufields);
                break;
            case 2:
                System.out.print("Enter Ticket field \n");
                tickets ticketObject = new tickets();
                Tfields = ticketMenu.nextLine();
                ticketObject.ticketField(Tfields);
                break;
            case 3:
                System.out.print("Enter Organizations field \n");
                organizations orgObject = new organizations();
                Ofields = orgMenu.nextLine();
                orgObject.orgField(Ofields);
                break;
            default:
                System.out.println("No File found");
                System.exit(0);
                break;
        }
    }
}
