
public class User {
	private int userID;
	private String username;
	private String name;
	private Address address;
	private UserRole role;
	
	public User(int userID, String username, String name, Address address, UserRole role) {
		this.userID = userID;
		this.username = username;
		this.name = name;
		this.address = address;
		this.role = role;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}
	

	
}
