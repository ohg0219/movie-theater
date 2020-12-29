package controller.vo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TheaterVO {
	private SimpleIntegerProperty num;
	private SimpleStringProperty region;
	private SimpleStringProperty name;
	private SimpleIntegerProperty count;
	
	public TheaterVO(int num, String region, String name,
			int count) {
		super();
		this.num = new SimpleIntegerProperty(num);
		this.region = new SimpleStringProperty(region);
		this.name = new SimpleStringProperty(name);
		this.count = new SimpleIntegerProperty(count);
	}
	public int getNum() {
		return num.get();
	}
	public void setNum(int num) {
		this.num.set(num);
	}
	public String getRegion() {
		return region.get();
	}
	public void setRegion(String region) {
		this.region.set(region);
	}
	public String getName() {
		return name.get();
	}
	public void setName(String name) {
		this.name.set(name);
	}
	public int getCount() {
		return count.get();
	}
	public void setCount(int count) {
		this.count.set(count);
	}
	
	
}
