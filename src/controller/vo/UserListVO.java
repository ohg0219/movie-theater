package controller.vo;

import javafx.beans.property.SimpleStringProperty;

public class UserListVO {
	private SimpleStringProperty userId;
	private SimpleStringProperty userName;
	private SimpleStringProperty userTel;
	private SimpleStringProperty userAddr;
	
	public UserListVO(String userId, String userName, String userTel,
			String userAddr) {
		this.userId = new SimpleStringProperty(userId);
		this.userName = new SimpleStringProperty(userName);
		this.userTel = new SimpleStringProperty(userTel);
		this.userAddr = new SimpleStringProperty(userAddr);
	}
	public String getUserId() {
		return userId.get();
	}
	public void setUserId(String userId) {
		this.userId.set(userId);
	}
	public String getUserName() {
		return userName.get();
	}
	public void setUserName(String userName) {
		this.userName.set(userName);
	}
	public String getUserTel() {
		return userTel.get();
	}
	public void setUserTel(String userTel) {
		this.userTel.set(userTel);
	}
	public String getUserAddr() {
		return userAddr.get();
	}
	public void setUserAddr(String userAddr) {
		this.userAddr.set(userAddr);
	}
	
	
	
}
