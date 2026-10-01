package thuc.ute.repository;

import java.util.List;
import thuc.ute.entity.User_24110349;

public interface IUserRepository_24110349 {
	List<User_24110349> findAll();
	User_24110349 findById(int id);
	void insert(User_24110349 user);
	User_24110349 findByUserName(String username);
	boolean checkExistEmail(String email);
	boolean checkExistUserName(String username);
	boolean checkExistPhone(String phone);
	User_24110349 findByEmail(String email);
}
