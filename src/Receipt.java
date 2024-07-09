import java.text.DecimalFormat;
import java.time.LocalDate;

public class Receipt {
	private double amount;
	private Address fullAddress;
	private String email;
	private String cardNumber;
	private String securityNumber;
	
	public Receipt(double amount, Address fullAddress, String email) {
		this.amount = amount;
		this.fullAddress = fullAddress;
		this.email = email;
	}
	
	public Receipt(double amount, Address fullAddress, String cardNumber, String securityNumber) {
		this.amount = amount;
		this.fullAddress = fullAddress;
		this.cardNumber = cardNumber;
		this.securityNumber = securityNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;

	} 

	public Address getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(Address fullAddress) {
		this.fullAddress = fullAddress;
	}
	
	public String getSecurityNumber() {
		return securityNumber;
	}
	
	public void setSecurityNumber(String securityNumber) {
		this.securityNumber = securityNumber;
	}
	
	public String toString() {
		LocalDate date = LocalDate.now();
	    DecimalFormat df = new DecimalFormat("#.##");
	    
		if (email != null) {
			return "$ " + df.format(this.amount) + " paid by PayPal using " + email + " on " + date + ", and the delivery address is " + this.fullAddress.toString();
		} else if ((cardNumber != null) && (securityNumber != null)) {
			return "$ " + df.format(this.amount) + " paid by Credit Card using " + cardNumber + " on " + date + ", and the delivery address is " + this.fullAddress.toString();
		} else {
			return "";
		}

	}
	
}
