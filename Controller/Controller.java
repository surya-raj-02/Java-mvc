package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Model.Model;
import View.View1;

public class Controller implements ActionListener {
    public static View1 view;

    public static void main(String args[]) {
        setView(new View1());
    }

    public static void setView(View1 view1) {
        view = view1;
    }

    public JTextField searchTermTextField = new JTextField(26);
    public DefaultTableModel model;

    public Controller(DefaultTableModel model) {
        super();
        this.model = model;
    }

    public Controller() {
        super();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Model.viewLogic(e, model, searchTermTextField, view);
    }

}