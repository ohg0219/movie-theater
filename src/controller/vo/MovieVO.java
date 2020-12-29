package controller.vo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MovieVO {
	private SimpleIntegerProperty num;
	private SimpleStringProperty name;
	private SimpleStringProperty genre;
	private SimpleStringProperty director;
	private SimpleStringProperty actor;
	private SimpleStringProperty date;
	private SimpleIntegerProperty time;
	private SimpleStringProperty image;
	private SimpleStringProperty rating;
	
	
	public MovieVO(int num, String name, String genre,
			String director, String actor, String date,
			int time,String image, String rating) {
		super();
		this.num = new SimpleIntegerProperty(num);
		this.name = new SimpleStringProperty(name);
		this.genre = new SimpleStringProperty(genre);
		this.director = new SimpleStringProperty(director);
		this.actor = new SimpleStringProperty(actor);
		this.date = new SimpleStringProperty(date);
		this.time = new SimpleIntegerProperty(time);
		this.image = new SimpleStringProperty(image);
		this.rating = new SimpleStringProperty(rating);
	}
	public String getRating() {
		return rating.get();
	}
	public void setRating(String rating) {
		this.rating.set(rating);
	}
	public String getImage() {
		return image.get();
	}
	public void setImage(String image) {
		this.image.set(image);
	}
	public int getNum() {
		return num.get();
	}
	public void setNum(int num) {
		this.num.set(num);
	}
	public String getName() {
		return name.get();
	}
	public void setName(String name) {
		this.name.set(name);
	}
	public String getGenre() {
		return genre.get();
	}
	public void setGenre(String genre) {
		this.genre.set(genre);
	}
	public String getDirector() {
		return director.get();
	}
	public void setDirector(String director) {
		this.director.set(director);
	}
	public String getActor() {
		return actor.get();
	}
	public void setActor(String actor) {
		this.actor.set(actor);
	}
	public String getDate() {
		return date.get();
	}
	public void setDate(String date) {
		this.date.set(date);
	}
	public int getTime() {
		return time.get();
	}
	public void setTime(int time) {
		this.time.set(time);
	}
	
	
}
