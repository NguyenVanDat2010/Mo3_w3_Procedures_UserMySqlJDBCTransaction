package user.service;

import user.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IUserService {
    private String jdbcUrl = "jdbc:mysql://localhost:3306/user_manager";
    private String jdbcUsername = "root";
    private String jdbcPassword = "123456";

    //SQL Query
    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String SELECT_USERS_BY_ID = "select * from users where id = ?";
    private static final String INSERT_USERS_VALUES = "insert into users(name ,email, country)" + "value(? ,? ,?)";
    private static final String UPDATE_USERS_VALUES = "update users set name = ?,email = ?, country = ? where id = ?";
    private static final String DELETE_USERS_VALUES = "delete from users where id = ?";
    private static final String SEARCH_USERS_VALUES = "select * from users where name = ? or email = ? or country = ?";

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
            System.out.println("Connection success");
        } catch (ClassNotFoundException e) {
            // TODO auto-generated catch block
            e.printStackTrace();
            System.out.println("Connection failed");
        } catch (SQLException throwables) {
            // TODO auto-generated catch block
            throwables.printStackTrace();
            System.out.println("Connection failed");
        }
        return connection;
    }

    @Override
    public List<User> SelectAllUsers() {
        List<User> users = new ArrayList<>();
        try (
                Connection connection = getConnection();
                PreparedStatement prstmt = connection.prepareStatement(SELECT_ALL_USERS);
        ) {
            System.out.println(prstmt);
            ResultSet rs = prstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(id, name, email, country));
            }
        } catch (SQLException throwables) {
            printSQLException(throwables);
        }
        return users;
    }

    @Override
    public User selectUserById(int id) {
        User user = null;
        try (
                Connection connection = getConnection();
                PreparedStatement prstmt = connection.prepareStatement(SELECT_USERS_BY_ID);
        ) {
            System.out.println(prstmt);
            prstmt.setInt(1,id);
            ResultSet rs = prstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                user = new User(name, email, country);
            }
        } catch (SQLException throwables) {
            printSQLException(throwables);
        }
        return user;
    }

    @Override
    public void insertUser(User user) throws SQLException {
        try (
                Connection connection = getConnection();
                PreparedStatement prstmt = connection.prepareStatement(INSERT_USERS_VALUES);
        ) {
            System.out.println(prstmt);
            prstmt.setString(1, user.getName());
            prstmt.setString(2, user.getEmail());
            prstmt.setString(3, user.getCountry());
            System.out.println(prstmt);

            prstmt.executeUpdate();
            System.out.println("Insert record successfully.");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    @Override
    public boolean updateUser(User user) {
        boolean rowUpdated = false;
        try (
                Connection connection = getConnection();
                PreparedStatement prstmt = connection.prepareStatement(UPDATE_USERS_VALUES);
        ) {
            System.out.println(prstmt);
            prstmt.setString(1, user.getName());
            prstmt.setString(2, user.getEmail());
            prstmt.setString(3, user.getCountry());
            prstmt.setInt(4, user.getId());

            System.out.println(prstmt);

            rowUpdated = prstmt.executeUpdate() > 0;
            System.out.println("Update successful yet? " + rowUpdated);
        } catch (SQLException throwables) {
            printSQLException(throwables);
        }
        return rowUpdated;
    }

    @Override
    public boolean deleteUser(int id) {
        boolean rowDeleted = false;
        try (
                Connection connection = getConnection();
                PreparedStatement prstmt = connection.prepareStatement(DELETE_USERS_VALUES);
        ){
            System.out.println(prstmt);
            prstmt.setInt(1,id);
            rowDeleted = prstmt.executeUpdate()>0;
            System.out.println("Successfully deleted? "+rowDeleted);
        } catch (SQLException throwables) {
            printSQLException(throwables);
        }
        return rowDeleted;
    }

    @Override
    public List<User> searchUsers(String value) throws SQLException {
        List<User> userList = new ArrayList<>();

        value = checkSpaceValue(value);
        try (
                Connection connection = getConnection();
                PreparedStatement prstmt = connection.prepareStatement(SEARCH_USERS_VALUES);
        ){
            System.out.println(prstmt);
//            prstmt.setInt(1,id);
            prstmt.setString(1,value);
            prstmt.setString(2,value);
            prstmt.setString(3,value);

            ResultSet rs = prstmt.executeQuery();
//            System.out.println("Gia tri cua rs "+rs);
            while (rs.next()){
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                userList.add(new User(name,email,country));
            }
        }
        return userList;
    }

    @Override
    public List<User> sortUsers(String sortBy, String styleSort) {
        List<User> userList = new ArrayList<>();
        if (!sortBy.equals("id") && !sortBy.equals("name") && !sortBy.equals("email") && !sortBy.equals("country")){
            sortBy = "id";
        }
        if (!styleSort.equals("asc")&&!styleSort.equals("desc")){
            styleSort="asc";
        }

        String SORT_USERS_VALUES = "select * from users order by "+sortBy+" "+styleSort;

        try (
                Connection connection = getConnection();
                PreparedStatement prstmts = connection.prepareStatement(SORT_USERS_VALUES);
        ) {
            System.out.println(prstmts);
            ResultSet rs = prstmts.executeQuery();

            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                userList.add(new User(id,name,email,country));
            }
        } catch (SQLException throwables) {
            printSQLException(throwables);
        }
        return userList;
    }

    @Override
    public void insertUserProcedures(User user) throws SQLException {

    }

    @Override
    public User getUserByIdProcedures() {
        return null;
    }

    @Override
    public void addUserTransaction(User user, int[] permission) throws SQLException {

    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    private String checkSpaceValue(String value) {
        if (value == null){
            return " ";
        }else
        if (value.charAt(0) == ' ' || value.charAt(value.length() - 1) == ' ') {
            value = value.trim(); //xóa khoảng trắng ở đầu và cuối thôi
        }
        return value;
    }
}
