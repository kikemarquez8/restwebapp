package data_models;

/**
 * Created by Bonsanto on 2/7/2015.
 */
public class Client {
	private int id, ci;
	private String firstMame, lastName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCi() {
		return ci;
	}

	public void setCi(int ci) {
		this.ci = ci;
	}

	public String getFirstMame() {
		return firstMame;
	}

	public void setFirstMame(String firstMame) {
		this.firstMame = firstMame;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
