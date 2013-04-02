package iaws.carpooling.domain.nomenclature;

public class Mail {
	
	private String name;
	private String domain;
	
	public Mail(String name, String domain) {
		this.name = name;;
		this.domain = domain;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public String toString() {
		return name + "@" + domain;
	}

}
