package myhomeaudio.server.database.field;

/**
 * Defines the field in a schema. Stores its name and type as a
 * DatabaseFieldType.
 * 
 * @author Cameron
 * 
 */
public class DatabaseField {

	protected String fieldName;
	protected Object fieldValue;

	public DatabaseField(String fieldName, Object fieldValue) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	/**
	 * Gets the type of the value stored.
	 * 
	 * @return
	 */
	public Class getType() {
		return this.fieldValue.getClass();
	}

	/**
	 * Get the value of the field.
	 * 
	 * @return The field's value as a string
	 */
	public String getFieldValue() {
		return this.fieldValue.toString();
	}

	/**
	 * Get the name of the field.
	 * 
	 * @return The field's name
	 */
	public String getFieldName() {
		return this.fieldName;
	}
}
