
public abstract class Product {
	int barcode;
	ProductCategory category;
	String brand;
	String color;
	ConnectivityType connectivity;
	int quantityInStock;
	double originalCost;
	double retailPrice;

	public Product(int barcode, String brand, String color, ConnectivityType connectivity, int quantityInStock, 
			double originalCost, double retailPrice, ProductCategory category) {
		this.barcode = barcode;
		this.brand = brand;
		this.color = color;
		this.connectivity = connectivity;
		this.quantityInStock = quantityInStock;
		this.originalCost = originalCost;
		this.retailPrice = retailPrice;
		this.category = category;
	}

	public int getBarcode() {
		return barcode;
	}

	public String getBrand() {
		return brand;
	}
	
	public String getColor() {
		return color;
	}
	
	public ConnectivityType getConnectivity() {
		return connectivity;
	}
	
	public int getQuantityInStock() {
		return quantityInStock;
	}

	public void setQuantityInStock(int quantityInStock) {
		this.quantityInStock = quantityInStock;
	}
	
	public double getOriginalCost() {
		return originalCost;
	}

	public double getRetailPrice() {
		return retailPrice;
	}
	
	public ProductCategory getCategory() {
		return category;
	}
	
	public abstract String toString();


}
