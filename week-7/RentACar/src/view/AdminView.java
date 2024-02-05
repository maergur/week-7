package view;

import business.BrandManager;
import business.CarManager;
import business.ModelManager;
import core.ComboItem;
import core.Helper;
import entity.Brand;
import entity.Car;
import entity.Model;
import entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.event.*;
import java.text.ParseException;
import java.util.ArrayList;

public class AdminView extends Layout {
    private JPanel container;
    private User user;
    private JLabel lbl_welcome;
    private JPanel w_top;
    private JTabbedPane tab_menu;
    private JPanel pnl_brand;
    private JButton logOutButton;
    private JScrollPane scrl_brand;
    private JTable tbl_brand;
    private JPanel pnl_model;
    private JTable tbl_model;
    private JScrollPane scrl_model;
    private JPanel pnl_fltr;
    private JComboBox cmb_s_model_id;
    private JComboBox cmb_s_model_type;
    private JComboBox cmb_s_model_fuel;
    private JComboBox cmb_s_model_gear;
    private JButton btn_search_model;
    private JButton btn_cancel_model;
    private JPanel pnl_car;
    private JTable tbl_car;
    private JPanel pnl_res;
    private JTable tbl_res;
    private JLabel start_dt_lbl;
    private JLabel fnsh_dt_lbl;
    private JLabel gear_type_lbl;
    private JLabel fuel_type_lbl;
    private JLabel car_type_lbl;
    private JTextField strt_dt_fld;
    private JTextField fnsh_dt_fld;
    private JComboBox gear_typ_cmb;
    private JComboBox fuel_type_cmb;
    private JComboBox car_type_cmb;
    private JButton srch_car_btn;
    private JButton rst_car_btn;
    private DefaultTableModel tmdl_brand = new DefaultTableModel();
    private DefaultTableModel tmdl_model = new DefaultTableModel();
    private DefaultTableModel tmdl_car = new DefaultTableModel();
    private DefaultTableModel tmdl_res = new DefaultTableModel();
    private BrandManager brandManager;
    private ModelManager modelManager;
    private CarManager carManager;
    private JPopupMenu jPopMenuBrand;
    private JPopupMenu jPopMenuModel;
    private JPopupMenu jPopupMenuCar;
    private JPopupMenu jPopupMenuBook;
    private Object[] col_model;
    private Object[] col_car;

    public AdminView(User user){
        this.brandManager = new BrandManager();
        this.modelManager = new ModelManager();
        this.carManager = new CarManager();
        this.add(container);
        this.guiInitialize(1000,500);
        this.user = user;

        if (this.user == null) {
            this.dispose();
        }

        this.lbl_welcome.setText("Hoşgeldiniz " + this.user.getUsername());

        // Brand Tab Menu
        loadBrandTable();
        loadBrandComponent();

        // Model Tab Menu
        loadModelTable(null);
        loadModelComponent();
        loadModelFilter();

        // Car Tab Menu
        loadCarTable();
        loadCarComponent();

        // Booking Tab Menu
        loadBookingTable(null);
        loadBookingComponent();
        loadBookingFilter();


        this.tbl_model.setComponentPopupMenu(jPopMenuModel);
        this.tbl_brand.setComponentPopupMenu(jPopMenuBrand);
        this.tbl_car.setComponentPopupMenu(jPopupMenuCar);


        logOutButton.addActionListener(e -> {
            dispose();
        });
        srch_car_btn.addActionListener(e-> {

                ArrayList<Car> carList = this.carManager.searchForBooking(strt_dt_fld.getText(),
                        fnsh_dt_fld.getText(),
                        (Model.Type)car_type_cmb.getSelectedItem(),
                        (Model.Gear)gear_typ_cmb.getSelectedItem(),
                        (Model.Fuel)fuel_type_cmb.getSelectedItem());
            ArrayList<Object[]> carBookingRow = this.carManager.getForTable(this.col_car.length,carList);
            loadBookingTable(carBookingRow);
        });
        rst_car_btn.addActionListener(e->{
            loadBookingFilter();
        });
    }
    public void loadModelFilter(){
        this.cmb_s_model_type.setModel(new DefaultComboBoxModel<>(Model.Type.values()));
        this.cmb_s_model_type.setSelectedItem(null);
        this.cmb_s_model_gear.setModel(new DefaultComboBoxModel<>(Model.Gear.values()));
        this.cmb_s_model_gear.setSelectedItem(null);
        this.cmb_s_model_fuel.setModel(new DefaultComboBoxModel<>(Model.Fuel.values()));
        this.cmb_s_model_fuel.setSelectedItem(null);
        loadModelFilterBrand();
    }
    public void loadModelFilterBrand() {
        this.cmb_s_model_id.removeAllItems();
        for (Brand obj : brandManager.findAll()) {
            this.cmb_s_model_id.addItem(new ComboItem(obj.getId(),obj.getName()));
        }
        this.cmb_s_model_id.setSelectedItem(null);
    }

    public void loadModelComponent() {
        tableRowSelect(this.tbl_model);
        this.jPopMenuModel = new JPopupMenu();
        this.jPopMenuModel.add("Yeni").addActionListener(e-> {
            ModelView modelView = new ModelView(new Model());
            modelView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadModelTable(null);
                }
            });
        });
        this.jPopMenuModel.add("Güncelle").addActionListener(e ->{
            int selectModelId = this.getTableSelectedRow(tbl_model,0);
            ModelView modelView = new ModelView(this.modelManager.getById(selectModelId));
            modelView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadModelTable(null);
                }
            });
        } );
        this.jPopMenuModel.add("Sil").addActionListener(e -> {
            if(Helper.confirm("sure")) {
                int selectModelId = this.getTableSelectedRow(tbl_model, 0);
                if(this.modelManager.delete(selectModelId)){
                    Helper.showMsg("done");
                    loadModelTable(null);
                }else {
                    Helper.showMsg("error");
                }
            }
        });

        this.btn_search_model.addActionListener(e ->{
            ComboItem selectedBrand = (ComboItem) this.cmb_s_model_id.getSelectedItem();
            int brandId = 0;
            if (selectedBrand != null) {
                brandId = selectedBrand.getKey();
            }
            ArrayList<Model> modelListBySearch = this.modelManager.searchForTable(
                    brandId,
                    (Model.Fuel) cmb_s_model_fuel.getSelectedItem(),
                    (Model.Gear) cmb_s_model_gear.getSelectedItem(),
                    (Model.Type) cmb_s_model_type.getSelectedItem()
            );

            ArrayList<Object[]> modelRowListBySearch = this.modelManager.getForTable(this.col_model.length,modelListBySearch);
            loadModelTable(modelRowListBySearch);
        });

        this.btn_cancel_model.addActionListener(e->{
            this.cmb_s_model_type.setSelectedItem(null);
            this.cmb_s_model_gear.setSelectedItem(null);
            this.cmb_s_model_fuel.setSelectedItem(null);
            this.cmb_s_model_id.setSelectedItem(null);
            loadModelTable(null);
        });

    }

    public void loadModelTable(ArrayList<Object[]> modelList) {
        this.col_model = new Object[] {"Model ID", "Marka", "Model Adı", "Tip", "Yıl", "Yakıt Türü", "Vites"};
        if (modelList == null) {
        modelList = this.modelManager.getForTable(col_model.length, this.modelManager.findAll());
        }
        createTable(this.tmdl_model, this.tbl_model,col_model,modelList);
    }

    public void loadBookingTable(ArrayList<Object[]> carList) {
        Object[] col_booking_list = {"ID", "Marka", "Model", "Plaka", "Renk", "KM", "Yıl", "Tip", "Yakıt Türü", "Vites"};
        createTable(this.tmdl_res, this.tbl_res,col_booking_list,carList);
    }

    public void loadBookingComponent(){
        tableRowSelect(this.tbl_res);
        this.jPopupMenuBook = new JPopupMenu();
        this.jPopupMenuBook.add("Rezervasyon Yap").addActionListener(e -> {

        });
        this.tbl_res.setComponentPopupMenu(jPopupMenuBook);
    }

    public void loadBookingFilter() {
        this.car_type_cmb.setModel(new DefaultComboBoxModel<>(Model.Type.values()));
        this.car_type_cmb.setSelectedItem(null);
        this.gear_typ_cmb.setModel(new DefaultComboBoxModel<>(Model.Gear.values()));
        this.gear_typ_cmb.setSelectedItem(null);
        this.fuel_type_cmb.setModel(new DefaultComboBoxModel<>(Model.Fuel.values()));
        this.fuel_type_cmb.setSelectedItem(null);
    }
    public void loadBrandTable() {

        String[] col_brand = {"Marka ID", "Marka Adı"};
        ArrayList<Object[]> brandList = this.brandManager.getForTable(col_brand.length);
        this.createTable(this.tmdl_brand, this.tbl_brand,col_brand,brandList);
    }

    public void loadBrandComponent() {
        tableRowSelect(this.tbl_brand);
        this.jPopMenuBrand = new JPopupMenu();
        this.jPopMenuBrand.add("Yeni").addActionListener(e-> {
            BrandView brandView = new BrandView(null);
            brandView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilterBrand();
                }
            });
        });
        this.jPopMenuBrand.add("Güncelle").addActionListener(e ->{
            int selectBrandId = this.getTableSelectedRow(tbl_brand,0);
            BrandView brandView = new BrandView(this.brandManager.getById(selectBrandId));
            brandView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilterBrand();
                    loadCarTable();
                }
            });
        } );
        this.jPopMenuBrand.add("Sil").addActionListener(e -> {
            if(Helper.confirm("sure")) {
                int selectBrandId = this.getTableSelectedRow(tbl_brand, 0);
                if(this.brandManager.delete(selectBrandId)){
                    Helper.showMsg("done");
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilterBrand();
                    loadCarTable();
                }else {
                    Helper.showMsg("error");
                }
            }
        });

    }
    public void loadCarTable(){
        col_car = new Object[]{"ID", "Marka", "Model", "Plaka", "Renk", "KM", "Yıl", "Tip", "Yakıt Türü", "Vites"};
        ArrayList<Object[]> carList = this.carManager.getForTable(col_car.length, this.carManager.findAll());
        createTable(this.tmdl_car, this.tbl_car,col_car,carList);
    }



    public void loadCarComponent(){
        tableRowSelect(this.tbl_car);
        this.jPopupMenuCar = new JPopupMenu();
        this.jPopupMenuCar.add("Yeni").addActionListener(e -> {
            CarView carView = new CarView(new Car());
            carView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCarTable();
                }
            });
        });
        this.jPopupMenuCar.add("Güncelle").addActionListener(e -> {
            int selectedCarId = this.getTableSelectedRow(tbl_car, 0);
            CarView carView = new CarView(this.carManager.getById(selectedCarId));
            carView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCarTable();
                }
            });
        });
        this.jPopupMenuCar.add("Sil").addActionListener(e -> {
            if (Helper.confirm("sure")){
                int selectedCarId = this.getTableSelectedRow(tbl_car, 0);
                if (this.carManager.deleteCar(selectedCarId)){
                    Helper.showMsg("done");
                    loadCarTable();
                } else {
                    Helper.showMsg("error");
                }
            }

        });;
        this.tbl_car.setComponentPopupMenu(jPopupMenuCar);
    }


    private void createUIComponents() throws ParseException {
        this.strt_dt_fld = new JFormattedTextField(new MaskFormatter("##/##/####"));
        this.strt_dt_fld.setText("10/10/2023");
        this.fnsh_dt_fld = new JFormattedTextField(new MaskFormatter("##/##/####"));
        this.fnsh_dt_fld.setText("16/10/2023");

    }
}
