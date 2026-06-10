/**
 * GridItem.java
 * Represents a single item stored in the warehouse grid.
 * Demonstrates: Encapsulation, Data Hiding, Constructors, Getters/Setters
 *
 * @author  Smart Warehouse Project
 * @version 1.0
 */
public class GridItem {

    // ─────────────────────────────────────────────
    // Private attributes – Data Hiding
    // ─────────────────────────────────────────────
    private String itemId;
    private String itemName;
    private int    quantity;

    // ─────────────────────────────────────────────
    // Constructors  (Method Overloading)
    // ─────────────────────────────────────────────

    /**
     * Default constructor – creates an empty / placeholder GridItem.
     */
    public GridItem() {
        this.itemId   = "";
        this.itemName = "";
        this.quantity = 0;
    }

    /**
     * Parameterized constructor – creates a fully populated GridItem.
     *
     * @param itemId   Unique identifier for the item (e.g. "I101")
     * @param itemName Descriptive name of the item   (e.g. "Laptop")
     * @param quantity Number of units stored (must be >= 0)
     */
    public GridItem(String itemId, String itemName, int quantity) {
        setItemId(itemId);
        setItemName(itemName);
        setQuantity(quantity);
    }

    // ─────────────────────────────────────────────
    // Getters  (Encapsulation – controlled read)
    // ─────────────────────────────────────────────

    /** @return the unique item ID */
    public String getItemId()   { return itemId;   }

    /** @return the item name */
    public String getItemName() { return itemName; }

    /** @return current stock quantity */
    public int    getQuantity() { return quantity; }

    // ─────────────────────────────────────────────
    // Setters  (Encapsulation – controlled write)
    // ─────────────────────────────────────────────

    /**
     * Sets the item ID after validation.
     * @param itemId must be non-null and non-empty
     */
    public void setItemId(String itemId) {
        if (itemId == null || itemId.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be empty.");
        }
        this.itemId = itemId.trim();
    }

    /**
     * Sets the item name after validation.
     * @param itemName must be non-null and non-empty
     */
    public void setItemName(String itemName) {
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty.");
        }
        this.itemName = itemName.trim();
    }

    /**
     * Sets the quantity after validation.
     * @param quantity must be >= 0
     */
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    // ─────────────────────────────────────────────
    // toString – human-readable representation
    // ─────────────────────────────────────────────

    @Override
    public String toString() {
        return "GridItem { " +
               "itemId='"     + itemId   + '\'' +
               ", itemName='" + itemName + '\'' +
               ", quantity="  + quantity +
               " }";
    }
}
