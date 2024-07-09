
public interface PaymentMethod {
	
	Receipt processPayment(double amount, Address fullAddress);

}
