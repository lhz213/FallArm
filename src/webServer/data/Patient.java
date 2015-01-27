package webServer.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Patient extends Person {
	private String homeAddress;
	private Calendar birthdate;
	private String birthdateStr;
	private int age;
	private int relatedNurseId;

	public Patient() {
		super();
	}

	public Patient(int id, String emailAddress, String firstName,
			String lastName, Calendar birthdate, int gender,
			String homeAddress, int relatedNurseId) {
		super(id, emailAddress, firstName, lastName, gender);
		this.homeAddress = homeAddress;
		this.birthdate = birthdate;
		this.relatedNurseId = relatedNurseId;
		calculateAge();
	}

	public Calendar getBirthdate() {
		return birthdate;
	}

	public String getBirthdateStr() {
		birthdateStr = new SimpleDateFormat("yyyy-MM-dd")
				.format(birthdate.getTime());
		return birthdateStr;
	}

	public int getAge() {
		return age;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public int getRelatedNurseId() {
		return relatedNurseId;
	}

	private int calculateAge() {
		Calendar today = Calendar.getInstance();
		age = today.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);
		if (today.get(Calendar.MONTH) < birthdate.get(Calendar.MONTH))
			return age;
		else if (today.get(Calendar.MONTH) == birthdate.get(Calendar.MONTH))
			if (today.get(Calendar.DAY_OF_MONTH) >= birthdate
					.get(Calendar.DAY_OF_MONTH))
				return ++age;
			else
				return age;
		else
			return ++age;
	}

	public String toString() {
		String str = "Patient:\n";
		str += super.toString();
		str += "Home:\t\t" + homeAddress + "\n";
		str += "Age:\t\t" + age + "\n";
		str += "Birthdate:\t" + birthdate.toString() + "\n";
		str += "Related nurse:\t" + relatedNurseId + "\n";
		return str;
	}

}
