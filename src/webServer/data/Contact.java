package webServer.data;

public class Contact extends Person {

	private String phoneNumber;
	private int patientId;

	public Contact() {
		super();
	}

	public Contact(int id, int patientId, String emailAddress,
			String phoneNumber, String firstName, String lastName, int gender) {
		super(id, emailAddress, firstName, lastName, gender);
		this.patientId = patientId;
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public int getPatientId() {
		return patientId;
	}

	public String toString() {
		String str = "Contact:\n";
		str += super.toString();
		str += "Patient ID:\t" + patientId + "\n";
		str += "Phone:\t\t" + phoneNumber + "\n";
		return str;
	}
}
