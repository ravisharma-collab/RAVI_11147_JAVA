public class Warehouse {

    private GridItem[][] grid;
    private int rows;
    private int cols;

    public Warehouse(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new GridItem[rows][cols];
    }

    public void addItem(int row, int col, GridItem item) {

        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            grid[row][col] = item;
        } else {
            System.out.println("Invalid Position!");
        }
    }

    public void displayGrid() {

        System.out.println("\n===== WAREHOUSE GRID =====");

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < cols; j++) {

                if (grid[i][j] != null) {
                    System.out.print("[" + grid[i][j].getItemId() + "]\t");
                } else {
                    System.out.print("[EMPTY]\t");
                }
            }

            System.out.println();
        }
    }

    public void searchItem(String itemId) {

        boolean found = false;

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < cols; j++) {

                if (grid[i][j] != null &&
                        grid[i][j].getItemId().equalsIgnoreCase(itemId)) {

                    System.out.println("\nItem Found!");
                    System.out.println("Item ID: " + grid[i][j].getItemId());
                    System.out.println("Item Name: " + grid[i][j].getItemName());
                    System.out.println("Quantity: " + grid[i][j].getQuantity());
                    System.out.println("Location -> Row: "
                            + i + ", Column: " + j);

                    found = true;
                    return;
                }
            }
        }

        if (!found) {
            System.out.println("\nItem not found in warehouse.");
        }
    }

    public void removeItem(String itemId) {

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < cols; j++) {

                if (grid[i][j] != null &&
                        grid[i][j].getItemId().equalsIgnoreCase(itemId)) {

                    grid[i][j] = null;

                    System.out.println(
                            "Item removed successfully."
                    );

                    return;
                }
            }
        }

        System.out.println("Item not found.");
    }

    public void updateQuantity(String itemId, int newQty) {

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < cols; j++) {

                if (grid[i][j] != null &&
                        grid[i][j].getItemId().equalsIgnoreCase(itemId)) {

                    grid[i][j].setQuantity(newQty);

                    System.out.println(
                            "Quantity updated successfully."
                    );

                    return;
                }
            }
        }

        System.out.println("Item not found.");
    }
}