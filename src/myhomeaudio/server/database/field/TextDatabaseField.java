package myhomeaudio.server.database.field;

public class TextDatabaseField extends DatabaseField {

	public TextDatabaseField(String fieldName, String fieldValue) {
		super(fieldName, fieldValue);
	}

	public void setFieldValue(String val) {
		this.fieldValue = val;
	}

}
