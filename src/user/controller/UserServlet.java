package user.controller;

import user.model.User;
import user.service.IUserService;
import user.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "UserServlet",urlPatterns = "/users")
public class UserServlet extends HttpServlet {
    IUserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    insertUser(request, response);
                    break;
                case "edit":
                    updateUser(request, response);
                    break;
                case "search":
                    searchUser(request, response);
//                    searchUserById(request,response);
                    break;
                case "sort":
                    sortUser(request, response);
                    break;
                default:
                    break;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void sortUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sortBy = request.getParameter("sortBy");
        String styleSort = request.getParameter("styleSort");

        List<User> userList = this.userService.sortUsers(sortBy, styleSort);

        request.setAttribute("users", userList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request, response);
    }

    private void searchUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        String value = request.getParameter("searchValue");
        List<User> userList = null;
        User user = null;

        boolean isString = value instanceof String;
        if (isString){
            userList = this.userService.searchUsers(value);
        }else {
            int id = Integer.parseInt(request.getParameter("searchValue"));
            user = this.userService.selectUserById(id);
            userList.add(user);
        }

        RequestDispatcher dispatcher;
        if (userList == null) {
            dispatcher = request.getRequestDispatcher("error-404.jsp");
        } else {
            request.setAttribute("users", userList);
            dispatcher = request.getRequestDispatcher("user/list.jsp");
        }
        dispatcher.forward(request, response);
    }

//    private void searchUserById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int id = Integer.parseInt(request.getParameter("searchValue"));
//
//        User user = this.userService.selectUserById(id);
//
//        RequestDispatcher dispatcher ;
//        if (user == null){
//            dispatcher= request.getRequestDispatcher("error-404.jsp");
//        }else {
//            request.setAttribute("users",user);
//            dispatcher = request.getRequestDispatcher("user/list.jsp");
//        }
//        dispatcher.forward(request, response);
//    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");
        int id = Integer.parseInt(request.getParameter("id"));

        User user = new User(id, name, email, country);
        this.userService.updateUser(user);

        request.getRequestDispatcher("user/createAndEdit.jsp");
        response.sendRedirect("/users");
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");

        User user = new User(name, email, country);
        this.userService.insertUser(user);

        RequestDispatcher dispatcher = request.getRequestDispatcher("user/createAndEdit.jsp");
        request.setAttribute("message", "New user was create");

        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    showInsertUser(request, response);
                    break;
                case "edit":
                    showUpdateUser(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
//                case "search":
//                    searchUserById(request,response);
//                    break;
                default:
                    showAllUsers(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        this.userService.deleteUser(id);

        List<User> userList = this.userService.SelectAllUsers();
        request.setAttribute("users", userList);

        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request, response);
    }

    private void showUpdateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User user = this.userService.selectUserById(id);

        request.setAttribute("user", user);

        RequestDispatcher dispatcher = request.getRequestDispatcher("user/createAndEdit.jsp");
        dispatcher.forward(request, response);
    }

    private void showInsertUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/createAndEdit.jsp");
        dispatcher.forward(request, response);
    }

    private void showAllUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> userList = this.userService.SelectAllUsers();

        request.setAttribute("users", userList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request, response);
    }
}
