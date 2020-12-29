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
import java.util.Date;
import java.util.ResourceBundle;

import controller.ConnUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MyTicket implements Initializable {

	private String id;
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	@FXML Label userId;
	@FXML Button closeBt;
	@FXML Button printBt;
	@FXML GridPane ticketList;
	Button cancel;
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	/**
	 * 예매목록 로드 (오늘까지)
	 * @param id
	 */
	public void load(String id) {
		ticketList.getChildren().clear();
		setId(id);
		try {
			conn = ConnUtil.getConnection();
			String sql = "select * from ticket join movie using(movie_name) where ticket_cancel='결제' and user_id=? and ticket_date>sysdate-1 "
					+ "order by ticket_date";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			int row = 0;
			while(rs.next()) {
				String tNum = String.valueOf(rs.getInt("ticket_num"));
				String tMname = rs.getString("movie_name");
				String tTname = rs.getString("theater_name");
				String tGname = rs.getString("gwan_name");
				Date tDate = rs.getDate("ticket_date");
				String tTime = rs.getString("ticket_time");
				String tSeat = rs.getString("ticket_seat");
				String tMoney = String.valueOf(rs.getInt("ticket_money"));
				AnchorPane listPane = new AnchorPane();
				listPane.setPrefHeight(246);
				listPane.setPrefWidth(585);
				ImageView image = new ImageView();
				image.setFitHeight(190);image.setFitWidth(150);
				image.setLayoutX(25); image.setLayoutY(40); 
				image.setPickOnBounds(true);image.setPreserveRatio(true);
				String movieImage = rs.getString("movie_image");
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try {
					File jpgImage = new File(movieImage);
					fis = new FileInputStream(jpgImage);
					bis = new BufferedInputStream(fis);
					Image img = new Image(bis);
					image.setImage(img);
				} catch (FileNotFoundException e) {
				} finally {
					try {if(bis!=null)bis.close();} catch (IOException e) {e.printStackTrace();	}
					try {if(fis!=null)fis.close();} catch (IOException e) {e.printStackTrace();	}
				}
				Label lnum = new Label("예매번호"); lnum.setLayoutX(33); lnum.setLayoutY(14);
				Label ltname = new Label("극장명"); ltname.setLayoutX(190); ltname.setLayoutY(75); 
				Label lgname = new Label("상영관명"); lgname.setLayoutX(190); lgname.setLayoutY(100);
				Label ldate = new Label("날짜"); ldate.setLayoutX(190); ldate.setLayoutY(125); 
				Label ltime = new Label("시간"); ltime.setLayoutX(190); ltime.setLayoutY(150); 
				Label lseat = new Label("좌석정보"); lseat.setLayoutX(409); lseat.setLayoutY(75); 
				Label lmoney = new Label("결제금액"); lmoney.setLayoutX(190); lmoney.setLayoutY(175); 
				Label movieName = new Label(tMname); movieName.setLayoutX(190); movieName.setLayoutY(40); movieName.setFont(new Font(18));
				Label ticketNum = new Label(tNum); ticketNum.setLayoutX(89); ticketNum.setLayoutY(14); ticketNum.setPrefHeight(14); ticketNum.setPrefWidth(101);
				Label theaterName = new Label(tTname); theaterName.setLayoutX(250); theaterName.setLayoutY(75);  
				Label gwanName = new Label(tGname); gwanName.setLayoutX(250); gwanName.setLayoutY(100);  
				Label date = new Label(tDate.toString()); date.setLayoutX(250); date.setLayoutY(125);  
				Label time = new Label(tTime); time.setLayoutX(250); time.setLayoutY(150);  
				Label pay = new Label(tMoney); pay.setLayoutX(250); pay.setLayoutY(175);  
				Label seat = new Label(tSeat); seat.setLayoutX(409); seat.setLayoutY(100); seat.setAlignment(Pos.TOP_LEFT); seat.prefHeight(50); seat.setPrefWidth(150);
				cancel = new Button("취소하기");
				cancel.setLayoutX(512); cancel.setLayoutY(10); 
				cancel.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						try {
							Stage dialog = new Stage(StageStyle.UNDECORATED);
							dialog.initModality(Modality.WINDOW_MODAL);
							dialog.initOwner(ticketList.getScene().getWindow());
							Parent root = FXMLLoader.load(getClass().getResource("/fxml/user/ChooseDialog.fxml"));
							Button yes = (Button) root.lookup("#yes");
							Button no = (Button) root.lookup("#no");
							no.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									dialog.close();
								}
							});
							yes.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									try {
										conn = ConnUtil.getConnection();
										String sql = "update ticket set ticket_cancel='취소' where TICKET_NUM=? ";
										pstmt = conn.prepareStatement(sql);
										pstmt.setInt(1, Integer.parseInt(ticketNum.getText()));
										pstmt.executeUpdate();
										
										load(getId());
										dialog.close();
									} catch (SQLException e) {
										e.printStackTrace();
									} finally {
										try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
										try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
									}
								}
							});
							Scene scene = new Scene(root);
							dialog.setScene(scene);
							dialog.setResizable(false);
							dialog.show();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				listPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, 
						BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(4), new Insets(1))));
				listPane.getChildren().addAll(image,lnum,ltname,lgname,ldate,ltime,lseat,lmoney,movieName,ticketNum,theaterName,gwanName,date,time,pay,seat,cancel);
				ticketList.add(listPane, 0, row);
				row++;
			}
			if(ticketList.getChildren().size()==0) {
				Label notice = new Label("예매하신 영화가 없습니다.");
				notice.setFont(new Font(24));
				notice.setPrefWidth(600);
				notice.setPrefHeight(150);
				notice.setAlignment(Pos.CENTER);
				ticketList.add(notice, 0, 0);
				printBt.setDisable(true);
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
	 * 닫기버튼
	 * @param event
	 */
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

	/**
	 * 인쇄하기
	 * @param event
	 */
	@FXML public void printAction(ActionEvent event) {
		PrinterJob job = PrinterJob.createPrinterJob();
		 if (job != null && job.showPrintDialog(ticketList.getScene().getWindow())) {
		  PageLayout pageLayout = job.getJobSettings().getPageLayout();
		  double scaleX = 1.0;
		  if (pageLayout.getPrintableWidth() < ticketList.getBoundsInParent().getWidth()) {
		   scaleX = pageLayout.getPrintableWidth() / ticketList.getBoundsInParent().getWidth();
		  }
		  double scaleY = 1.0;
		  if (pageLayout.getPrintableHeight() < ticketList.getBoundsInParent().getHeight()) {
		   scaleY = pageLayout.getPrintableHeight() / ticketList.getBoundsInParent().getHeight();
		  }
		  double scaleXY = Double.min(scaleX, scaleY);
		  Scale scale = new Scale(scaleXY, scaleXY);
		  ticketList.getChildren().remove(cancel);
		  ticketList.getTransforms().add(scale);
		  boolean success = job.printPage(ticketList);
		  ticketList.getTransforms().remove(scale);
		  if (success) {
		   job.endJob();
		  }
		 }
	}
	
	
	

}
