package data_models;

/**
 * Created by Bonsanto on 2/7/2015.
 */
public class Supplier {
	private int id, zip; //Zip should have its own dataType but who cares.
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
