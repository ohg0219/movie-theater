package controller.vo;

public class TikectingVO {
	private String movieImage;
	private int movieNum;
	private String movieName;
	private String movieRating;
	private String theaterRegion;
	private int theaterNum;
	private String theaterName;
	private int gwanNum;
	private String gwanName;
	private String movieStartday;
	private String movieEndday;
	private String movieDayschedule;
	private int gwanSeatcount;
	private String gwanSeat;
	
	public TikectingVO(String movieImage,int movieNum, String movieName, String movieRating, String theaterRegion, int theaterNum,
			String theaterName, int gwanNum, String gwanName, String movieStartday, String movieEndday,
			String movieDayschedule, int gwanSeatcount, String gwanSeat) {
		this.movieImage = movieImage;
		this.movieNum = movieNum;
		this.movieName = movieName;
		this.movieRating = movieRating;
		this.theaterRegion = theaterRegion;
		this.theaterNum = theaterNum;
		this.theaterName = theaterName;
		this.gwanNum = gwanNum;
		this.gwanName = gwanName;
		this.movieStartday = movieStartday;
		this.movieEndday = movieEndday;
		this.movieDayschedule = movieDayschedule;
		this.gwanSeatcount = gwanSeatcount;
		this.gwanSeat = gwanSeat;
	}
	public String getMovieImage() {
		return movieImage;
	}
	public void setMovieImage(String movieImage) {
		this.movieImage = movieImage;
	}
	public int getMovieNum() {
		return movieNum;
	}
	public void setMovieNum(int movieNum) {
		this.movieNum = movieNum;
	}
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	public String getMovieRating() {
		return movieRating;
	}
	public void setMovieRating(String movieRating) {
		this.movieRating = movieRating;
	}
	public String getTheaterRegion() {
		return theaterRegion;
	}
	public void setTheaterRegion(String theaterRegion) {
		this.theaterRegion = theaterRegion;
	}
	public int getTheaterNum() {
		return theaterNum;
	}
	public void setTheaterNum(int theaterNum) {
		this.theaterNum = theaterNum;
	}
	public String getTheaterName() {
		return theaterName;
	}
	public void setTheaterName(String theaterName) {
		this.theaterName = theaterName;
	}
	public int getGwanNum() {
		return gwanNum;
	}
	public void setGwanNum(int gwanNum) {
		this.gwanNum = gwanNum;
	}
	public String getGwanName() {
		return gwanName;
	}
	public void setGwanName(String gwanName) {
		this.gwanName = gwanName;
	}
	public String getMovieStartday() {
		return movieStartday;
	}
	public void setMovieStartday(String movieStartday) {
		this.movieStartday = movieStartday;
	}
	public String getMovieEndday() {
		return movieEndday;
	}
	public void setMovieEndday(String movieEndday) {
		this.movieEndday = movieEndday;
	}
	public String getMovieDayschedule() {
		return movieDayschedule;
	}
	public void setMovieDayschedule(String movieDayschedule) {
		this.movieDayschedule = movieDayschedule;
	}
	public int getGwanSeatcount() {
		return gwanSeatcount;
	}
	public void setGwanSeatcount(int gwanSeatcount) {
		this.gwanSeatcount = gwanSeatcount;
	}
	public String getGwanSeat() {
		return gwanSeat;
	}
	public void setGwanSeat(String gwanSeat) {
		this.gwanSeat = gwanSeat;
	}
	
	
	
}
