
public class Address {
	private int houseNumber;
	private String postcode;
	private String city;
	
	public Address(int houseNumber, String postcode, String city) {
		this.houseNumber = houseNumber;
		this.postcode = postcode;
		this.city = city;
	}

	public int getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(int houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public String toString() {
		return this.city + ", " + this.houseNumber + ", " + this.postcode + ".";
	}
	
	
}
