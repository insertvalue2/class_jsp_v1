package com.tenco.mvc.models;

import java.util.List;

public interface UserDAO {
	
	void addUser(UserDTO user);
	UserDTO getUserById(int id);
	UserDTO getUserByUsername(String username);
	List<UserDTO> getAllUsers();
	void updateUser(UserDTO user);
	void deleteUser(int id);
}
