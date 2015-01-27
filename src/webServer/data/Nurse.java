package webServer.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Nurse extends Person {
	private Calendar birthdate;
	private String birthdateStr;
	private int age;

	public Nurse() {
		super();
	}

	public Nurse(int id, String emailAddress, String firstName,
			String lastName, Calendar birthdate, int gender) {
		super(id, emailAddress, firstName, lastName, gender);
		this.birthdate = birthdate;
		calculateAge();
	}

	public Calendar getBirthdate() {
		return birthdate;
	}

	public String getBirthdateStr() {
		birthdateStr = new SimpleDateFormat("yyyy-MM-dd").format(birthdate
				.getTime());
		return birthdateStr;
	}

	public int getAge() {
		return age;
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
		String str = "Nurse:\n";
		str += super.toString();
		str += "Age:\t\t" + age + "\n";
		str += "Birthdate:\t" + birthdate.toString() + "\n";
		return str;
	}

}
