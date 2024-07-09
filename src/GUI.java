import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;

public class GUI {

	// Name of user logged in
	public static String welcomeName;
	public static String welcomeUsername;
	public static Address welcomeAddress;

	// Method to put all users from the txt file in an ArrayList
	public static ArrayList<User> addUsers() {
		ArrayList<User> users = new ArrayList<>();

		try {
			File userData = new File("UserAccounts");
			Scanner sc = new Scanner(userData);

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] info = line.split(",");

				int userID = Integer.parseInt(info[0].trim());
				String username = info[1].trim();
				String name = info[2].trim();
				int houseNum = Integer.parseInt(info[3].trim());
				String postcode = info[4].trim();
				String city = info[5].trim();
				Address address = new Address(houseNum, postcode, city);
				UserRole role = info[6].trim().equalsIgnoreCase("admin") ? UserRole.ADMIN : UserRole.CUSTOMER;
				User user = new User(userID, username, name, address, role);
				users.add(user);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		}

		return users;
	}

	public static void main(String[] args) {

		// Users ArrayList (Stores all users)
		ArrayList<User> users = addUsers();

		JFrame userPage = new JFrame("User Select");
		userPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		userPage.setLayout(new BorderLayout());

		JPanel title = new JPanel();
		title.setPreferredSize(new Dimension(300, 300));
		title.setLayout(new BorderLayout());
		JLabel label = new JLabel("Please Select Your User ID");
		label.setFont(new Font("Arial", Font.BOLD, 30));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		title.add(label, BorderLayout.CENTER);
		userPage.add(title, BorderLayout.NORTH);

		// Creates table of users with these column names
		String[] columns = { "Role", "Name", "Username", "User ID", "Address" };
		DefaultTableModel userTable = new DefaultTableModel(columns, 0) {

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Gets the information of each user in the ArrayList as an array of string and
		// adds it to the table as a row
		for (User u : users) {
			String[] data = { u.getRole().toString(), u.getName(), u.getUsername(), String.valueOf(u.getUserID()),
					u.getAddress().getCity() + " - " + u.getAddress().getHouseNumber() + " - "
							+ u.getAddress().getPostcode() };
			userTable.addRow(data);
		}

		JPanel tablePanel = new JPanel();
		tablePanel.setPreferredSize(new Dimension(userPage.getWidth() - 100, 300));

		JTable table = new JTable(userTable);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(50);
		table.getColumnModel().getColumn(0).setPreferredWidth(200);
		table.setPreferredScrollableViewportSize(new Dimension(1000, 200));
		JScrollPane scrollPane = new JScrollPane(table);

		tablePanel.add(scrollPane);

		userPage.add(tablePanel, BorderLayout.CENTER);

		userPage.setSize(1500, 1000);
		userPage.setLocationRelativeTo(null);
		userPage.setVisible(true);

		// Enters the users page and gets their name and username based on the row
		// clicked
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					String role = userTable.getValueAt(row, 0).toString();
					welcomeName = userTable.getValueAt(row, 1).toString();
					welcomeUsername = userTable.getValueAt(row, 2).toString();
					for (User u : users) {
						if (u.getUsername().equalsIgnoreCase(welcomeUsername)) {
							welcomeAddress = u.getAddress();
							break;
						}
					}

					// Opens admin page or customer page depending on user clicked
					if (role.equalsIgnoreCase("admin")) {
						openAdminPage();
					} else if (role.equalsIgnoreCase("customer")) {
						openCustomerPage();
					}
				}
			}
		});
	}

	public static void openAdminPage() {
		JFrame adminPage = new JFrame("Admin Page");
		adminPage.setSize(1500, 1000);
		adminPage.setLayout(new BoxLayout(adminPage.getContentPane(), BoxLayout.Y_AXIS));

		// ArrayList of all products in stock
		ArrayList<Product> products = new ArrayList<>();

		try {
			File stockData = new File("Stock");
			Scanner sc = new Scanner(stockData);

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] info = line.split(",");
				// Gets all information from Stock txt file and creates a product object to put
				// in the products ArrayList
				int barcode = Integer.parseInt(info[0].trim());
				ProductCategory category = info[1].trim().equalsIgnoreCase("mouse") ? ProductCategory.MOUSE
						: ProductCategory.KEYBOARD;
				String brand = info[3].trim();
				String color = info[4].trim();
				ConnectivityType connectivity = info[5].trim().equalsIgnoreCase("wired") ? ConnectivityType.WIRED
						: ConnectivityType.WIRELESS;
				int quantityInStock = Integer.parseInt(info[6].trim());
				double originalCost = Double.parseDouble(info[7].trim());
				double retailPrice = Double.parseDouble(info[8].trim());

				if (info[1].trim().equalsIgnoreCase("mouse")) {
					if (info[2].trim().equalsIgnoreCase("standard")) {
						MouseTypes mouseType = MouseTypes.STANDARD;
						int buttonNum = Integer.parseInt(info[9].trim());
						Mouse mouse = new Mouse(barcode, brand, color, connectivity, quantityInStock, originalCost,
								retailPrice, category, buttonNum, mouseType);
						products.add(mouse);
					} else if (info[2].trim().equalsIgnoreCase("gaming")) {
						MouseTypes mouseType = MouseTypes.GAMING;
						int buttonNum = Integer.parseInt(info[9].trim());
						Mouse mouse = new Mouse(barcode, brand, color, connectivity, quantityInStock, originalCost,
								retailPrice, category, buttonNum, mouseType);
						products.add(mouse);
					}
				} else if (info[1].trim().equalsIgnoreCase("keyboard")) {
					if (info[2].trim().equalsIgnoreCase("flexible")) {
						Layout layout = info[9].trim().equalsIgnoreCase("uk") ? Layout.UK : Layout.US;
						KeyboardTypes keyboardtype = KeyboardTypes.FLEXIBLE;
						Keyboard keyboard = new Keyboard(barcode, brand, color, connectivity, quantityInStock,
								originalCost, retailPrice, category, layout, keyboardtype);
						products.add(keyboard);
					} else if (info[2].trim().equalsIgnoreCase("gaming")) {
						Layout layout = info[9].trim().equalsIgnoreCase("uk") ? Layout.UK : Layout.US;
						KeyboardTypes keyboardType = KeyboardTypes.GAMING;
						Keyboard keyboard = new Keyboard(barcode, brand, color, connectivity, quantityInStock,
								originalCost, retailPrice, category, layout, keyboardType);
						products.add(keyboard);
					} else if (info[2].trim().equalsIgnoreCase("standard")) {
						Layout layout = info[9].trim().equalsIgnoreCase("uk") ? Layout.UK : Layout.US;
						KeyboardTypes keyboardType = KeyboardTypes.STANDARD;
						Keyboard keyboard = new Keyboard(barcode, brand, color, connectivity, quantityInStock,
								originalCost, retailPrice, category, layout, keyboardType);
						products.add(keyboard);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		}

		JPanel itemContainer = new JPanel();
		itemContainer.setLayout(new GridLayout(2, 1));

		JPanel welcome = new JPanel();
		welcome.setLayout(new BoxLayout(welcome, BoxLayout.Y_AXIS));
		welcome.setPreferredSize(new Dimension(300, 100));
		JLabel message_1 = new JLabel("Logged In As: " + welcomeUsername);
		message_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		JLabel message_2 = new JLabel("Welcome, " + welcomeName);
		message_2.setAlignmentX(Component.CENTER_ALIGNMENT);
		message_1.setFont(new Font("Arial", Font.BOLD, 20));
		message_2.setFont(new Font("Arial", Font.BOLD, 20));
		welcome.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));
		welcome.add(message_1);
		welcome.add(message_2);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
		buttonPanel.setPreferredSize(new Dimension(400, 180));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

		JButton viewStock = new JButton("Click To View Stock");
		viewStock.setPreferredSize(new Dimension(175, 175));
		JButton addStock = new JButton("Click To Add Stock");
		addStock.setPreferredSize(new Dimension(175, 175));

		buttonPanel.add(viewStock);
		buttonPanel.add(addStock);

		itemContainer.add(welcome);
		itemContainer.add(buttonPanel);

		adminPage.add(itemContainer);

		JPanel stockPanel = new JPanel();
		stockPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		stockPanel.setPreferredSize(new Dimension(adminPage.getWidth() - 100, 375));

		// Table that displays the stock for the admin to see with all attributes
		String[] columnNames = { "Barcode", "Product Category", "Brand", "Type", "Connectivity", "Color",
				"Quantity In Stock", "Original Cost", "Retail Price", "Additional Information" };

		DefaultTableModel stockTable = new DefaultTableModel(columnNames, 0) {

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		JTable table = new JTable(stockTable);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(50);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(9).setPreferredWidth(150);
		table.setPreferredScrollableViewportSize(new Dimension(1200, 500));
		JScrollPane stockPane = new JScrollPane(table);
		stockPane.setPreferredSize(new Dimension(adminPage.getWidth() - 100, 300));

		stockPanel.add(stockPane);

		adminPage.add(stockPanel, BorderLayout.CENTER);

		stockPanel.setVisible(false);

		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		inputPanel.setLayout(new FlowLayout());

		viewStock.addActionListener(e -> {

			stockTable.setRowCount(0);

			inputPanel.setVisible(false);

			stockPanel.setVisible(true);

			// Sorting the products ArrayList depending on its retail price
			Collections.sort(products, Comparator.comparingDouble(Product::getRetailPrice));

			// Adds a row of all the information of the stock depending on if it is a mouse
			// or keyboard
			for (Product p : products) {
				if (String.valueOf(p.getCategory()).equalsIgnoreCase("mouse")) {
					Mouse m = (Mouse) p;
					String[] data = { String.valueOf(m.getBarcode()), m.getCategory().toString(), m.getBrand(),
							String.valueOf(m.getMouseType()), m.getConnectivity().toString(),
							m.getColor().substring(0, 1).toUpperCase() + m.getColor().substring(1),
							String.valueOf(m.getQuantityInStock()), "$ " + String.valueOf(m.getOriginalCost()),
							"$ " + String.valueOf(m.getRetailPrice()), "Buttons: " + String.valueOf(m.getButtonNum()) };
					stockTable.addRow(data);
				} else if (String.valueOf(p.getCategory()).equalsIgnoreCase("keyboard")) {
					Keyboard k = (Keyboard) p;
					String[] data = { String.valueOf(k.getBarcode()), k.getCategory().toString(), k.getBrand(),
							String.valueOf(k.getKeyboardType()), k.getConnectivity().toString(),
							k.getColor().substring(0, 1).toUpperCase() + k.getColor().substring(1),
							String.valueOf(k.getQuantityInStock()), "$ " + String.valueOf(k.getOriginalCost()),
							"$ " + String.valueOf(k.getRetailPrice()), "Layout: " + String.valueOf(k.getLayout()) };
					stockTable.addRow(data);
				}
			}
		});

		NumberFormat formating = NumberFormat.getIntegerInstance();
		formating.setGroupingUsed(false);
		NumberFormatter integerFormatter = new NumberFormatter(formating);
		integerFormatter.setValueClass(Integer.class);
		integerFormatter.setMinimum(0);
		integerFormatter.setAllowsInvalid(false);

		NumberFormat barcodeFormat = NumberFormat.getIntegerInstance();
		barcodeFormat.setGroupingUsed(false);
		NumberFormatter barcodeFormatter = new NumberFormatter(barcodeFormat);
		barcodeFormatter.setValueClass(Integer.class);
		barcodeFormatter.setMaximum(999999);
		barcodeFormatter.setAllowsInvalid(false);

		JPanel inputPanel_1 = new JPanel();
		inputPanel_1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));
		inputPanel_1.setLayout(new BoxLayout(inputPanel_1, BoxLayout.Y_AXIS));

		JLabel barcode = new JLabel("Barcode Of Product:");
		barcode.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 33));
		JFormattedTextField barcodeField = new JFormattedTextField(barcodeFormatter);
		inputPanel_1.add(barcode);
		inputPanel_1.add(barcodeField);
		inputPanel_1.add(Box.createRigidArea(new Dimension(0, 40)));

		JLabel category = new JLabel("Category Of Product:");
		category.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 35));
		JComboBox<String> categories = new JComboBox<>(new String[] { "Mouse", "Keyboard" });
		inputPanel_1.add(category);
		inputPanel_1.add(categories);
		inputPanel_1.add(Box.createRigidArea(new Dimension(0, 40)));

		JLabel brand = new JLabel("Brand Of Product:");
		brand.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 0));
		JTextField brandField = new JTextField(6);
		inputPanel_1.add(brand);
		inputPanel_1.add(brandField);
		inputPanel_1.add(Box.createRigidArea(new Dimension(0, 40)));

		JLabel color = new JLabel("Color Of Product:");
		color.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		JTextField colorField = new JTextField();
		inputPanel_1.add(color);
		inputPanel_1.add(colorField);
		inputPanel_1.add(Box.createRigidArea(new Dimension(0, 40)));

		JLabel type = new JLabel("Product Type:");
		type.setBorder(BorderFactory.createEmptyBorder(0, 13, 0, 0));
		JComboBox<String> types = new JComboBox<>(new String[] { "Standard", "Gaming", "Flexible" });
		inputPanel_1.add(type);
		inputPanel_1.add(types);
		inputPanel_1.add(Box.createRigidArea(new Dimension(0, 40)));

		JPanel inputPanel_2 = new JPanel();
		inputPanel_2.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
		inputPanel_2.setLayout(new BoxLayout(inputPanel_2, BoxLayout.Y_AXIS));

		JLabel quantity = new JLabel("Quantity In Stock:");
		quantity.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));
		JFormattedTextField quantityField = new JFormattedTextField(integerFormatter);
		inputPanel_2.add(quantity);
		inputPanel_2.add(quantityField);
		inputPanel_2.add(Box.createRigidArea(new Dimension(0, 40)));

		JLabel connectivity = new JLabel("Connectivity Of Product:");
		connectivity.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 35));
		JComboBox<String> connectivities = new JComboBox<>(new String[] { "Wired", "Wireless" });
		inputPanel_2.add(connectivity);
		inputPanel_2.add(connectivities);
		inputPanel_2.add(Box.createRigidArea(new Dimension(0, 40)));

		JLabel cost = new JLabel("Original Cost Of Product:");
		JTextField costField = new JTextField();
		inputPanel_2.add(cost);
		inputPanel_2.add(costField);
		inputPanel_2.add(Box.createRigidArea(new Dimension(0, 40)));

		JLabel price = new JLabel("Price Of Product:");
		price.setBorder(BorderFactory.createEmptyBorder(0, 19, 0, 0));
		JTextField priceField = new JTextField();
		inputPanel_2.add(price);
		inputPanel_2.add(priceField);
		inputPanel_2.add(Box.createRigidArea(new Dimension(0, 40)));

		JLabel info = new JLabel("Additional Information:");
		info.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		JTextField infos = new JTextField();
		inputPanel_2.add(info);
		inputPanel_2.add(infos);
		inputPanel_2.add(Box.createRigidArea(new Dimension(0, 40)));

		JButton submit = new JButton("Submit");
		submit.setPreferredSize(new Dimension(75, 75));

		inputPanel.add(inputPanel_1);
		inputPanel.add(submit);
		inputPanel.add(inputPanel_2);

		inputPanel.setVisible(false);

		adminPage.add(inputPanel);

		addStock.addActionListener(e -> {
			stockPanel.setVisible(false);

			inputPanel.setVisible(true);
		});

		// Button that handles adding the product to the stock by adding it to the
		// products ArrayList
		submit.addActionListener(e -> {
			if (!(barcodeField.getText().trim().isEmpty() || brandField.getText().trim().isEmpty()
					|| colorField.getText().trim().isEmpty() || infos.getText().trim().isEmpty()
					|| quantityField.getText().trim().isEmpty() || costField.getText().trim().isEmpty()
					|| priceField.getText().trim().isEmpty()) && (barcodeField.getText().trim().length() == 6)
					&& (isDouble(costField.getText())) && (isDouble(priceField.getText()))
					&& (Integer.parseInt(barcodeField.getText()) >= 100000)
					&& (Integer.parseInt(barcodeField.getText()) <= 999999)) {
				DecimalFormat df = new DecimalFormat("#.##");
				int barcodeInput = Integer.parseInt(barcodeField.getText().trim());
				String brandInput = brandField.getText().trim();
				String colorInput = colorField.getText().trim();
				String additionalInput = infos.getText().trim();
				int quantityInput = Integer.parseInt(quantityField.getText().trim());
				double costInput = Double.parseDouble(df.format(Double.parseDouble(costField.getText().trim())));
				double priceInput = Double.parseDouble(df.format(Double.parseDouble(priceField.getText().trim())));
				String connectivityInput = (String) connectivities.getSelectedItem();
				ConnectivityType inputConnectivity = connectivityInput.trim().equalsIgnoreCase("wired")
						? ConnectivityType.WIRED
						: ConnectivityType.WIRELESS;
				String categoryInput = (String) categories.getSelectedItem();
				ProductCategory inputCategory = categoryInput.trim().equalsIgnoreCase("mouse") ? ProductCategory.MOUSE
						: ProductCategory.KEYBOARD;
				String typeInput = (String) types.getSelectedItem();

				// Adding stock to the txt file depending on if the product is a mouse or
				// keyboard
				if (categoryInput.equalsIgnoreCase("mouse") && (isInteger(additionalInput.trim()))) {
					int numButton = Integer.parseInt(additionalInput);
					if (typeInput.equalsIgnoreCase("standard")) {
						MouseTypes inputType = MouseTypes.STANDARD;
						Mouse Mouse = new Mouse(barcodeInput, brandInput, colorInput, inputConnectivity, quantityInput,
								costInput, priceInput, inputCategory, numButton, inputType);
						productsCheck(Mouse, products);
						String str = Mouse.toString();
						if (barcodeCheck(String.valueOf(Mouse.getBarcode())) == false) {
							writeToStock(str);
						} else {
							JOptionPane.showMessageDialog(null, "Barcode Is Already In Stock", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} else if (typeInput.equalsIgnoreCase("gaming")) {
						MouseTypes inputType = MouseTypes.GAMING;
						Mouse Mouse = new Mouse(barcodeInput, brandInput, colorInput, inputConnectivity, quantityInput,
								costInput, priceInput, inputCategory, numButton, inputType);
						productsCheck(Mouse, products);
						String str = Mouse.toString();
						if (barcodeCheck(String.valueOf(Mouse.getBarcode())) == false) {
							writeToStock(str);
						} else {
							JOptionPane.showMessageDialog(null, "Barcode Is Already In Stock", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} else if (typeInput.equalsIgnoreCase("flexible")) {
						JOptionPane.showMessageDialog(null, "The Mouse Can Not Be of Type Flexible", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (categoryInput.equalsIgnoreCase("mouse") && !(isInteger(additionalInput.trim()))) {
					JOptionPane.showMessageDialog(null, "The Number Of Buttons For A Mouse Must Be An Integer", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (categoryInput.equalsIgnoreCase("keyboard") && (additionalInput.trim().equalsIgnoreCase("uk")
						|| additionalInput.trim().equalsIgnoreCase("us"))) {
					Layout layout = additionalInput.equalsIgnoreCase("uk") ? Layout.UK : Layout.US;
					if (typeInput.equalsIgnoreCase("standard")) {
						KeyboardTypes inputType = KeyboardTypes.STANDARD;
						Keyboard Keyboard = new Keyboard(barcodeInput, brandInput, colorInput, inputConnectivity,
								quantityInput, costInput, priceInput, inputCategory, layout, inputType);
						productsCheck(Keyboard, products);
						String str = Keyboard.toString();
						if (barcodeCheck(String.valueOf(Keyboard.getBarcode())) == false) {
							writeToStock(str);
						} else {
							JOptionPane.showMessageDialog(null, "Barcode Is Already In Stock", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} else if (typeInput.equalsIgnoreCase("flexible")) {
						KeyboardTypes inputType = KeyboardTypes.FLEXIBLE;
						Keyboard Keyboard = new Keyboard(barcodeInput, brandInput, colorInput, inputConnectivity,
								quantityInput, costInput, priceInput, inputCategory, layout, inputType);
						productsCheck(Keyboard, products);
						String str = Keyboard.toString();
						if (barcodeCheck(String.valueOf(Keyboard.getBarcode())) == false) {
							writeToStock(str);
						} else {
							JOptionPane.showMessageDialog(null, "Barcode Is Already In Stock", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} else if (typeInput.equalsIgnoreCase("gaming")) {
						KeyboardTypes inputType = KeyboardTypes.GAMING;
						Keyboard Keyboard = new Keyboard(barcodeInput, brandInput, colorInput, inputConnectivity,
								quantityInput, costInput, priceInput, inputCategory, layout, inputType);
						productsCheck(Keyboard, products);
						String str = Keyboard.toString();
						if (barcodeCheck(String.valueOf(Keyboard.getBarcode())) == false) {
							writeToStock(str);
						} else {
							JOptionPane.showMessageDialog(null, "Barcode Is Already In Stock", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				} else if (categoryInput.equalsIgnoreCase("keyboard")
						&& !((additionalInput.trim().equalsIgnoreCase("uk")
								|| additionalInput.trim().equalsIgnoreCase("us")))) {
					JOptionPane.showMessageDialog(null, "The Keyboard Category Must Be UK Or US", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} else if ((barcodeField.getText().trim().isEmpty() || brandField.getText().trim().isEmpty()
					|| colorField.getText().trim().isEmpty() || infos.getText().trim().isEmpty()
					|| quantityField.getText().trim().isEmpty() || costField.getText().trim().isEmpty()
					|| priceField.getText().trim().isEmpty())) {
				JOptionPane.showMessageDialog(null, "Please Fill In The Empty Fields", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else if (!(Integer.parseInt(barcodeField.getText().trim()) > 100000)) {
				JOptionPane.showMessageDialog(null, "Barcode Is Not In A Valid Format", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else if (!(isDouble(costField.getText()))) {
				JOptionPane.showMessageDialog(null, "The Cost Field Needs To Be In The Form Of A Double", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else if (!(isDouble(priceField.getText()))) {
				JOptionPane.showMessageDialog(null, "The Price Field Is Not In The Form Of A Double", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			// Making all input fields blank
			barcodeField.setValue(null);
			brandField.setText("");
			colorField.setText("");
			quantityField.setValue(null);
			costField.setText("");
			priceField.setText("");
			infos.setText("");
		});

		adminPage.setLocationRelativeTo(null);
		adminPage.setVisible(true);

	}

	public static void openCustomerPage() {
		JFrame customerPage = new JFrame("Customer Page");
		customerPage.setSize(1500, 1000);

		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel header = new JPanel();
		header.setLayout(new GridLayout(1, 2));

		JPanel userDetails = new JPanel();
		userDetails.setLayout(new BoxLayout(userDetails, BoxLayout.Y_AXIS));
		JLabel uname = new JLabel("Logged In As: " + welcomeUsername);
		JLabel name = new JLabel("Welcome, " + welcomeName);
		userDetails.add(uname);
		userDetails.add(name);

		Font detailFont = new Font("Arial", Font.BOLD, 17);
		uname.setFont(detailFont);
		name.setFont(detailFont);
		Border emptyBorder_1 = BorderFactory.createEmptyBorder(20, 180, 0, 0);
		userDetails.setBorder(emptyBorder_1);
		header.add(userDetails, BorderLayout.WEST);

		JLabel filterBy = new JLabel("Search By: ");
		JComboBox<String> sortDropdown = new JComboBox<>(new String[] { "Number Of Mouse Buttons", "Barcode" });
		NumberFormat intFormat = NumberFormat.getIntegerInstance();
		intFormat.setGroupingUsed(false);
		NumberFormatter formatInt = new NumberFormatter(intFormat);
		formatInt.setValueClass(Integer.class);
		formatInt.setMinimum(0);
		formatInt.setAllowsInvalid(false);
		JFormattedTextField inputBox = new JFormattedTextField(formatInt);
		inputBox.setColumns(17);
		JButton searchButton = new JButton("Search");
		JButton resetButton = new JButton("Reset Table");

		JPanel sortPanel = new JPanel();
		sortPanel.add(filterBy);
		sortPanel.add(sortDropdown);
		sortPanel.add(inputBox);
		Border emptyBorder_2 = BorderFactory.createEmptyBorder(20, 0, 0, 75);
		sortPanel.setBorder(emptyBorder_2);
		sortPanel.add(searchButton);
		sortPanel.add(resetButton);

		header.add(sortPanel, BorderLayout.CENTER);

		mainPanel.add(header, BorderLayout.NORTH);

		ArrayList<Product> products = new ArrayList<>();

		try {
			File stockData = new File("Stock");
			Scanner sc = new Scanner(stockData);

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] info = line.split(",");
				int barcode = Integer.parseInt(info[0].trim());

				ProductCategory category = info[1].trim().equalsIgnoreCase("mouse") ? ProductCategory.MOUSE
						: ProductCategory.KEYBOARD;
				String brand = info[3].trim();
				String color = info[4].trim();
				ConnectivityType connectivity = info[5].trim().equalsIgnoreCase("wired") ? ConnectivityType.WIRED
						: ConnectivityType.WIRELESS;
				int quantityInStock = Integer.parseInt(info[6].trim());
				double originalCost = Double.parseDouble(info[7].trim());
				double retailPrice = Double.parseDouble(info[8].trim());

				if (info[1].trim().equalsIgnoreCase("mouse")) {
					if (info[2].trim().equalsIgnoreCase("standard")) {
						MouseTypes mouseType = MouseTypes.STANDARD;
						int buttonNum = Integer.parseInt(info[9].trim());
						Mouse mouse = new Mouse(barcode, brand, color, connectivity, quantityInStock, originalCost,
								retailPrice, category, buttonNum, mouseType);
						products.add(mouse);
					} else if (info[2].trim().equalsIgnoreCase("gaming")) {
						MouseTypes mouseType = MouseTypes.GAMING;
						int buttonNum = Integer.parseInt(info[9].trim());
						Mouse mouse = new Mouse(barcode, brand, color, connectivity, quantityInStock, originalCost,
								retailPrice, category, buttonNum, mouseType);
						products.add(mouse);
					}
				} else if (info[1].trim().equalsIgnoreCase("keyboard")) {
					if (info[2].trim().equalsIgnoreCase("flexible")) {
						Layout layout = info[9].trim().equalsIgnoreCase("uk") ? Layout.UK : Layout.US;
						KeyboardTypes keyboardtype = KeyboardTypes.FLEXIBLE;
						Keyboard keyboard = new Keyboard(barcode, brand, color, connectivity, quantityInStock,
								originalCost, retailPrice, category, layout, keyboardtype);
						products.add(keyboard);
					} else if (info[2].trim().equalsIgnoreCase("gaming")) {
						Layout layout = info[9].trim().equalsIgnoreCase("uk") ? Layout.UK : Layout.US;
						KeyboardTypes keyboardType = KeyboardTypes.GAMING;
						Keyboard keyboard = new Keyboard(barcode, brand, color, connectivity, quantityInStock,
								originalCost, retailPrice, category, layout, keyboardType);
						products.add(keyboard);
					} else if (info[2].trim().equalsIgnoreCase("standard")) {
						Layout layout = info[9].trim().equalsIgnoreCase("uk") ? Layout.UK : Layout.US;
						KeyboardTypes keyboardType = KeyboardTypes.STANDARD;
						Keyboard keyboard = new Keyboard(barcode, brand, color, connectivity, quantityInStock,
								originalCost, retailPrice, category, layout, keyboardType);
						products.add(keyboard);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Collections.sort(products, Comparator.comparingDouble(Product::getRetailPrice));

		// Table that allows the customer to view all the products in stock
		String[] columnNames = { "Barcode", "Product Category", "Brand", "Type", "Connectivity", "Color",
				"Quantity In Stock", "Retail Price", "Additional Information" };

		DefaultTableModel viewTable = new DefaultTableModel(columnNames, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		DefaultTableModel tempTable = new DefaultTableModel(columnNames, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		JTable customerTable = new JTable(viewTable);
		customerTable.getTableHeader().setReorderingAllowed(false);
		customerTable.setRowHeight(50);
		customerTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		customerTable.setPreferredScrollableViewportSize(new Dimension(1200, 500));

		for (Product p : products) {
			if (String.valueOf(p.getCategory()).equalsIgnoreCase("mouse")) {
				Mouse m = (Mouse) p;
				String[] data = { String.valueOf(m.getBarcode()), m.getCategory().toString(), m.getBrand(),
						String.valueOf(m.getMouseType()), m.getConnectivity().toString(),
						m.getColor().substring(0, 1).toUpperCase() + m.getColor().substring(1),
						String.valueOf(m.getQuantityInStock()), "$ " + String.valueOf(m.getRetailPrice()),
						"Buttons: " + String.valueOf(m.getButtonNum()) };
				viewTable.addRow(data);
				tempTable.addRow(data);
			} else if (String.valueOf(p.getCategory()).equalsIgnoreCase("keyboard")) {
				Keyboard k = (Keyboard) p;
				String[] data = { String.valueOf(k.getBarcode()), k.getCategory().toString(), k.getBrand(),
						String.valueOf(k.getKeyboardType()), k.getConnectivity().toString(),
						k.getColor().substring(0, 1).toUpperCase() + k.getColor().substring(1),
						String.valueOf(k.getQuantityInStock()), "$ " + String.valueOf(k.getRetailPrice()),
						"Layout: " + String.valueOf(k.getLayout()) };
				viewTable.addRow(data);
				tempTable.addRow(data);
			}
		}

		// Button that changes the content of the table depending on the search option
		searchButton.addActionListener(e -> {
			String searchText = inputBox.getText();
			String selected = (String) sortDropdown.getSelectedItem();

			if (searchText.trim().length() <= 0) {
				JOptionPane.showMessageDialog(null, "Search Field Is Empty", "Error", JOptionPane.ERROR_MESSAGE);
			}

			else if (selected.equalsIgnoreCase("barcode")
					&& ((searchText.trim().length() != 6) || (!isInteger(searchText.trim())))) {
				JOptionPane.showMessageDialog(null, "Search Format For Barcode Is Invalid", "Error",
						JOptionPane.ERROR_MESSAGE);
				inputBox.setValue(null);
			}

			if ((selected.equalsIgnoreCase("number of mouse buttons")) && (searchText.length() > 0)
					&& (isInteger(searchText))) {
				viewTable.setRowCount(0);
				for (Product p : products) {
					if (p.getCategory().toString().equalsIgnoreCase("mouse")) {
						Mouse m = (Mouse) p;
						if (Integer.parseInt(searchText.trim()) == m.getButtonNum()) {
							String[] data = { String.valueOf(m.getBarcode()), m.getCategory().toString(), m.getBrand(),
									String.valueOf(m.getMouseType()), m.getConnectivity().toString(),
									m.getColor().substring(0, 1).toUpperCase() + m.getColor().substring(1),
									String.valueOf(m.getQuantityInStock()), "$ " + String.valueOf(m.getRetailPrice()),
									"Buttons: " + String.valueOf(m.getButtonNum()) };
							viewTable.addRow(data);
						}
					}
				}
				if (viewTable.getRowCount() == 0) {
					JOptionPane.showMessageDialog(null, "No Mice Have " + inputBox.getText() + " Buttons", "Error",
							JOptionPane.ERROR_MESSAGE);
					for (Product p : products) {
						if (String.valueOf(p.getCategory()).equalsIgnoreCase("mouse")) {
							Mouse m = (Mouse) p;
							String[] data = { String.valueOf(m.getBarcode()), m.getCategory().toString(), m.getBrand(),
									String.valueOf(m.getMouseType()), m.getConnectivity().toString(),
									m.getColor().substring(0, 1).toUpperCase() + m.getColor().substring(1),
									String.valueOf(m.getQuantityInStock()), "$ " + String.valueOf(m.getRetailPrice()),
									"Buttons: " + String.valueOf(m.getButtonNum()) };
							viewTable.addRow(data);
							tempTable.addRow(data);
						} else if (String.valueOf(p.getCategory()).equalsIgnoreCase("keyboard")) {
							Keyboard k = (Keyboard) p;
							String[] data = { String.valueOf(k.getBarcode()), k.getCategory().toString(), k.getBrand(),
									String.valueOf(k.getKeyboardType()), k.getConnectivity().toString(),
									k.getColor().substring(0, 1).toUpperCase() + k.getColor().substring(1),
									String.valueOf(k.getQuantityInStock()), "$ " + String.valueOf(k.getRetailPrice()),
									"Layout: " + String.valueOf(k.getLayout()) };
							viewTable.addRow(data);
							tempTable.addRow(data);
						}
					}
				}
				inputBox.setValue(null);
			} else if (selected.equalsIgnoreCase("barcode")) {
				if ((searchText.trim().length() == 6) && (isInteger(searchText.trim()))) {
					viewTable.setRowCount(0);
					for (Product p : products) {
						if (searchText.trim().equals((String.valueOf(p.getBarcode())))) {
							if (String.valueOf(p.getCategory()).equalsIgnoreCase("mouse")) {
								Mouse m = (Mouse) p;
								String[] data = { String.valueOf(m.getBarcode()), m.getCategory().toString(),
										m.getBrand(), String.valueOf(m.getMouseType()), m.getConnectivity().toString(),
										m.getColor().substring(0, 1).toUpperCase() + m.getColor().substring(1),
										String.valueOf(m.getQuantityInStock()),
										"$ " + String.valueOf(m.getRetailPrice()),
										"Buttons: " + String.valueOf(m.getButtonNum()) };
								viewTable.addRow(data);
							} else if (String.valueOf(p.getCategory()).equalsIgnoreCase("keyboard")) {
								Keyboard k = (Keyboard) p;
								String[] data = { String.valueOf(k.getBarcode()), k.getCategory().toString(),
										k.getBrand(), String.valueOf(k.getKeyboardType()),
										k.getConnectivity().toString(),
										k.getColor().substring(0, 1).toUpperCase() + k.getColor().substring(1),
										String.valueOf(k.getQuantityInStock()),
										"$ " + String.valueOf(k.getRetailPrice()),
										"Layout: " + String.valueOf(k.getLayout()) };
								viewTable.addRow(data);
							}
						}
					}
				}
				if (viewTable.getRowCount() == 0) {
					JOptionPane.showMessageDialog(null,
							"The Barcode " + inputBox.getText() + " Does Not Exist In Stock", "Error",
							JOptionPane.ERROR_MESSAGE);
					for (Product p : products) {
						if (String.valueOf(p.getCategory()).equalsIgnoreCase("mouse")) {
							Mouse m = (Mouse) p;
							String[] data = { String.valueOf(m.getBarcode()), m.getCategory().toString(), m.getBrand(),
									String.valueOf(m.getMouseType()), m.getConnectivity().toString(),
									m.getColor().substring(0, 1).toUpperCase() + m.getColor().substring(1),
									String.valueOf(m.getQuantityInStock()), "$ " + String.valueOf(m.getRetailPrice()),
									"Buttons: " + String.valueOf(m.getButtonNum()) };
							viewTable.addRow(data);
							tempTable.addRow(data);
						} else if (String.valueOf(p.getCategory()).equalsIgnoreCase("keyboard")) {
							Keyboard k = (Keyboard) p;
							String[] data = { String.valueOf(k.getBarcode()), k.getCategory().toString(), k.getBrand(),
									String.valueOf(k.getKeyboardType()), k.getConnectivity().toString(),
									k.getColor().substring(0, 1).toUpperCase() + k.getColor().substring(1),
									String.valueOf(k.getQuantityInStock()), "$ " + String.valueOf(k.getRetailPrice()),
									"Layout: " + String.valueOf(k.getLayout()) };
							viewTable.addRow(data);
							tempTable.addRow(data);
						}
					}
				}
				inputBox.setValue(null);
			}
		});

		// Button that allows user to reset the table once a search is done
		resetButton.addActionListener(e -> {
			inputBox.setValue(null);
			viewTable.setRowCount(0);
			Collections.sort(products, Comparator.comparingDouble(Product::getRetailPrice));
			for (Product p : products) {
				if (String.valueOf(p.getCategory()).equalsIgnoreCase("mouse")) {
					Mouse m = (Mouse) p;
					String[] data = { String.valueOf(m.getBarcode()), m.getCategory().toString(), m.getBrand(),
							String.valueOf(m.getMouseType()), m.getConnectivity().toString(),
							m.getColor().substring(0, 1).toUpperCase() + m.getColor().substring(1),
							String.valueOf(m.getQuantityInStock()), "$ " + String.valueOf(m.getRetailPrice()),
							"Buttons: " + String.valueOf(m.getButtonNum()) };
					viewTable.addRow(data);
				} else if (String.valueOf(p.getCategory()).equalsIgnoreCase("keyboard")) {
					Keyboard k = (Keyboard) p;
					String[] data = { String.valueOf(k.getBarcode()), k.getCategory().toString(), k.getBrand(),
							String.valueOf(k.getKeyboardType()), k.getConnectivity().toString(),
							k.getColor().substring(0, 1).toUpperCase() + k.getColor().substring(1),
							String.valueOf(k.getQuantityInStock()), "$ " + String.valueOf(k.getRetailPrice()),
							"Layout: " + String.valueOf(k.getLayout()) };
					viewTable.addRow(data);
				}
			}
		});

		JScrollPane tableScrollPane = new JScrollPane(customerTable);
		tableScrollPane.setPreferredSize(new Dimension(1200, 365));

		JPanel tablePanel = new JPanel();
		tablePanel.add(tableScrollPane);

		tablePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

		mainPanel.add(tablePanel, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new GridLayout(1, 2));

		JPanel leftColumnPanel = new JPanel();
		JPanel clearInfo = new JPanel();
		clearInfo.setLayout(new BoxLayout(clearInfo, BoxLayout.Y_AXIS));
		JLabel basketLabel = new JLabel("Basket");
		basketLabel.setFont(new Font("Arial", Font.BOLD, 20));
		basketLabel.setBorder(BorderFactory.createEmptyBorder(0, 18, 20, 0));
		basketLabel.setPreferredSize(new Dimension(40, 40));
		JButton clearButton = new JButton("Clear Basket");

		NumberFormat barcodeFormat = NumberFormat.getIntegerInstance();
		barcodeFormat.setGroupingUsed(false);
		NumberFormatter barcodeFormatter = new NumberFormatter(barcodeFormat);
		barcodeFormatter.setValueClass(Integer.class);
		barcodeFormatter.setMaximum(999999);
		barcodeFormatter.setAllowsInvalid(false);
		JLabel barcodeInput = new JLabel("Enter Barcode Of The Item You Want To Add:");
		JFormattedTextField inputBarcodeField = new JFormattedTextField(barcodeFormatter);

		NumberFormat quantityFormat = NumberFormat.getIntegerInstance();
		quantityFormat.setGroupingUsed(false);
		NumberFormatter integerFormatter = new NumberFormatter(quantityFormat);
		integerFormatter.setValueClass(Integer.class);
		integerFormatter.setMinimum(1);
		integerFormatter.setAllowsInvalid(false);

		JLabel quantityInput = new JLabel("Enter The Quantity Of The Item You Want:");
		JFormattedTextField inputQuantityField = new JFormattedTextField(integerFormatter);
		JButton addButton = new JButton("Click To Add Product");
		clearInfo.add(basketLabel);
		clearInfo.add(barcodeInput);
		clearInfo.add(inputBarcodeField);
		clearInfo.add(quantityInput);
		clearInfo.add(inputQuantityField);
		clearInfo.add(addButton);
		clearInfo.add(clearButton);
		leftColumnPanel.add(clearInfo);

		// Table for the user to view the contents of the basket
		String[] columns = { "Barcode", "Product Category", "Quantity", "Retail Price", "Total Price" };
		DefaultTableModel basketTable = new DefaultTableModel(columns, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		JTable basket = new JTable(basketTable);
		basket.getTableHeader().setReorderingAllowed(false);
		basket.getColumnModel().getColumn(1).setPreferredWidth(125);
		basket.setRowHeight(28);
		JScrollPane basketPane = new JScrollPane(basket);
		basketPane.setPreferredSize(new Dimension(400, 200));
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));

		leftColumnPanel.add(basketPane);

		JPanel rightColumnPanel = new JPanel();
		rightColumnPanel.setLayout(new BoxLayout(rightColumnPanel, BoxLayout.Y_AXIS));

		JButton paypalButton = new JButton("Pay By PayPal");
		JButton creditButton = new JButton("Pay By Credit Card");

		JPanel paypalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel paypalLabel = new JLabel("Enter Your PayPal Email Address:");
		JTextField paypalInput = new JTextField(20);
		JButton paypalPay = new JButton("Pay");
		paypalPanel.add(paypalLabel);
		paypalPanel.add(paypalInput);
		paypalPanel.add(paypalPay);
		paypalPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 60, 160));

		NumberFormat creditFormat = NumberFormat.getIntegerInstance();
		creditFormat.setGroupingUsed(false);
		NumberFormatter creditFormatter = new NumberFormatter(creditFormat);
		creditFormatter.setValueClass(Integer.class);
		creditFormatter.setMaximum(999999);
		creditFormatter.setAllowsInvalid(false);
		JPanel creditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		creditPanel.setPreferredSize(new Dimension(300, 100));
		JLabel cardLabel = new JLabel("Enter Your 6-Digit Card Number:");
		JFormattedTextField cardInput = new JFormattedTextField(creditFormatter);
		cardInput.setColumns(20);
		JLabel digitLabel = new JLabel("Enter Your 3-Digit Security Code:");
		NumberFormat securityFormat = NumberFormat.getIntegerInstance();
		securityFormat.setGroupingUsed(false);
		NumberFormatter securityFormatter = new NumberFormatter(securityFormat);
		securityFormatter.setValueClass(Integer.class);
		securityFormatter.setMaximum(999);
		securityFormatter.setAllowsInvalid(false);
		JFormattedTextField digitInput = new JFormattedTextField(securityFormatter);
		digitInput.setColumns(20);
		JButton creditPay = new JButton("Pay");
		creditPanel.add(cardLabel);
		creditPanel.add(cardInput);
		creditPanel.add(digitLabel);
		creditPanel.add(digitInput);
		creditPanel.add(creditPay);
		creditPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 60, 150));

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(paypalButton);
		buttonPanel.add(creditButton);

		rightColumnPanel.add(buttonPanel);
		paypalPanel.setVisible(false);
		rightColumnPanel.add(paypalPanel);
		creditPanel.setVisible(false);
		rightColumnPanel.add(creditPanel);

		rightColumnPanel.setBorder(BorderFactory.createEmptyBorder(0, 170, 0, 0));

		bottomPanel.add(leftColumnPanel);
		bottomPanel.add(rightColumnPanel);

		mainPanel.add(bottomPanel, BorderLayout.SOUTH);

		// ArrayList that contains all items added to the basket by the user
		ArrayList<Product> basketProducts = new ArrayList<>();

		// Button that handles adding the product to the basket and all the validation
		// checks
		addButton.addActionListener(e -> {
			DecimalFormat df = new DecimalFormat("#.##");
			if ((inputBarcodeField.getText().length() == 6) && (isInteger(inputBarcodeField.getText().trim()))
					&& (inputQuantityField.getText().trim().length() > 0)
					&& (Integer.parseInt(inputQuantityField.getText().trim()) > 0)
					&& (isInteger(inputQuantityField.getText().trim()))) {
				boolean inBasket = false;
				for (Product p : products) {

					if (String.valueOf(p.getBarcode()).equals(inputBarcodeField.getText().trim())
							&& ((p.getQuantityInStock()) == 0)) {
						JOptionPane.showMessageDialog(null, "The Item " + p.getBarcode() + " Is Not Currently In Stock",
								"Error", JOptionPane.ERROR_MESSAGE);
					} else if (String.valueOf(p.getBarcode()).equals(inputBarcodeField.getText().trim())
							&& (Integer.parseInt(inputQuantityField.getText()) > p.getQuantityInStock())) {
						JOptionPane.showMessageDialog(null,
								"The Quantity You Want Is Greater Than The Quantity Currently in Stock", "Error",
								JOptionPane.ERROR_MESSAGE);
					}

					if (String.valueOf(p.getBarcode()).equals(inputBarcodeField.getText().trim())
							&& (Integer.parseInt(inputQuantityField.getText()) <= p.getQuantityInStock())) {

						for (Product x : basketProducts) {
							if (x.getBarcode() == p.getBarcode()) {
								inBasket = true;
								break;
							}
						}

						if (!inBasket) {
							if (String.valueOf(p.getCategory()).equalsIgnoreCase("mouse")) {
								Mouse m = (Mouse) p;
								Mouse Mouse = new Mouse(m.getBarcode(), m.getBrand(), m.getColor(), m.getConnectivity(),
										m.getQuantityInStock(), m.getOriginalCost(), m.getRetailPrice(),
										m.getCategory(), m.getButtonNum(), m.getMouseType());
								Mouse.setQuantityInStock(Integer.parseInt(inputQuantityField.getText()));
								basketProducts.add(Mouse);
								String[] data = { String.valueOf(Mouse.getBarcode()),
										String.valueOf(Mouse.getCategory()), String.valueOf(Mouse.getQuantityInStock()),
										String.valueOf("$ " + Mouse.getRetailPrice()), String.valueOf("$ "
												+ df.format(Mouse.getQuantityInStock() * Mouse.getRetailPrice())) };
								basketTable.addRow(data);
							} else if (String.valueOf(p.getCategory()).equalsIgnoreCase("keyboard")) {
								Keyboard k = (Keyboard) p;
								Keyboard Keyboard = new Keyboard(k.getBarcode(), k.getBrand(), k.getColor(),
										k.getConnectivity(), k.getQuantityInStock(), k.getOriginalCost(),
										k.getRetailPrice(), k.getCategory(), k.getLayout(), k.getKeyboardType());
								Keyboard.setQuantityInStock(Integer.parseInt(inputQuantityField.getText()));
								basketProducts.add(Keyboard);
								String[] data = { String.valueOf(Keyboard.getBarcode()),
										String.valueOf(Keyboard.getCategory()),
										String.valueOf(Keyboard.getQuantityInStock()),
										String.valueOf("$ " + Keyboard.getRetailPrice()), String.valueOf("$ " + df
												.format(Keyboard.getQuantityInStock() * Keyboard.getRetailPrice())) };
								basketTable.addRow(data);
							}

						} else {
							JOptionPane.showMessageDialog(null, "Item Is Already In The Basket", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
						
						
					}
				}
			} else if (inputBarcodeField.getText().isEmpty() || inputQuantityField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Please Fill In The Empty Fields", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else if (!(Integer.parseInt(inputBarcodeField.getText().trim()) > 100000)) {
				JOptionPane.showMessageDialog(null, "Barcode Is Not In A Valid Format", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			if ((inputBarcodeField.getText().trim().length() == 6)
					&& (!barcodeInArray(inputBarcodeField.getText().trim(), products))
					&& !inputBarcodeField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Barcode Does Not Exist In The Stock", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			inputBarcodeField.setValue(null);
			inputQuantityField.setValue(null);
		});

		paypalButton.addActionListener(e -> {
			creditPanel.setVisible(false);
			paypalPanel.setVisible(true);
		});

		creditButton.addActionListener(e -> {
			paypalPanel.setVisible(false);
			creditPanel.setVisible(true);
		});

		// Button that allows the user to clear their basket
		clearButton.addActionListener(e -> {
			basketTable.setRowCount(0);
			basketProducts.clear();
		});

		// Button that allows the user to pay for their items using PayPal and displays
		// their receipt
		paypalPay.addActionListener(e -> {
			ArrayList<Product> basketProductsCopy = new ArrayList<>(basketProducts);

			if ((!paypalInput.getText().trim().isEmpty()) && (isValidEmail(paypalInput.getText().trim()))
					&& (basketProducts.size() > 0)) {
				for (Product p : products) {
					for (Product x : basketProductsCopy) {
						if (String.valueOf(p.getBarcode()).equalsIgnoreCase(String.valueOf(x.getBarcode()))) {
							p.setQuantityInStock(p.getQuantityInStock() - x.getQuantityInStock());

							try {
								File stockFile = new File("Stock");
								Scanner sc = new Scanner(stockFile);
								StringBuilder sb = new StringBuilder();

								while (sc.hasNextLine()) {
									String line = sc.nextLine();
									String[] parts = line.split(", ");
									if (parts[0].trim().equals(String.valueOf(x.getBarcode()))) {
										DecimalFormat df = new DecimalFormat("#.##");
										parts[6] = String.valueOf(df.format(p.getQuantityInStock()));
										line = String.join(", ", parts);
									}
									sb.append(line).append("\n");
								}
								basketTable.setRowCount(0);
								basketProducts.clear();
								sc.close();
								FileWriter writer = new FileWriter(stockFile);
								writer.write(sb.toString());
								writer.close();
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}

					}
				}
				PayPalPayment payment = new PayPalPayment(String.valueOf(paypalInput.getText().trim()));
				Receipt paypalReceipt = payment.processPayment(sumBasket(basketProductsCopy), welcomeAddress);
				JFrame paypalFrame = new JFrame("PayPal Receipt");
				JPanel PayPalPanel = new JPanel(new BorderLayout());
				JLabel paypalTitle = new JLabel("PayPal Receipt");
				paypalTitle.setHorizontalAlignment(JLabel.CENTER);
				paypalTitle.setFont(new Font("Arial", Font.BOLD, 24));
				PayPalPanel.add(paypalTitle, BorderLayout.NORTH);
				JLabel paypalText = new JLabel(paypalReceipt.toString());
				paypalText.setHorizontalAlignment(JLabel.CENTER);
				paypalText.setFont(new Font("Arial", Font.BOLD, 14));
				PayPalPanel.add(paypalText, BorderLayout.CENTER);
				paypalFrame.add(PayPalPanel);
				paypalFrame.setSize(800, 200);
				paypalFrame.setLocationRelativeTo(null);
				paypalFrame.setResizable(false);
				paypalFrame.setVisible(true);
				paypalInput.setText("");

				viewTable.setRowCount(0);

				ArrayList<Product> productsCopy = new ArrayList<>(products);
				Collections.sort(productsCopy, Comparator.comparingDouble(Product::getRetailPrice));

				for (Product y : productsCopy) {
					if (String.valueOf(y.getCategory()).equalsIgnoreCase("mouse")) {
						Mouse m = (Mouse) y;
						String[] data = { String.valueOf(m.getBarcode()), m.getCategory().toString(), m.getBrand(),
								String.valueOf(m.getMouseType()), m.getConnectivity().toString(),
								m.getColor().substring(0, 1).toUpperCase() + m.getColor().substring(1),
								String.valueOf(m.getQuantityInStock()), "$ " + String.valueOf(m.getRetailPrice()),
								"Buttons: " + String.valueOf(m.getButtonNum()) };
						viewTable.addRow(data);
					} else if (String.valueOf(y.getCategory()).equalsIgnoreCase("keyboard")) {
						Keyboard k = (Keyboard) y;
						String[] data = { String.valueOf(k.getBarcode()), k.getCategory().toString(), k.getBrand(),
								String.valueOf(k.getKeyboardType()), k.getConnectivity().toString(),
								k.getColor().substring(0, 1).toUpperCase() + k.getColor().substring(1),
								String.valueOf(k.getQuantityInStock()), "$ " + String.valueOf(k.getRetailPrice()),
								"Layout: " + String.valueOf(k.getLayout()) };
						viewTable.addRow(data);
					}
				}
			} else if (basketProductsCopy.size() == 0) {
				JOptionPane.showMessageDialog(null, "Unable To Pay Since The Basket Is Empty", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else if (paypalInput.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Please Fill In The Empty Fields", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else if (!(isValidEmail(paypalInput.getText().trim()))) {
				JOptionPane.showMessageDialog(null, "Email Is Not In A Valid Format", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		// Button that allows the user to pay for their items using Credit Card and
		// displays their receipt
		creditPay.addActionListener(e -> {

			ArrayList<Product> basketProductsCopy = new ArrayList<>(basketProducts);
			ArrayList<Product> productsCopy = new ArrayList<>(products);

			if (isInteger(cardInput.getText()) && isInteger(digitInput.getText())
					&& (Integer.parseInt(cardInput.getText()) >= 100000)
					&& (Integer.parseInt(digitInput.getText()) >= 100) && (basketProductsCopy.size() > 0)) {
				for (Product p : products) {
					for (Product x : basketProductsCopy) {
						if (String.valueOf(p.getBarcode()).equalsIgnoreCase(String.valueOf(x.getBarcode()))) {
							p.setQuantityInStock(p.getQuantityInStock() - x.getQuantityInStock());

							try {
								File stockFile = new File("Stock");
								Scanner sc = new Scanner(stockFile);
								StringBuilder sb = new StringBuilder();

								while (sc.hasNextLine()) {
									String line = sc.nextLine();
									String[] parts = line.split(", ");
									if (parts[0].trim().equals(String.valueOf(x.getBarcode()))) {
										DecimalFormat df = new DecimalFormat("#.##");
										parts[6] = String.valueOf(df.format(p.getQuantityInStock()));
										line = String.join(", ", parts);
									}
									sb.append(line).append("\n");
								}
								basketTable.setRowCount(0);
								basketProducts.clear();
								sc.close();
								FileWriter writer = new FileWriter(stockFile);
								writer.write(sb.toString());
								writer.close();
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}
					}
				}
				CreditPayment payment = new CreditPayment(String.valueOf(cardInput.getText().trim()),
						String.valueOf(digitInput.getText().trim()));
				Receipt creditReceipt = payment.processPayment(sumBasket(basketProductsCopy), welcomeAddress);
				JFrame creditFrame = new JFrame("Credit Card Receipt");
				JPanel creditCardPanel = new JPanel(new BorderLayout());
				JLabel creditTitle = new JLabel("Credit Card Receipt");
				creditTitle.setHorizontalAlignment(JLabel.CENTER);
				creditTitle.setFont(new Font("Arial", Font.BOLD, 24));
				creditCardPanel.add(creditTitle, BorderLayout.NORTH);
				JLabel creditText = new JLabel(creditReceipt.toString());
				creditText.setHorizontalAlignment(JLabel.CENTER);
				creditText.setFont(new Font("Arial", Font.BOLD, 14));
				creditCardPanel.add(creditText, BorderLayout.CENTER);
				creditFrame.add(creditCardPanel);
				creditFrame.setSize(800, 200);
				creditFrame.setLocationRelativeTo(null);
				creditFrame.setResizable(false);
				creditFrame.setVisible(true);

				cardInput.setValue(null);
				digitInput.setValue(null);

				viewTable.setRowCount(0);
				Collections.sort(productsCopy, Comparator.comparingDouble(Product::getRetailPrice));

				for (Product y : productsCopy) {
					if (String.valueOf(y.getCategory()).equalsIgnoreCase("mouse")) {
						Mouse m = (Mouse) y;
						String[] data = { String.valueOf(m.getBarcode()), m.getCategory().toString(), m.getBrand(),
								String.valueOf(m.getMouseType()), m.getConnectivity().toString(),
								m.getColor().substring(0, 1).toUpperCase() + m.getColor().substring(1),
								String.valueOf(m.getQuantityInStock()), "$ " + String.valueOf(m.getRetailPrice()),
								"Buttons: " + String.valueOf(m.getButtonNum()) };
						viewTable.addRow(data);
					} else if (String.valueOf(y.getCategory()).equalsIgnoreCase("keyboard")) {
						Keyboard k = (Keyboard) y;
						String[] data = { String.valueOf(k.getBarcode()), k.getCategory().toString(), k.getBrand(),
								String.valueOf(k.getKeyboardType()), k.getConnectivity().toString(),
								k.getColor().substring(0, 1).toUpperCase() + k.getColor().substring(1),
								String.valueOf(k.getQuantityInStock()), "$ " + String.valueOf(k.getRetailPrice()),
								"Layout: " + String.valueOf(k.getLayout()) };
						viewTable.addRow(data);
					}
				}
			} else if (cardInput.getText().isEmpty() || digitInput.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Please Fill In The Empty Fields", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else if (basketProductsCopy.size() == 0) {
				JOptionPane.showMessageDialog(null, "Unable To Pay Since The Basket Is Empty", "Error", JOptionPane.ERROR_MESSAGE);
			} else if (Integer.parseInt(cardInput.getText()) < 100000) {
				JOptionPane.showMessageDialog(null, "Invalid Card Number", "Error", JOptionPane.ERROR_MESSAGE);
			} else if (Integer.parseInt(digitInput.getText()) < 100) {
				JOptionPane.showMessageDialog(null, "Invalid Security Code", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		customerPage.getContentPane().add(mainPanel);
		customerPage.setLocationRelativeTo(null);
		customerPage.setVisible(true);
	}

	public static double sumBasket(ArrayList<Product> products) {
		double sum = 0;

		for (Product p : products) {
			sum += p.getRetailPrice() * p.getQuantityInStock();
		}
		return sum;
	}

	public static boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static void writeToStock(String line) {
		try {
			FileWriter fileWriter = new FileWriter("Stock", true);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println(line);
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean barcodeCheck(String barcode) {
		boolean check = false;
		try {
			File stockData = new File("Stock");
			Scanner sc = new Scanner(stockData);

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] info = line.split(",");
				String barcodes = info[0].trim();

				if (barcodes.equals(barcode)) {
					check = true;
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return check;
	}

	public static void productsCheck(Product product, ArrayList<Product> products) {
		boolean exists = false;
		for (int i = 0; i < products.size(); i++) {
			if (product.getBarcode() == products.get(i).getBarcode()) {
				exists = true;
				break;
			}
		}
		if (!exists) {
			products.add(product);
		}
	}

	public static boolean barcodeInArray(String barcode, ArrayList<Product> products) {
		for (Product p : products) {
			if (String.valueOf(p.getBarcode()).equals(barcode)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isValidEmail(String email) {

		long count = email.chars().filter(ch -> ch == '@').count();

		return count == 1 && email.indexOf('@') > 0 && email.indexOf('@') < email.length() - 1;
	}

}
