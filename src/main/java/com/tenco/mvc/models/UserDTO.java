package com.tenco.mvc.models;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDTO {
	private int id;
	private String username;
	private String password;
	private String email;
}
