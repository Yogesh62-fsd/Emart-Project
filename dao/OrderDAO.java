/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emart.dao;

import emart.dbutil.DBConnection;
import emart.pojo.ProductPojo;
import emart.pojo.UserProfile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dell
 */
public class OrderDAO {

    public static String getNextOrderId() throws SQLException {

        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("Select Max(order_id) from orders");
        rs.next();
        String orderId = rs.getString(1);
        if (orderId == null) {
            return "O-101";
        }
        int orderno = Integer.parseInt(orderId.substring(2));
        orderno++;
        return "O-" + orderno;
    }

    public static boolean addOrders(List<ProductPojo> al, String orderid) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("Insert into orders values(?,?,?,?)");

        int count = 0;
        for (ProductPojo p : al) {
            ps.setString(1, orderid);
            ps.setString(2, p.getProductId());
            ps.setInt(3, p.getQuantity());
            ps.setString(4, UserProfile.getUserid());
            count = count + ps.executeUpdate();
        }
        return count == al.size();
    }

    public static List<String> getAllOrderId() throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("Select distinct Order_id  from orders order by order_id");
        ResultSet rs = ps.executeQuery();
        ArrayList<String> orderid = new ArrayList<>();
        while (rs.next()) {
            String ordid = rs.getString(1);
            orderid.add(ordid);
        }
        return orderid;
    }

    public static List<String> getAllOrderIdByReceptionist(String userid) throws SQLException {
        Connection conn = DBConnection.getConnection();
          System.out.println("Userid of Receptionist is "+userid);
        PreparedStatement ps = conn.prepareStatement("Select distinct Order_id  from orders where userid=? order by order_id");
        ps.setString(1, userid);
        ResultSet rs = ps.executeQuery();
        ArrayList<String> orderid = new ArrayList<>();
        while (rs.next()) {
            String ordid = rs.getString(1);
            orderid.add(ordid);
        }
        return orderid;
    }

    public static HashMap<String, Long> getAllProductByOrderId(String orderid) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("Select p_id ,quantity from orders where order_id=? order by p_id");
        ps.setString(1, orderid);
        ResultSet rs = ps.executeQuery();
        HashMap<String, Long> pidlist = new HashMap<>();
        while (rs.next()) {
            String pid = rs.getString(1);
            Long quantity = rs.getLong(2);
            System.out.println("pid"+pid);
            System.out.println("quant"+quantity);
            pidlist.put(pid, quantity);
        }
        return pidlist;
    }
}
