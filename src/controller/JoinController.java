package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JoinController implements Initializable {

	@FXML Button joinBt;
	@FXML Button idCheckBt;
	@FXML TextField idTf;
	@FXML PasswordField pwTf;
	@FXML PasswordField pwConfirmTf;
	@FXML TextField nameTf;
	@FXML ComboBox<String> telCh;
	@FXML TextField telFirst;
	@FXML TextField telSecond;
	@FXML TextField addr;
	@FXML TextField jumin1Tf;
	@FXML TextField jumin2Tf;
	@FXML Button cancelBt;

	boolean idCheck = false;
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	@FXML Label pwChk;
	
	/**
	 * üũ���� ��� ���̾�α�
	 * @param failInfo
	 * @param flag
	 */
	public void CheckDia(String failInfo,boolean flag) {
		Stage dialog = new Stage(StageStyle.TRANSPARENT);
		dialog.initModality(Modality.WINDOW_MODAL);
		Stage primaryStage = (Stage) idTf.getScene().getWindow();
		dialog.initOwner(primaryStage);
		try {
			dialog.setTitle("����");
			Parent parent = FXMLLoader.load(getClass().getResource("../fxml/FailDialog.fxml"));
			Button btnOk = (Button) parent.lookup("#btnOk");
			Label label = (Label) parent.lookup("#txtTitle");
			label.setText(failInfo);
			if(flag) {
				dialog.setTitle("Ȯ��");
				ImageView image = (ImageView) parent.lookup("#image");
				image.setImage(new Image(getClass().getResource("../fxml/images/dialog-info.png").toString()));
			}
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
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		pwConfirmTf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.equals(pwTf.getText())) {
					pwChk.setText("��й�ȣ�� �����մϴ�.");
					pwChk.setTextFill(new Color(0,0,0,1));
				}else {
					pwChk.setText("��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
					pwChk.setTextFill(new Color(1,0,0,1));
				}
			}
		});
		pwTf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.equals(pwConfirmTf.getText())) {
					pwChk.setText("��й�ȣ�� �����մϴ�.");
					pwChk.setTextFill(new Color(0,0,0,1));
				}else {
					pwChk.setText("��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
					pwChk.setTextFill(new Color(1,0,0,1));
				}
			}
		});
		telCh.getSelectionModel().select("010");
	}
	
	/**
	 * ���̵� �ߺ�üũ
	 * @param event
	 */
	@FXML public void idCheckAction(ActionEvent event) {
		if(idTf.getText().trim().length()!=0) {
			try {
				conn = ConnUtil.getConnection();
				String id = idTf.getText().trim();
				String sql = "select user_id from user_table where user_id=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					if(rs.getString("user_id").equals(id)) {
						CheckDia(id+"��(��) �ߺ��� ���̵��Դϴ�.",false);
						idTf.setText("");
						idTf.requestFocus();
						return;
					}
				}
					CheckDia(id+"��(��) ��밡���� ���̵��Դϴ�.", true);
					idCheck=true;
					idTf.setDisable(true);
					
					pwTf.requestFocus();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}else {
			CheckDia("���̵� �Է����ּ���.",false);
			idTf.requestFocus();
		}
	}

	/**
	 * ȸ������ ���
	 * @param event
	 */
	@FXML public void cancelAction(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Login.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = (Stage) cancelBt.getScene().getWindow();
			stage.setTitle("�α���");
			stage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ȸ������ �̺�Ʈ
	 * @param event
	 */
	@FXML public void joinAction(ActionEvent event) {
		if(!idCheck) {
			CheckDia("���̵� �ߺ�üũ�� ���ּ���.", false);idTf.requestFocus();
			return;
		}
		String id = idTf.getText();
		String pw = pwTf.getText();
		String confirmPw = pwConfirmTf.getText();
		String name = nameTf.getText();
		String jumin = jumin1Tf.getText()+jumin2Tf.getText();
		String tel = telCh.getSelectionModel().getSelectedItem()+"-"+telFirst.getText()+"-"+telSecond.getText();
		String address = addr.getText();
		if(!pw.equals(confirmPw)) {
			CheckDia("��й�ȣȮ���� Ʋ���ϴ�.", false); pwConfirmTf.requestFocus();return;
		}
		if(id.trim().length()==0) { CheckDia("���̵� �Է����ּ���.", false);idTf.requestFocus();return;}
		if(pw.trim().length()==0) { CheckDia("��й�ȣ�� �Է����ּ���.", false); pwTf.requestFocus();return;}
		if(confirmPw.trim().length()==0) { CheckDia("��й�ȣȮ���� �Է����ּ���.", false); pwConfirmTf.requestFocus();return;}
		if(name.trim().length()==0) { CheckDia("�̸��� �Է����ּ���.", false); nameTf.requestFocus();return;}
		if(jumin.trim().length()==0) { CheckDia("�ֹι�ȣ�� �Է����ּ���.", false); jumin1Tf.requestFocus();return;}
		if(telFirst.getText().trim().length()<3||telSecond.getText().trim().length()<4) { CheckDia("��ȭ��ȣ�� �Է����ּ���.", false); telFirst.requestFocus();return;}
		
		
		// �ֹε�Ϲ�ȣ ��ȿ�� üũ
		String[] imsi = jumin.split("");
		int[] juminInt = new int[14];
		for(int i=0;i<imsi.length;i++) {
			juminInt[i] = Integer.parseInt(imsi[i]);
		}
		for(int i = 0;i<=7;i++){
			juminInt[juminInt.length-1]+=(juminInt[i]*(i+2));
		}
		for(int i = 8;i<=11;i++){
			juminInt[juminInt.length-1]+=(juminInt[i]*(i-6));
		}
		int temp = juminInt[13]%11;
		int total = 11-temp;
		if(total!=juminInt[12]) {
			CheckDia("�߸��� �ֹι�ȣ�Դϴ�.", false);
			return;
		}
		try {
			conn = ConnUtil.getConnection();
			String sql = "insert into user_table values(?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, name);
			pstmt.setLong(4, Long.parseLong(jumin));
			pstmt.setString(5, tel);
			pstmt.setString(6, address);
			pstmt.setString(7,"0");
			pstmt.executeUpdate();
			CheckDia("ȸ�����Կ� �����Ͽ����ϴ�.", true);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Login.fxml"));
			try {
				Parent root = loader.load();
				Scene scene = new Scene(root);
				Stage stage = (Stage) idTf.getScene().getWindow();
				stage.setTitle("�α���");
				stage.setScene(scene);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}			
	}//end joinAction
}//end class
