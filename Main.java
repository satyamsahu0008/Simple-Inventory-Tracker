import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

/**
 * Simple Inventory Tracker
 * DSA concepts: HashMap, ArrayList, Queue, HashSet, searching and sorting.
 * Compile: javac Main.java
 * Run:     java Main
 */
class Product {
    private final int id;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private int reorderLevel;

    public Product(int id, String name, String category, double price,
                   int quantity, int reorderLevel) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public int getReorderLevel() { return reorderLevel; }

    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setReorderLevel(int reorderLevel) { this.reorderLevel = reorderLevel; }

    public boolean isLowStock() {
        return quantity <= reorderLevel;
    }

    @Override
    public String toString() {
        return String.format(
            "ID: %d | Name: %s | Category: %s | Price: Rs. %.2f | Quantity: %d | Reorder Level: %d%s",
            id, name, category, price, quantity, reorderLevel,
            isLowStock() ? " | LOW STOCK" : ""
        );
    }
}

class InventoryManager {
    // Hashing: product ID is the key, Product object is the value.
    private final Map<Integer, Product> products = new HashMap<>();

    // Queue: product IDs waiting for restocking.
    private final Queue<Integer> restockQueue = new LinkedList<>();

    // Prevents the same product ID from being added to the queue more than once.
    private final Set<Integer> queuedProductIds = new HashSet<>();

    public boolean addProduct(Product product) {
        if (products.containsKey(product.getId())) {
            return false;
        }
        products.put(product.getId(), product);
        updateRestockQueue(product);
        return true;
    }

    public void displayAllProducts() {
        if (products.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        List<Product> list = new ArrayList<>(products.values());
        list.sort(Comparator.comparingInt(Product::getId));
        displayProducts(list);
    }

    public Product searchProduct(int id) {
        return products.get(id); // Average O(1) lookup.
    }

    public boolean updateProduct(int id, String name, String category,
                                 double price, int reorderLevel) {
        Product product = products.get(id);
        if (product == null) {
            return false;
        }

        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setReorderLevel(reorderLevel);
        updateRestockQueue(product);
        return true;
    }

    public boolean deleteProduct(int id) {
        Product removed = products.remove(id);
        if (removed == null) {
            return false;
        }
        removeFromRestockQueue(id);
        return true;
    }

    public boolean stockIn(int id, int amount) {
        Product product = products.get(id);
        if (product == null || amount <= 0) {
            return false;
        }

        product.setQuantity(product.getQuantity() + amount);
        updateRestockQueue(product);
        return true;
    }

    public boolean stockOut(int id, int amount) {
        Product product = products.get(id);
        if (product == null || amount <= 0 || amount > product.getQuantity()) {
            return false;
        }

        product.setQuantity(product.getQuantity() - amount);
        updateRestockQueue(product);
        return true;
    }

    public void displayLowStockProducts() {
        List<Product> lowStock = new ArrayList<>();
        for (Product product : products.values()) {
            if (product.isLowStock()) {
                lowStock.add(product);
            }
        }

        if (lowStock.isEmpty()) {
            System.out.println("No low-stock products.");
            return;
        }

        lowStock.sort(Comparator.comparingInt(Product::getQuantity));
        displayProducts(lowStock);
    }

    public void sortByPrice() {
        List<Product> list = new ArrayList<>(products.values());
        list.sort(Comparator.comparingDouble(Product::getPrice));
        displayProducts(list);
    }

    public void sortByName() {
        List<Product> list = new ArrayList<>(products.values());
        list.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
        displayProducts(list);
    }

    public void sortByQuantity() {
        List<Product> list = new ArrayList<>(products.values());
        list.sort(Comparator.comparingInt(Product::getQuantity));
        displayProducts(list);
    }

    public void displayRestockQueue() {
        if (restockQueue.isEmpty()) {
            System.out.println("Restock queue is empty.");
            return;
        }

        System.out.println("Products waiting for restocking (FIFO order):");
        for (Integer id : restockQueue) {
            Product product = products.get(id);
            if (product != null) {
                System.out.println(product);
            }
        }
    }

    public boolean processNextRestock(int amount) {
        if (amount <= 0 || restockQueue.isEmpty()) {
            return false;
        }

        // poll() removes the oldest pending request (FIFO).
        Integer nextId = restockQueue.poll();
        queuedProductIds.remove(nextId);
        Product product = products.get(nextId);
        if (product == null) {
            return false;
        }

        product.setQuantity(product.getQuantity() + amount);
        // Re-enqueue it only if its quantity is still at/below the reorder level.
        updateRestockQueue(product);
        return true;
    }

    private void updateRestockQueue(Product product) {
        if (product.isLowStock()) {
            if (queuedProductIds.add(product.getId())) {
                restockQueue.offer(product.getId());
            }
        } else {
            removeFromRestockQueue(product.getId());
        }
    }

    private void removeFromRestockQueue(int id) {
        queuedProductIds.remove(id);
        restockQueue.remove(Integer.valueOf(id));
    }

    private void displayProducts(List<Product> list) {
        if (list.isEmpty()) {
            System.out.println("No products to display.");
            return;
        }
        for (Product product : list) {
            System.out.println(product);
        }
    }
}

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final InventoryManager INVENTORY = new InventoryManager();

    public static void main(String[] args) {
        boolean running = true;
        System.out.println("===== SIMPLE INVENTORY TRACKER =====");

        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1: addProduct(); break;
                case 2: INVENTORY.displayAllProducts(); break;
                case 3: searchProduct(); break;
                case 4: updateProduct(); break;
                case 5: deleteProduct(); break;
                case 6: changeStock(true); break;
                case 7: changeStock(false); break;
                case 8: INVENTORY.displayLowStockProducts(); break;
                case 9: INVENTORY.sortByPrice(); break;
                case 10: INVENTORY.sortByName(); break;
                case 11: INVENTORY.sortByQuantity(); break;
                case 12: INVENTORY.displayRestockQueue(); break;
                case 13: processNextRestock(); break;
                case 0:
                    running = false;
                    System.out.println("Thank you for using Simple Inventory Tracker.");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a menu option.");
            }
            System.out.println();
        }
        SCANNER.close();
    }

    private static void printMenu() {
        System.out.println("\n--------------- MENU ---------------");
        System.out.println("1. Add Product");
        System.out.println("2. View All Products");
        System.out.println("3. Search Product by ID");
        System.out.println("4. Update Product Details");
        System.out.println("5. Delete Product");
        System.out.println("6. Stock In (Add Quantity)");
        System.out.println("7. Stock Out (Sell/Remove Quantity)");
        System.out.println("8. Show Low-Stock Products");
        System.out.println("9. Sort Products by Price");
        System.out.println("10. Sort Products by Name");
        System.out.println("11. Sort Products by Quantity");
        System.out.println("12. View Restock Queue");
        System.out.println("13. Process Next Restock Request (FIFO)");
        System.out.println("0. Exit");
        System.out.println("------------------------------------");
    }

    private static void addProduct() {
        int id = readInt("Product ID: ");
        String name = readNonEmptyString("Product name: ");
        String category = readNonEmptyString("Category: ");
        double price = readDouble("Price: ");
        int quantity = readInt("Available quantity: ");
        int reorderLevel = readInt("Reorder level: ");

        if (price < 0 || quantity < 0 || reorderLevel < 0) {
            System.out.println("Price, quantity and reorder level cannot be negative.");
            return;
        }

        Product product = new Product(id, name, category, price, quantity, reorderLevel);
        if (INVENTORY.addProduct(product)) {
            System.out.println("Product added successfully.");
        } else {
            System.out.println("A product with this ID already exists.");
        }
    }

    private static void searchProduct() {
        int id = readInt("Enter product ID to search: ");
        Product product = INVENTORY.searchProduct(id);
        if (product == null) {
            System.out.println("Product not found.");
        } else {
            System.out.println(product);
        }
    }

    private static void updateProduct() {
        int id = readInt("Enter product ID to update: ");
        String name = readNonEmptyString("New product name: ");
        String category = readNonEmptyString("New category: ");
        double price = readDouble("New price: ");
        int reorderLevel = readInt("New reorder level: ");

        if (price < 0 || reorderLevel < 0) {
            System.out.println("Price and reorder level cannot be negative.");
            return;
        }

        if (INVENTORY.updateProduct(id, name, category, price, reorderLevel)) {
            System.out.println("Product details updated successfully.");
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void deleteProduct() {
        int id = readInt("Enter product ID to delete: ");
        if (INVENTORY.deleteProduct(id)) {
            System.out.println("Product deleted successfully.");
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void processNextRestock() {
        int amount = readInt("Enter quantity to add to the next restock request: ");
        if (INVENTORY.processNextRestock(amount)) {
            System.out.println("Next restock request processed successfully.");
        } else {
            System.out.println("Could not process request. Check that the queue is not empty and quantity is positive.");
        }
    }

    private static void changeStock(boolean isStockIn) {
        int id = readInt("Enter product ID: ");
        int amount = readInt("Enter quantity: ");

        boolean updated = isStockIn
            ? INVENTORY.stockIn(id, amount)
            : INVENTORY.stockOut(id, amount);

        if (updated) {
            System.out.println(isStockIn
                ? "Stock added successfully."
                : "Stock reduced successfully.");
        } else {
            System.out.println("Operation failed. Check the product ID and quantity; stock-out cannot exceed available stock.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("This field cannot be empty.");
        }
    }
}
