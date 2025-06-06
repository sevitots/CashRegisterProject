import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Fitem {
    public String name;
    public double price;
    public int quantity;

    public Fitem(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static double getTotalPrice() {
        double total = 0;
        for (Fitem item : cashregister.cart) {
            total += item.price * item.quantity;
        }
        return total;
    }
}

public class cashregister {
    static ArrayList<Fitem> menu = new ArrayList<>();
    static ArrayList<Fitem> cart = new ArrayList<>();
    static Scanner scan = new Scanner(System.in);
    static String CU = "";

    public static void main(String[] args) {
        String savedUsername = "";
        String savedPassword = "";
        initializeMenu();
        boolean sev = true;

        System.out.println("=== SIGN UP ===");
        System.out.print("Create a username: ");
        String um = scan.nextLine();
        if (!um.matches("[a-zA-Z0-9_]{3,10}")) {
            System.out.println("Invalid username. Must include letters, numbers, underscores (3-10 characters).");
            return;
        }

        System.out.print("Create a password: ");
        String password = scan.nextLine();
        if (!password.matches("[a-zA-Z0-9_]{5,15}")) {
            System.out.println("Invalid password. Must include letters, numbers, underscores (5-15 characters).");
            return;
        }

        savedUsername = um;
        savedPassword = password;
        System.out.println("Sign up successful.\n");

        System.out.println("=== Login ===");
        System.out.print("Enter username: ");
        String um2 = scan.nextLine();
        System.out.print("Enter password: ");
        String password2 = scan.nextLine();

        if (!um2.equals(savedUsername) || !password2.equals(savedPassword)) {
            System.out.println("Login failed. Incorrect username or password.");
            return;
        }

        System.out.println("\nLogin successful, " + um + "!!!");
        CU = um2;

        while (sev) {
            System.out.println("\nDiwata Kainan");
            System.out.println("1 - Show Menu");
            System.out.println("2 - Add to cart");
            System.out.println("3 - Show Cart");
            System.out.println("4 - Show Total");
            System.out.println("5 - Accept Payment");
            System.out.println("6 - Remove Item");
            System.out.println("7 - Update Item Quantity");
            System.out.println("8 - Exit");
            System.out.print("Select an option: ");
            int option = scan.nextInt();

            switch (option) {
                case 1: displayMenu(); break;
                case 2: addtoCart(); break;
                case 3: displayCart(); break;
                case 4: displayTotal(); break;
                case 5: displayPayment(); break;
                case 6: removeItem(); break;
                case 7: updateItemQuantity(); break;
                case 8: sev = false; break;
                default: System.out.println("Invalid option, please try again.");
            }
        }

        System.out.println("Thank you ordering at Kainan ni Diwata.");
    }

    public static void initializeMenu() {
        menu.add(new Fitem("Crispy Pata", 150, 0));
        menu.add(new Fitem("Burger", 80, 0));
        menu.add(new Fitem("Fries", 60, 0));
        menu.add(new Fitem("Sisig", 90, 0));
        menu.add(new Fitem("Coke", 20, 0));
    }

    public static void displayMenu() {
        System.out.println("\nFood Menu:");
        for (int i = 0; i < menu.size(); i++) {
            System.out.println((i + 1) + ". " + menu.get(i).name + " - Php " + menu.get(i).price);
        }
    }

    public static void addtoCart() {
        displayMenu();
        System.out.print("Enter the number of the food item to add: ");
        int option = scan.nextInt();

        if (option > 0 && option <= menu.size()) {
            System.out.print("Enter quantity: ");
            int quantity = scan.nextInt();
            scan.nextLine();

            Fitem selectedItem = menu.get(option - 1);
            cart.add(new Fitem(selectedItem.name, selectedItem.price, quantity));
            System.out.println(quantity + "x " + selectedItem.name + " added to cart.");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    public static void displayCart() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("\nYour Cart:");
            for (Fitem item : cart) {
                System.out.println(item.quantity + "x " + item.name + " - Php " + (item.price * item.quantity));
            }
        }
    }

    public static void displayTotal() {
        double total = Fitem.getTotalPrice();
        System.out.println("Total Price: Php " + total);
    }

    public static void displayPayment() {
        displayTotal();
        System.out.print("Enter payment amount: ");
        double payment = scan.nextDouble();
        double total = Fitem.getTotalPrice();

        if (payment >= total) {
            System.out.println("Change: Php " + (payment - total));
            logTransaction(CU, new ArrayList<>(cart), total);
            cart.clear();
            System.out.println("Thank you for eating at Kainan ni Diwata, Payment Completed!");
            scan.nextLine();
            System.out.print("Would you like to make another order? (yes/no): ");
            String order = scan.nextLine();
            if (order.equalsIgnoreCase("no")) {
                System.out.println("Thank you for ordering at Kainan ni Diwata.");
                System.exit(0);
            }
        } else {
            System.out.println("Insufficient payment. Please try again.");
        }
    }

    public static void removeItem() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        scan.nextLine();
        System.out.print("Enter the name of the item to remove: ");
        String name = scan.nextLine();
        boolean removed = false;

        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).name.equalsIgnoreCase(name)) {
                cart.remove(i);
                removed = true;
                break;
            }
        }

        if (removed) {
            System.out.println("Item removed successfully!");
        } else {
            System.out.println("Item not found in the cart.");
        }
    }

    public static void updateItemQuantity() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        scan.nextLine();
        System.out.print("Enter the name of the item to be updated: ");
        String iname = scan.nextLine();

        for (Fitem item : cart) {
            if (item.name.equalsIgnoreCase(iname)) {
                System.out.print("Enter new quantity: ");
                try {
                    int newqty = scan.nextInt();
                    if (newqty <= 0) {
                        System.out.println("Invalid quantity.");
                        return;
                    }
                    item.quantity = newqty;
                    System.out.println("Quantity updated successfully.");
                    return;
                } catch (InputMismatchException ex) {
                    System.out.println("Invalid input. Please enter a number.");
                    scan.nextLine();
                    return;
                }
            }
        }
        System.out.println("Item not found in cart.");
    }

    public static void logTransaction(String username, List<Fitem> items, double total) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("Date/Time : ").append(LocalDateTime.now().format(fmt)).append(System.lineSeparator());
        sb.append("Cashier : ").append(username).append(System.lineSeparator());
        sb.append("Items : ").append(System.lineSeparator());
        for (Fitem it : items) {
            sb.append(" - ").append(it.quantity).append(" x ").append(it.name)
                    .append(" = ").append(it.price * it.quantity).append(System.lineSeparator());
        }
        sb.append("TOTAL    : ").append(total).append(System.lineSeparator());
        sb.append("--------------------------------------------------").append(System.lineSeparator());

        try (FileWriter fw = new FileWriter("transaction.txt", true)) {
            fw.write(sb.toString());
        } catch (IOException e) {
            System.out.println("Unable to write to transaction.txt: " + e.getMessage());
        }
    }
}
