package controller.user;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import controller.ConnUtil;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UserMain implements Initializable {

	@FXML Button logoutBt;
	@FXML Label userId;

	@FXML Button movieInfoBt;
	@FXML Button ticketingBt;
	@FXML Button myTicketBt;
	@FXML Button myMovieBt;
	private String id;
	@FXML Button userEditBt;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void userId(String text) {
		userId.setText(text);
	}
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

	@FXML public void logoutAction(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("로그인");
			stage.centerOnScreen();
			stage.setScene(scene);
			Stage beforeStage = (Stage) userId.getScene().getWindow();
			beforeStage.close();
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML public void movieInfoAction(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/MovieInfo.fxml"));
			Parent root = loader.load();
			MovieInfo umc = loader.getController();
			umc.userId.setText(this.userId.getText());
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("영화정보");
			stage.setResizable(false);
			stage.centerOnScreen();
			stage.setScene(scene);
			Stage beforeStage = (Stage) userId.getScene().getWindow();
			beforeStage.close();
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML public void ticketingAction(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/Ticketing.fxml"));
			Parent root = loader.load();
			Ticketing umc = loader.getController();
			umc.userId.setText(this.userId.getText());
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("예매하기");
			stage.setResizable(false);
			stage.centerOnScreen();
			stage.setScene(scene);
			Stage beforeStage = (Stage) userId.getScene().getWindow();
			beforeStage.close();
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML public void myTicketAction(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/MyTicket.fxml"));
			Parent root = loader.load();
			MyTicket umc = loader.getController();
			umc.userId.setText(this.userId.getText());
			umc.load(this.userId.getText());
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("나의 예매정보");
			stage.setResizable(false);
			stage.centerOnScreen();
			stage.setScene(scene);
			Stage beforeStage = (Stage) userId.getScene().getWindow();
			beforeStage.close();
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@FXML public void myMovieAction(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/MyMovie.fxml"));
			Parent root = loader.load();
			MyMovie umc = loader.getController();
			umc.userId.setText(this.userId.getText());
			umc.loadMyMovie(this.userId.getText());
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("내가 본 영화");
			stage.setResizable(false);
			stage.centerOnScreen();
			stage.setScene(scene);
			Stage beforeStage = (Stage) userId.getScene().getWindow();
			beforeStage.close();
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML public void userEditAction(ActionEvent event) {
		Stage dialog = new Stage(StageStyle.UTILITY);
		dialog.initModality(Modality.WINDOW_MODAL);
		Stage stage = (Stage) userId.getScene().getWindow();
		dialog.initOwner(stage);
		dialog.setTitle("내정보수정");
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/UserEdit.fxml"));
			TextField idTf = (TextField) root.lookup("#idTf");
			Button pwChange = (Button) root.lookup("#pwChangeBt");
			TextField nameTf = (TextField) root.lookup("#nameTf");
			TextField jumin1Tf = (TextField) root.lookup("#jumin1Tf");
			PasswordField jumin2Tf = (PasswordField) root.lookup("#jumin2Tf");
			@SuppressWarnings("unchecked")
			ComboBox<String> telCh = (ComboBox<String>) root.lookup("#telCh");
			TextField telFirst = (TextField) root.lookup("#telFirst");
			TextField telSecond = (TextField) root.lookup("#telSecond");
			TextField addrTf = (TextField) root.lookup("#addrTf");
			Button cancel = (Button) root.lookup("#cancel");
			Button save = (Button) root.lookup("#save");
			conn = ConnUtil.getConnection();
			String sql = "select * from user_table where user_id =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId.getText());
			rs = pstmt.executeQuery();
			rs.next();		
			String userId = rs.getString("user_id");
			String userPw = rs.getString("user_pw");
			String userName = rs.getString("user_name");
			String userJumin = String.valueOf(rs.getLong("user_jumin"));
			String userTel = rs.getString("user_tel");
			String userAddr = rs.getString("user_addr");
			
			idTf.setText(userId);
			nameTf.setText(userName);
			String jumin1 = userJumin.substring(0,6);
			String jumin2 = userJumin.substring(6);
			jumin1Tf.setText(jumin1);
			jumin2Tf.setText(jumin2);
			String tel[] = userTel.split("-");
			telCh.getSelectionModel().select(tel[0]);
			telFirst.setText(tel[1]);
			telSecond.setText(tel[2]);
			addrTf.setText(userAddr);
			pwChange.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Stage changeDialog = new Stage(StageStyle.UTILITY);
					changeDialog.initModality(Modality.WINDOW_MODAL);
					Stage stage = (Stage) pwChange.getScene().getWindow();
					changeDialog.initOwner(stage);
					changeDialog.setTitle("비밀번호변경");
					try {
						Parent root = FXMLLoader.load(getClass().getResource("/fxml/ChangePw.fxml"));
						PasswordField oldPw = (PasswordField) root.lookup("#oldPw");
						PasswordField newPw = (PasswordField) root.lookup("#newPw");
						PasswordField newConfirmPw = (PasswordField) root.lookup("#newConfirmPw");
						Label checkPw = (Label) root.lookup("#checkPw");
						Button close = (Button) root.lookup("#closeBt");
						Button change = (Button) root.lookup("#changeBt");
						close.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								changeDialog.close();
							}
						});
						change.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								checkPw.setText("");
								if(oldPw.getText().isEmpty()) {return;}
								if(newPw.getText().isEmpty()) {return;}
								if(newConfirmPw.getText().isEmpty()) {return;}
								if(!userPw.equals(oldPw.getText())){
									checkPw.setText("현재 비밀번호가 틀립니다.");
									return;
								}
								if(!newPw.getText().equals(newConfirmPw.getText())) {
									checkPw.setText("비밀번호 확인이 틀립니다.");
									return;
								}
								changePw(userId, newPw.getText());
								changeDialog.close();
								Popup popDialog = new Popup();
								Stage primaryStage = (Stage) pwChange.getScene().getWindow();
								try {
									Parent parent = FXMLLoader.load(getClass().getResource("/fxml/admin/ErrDialog.fxml"));
									Button btnOk = (Button) parent.lookup("#btnOk");
									Label txtLabel = (Label) parent.lookup("#txtTitle");
									txtLabel.setText("비밀번호 변경에 성공하였습니다.");
									btnOk.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
											popDialog.hide();
										}
									});
									popDialog.getContent().add(parent);
									popDialog.setAutoHide(true);
									popDialog.show(primaryStage);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						});
						Scene scene = new Scene(root);
						changeDialog.setScene(scene);
						changeDialog.show();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			cancel.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					dialog.close();
				}
			});
			save.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						conn = ConnUtil.getConnection();
						String sql = "update user_table set user_tel = ?,user_addr=? where user_id =?";
						pstmt = conn.prepareStatement(sql);
						String tel = telCh.getSelectionModel().getSelectedItem()+"-"+telFirst.getText()+"-"+telSecond.getText();
						pstmt.setString(1, tel);
						pstmt.setString(2, addrTf.getText());
						pstmt.setString(3, userId);
						pstmt.executeUpdate();
						Popup popDialog = new Popup();
						Stage primaryStage = (Stage) pwChange.getScene().getWindow();
						Parent parent = FXMLLoader.load(getClass().getResource("/fxml/admin/ErrDialog.fxml"));
						Button btnOk = (Button) parent.lookup("#btnOk");
						Label txtLabel = (Label) parent.lookup("#txtTitle");
						txtLabel.setText("내정보 변경에 성공하였습니다.");
						btnOk.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								popDialog.hide();
							}
						});
						popDialog.getContent().add(parent);
						popDialog.setAutoHide(true);
						popDialog.show(primaryStage);
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
						try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
						try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
					}
				}
			});
			Scene scene = new Scene(root);
			dialog.setScene(scene);
			dialog.show();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	public void changePw(String uId, String uPw) {
		try {
			conn = ConnUtil.getConnection();
			String sql = "update user_table set user_pw = ? where user_id =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, uPw);
			pstmt.setString(2, uId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
	}
	
}
