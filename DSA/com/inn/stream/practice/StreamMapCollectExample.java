package com.inn.stream.practice;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamMapCollectExample {
	public static void main(String[] args) {
		List<User> list = new ArrayList<>();
		list.add(new User(1, "rosh", "secret", "rosh@gmail.com"));
		list.add(new User(2, "akku", "secret", "akku@gmail.com"));
		list.add(new User(3, "lali", "secret", "lali@gmail.com"));
		list.add(new User(4, "nancy", "secret", "nancy@gmail.com"));
		list.add(new User(5, "kalyani", "secret", "kalyani@gmail.com"));
		list.add(new User(6, "kanu", "secret", "kanu@gmail.com"));
		
		
		//using stream convert user into userDTO obj
		List<UserDTO> listDTO = list.stream().map(new Function<User, UserDTO>() {

			@Override
			public UserDTO apply(User t) {
				return new UserDTO(t.getId(), t.getUserName(), t.getEmail());
			}
			
		}).collect(Collectors.toList());

		List<UserDTO> listDTO1 = list.stream().map((t) -> new UserDTO(t.getId(), t.getUserName(), t.getEmail())).collect(Collectors.toList());
			
	}
}


class UserDTO {
	private int id;
	private String userName;
	private String email;

	public UserDTO(int id, String userName, String email) {
		super();
		this.id = id;
		this.userName = userName;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", email=" + email + "]";
	}

}

class User {
	private int id;
	private String userName;
	private String password;
	private String email;

	public User(int id, String userName, String password, String email) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", email=" + email + "]";
	}
	
}
