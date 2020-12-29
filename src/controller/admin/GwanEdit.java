package controller.admin;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import controller.ConnUtil;
import controller.vo.GwanVO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GwanEdit implements Initializable{

	@FXML Label userId;
	@FXML Button backGo;
	@FXML Button newBt;
	@FXML TextField theaterNumtf;
	@FXML ComboBox<String> theaterRegionCombo;
	@FXML ComboBox<String> theaterNameCombo;
	@FXML TextField gwanNumtf;
	@FXML TextField gwanNametf;
	@FXML Button seatEdit;
	@FXML Button addAndModifyBt;
	@FXML Button deleteBt;
	@FXML TableView<GwanVO> gwanTable;
	@FXML TableColumn<GwanVO,String> region;
	@FXML TableColumn<GwanVO,Integer> theaterNum;
	@FXML TableColumn<GwanVO,String> theaterName;
	@FXML TableColumn<GwanVO,Integer> gwanNum;
	@FXML TableColumn<GwanVO,String> gwanName;
	@FXML TableColumn<GwanVO,Integer> gwanSeatCount;
	@FXML ComboBox<String> searchRegion;
	@FXML Button searchBt;
	@FXML Button resetBt;
	@FXML ComboBox<String> searchSelect;
	@FXML TextField searchTf;
	
	private String id;
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ObservableList<GwanVO> list;
	
	/**
	 * 구성요소 설정
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		theaterNumtf.setDisable(true);
		gwanNumtf.setDisable(true);
		searchRegion.getSelectionModel().select("전체");
		searchSelect.getSelectionModel().select("극장명");
		list = FXCollections.observableArrayList();
		region.setCellValueFactory(new PropertyValueFactory<>("Region"));
		theaterNum.setCellValueFactory(new PropertyValueFactory<>("theaterNum"));
		theaterName.setCellValueFactory(new PropertyValueFactory<>("theaterName"));
		gwanNum.setCellValueFactory(new PropertyValueFactory<>("gwanNum"));
		gwanName.setCellValueFactory(new PropertyValueFactory<>("gwanName"));
		gwanSeatCount.setCellValueFactory(new PropertyValueFactory<>("gwanSeatCount"));
		gwanList();
		gwanTable.setPlaceholder(new Label("검색결과가 없습니다."));
		gwanTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					if(event.getClickCount()>1) {
						GwanVO gwan = gwanTable.getSelectionModel().getSelectedItem();
						theaterNumtf.setText(String.valueOf(gwan.getTheaterNum()));
						theaterRegionCombo.getSelectionModel().select(gwan.getRegion());
						theaterNameCombo.getSelectionModel().select(gwan.getTheaterName());
						gwanNumtf.setText(String.valueOf(gwan.getGwanNum()));
						gwanNametf.setText(gwan.getGwanName());
					}
				}catch (Exception e) {
				}
				
			}
		});
		
		theaterRegionCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String region = newValue;
				ObservableList<String> theaterList = FXCollections.observableArrayList();
				try {
					conn = ConnUtil.getConnection();
					String sql = "Select theater_name from theater where theater_region =?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, region);
					rs = pstmt.executeQuery();
					while(rs.next()) {
						theaterList.add(rs.getString("theater_name"));
					}
					theaterNameCombo.setItems(theaterList);
					theaterNameCombo.getSelectionModel().select(0);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
					try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
					try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
				}
			}
		});
		theaterRegionCombo.getSelectionModel().select(0);
		theaterNameCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
					String newValue) {
				String theaterName = newValue;
				if(theaterNameCombo.getItems().isEmpty()) { theaterNumtf.clear();}
				try {
					conn = ConnUtil.getConnection();
					String sql = "Select theater_num from theater where theater_name =?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, theaterName);
					rs = pstmt.executeQuery();
					while(rs.next()) {
						theaterNumtf.setText(String.valueOf(rs.getInt("theater_num")));
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
		
		theaterNameCombo.getSelectionModel().select(0);
		searchTf.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				searchAction(event);
			}
		});
	}//end initialize
	
	/**
	 * 상영관 리스트 로딩
	 */
	public void gwanList() {
		gwanTable.getItems().clear();
		try {
			conn = ConnUtil.getConnection();
			String sql = "select theater_region, theater_num,theater_name, gwan_num,gwan_name,gwan_seatcount,gwan_seat " + 
					"from theater join gwan using(theater_num) order by theater_region, theater_name, gwan_num";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String theater_region = rs.getString("theater_region");
				int theater_num = rs.getInt("theater_num");
				String theater_name = rs.getString("theater_name");
				int gwan_num = rs.getInt("gwan_num");
				String gwan_name = rs.getString("gwan_name");
				int gwan_seatCount = rs.getInt("gwan_seatCount");
				String gwan_seat = rs.getString("gwan_seat");
				list.add(new GwanVO(theater_region, theater_num, theater_name, gwan_num, gwan_name, gwan_seatCount, gwan_seat));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		gwanTable.setItems(list);
	}
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
	 * 신규등록
	 * @param event
	 */
	@FXML public void newAction(ActionEvent event) {
		theaterNumtf.clear();
		theaterRegionCombo.getSelectionModel().select(0);
		gwanNumtf.clear();
		gwanNametf.clear();
		
	}
	/**
	 * 상영관 추가
	 * @param event
	 */
	@FXML public void addAndModifyAction(ActionEvent event) {
		if(theaterNumtf.getText().isEmpty()) {theaterRegionCombo.requestFocus(); return;}
		if(theaterRegionCombo.getSelectionModel().getSelectedItem().equals("선택")) {
			theaterRegionCombo.requestFocus(); return;	}
		if(theaterNameCombo.getSelectionModel().getSelectedItem()==null) { 
			theaterNameCombo.requestFocus();  return;};
		if(gwanNametf.getText().trim().length()==0) { gwanNametf.requestFocus(); return;}
		
		if(gwanNumtf.getText().isEmpty()) {//신규
			try {
				conn = ConnUtil.getConnection();
				String sql = "insert into gwan (gwan_num,gwan_name,theater_num)"
						+ " values(gwan_num_seq.nextval,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, gwanNametf.getText());
				pstmt.setInt(2, Integer.parseInt(theaterNumtf.getText()));
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}else {
			try {
				conn = ConnUtil.getConnection();
				String sql = "update gwan set gwan_name=?, theater_num=? where gwan_num=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, gwanNametf.getText());
				pstmt.setInt(2, Integer.parseInt(theaterNumtf.getText()));
				pstmt.setInt(3, Integer.parseInt(gwanNumtf.getText()));
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}
		newAction(event);
		gwanList();
	}
	/**
	 * 상영관 삭제
	 * @param event
	 */
	@FXML public void delAction(ActionEvent event) {
		try {
			conn = ConnUtil.getConnection();
			String sql = "delete from gwan where gwan_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(gwanNumtf.getText()));
			pstmt.executeUpdate();
		} catch (Exception e) {
		} finally {
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		newAction(event);
		gwanList();
	}
	/**
	 * 상영관 검색
	 * @param event
	 */
	@FXML public void searchAction(ActionEvent event) {
		try {
			gwanTable.getItems().clear();
			conn = ConnUtil.getConnection();
			String sql = "";
			if (searchTf.getText().trim().length() == 0) {
				// 칸이 비었다.
				if (searchRegion.getSelectionModel().getSelectedItem().equals("전체")) {
					// 아무것도 선택안함.
					gwanList();
					return;
				} else {
					// 지역만 선택
					sql = "select theater_region, theater_num,theater_name, gwan_num,gwan_name,gwan_seatcount,gwan_seat "
							+ "from theater join gwan using(theater_num) "
							+ "where theater_region=? order by theater_region, theater_name, gwan_num";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, searchRegion.getSelectionModel().getSelectedItem());
					rs = pstmt.executeQuery();
				}
			} else {
				// 칸에 글자가 있다.
				if(searchSelect.getSelectionModel().getSelectedItem().equals("극장명")) {
					//선택이 극장명이다.
					if(searchRegion.getSelectionModel().getSelectedItem().equals("전체")) {
						//극장명 선택 + 검색어있음 + 지역전체
						sql = "select theater_region, theater_num,theater_name, gwan_num,gwan_name,gwan_seatcount,gwan_seat "
								+ "from theater join gwan using(theater_num) "
								+ "where theater_name like ? order by theater_region, theater_name, gwan_num";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, "%"+searchTf.getText()+"%");
						rs = pstmt.executeQuery();
					}else {
						//극장명 선택 + 검색어있음 + 지역선택함.
						sql = "select theater_region, theater_num,theater_name, gwan_num,gwan_name,gwan_seatcount,gwan_seat "
								+ "from theater join gwan using(theater_num) "
								+ "where theater_name like ?, theater_region=? order by theater_region, theater_name, gwan_num";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, "%"+searchTf.getText()+"%");
						pstmt.setString(2, searchRegion.getSelectionModel().getSelectedItem());
						rs = pstmt.executeQuery();
					}
				}else {
					//선택이 상영관명이다.
					if(searchRegion.getSelectionModel().getSelectedItem().equals("전체")) {
						//상영관명 선택 + 검색어있음 + 지역전체
						sql = "select theater_region, theater_num,theater_name, gwan_num,gwan_name,gwan_seatcount,gwan_seat "
								+ "from theater join gwan using(theater_num) "
								+ "where gwan_name like ? order by theater_region, theater_name, gwan_num";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, "%"+searchTf.getText()+"%");
						rs = pstmt.executeQuery();
					}else {
						//상영관명 선택 + 검색어있음 + 지역선택함.
						sql = "select theater_region, theater_num,theater_name, gwan_num,gwan_name,gwan_seatcount,gwan_seat "
								+ "from theater join gwan using(theater_num) "
								+ "where gwan_name like ?, theater_region=? order by theater_region, theater_name, gwan_num";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, "%"+searchTf.getText()+"%");
						pstmt.setString(2, searchRegion.getSelectionModel().getSelectedItem());
						rs = pstmt.executeQuery();
					}
				}
			}//검색조건 끝
			while(rs.next()) {
				String theater_region = rs.getString("theater_region");
				int theater_num = rs.getInt("theater_num");
				String theater_name = rs.getString("theater_name");
				int gwan_num = rs.getInt("gwan_num");
				String gwan_name = rs.getString("gwan_name");
				int gwan_seatCount = rs.getInt("gwan_seatCount");
				String gwan_seat = rs.getString("gwan_seat");
				list.add(new GwanVO(theater_region, theater_num, theater_name, gwan_num, gwan_name, gwan_seatCount, gwan_seat));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		gwanTable.setItems(list);
	}
	/**
	 * 검색 초기화 
	 * @param event
	 */
	@FXML public void resetAction(ActionEvent event) {
		searchRegion.getSelectionModel().select("전체");
		searchTf.clear();
		gwanList();
		newAction(event);
	}
	/**
	 * 좌석설정 다이얼로그 호출
	 * @param event
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@FXML public void seatEditAction(ActionEvent event) throws IOException {
		if(theaterNumtf.getText().isEmpty()) {return;}
		if(gwanNametf.getText().isEmpty()) {gwanNametf.requestFocus(); return;}
		Stage dialog = new Stage(StageStyle.UTILITY);
		dialog.initModality(Modality.WINDOW_MODAL);
		Stage stage = (Stage) seatEdit.getScene().getWindow();
		dialog.initOwner(stage);
		dialog.setTitle("좌석설정");
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/SeatEdit.fxml"));
		Label theaterName = (Label) root.lookup("#StheaterName");
		Label gwanName = (Label) root.lookup("#SgwanName");
		ComboBox<Integer> rowSelect = (ComboBox<Integer>) root.lookup("#SrowSelect");
		ComboBox<Integer> colSelect = (ComboBox<Integer>) root.lookup("#ScolSelect");
		Button createTable = (Button) root.lookup("#ScreateTable");
		GridPane createGrid = (GridPane) root.lookup("#ScreateGrid");
		Button seeTable = (Button) root.lookup("#SseeTable");
		GridPane seeGrid = (GridPane) root.lookup("#SseeGrid");
		Button saveBt = (Button) root.lookup("#SsaveBt");
		Label savefail = (Label) root.lookup("#Ssavefail");
		Button cancelBt = (Button) root.lookup("#ScancelBt");
		theaterName.setText("극장명 : "+theaterNameCombo.getSelectionModel().getSelectedItem());
		gwanName.setText("관명 : "+gwanNametf.getText());
		
		ObservableList<Integer> rowCount = rowSelect.getItems();
		ObservableList<Integer> colCount = colSelect.getItems();
		for(int i =0;i<=20;i++) { rowCount.add(i); colCount.add(i);	}

		//줄, 컬럼 선택값 확인
		Label colSave = new Label();
		Label rowSave = new Label();
		colSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				colSave.setText(String.valueOf(newValue));
			}
		});
		rowSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				rowSave.setText(String.valueOf(newValue));
			}
		});
		//좌석내용있을시 로딩값.
		
		try {
			String getSeat ="";
			conn = ConnUtil.getConnection();
			String sql = "select gwan_seat from gwan where gwan_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(gwanNumtf.getText()));
			rs = pstmt.executeQuery();
			while(rs.next()) {
				getSeat = rs.getString("gwan_seat");
			}
			String[] seatSplit =  getSeat.split(":");
			int getCol = Integer.parseInt(seatSplit[0]);
			String[] seatNum = seatSplit[1].split("/");
			rowSelect.getSelectionModel().select(seatNum.length/getCol);
			colSelect.getSelectionModel().select(getCol);
			int col = 0;
			int row = 0;
			for(int i =0;i<seatNum.length;i++) {
				String[] realNum = seatNum[i].split(",");
				Button button = new Button(realNum[0]);
				button.setFont(new Font(8));
				button.setMinWidth(30);
				button.setMinHeight(30);
				if(realNum[0].equals("통")) {
					button.setVisible(false);
				}else if(realNum[0].equals("   ")) {
					button.setVisible(false);;
				}
				seeGrid.add(button, col, row);
				col++;
				if(col==getCol) {
					row++;
					col=0;
				}
			}
			row =0;
			col =0;
			for(int i =0;i<seatNum.length;i++) {
				String[] realNum = seatNum[i].split(",");
				Button button = new Button(realNum[0]);
				if(realNum[0].equals("통")) {
					button.setText("통");
					button.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
				}else if(realNum[0].equals("   ")) {
					button.setText("   ");
					button.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
				}else {
					button.setText("ㅇ");
					button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
				}
				button.setOnMouseReleased(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						if(button.getText().equals("ㅇ")) {
							button.setText("통");
							button.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
						}else if(button.getText().equals("   ")) {
							button.setText("ㅇ");
							button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
						}else if(button.getText().equals("통")) {
							button.setText("   ");
							button.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
						}
					}
				});
				createGrid.add(button, col, row);
				col++;
				if(col==getCol) {
					row++;
					col=0;
				}
			}
		} catch (Exception e1) {
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}//end 좌석로드
		
		//좌석생성
		createTable.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				createGrid.getChildren().clear();
				int rowSel = (int) rowSelect.getSelectionModel().getSelectedItem();
				int colSel = (int) colSelect.getSelectionModel().getSelectedItem();
				for(int row = 0;row<rowSel;row++) {
					for(int col = 0;col<colSel;col++) {
						Button button = new Button("ㅇ");
						button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
						button.setOnMouseReleased(new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent arg0) {
								if(button.getText().equals("ㅇ")) {
									button.setText("통");
									button.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
								}else if(button.getText().equals("   ")) {
									button.setText("ㅇ");
									button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
								}else if(button.getText().equals("통")) {
									button.setText("   ");
									button.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
								}
							}
						});
						createGrid.add(button, col, row);
					}
				}
			}
		});//end 좌석생성
		//미리보기 생성
		seeTable.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				seeGrid.getChildren().clear();
				int col = 0;
				int row = 0;
				int tong =0;
				for(int i =0;i<createGrid.getChildren().size();i++) {
					Button button = (Button) createGrid.getChildren().get(i);
					char a = 65;
					Button newButton = new Button((char)(row+a)+""+(col-tong));
					newButton.setFont(new Font(8));
					newButton.setMinWidth(30);
					newButton.setMinHeight(30);
					seeGrid.add(newButton, col, row);
					if(button.getText().equals("통")) {
						newButton.setText("통");
						newButton.setVisible(false);
						tong++;
					}else if(button.getText().equals("   ")) {
						newButton.setText("   ");
						newButton.setVisible(false);
					}
					col++;
					
					if(col==(int)colSelect.getSelectionModel().getSelectedItem()) {
						row++;
						col=0;
						tong=0;
					}
				}
				savefail.setText("");
			}
		});//end 미리보기
		
		//저장하기
		saveBt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(createGrid.getChildren().size()!=seeGrid.getChildren().size()) {
					savefail.setTextFill(new Color(1, 0, 0, 1));
					savefail.setText("미리보기를 확인해주세요."); return;
				}
				int row = Integer.parseInt(rowSave.getText());
				int col = Integer.parseInt(colSave.getText());
				if(createGrid.getChildren().size()!=(row*col)) {
					savefail.setTextFill(new Color(1, 0, 0, 1));
					savefail.setText("생성된좌석을 확인하세요."); return;
				}
				int count =0;
				StringBuffer seat = new StringBuffer();
				seat.append(colSelect.getSelectionModel().getSelectedItem()+":");
				for(int i=0;i<seeGrid.getChildren().size();i++) {
					Button button = (Button) seeGrid.getChildren().get(i);
					seat.append(button.getText()+",0/");
					if(!button.getText().equals("통")&&!button.getText().equals("   ")) {
						count++;
					}
				}
				try {
					conn = ConnUtil.getConnection();
					String sql = "update gwan set gwan_seat=?,gwan_seatCount=? where gwan_num=?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, seat.toString());
					pstmt.setInt(2, count);
					pstmt.setInt(3, Integer.parseInt(gwanNumtf.getText()));
					pstmt.executeUpdate();
					savefail.setText("성공적으로 저장되었습니다.");
				} catch (SQLException e) {
					
				} catch (NullPointerException e) {
					
				} finally {
					try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
					try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
				}
				gwanList();
				newAction(event);
				dialog.close();
			}
		});//end save
		
		//종료하기.
		cancelBt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dialog.close();
			}
		});
		Scene scene = new Scene(root);
		dialog.setScene(scene);
		dialog.show();
	}//end 좌석설정
}
