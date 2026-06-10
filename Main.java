import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Warehouse warehouse = new Warehouse(5, 5);

        warehouse.addItem(0, 0,
                new GridItem("I101", "Laptop", 10));

        warehouse.addItem(1, 2,
                new GridItem("I102", "Phone", 25));

        warehouse.addItem(2, 3,
                new GridItem("I103", "Keyboard", 15));

        warehouse.addItem(4, 1,
                new GridItem("I104", "Monitor", 8));

        int choice;

        do {

            System.out.println("\n===== SMART WAREHOUSE GRID LOCATOR =====");
            System.out.println("1. Display Grid");
            System.out.println("2. Search Item");
            System.out.println("3. Update Quantity");
            System.out.println("4. Remove Item");
            System.out.println("5. Exit");

            System.out.print("Enter Choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:

                    warehouse.displayGrid();
                    break;

                case 2:

                    System.out.print("Enter Item ID: ");
                    String id = sc.next();

                    warehouse.searchItem(id);
                    break;

                case 3:

                    System.out.print("Enter Item ID: ");
                    id = sc.next();

                    System.out.print("Enter New Quantity: ");
                    int qty = sc.nextInt();

                    warehouse.updateQuantity(id, qty);
                    break;

                case 4:

                    System.out.print("Enter Item ID: ");
                    id = sc.next();

                    warehouse.removeItem(id);
                    break;

                case 5:

                    System.out.println("Exiting...");
                    break;

                default:

                    System.out.println("Invalid Choice");
            }

        } while (choice != 5);

        sc.close();
    }
}