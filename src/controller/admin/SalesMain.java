package controller.admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import controller.ConnUtil;
import controller.vo.SalesDownloadVO;
import controller.vo.SalesGenreVO;
import controller.vo.SalesRegionVO;
import controller.vo.SalesTheaterVO;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SalesMain implements Initializable {

	@FXML Button backGo;
	
	@FXML ComboBox<String> totalSYear;
	@FXML ComboBox<String> totalSMonth;
	@FXML ComboBox<String> totalEYear;
	@FXML ComboBox<String> totalEMonth;
	@FXML Button totalSearch;
	@FXML LineChart<String, Integer> totalChart;
	
	@FXML DatePicker dailySday;
	@FXML DatePicker dailyEday;
	@FXML Button dailySearch;
	@FXML LineChart<String, Integer> dailyChart;
	
	@FXML ComboBox<String> regionSYear;
	@FXML ComboBox<String> regionSMonth;
	@FXML ComboBox<String> regionEYear;
	@FXML ComboBox<String> regionEMonth;
	@FXML Button regionSearch;
	@FXML PieChart regionChart;
	@FXML TableView<SalesRegionVO> regionTable;
	@FXML TableColumn<SalesRegionVO, String> rRegion;
	@FXML TableColumn<SalesRegionVO, String> rMoney;
	@FXML TableColumn<SalesRegionVO, String> rPercent;
	
	@FXML ComboBox<String> theaterSYear;
	@FXML ComboBox<String> theaterSMonth;
	@FXML ComboBox<String> theaterEYear;
	@FXML ComboBox<String> theaterEMonth;
	@FXML Button theaterSearch;
	@FXML ComboBox<String> theaterRegionSelect;
	@FXML BarChart<String, Integer> theaterChart;
	@FXML TableView<SalesTheaterVO> theaterTable;
	@FXML TableColumn<SalesTheaterVO, String> tRegion;
	@FXML TableColumn<SalesTheaterVO, String> tTheater;
	@FXML TableColumn<SalesTheaterVO, String> tMoney;
	
	@FXML ComboBox<String> genreSYear;
	@FXML ComboBox<String> genreSMonth;
	@FXML ComboBox<String> genreEYear;
	@FXML ComboBox<String> genreEMonth;
	@FXML Button genreSearch;
	@FXML PieChart genreChart;
	@FXML TableView<SalesGenreVO> genreTable;
	@FXML TableColumn<SalesGenreVO,String> gGenre;
	@FXML TableColumn<SalesGenreVO,String> gMoney;
	@FXML TableColumn<SalesGenreVO,String> gPercent;
	@FXML ComboBox<String> genreRegionSelect;
	@FXML ComboBox<String> genreTheaterSelect;

	@FXML Button download;
	@FXML Label userId;

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	String nowYear = "";
	String nowMonth = "";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ArrayList<String> yearList = new ArrayList<String>();
		try {
			conn = ConnUtil.getConnection();
			String sql = "select to_char(ticket_date,'YYYY') as year from ticket order by ticket_date";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			String year = rs.getString("year");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			SimpleDateFormat sdfm = new SimpleDateFormat("MM");
			Date date = new Date();
			nowYear = sdf.format(date);
			nowMonth = sdfm.format(date);
			int startYear = Integer.parseInt(year);
			int thisYear = Integer.parseInt(sdf.format(date));
			for(int i = startYear;i<=thisYear+1;i++) {
				yearList.add(String.valueOf(i));
			}
		} catch (SQLException e) {
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		totalSYear.getItems().addAll(yearList);			totalEYear.getItems().addAll(yearList);
		regionSYear.getItems().addAll(yearList);		regionEYear.getItems().addAll(yearList);
		theaterSYear.getItems().addAll(yearList);		theaterEYear.getItems().addAll(yearList);
		genreSYear.getItems().addAll(yearList);			genreEYear.getItems().addAll(yearList);
		totalSYear.getSelectionModel().select(nowYear);		totalEYear.getSelectionModel().select(nowYear);
		totalSMonth.getSelectionModel().select(nowMonth);	totalEMonth.getSelectionModel().select(nowMonth);
		regionSYear.getSelectionModel().select(nowYear);	regionEYear.getSelectionModel().select(nowYear);
		regionSMonth.getSelectionModel().select(nowMonth);	regionEMonth.getSelectionModel().select(nowMonth);
		theaterSYear.getSelectionModel().select(nowYear);	theaterEYear.getSelectionModel().select(nowYear);
		theaterSMonth.getSelectionModel().select(nowMonth);	theaterEMonth.getSelectionModel().select(nowMonth);
		genreSYear.getSelectionModel().select(nowYear);		genreEYear.getSelectionModel().select(nowYear);
		genreSMonth.getSelectionModel().select(nowMonth);	genreEMonth.getSelectionModel().select(nowMonth);
		LocalDate d = LocalDate.now();
		dailySday.setValue(d);	dailyEday.setValue(d);
		rRegion.setCellValueFactory(new PropertyValueFactory<>("region"));
		rRegion.setStyle("-fx-alignment:center");
		rMoney.setCellValueFactory(new PropertyValueFactory<>("money"));
		rMoney.setStyle("-fx-alignment:center-right");
		rPercent.setCellValueFactory(new PropertyValueFactory<>("percent"));
		rPercent.setStyle("-fx-alignment:center-right");
		theaterRegionSelect.getSelectionModel().select(0);
		tRegion.setCellValueFactory(new PropertyValueFactory<>("region"));
		tRegion.setStyle("-fx-alignment:center");
		tTheater.setCellValueFactory(new PropertyValueFactory<>("theater"));
		tMoney.setCellValueFactory(new PropertyValueFactory<>("money"));
		tMoney.setStyle("-fx-alignment:center-right");
		gGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
		gGenre.setStyle("-fx-alignment:center");
		gMoney.setCellValueFactory(new PropertyValueFactory<>("money"));
		gMoney.setStyle("-fx-alignment:center-right");
		gPercent.setCellValueFactory(new PropertyValueFactory<>("percent"));
		gPercent.setStyle("-fx-alignment:center-right");
		
		genreRegionSelect.getSelectionModel().select(0);
		genreTheaterSelect.getItems().add("전체");
		genreTheaterSelect.getSelectionModel().select(0);
		try {
			conn = ConnUtil.getConnection();
			String sql = "select * from theater";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				genreTheaterSelect.getItems().add(rs.getString("theater_name"));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		genreRegionSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				genreTheaterSelect.getItems().clear();
				genreTheaterSelect.getItems().add("전체");
				genreTheaterSelect.getSelectionModel().select(0);
				try {
					conn = ConnUtil.getConnection();
					String sql = "";
					if(genreRegionSelect.getSelectionModel().getSelectedItem().equals("전체")) {
						sql = "select * from theater";
						pstmt = conn.prepareStatement(sql);
					}else {
						sql = "select * from theater where theater_region=?";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, genreRegionSelect.getSelectionModel().getSelectedItem());
					}
					rs = pstmt.executeQuery();
					while(rs.next()) {
						genreTheaterSelect.getItems().add(rs.getString("theater_name"));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
					try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
					try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
				}
			}
		});
		genreChart.setStartAngle(90);
		regionChart.setStartAngle(90);
		
	}

	/**
	 * 뒤로가기
	 * @param event
	 */
	@FXML public void backGo(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/AdminMain.fxml"));
			Parent root = loader.load();
			AdminMain umc = loader.getController();
			umc.userId.setText(this.userId.getText());
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("관리자모드");
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
	 * 월별 전체매출 조회
	 * @param event
	 */
	@FXML public void totalSearchAction(ActionEvent event) {
		int sYear = Integer.parseInt(totalSYear.getSelectionModel().getSelectedItem());
		int eYear = Integer.parseInt(totalEYear.getSelectionModel().getSelectedItem());
		int sMonth = Integer.parseInt(totalSMonth.getSelectionModel().getSelectedItem());
		int eMonth = Integer.parseInt(totalEMonth.getSelectionModel().getSelectedItem());
		if(eYear<sYear) {
			errDialog();
			return;
		}else if(eYear==sYear){
			if(eMonth<sMonth) {
				errDialog();
				return;
			}
		}
		Calendar cal = Calendar.getInstance();
		cal.set(eYear, eMonth-1, 1);
		int eDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		try {
			String start = sYear+"-"+sMonth+"-"+"01";
			String end = eYear+"-"+eMonth+"-"+eDay;
			conn = ConnUtil.getConnection();
			String sql = "select to_char(ticket_date,'YYYY-MM') as year_month, sum(ticket_money) as money "
					+ "from ticket "
					+ "where ticket_date between ? and ? "
					+ "group by to_char(ticket_date,'YYYY-MM') "
					+ "order by year_month";
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, java.sql.Date.valueOf(start));
			pstmt.setDate(2, java.sql.Date.valueOf(end));
			rs = pstmt.executeQuery();
			ObservableList<XYChart.Series<String, Integer>> list = FXCollections.observableArrayList();
			XYChart.Series<String, Integer> series1 = new XYChart.Series<String,Integer>();
			while(rs.next()) {
				series1.getData().add(new XYChart.Data<String, Integer>(rs.getString("year_month"), rs.getInt("money")));
			}
			list.add(series1);
			totalChart.setData(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
	}
	/**
	 * 일별 매출현황
	 * @param event
	 */
	@FXML public void dailySearchAction(ActionEvent event) {
		LocalDate start = dailySday.getValue();
		LocalDate end = dailyEday.getValue();
		if(start.isAfter(end)) {	errDialog();	return;		}
		try {
			conn = ConnUtil.getConnection();
			String sql = "select DISTINCT to_char(ticket_date,'yyyy-mm-dd') as ticket_date , sum(ticket_money) as money " + 
					"from ticket where ticket_date "
					+ "between ? and ? "
					+ "group by ticket_date, to_char(ticket_date,'yyyy-mm-dd') "
					+ "order by ticket_date";
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, java.sql.Date.valueOf(start));
			pstmt.setDate(2, java.sql.Date.valueOf(end));
			rs = pstmt.executeQuery();
			ObservableList<XYChart.Series<String, Integer>> list = FXCollections.observableArrayList();
			XYChart.Series<String, Integer> series1 = new XYChart.Series<String,Integer>();
			while(rs.next()) {
				series1.getData().add(new XYChart.Data<String, Integer>(rs.getString("ticket_date"), rs.getInt("money")));
			}
			list.add(series1);
			dailyChart.setData(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
	}
	/**
	 * 지역별 매출현황
	 * @param event
	 */
	@FXML public void regionSearchAction(ActionEvent event) {
		int sYear = Integer.parseInt(regionSYear.getSelectionModel().getSelectedItem());
		int eYear = Integer.parseInt(regionEYear.getSelectionModel().getSelectedItem());
		int sMonth = Integer.parseInt(regionSMonth.getSelectionModel().getSelectedItem());
		int eMonth = Integer.parseInt(regionEMonth.getSelectionModel().getSelectedItem());
		if(eYear<sYear) {
			errDialog();
			return;
		}else if(eYear==sYear){
			if(eMonth<sMonth) {
				errDialog();
				return;
			}
		}
		Calendar cal = Calendar.getInstance();
		cal.set(eYear, eMonth-1, 1);
		int eDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		try {
			String start = sYear+"-"+sMonth+"-"+"01";
			String end = eYear+"-"+eMonth+"-"+eDay;
			conn = ConnUtil.getConnection();
			String sql = "select to_char(sum(ticket_money),'999,999,999') as money, "
					+ "sum(ticket_money) as moneyNum, "
					+ "round((RATIO_TO_REPORT(sum(ticket_money)) OVER ())*100,2) as percent , "
					+ "theater_region "
					+ "from ticket join theater using (theater_name) " 
					+ "where ticket_date between ? and ? and ticket_cancel='결제' group by theater_region order by moneyNum desc";
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, java.sql.Date.valueOf(start));
			pstmt.setDate(2, java.sql.Date.valueOf(end));
			rs = pstmt.executeQuery();
			ObservableList<Data> list = FXCollections.observableArrayList();
			ObservableList<SalesRegionVO> regionVO = FXCollections.observableArrayList();
			while(rs.next()) {
				int moneyNum = rs.getInt("moneyNum");
				String money = rs.getString("money");
				String region = rs.getString("theater_region");
				String percent = rs.getString("percent");
				Data data = new Data(region, moneyNum);
				list.add(data);
				regionVO.add(new SalesRegionVO(region, money, percent+"%"));
			}
			regionChart.setData(list);
			regionTable.setItems(regionVO);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
	}
	
	/**
	 * 극장별 매출현황
	 * @param event
	 */
	@FXML public void theaterSearchAction(ActionEvent event) {
		int sYear = Integer.parseInt(theaterSYear.getSelectionModel().getSelectedItem());
		int eYear = Integer.parseInt(theaterEYear.getSelectionModel().getSelectedItem());
		int sMonth = Integer.parseInt(theaterSMonth.getSelectionModel().getSelectedItem());
		int eMonth = Integer.parseInt(theaterEMonth.getSelectionModel().getSelectedItem());
		if(eYear<sYear) {
			errDialog();
			return;
		}else if(eYear==sYear){
			if(eMonth<sMonth) {
				errDialog();
				
				return;
			}
		}
		Calendar cal = Calendar.getInstance();
		cal.set(eYear, eMonth-1, 1);
		int eDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		String start = sYear+"-"+sMonth+"-"+"01";
		String end = eYear+"-"+eMonth+"-"+eDay;
		try {
			ObservableList<XYChart.Series<String, Integer>> list = FXCollections.observableArrayList();
			XYChart.Series<String, Integer> series1 = new XYChart.Series<String,Integer>();
			ObservableList<SalesTheaterVO> theaterVO = FXCollections.observableArrayList();
			conn = ConnUtil.getConnection();
			if(theaterRegionSelect.getSelectionModel().getSelectedItem().equals("전체")) {
				//전체조회
				String sql = "select sum(ticket_money) as money ,theater_region, theater_name " + 
						"from ticket join theater using (theater_name) where ticket_date " + 
						"between ? and ? " + 
						"group by theater_name,theater_region "
						+ "order by money desc, theater_region, theater_name ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setDate(1, java.sql.Date.valueOf(start));
				pstmt.setDate(2, java.sql.Date.valueOf(end));
			}else {
				String sql = "select sum(ticket_money) as money ,theater_region, theater_name " + 
						"from ticket join theater using (theater_name) where ticket_date " + 
						"between ? and ? and theater_region=? " + 
						"group by theater_name,theater_region "
						+ "order by money desc, theater_region, theater_name ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setDate(1, java.sql.Date.valueOf(start));
				pstmt.setDate(2, java.sql.Date.valueOf(end));
				pstmt.setString(3, theaterRegionSelect.getSelectionModel().getSelectedItem());
			}
			rs = pstmt.executeQuery();
			while(rs.next()) {
				DecimalFormat df = new DecimalFormat("###,###,###");
				String money = df.format(rs.getInt("money"));
				series1.getData().add(new XYChart.Data<String, Integer>(rs.getString("theater_name"), rs.getInt("money")));
				theaterVO.add(new SalesTheaterVO(rs.getString("theater_region"), money, rs.getString("theater_name")));
			}
			list.add(series1);
			theaterChart.setData(list);
			theaterTable.setItems(theaterVO);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
	}

	/**
	 * 장르별 매출현황
	 * @param event
	 */
	@FXML public void genreSearchAction(ActionEvent event) {
		int sYear = Integer.parseInt(genreSYear.getSelectionModel().getSelectedItem());
		int eYear = Integer.parseInt(genreEYear.getSelectionModel().getSelectedItem());
		int sMonth = Integer.parseInt(genreSMonth.getSelectionModel().getSelectedItem());
		int eMonth = Integer.parseInt(genreEMonth.getSelectionModel().getSelectedItem());
		if(eYear<sYear) {errDialog(); return;}
		else if(eYear==sYear){ if(eMonth<sMonth) {	errDialog(); return; }	}
		Calendar cal = Calendar.getInstance();
		cal.set(eYear, eMonth-1, 1);
		int eDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		try {
			
			String start = sYear+"-"+sMonth+"-"+"01";
			String end = eYear+"-"+eMonth+"-"+eDay;
			conn = ConnUtil.getConnection();
			String region = genreRegionSelect.getSelectionModel().getSelectedItem();
			String theater = genreTheaterSelect.getSelectionModel().getSelectedItem();

			String sql = "";
			if(region.equals("전체")) {
				if(theater.equals("전체")) {
					sql = "select to_char(sum(ticket_money),'999,999,999') as money, " + 
							"sum(ticket_money) as moneyNum, " + 
							"round((RATIO_TO_REPORT(sum(ticket_money)) OVER ())*100,2) as percent , " + 
							"movie_genre " + 
							"from ticket join theater using (theater_name) join movie using(movie_name) " + 
							"where ticket_date " + 
							"between ? and ? " +  
							" and ticket_cancel='결제' group by movie_genre order by moneyNum desc";
					pstmt = conn.prepareStatement(sql);
					pstmt.setDate(1, java.sql.Date.valueOf(start));
					pstmt.setDate(2, java.sql.Date.valueOf(end));
				}else {
					//전체 + 극장만
					sql = "select to_char(sum(ticket_money),'999,999,999') as money, " + 
							"sum(ticket_money) as moneyNum, " + 
							"round((RATIO_TO_REPORT(sum(ticket_money)) OVER ())*100,2) as percent , " + 
							"movie_genre " + 
							"from ticket join theater using (theater_name) join movie using(movie_name) " + 
							"where ticket_date " + 
							"between ? and ? and theater_name=? " + 
							"and ticket_cancel='결제' group by movie_genre, theater_name order by moneyNum desc";
					pstmt = conn.prepareStatement(sql);
					pstmt.setDate(1, java.sql.Date.valueOf(start));
					pstmt.setDate(2, java.sql.Date.valueOf(end));
					pstmt.setString(3, theater);
				}
			}else {
				if(theater.equals("전체")) {
					//지역 + 전체
					sql = "select to_char(sum(ticket_money),'999,999,999') as money, " + 
							"sum(ticket_money) as moneyNum, " + 
							"round((RATIO_TO_REPORT(sum(ticket_money)) OVER ())*100,2) as percent , " + 
							"movie_genre , theater_region " + 
							"from ticket join theater using (theater_name) join movie using(movie_name) " + 
							"where ticket_date " + 
							"between ? and ? and theater_region =? " + 
							"and ticket_cancel='결제' group by movie_genre , theater_region order by moneyNum desc";
					pstmt = conn.prepareStatement(sql);
					pstmt.setDate(1, java.sql.Date.valueOf(start));
					pstmt.setDate(2, java.sql.Date.valueOf(end));
					pstmt.setString(3, region);
				}else {
					//지역 + 극장
					sql = "select to_char(sum(ticket_money),'999,999,999') as money, " + 
							"sum(ticket_money) as moneyNum, " + 
							"round((RATIO_TO_REPORT(sum(ticket_money)) OVER ())*100,2) as percent , " + 
							"movie_genre " + 
							"from ticket join theater using (theater_name) join movie using(movie_name)" + 
							"where ticket_date " + 
							"between ? and ? and theater_region =? and theater_name=? " + 
							"and ticket_cancel='결제' group by movie_genre , theater_region order by moneyNum desc";
					pstmt = conn.prepareStatement(sql);
					pstmt.setDate(1, java.sql.Date.valueOf(start));
					pstmt.setDate(2, java.sql.Date.valueOf(end));
					pstmt.setString(3, region);
					pstmt.setString(4, theater);
				}
			}
			rs = pstmt.executeQuery();
			ObservableList<Data> list = FXCollections.observableArrayList();
			ObservableList<SalesGenreVO> genreVO = FXCollections.observableArrayList();
			while(rs.next()) {
				int moneyNum = rs.getInt("moneyNum");
				String money = rs.getString("money");
				String genre = rs.getString("movie_genre");
				String percent = rs.getString("percent");
				Data data = new Data(genre, moneyNum);
				list.add(data);
				genreVO.add(new SalesGenreVO(genre, money, percent+"%"));
			}
			genreChart.setData(list);
			genreTable.setItems(genreVO);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
	}

	/**
	 * 종료일이 시작일보다 작을시 오류 팝업
	 */
	public void errDialog() {
		Popup dialog = new Popup();
		Stage primaryStage = (Stage) userId.getScene().getWindow();
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/fxml/admin/ErrDialog.fxml"));
			Button btnOk = (Button) parent.lookup("#btnOk");
			btnOk.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					dialog.hide();
				}
			});
			dialog.getContent().add(parent);
			dialog.setAutoHide(true);
			dialog.show(primaryStage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 판매자료 다운로드
	 * @param event
	 */
	@FXML public void downloadAction(ActionEvent event) {
		try {
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(userId.getScene().getWindow());
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/Download.fxml"));
			dialog.setTitle("매출현황 다운로드");
			DatePicker startD = (DatePicker) root.lookup("#startDay");
			DatePicker endD = (DatePicker) root.lookup("#endDay");
			Button download = (Button) root.lookup("#download");
			Button close = (Button) root.lookup("#close");
			close.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					dialog.close();
				}
			});
			download.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					List<SalesDownloadVO> list = new ArrayList<SalesDownloadVO>();
					try {
						conn = ConnUtil.getConnection();
						String sql ="select distinct " + 
								"ticket_date , ticket_time , user_id , movie_name , movie_genre, movie_rating , theater_region, theater_name " + 
								", gwan_name , ticket_seat , ticket_money , ticket_payment , ticket_cancel " + 
								"from ticket join theater using (theater_name) join movie using(movie_name) join gwan using(gwan_name) " +
								"where ticket_date between ? and ? "+
								"order by ticket_date";
						pstmt = conn.prepareStatement(sql);
						pstmt.setDate(1, java.sql.Date.valueOf(startD.getValue()));
						pstmt.setDate(2, java.sql.Date.valueOf(endD.getValue()));
						rs = pstmt.executeQuery();
						while(rs.next()) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							Date date = rs.getDate("ticket_date");
							String ticketDate = sdf.format(date);
							String ticketTime = rs.getString("ticket_time");
							String userId = rs.getString("user_id");
							String movieName = rs.getString("movie_name");
							String movieGenre = rs.getString("movie_genre");
							String movieRating = rs.getString("movie_rating");
							String theaterRegion = rs.getString("theater_region");
							String theaterName = rs.getString("theater_name");
							String gwanName = rs.getString("gwan_name");
							String ticketSeat = rs.getString("ticket_seat");
							int ticketMoney = rs.getInt("ticket_money");
							String ticketPayment = rs.getString("ticket_payment");
							String ticketCancel = rs.getString("ticket_cancel");
							list.add(new SalesDownloadVO(ticketDate, ticketTime, userId, movieName, movieGenre, movieRating, theaterRegion, theaterName, gwanName, ticketSeat, ticketMoney, ticketPayment, ticketCancel));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
						try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
						try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
					}
					String fileName = startD.getValue().toString()+" ~ "+endD.getValue().toString() + "매출현황";
					
					xlsWriter(list,fileName);
				}
			});
		
			Scene scene = new Scene(root);
			dialog.setScene(scene);
			dialog.show();
		} catch (IOException e1) {
		
			e1.printStackTrace();
		}
		
		
		
		
		
	}

	 public void xlsWriter(List<SalesDownloadVO> list, String fileName) {
	        // 워크북 생성
	        HSSFWorkbook workbook = new HSSFWorkbook();
	        // 워크시트 생성
	        HSSFSheet sheet = workbook.createSheet();
	        // 행 생성
	        HSSFRow row = sheet.createRow(0);
	        // 쎌 생성
	        HSSFCell cell;
	        
	        cell = row.createCell(0);
	        cell.setCellValue("NO");
	        cell = row.createCell(1);
	        cell.setCellValue("티켓날짜");
	        cell = row.createCell(2);
	        cell.setCellValue("시간");
	        cell = row.createCell(3);
	        cell.setCellValue("유저ID");
	        cell = row.createCell(4);
	        cell.setCellValue("영화명");
	        cell = row.createCell(5);
	        cell.setCellValue("장르");
	        cell = row.createCell(6);
	        cell.setCellValue("관람등급");
	        cell = row.createCell(7);
	        cell.setCellValue("지역");
	        cell = row.createCell(8);
	        cell.setCellValue("극장명");
	        cell = row.createCell(9);
	        cell.setCellValue("상영관명");
	        cell = row.createCell(10);
	        cell.setCellValue("좌석정보");
	        cell = row.createCell(11);
	        cell.setCellValue("결제금액");
	        cell = row.createCell(12);
	        cell.setCellValue("결제방법");
	        cell = row.createCell(13);
	        cell.setCellValue("결제구분");
	        
	        SalesDownloadVO vo;
	        int no = 1;
	        for(int i = 0; i<list.size();i++) {
	        	vo = list.get(i);
	        	
	        	row = sheet.createRow(i+1);
	        	
	        	cell = row.createCell(0);
		        cell.setCellValue(no);
		        cell = row.createCell(1);
		        cell.setCellValue(vo.getTicketDate());
		        cell = row.createCell(2);
		        cell.setCellValue(vo.getTicketTime());
		        cell = row.createCell(3);
		        cell.setCellValue(vo.getUserId());
		        cell = row.createCell(4);
		        cell.setCellValue(vo.getMovieName());
		        cell = row.createCell(5);
		        cell.setCellValue(vo.getMovieGenre());
		        cell = row.createCell(6);
		        cell.setCellValue(vo.getMovieRating());
		        cell = row.createCell(7);
		        cell.setCellValue(vo.getTheaterRegion());
		        cell = row.createCell(8);
		        cell.setCellValue(vo.getTheaterName());
		        cell = row.createCell(9);
		        cell.setCellValue(vo.getGwanName());
		        cell = row.createCell(10);
		        cell.setCellValue(vo.getTicketSeat());
		        cell = row.createCell(11);
		        cell.setCellValue(vo.getTicketMoney());
		        cell = row.createCell(12);
		        cell.setCellValue(vo.getTicketPayment());
		        cell = row.createCell(13);
		        cell.setCellValue(vo.getTicketCancel());
	        	no++;
	        }
	        for(int j =0;j<14;j++) {
	        	sheet.autoSizeColumn(j);
	        	sheet.setColumnWidth(j, sheet.getColumnWidth(j)+(short)1024);
	        }
	        FileChooser fc = new FileChooser();
	        fc.getExtensionFilters().add(new ExtensionFilter("Excel File(*.xls)", "*.xls"));
	        fc.setInitialFileName(fileName);
	        File file = fc.showSaveDialog(userId.getScene().getWindow());
	        FileOutputStream fos = null;
	        
	        try {
	            fos = new FileOutputStream(file);
	            workbook.write(fos);
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if(workbook!=null) workbook.close();
	                if(fos!=null) fos.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        
	 }
}
