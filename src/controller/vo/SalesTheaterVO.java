package controller.vo;

import javafx.beans.property.SimpleStringProperty;

public class SalesTheaterVO {
	private SimpleStringProperty region;
	private SimpleStringProperty money;
	private SimpleStringProperty theater;
	
	public SalesTheaterVO(String region, String money, String theater) {
		this.region = new SimpleStringProperty(region);
		this.money = new SimpleStringProperty(money);
		this.theater = new SimpleStringProperty(theater);
	}
	public String getRegion() {
		return region.get();
	}
	public void setRegion(String region) {
		this.region.set(region);
	}
	public String getTheater() {
		return theater.get();
	}
	public void setTheater(String theater) {
		this.theater.set(theater);
	}
	public String getMoney() {
		return money.get();
	}
	public void setMoney(String money) {
		this.money.set(money);
	}
}
