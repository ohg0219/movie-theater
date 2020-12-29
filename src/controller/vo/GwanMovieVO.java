package controller.vo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class GwanMovieVO {
	private SimpleStringProperty region;
	private SimpleIntegerProperty theaterNum;
	private SimpleStringProperty theaterName;
	private SimpleIntegerProperty gwanNum;
	private SimpleStringProperty gwanName;
	private SimpleIntegerProperty movieNum;
	private SimpleStringProperty movieName;
	private SimpleStringProperty startDay;
	private SimpleStringProperty endDay;
	private SimpleStringProperty daySchedule;
	
	public GwanMovieVO(String region, int theaterNum, String theaterName,
			int gwanNum, String gwanName, int movieNum,
			String movieName, String startDay, String endDay,
			String daySchedule) {
		this.region = new SimpleStringProperty(region);
		this.theaterNum = new SimpleIntegerProperty(theaterNum);
		this.theaterName = new SimpleStringProperty(theaterName);
		this.gwanNum = new SimpleIntegerProperty(gwanNum);
		this.gwanName = new SimpleStringProperty(gwanName);
		this.movieNum = new SimpleIntegerProperty(movieNum);
		this.movieName = new SimpleStringProperty(movieName);
		this.startDay = new SimpleStringProperty(startDay);
		this.endDay = new SimpleStringProperty(endDay);
		this.daySchedule = new SimpleStringProperty(daySchedule);
	}
	public String getRegion() {
		return region.get();
	}
	public void setRegion(String region) {
		this.region.set(region);
	}
	public int getTheaterNum() {
		return theaterNum.get();
	}
	public void setTheaterNum(int theaterNum) {
		this.theaterNum.set(theaterNum);
	}
	public String getTheaterName() {
		return theaterName.get();
	}
	public void setTheaterName(String theaterName) {
		this.theaterName.set(theaterName);
	}
	public int getGwanNum() {
		return gwanNum.get();
	}
	public void setGwanNum(int gwanNum) {
		this.gwanNum.set(gwanNum);
	}
	public String getGwanName() {
		return gwanName.get();
	}
	public void setGwanName(String gwanName) {
		this.gwanName.set(gwanName);
	}
	public int getMovieNum() {
		return movieNum.get();
	}
	public void setMovieNum(int movieNum) {
		this.movieNum.set(movieNum);
	}
	public String getMovieName() {
		return movieName.get();
	}
	public void setMovieName(String movieName) {
		this.movieName.set(movieName);
	}
	public String getStartDay() {
		return startDay.get();
	}
	public void setStartDay(String startDay) {
		this.startDay.set(startDay);
	}
	public String getEndDay() {
		return endDay.get();
	}
	public void setEndDay(String endDay) {
		this.endDay.set(endDay);
	}
	public String getDaySchedule() {
		return daySchedule.get();
	}
	public void setDaySchedule(String daySchedule) {
		this.daySchedule.set(daySchedule);
	}
	
	
	
	
}
