package dao;

import core.Db;
import entity.Book;
import entity.Model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class BookDao {

        private final Connection conn;
        private final CarDao carDao = new CarDao();
        public BookDao() {
            this.conn = Db.getInstance();
        }
        public ArrayList<Book> findAll() {
            return this.selectByQuery("SELECT * FROM public.book ORDER BY book_id ASC");
        }
//        public ArrayList<Book> getByListBrandId(int bookId)
//        {
//            return this.selectByQuery("SELECT * FROM public.model WHERE model_brand_id = " + bookId);
//        }
        public ArrayList<Book> selectByQuery(String query){
            ArrayList<Book> bookList = new ArrayList<>();
            try {;
                ResultSet rs = this.conn.createStatement().executeQuery(query);
                while (rs.next()) {
                    bookList.add(this.match(rs));
                }
            }
            catch (SQLException e){
                throw new RuntimeException();
            }
            return bookList;
        }

        public boolean save(Book book) {
            String query = "INSERT INTO public.book" +
                    "(" +
                    "book_car_id," +
                    "book_name," +
                    "book_idno," +
                    "book_mpno," +
                    "book_mail," +
                    "book_strt_date" +
                    "book_fnsh_date" +
                    "book_prc" +
                    "book_note" +
                    "book_case" +
                    ")" +
                    " VALUES (?,?,?,?,?,?,?,?,?,?)";
            try {
                PreparedStatement pr = this.conn.prepareStatement(query);
                pr.setInt(1,book.getCar_id());
                pr.setString(2,book.getName());
                pr.setString(3,book.getIdno());
                pr.setString(4,book.getMpno());
                pr.setString(5,book.getMail());
                pr.setDate(6, Date.valueOf(book.getStrt_date()));
                pr.setDate(7, Date.valueOf(book.getFnsh_date()));
                pr.setInt(8,book.getPrc());
                pr.setString(9,book.getbCase());
                pr.setString(10,book.getNote());
                return pr.executeUpdate() != -1;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }
        public boolean update (Book book) {
            String query = "UPDATE public.book SET " +
                    "book_car_id = ? , " +
                    "book_name = ? , " +
                    "book_idno = ? , " +
                    "book_mpno = ? , " +
                    "book_mail = ? , " +
                    "book_strt_date = ? , " +
                    "book_fnsh_date = ? , " +
                    "book_prc = ? , " +
                    "book_note = ? , " +
                    "book_case = ? " +
                    "WHERE book_id = ?";
            try {
                PreparedStatement pr = this.conn.prepareStatement(query);
                pr.setInt(1, book.getCar_id());
                pr.setString(2,book.getName());
                pr.setString(3,book.getIdno());
                pr.setString(4,book.getMpno());
                pr.setString(5,book.getMail());
                pr.setDate(6, Date.valueOf(book.getStrt_date()));
                pr.setDate(7, Date.valueOf(book.getFnsh_date()));
                pr.setInt(8,book.getPrc());
                pr.setString(9,book.getbCase());
                pr.setString(10,book.getNote());
                pr.setInt(11,book.getId());
                return pr.executeUpdate() != -1;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }

        public boolean delete(int book_id) {
            String query = "DELETE FROM public.book WHERE book_id = ?";
            try {
                PreparedStatement pr = this.conn.prepareStatement(query);
                pr.setInt(1,book_id);
                return pr.executeUpdate() != -1;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }
        public Book getById(int id){
            Book obj = null;
            String query = "SELECT * FROM public.book WHERE book_id = ?";
            try {PreparedStatement pr = this.conn.prepareStatement(query);
                pr.setInt(1,id);
                ResultSet rs = pr.executeQuery();
                if (rs.next()) {
                    obj = this.match(rs);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            return obj;
        }
        public Book match(ResultSet rs) throws SQLException{
            Book book = new Book();
            book.setCar(this.carDao.getById(rs.getInt("book_car_id")));
            book.setCar_id(rs.getInt("book_car_id"));
            book.setId(rs.getInt("book_id"));
            book.setName(rs.getString("book_name"));
            book.setIdno(rs.getString("book_idno"));
            book.setMpno(rs.getString("book_mpno"));
            book.setMail(rs.getString("book_mail"));
            book.setStrt_date(LocalDate.parse(rs.getString("book_strt_date")));
            book.setFnsh_date(LocalDate.parse(rs.getString("book_fnsh_date")));
            book.setNote(rs.getString("book_note"));
            book.setbCase(rs.getString("book_case"));
            book.setPrc(rs.getInt("book_prc"));
            return book;
        }
    }
