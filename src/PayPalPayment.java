
public class PayPalPayment implements PaymentMethod {
	private String email;
	
	public PayPalPayment(String email) {
		this.email = email;
	}

	@Override
	public Receipt processPayment(double amount, Address fullAddress) {
		return new Receipt(amount, fullAddress, this.email);
	}
	
	public String toString() {
		return "";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
