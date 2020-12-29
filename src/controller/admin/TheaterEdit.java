package controller.admin;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import controller.ConnUtil;
import controller.vo.TheaterVO;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class TheaterEdit implements Initializable {

	@FXML Label userId;
	@FXML Button backGo;
	@FXML Button newBt;
	@FXML Button addAndModifyBt;
	@FXML Button delBt;
	@FXML TextField numTf;
	@FXML TextField nameTf;
	@FXML ComboBox<String> regionCombo;
	@FXML TableView<TheaterVO> theaterTable;
	@FXML TableColumn<TheaterVO, String> region;
	@FXML TableColumn<TheaterVO, Integer> num;
	@FXML TableColumn<TheaterVO, String> name;
	@FXML TableColumn<TheaterVO, Integer> count;
	@FXML TextField searchNameTf;
	@FXML ComboBox<String> searchRegion;
	@FXML Button searchBt;
	@FXML Button resetBt;
	
	private String id;
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ObservableList<TheaterVO> list;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		searchRegion.getSelectionModel().select("전체");
		regionCombo.getSelectionModel().select("서울");
		list = FXCollections.observableArrayList();
		region.setCellValueFactory(new PropertyValueFactory<>("Region"));
		num.setCellValueFactory(new PropertyValueFactory<>("Num"));
		name.setCellValueFactory(new PropertyValueFactory<>("Name"));
		count.setCellValueFactory(new PropertyValueFactory<>("Count"));
		theaterList();
		numTf.setDisable(true);
		theaterTable.setPlaceholder(new Label("검색결과가 없습니다."));
		theaterTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					if(event.getClickCount()>1) {
						TheaterVO theater = theaterTable.getSelectionModel().getSelectedItem();
						numTf.setText(String.valueOf(theater.getNum()));
						nameTf.setText(theater.getName());
						regionCombo.getSelectionModel().select(theater.getRegion());
					}
				}catch (Exception e) {
				}
			}
		});
		searchNameTf.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				searchAction(event);
			}
		});
	}

	public void theaterList() {
		theaterTable.getItems().clear();
		try {
			conn = ConnUtil.getConnection();
			String sql = "select t.theater_num,t.theater_region,t.theater_name, " + 
					"(select count(*) from gwan g where g.theater_num=t.theater_num) as count " + 
					"from theater t order by count desc, t.theater_region,t.theater_name";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				int num = rs.getInt("theater_num");
				String region = rs.getString("theater_region");
				String name = rs.getString("theater_name");
				int count = rs.getInt("count");
				list.add(new TheaterVO(num, region, name, count));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		theaterTable.setItems(list);
	}
	
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

	@FXML public void newAction(ActionEvent event) {
		numTf.clear();
		regionCombo.getSelectionModel().select("서울");
		nameTf.clear();
		
	}

	@FXML public void addAndModifyAction(ActionEvent event) {
		if(nameTf.getText().trim().length()==0) {nameTf.requestFocus(); return;}
		
		if(numTf.getText().isEmpty()) { //신규등록	
			try {
				conn = ConnUtil.getConnection();
				String sql = "insert into theater(theater_num,theater_region,theater_name) "
						+ "values(theater_num_seq.nextval,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, regionCombo.getSelectionModel().getSelectedItem());
				pstmt.setString(2, nameTf.getText());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}else {//수정
			try {
				conn = ConnUtil.getConnection();
				String sql = "update theater set "
						+ "theater_region=?,theater_name=? where theater_num=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, regionCombo.getSelectionModel().getSelectedItem());
				pstmt.setString(2, nameTf.getText());
				pstmt.setInt(3, Integer.parseInt(numTf.getText()));
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}
		theaterList();
		newAction(event);
	}

	@FXML public void delAction(ActionEvent event) {
		try {
			conn = ConnUtil.getConnection();
			String sql = "delete from theater where theater_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(numTf.getText()));
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		theaterList();
		newAction(event);
	}
	
	@FXML public void searchAction(ActionEvent event) {
		list.clear();
		theaterTable.getItems().clear();
		try {
			conn = ConnUtil.getConnection();
			String sql ="";
			if(searchRegion.getSelectionModel().getSelectedItem().equals("전체")&&searchNameTf.getText().trim().length()!=0) {
				sql = "select t.theater_num,t.theater_region,t.theater_name, " + 
						"(select count(*) from gwan g where g.theater_num=t.theater_num) as count " + 
						"from theater t where theater_name like ? order by t.theater_region,t.theater_name";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%"+searchNameTf.getText()+"%");
				rs = pstmt.executeQuery();
			}else if(!searchRegion.getSelectionModel().getSelectedItem().equals("전체")&&searchNameTf.getText().trim().length()==0) {
				sql = "select t.theater_num,t.theater_region,t.theater_name, " + 
						"(select count(*) from gwan g where g.theater_num=t.theater_num) as count " + 
						"from theater t where theater_region=? order by t.theater_region,t.theater_name";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchRegion.getSelectionModel().getSelectedItem());
				rs = pstmt.executeQuery();
			}else if(!searchRegion.getSelectionModel().getSelectedItem().equals("전체")&&searchNameTf.getText().trim().length()!=0) {
				sql = "select t.theater_num,t.theater_region,t.theater_name, " + 
						"(select count(*) from gwan g where g.theater_num=t.theater_num) as count " + 
						"from theater t where theater_region=? and theater_name like ? order by t.theater_region,t.theater_name";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchRegion.getSelectionModel().getSelectedItem());
				pstmt.setString(2, "%"+searchNameTf.getText()+"%");
				rs = pstmt.executeQuery();
			}else {
				theaterList();
			}
			while(rs.next()) {
				int num = rs.getInt("theater_num");
				String region = rs.getString("theater_region");
				String name = rs.getString("theater_name");
				int count = rs.getInt("count");
				list.add(new TheaterVO(num, region, name, count));
			}
			
		} catch (SQLException e) {
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		theaterTable.setItems(list);
	}

	@FXML public void resetAction(ActionEvent event) {
		searchNameTf.clear();
		searchRegion.getSelectionModel().select("전체");
		theaterList();
		newAction(event);
	}

}
