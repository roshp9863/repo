package com.inn.startandconnect.dto;

public class ClientDTO {

	private Long userId;
	private String username;
	private String email;
	private String passwordHash;

	public ClientDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ClientDTO(Long userId, String username, String email, String passwordHash) {
		super();
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.passwordHash = passwordHash;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	@Override
	public String toString() {
		return "UserDTO [userId=" + userId + ", username=" + username + ", email=" + email + ", passwordHash="
				+ passwordHash + "]";
	}
	
	
	

}
