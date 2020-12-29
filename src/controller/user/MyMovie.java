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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import controller.ConnUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MyMovie implements Initializable {

	@FXML Label userId;
	@FXML ComboBox<String> yearCombo;
	@FXML Button yearSearch;
	@FXML GridPane movieList;
	@FXML Button closeBt;

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

	public void list(ResultSet rs,String sYear) throws SQLException {
		
		
		int row = 0;
		while(rs.next()) {
			String timage = rs.getString("movie_image");
			String tmovieName = rs.getString("movie_name");
			String ttheaterName = rs.getString("theater_name");
			String tgwanName = rs.getString("gwan_name");
			Date tdate = rs.getDate("ticket_date");
			String ttime = rs.getString("ticket_time");
			AnchorPane listPane = new AnchorPane();
			listPane.setPrefHeight(150);
			listPane.setPrefWidth(370);
			ImageView image = new ImageView();
			image.setFitHeight(110);image.setFitWidth(80);
			image.setLayoutX(10); image.setLayoutY(10); 
			image.setPickOnBounds(true);image.setPreserveRatio(true);
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
				File jpgImage = new File(timage);
				fis = new FileInputStream(jpgImage);
				bis = new BufferedInputStream(fis);
				Image img = new Image(bis);
				image.setImage(img);
			} catch (FileNotFoundException e) {
			} finally {
				try {if(bis!=null)bis.close();} catch (IOException e) {e.printStackTrace();	}
				try {if(fis!=null)fis.close();} catch (IOException e) {e.printStackTrace();	}
			}
			Label movieName = new Label();
			Label theaterAndGwanName = new Label();
			Label dateAndTime = new Label();
			movieName.setLayoutX(105); movieName.setLayoutY(10);
			movieName.setPrefHeight(23); movieName.setPrefWidth(200);
			movieName.setText(tmovieName);
			theaterAndGwanName.setLayoutX(105); theaterAndGwanName.setLayoutY(90);
			theaterAndGwanName.setText(ttheaterName+" / "+tgwanName);
			dateAndTime.setLayoutX(105); dateAndTime.setLayoutY(60);
			dateAndTime.setText(tdate.toString()+" / "+ttime);
			listPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1), new Insets(1))));
			listPane.getChildren().addAll(image,movieName,theaterAndGwanName,dateAndTime);
			row++;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String year = sdf.format(tdate);
			if(!yearCombo.getItems().contains(year)) {
				yearCombo.getItems().add(year);
			}
			if(sYear != null) {
				if(year.equals(sYear)||sYear.equals("전체")) {
					movieList.add(listPane, 0, row); 
				}
			}else if(sYear==null){
				movieList.add(listPane, 0, row);
			}
		}
	}
	
	
	public void loadMyMovie(String id) {
		try {
			conn = ConnUtil.getConnection();
			String sql = "select * from ticket join movie using(movie_name) where user_id = ? and ticket_date<sysdate order by ticket_date desc";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			yearCombo.getItems().add("전체");
			yearCombo.getSelectionModel().select("전체");
			list(rs , null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@FXML public void yearSearchAction(ActionEvent event) {
		String sYear = yearCombo.getSelectionModel().getSelectedItem();
		movieList.getChildren().clear();
		try {
			conn = ConnUtil.getConnection();
			String sql = "select * from ticket join movie using(movie_name) where user_id = ? and ticket_date<sysdate order by ticket_date desc";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId.getText());
			rs = pstmt.executeQuery();
			list(rs,sYear);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@FXML public void closeAction(ActionEvent event) {
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
			Stage beforeStage = (Stage) closeBt.getScene().getWindow();
			beforeStage.close();
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
