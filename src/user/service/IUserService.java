package user.service;

import user.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserService {
    List<User> SelectAllUsers();

    User selectUserById(int id);

    void insertUser(User user) throws SQLException;

    boolean updateUser(User user);

    boolean deleteUser(int id);

    List<User> searchUsers(String value) throws SQLException;

    List<User> sortUsers(String sortBy,String styleSort);

    void insertUserProcedures(User user)throws SQLException;

    User getUserByIdProcedures();

    void addUserTransaction(User user, int[] permission)throws SQLException;
}
