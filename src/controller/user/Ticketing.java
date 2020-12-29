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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import controller.ConnUtil;
import controller.vo.TikectingVO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Ticketing implements Initializable {

	@FXML Button backGo;
	@FXML ImageView selImage;
	@FXML Label selName;
	@FXML Button seatChoice;
	@FXML Button resetBt;
	@FXML Label selTheaterName;
	@FXML Label selGwanName;
	@FXML Label selDate;
	@FXML Label selTime;
	@FXML ComboBox<String> movieSort;
	@FXML FlowPane regionBt;
	@FXML GridPane movieList;
	@FXML GridPane theaterList;
	@FXML GridPane dateList;
	@FXML GridPane timeList;
	@FXML Label userId;
	private String id;
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ArrayList<TikectingVO> list = new ArrayList<TikectingVO>();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		for(int i=1;i<regionBt.getChildren().size();i++) {Button bt= (Button) regionBt.getChildren().get(i);bt.setDisable(true);}
		seatChoice.setDisable(true);
		setMovieList();
		allListLoad();
		movieListAction();
		regionListAction();
	}

	/**
	 * ������ ��ȭ��� ��������
	 */
	public void setMovieList() {
		try {
			conn = ConnUtil.getConnection();
			String sql = "select distinct (movie_name) "
					+ "from gwan join movie using (movie_num) "
					+ "where movie_num>0 and movie_endday>sysdate";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int i=0;
			while(rs.next()) {
				String movieName = rs.getString("movie_name");
				Button button = new Button(movieName);
				button.setPrefWidth(185);
				button.setPrefHeight(30);
				movieList.add(button, 0, i);
				i++;
			}
		} catch (SQLException e) {
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
	}
	
	/**
	 * ��ȭ���� �Ѿ����
	 */
	public void selectMovie(String movieName) {
		movieListAction();
		for(int i =0;i<movieList.getChildren().size();i++) {
			Button button = (Button) movieList.getChildren().get(i);
			if(movieName.equals(button.getText())) {
				button.fire();
			}
		}
	}
	
	
	/**
	 * ��ȭ Ŭ���� ������ ���� ��ư Ȱ��ȭ
	 */
	public void movieListAction() {
		for(int i = 0;i< movieList.getChildren().size();i++) {
			Button button = (Button) movieList.getChildren().get(i);
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Button button = (Button) event.getSource();
					String movieName = button.getText();
					selName.setText(movieName);
					for(int i =0;i<list.size();i++) {
						if(list.get(i).getMovieName().equals(movieName)) {
							//������ ��ȭ�� �̹��� �ּ� ��������
							String image = list.get(i).getMovieImage();
							FileInputStream fis = null;
							BufferedInputStream bis = null;
							try {
								File jpgImage = new File(image);
								fis = new FileInputStream(jpgImage);
								bis = new BufferedInputStream(fis);
								Image img = new Image(bis);
								selImage.setImage(img);
							} catch (FileNotFoundException e) {
							} finally {
								try {if(bis!=null)bis.close();} catch (IOException e) {e.printStackTrace();	}
								try {if(fis!=null)fis.close();} catch (IOException e) {e.printStackTrace();	}
							}
						}
					}//end �̹��� ��������
					for(int i=1;i<regionBt.getChildren().size();i++) {Button bt = (Button) regionBt.getChildren().get(i);bt.setDisable(true);}
					theaterList.getChildren().clear();
					dateList.getChildren().clear();
					timeList.getChildren().clear();
					selTheaterName.setText("");
					for(int i =1;i<regionBt.getChildren().size();i++) {
						Button reBt = (Button) regionBt.getChildren().get(i);
						for(int j =0;j<list.size();j++) {
							if(list.get(j).getMovieName().equals(selName.getText())) {
								String region = list.get(j).getTheaterRegion();//������ ��ȭ��� ���� ������ ��������
								if(reBt.getText().equals(region)) {
									reBt.setDisable(false);
								}
							}
						}
					}
				}
			});
		}
	}
	/**
	 * ��ȭ���� �� ������ ���ý� �ش� �󿵰� ����Ʈ ���
	 */
	public void regionListAction() {
		for(int i = 1; i<regionBt.getChildren().size();i++) {
			Button button = (Button) regionBt.getChildren().get(i);
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					theaterList.getChildren().clear();
					String region = button.getText();
					String movieName = selName.getText();
					int count =0;
					try {
						conn = ConnUtil.getConnection();
						String sql = "select distinct(theater_name) from gwan join movie using (movie_num) "+
								"join theater using (theater_num) "+
								"where movie_num>0 and movie_endday>sysdate "+
								"and theater_region=? and movie_name=?";
						pstmt =conn.prepareStatement(sql);
						pstmt.setString(1, region);
						pstmt.setString(2, movieName);
						rs = pstmt.executeQuery();
						while(rs.next()) {
							String theaterName = rs.getString("theater_name");
							Button theaterBt = new Button(theaterName);
							dateList.getChildren().clear();
							timeList.getChildren().clear();
							//����� ���ý� �Է�
							theaterBt.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									Button thBt = (Button) event.getSource();
									String theaterName = thBt.getText();
									selTheaterName.setText(theaterName);
									dateList();
								}
							});
							theaterBt.setPrefWidth(120);
							theaterList.add(theaterBt, 0, count);
							count++;
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
		}
	}
	/**
	 * ���� ���ý� ��¥ ���
	 * @throws ParseException 
	 */
	public void dateList()  {
		String movieName = selName.getText();
		String theaterName = selTheaterName.getText();
		for(int i =0;i<list.size();i++) {
			if(list.get(i).getMovieName().equals(movieName)&&list.get(i).getTheaterName().equals(theaterName)) {
				try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String start = list.get(i).getMovieStartday();
				String end = list.get(i).getMovieEndday();
				Date startDate = sdf.parse(start);
				Date endDate = sdf.parse(end);
				Date date = new Date(System.currentTimeMillis());
				String dateS = sdf.format(date);
				Date dateD = sdf.parse(dateS);
				ArrayList<String> dates = new ArrayList<String>();
				if(startDate.compareTo(dateD)==-1) {
					//���� ~ ������
					while (dateD.compareTo(endDate) <= 0) {
						dates.add(sdf.format(dateD));
						Calendar c = Calendar.getInstance();
						c.setTime(dateD);
						c.add(Calendar.DAY_OF_MONTH, 1);
						dateD = c.getTime();
					}
				}else if(startDate.compareTo(dateD)>-1) {
					//������ ~ ������
					while (startDate.compareTo(endDate) <= 0) {
						dates.add(sdf.format(startDate));
						Calendar c = Calendar.getInstance();
						c.setTime(startDate);
						c.add(Calendar.DAY_OF_MONTH, 1);
						startDate = c.getTime();
					}
				}
				int count =0;
				for (String date1 : dates) {
					Button button = new Button(date1);
					button.setPrefWidth(180);
					button.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							String day = button.getText();
							selDate.setText(day);
							gwanList(button);
						}
					});
					dateList.add(button, 0, count);
					count++;
				}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * ���� ���ý� �ð� ���
	 */
	public void gwanList(Button button) {
		selTime.setText("");
		seatChoice.setDisable(true);
		timeList.getChildren().clear();
		String movieName = selName.getText();
		String theaterName = selTheaterName.getText();
		int row = 0;
		int col = 0;
		for(int i =0;i<list.size();i++) {
			if(list.get(i).getMovieName().equals(movieName)&&list.get(i).getTheaterName().equals(theaterName)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date;
				try {
					date = sdf.parse(selDate.getText());
					System.out.println(date.toString());
					conn = ConnUtil.getConnection();
					
				} catch (ParseException | SQLException e) {
					e.printStackTrace();
				}
				String gwanName = list.get(i).getGwanName();
				String schedule = list.get(i).getMovieDayschedule();
				String counts[] = schedule.split("=");
				String[] days = counts[1].split("/");//ȸ���� �ð�����
				FlowPane fp = new FlowPane();
				Label label = new Label(gwanName);
				label.setPrefWidth(180);
				fp.getChildren().add(label);
				for(int j=0;j<days.length;j++) {
					String[] times = days[j].split(",");
					String time = times[1];//�󿵽ð�
					Button timeBt = new Button(time);
					timeBt.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							timeSelect(timeBt);
						}
					});
					fp.getChildren().add(timeBt);
					fp.setBorder(new Border(new BorderStroke(null, null, Color.BLACK, null, null, null, BorderStrokeStyle.SOLID, null, CornerRadii.EMPTY, new BorderWidths(1), Insets.EMPTY)));
					fp.setPadding(new Insets(3));
					
				}
				GridPane.setMargin(fp, new Insets(10));
				timeList.add(fp, col, row);
				row++;
			}
		}
	}
	/**
	 * �ð� Ŭ�� �׼�
	 */
	public void timeSelect(Button timeBt) {
		selTime.setText(timeBt.getText());
		FlowPane fp = (FlowPane) timeBt.getParent();
		Label label = (Label) fp.getChildren().get(0);
		selGwanName.setText(label.getText());
		seatChoice.setDisable(false);
	}
	
	/**
	 * ������ ��ȭ�� ��ü���� ��������
	 */
	public void allListLoad() {
		try {
			conn = ConnUtil.getConnection();
			String sql = "select movie_image,movie_num,movie_name,movie_rating,theater_region,theater_num,theater_name,"+
					"gwan_num,gwan_name, movie_startday,movie_endday,movie_dayschedule,"+
					"gwan_seatcount,gwan_seat from gwan join movie using (movie_num)" + 
					"join theater using (theater_num) " + 
					"where movie_num>0 and movie_endday>sysdate";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String movieImage = rs.getString("movie_image");
				int movieNum = rs.getInt("movie_num");
				String movieName = rs.getString("movie_name");
				String movieRating = rs.getString("movie_rating");
				String theaterRegion = rs.getString("theater_region");
				int theaterNum = rs.getInt("theater_num");
				String theaterName = rs.getString("theater_name");
				int gwanNum = rs.getInt("gwan_num");
				String gwanName = rs.getString("gwan_name");
				String movieStartday = rs.getDate("movie_startday").toString();
				String movieEndday = rs.getDate("movie_endday").toString();
				String movieDayschedule = rs.getString("movie_dayschedule");
				int gwanSeatcount = rs.getInt("gwan_seatcount");
				String gwanSeat = rs.getString("gwan_seat");
				list.add(new TikectingVO(movieImage,movieNum, movieName, movieRating, theaterRegion, theaterNum, theaterName, gwanNum, gwanName, movieStartday, movieEndday, movieDayschedule, gwanSeatcount, gwanSeat));
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
	 * �ڷΰ���
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
			stage.setTitle(this.userId.getText()+"�� ȯ���մϴ�.");
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
	 * �¼����� ���̾�α� 
	 * @param event
	 */
	@FXML public void seatChoiceAction(ActionEvent event) {
		try {
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(seatChoice.getScene().getWindow());
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/user/SeatChoice.fxml"));
			dialog.setTitle("�¼�����");
			HBox adult = (HBox) root.lookup("#Dadult"); //���μ�
			HBox student = (HBox) root.lookup("#Dstu"); //�л���
			Button reset = (Button) root.lookup("#Dreset");//�ʱ�ȭ
			Button pay = (Button) root.lookup("#Dpay");//�����ϱ�
			Button cancel = (Button) root.lookup("#Dcancel");
			GridPane seatTable = (GridPane) root.lookup("#DseatTable");//�¼�
			Label movieName = (Label) root.lookup("#DmovieName");//��ȭ��
			Label theaterName = (Label) root.lookup("#DtheaterName");//�����
			Label gwanName = (Label) root.lookup("#DgwanName");//�󿵰���
			Label date = (Label) root.lookup("#Ddate");//��¥
			Label time = (Label) root.lookup("#Dtime");//�ð�
			Label adultmoney = (Label) root.lookup("#DAmoney");//���αݾ�
			Label stumoney = (Label) root.lookup("#DSmoney");//û�ҳ�ݾ�
			Label totalmoney = (Label) root.lookup("#DTmoney");//�ѱݾ�
			Label aPeople = (Label) root.lookup("#aPeople");
			Label sPeople = (Label) root.lookup("#sPeople");
			Label people = (Label) root.lookup("#people");
			Label seat = (Label) root.lookup("#Dseat");//�¼�����
			//ȭ�� �ʱⱸ��
			movieName.setText(selName.getText());
			theaterName.setText(selTheaterName.getText());
			gwanName.setText(selGwanName.getText());
			date.setText(selDate.getText());
			time.setText(selTime.getText());
			pay.setDisable(true);
			ArrayList<String> slist = new ArrayList<String>();
			cancel.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					dialog.close();
				}
			});
			//�ʱ�ȭ ��ư
			reset.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					for(int i =0;i<adult.getChildren().size();i++) {
						if(i==0) {
							Button aBt = (Button) adult.getChildren().get(0);
							Button sBt = (Button) student.getChildren().get(0);
							aBt.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
							aBt.setTextFill(Color.WHITE);
							sBt.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
							sBt.setTextFill(Color.WHITE);
						}else {
							Button aBt = (Button) adult.getChildren().get(i);
							Button sBt = (Button) student.getChildren().get(i);
							aBt.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
							aBt.setTextFill(Color.BLACK);
							sBt.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
							sBt.setTextFill(Color.BLACK);
						}
					}
					aPeople.setText("0");	sPeople.setText("0");	people.setText("0");	slist.clear();	pay.setDisable(true);
					seat.setText("[]");	adultmoney.setText("0");	stumoney.setText("0");	totalmoney.setText("0");
					for(int j = 0;j<seatTable.getChildren().size();j++) {
						Button button = (Button) seatTable.getChildren().get(j);
						button.setTextFill(Color.BLACK);
						button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
					}
				}
			});
			//�����ϱ�
			pay.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					int saram = Integer.parseInt(people.getText());
					int jari = slist.size();
					if(saram!=jari) {return;}
					if(saram==0||jari==0) {return;}
					Stage payDialog = new Stage(StageStyle.UTILITY);
					payDialog.initModality(Modality.WINDOW_MODAL);
					payDialog.initOwner(pay.getScene().getWindow());
					payDialog.setTitle("�����ϱ�");
					try {
						Parent payRoot = FXMLLoader.load(getClass().getResource("/fxml/user/PayDialog.fxml"));
						Label pmName = (Label) payRoot.lookup("#Pmname");
						Label ptName = (Label) payRoot.lookup("#Ptname");
						Label pgName = (Label) payRoot.lookup("#Pgname");
						Label pDate = (Label) payRoot.lookup("#Pdate");
						Label pTime = (Label) payRoot.lookup("#Ptime");
						Label pSeat = (Label) payRoot.lookup("#Pseat");
						Label pMoney = (Label) payRoot.lookup("#Pmoney");
						@SuppressWarnings("unchecked")
						ComboBox<String> pSelect = (ComboBox<String>) payRoot.lookup("#Ppayselect");
						Button pCancel = (Button) payRoot.lookup("#payCancel");
						Button pOk = (Button) payRoot.lookup("#payOk");
						ImageView pView = (ImageView) payRoot.lookup("#Pimage"); 
						//����ȭ�� �ʱ�ȭ ����
						pSelect.getSelectionModel().select(0);
						pmName.setText(movieName.getText());
						ptName.setText(theaterName.getText());
						pgName.setText(gwanName.getText());
						pDate.setText(date.getText());
						pTime.setText(time.getText());
						pSeat.setText(seat.getText());
						pMoney.setText(totalmoney.getText());
						for(int i =0;i<list.size();i++) {
							if(list.get(i).getMovieName().equals(movieName.getText())) {
								String image = list.get(i).getMovieImage();
								FileInputStream fis = null;
								BufferedInputStream bis = null;
								try {
									File jpgImage = new File(image);
									fis = new FileInputStream(jpgImage);
									bis = new BufferedInputStream(fis);
									Image img = new Image(bis);
									pView.setImage(img);
								} catch (FileNotFoundException e) {
								} finally {
									try {if(bis!=null)bis.close();} catch (IOException e) {e.printStackTrace();	}
									try {if(fis!=null)fis.close();} catch (IOException e) {e.printStackTrace();	}
								}
							}
						}
						//���� ���
						pCancel.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								payDialog.close();
							}
						});
						//���� �ϱ�
						pOk.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								try {
									conn = ConnUtil.getConnection();
									String sql = "insert into ticket values(ticket_num_seq.nextval,?,?,?,?,?,?,?,?,?,?)";
									pstmt = conn.prepareStatement(sql);
									pstmt.setString(1, userId.getText());
									pstmt.setString(2, pmName.getText());
									pstmt.setString(3, ptName.getText());
									pstmt.setString(4, pgName.getText());
									pstmt.setDate(5, java.sql.Date.valueOf(pDate.getText()));
									pstmt.setString(6, pTime.getText());
									pstmt.setString(7, pSeat.getText());
									pstmt.setInt(8, Integer.parseInt(pMoney.getText()));
									pstmt.setString(9, pSelect.getSelectionModel().getSelectedItem());
									pstmt.setString(10, "����");
									pstmt.executeUpdate();
									
									Stage okDialog = new Stage(StageStyle.TRANSPARENT);
									okDialog.initModality(Modality.WINDOW_MODAL);
									Stage primaryStage = (Stage) userId.getScene().getWindow();
									okDialog.initOwner(primaryStage);
									Parent parent = FXMLLoader.load(getClass().getResource("/fxml/FailDialog.fxml"));
									Button btnOk = (Button) parent.lookup("#btnOk");
									Label label = (Label) parent.lookup("#txtTitle");
									label.setText("���Ÿ� �Ϸ��Ͽ����ϴ�.");
									ImageView image = (ImageView) parent.lookup("#image");
									image.setImage(new Image(getClass().getResource("/fxml/images/dialog-info.png").toString()));
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
									Scene scene = new Scene(parent);
									okDialog.setScene(scene);
									okDialog.setResizable(false);
									okDialog.show();
								} catch (SQLException | IOException e) {
									e.printStackTrace();
								} finally {
									try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
									try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
									payDialog.close();
									dialog.close();
									resetAction(event);
									
								}
							}
						});
						Scene payScene = new Scene(payRoot);
						payDialog.setScene(payScene);
						payDialog.setResizable(false);
						payDialog.show();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			
			// �¼� ���� ��������.
			String seatInfo = "";
			for(int k = 0;k<list.size();k++) {
				if(list.get(k).getMovieName().equals(movieName.getText())
					&&list.get(k).getTheaterName().equals(theaterName.getText())
					&&list.get(k).getGwanName().equals(gwanName.getText())) {
					seatInfo = list.get(k).getGwanSeat();
				}
			}
			String[] seatsplit = seatInfo.split(":");
			String realCol = seatsplit[0]; //col
			String seats = seatsplit[1]; //��ü�¼� 
			String[] realSeats = seats.split("/");
			int col = 0;
			int row = 0;
			// �¼� �����ε�
			for(int s = 0;s<realSeats.length;s++) {
				Button sbutton = new Button(); 
				String[] imsi = realSeats[s].split(",");
				String sNum = imsi[0];
				if(sNum.equals("��")||sNum.equals("   ")) {
					sbutton.setVisible(false);
					sbutton.setDisable(true);
				}
				sbutton.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, 
						BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, 
						CornerRadii.EMPTY, new BorderWidths(1), new Insets(-1))));
				sbutton.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
				sbutton.setText(sNum);
				sbutton.setTextFill(Color.BLACK);
				sbutton.setPrefHeight(25);
				sbutton.setPrefWidth(33);
				sbutton.setFont(new Font(9));
				sbutton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if(sbutton.getTextFill().equals(Color.BLACK)){
							if(slist.size()>=Integer.parseInt(people.getText())) {
								Popup dialog = new Popup();
								Stage primaryStage = (Stage) sbutton.getScene().getWindow();
								try {
									Parent parent = FXMLLoader.load(getClass().getResource("/fxml/user/SeatDialog.fxml"));
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
								return;
							}
							sbutton.setTextFill(Color.WHITE);
							sbutton.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
							slist.add(sbutton.getText());
						}else {
							sbutton.setTextFill(Color.BLACK);
							sbutton.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
							if(slist.contains(sbutton.getText())) { slist.remove(sbutton.getText()); }
						}
						seat.setText(slist.toString());
						if(Integer.parseInt(people.getText())==slist.size()) {
							pay.setDisable(false);
						}else {
							pay.setDisable(true);
						}
					}
				});
				seatTable.add(sbutton, col, row);
				col++;
				if(col==Integer.parseInt(realCol)) { row++;	col=0; }
			}//�¼� �ε�
			seatTable.setDisable(true);
			
			//�����¼� �ε��ؼ� �����¼��� ����ǥ��
			try {
				conn = ConnUtil.getConnection();
				String sql = "select * from ticket";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					String getMname = rs.getString("movie_name");
					String getTname = rs.getString("theater_name");
					String getGname = rs.getString("gwan_name");
					Date getDate = rs.getDate("ticket_date");
					String getTime = rs.getString("ticket_time");
					String getSeat = rs.getString("ticket_seat");
					String getCancel = rs.getString("ticket_cancel");
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String getDateString = sdf.format(getDate);
					if(getMname.equals(movieName.getText())&&getTname.equals(theaterName.getText())
							&&getGname.equals(gwanName.getText())&&getDateString.equals(date.getText())
							&&getTime.equals(time.getText())&&getCancel.equals("����")) {
						//���缱���� ��ȭ�� ������ Ƽ��
						String realSeat = getSeat.substring(1, getSeat.length()-1);
						String[] getSeats = realSeat.split(",");
						for(int i =0;i<seatTable.getChildren().size();i++) {
							Button button = (Button) seatTable.getChildren().get(i);
							for(int j = 0;j<getSeats.length;j++) {
								if(button.getText().equals(getSeats[j].trim())) {
									button.setDisable(true);
									button.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
								}
							}
						}
					}
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			} finally {
				try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
			
			//�����ο� ���� ������
			people.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(Integer.parseInt(people.getText())!=slist.size()) {
						pay.setDisable(true);
					}else {
						pay.setDisable(false);
					}
					if(!people.getText().equals("0")) {
						seatTable.setDisable(false);
					}else {
						for(int i =0;i<adult.getChildren().size();i++) {
							if(i==0) {
								Button aBt = (Button) adult.getChildren().get(0);
								Button sBt = (Button) student.getChildren().get(0);
								aBt.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
								aBt.setTextFill(Color.WHITE);
								sBt.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
								sBt.setTextFill(Color.WHITE);
							}else {
								Button aBt = (Button) adult.getChildren().get(i);
								Button sBt = (Button) student.getChildren().get(i);
								aBt.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
								aBt.setTextFill(Color.BLACK);
								sBt.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
								sBt.setTextFill(Color.BLACK);
							}
						}
						aPeople.setText("0");	sPeople.setText("0");	people.setText("0");	slist.clear();
						seat.setText("[]");	adultmoney.setText("0");	stumoney.setText("0");	totalmoney.setText("0");
						for(int j = 0;j<seatTable.getChildren().size();j++) {
							Button button = (Button) seatTable.getChildren().get(j);
							button.setTextFill(Color.BLACK);
							button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
						}
						pay.setDisable(true);
						seatTable.setDisable(true);
					}
				}
			});
			
			//�����ο� ����
			for(int i=0;i<adult.getChildren().size();i++) {
				Button button = (Button) adult.getChildren().get(i);
				button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
				Button Abutton = (Button) adult.getChildren().get(0);
				Abutton.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
				Abutton.setTextFill(Color.WHITE);
				button.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						for(int i =0;i<adult.getChildren().size();i++) {
							Button button = (Button) adult.getChildren().get(i);
							button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
							button.setTextFill(Color.BLACK);
						}
						Button getBt = (Button) event.getSource();
						getBt.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
						getBt.setTextFill(Color.WHITE);
						int p = Integer.parseInt(getBt.getText());
						int a = p*10000;
						aPeople.setText(String.valueOf(p));
						adultmoney.setText(String.valueOf(a));
						totalmoney.setText(Integer.parseInt(adultmoney.getText())+Integer.parseInt(stumoney.getText())+"");
						people.setText(Integer.parseInt(aPeople.getText())+Integer.parseInt(sPeople.getText())+"");
					}
				});
			}
			for(int i=0;i<student.getChildren().size();i++) {
				Button button = (Button) student.getChildren().get(i);
				button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
				Button Sbutton = (Button) student.getChildren().get(0);
				Sbutton.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
				Sbutton.setTextFill(Color.WHITE);
				button.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						for(int i =0;i<student.getChildren().size();i++) {
							Button button = (Button) student.getChildren().get(i);
							button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
							button.setTextFill(Color.BLACK);
						}
						Button getBt = (Button) event.getSource();
						getBt.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
						getBt.setTextFill(Color.WHITE);
						int p = Integer.parseInt(getBt.getText());
						int a = p*8000;
						sPeople.setText(String.valueOf(p));
						stumoney.setText(String.valueOf(a));
						totalmoney.setText(Integer.parseInt(adultmoney.getText())+Integer.parseInt(stumoney.getText())+"");
						people.setText(Integer.parseInt(aPeople.getText())+Integer.parseInt(sPeople.getText())+"");
					}
				});
			}
			Scene scene = new Scene(root);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();
			//������� �˾�
			Stage ratingPop = new Stage(StageStyle.UTILITY);
			ratingPop.initModality(Modality.WINDOW_MODAL);
			Stage primaryStage = (Stage) adult.getScene().getWindow();
			ratingPop.initOwner(primaryStage);
			ratingPop.setTitle("������޾ȳ�");
			try {
				Parent Rparent = FXMLLoader.load(getClass().getResource("/fxml/user/RatingDialog.fxml"));
				Button btnOk = (Button) Rparent.lookup("#btnOk");
				btnOk.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						ratingPop.close();
					}
				});
				Label rating = (Label) Rparent.lookup("#rating");
				Label content = (Label) Rparent.lookup("#content");
				String ratingInfo = "";
				for(int k = 0;k<list.size();k++) {
					if(list.get(k).getMovieName().equals(movieName.getText())
						&&list.get(k).getTheaterName().equals(theaterName.getText())
						&&list.get(k).getGwanName().equals(gwanName.getText())) {
						ratingInfo = list.get(k).getMovieRating();
					}
				}
				if(ratingInfo.equals("û�ҳ�����Ұ�")) {
					rating.setText("û�ҳ� �����Ұ�");
					rating.setTextFill(Color.RED);
					content.setText("�� ��ȭ�� [û�ҳ� ���� �Ұ�] ��ȭ�Դϴ�.\r\n" + 
							"���� ��, �ź����� �� ������ �ּ���.\r\n" + 
							"\r\n" + 
							"�� 18�� �̸�(��,���� ����)�� ��ȣ�ڰ� �־ �����Ͻ� �� ������,\r\n" + 
							"�� 18�� �̻��̴��� �����ߵ�������� ���� \r\n �������� �л��� ������ ���ѵ˴ϴ�.");
				}else if(ratingInfo.equals("15��������")){
					rating.setText("15�� �̻� ������");
					rating.setTextFill(Color.ORANGE);
					content.setText("�� ��ȭ�� �� 15�� �̻� ���� ������ ��ȭ�Դϴ�.\r\n" + 
							"�� 15�� �̸� ���� �θ�� �Ǵ� �� 19�� �̻� ��ȣ�� ���� �� ������ �����մϴ�.\r\n" + 
							"���� Ȯ�� �Ұ� �� ������ ���� �� �� �ֽ��ϴ�.");
				}else if(ratingInfo.equals("12��������")) {
					rating.setText("12�� �̻� ������");
					rating.setTextFill(Color.BLUE);
					content.setText("�� ��ȭ�� �� 12�� �̻� ���� ������ ��ȭ�Դϴ�.\r\n" + 
							"�� 12�� �̸� ���� �θ�� �Ǵ� �� 19�� �̻� ��ȣ�� ���� �� ������ �����մϴ�.\r\n" + 
							"���� Ȯ�� �Ұ� �� ������ ���� �� �� �ֽ��ϴ�.");
				}
				Scene ratingScene = new Scene(Rparent);
				ratingPop.setScene(ratingScene);
				ratingPop.show();
				if(ratingInfo.equals("��ü������")) {
					ratingPop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �ʱ�ȭ ��ư
	 * @param event
	 */
	@FXML public void resetAction(ActionEvent event) {
		for(int i=1;i<regionBt.getChildren().size();i++) {Button bt= (Button) regionBt.getChildren().get(i);bt.setDisable(true);}
		theaterList.getChildren().clear();
		dateList.getChildren().clear();
		timeList.getChildren().clear();
		selImage.setImage(null);
		selName.setText("");
		selTheaterName.setText("");
		selGwanName.setText("");
		selDate.setText("");
		selTime.setText("");
		seatChoice.setDisable(true);
	}
}
