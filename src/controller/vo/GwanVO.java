package controller.vo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class GwanVO {
	private SimpleStringProperty region;
	private SimpleIntegerProperty theaterNum;
	private SimpleStringProperty theaterName;
	private SimpleIntegerProperty gwanNum;
	private SimpleStringProperty gwanName;
	private SimpleIntegerProperty gwanSeatCount;
	private SimpleStringProperty gwanSeat;
	
	public GwanVO(String region, int theaterNum, String theaterName,
			int gwanNum, String gwanName, int gwanSeatCount, String gwanSeat) {
		super();
		this.region = new SimpleStringProperty(region);
		this.theaterNum = new SimpleIntegerProperty(theaterNum);
		this.theaterName = new SimpleStringProperty(theaterName);
		this.gwanNum = new SimpleIntegerProperty(gwanNum);
		this.gwanName = new SimpleStringProperty(gwanName);
		this.gwanSeatCount = new SimpleIntegerProperty(gwanSeatCount);
		this.gwanSeat = new SimpleStringProperty(gwanSeat);
	}
	public String getRegion() {
		return region.get();
	}
	public void setRegion(String region) {
		this.region.set(region);;
	}
	public int getTheaterNum() {
		return theaterNum.get();
	}
	public void setTheaterNum(int theaterNum) {
		this.theaterNum.set(theaterNum);;
	}
	public String getTheaterName() {
		return theaterName.get();
	}
	public void setTheaterName(String theaterName) {
		this.theaterName.set(theaterName);;
	}
	public int getGwanNum() {
		return gwanNum.get();
	}
	public void setGwanNum(int gwanNum) {
		this.gwanNum.set(gwanNum);;
	}
	public String getGwanName() {
		return gwanName.get();
	}
	public void setGwanName(String gwanName) {
		this.gwanName.set(gwanName);;
	}
	public int getGwanSeatCount() {
		return gwanSeatCount.get();
	}
	public void setGwanSeatCount(int gwanSeatCount) {
		this.gwanSeatCount.set(gwanSeatCount);
	}
	public String getGwanSeat() {
		return gwanSeat.get();
	}
	public void setGwanSeat(String gwanSeat) {
		this.gwanSeat.set(gwanSeat);
	}
	
	
	
}
