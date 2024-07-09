
public class Mouse extends Product {
	private int buttonNum;
	private MouseTypes mouseType;
	
	public Mouse(int barcode, String brand, String color, ConnectivityType connectivity, int quantityInStock,
			double originalCost, double retailPrice, ProductCategory category, int buttonNum, MouseTypes mouseType) {
		super(barcode, brand, color, connectivity, quantityInStock, originalCost, retailPrice, category);
		this.setButtonNum(buttonNum);
		this.mouseType = mouseType;
	}

	public int getButtonNum() {
		return buttonNum;
	}

	public void setButtonNum(int buttonNum) {
		this.buttonNum = buttonNum;
	}
	
	public MouseTypes getMouseType() {
		return mouseType;
	}

	public void setMouseType(MouseTypes mouseType) {
		this.mouseType = mouseType;
	}

	@Override
	public String toString() {
		return this.barcode + ", " + this.category.name().toLowerCase() + ", " + this.mouseType.name().toLowerCase() + ", " + this.brand + ", " + this.color + ", " + this.connectivity.name().toLowerCase() + ", " + this.quantityInStock + ", " + this.originalCost + ", " + this.retailPrice + ", " + this.buttonNum;
	}
	
	public double getTotalPrice() {
		return getQuantityInStock() * getRetailPrice();
	}
	
}
