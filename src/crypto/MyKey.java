package crypto;

import java.util.List;

//Klasse stellt einen Schlüssel dar mit seinen wesentlichen Informationen 
public class MyKey {

	private int id;
	private String filepath;
	private int user_id;
	private long currentDate;
	private String description;
	private String keytype;
	private String keypart;

	public MyKey()
	{
		
	}
	public MyKey(int user_id, long currentDate, String description, String filepath, String keytype, String keypart) {
		this.user_id=user_id;
		this.currentDate=currentDate;
		this.description=description;
		this.filepath=filepath;
		this.keytype=keytype;
		this.keypart=keypart;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	public String getFilepath() {
		return filepath;
	}

	public int getUser_id() {
		return user_id;
	}

	public long getCurrentDate() {
		return currentDate;
	}

	public String getDescription() {
		return description;
	}

	public String getKeytype() {
		return keytype;
	}

	public String getKeypart() {
		return keypart;
	}
	
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public void setCurrentDate(long currentDate) {
		this.currentDate = currentDate;
	}

}
