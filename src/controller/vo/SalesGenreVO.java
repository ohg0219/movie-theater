package controller.vo;

import javafx.beans.property.SimpleStringProperty;

public class SalesGenreVO {
	private SimpleStringProperty genre;
	private SimpleStringProperty money;
	private SimpleStringProperty percent;
	
	public SalesGenreVO(String genre, String money, String percent) {
		this.genre = new SimpleStringProperty(genre);
		this.money = new SimpleStringProperty(money);
		this.percent = new SimpleStringProperty(percent);
	}
	public String getGenre() {
		return genre.get();
	}
	public void setGenre(String genre) {
		this.genre.set(genre);
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
