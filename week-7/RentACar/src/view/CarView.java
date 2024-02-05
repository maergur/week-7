package view;

import business.CarManager;
import business.ModelManager;
import core.ComboItem;
import core.Helper;
import entity.Car;
import entity.Model;

import javax.swing.*;

public class CarView extends Layout{
    private JPanel container;
    private JLabel lbl_header;
    private JLabel lbl_model;
    private JComboBox cmb_models;
    private JLabel lbl_color;
    private JLabel lbl_km;
    private JComboBox cmb_color;
    private JTextField fld_car_km;
    private JLabel lbl_plate;
    private JTextField fld_car_plate;
    private JButton btn_save;
    private CarManager carManager;
    private ModelManager modelManager;
    private Car car;

public CarView(Car car){
    this.car = car;
    this.add(container);
    this.carManager = new CarManager();
    this.modelManager = new ModelManager();

    this.guiInitialize(400,300);

    this.cmb_color.setModel(new DefaultComboBoxModel<>(Car.Color.values()));

    for (Model model : this.modelManager.findAll()){
        this.cmb_models.addItem(model.getComboItem());
    }

    if (this.car.getId() != 0 ){
        ComboItem selectedItem = car.getModel().getComboItem();
        this.cmb_models.getModel().setSelectedItem(selectedItem);
        this.cmb_color.getModel().setSelectedItem(car.getColor());
        this.fld_car_plate.setText(car.getPlate());
        this.fld_car_km.setText(Integer.toString(car.getKm()));
    }

    this.btn_save.addActionListener(e -> {
        if (Helper.isFieldListEmpty(new JTextField[]{this.fld_car_km, this.fld_car_plate})){
            Helper.showMsg("fill");
        }
        else {
                boolean result;
                ComboItem selectedModel = (ComboItem) this.cmb_models.getSelectedItem();
                this.car.setModel_id(selectedModel.getKey());
                this.car.setColor((Car.Color) this.cmb_color.getSelectedItem());
                this.car.setPlate(this.fld_car_plate.getText());
                this.car.setKm(Integer.parseInt(this.fld_car_km.getText()));
                if (this.car.getId() != 0){
                    result = this.carManager.updateCar(this.car);
                } else {
                    result = this.carManager.saveCar(this.car);
                }

                if (result){
                    Helper.showMsg("done");
                    dispose();
                } else {
                    Helper.showMsg("error");
                }
            }
    });
}
}