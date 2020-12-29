package controller.vo;

public class SalesDownloadVO {
	private String ticketDate;
	private String ticketTime;
	private String userId;
	private String movieName;
	private String movieGenre;
	private String movieRating;
	private String theaterRegion;
	private String theaterName;
	private String gwanName;
	private String ticketSeat;
	private int ticketMoney;
	private String ticketPayment;
	private String ticketCancel;
	
	public SalesDownloadVO(String ticketDate, String ticketTime, String userId, String movieName, String movieGenre,
			String movieRating, String theaterRegion, String theaterName, String gwanName, String ticketSeat,
			int ticketMoney, String ticketPayment, String ticketCancel) {
		this.ticketDate = ticketDate;
		this.ticketTime = ticketTime;
		this.userId = userId;
		this.movieName = movieName;
		this.movieGenre = movieGenre;
		this.movieRating = movieRating;
		this.theaterRegion = theaterRegion;
		this.theaterName = theaterName;
		this.gwanName = gwanName;
		this.ticketSeat = ticketSeat;
		this.ticketMoney = ticketMoney;
		this.ticketPayment = ticketPayment;
		this.ticketCancel = ticketCancel;
	}
	public String getTicketDate() {
		return ticketDate;
	}
	public void setTicketDate(String ticketDate) {
		this.ticketDate = ticketDate;
	}
	public String getTicketTime() {
		return ticketTime;
	}
	public void setTicketTime(String ticketTime) {
		this.ticketTime = ticketTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	public String getMovieGenre() {
		return movieGenre;
	}
	public void setMovieGenre(String movieGenre) {
		this.movieGenre = movieGenre;
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
	public String getTheaterName() {
		return theaterName;
	}
	public void setTheaterName(String theaterName) {
		this.theaterName = theaterName;
	}
	public String getGwanName() {
		return gwanName;
	}
	public void setGwanName(String gwanName) {
		this.gwanName = gwanName;
	}
	public String getTicketSeat() {
		return ticketSeat;
	}
	public void setTicketSeat(String ticketSeat) {
		this.ticketSeat = ticketSeat;
	}
	public int getTicketMoney() {
		return ticketMoney;
	}
	public void setTicketMoney(int ticketMoney) {
		this.ticketMoney = ticketMoney;
	}
	public String getTicketPayment() {
		return ticketPayment;
	}
	public void setTicketPayment(String ticketPayment) {
		this.ticketPayment = ticketPayment;
	}
	public String getTicketCancel() {
		return ticketCancel;
	}
	public void setTicketCancel(String ticketCancel) {
		this.ticketCancel = ticketCancel;
	}
	
	
}
