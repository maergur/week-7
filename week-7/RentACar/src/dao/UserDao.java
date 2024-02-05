package dao;

import core.Db;
import java.sql.*;
import java.util.ArrayList;
import entity.User;

import javax.swing.plaf.nimbus.State;

public class UserDao {
    private final Connection conn;
    public UserDao() {
        this.conn = Db.getInstance();
    }

    public ArrayList<User> findAll() {
        ArrayList<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM public.user";
        try {;
            ResultSet rs = this.conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                userList.add(this.match(rs));
            }
        }
        catch (SQLException e){
            throw new RuntimeException();
        }
        return userList;
    }

    public User findByLogin(String username, String password) {
        User obj = null;
        String query = "SELECT * FROM public.user WHERE user_name = ? AND user_pass = ?";
        try {
            PreparedStatement pr = this.conn.prepareStatement(query);
            pr.setString(1,username);
            pr.setString(2,password);
            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                obj = this.match(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return obj;
    }

    public User match(ResultSet rs) throws SQLException{
        User obj = new User();
        obj.setId(rs.getInt("user_id"));
        obj.setUsername(rs.getString("user_name"));
        obj.setPassword(rs.getString("user_pass"));
        obj.setRole(rs.getString("user_role"));

        return obj;
    }
}
