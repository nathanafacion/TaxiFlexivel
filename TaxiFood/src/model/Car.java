package model;

public class Car {
	private String licensePlat;
	private String color;
	private String model;
	
	
	
	public Car(String licensePlat, String color, String model) {
		if (licensePlat == null || licensePlat.isEmpty()) {
			throw new IllegalArgumentException("licensePlat cannot be null or empty.");
		}
		
		if (color == null || color.isEmpty()) {
			throw new IllegalArgumentException("color cannot be null or empty.");
		}
		
		if (model == null || model.isEmpty()) {
			throw new IllegalArgumentException("model cannot be null or empty.");
		}
		
		this.licensePlat = licensePlat;
		this.color = color;
		this.model = model;
	}
	public String getLicensePlat() {
		return licensePlat;
	}
	public void setLicensePlat(String licensePlat) {
		this.licensePlat = licensePlat;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	@Override
	public String toString() {
		return "Car [licensePlat=" + licensePlat + ", color=" + color + ", model=" + model + "]";
	}
	
	

}
