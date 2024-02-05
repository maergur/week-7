package dao;

import core.Db;
import core.Helper;
import entity.Car;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CarDao {

    private Connection conn;
    private final BrandDao brandDao;
    private final ModelDao modelDao;

    public CarDao() {
        this.conn = Db.getInstance();
        this.brandDao = new BrandDao();
        this.modelDao = new ModelDao();
    }

    public Car match(ResultSet rs) throws SQLException {
        Car car = new Car();
        car.setId(rs.getInt("car_id"));
        car.setModel_id(rs.getInt("car_model_id"));
        car.setPlate(rs.getString("car_plate"));
        car.setColor(Car.Color.valueOf(rs.getString("car_color")));
        car.setKm(rs.getInt("car_km"));
        car.setModel(modelDao.getById(car.getModel_id()));
        return car;
    }
    public  ArrayList<Car> findAll (){ return this.selectByQuery("SELECT * FROM public.car ORDER BY car_id ASC");}
    public ArrayList<Car> selectByQuery(String query) {
        ArrayList<Car> carList = new ArrayList<>();
        try {
            ResultSet rs = this.conn.createStatement().executeQuery(query);
            while (rs.next()) {
                carList.add(this.match(rs));
            }
        } catch (SQLException e) {
            Helper.showMsg(e.getMessage());
        }
        return carList;
    }

    public Car getById(int id) {
        Car obj = null;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM public.car WHERE car_id = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) obj = this.match(rs);
        } catch (SQLException e) {
            Helper.showMsg(e.getMessage());
        }
        return obj;
    }

    public boolean update(Car car) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE public.car SET " +
                    "car_model_id = ?, " +
                    "car_color = ?, " +
                    "car_km = ?, " +
                    "car_plate = ? " +
                    "WHERE car_id = ?");
            preparedStatement.setInt(1, car.getModel_id());
            preparedStatement.setString(2, car.getColor().toString());
            preparedStatement.setInt(3, car.getKm());
            preparedStatement.setString(4, car.getPlate());
            preparedStatement.setInt(5, car.getId());
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            Helper.showMsg(e.getMessage());
        }
        return true;
    }
    public boolean save(Car car) {
        String query = "INSERT INTO public.car" +
                "(" +
                "car_model_id," +
                "car_color," +
                "car_km," +
                "car_plate" +
                ")" +
                " VALUES (?,?,?,?)";
        try {
            PreparedStatement pr = this.conn.prepareStatement(query);
            pr.setInt(1,car.getModel_id());
            pr.setString(2,car.getColor().toString());
            pr.setInt(3,car.getKm());
            pr.setString(4, car.getPlate());
            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();        }
        return true;
    }
    public boolean delete(int id) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM public.car WHERE car_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            Helper.showMsg(e.getMessage());
            return false;
        }
    }

    public ArrayList<Car> search(String str) {
        ArrayList<Car> carList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM public.car WHERE car_plate LIKE ? OR car_color LIKE ?");
            preparedStatement.setString(1, "%" + str + "%");
            preparedStatement.setString(2, "%" + str + "%");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                carList.add(match(rs));
            }
        } catch (SQLException e) {
            Helper.showMsg(e.getMessage());
            return null;
        }
        return carList;
    }

    public ArrayList<Car> search(int id) {
        ArrayList<Car> carList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM public.car WHERE car_model_id = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                carList.add(match(rs));
            }
        } catch (SQLException e) {
            Helper.showMsg(e.getMessage());
            return null;
        }
        return carList;
    }

}