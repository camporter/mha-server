package myhomeaudio.server.database.field;

public class NumericalDatabaseField extends DatabaseField {

	public NumericalDatabaseField(String fieldName, Integer fieldValue) {
		super(fieldName, fieldValue);
		// TODO Auto-generated constructor stub
	}

	public NumericalDatabaseField(String fieldName, Double fieldValue) {
		super(fieldName, fieldValue);
	}

	public NumericalDatabaseField(String fieldName, Float fieldValue) {
		super(fieldName, fieldValue);
	}

	public void setFieldValue(Integer val) {
		this.fieldValue = val;
	}

	public void setFieldValue(Double val) {
		this.fieldValue = val;
	}

	public void setFieldValue(Float val) {
		this.fieldValue = val;
	}
}
