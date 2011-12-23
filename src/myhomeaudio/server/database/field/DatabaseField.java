package myhomeaudio.server.database.field;

/**
 * Defines the field in a schema.
 * Stores its name and type as a DatabaseFieldType.
 * 
 * @author Cameron
 *
 */
public class DatabaseField {
	private String fieldName;
	private Object fieldValue;
	
	public DatabaseField(String fieldName, Object fieldValue)
	{
		this.fieldName = fieldName;
		this.fieldType = fieldType;
	}
	
	/**
	 * Gets the type of the value stored.
	 * @return
	 */
	public Class getType()
	{
		return this.fieldValue.getClass();
	}
}
