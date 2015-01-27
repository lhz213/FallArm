package webServer.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Record {
	private final static int TEMP_RECORD_ID = -101;
	public final static int ACCELEROMETER_X_INDEX = 0;
	public final static int ACCELEROMETER_Y_INDEX = 1;
	public final static int ACCELEROMETER_Z_INDEX = 2;
	public final static int GYROSCOPE_X_INDEX = 3;
	public final static int GYROSCOPE_Y_INDEX = 4;
	public final static int GYROSCOPE_Z_INDEX = 5;
	public final static int LONGITUDE_INDEX = 6;
	public final static int LATITUDE_INDEX = 7;
	public final static int RECORD_TAG_UNKNOW = -1;
	public final static int RECORD_TAG_FALL = 1;
	public final static int RECORD_TAG_MAYBE = 2;
	public final static int RECORD_TAG_NORMAL = 3;

	private int recordId;
	private int patientId;
	private Calendar datetime;
	private String datetimeStr;
	private double[] data = new double[8];
	private int state;

	public Record() {
	}

	public Record(int recordId, int patientId, Calendar datetime, double acc_x,
			double acc_y, double acc_z, double gyr_x, double gyr_y,
			double gyr_z, double longitude, double latitude, int state) {
		this.recordId = recordId;
		this.patientId = patientId;
		this.datetime = datetime;
		this.data[0] = acc_x;
		this.data[1] = acc_y;
		this.data[2] = acc_z;
		this.data[3] = gyr_x;
		this.data[4] = gyr_y;
		this.data[5] = gyr_z;
		this.data[6] = longitude;
		this.data[7] = latitude;
		this.state = state;
	}

	public Record(int recordId, int patientId, Calendar datetime,
			double[] data, int state) {
		this.recordId = recordId;
		this.patientId = patientId;
		this.datetime = datetime;
		this.data = data;
		this.state = state;
	}

	public int getRecordId() {
		return recordId;
	}

	public int getPatientId() {
		return patientId;
	}

	public Calendar getDatetime() {
		return datetime;
	}

	public String getDatetimeStr() {
		datetimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(datetime.getTime());
		return datetimeStr;
	}

	public double[] getData() {
		return data;
	}

	public int getState() {
		return state;
	}

	public static Record parseRecord(String str) {
		String data[] = str.split(",");
		if (data.length != 9) {
			System.out.println("Bad record!");
			return null;
		}
		Record record;
		Calendar now = Calendar.getInstance();
		try {
			int patientId = Integer.parseInt(data[0]);
			double acc_x = Double.parseDouble(data[1]);
			double acc_y = Double.parseDouble(data[2]);
			double acc_z = Double.parseDouble(data[3]);
			double gyr_x = Double.parseDouble(data[4]);
			double gyr_y = Double.parseDouble(data[5]);
			double gyr_z = Double.parseDouble(data[6]);
			double longitude = Double.parseDouble(data[7]);
			double latitude = Double.parseDouble(data[8]);
			// System.out.println("Record:\npid:\t" + patientId);
			// System.out.println("acc:\t" + acc_x + "\t" + acc_y + "\t" +
			// acc_z);
			// System.out.println("gyr:\t" + gyr_x + "\t" + gyr_y + "\t" +
			// gyr_z);
			// System.out.println("location:\t" + longitude + "\t" + latitude);
			record = new Record(TEMP_RECORD_ID, patientId, now, acc_x, acc_y,
					acc_z, gyr_x, gyr_y, gyr_z, longitude, latitude,
					RECORD_TAG_UNKNOW);
			return record;
		} catch (Exception ex) {
			System.out.println("Record foramt issue");
			ex.printStackTrace();
			return null;
		}
	}
}
