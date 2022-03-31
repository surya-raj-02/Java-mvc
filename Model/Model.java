package Model;

import java.awt.event.ActionEvent;

import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import View.View;
import View.View1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



class DataForTable {

    public static Object[] TABLE_HEADER = { "Name", "Mobile", "DOB", "Address" };

    public static Object[][] DATA;

    public static void setData(Object[][] data) {
        DATA = data;
    }

    public static Object[][] getData() {
        return DATA;
    }

}

@SuppressWarnings("serial")
public class Model extends DefaultTableModel {
    public static String data;
    public static String data1;
    public static String data2;
    public static String data3;
    public static Connection con;
    public static int count;

    public Model() {
        super(Constants.DATA, Constants.TABLE_HEADER);
    }

    public static void addToPostgres(String name, String mobile, String dob, String address) {
        data = name;
        data1 = mobile;
        data2 = dob;
        data3 = address;
        String sql = "INSERT INTO mvc (\"name\", \"mobile\", \"dob\", \"address\") VALUES ('" + data + "','" + data1 + "','" + data2 + "','" + data3 + "')";
         try (
                 Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
             rs.next();
             count = rs.getInt(1);
         } catch (SQLException ex) {
             System.out.println(ex.getMessage());
         }
    }
    public static void connectToPostgres() {
        try {
			con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
		} catch (SQLException e) {
			e.printStackTrace();
		}
        System.out.println("Connected to Postgres");

    }

    public static void getFromPostgresIntoObject(){
        String sql = "SELECT COUNT(*) AS count FROM mvc;";
        try (
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
	            while(rs.next()){
	                count = rs.getInt("count");
	                System.out.println(count);
	            }
	            System.out.println("Counted");
        	}
        catch (SQLException ex) {
                System.out.println("AAA"+ex.getMessage());
            }	
    }
    public static void returnCount(){
        System.out.println(count);
    }
    public static void viewLogic(ActionEvent e, DefaultTableModel model, JTextField searchTermTextField, View1 view) {
        if (e.getSource() == View1.sub) 
        {
            if (View1.term.isSelected()) {
                data = View1.tname.getText();
                data1 = View1.tmno.getText();
                data2 = (String) View1.date.getSelectedItem()
                        + "/" + (String) View1.month.getSelectedItem()
                        + "/" + (String) View1.year.getSelectedItem();

                data3 = View1.tadd.getText();
                connectToPostgres();
                getFromPostgresIntoObject();
                Constants.DATA = new Object[count+1][Constants.TABLE_HEADER.length];// { { data, data1, data2, data3 } };
                Constants.DATA[0][0] = data;
                Constants.DATA[0][1] = data1;
                Constants.DATA[0][2] = data2;
                Constants.DATA[0][3] = data3;
                String sql = "SELECT * FROM mvc;";
                    try (
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(sql)){
                    		int i = 1;
            	            while(rs.next()){
            	            	Constants.DATA[i][0] = rs.getString("name");
            	            	Constants.DATA[i][1] = rs.getString("mobile");
            	            	Constants.DATA[i][2] = rs.getString("dob");
            	            	Constants.DATA[i][3] = rs.getString("address");
            	            	i++;
            	            }
                    	}
                    catch (SQLException ex) {
                            System.out.println("AAA"+ex.getMessage());
                        }
                        addToPostgres(data, data1, data2, data3);
                         view.setVisible(false);
                        new View();
                }
            else {
            		data = View1.tname.getText();
					data1 = View1.tmno.getText();
					data2 = (String) View1.date.getSelectedItem()
					        + "/" + (String) View1.month.getSelectedItem()
					        + "/" + (String) View1.year.getSelectedItem();
					
					data3 = View1.tadd.getText();
					Constants.DATA = new Object[][] { { data, data1, data2, data3 } };
					connectToPostgres();
                    getFromPostgresIntoObject();
                    returnCount();
					addToPostgres(data, data1, data2, data3);
					 view.setVisible(false);
					new View();
            }
        }

        else if (e.getSource() == View1.reset) {
            String def = "";
            View1.tname.setText(def);
            View1.tadd.setText(def);
            View1.tmno.setText(def);
            View1.res.setText(def);
            View1.tout.setText(def);
            View1.term.setSelected(false);
            View1.date.setSelectedIndex(0);
            View1.month.setSelectedIndex(0);
            View1.year.setSelectedIndex(0);
        }
    }
}