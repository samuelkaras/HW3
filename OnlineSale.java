// Homework 3: Online Sale System
// Course: CIS357
// Due date: July 17, 2024
// Name: Samuel Karas
// Instructor: Il-Hyung Cho
// Program description: A Java program that emulates an online shopping system.

import java.io.*;
import java.util.*;

/**
 * The OnlineSale class represents an online shopping system.
 * It handles loading items, processing sales, and calculating totals.
 */
public class OnlineSale {

    private static final double SALESTAX = 0.06;
    private static final int MAX_ITEMS_CSV = 15;
    private static Item[] items = new Item[MAX_ITEMS_CSV];

    /**
     * Main method is contained in OnlineSale class
     */
    public static void main(String[] args) {
        loadItems();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Karas online sale system!");
        
        double totalSales = 0.0;

        while (true) {
            System.out.println("");
            String startNewSale;
            while (true) {
                System.out.print("Beginning a new sale (Y/N) ");
                startNewSale = scanner.nextLine().trim().toLowerCase();
                if (startNewSale.equals("y") || startNewSale.equals("n")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter Y or N.");
                }
            }
            
            if (startNewSale.equals("n")) {
                break;
            }

            double saleTotal = processSale(scanner);
            totalSales += saleTotal;
        }

        System.out.println("");
        System.out.printf("The total sale for the day is  $  %.2f\n", totalSales);
        System.out.println("");
        System.out.println("Thanks for using Karas's online sale system. Goodbye!");
        scanner.close();
    }

    /**
     * Loads items from a CSV file into the items array.
     */
    private static void loadItems() {
        String fileName = "items.csv"; // File Name for Online Sale Items
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String currLine;
            int index = 0;
            // Skip the header line
            br.readLine();
            while ((currLine = br.readLine()) != null && index < MAX_ITEMS_CSV) {
                String[] item = currLine.split(",");
                
                // Trim the whitespace from each value and parse unit price to double
                String itemCode = item[0].trim();
                String itemName = item[1].trim();
                String itemDescription = item[2].trim();
                double unitPrice = Double.parseDouble(item[3].trim());

                // Create a new Item object and add it to the items array
                items[index++] = new Item(itemCode, itemName, itemDescription, unitPrice);
            }
        } catch (IOException e) {	//Catch IOException
            e.printStackTrace();
        }
    }

    /**
     * Processes a sale by prompting the user for product codes and quantities.
     * Calculates the subtotal and total with tax, and prints the items list.
     * 
     * @param scanner The Scanner object for reading user input.
     * @return The total amount for the sale including tax.
     */
    private static double processSale(Scanner scanner) {
        double subtotal = 0.0;
        String[] purchasedItems = new String[MAX_ITEMS_CSV];
        int index = 0;
        boolean containsE = false;

        System.out.println("----------------------------");
        while (true) {
            System.out.print("Enter product code: ");
            String productCode = scanner.nextLine().trim();
            if (productCode.equals("-1")) break;

            Item item = findItemByCode(productCode);
            if (item == null) {
                System.out.print("!!! Invalid product code\n");
                continue;
            }

            System.out.printf("         item name: %s\n", item.getItemName());

            int quantity = -1;  // Initialize quantity with an invalid value
            while (quantity < 0) {
                System.out.print("Enter quantity:     ");
                String quantityInput = scanner.nextLine().trim();
                try {
                    quantity = Integer.parseInt(quantityInput);
                    if (quantity <= 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    System.out.print("!!! Invalid quantity\n");
                    quantity = -1;  // Reset quantity to an invalid value
                }
            }

            double itemTotal = item.getUnitPrice() * quantity;
            System.out.printf("      item total: $  %.2f\n", itemTotal);

            if (item.getItemCode().contains("E")) {
                containsE = true;
            }
            
            purchasedItems[index++] = String.format("    %-1d %-17s    $%8.2f", quantity, item.getItemName(), itemTotal);
            subtotal += itemTotal;
        }

        double totalWithTax = subtotal;
        System.out.println("----------------------------");
        System.out.println("Items list:");
        for (int i = 0; i < index; i++) {
            System.out.println(purchasedItems[i]);
        }
        System.out.printf("%-26s $%8.2f\n", "Subtotal", subtotal);
        if (!containsE) {
            totalWithTax = subtotal * (1 + SALESTAX);
        }
        System.out.printf("%-20s $%8.2f\n", "Total with Tax (6%)       ", totalWithTax);
        System.out.println("----------------------------");

        return totalWithTax;
    }

    /**
     * Finds an item by its code.
     * 
     * @param code The product code to search for.
     * @return The Item object if found, or null if not found.
     */
    private static Item findItemByCode(String code) {
        for (Item item : items) {
            if (item != null && item.getItemCode().equalsIgnoreCase(code)) {
                return item;
            }
        }
        return null;
    }
}

/**
 * The Item class represents a product with its code, name, description, and unit price.
 */
class Item {
    private String itemCode;
    private String itemName;
    private String itemDescription;
    private double unitPrice;

    /**
     * Item Constructor to initialize a new instance of the Item class with the provided value.
     * 
     * @param itemCode The product code.
     * @param itemName The name of the product.
     * @param itemDescription The description of the product.
     * @param unitPrice The unit price of the product.
     */
    public Item(String itemCode, String itemName, String itemDescription, double unitPrice) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.unitPrice = unitPrice;
    }

    /**
     * Gets the product code.
     * @return The product code.
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * Gets the product name.
     * @return The product name.
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Gets the product description.
     * @return The product description.
     */
    public String getItemDescription() {
        return itemDescription;
    }

    /**
     * Gets the unit price of the product.
     * @return The unit price.
     */
    public double getUnitPrice() {
        return unitPrice;
    }
}