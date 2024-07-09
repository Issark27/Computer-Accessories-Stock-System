
public class CreditPayment implements PaymentMethod {
	private String cardNumber;
	private String securityCode;
	
	public CreditPayment(String cardNumber, String securityCode) {
		this.cardNumber = cardNumber;
		this.securityCode = securityCode;
	}
	

	@Override
	public Receipt processPayment(double amount, Address fullAddress) {
		return new Receipt(amount, fullAddress, this.cardNumber, this.securityCode);
	}
	

}
