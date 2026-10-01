package thuc.ute.service.impl;

import java.util.List;

import thuc.ute.entity.User_24110349;
import thuc.ute.repository.IUserRepository_24110349;
import thuc.ute.repository.impl.UserRepositoryImpl_24110349;
import thuc.ute.service.IUserService_24110349;

public class UserServiceImpl_24110349 implements IUserService_24110349 {

    private IUserRepository_24110349 userRepo = new UserRepositoryImpl_24110349();

    @Override
    public List<User_24110349> findAll() {
        return userRepo.findAll();
    }

    @Override
    public User_24110349 findById(int id) {
        return userRepo.findById(id);
    }

    @Override
    public void insert(User_24110349 user) {
        userRepo.insert(user);
    }

    @Override
    public User_24110349 findByUserName(String username) {
        return userRepo.findByUserName(username);
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userRepo.checkExistEmail(email);
    }

    @Override
    public boolean checkExistUserName(String username) {
        return userRepo.checkExistUserName(username);
    }

    @Override
    public boolean checkExistPhone(String phone) {
        return userRepo.checkExistPhone(phone);
    }

    @Override
    public User_24110349 findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User_24110349 login(String email, String passwd) {
        User_24110349 user = this.findByEmail(email);
        if (user != null && passwd.equals(user.getPasswd())) {
            return user;
        }
        return null;
    }

    @Override
    public void registerUser(User_24110349 user) {
        this.insert(user);
    }
}
