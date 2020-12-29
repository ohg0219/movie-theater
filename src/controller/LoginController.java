package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import controller.admin.AdminMain;
import controller.user.UserMain;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController implements Initializable {
	
	@FXML TextField idTf;
	@FXML PasswordField pwTf;
	@FXML Button loginBt;
	@FXML Button joinBt;
	@FXML Button findBt;
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
	
	/**
	 * �α��� ���� ���̾�α�
	 */
	public void loginFail() {
		Stage dialog = new Stage(StageStyle.UNIFIED);
		dialog.initModality(Modality.WINDOW_MODAL);
		Stage primaryStage = (Stage) idTf.getScene().getWindow();
		dialog.initOwner(primaryStage);
		dialog.setTitle("�α��� ����");
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("../fxml/FailDialog.fxml"));
			Button btnOk = (Button) parent.lookup("#btnOk");
			Label label = (Label) parent.lookup("#txtTitle");
			label.setText("�α��ο� �����Ͽ����ϴ�.");
			pwTf.clear();
			btnOk.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					dialog.close();
				}
			});
			btnOk.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					dialog.close();
					
				}
			});
			Scene scene = new Scene(parent);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �α��� �׼� (�����ڿ� ������� �б���)
	 * @param event
	 * @throws IOException
	 */
	@FXML public void loginAction(ActionEvent event) throws IOException {
		String id = idTf.getText();
		String pw = pwTf.getText();
		try {
			conn = ConnUtil.getConnection();
			String sql = "Select user_id,user_pw from user_table where user_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String dbId = rs.getString("user_id");
				String dbPw = rs.getString("user_pw");
				if(dbId.equals(id)) {
					if(dbPw.equals(pw)){
						System.out.println("�α��μ���");
						if(id.equals("admin")) {
							FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/AdminMain.fxml"));
							Parent root = loader.load();
							AdminMain umc = loader.getController();
							umc.userId(id);
							umc.setId(id);
							Scene scene = new Scene(root);
							Stage stage = new Stage();
							stage.centerOnScreen();
							stage.setScene(scene);
							stage.setTitle("�����ڸ��");
							Stage beforeStage = (Stage) loginBt.getScene().getWindow();
							beforeStage.close();
							stage.show();
							return;
						}else {
							FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/user/UserMain.fxml"));
							Parent root = loader.load();
							UserMain umc = loader.getController();
							umc.userId(id);
							Scene scene = new Scene(root);
							Stage stage = new Stage();
							stage.centerOnScreen();
							stage.setScene(scene);
							stage.setTitle(id+ "�� ȯ���մϴ�.");
							Stage beforeStage = (Stage) loginBt.getScene().getWindow();
							beforeStage.close();
							stage.show();
							return;
						}
					}
				}
			}
			loginFail();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
				try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
	}

	/**
	 * ȸ�����Թ�ư Ŭ��
	 * @param event
	 */
	@FXML public void joinAction(ActionEvent event) {
		try {
			Stage stage = (Stage) joinBt.getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("../fxml/Join.fxml"));
			Scene scene = new Scene(root);
			stage.setTitle("ȸ������");
			stage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * ������� ���̾�α�
	 * @throws IOException 
	 */
	public void dialog(String message, boolean flag, Button event) throws IOException {
		Stage okDialog = new Stage(StageStyle.TRANSPARENT);
		okDialog.initModality(Modality.WINDOW_MODAL);
		Stage primaryStage = (Stage) event.getScene().getWindow();
		okDialog.initOwner(primaryStage);
		Parent parent = FXMLLoader.load(getClass().getResource("../fxml/FailDialog.fxml"));
		Button btnOk = (Button) parent.lookup("#btnOk");
		
		if(flag == true) {
			ImageView image = (ImageView) parent.lookup("#image");
			image.setImage(new Image(getClass().getResource("../fxml/images/dialog-info.png").toString()));
		}
		btnOk.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				okDialog.close();
			}
		});
		btnOk.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				okDialog.close();
			}
		});
		Label label = (Label) parent.lookup("#txtTitle");
		label.setText(message);
		Scene scene = new Scene(parent);
		okDialog.setScene(scene);
		okDialog.setResizable(false);
		okDialog.show();
	}
	
	/**
	 * ���̵� / ��� ã��
	 * @param event
	 */
	@FXML public void findAction(ActionEvent event) {
		Stage dialog = new Stage(StageStyle.UTILITY);
		dialog.initModality(Modality.WINDOW_MODAL);
		Stage stage = (Stage) findBt.getScene().getWindow();
		dialog.initOwner(stage);
		dialog.setTitle("���̵�/��й�ȣ ã��");
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/FindIdPw.fxml"));
			TextField idNameTf = (TextField) root.lookup("#idNameTf");
			TextField idJumin1Tf = (TextField) root.lookup("#idJumin1Tf");
			PasswordField idJumin2Tf = (PasswordField) root.lookup("#idJumin2Tf");
			Button findId = (Button) root.lookup("#findId");
			findId.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if(idNameTf.getText().isEmpty()) {idNameTf.requestFocus(); return;}
					if(idJumin1Tf.getText().isEmpty()) {idJumin1Tf.requestFocus(); return;}
					if(idJumin2Tf.getText().isEmpty()) {idJumin2Tf.requestFocus(); return;}
					try {
						conn = ConnUtil.getConnection();
						String sql = "select user_id from user_table where user_name=? and user_jumin=? and user_type=0";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, idNameTf.getText());
						long jumin = Long.parseLong(idJumin1Tf.getText()+idJumin2Tf.getText());
						pstmt.setLong(2, jumin);
						rs = pstmt.executeQuery();
						if(rs.next()) {
							String getId = rs.getString("user_id");
							dialog("ȸ������ ���̵�� "+getId+"�Դϴ�", true, findId);
						}else {
							dialog("��ġ�ϴ� ������ �����ϴ�.", false, findId);
							return;
						}
					} catch (SQLException | IOException e) {
					} catch (NumberFormatException e) {
						try{
							dialog("��Ȯ�� �ֹε�Ϲ�ȣ�� �Է��ϼ���", false, findId);
							return;
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			TextField pwIdTf = (TextField) root.lookup("#pwIdTf");
			TextField pwNameTf = (TextField) root.lookup("#pwNameTf");
			TextField pwJumin1Tf = (TextField) root.lookup("#pwJumin1Tf");
			PasswordField pwJumin2Tf = (PasswordField) root.lookup("#pwJumin2Tf");
			Button findPw = (Button) root.lookup("#findPw");
			findPw.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					System.out.println("???");
					if(pwIdTf.getText().isEmpty()) {pwIdTf.requestFocus(); return;}
					if(pwNameTf.getText().isEmpty()) {pwNameTf.requestFocus(); return;}
					if(pwJumin1Tf.getText().isEmpty()) {pwJumin1Tf.requestFocus(); return;}
					if(pwJumin2Tf.getText().isEmpty()) {pwJumin2Tf.requestFocus(); return;}
					try {
						System.out.println("222222222");
						conn = ConnUtil.getConnection();
						String sql = "select user_id from user_table where user_id=? and user_name=? and user_jumin=? and user_type=0";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, pwIdTf.getText());
						pstmt.setString(2, pwNameTf.getText());
						long jumin = Long.parseLong(pwJumin1Tf.getText()+pwJumin2Tf.getText());
						pstmt.setLong(3, jumin);
						rs = pstmt.executeQuery();
						System.out.println("33333333");
						if(rs.next()) {
							System.out.println("44444444444");
							Stage newPwDialog = new Stage(StageStyle.UTILITY);
							newPwDialog.initModality(Modality.WINDOW_MODAL);
							Stage stage = (Stage) findPw.getScene().getWindow();
							newPwDialog.initOwner(stage);
							newPwDialog.setTitle("��й�ȣ �缳��");
							Parent root = FXMLLoader.load(getClass().getResource("/fxml/NewPw.fxml"));
							PasswordField newPw = (PasswordField) root.lookup("#newPw");
							PasswordField newConfirmPw = (PasswordField) root.lookup("#newConfirmPw");
							Button cancel = (Button) root.lookup("#cancel");
							Button changePw = (Button) root.lookup("#changePw");
							Label checkPw = (Label) root.lookup("#checkPw");
							System.out.println("55555555");
							cancel.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									newPwDialog.close();
								}
							});
							cancel.setOnKeyPressed(new EventHandler<KeyEvent>() {
								@Override
								public void handle(KeyEvent event) {
									newPwDialog.close();
								}
							});
							changePw.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									if(newPw.getText().isEmpty()) {newPw.requestFocus();return;}
									if(newConfirmPw.getText().isEmpty()) {newConfirmPw.requestFocus();return;}
									if(newPw.getText().equals(newConfirmPw.getText())) {
										try {
											conn = ConnUtil.getConnection();
											String sql = "update user_table set user_pw =? where user_id=?";
											pstmt = conn.prepareStatement(sql);
											pstmt.setString(1, newPw.getText());
											pstmt.setString(2, pwIdTf.getText());
											pstmt.executeUpdate();
											dialog("��й�ȣ ���濡 �����Ͽ����ϴ�.", true, findBt);
											newPwDialog.close();
											dialog.close();
										} catch (SQLException | IOException e) {
											e.printStackTrace();
										}
									}else {
										checkPw.setText("��й�ȣȮ���� ��ġ���� �ʽ��ϴ�.");
										return;
									}
								}
							});
							Scene scene = new Scene(root);
							newPwDialog.setScene(scene);
							newPwDialog.show();
						}else {
							dialog("��ġ�ϴ� ������ �����ϴ�.", false, findPw);
							return;
						}
					} catch (SQLException | IOException e) {
					} catch (NumberFormatException e) {
						try {
							dialog("��Ȯ�� �ֹε�Ϲ�ȣ�� �Է��ϼ���", false, findPw);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						return;
					}
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
	 * �ؽ�Ʈ�ʵ忡�� ���Ͱ��Է½� ��Ŀ�� ����
	 * @param event
	 * @throws IOException
	 */
	@FXML public void Enter(ActionEvent event) throws IOException {
		if(event.getSource()==idTf) {pwTf.requestFocus();}
		if(event.getSource()==pwTf) {loginAction(event);}
	}



}
