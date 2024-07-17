// Homework 3: Online Sale System
// Course: CIS357
// Due date: July 17, 2024
// Name: Samuel Karas
// Instructor: Il-Hyung Cho
// Program description: A Java program that emulates an online shopping system.

import java.io.*;
import java.util.*;

/**
 * The OnlineSale class represents an online shopping system. It handles loading
 * items, processing sales, and calculating totals.
 */
public class OnlineSale {

	private static final double SALESTAX = 0.06;
	private static final int MAX_ITEMS_CSV = 15;
	private static Item[] items = new Item[MAX_ITEMS_CSV]; // Array of max item size of the csv file

	/**
	 * Main method is contained in OnlineSale class
	 */
	public static void main(String[] args) {
		loadItems();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to the Karas online sale system!");

		double totalSales = 0.0;

		// While loop starts the program command line loop and checks for validity on Y
		// or N input
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

		scanner.close(); // Close scanner when done
	}

	/**
	 * loadsItems method loads from a CSV file into the items array.
	 */
	private static void loadItems() {
		String fileName = "items.csv"; // File Name for Online Sale Items csv file
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
		} catch (IOException e) { // Catch IOException
			e.printStackTrace();
		}
	}

	/**
	 * This method processes a sale by asking the user for product codes and
	 * quantities. This method calculates the subtotal, total with tax, and prints
	 * the items list.
	 * 
	 * @param scanner The Scanner object that reads user input.
	 * @return The total amount for the sale including tax.
	 */
	private static double processSale(Scanner scanner) {
		double subtotal = 0.0;
		double subtotalTaxable = 0.0;
		String[] purchasedItems = new String[MAX_ITEMS_CSV];
		int index = 0;

		System.out.println("----------------------------");
		while (true) {
			System.out.print("Enter product code: ");
			String productCode = scanner.nextLine().trim();
			if (productCode.equals("-1"))
				break;

			Item item = findItem(productCode);
			if (item == null) {
				System.out.print("!!! Invalid product code\n");
				continue;
			}

			System.out.printf("         item name: %s\n", item.getItemName());

			int quantity = -1; // Initialize quantity with an invalid value to trigger prompt
			while (quantity < 0) {
				System.out.print("Enter quantity:     ");
				String quantityInput = scanner.nextLine().trim();
				try {
					quantity = Integer.parseInt(quantityInput);
					if (quantity <= 0)
						throw new NumberFormatException(); // Throw and catch exception when needed
				} catch (NumberFormatException e) {
					System.out.print("!!! Invalid quantity\n");
					quantity = -1; // Reset quantity to an invalid value for next prompt
				}
			}

			double itemTotal = item.getUnitPrice() * quantity;
			System.out.printf("      item total: $  %.2f\n", itemTotal); // Format the output

			purchasedItems[index++] = String.format("    %-1d %-17s    $%8.2f", quantity, item.getItemName(),
					itemTotal);

			// Check if item code contains "E" to determine if tax should apply
			if (item.getItemCode().contains("E")) {
				subtotal += itemTotal; // Item is not taxed
			} else {
				subtotalTaxable += itemTotal; // Item is taxed
			}
		}

		double totalWithTax = subtotalTaxable * (1 + SALESTAX) + subtotal; // Calculate the total with tax

		System.out.println("----------------------------");
		System.out.println("Items list:");
		for (int i = 0; i < index; i++) {
			System.out.println(purchasedItems[i]);
		}
		System.out.printf("%-26s $%8.2f\n", "Subtotal", subtotal + subtotalTaxable); // Format and print the outputs
		System.out.printf("%-20s $%8.2f\n", "Total with Tax (6%)       ", totalWithTax);
		System.out.println("----------------------------");

		return totalWithTax;
	}

	/**
	 * findItem method finds an item using its Item Code.
	 * 
	 * @param theCode The product code to search for.
	 * @return The Item object if found, or null if not found.
	 */
	private static Item findItem(String theCode) {
		for (int i = 0; i < items.length; i++) {
			Item item = items[i];
			if (item != null && item.getItemCode().equalsIgnoreCase(theCode)) {
				return item;
			}
		}
		return null;
	}
}

/**
 * The Item class represents a product with its code, name, description, and
 * unit price.
 */
class Item {
	private String itemCode;
	private String itemName;
	private String itemDescription;
	private double unitPrice;

	/**
	 * Item Constructor to initialize a new instance of the Item class with the
	 * provided value.
	 * 
	 * @param itemCode        The product code.
	 * @param itemName        The name of the item.
	 * @param itemDescription The description of the item.
	 * @param unitPrice       The unit price of the item.
	 */
	public Item(String itemCode, String itemName, String itemDescription, double unitPrice) {
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.unitPrice = unitPrice;
	}

	/**
	 * Gets the item code.
	 * 
	 * @return The item code.
	 */
	public String getItemCode() {
		return itemCode;
	}

	/**
	 * Gets the item name.
	 * 
	 * @return The item name.
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * Gets the item description.
	 * 
	 * @return The item description.
	 */
	public String getItemDescription() {
		return itemDescription;
	}

	/**
	 * Gets the unit price of the item.
	 * 
	 * @return The unit price.
	 */
	public double getUnitPrice() {
		return unitPrice;
	}
}