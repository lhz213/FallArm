package webServer.data;

public class Person {
	private static final int MALE = 1;
	private static final int FEMALE = 0;

	private int id;
	private String emailAddress;
	private String firstName;
	private String lastName;
	private int gender;

	public Person() {
	}

	public Person(int id, String emailAddress, String firstName,
			String lastName, int gender) {
		this.id = id;
		this.emailAddress = emailAddress;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
	}

	public int getId() {
		return id;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public int getGender() {
		return gender;
	}

	public String toString() {
		String str = "";
		str += "First name:\t" + firstName + "\n";
		str += "Last name:\t" + lastName + "\n";
		if (gender == MALE)
			str += "Gender:\t\tMale\n";
		else if (gender == FEMALE)
			str += "Gender:\t\tFemale\n";
		else
			str += "Gender:\t\tUnknow\n";
		str += "E-mail:\t\t" + emailAddress + "\n";
		return str;
	}

}
