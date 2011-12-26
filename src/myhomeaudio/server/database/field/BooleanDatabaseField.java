package myhomeaudio.server.database.field;

public class BooleanDatabaseField extends DatabaseField {
	
	public BooleanDatabaseField(String fieldName, Boolean fieldValue) {
		super(fieldName, fieldValue);
	}
	
	public void setFieldValue(Boolean val) {
		this.fieldValue = val;
	}
}
