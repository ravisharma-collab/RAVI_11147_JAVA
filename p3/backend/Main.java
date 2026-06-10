import java.util.List;

/**
 * Main.java
 * Entry point for the Smart Warehouse Grid Locator backend.
 * Demonstrates all CRUD operations and OOP features.
 *
 * Flowchart:
 *   START → Initialize Warehouse → Add Sample Items
 *        → Test Search / Update / Remove → Display Grid → END
 *
 * @author  Smart Warehouse Project
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {

        printBanner();

        // ────────────────────────────────────────
        // STEP 1 – Initialise a 5×5 warehouse grid
        // ────────────────────────────────────────
        System.out.println("► Initialising 5×5 warehouse grid...\n");
        Warehouse warehouse = new Warehouse(5, 5);

        // ────────────────────────────────────────
        // STEP 2 – Add sample items (using overloaded addItem)
        // ────────────────────────────────────────
        System.out.println("═══════════════════════════════════════");
        System.out.println("         ADDING SAMPLE ITEMS");
        System.out.println("═══════════════════════════════════════");

        warehouse.addItem("I101", "Laptop",        50,  0, 0);
        warehouse.addItem("I102", "Phone",         25,  1, 2);
        warehouse.addItem("I103", "Tablet",        30,  2, 1);
        warehouse.addItem("I104", "Monitor",       15,  0, 4);
        warehouse.addItem("I105", "Keyboard",      100, 3, 3);
        warehouse.addItem("I106", "Mouse",         80,  4, 1);
        warehouse.addItem("I107", "USB Hub",       60,  2, 4);
        warehouse.addItem("I108", "Webcam",        40,  1, 0);
        warehouse.addItem("I109", "Headphones",    20,  3, 2);
        warehouse.addItem("I110", "SSD Drive",     35,  4, 4);

        // Attempt to add a duplicate ID – should fail gracefully
        System.out.println("\n[TEST] Attempting to add duplicate ID 'I101':");
        warehouse.addItem("I101", "Duplicate Item", 5, 0, 1);

        // Attempt an out-of-range position – should fail gracefully
        System.out.println("\n[TEST] Attempting to add item at invalid position (9, 9):");
        warehouse.addItem("I999", "Ghost Item", 1, 9, 9);

        // ────────────────────────────────────────
        // STEP 3 – Display initial grid
        // ────────────────────────────────────────
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("        INITIAL WAREHOUSE GRID");
        System.out.println("═══════════════════════════════════════");
        warehouse.displayGrid();

        // ────────────────────────────────────────
        // STEP 4 – Search operations
        // ────────────────────────────────────────
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("           SEARCH OPERATIONS");
        System.out.println("═══════════════════════════════════════");

        System.out.println("\n▸ Search by ID → 'I102':");
        warehouse.searchItemById("I102");

        System.out.println("\n▸ Search by ID → 'I999' (non-existent):");
        warehouse.searchItemById("I999");

        System.out.println("\n▸ Search by Name → 'Keyboard':");
        warehouse.searchItemByName("Keyboard");

        System.out.println("\n▸ Search by Name → 'drive' (partial, case-insensitive):");
        warehouse.searchItemByName("drive");

        // ────────────────────────────────────────
        // STEP 5 – Update quantity
        // ────────────────────────────────────────
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("          UPDATE QUANTITY");
        System.out.println("═══════════════════════════════════════");

        System.out.println("\n▸ Update I105 (Keyboard) quantity to 200:");
        warehouse.updateQuantity("I105", 200);

        System.out.println("\n▸ Update I999 (non-existent) – should fail:");
        warehouse.updateQuantity("I999", 10);

        System.out.println("\n▸ Update I101 with negative quantity – should fail:");
        warehouse.updateQuantity("I101", -5);

        // ────────────────────────────────────────
        // STEP 6 – Remove an item
        // ────────────────────────────────────────
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("           REMOVE OPERATIONS");
        System.out.println("═══════════════════════════════════════");

        System.out.println("\n▸ Remove item 'I108' (Webcam):");
        warehouse.removeItem("I108");

        System.out.println("\n▸ Remove item 'I999' (non-existent) – should fail:");
        warehouse.removeItem("I999");

        // ────────────────────────────────────────
        // STEP 7 – Display updated grid
        // ────────────────────────────────────────
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("         UPDATED WAREHOUSE GRID");
        System.out.println("═══════════════════════════════════════");
        warehouse.displayGrid();

        // ────────────────────────────────────────
        // STEP 8 – Statistics
        // ────────────────────────────────────────
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("          WAREHOUSE STATISTICS");
        System.out.println("═══════════════════════════════════════");

        List<GridItem> allItems = warehouse.getAllItems();
        System.out.println("Total Items    : " + allItems.size());
        System.out.println("Occupied Slots : " + warehouse.getOccupiedCount());
        System.out.println("Empty Slots    : " + warehouse.getEmptySlots().size());
        System.out.println("Total Quantity : " + warehouse.getTotalQuantity());
        System.out.printf ("Occupancy      : %.1f%%%n", warehouse.getOccupancyPercentage());

        // ────────────────────────────────────────
        // STEP 9 – List empty slots
        // ────────────────────────────────────────
        System.out.println("\n▸ Available (empty) slots:");
        for (int[] slot : warehouse.getEmptySlots()) {
            System.out.println("   Row " + slot[0] + ", Col " + slot[1]);
        }

        // ────────────────────────────────────────
        // STEP 10 – Export report
        // ────────────────────────────────────────
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("          FULL ITEM REPORT");
        System.out.println("═══════════════════════════════════════");
        System.out.printf("%-8s %-15s %8s%n", "Item ID", "Item Name", "Quantity");
        System.out.println("─".repeat(35));
        for (GridItem item : allItems) {
            System.out.printf("%-8s %-15s %8d%n",
                    item.getItemId(), item.getItemName(), item.getQuantity());
        }
        System.out.println("─".repeat(35));
        System.out.printf("%-24s %8d%n", "TOTAL", warehouse.getTotalQuantity());

        System.out.println("\n[DONE] All operations completed successfully.");
    }

    // ─────────────────────────────────────────────
    // Helper – ASCII banner
    // ─────────────────────────────────────────────
    private static void printBanner() {
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║    SMART WAREHOUSE GRID LOCATOR v1.0      ║");
        System.out.println("║       Backend – Core Java (OOP)           ║");
        System.out.println("╚═══════════════════════════════════════════╝");
        System.out.println();
    }
}
