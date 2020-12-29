package controller.vo;

import javafx.beans.property.SimpleStringProperty;

public class SalesRegionVO {
	private SimpleStringProperty region;
	private SimpleStringProperty money;
	private SimpleStringProperty percent;
	
	public SalesRegionVO(String region, String money, String percent) {
		this.region = new SimpleStringProperty(region);
		this.money = new SimpleStringProperty(money);
		this.percent = new SimpleStringProperty(percent);
	}
	public String getRegion() {
		return region.get();
	}
	public void setRegion(String region) {
		this.region.set(region);
	}
	public String getPercent() {
		return percent.get();
	}
	public void setPercent(String percent) {
		this.percent.set(percent);
	}
	public String getMoney() {
		return money.get();
	}
	public void setMoney(String money) {
		this.money.set(money);
	}
	
	
}
