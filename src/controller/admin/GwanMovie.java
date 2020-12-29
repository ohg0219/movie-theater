package controller.admin;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import controller.ConnUtil;
import controller.vo.GwanMovieVO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GwanMovie implements Initializable {

	
	
	@FXML Label userId;
	@FXML Button backGo;
	@FXML TextField theaterNumtf;
	@FXML TextField theaterNametf;
	@FXML TextField theaterRegiontf;
	@FXML TextField gwanNumtf;
	@FXML TextField gwanNametf;
	@FXML Button modifyBt;
	@FXML ComboBox<String> searchRegion;
	@FXML Button searchBt;
	@FXML Button resetBt;
	@FXML ComboBox<String> searchSelect;
	@FXML TextField searchTf;
	@FXML DatePicker startDate;
	@FXML DatePicker endDate;
	@FXML ComboBox<String> selectMovie;
	@FXML ComboBox<String> dayCount;
	@FXML GridPane daySchedule;
	@FXML TableView<GwanMovieVO> listTable;
	@FXML TableColumn<GwanMovieVO, String> region;
	@FXML TableColumn<GwanMovieVO, String> theaterName;
	@FXML TableColumn<GwanMovieVO, String> gwanName;
	@FXML TableColumn<GwanMovieVO, String> movieName;

	private String id;
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	ObservableList<GwanMovieVO> list;
	
	public void actionList() {
		searchTf.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				searchAction(event);
			}
		});
		dayCount.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				daySchedule.getChildren().clear();
				if(newValue==null) {return;}
				for(int i=0;i<Integer.parseInt(newValue);i++) {
					daySchedule.add(new Label((i+1)+"회차"), 0, i);
					TextField tf = new TextField();
					tf.setPromptText("시:분");
					daySchedule.add(tf, 1, i);
				}
			}
		});
		
		listTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					if(event.getClickCount()>1) {
						GwanMovieVO gwanMovie = listTable.getSelectionModel().getSelectedItem();
						theaterNumtf.setText(String.valueOf(gwanMovie.getTheaterNum()));
						theaterRegiontf.setText(gwanMovie.getTheaterName());
						theaterNametf.setText(gwanMovie.getTheaterName());
						gwanNumtf.setText(String.valueOf(gwanMovie.getGwanNum()));
						gwanNametf.setText(gwanMovie.getGwanName());
						selectMovie.getSelectionModel().select(gwanMovie.getMovieName());
						if(gwanMovie.getDaySchedule()!="") {
							String[] firstS = gwanMovie.getDaySchedule().split("=");
							dayCount.getSelectionModel().select(firstS[0]); //상영횟수
							String[] time = firstS[1].split("/"); //상영시간표
							int count =0;
							for(int i = 0;i<daySchedule.getChildren().size();i++) {
								if(i%2!=0) {
									TextField tf = (TextField) daySchedule.getChildren().get(i);
									String[] j = time[count].split(",");
									tf.setText(j[1]);
									count++;
								}
							}
						}else {
							dayCount.getSelectionModel().clearSelection();
							daySchedule.getChildren().clear();
						}
						try {
							startDate.setValue(LocalDate.parse(gwanMovie.getStartDay()));
							endDate.setValue(LocalDate.parse(gwanMovie.getEndDay()));
						}catch (Exception e) {
							startDate.setValue(null);
							endDate.setValue(null);
						}
					}
				}catch (Exception e) {
				}
			}
		});
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		theaterNumtf.setDisable(true);
		theaterNametf.setDisable(true);
		theaterRegiontf.setDisable(true);
		gwanNumtf.setDisable(true);
		gwanNametf.setDisable(true);
		searchRegion.getSelectionModel().select("전체");
		searchSelect.getSelectionModel().select("극장명");
		list = FXCollections.observableArrayList();
		region.setCellValueFactory(new PropertyValueFactory<>("region"));
		theaterName.setCellValueFactory(new PropertyValueFactory<>("theaterName"));
		gwanName.setCellValueFactory(new PropertyValueFactory<>("gwanName"));
		movieName.setCellValueFactory(new PropertyValueFactory<>("movieName"));
		ObservableList<String> dCount = dayCount.getItems();
		for(int i =1;i<=10;i++) {dCount.add(i+"");}
		listLoad();
		movieList();
		actionList();
	}
	
	/**
	 * 테이블 로드
	 */
	public void listLoad() {
		listTable.getItems().clear();
		try {
			conn = ConnUtil.getConnection();
			String sql = "select theater_num,theater_region,theater_name, " + 
					"gwan_num,gwan_name,movie_startday,movie_endday,movie_dayschedule, " + 
					"movie_num,movie_name " + 
					"from theater join gwan USING (theater_num) JOIN movie using(movie_num) "+ 
					"where gwan_seatcount>0";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String region = rs.getString("theater_region");
				int theaterNum = rs.getInt("theater_num");
				String theaterName = rs.getString("theater_name");
				int gwanNum = rs.getInt("gwan_num");
				String gwanName = rs.getString("gwan_name");
				int movieNum = rs.getInt("movie_num");
				String movieName = rs.getString("movie_name");
				String startDay;
				if(rs.getDate("movie_startday")==null) {
					startDay="";
				}else {
					startDay = rs.getDate("movie_startday").toString();
				}
				String endDay;
				if(rs.getDate("movie_endday")==null) {
					endDay = "";
				}else {
					endDay = rs.getDate("movie_endday").toString();
				}
				
				String daySchedule;
				if(rs.getString("movie_dayschedule")==null) {
					daySchedule ="";
				}else {
					daySchedule = rs.getString("movie_dayschedule");
				}
				
				list.add(new GwanMovieVO(region, theaterNum, theaterName, gwanNum, gwanName, movieNum, movieName, startDay, endDay, daySchedule));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		listTable.setItems(list);
	}//테이블 리스트 로딩끝
	
	
	public void clear() {
		theaterNumtf.clear();
		theaterRegiontf.clear();
		theaterNametf.clear();
		gwanNumtf.clear();
		gwanNametf.clear();
		startDate.setValue(null);
		endDate.setValue(null);
		dayCount.getSelectionModel().select(0);
	}
	
	
	/**
	 * 영화목록 로드
	 */
	public void movieList() {
		try {
			ObservableList<String> movieList = FXCollections.observableArrayList();
			conn = ConnUtil.getConnection();
			String sql = "select movie_name from movie where movie_date is not null order by movie_num desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String movie_name = rs.getString("movie_name");
				movieList.add(movie_name);
			}
			selectMovie.setItems(movieList);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
	}//영화목록 로드 끝
	
	/**
	 * 뒤로가기
	 * @param event
	 */
	@FXML public void backGo(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/TheaterMain.fxml"));
			Parent root = loader.load();
			TheaterMain umc = loader.getController();
			umc.userId.setText(this.userId.getText());
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("극장/상영관관리");
			stage.centerOnScreen();
			stage.setScene(scene);
			Stage beforeStage = (Stage) backGo.getScene().getWindow();
			beforeStage.close();
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 수정
	 * @param event
	 */
	@FXML public void modifyAction(ActionEvent event) {
		if(startDate.getValue()==null) {startDate.requestFocus(); return;}
		if(endDate.getValue()==null) {endDate.requestFocus(); return;}
		if(selectMovie.getSelectionModel().getSelectedItem().equals("없음")) {selectMovie.requestFocus(); return;}
		if(dayCount.getSelectionModel().getSelectedItem()==null) {dayCount.requestFocus();return;}
		for(int i =0; i<daySchedule.getChildren().size();i++) {
			if(i%2!=0) {
				TextField tf = (TextField) daySchedule.getChildren().get(i);
				if(tf.getText().trim().length()==0) {tf.requestFocus(); return;}
			}
		}
		String gwanNum = gwanNumtf.getText();
		String movieName = selectMovie.getSelectionModel().getSelectedItem();
		int movieNum = 0;
		try {
			conn = ConnUtil.getConnection();
			String sql = "select movie_num from movie where movie_name=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, movieName);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				movieNum = rs.getInt("movie_num");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		StringBuffer movieSchedule = new StringBuffer(); 
		movieSchedule.append(dayCount.getSelectionModel().getSelectedItem()+"=");
		for(int i = 0;i<daySchedule.getChildren().size();i++) {
			if(i%2==0) {
				Label label = (Label) daySchedule.getChildren().get(i);
				movieSchedule.append(label.getText()+",");
			}
			if(i%2!=0) {
				TextField textField = (TextField) daySchedule.getChildren().get(i);
				movieSchedule.append(textField.getText()+"/");
			}
		}
		
		try {
			conn = ConnUtil.getConnection();
			String sql = "update gwan set movie_num=?,movie_dayschedule=?,movie_Startday=?,movie_endday=?"
					+ "where gwan_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, movieNum);
			pstmt.setString(2, movieSchedule.toString());
			pstmt.setDate(3, Date.valueOf(startDate.getValue()));
			pstmt.setDate(4, Date.valueOf(endDate.getValue()));
			pstmt.setInt(5, Integer.parseInt(gwanNum));
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		listLoad();
		clear();
	}
	
	
	@FXML public void searchAction(ActionEvent event) {
		listTable.getItems().clear();
		try {
			conn = ConnUtil.getConnection();
			String sql ="";
			if(searchTf.getText().trim().length()==0) {
				//아무것도 입력안함
				if(searchRegion.getSelectionModel().getSelectedItem().equals("전체")) {
					//지역 선택안함
					listLoad();
					return;
				}else {
					//지역을 선택했다.
					sql = "select theater_num,theater_region,theater_name, " + 
							"gwan_num,gwan_name,movie_startday,movie_endday,movie_dayschedule, " + 
							"movie_num,movie_name " + 
							"from theater join gwan USING (theater_num) JOIN movie using(movie_num) " + 
							"where gwan_seatcount>0 and theater_region=?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, searchRegion.getSelectionModel().getSelectedItem());
				}
			} else {
				//검색어가 있다.
				if(searchSelect.getSelectionModel().getSelectedItem().equals("극장명")) {
					//선택은 극장명
					if(searchRegion.getSelectionModel().getSelectedItem().equals("전체")) {
						//검색어 있 + 극장명 선택 + 지역전체
						sql = "select theater_num,theater_region,theater_name, " + 
								"gwan_num,gwan_name,movie_startday,movie_endday,movie_dayschedule, " + 
								"movie_num,movie_name " + 
								"from theater join gwan USING (theater_num) JOIN movie using(movie_num) " + 
								"where gwan_seatcount>0 and theater_name like ?";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, "%"+searchTf.getText()+"%");
					}else {
						//검색어 있 + 극장명 선택 + 지역선택
						sql = "select theater_num,theater_region,theater_name, " + 
								"gwan_num,gwan_name,movie_startday,movie_endday,movie_dayschedule, " + 
								"movie_num,movie_name " + 
								"from theater join gwan USING (theater_num) JOIN movie using(movie_num) " + 
								"where gwan_seatcount>0 and theater_region=? and theater_name like ? ";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, searchRegion.getSelectionModel().getSelectedItem());
						pstmt.setString(2, "%"+searchTf.getText()+"%");
					}
				}else {
					//선택 상영관명
					if(searchRegion.getSelectionModel().getSelectedItem().equals("전체")) {
						//상영관명 선택 + 검색어있음 + 지역전체
						sql = "select theater_num,theater_region,theater_name, " + 
								"gwan_num,gwan_name,movie_startday,movie_endday,movie_dayschedule, " + 
								"movie_num,movie_name " + 
								"from theater join gwan USING (theater_num) JOIN movie using(movie_num) " + 
								"where gwan_seatcount>0 and gwan_name like ? ";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, "%"+searchTf.getText()+"%");
					}else {
						//상영관명 선택 + 검색어있음 + 지역선택함.
						sql = "select theater_num,theater_region,theater_name, " + 
								"gwan_num,gwan_name,movie_startday,movie_endday,movie_dayschedule, " + 
								"movie_num,movie_name " + 
								"from theater join gwan USING (theater_num) JOIN movie using(movie_num) " + 
								"where gwan_seatcount>0 and theater_region=? and gwan_name like ? ";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, searchRegion.getSelectionModel().getSelectedItem());
						pstmt.setString(2, "%"+searchTf.getText()+"%");
					}
				}	
			}//검색조건 끝
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String region = rs.getString("theater_region");
				int theaterNum = rs.getInt("theater_num");
				String theaterName = rs.getString("theater_name");
				int gwanNum = rs.getInt("gwan_num");
				String gwanName = rs.getString("gwan_name");
				int movieNum = rs.getInt("movie_num");
				String movieName = rs.getString("movie_name");
				String startDay;
				if(rs.getDate("movie_startday")==null) {
					startDay="";
				}else {
					startDay = rs.getDate("movie_startday").toString();
				}
				String endDay;
				if(rs.getDate("movie_endday")==null) {
					endDay = "";
				}else {
					endDay = rs.getDate("movie_endday").toString();
				}
				
				String daySchedule;
				if(rs.getString("movie_dayschedule")==null) {
					daySchedule ="";
				}else {
					daySchedule = rs.getString("movie_dayschedule");
				}
				
				list.add(new GwanMovieVO(region, theaterNum, theaterName, gwanNum, gwanName, movieNum, movieName, startDay, endDay, daySchedule));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		listTable.setItems(list);
		
	}
	
	@FXML public void resetAction(ActionEvent event) {
		searchRegion.getSelectionModel().select("전체");
		searchTf.clear();
		movieList();
		clear();
	}

}
