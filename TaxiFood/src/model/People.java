package model;

public class People {
	private String name;
	private String telephone;
	private Localization originlocalization;

	
	public People(String name, String telephone,Localization localization) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Name cannot be null or empty.");
		}
		if(telephone == null || telephone.isEmpty()) {
			throw new IllegalArgumentException("Telephone cannot be null or empty.");
		}

		if(localization == null) {
			throw new IllegalArgumentException("Localization cannot be null.");
		}
		
		verificationTelephone(telephone);
		
		this.name = name;
		this.telephone = telephone;
		this.originlocalization = localization;
	}
	
	public void verificationTelephone(String telephone) {
		for (int i = 0; i < telephone.length(); i++) {
			if (Character.isDigit(telephone.charAt(i)) == false){
				throw new IllegalArgumentException("Telephone cannot be letter.");
	        }
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Localization getLocalization() {
		return originlocalization;
	}
	public void setLocalization(Localization localization) {
		this.originlocalization = localization;
	}
	
}
