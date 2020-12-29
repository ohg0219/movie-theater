package controller.admin;

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
import java.time.LocalDate;
import java.util.ResourceBundle;

import controller.ConnUtil;
import controller.vo.MovieVO;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MovieEdit implements Initializable {
	//ȭ�鱸��
	@FXML Button backGo;
	@FXML Label userId;
	@FXML ComboBox<String> addGenre;
	@FXML Button newBt;
	@FXML TextField addNumTf;
	@FXML TextField addNameTf;
	@FXML TextField addDirecTf;
	@FXML TextField addActorTf;
	@FXML DatePicker addDate;
	@FXML TextField addTimeTf;
	@FXML Button addAndModifyBt;
	@FXML TextField searchNameTf;
	@FXML ComboBox<String> searchGenre;
	@FXML Button searchBt;
	@FXML Button resetBt;
	@FXML Button deleteBt;
	@FXML ComboBox<String> addRating;
	//���̺� ����
	@FXML TableView<MovieVO> movieTable;
	@FXML TableColumn<MovieVO, Integer> num;
	@FXML TableColumn<MovieVO, String> name;
	@FXML TableColumn<MovieVO, String> genre;
	@FXML TableColumn<MovieVO, String> director;
	@FXML TableColumn<MovieVO, String> actor;
	@FXML TableColumn<MovieVO, String> date;
	@FXML TableColumn<MovieVO, String> time;
	//�̹��� ����
	@FXML ImageView imageView;
	@FXML Button addImage;
	@FXML Button delImage;
	private String id;
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ObservableList<MovieVO> list;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		list = FXCollections.observableArrayList();
		num.setCellValueFactory(new PropertyValueFactory<>("num"));
		num.setStyle("-fx-alignment:center");
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
		director.setCellValueFactory(new PropertyValueFactory<>("director"));
		actor.setCellValueFactory(new PropertyValueFactory<>("actor"));
		date.setCellValueFactory(new PropertyValueFactory<>("date"));
		date.setStyle("-fx-alignment:center");
		time.setCellValueFactory(new PropertyValueFactory<>("time"));
		movieList();
		addNumTf.setDisable(true);
		addGenre.getSelectionModel().select("����");
		searchGenre.getSelectionModel().select("��ü");
		addRating.getSelectionModel().select("��ü������");
		movieTable.setPlaceholder(new Label("�˻������ �����ϴ�."));
		movieTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
			
					if(event.getClickCount()>1) {
						MovieVO movie = movieTable.getSelectionModel().getSelectedItem();
						addNumTf.setText(String.valueOf(movie.getNum()));
						addNameTf.setText(movie.getName());
						addGenre.getSelectionModel().select(movie.getGenre());
						addDirecTf.setText(movie.getDirector());
						addActorTf.setText(movie.getActor());
						if(movie.getDate()==null) {
							addDate.setValue(null);
						}else {
							addDate.setValue(LocalDate.parse(movie.getDate()));
						}
						addTimeTf.setText(String.valueOf(movie.getTime()));
						addRating.getSelectionModel().select(movie.getRating());
						FileInputStream fis = null;
						BufferedInputStream bis = null;
						if(movie.getImage()!=null) {
							try {
								File jpgImage = new File(movie.getImage());
								fis = new FileInputStream(jpgImage);
								bis = new BufferedInputStream(fis);
								Image img = new Image(bis);
								imageView.setImage(img);
								path = movie.getImage();
							} catch (FileNotFoundException e) {
								imageView.setImage(null);
							} catch (NullPointerException e2) {
							} finally {
									try {if(bis!=null)bis.close();} catch (IOException e) {e.printStackTrace();	}
									try {if(fis!=null)fis.close();} catch (IOException e) {e.printStackTrace();	}
							}
						} else {
							imageView.setImage(null);
						}
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
	/**
	 * ��ȭ��� �ε�
	 */
	public void movieList() {
		movieTable.getItems().clear();
		try {
			conn = ConnUtil.getConnection();
			String sql = "Select * from movie where movie_num>0 order by movie_num desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				int num = rs.getInt("movie_num");
				String name = rs.getString("movie_name");
				String genre = rs.getString("movie_genre");
				String director = rs.getString("movie_director");
				String actor = rs.getString("movie_actor");
				String date;
				if(rs.getDate("movie_date")==null) {
					date = null;
				}else {
					date = rs.getDate("movie_date").toString();
				}
				int time = rs.getInt("movie_time");
				String image = rs.getString("movie_image");
				String rating = rs.getString("movie_rating");
				list.add(new MovieVO(num, name, genre, director, actor, date, time, image,rating));
			}
		} catch (SQLException e) {
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		movieTable.setItems(list);
	}
	
	/**
	 * �ڷΰ��� 
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
			stage.setTitle("�����ڸ��");
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
	 * �űԵ��
	 * @param event
	 */
	@FXML public void newAction(ActionEvent event) {
		imageView.setImage(null);
		path = null;
		addNumTf.clear();
		addNameTf.clear();
		addGenre.getSelectionModel().select("����");
		addRating.getSelectionModel().select("��ü������");
		addDirecTf.clear();
		addActorTf.clear();
		addDate.setValue(null);
		addTimeTf.clear();
		
	}
	/**
	 * �̹��� ��������
	 * @param event
	 */
	String path = null;
	
	@FXML public void addImageAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("������ ����");
		fileChooser.setInitialDirectory(new File("C:\\myProject\\myJAVA\\movietheater\\src\\controller\\images"));
		File jpgImage = fileChooser.showOpenDialog(addImage.getScene().getWindow());
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(jpgImage);
			bis = new BufferedInputStream(fis);
			Image img = new Image(bis);
			imageView.setImage(img);
			path = jpgImage.toURI().getPath();
		} catch (FileNotFoundException e) {
		} catch (NullPointerException e2) {
		} finally {
			try {if(bis!=null)bis.close();} catch (IOException e) {e.printStackTrace();	}
			try {if(fis!=null)fis.close();} catch (IOException e) {e.printStackTrace();	}
		}
	}
	/**
	 * �̹��� �����
	 * @param event
	 */
	@FXML public void delImageAction(ActionEvent event) {
		imageView.setImage(null);
		path=null;
	}
	
	
	/**
	 * ���/���� ��ư
	 * @param event
	 * @throws IOException 
	 */
	@FXML public void addAndModifyAction(ActionEvent event) throws IOException{
		
		if(addNameTf.getText().trim().length()==0) {addNameTf.requestFocus(); return;}
		if(addDirecTf.getText().trim().length()==0) {addDirecTf.requestFocus(); return; }
		if(addActorTf.getText().trim().length()==0) {addActorTf.requestFocus(); return; }
		if(addTimeTf.getText().length()==0) {addTimeTf.requestFocus(); return;}
		try {Integer.parseInt(addTimeTf.getText());} catch (Exception e) {addTimeTf.requestFocus(); return; }
		
		if(addNumTf.getText().isEmpty()) { //�űԵ��
			try {
				conn = ConnUtil.getConnection();
				String sql = "insert into movie("
						+ "movie_num,movie_name,movie_genre,movie_director,movie_actor,movie_date,movie_time,movie_image,movie_rating)"
						+ " values(movie_num_seq.nextval,?,?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, addNameTf.getText());
				pstmt.setString(2, addGenre.getSelectionModel().getSelectedItem());
				pstmt.setString(3, addDirecTf.getText());
				pstmt.setString(4, addActorTf.getText());
				if(addDate.getValue()==null) {
					pstmt.setDate(5, null);
				}else {
					pstmt.setDate(5, java.sql.Date.valueOf(addDate.getValue()));
				}
				pstmt.setString(6, addTimeTf.getText());
				pstmt.setString(7, path);
				pstmt.setString(8, addRating.getSelectionModel().getSelectedItem());
				pstmt.executeUpdate();
			} catch (SQLException e) {
			} catch (NullPointerException e2) {
			}finally {
				try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}else { //����
			try {
				conn = ConnUtil.getConnection();
				String sql = "update movie set "
						+ "movie_name=?,movie_genre=?,movie_director=?,movie_actor=?,movie_date=?,movie_time=?,movie_image=?,movie_rating=? "
						+ "where movie_num=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, addNameTf.getText());
				pstmt.setString(2, addGenre.getSelectionModel().getSelectedItem());
				pstmt.setString(3, addDirecTf.getText());
				pstmt.setString(4, addActorTf.getText());
				if(addDate.getValue()==null) {
					pstmt.setDate(5, null);
				}else {
					pstmt.setDate(5, java.sql.Date.valueOf(addDate.getValue()));
				}
				pstmt.setString(6, addTimeTf.getText());
				pstmt.setString(7, path);
				pstmt.setString(8, addRating.getSelectionModel().getSelectedItem());
				pstmt.setInt(9, Integer.parseInt(addNumTf.getText()));
				pstmt.executeUpdate();
			} catch (SQLException e) {
			} catch (NullPointerException e2) {
			} finally {
				try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}
		path = null;
		movieList();
		newAction(event);
	}
	/**
	 * �˻� ��ư
	 * @param event
	 * @throws SQLException 
	 */
	@FXML public void searchAction(ActionEvent event) {
		try {	
			list.clear();
			movieTable.getItems().clear();
			conn = ConnUtil.getConnection();
			String sql ="";
			if(searchNameTf.getText().trim().length()!=0&&searchGenre.getSelectionModel().getSelectedItem().equals("��ü")) {
				sql = "select * from movie where movie_name like ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%"+searchNameTf.getText()+"%");
				rs = pstmt.executeQuery();
			}else if(searchNameTf.getText().trim().length()!=0&&!searchGenre.getSelectionModel().getSelectedItem().equals("��ü")) {
				sql = "select * from movie where movie_name like ? and movie_genre=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%"+searchNameTf.getText()+"%");
				pstmt.setString(2, searchGenre.getSelectionModel().getSelectedItem());
				rs = pstmt.executeQuery();	
			}else if(searchNameTf.getText().trim().length()==0&&!searchGenre.getSelectionModel().getSelectedItem().equals("��ü")) {
				sql = "select * from movie where movie_genre=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchGenre.getSelectionModel().getSelectedItem());
				rs = pstmt.executeQuery();	
			}else {
				movieList();
			}
			while(rs.next()) {
				int num = rs.getInt("movie_num");
				String name = rs.getString("movie_name");
				String genre = rs.getString("movie_genre");
				String director = rs.getString("movie_director");
				String actor = rs.getString("movie_actor");
				String date = rs.getDate("movie_date").toString();
				int time = rs.getInt("movie_time");
				String image =rs.getString("movie_image");
				String rating = rs.getString("movie_rating");
				list.add(new MovieVO(num, name, genre, director, actor, date, time,image,rating));
			}
		} catch (SQLException e) {
		} finally {
			try { if(rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		movieTable.setItems(list);
	}
	/**
	 * �ʱ�ȭ ��ư
	 * @param event
	 */
	@FXML public void resetAction(ActionEvent event) {
		searchNameTf.setText("");
		searchGenre.getSelectionModel().select("��ü");
		movieList();
		newAction(event);
	}
	/**
	 * ��ȭ ����
	 * @param event
	 */
	@FXML public void deleteAction(ActionEvent event) {
		try {
			conn = ConnUtil.getConnection();
			String sql = "delete from movie where movie_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(addNumTf.getText()));
			pstmt.executeUpdate();
		} catch (Exception e) {
		} finally {
			try { if(pstmt!=null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		newAction(event);
		movieList();
	}
}