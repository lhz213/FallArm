package webServer.exceptions;

public class FallArmDbFailure extends Exception {
	private static final long serialVersionUID = 1L;
	public static final int STMT_FAILED = 0;
	public static final int PATIENT_TABLE_EMPTY = 1;
	public static final int NURSE_TABLE_EMPTY = 2;
	public static final int CONTACT_TABLE_EMPTY = 3;
	public static final int RECORD_TABLE_EMPTY = 4;
	public static final int NO_PATIENT = 5;
	public static final int NO_NURSE = 6;
	public static final int PATIENT_NO_RECORD = 7;
	public static final int DATE_FORMAT = 8;
	public static final int RETRY = 9;
	public static final int RETRY_LIMIT_EXCEEDED = 10;

	private int failureReason;

	public FallArmDbFailure(int failureReason) {
		this.failureReason = failureReason;
	}

	public FallArmDbFailure(int failureReason, String msg) {
		super(msg);
		this.failureReason = failureReason;
	}

	public int getFailureReason() {
		return failureReason;
	}

	public String getReasonStr() {
		switch (failureReason) {
		case STMT_FAILED:
			return "Failure Executing Statement";
		case PATIENT_TABLE_EMPTY:
			return "There is no patients";
		case NURSE_TABLE_EMPTY:
			return "There is no nurses";
		case CONTACT_TABLE_EMPTY:
			return "There is no contacts";
		case RECORD_TABLE_EMPTY:
			return "There is no records";
		case NO_PATIENT:
			return "No patient found";
		case NO_NURSE:
			return "No nurse found";
		case PATIENT_NO_RECORD:
			return "No record found";
		case DATE_FORMAT:
			return "Date format is incorrect";
		case RETRY:
			return "Operation rolled back, retry";
		case RETRY_LIMIT_EXCEEDED:
			return "Multiple attempts to perform the statement failed";
		default:
			return "Unknown Reason";
		}
	}

}
