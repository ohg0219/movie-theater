package controller.admin;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import controller.ConnUtil;
import controller.vo.UserListVO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.stage.Stage;

public class UserList implements Initializable {

	@FXML Label userId;
	@FXML TableView<UserListVO> userListTable;
	@FXML TableColumn<UserListVO, String> dbuserId;
	@FXML TableColumn<UserListVO, String> dbuserName;
	@FXML TableColumn<UserListVO, String> dbuserTel;
	@FXML TableColumn<UserListVO, String> dbuserAddr;
	@FXML Button backGo;
	@FXML TextField searchTf;
	@FXML ComboBox<String> searchCombo;
	@FXML Button searchBt;
	@FXML Button resetBt;

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		searchCombo.getSelectionModel().select(0);
		dbuserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
		dbuserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
		dbuserTel.setCellValueFactory(new PropertyValueFactory<>("userTel"));
		dbuserAddr.setCellValueFactory(new PropertyValueFactory<>("userAddr"));
		userList();
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
	 * 전체목록 보기
	 */
	public void userList() {
		ObservableList<UserListVO> list = FXCollections.observableArrayList();
		try {
			conn = ConnUtil.getConnection();
			String sql ="select user_id, user_name, user_tel, user_addr from user_table where user_type = 0";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(new UserListVO(rs.getString("user_id"), rs.getString("user_name"), rs.getString("user_tel"), rs.getString("user_addr")));
			}
			userListTable.setItems(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
	}
	
	/**
	 * 검색
	 * @param event
	 */
	@FXML public void searchAction(ActionEvent event) {
		if(searchTf.getText().isEmpty()) {
			userList(); return;
		}
		userListTable.getItems().clear();
		ObservableList<UserListVO> list = FXCollections.observableArrayList();
		String select = searchCombo.getSelectionModel().getSelectedItem();
		try {
			conn = ConnUtil.getConnection();
			String sql ="";
			if(select.equals("아이디")) {
				sql ="select user_id, user_name, user_tel, user_addr from user_table where user_id like ? and user_type = 0";
			}else if(select.equals("이름")) {
				sql ="select user_id, user_name, user_tel, user_addr from user_table where user_name like ? and user_type = 0";
			}else if(select.equals("전화번호")) {
				sql ="select user_id, user_name, user_tel, user_addr from user_table where user_tel like ? and user_type = 0";
			}else if(select.equals("주소")) {
				sql ="select user_id, user_name, user_tel, user_addr from user_table where user_addr like ? and user_type = 0";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+searchTf.getText()+"%");
			rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(new UserListVO(rs.getString("user_id"), rs.getString("user_name"), rs.getString("user_tel"), rs.getString("user_addr")));
			}
			userListTable.setItems(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
		
	}
	/**
	 * 검색 초기화 및 전체목록 출력
	 * @param event
	 */
	@FXML public void resetAction(ActionEvent event) {
		searchCombo.getSelectionModel().select(0);
		searchTf.clear();
		userList();
		
	}

}
