package controller.user;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import controller.ConnUtil;
import controller.vo.MovieVO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MovieInfo implements Initializable {

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	@FXML GridPane movieList;
	@FXML Label userId;
	@FXML Button backGo;
	@FXML TextField searchTf;
	@FXML ComboBox<String> searchGenre;
	@FXML Button searchBt;
	@FXML Button resetBt;
	
	
	
	private String id;
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		searchGenre.getSelectionModel().select(0);
		movieList();
	}
	/**
	 * 영화포스터 클릭시 상세정보 다이얼로그
	 * @param movie
	 */
	public void movieInfoDialog(MovieVO movie) {
		try {
			Stage dialog = new Stage(StageStyle.UNDECORATED);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(movieList.getScene().getWindow());
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/user/MovieInfoDialog.fxml"));
			ImageView imageView = (ImageView) root.lookup("#Dimage");
			Label name = (Label) root.lookup("#Dname");
			Label genre = (Label) root.lookup("#Dgenre");
			Label date = (Label) root.lookup("#Ddate");
			Label time = (Label) root.lookup("#Dtime");
			Label rating = (Label) root.lookup("#Drating");
			Label director = (Label) root.lookup("#Ddirector");
			Label actor = (Label) root.lookup("#Dactor");
			Button ticketing = (Button) root.lookup("#DticketingBt");
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			if(movie.getImage()!=null) {
				try {
					File jpgImage = new File(movie.getImage());
					fis = new FileInputStream(jpgImage);
					bis = new BufferedInputStream(fis);
					Image img = new Image(bis);
					imageView.setImage(img);
				} catch (FileNotFoundException e) {
					imageView.setImage(null);
				} catch (NullPointerException e2) {
				} finally {
						try {if(bis!=null)bis.close();} catch (IOException e) {e.printStackTrace();	}
						try {if(fis!=null)fis.close();} catch (IOException e) {e.printStackTrace();	}
				}
			} else {
				imageView.setImage(null);
			}
			name.setText(movie.getName());
			genre.setText(movie.getGenre());
			date.setText(movie.getDate());
			time.setText(movie.getTime()+"분");
			director.setText(movie.getDirector());
			actor.setText(movie.getActor());
			rating.setText(movie.getRating());
			ticketing.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/Ticketing.fxml"));
						Parent root = loader.load();
						Ticketing umc = loader.getController();
						umc.userId.setText(userId.getText());
						Scene scene = new Scene(root);
						Stage stage = new Stage();
						stage.setTitle("예매하기");
						stage.centerOnScreen();
						stage.setScene(scene);
						Stage beforeStage = (Stage) userId.getScene().getWindow();
						beforeStage.close();
						stage.show();
						umc.selectMovie(name.getText());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			Button close = (Button) root.lookup("#Dclose");
			close.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					dialog.close();
				}
			});
			Scene scene = new Scene(root);
			dialog.setScene(scene);
			dialog.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 영화목록 로드
	 * @param rs
	 */
	public void makeList(ResultSet rs) {
		try {
			int row = 0;
			int col = 0;
			while(rs.next()) {
				int num = rs.getInt("movie_num");
				String name = rs.getString("movie_name");
				String genre = rs.getString("movie_genre");
				String director = rs.getString("movie_director");
				String actor = rs.getString("movie_actor");
				String date;
				if(rs.getDate("movie_date")==null) {
					date = null;
				}else {
					date = rs.getDate("movie_date").toString();
				}
				int time = rs.getInt("movie_time");
				String image = rs.getString("movie_image");
				String rating = rs.getString("movie_rating");
				MovieVO movieVO = new MovieVO(num, name, genre, director, actor, date, time, image, rating);
				VBox vBox = new VBox();
				vBox.setMaxHeight(400);
				vBox.setPrefWidth(300);
				vBox.setAlignment(Pos.CENTER);
				ImageView imageView = new ImageView();
				imageView.setFitHeight(260);
				imageView.setPickOnBounds(true);
				imageView.setPreserveRatio(true);
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				if(rs.getString("movie_image")!=null) {
					try {
						File jpgImage = new File(rs.getString("movie_image"));
						fis = new FileInputStream(jpgImage);
						bis = new BufferedInputStream(fis);
						Image img = new Image(bis);
						imageView.setImage(img);
					} catch (FileNotFoundException e) {
						imageView.setImage(null);
					} catch (NullPointerException e2) {
					} finally {
							try {if(bis!=null)bis.close();} catch (IOException e) {e.printStackTrace();	}
							try {if(fis!=null)fis.close();} catch (IOException e) {e.printStackTrace();	}
					}
				} else {imageView.setImage(null);}
				imageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
					@Override public void handle(MouseEvent event) {imageView.setCursor(Cursor.HAND);}});
				imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override public void handle(MouseEvent event) {movieInfoDialog(movieVO);}});
				Label label = new Label(rs.getString("movie_name"));
				label.setStyle("-fx-font-size:12pt;");
				Button button = new Button("예매하기");
				button.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						try {
							FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/Ticketing.fxml"));
							Parent root = loader.load();
							Ticketing umc = loader.getController();
							VBox vBox = (VBox) button.getParent();
							Label label = (Label) vBox.getChildren().get(1);
							umc.userId.setText(userId.getText());
							Scene scene = new Scene(root);
							Stage stage = new Stage();
							stage.setTitle("예매하기");
							stage.centerOnScreen();
							stage.setScene(scene);
							Stage beforeStage = (Stage) button.getScene().getWindow();
							beforeStage.close();
							stage.show();
							umc.selectMovie(label.getText());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				vBox.getChildren().addAll(imageView,label,button);
				movieList.add(vBox, col, row);
				GridPane.setMargin(vBox, new Insets(10));
				col++;
				if(col==3) { row++; col=0; }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
	}
	
	
	/**
	 * 초기 영화목록 노출
	 */
	public void movieList() {
			try {
				conn = ConnUtil.getConnection();
				String sql = "select * from movie where movie_num>0 and movie_date is not null";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				makeList(rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * 영화 검색
	 * @param event
	 */
	@FXML public void searchAction(ActionEvent event) {
		movieList.getChildren().clear();
		try {
			conn = ConnUtil.getConnection();
			String sql ="";
			if(searchGenre.getSelectionModel().getSelectedItem().equals("전체")) {
				//장르 전체
				if(searchTf.getText().trim().length()==0) {
					//검색어없음
					movieList();
					return;
				}else {
					//검색어있음
					sql = "select * from movie where movie_num>0 and movie_date is not null and movie_name like ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, "%"+searchTf.getText()+"%");
					rs = pstmt.executeQuery();
				}
			}else {
				//장르 선택
				if(searchTf.getText().trim().length()==0) {
					//검색어없음
					sql = "select * from movie where movie_num>0 and movie_date is not null and movie_genre=?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, searchGenre.getSelectionModel().getSelectedItem());
					rs = pstmt.executeQuery();
				}else {
					//검색어있음
					sql = "select * from movie where movie_num>0 and movie_date is not null and movie_genre=? and movie_name like ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, searchGenre.getSelectionModel().getSelectedItem());
					pstmt.setString(2, searchTf.getText());
					rs = pstmt.executeQuery();
				}
			}//end 검색조건
			makeList(rs);
		} catch (SQLException e) {
			
		} 
	}
	
	/**
	 * 영화검색 초기화
	 * @param event
	 */
	@FXML public void resetAction(ActionEvent event) {
		movieList.getChildren().clear();
		searchGenre.getSelectionModel().select(0);
		searchTf.clear();
		movieList();
	}
	/**
	 * 뒤로가기
	 * @param event
	 */
	@FXML public void backGo(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/UserMain.fxml"));
			Parent root = loader.load();
			UserMain umc = loader.getController();
			umc.userId.setText(this.userId.getText());
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle(this.userId.getText()+"님 환영합니다.");
			stage.centerOnScreen();
			stage.setScene(scene);
			Stage beforeStage = (Stage) backGo.getScene().getWindow();
			beforeStage.close();
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
