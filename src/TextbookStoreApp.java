import java.io.*;
import java.util.*;

class Textbook implements Serializable {
    private int sku;
    private String title;
    private double price;
    private int quantity;

    public Textbook(int sku, String title, double price, int quantity) {
        this.sku = sku;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and setters

    public int getSku() {
        return sku;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "SKU: " + sku + ", Title: " + title + ", Price: $" + price + ", Quantity: " + quantity;
    }
}

class InventoryManager implements Serializable {
    private List<Textbook> inventory;

    public InventoryManager() {
        this.inventory = new ArrayList<>();
    }

    public void addTextbook(Textbook textbook) {
        inventory.add(textbook);
    }

    public void removeTextbook(int sku) {
        inventory.removeIf(t -> t.getSku() == sku);
    }

    public Textbook getTextbook(int sku) {
        return inventory.stream().filter(t -> t.getSku() == sku).findFirst().orElse(null);
    }

    public List<Textbook> getInventory() {
        return inventory;
    }
}

public class TextbookStoreApp {
    private static final String FILE_PATH = "inventory.ser";

    public static void main(String[] args) {
        InventoryManager inventoryManager = loadInventory();

        Scanner scanner = new Scanner(System.in);

        int choice;
        do {
            displayMenu();
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addTextbook(scanner, inventoryManager);
                    break;
                case 2:
                    removeTextbook(scanner, inventoryManager);
                    break;
                case 3:
                    displayTextbook(scanner, inventoryManager);
                    break;
                case 4:
                    displayInventory(inventoryManager);
                    break;
                case 5:
                    saveInventory(inventoryManager);
                    break;
                case 0:
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Add a textbook to the inventory");
        System.out.println("2. Remove a textbook from the inventory");
        System.out.println("3. Display the information for a textbook");
        System.out.println("4. Display the inventory");
        System.out.println("5. Save the inventory to file");
        System.out.println("0. Exit");
    }

    private static void addTextbook(Scanner scanner, InventoryManager inventoryManager) {
        System.out.print("Enter SKU: ");
        int sku = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter price: $");
        double price = scanner.nextDouble();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();

        Textbook textbook = new Textbook(sku, title, price, quantity);
        inventoryManager.addTextbook(textbook);

        System.out.println("Textbook added to the inventory.");
    }

    private static void removeTextbook(Scanner scanner, InventoryManager inventoryManager) {
        System.out.print("Enter SKU to remove: ");
        int sku = scanner.nextInt();
        Textbook removedTextbook = inventoryManager.getTextbook(sku);

        if (removedTextbook != null) {
            inventoryManager.removeTextbook(sku);
            System.out.println("Textbook removed from the inventory: " + removedTextbook);
        } else {
            System.out.println("Textbook with SKU " + sku + " not found in the inventory.");
        }
    }

    private static void displayTextbook(Scanner scanner, InventoryManager inventoryManager) {
        System.out.print("Enter SKU to display: ");
        int sku = scanner.nextInt();
        Textbook textbook = inventoryManager.getTextbook(sku);

        if (textbook != null) {
            System.out.println("Textbook information: " + textbook);
        } else {
            System.out.println("Textbook with SKU " + sku + " not found in the inventory.");
        }
    }

    private static void displayInventory(InventoryManager inventoryManager) {
        List<Textbook> inventory = inventoryManager.getInventory();

        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("Inventory:");
            for (Textbook textbook : inventory) {
                System.out.println(textbook);
            }
        }
    }

    private static InventoryManager loadInventory() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (InventoryManager) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No existing inventory file found. Starting with an empty inventory.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new InventoryManager();
    }

    private static void saveInventory(InventoryManager inventoryManager) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(inventoryManager);
            System.out.println("Inventory saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
