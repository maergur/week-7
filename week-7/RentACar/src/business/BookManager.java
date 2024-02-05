package business;

import core.Helper;
import dao.BookDao;
import entity.Book;
import java.util.ArrayList;

public class BookManager {

    private final BookDao bookDao;
    public BookManager() {
        this.bookDao = new BookDao();
    }


    public boolean saveBook(Book book) {
        if(this.getById(book.getId()) !=null) {
            Helper.showMsg("error");
            return false;
        };
        return bookDao.save(book);
    }

    public Book getById(int bookId) {
        return bookDao.getById(bookId);
    }

    public boolean updateBook(Book book) {
        if (bookDao.getById(book.getId()) == null) {
            Helper.showMsg(book.getId() + " ID kayıtlı rezervasyon bulunamadı!");
            return false;
        }
        return this.bookDao.update(book);
    }

    public boolean deleteBook(int id) {
        if (this.getById(id) == null) {
            Helper.showMsg(id + " ID kayıtlı rezervasyon bulunamadı");
            return false;
        }
        return this.bookDao.delete(id);
    }

//    public ArrayList<Object[]> getForTable(int size, ArrayList<Car> cars) {
//        ArrayList<Object[]> carList = new ArrayList<>();
//        for (Car obj : cars) {
//            int i = 0;
//            Object[] rowObject = new Object[size];
//            rowObject[i++] = obj.getId();
//            rowObject[i++] = obj.getModel().getBrand().getName();
//            rowObject[i++] = obj.getModel().getName();
//            rowObject[i++] = obj.getPlate();
//            rowObject[i++] = obj.getColor();
//            rowObject[i++] = obj.getKm();
//            rowObject[i++] = obj.getModel().getYear();
//            rowObject[i++] = obj.getModel().getType();
//            rowObject[i++] = obj.getModel().getFuel();
//            rowObject[i++] = obj.getModel().getGear();
//            carList.add(rowObject);
//        }
//        return carList;
//    }
//
//    public ArrayList<Car> findAll() { return this.carDao.findAll(); }
}
