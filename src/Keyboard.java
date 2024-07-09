
public class Keyboard extends Product {
	private Layout layout;
	private KeyboardTypes keyboardType;
	
	public Keyboard(int barcode, String brand, String color, ConnectivityType connectivity,
			int quantityInStock, double originalCost, double retailPrice, ProductCategory category, Layout layout, KeyboardTypes keyboardType) {
		super(barcode, brand, color, connectivity, quantityInStock, originalCost, retailPrice, category);
		this.layout = layout;
		this.keyboardType = keyboardType;
	}

	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}
	
	@Override
	public String toString() {
		return this.barcode + ", " + this.category.name().toLowerCase() + ", " + this.keyboardType.name().toLowerCase() + ", " + this.brand + ", " + this.color + ", " + this.connectivity.name().toLowerCase() + ", " + this.quantityInStock + ", " + this.originalCost + ", " + this.retailPrice + ", " + this.layout.name().toLowerCase();
	}

	public KeyboardTypes getKeyboardType() {
		return keyboardType;
	}

	public void setKeyboardType(KeyboardTypes keyboardType) {
		this.keyboardType = keyboardType;
	}
	
	public double getTotalPrice() {
		return getQuantityInStock() * getRetailPrice();
	}
	

}
