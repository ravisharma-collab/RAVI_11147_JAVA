import java.util.ArrayList;
import java.util.List;

/**
 * Warehouse.java
 * Core class that manages the 2D warehouse grid.
 * Demonstrates: 2D Arrays, OOP, Encapsulation, Method Overloading
 *
 * @author  Smart Warehouse Project
 * @version 1.0
 */
public class Warehouse {

    // ─────────────────────────────────────────────
    // Attributes
    // ─────────────────────────────────────────────
    private GridItem[][] grid;   // 2D array of grid cells
    private int rows;
    private int columns;

    // ─────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────

    /**
     * Initialises the warehouse with the given dimensions.
     * All cells start as null (empty).
     *
     * @param rows    number of rows    (>= 1)
     * @param columns number of columns (>= 1)
     */
    public Warehouse(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new IllegalArgumentException("Grid dimensions must be at least 1x1.");
        }
        this.rows    = rows;
        this.columns = columns;
        this.grid    = new GridItem[rows][columns];   // all cells = null
    }

    // ─────────────────────────────────────────────
    // Getters
    // ─────────────────────────────────────────────

    public int getRows()    { return rows;    }
    public int getColumns() { return columns; }

    /** Direct access to the grid (read-only intent). */
    public GridItem[][] getGrid() { return grid; }

    // ─────────────────────────────────────────────
    // addItem  – Method Overloading
    // ─────────────────────────────────────────────

    /**
     * Adds a pre-built GridItem object at the specified position.
     *
     * @param item   the GridItem to store
     * @param row    target row index
     * @param column target column index
     * @return true if added successfully, false otherwise
     */
    public boolean addItem(GridItem item, int row, int column) {
        // Validate coordinates
        if (!isValidPosition(row, column)) {

            System.out.println("[ERROR] Invalid position (" + row + ", " + column + ").");
            return false;
        }
        // Check slot occupancy
        if (grid[row][column] != null) {
            System.out.println("[ERROR] Slot (" + row + ", " + column + ") is already occupied by "
                    + grid[row][column].getItemId() + ".");
            return false;
        }
        // Check for duplicate ID (use silent internal check to avoid stray output)
        if (findItemSilent(item.getItemId()) != null) {
            System.out.println("[ERROR] Item ID '" + item.getItemId() + "' already exists in the warehouse.");
            return false;
        }
        grid[row][column] = item;
        System.out.println("[SUCCESS] Item '" + item.getItemId() + "' added at (" + row + ", " + column + ").");
        return true;
    }

    /**
     * Overloaded addItem – creates a GridItem from raw parameters and adds it.
     *
     * @param itemId   unique item ID
     * @param itemName item name
     * @param quantity stock quantity
     * @param row      target row
     * @param column   target column
     * @return true if added successfully, false otherwise
     */
    public boolean addItem(String itemId, String itemName, int quantity, int row, int column) {
        try {
            GridItem newItem = new GridItem(itemId, itemName, quantity);
            return addItem(newItem, row, column);
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
            return false;
        }
    }

    // ─────────────────────────────────────────────
    // searchItemById
    // ─────────────────────────────────────────────

    /**
     * Searches the entire grid for an item by its ID using nested loops.
     *
     * @param itemId the ID to search for
     * @return the GridItem if found, null otherwise
     */
    public GridItem searchItemById(String itemId) {
        if (itemId == null || itemId.trim().isEmpty()) return null;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (grid[r][c] != null && grid[r][c].getItemId().equalsIgnoreCase(itemId.trim())) {
                    System.out.println("[FOUND] Item ID: " + itemId
                            + "  →  Row: " + r + ", Column: " + c
                            + " | " + grid[r][c].getItemName()
                            + " | Qty: " + grid[r][c].getQuantity());
                    return grid[r][c];
                }
            }
        }
        System.out.println("[NOT FOUND] No item with ID '" + itemId + "' exists in the warehouse.");
        return null;
    }

    // ─────────────────────────────────────────────
    // searchItemByName
    // ─────────────────────────────────────────────

    /**
     * Searches the grid for items whose name contains the given keyword (case-insensitive).
     * Multiple matches are possible; all are returned.
     *
     * @param name keyword / full name to search
     * @return list of matching GridItems (may be empty)
     */
    public List<GridItem> searchItemByName(String name) {
        List<GridItem> results = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) return results;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (grid[r][c] != null &&
                        grid[r][c].getItemName().toLowerCase().contains(name.trim().toLowerCase())) {
                    results.add(grid[r][c]);
                    System.out.println("[MATCH] '" + name + "'  →  Row: " + r + ", Column: " + c
                            + " | ID: " + grid[r][c].getItemId()
                            + " | Qty: " + grid[r][c].getQuantity());
                }
            }
        }
        if (results.isEmpty()) {
            System.out.println("[NOT FOUND] No item with name containing '" + name + "' found.");
        }
        return results;
    }

    // ─────────────────────────────────────────────
    // removeItem
    // ─────────────────────────────────────────────

    /**
     * Removes an item from the grid by its ID.
     *
     * @param itemId the ID of the item to remove
     * @return true if removed, false if not found
     */
    public boolean removeItem(String itemId) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (grid[r][c] != null && grid[r][c].getItemId().equalsIgnoreCase(itemId.trim())) {
                    System.out.println("[REMOVED] Item '" + itemId + "' removed from (" + r + ", " + c + ").");
                    grid[r][c] = null;
                    return true;
                }
            }
        }
        System.out.println("[ERROR] Item '" + itemId + "' not found. Cannot remove.");
        return false;
    }

    // ─────────────────────────────────────────────
    // updateQuantity
    // ─────────────────────────────────────────────

    /**
     * Updates the stock quantity of an existing item.
     *
     * @param itemId      ID of the item to update
     * @param newQuantity new quantity value (>= 0)
     * @return true if updated, false if item not found
     */
    public boolean updateQuantity(String itemId, int newQuantity) {
        if (newQuantity < 0) {
            System.out.println("[ERROR] Quantity cannot be negative.");
            return false;
        }
        GridItem item = searchItemById(itemId);
        if (item != null) {
            int old = item.getQuantity();
            item.setQuantity(newQuantity);
            System.out.println("[UPDATED] Item '" + itemId + "' quantity changed from " + old + " to " + newQuantity + ".");
            return true;
        }
        return false;  // message already printed by searchItemById
    }

    // ─────────────────────────────────────────────
    // displayGrid
    // ─────────────────────────────────────────────

    /**
     * Prints the warehouse grid to the console.
     * Occupied cells show the item ID; empty cells show "[ --- ]".
     */
    public void displayGrid() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║        WAREHOUSE GRID (" + rows + "x" + columns + ")              ║");
        System.out.println("╠══════════════════════════════════════════╣");

        // Column header
        System.out.print("     ");
        for (int c = 0; c < columns; c++) {
            System.out.printf("  Col%-3d", c);
        }
        System.out.println();

        for (int r = 0; r < rows; r++) {
            System.out.printf("Row%d ", r);
            for (int c = 0; c < columns; c++) {
                if (grid[r][c] == null) {
                    System.out.print(" [-----] ");
                } else {
                    System.out.printf(" [%-5s] ", grid[r][c].getItemId());
                }
            }
            System.out.println();
        }
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println("Occupied: " + getOccupiedCount()
                + " | Empty: " + getEmptySlots().size()
                + " | Total: " + (rows * columns));
    }

    // ─────────────────────────────────────────────
    // getEmptySlots
    // ─────────────────────────────────────────────

    /**
     * Returns a list of all empty slot positions as int[] {row, col}.
     *
     * @return list of [row, col] pairs that are currently null
     */
    public List<int[]> getEmptySlots() {
        List<int[]> empty = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (grid[r][c] == null) {
                    empty.add(new int[]{r, c});
                }
            }
        }
        return empty;
    }

    // ─────────────────────────────────────────────
    // getAllItems
    // ─────────────────────────────────────────────

    /**
     * Returns every non-null GridItem in the warehouse.
     *
     * @return list of all stored items
     */
    public List<GridItem> getAllItems() {
        List<GridItem> items = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (grid[r][c] != null) {
                    items.add(grid[r][c]);
                }
            }
        }
        return items;
    }

    // ─────────────────────────────────────────────
    // Utility helpers
    // ─────────────────────────────────────────────

    /** @return number of occupied (non-null) cells */
    public int getOccupiedCount() {
        return getAllItems().size();
    }

    /** @return occupancy as a percentage (0–100) */
    public double getOccupancyPercentage() {
        return (getOccupiedCount() * 100.0) / (rows * columns);
    }

    /** @return total quantity of all items combined */
    public int getTotalQuantity() {
        return getAllItems().stream().mapToInt(GridItem::getQuantity).sum();
    }

    /**
     * Validates that (row, col) is within grid bounds.
     *
     * @param row    row index
     * @param column column index
     * @return true if valid
     */
    private boolean isValidPosition(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    /**
     * Silent duplicate-check helper – does NOT print anything.
     * Used internally by addItem() to avoid noisy "NOT FOUND" output.
     *
     * @param itemId the ID to check
     * @return the GridItem if found, null otherwise
     */
    private GridItem findItemSilent(String itemId) {
        if (itemId == null || itemId.trim().isEmpty()) return null;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (grid[r][c] != null && grid[r][c].getItemId().equalsIgnoreCase(itemId.trim())) {
                    return grid[r][c];
                }
            }
        }
        return null;
    }
}
