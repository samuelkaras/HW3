


// Homework 3: Online Sale System
// Course: CIS357
// Due date: July 17, 2024
// Name: [Your Name]
// Instructor: Il-Hyung Cho
// Program description: A Java program that emulates an online shopping system.

import java.io.*;
import java.util.*;

/** Online Sale Class contains    all taken from items.csv */
public class OnlineSale {

    private static final double SALESTAX = 0.06;
    private static final int MAX_NUM_ITEMS = 15;
    private static Item[] items = new Item[MAX_NUM_ITEMS];

    public static void main(String[] args) {
        loadItems();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Karas online sale system!");
        
        double totalSales = 0.0;

        while (true) {
        	System.out.println("");
            System.out.print("Beginning a new sale (Y/N) ");
            String startNewSale = scanner.nextLine().trim().toLowerCase();
            if (startNewSale.equals("n")) break;

            double saleTotal = processSale(scanner);
            totalSales += saleTotal;
        }

        System.out.println("");
        System.out.printf("The total sale for the day is  $  %.2f\n", totalSales);
        System.out.println("");
        System.out.println("Thanks for using Karas's online sale system. Goodbye!");
        scanner.close();
    }

    private static void loadItems() {
        String filePath = "items.csv"; // Adjust the path as needed
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int index = 0;
            // Skip the header line
            br.readLine();
            while ((line = br.readLine()) != null && index < MAX_NUM_ITEMS) {
                String[] values = line.split(",");
                // Trim whitespace from each value and parse unit price to double
                String itemCode = values[0].trim();
                String itemName = values[1].trim();
                String itemDescription = values[2].trim();
                double unitPrice = Double.parseDouble(values[3].trim());

                // Create a new Item object and add it to the items array
                items[index++] = new Item(itemCode, itemName, itemDescription, unitPrice);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double processSale(Scanner scanner) {
        double subtotal = 0.0;
        String[] purchasedItems = new String[MAX_NUM_ITEMS];
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

    private static Item findItemByCode(String code) {
        for (Item item : items) {
            if (item != null && item.getItemCode().equalsIgnoreCase(code)) {
                return item;
            }
        }
        return null;
    }
}

/** Item Class contains itemCode,itemName,itemDesciption, and unitPrice all taken from items.csv */
class Item {
 private String itemCode;
 private String itemName;
 private String itemDescription;
 private double unitPrice;

 /** Item Constructor to initialize new instance of the "Item" class with the provided values */
 public Item(String itemCode, String itemName, String itemDescription, double unitPrice) {
     this.itemCode = itemCode;
     this.itemName = itemName;
     this.itemDescription = itemDescription;
     this.unitPrice = unitPrice;
 }

 /* Get methods */
 public String getItemCode() {
     return itemCode;
 }

 public String getItemName() {
     return itemName;
 }

 public String getItemDescription() {
     return itemDescription;
 }

 public double getUnitPrice() {
     return unitPrice;
 }
}