/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emart.dao;

import emart.dbutil.DBConnection;
import emart.pojo.ReceptionistPojo;
import emart.pojo.UserPojo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ReceptionistDAO {
    
    public static Map<String,String>getNotRegisteredReceptionist()throws SQLException
    {
        Connection conn=DBConnection.getConnection();
        Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery("Select empid,empname from employees where job='Receptionist' and empid not in (Select empid  from users  where usertype='Receptionist') ");
        HashMap<String,String>recpList=new HashMap<>();
        while(rs.next())
        {
            String empid=rs.getString(1);
            String empname=rs.getString(2);
            recpList.put(empid, empname);
        }
        return recpList;
    }
     public static boolean addReceptionist(UserPojo recp) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("Insert into users values(?,?,?,?,?)");
        ps.setString(1, recp.getUserid());
        ps.setString(2, recp.getEmpid());
        ps.setString(3, recp.getPassword());
        ps.setString(4, recp.getUsertype());
        ps.setString(5, recp.getUsername());
        int result = ps.executeUpdate();
        System.out.println("db");
        return result == 1;
    }
     public static ArrayList<ReceptionistPojo> getAllReceptionist()throws SQLException{
         
          Connection conn=DBConnection.getConnection();
          Statement st=conn.createStatement();
          ResultSet rs=st.executeQuery("Select users.empid,empname,userid,job,salary from users,employees where usertype='Receptionist'and users.empid=employees.empid order by empid");
          ArrayList<ReceptionistPojo>recplist=new ArrayList<>();
          while(rs.next())
          {
              ReceptionistPojo recp=new ReceptionistPojo();
              recp.setEmpid(rs.getString(1));
              recp.setEmpname(rs.getString(2));
              recp.setUserid(rs.getString(3));
              recp.setJob(rs.getString(4));
              recp.setSalary(rs.getDouble(5));
              recplist.add(recp);
          }
          return recplist;
     }
     
     public static Map<String,String> getAllReceptionistId()throws SQLException
     {
          Connection conn = DBConnection.getConnection();
          Statement st = conn.createStatement();
          ResultSet rs= st.executeQuery("Select userid ,username from users where usertype='Receptionist' order by empid");
          Map<String,String>recpid=new HashMap<>();
        while(rs.next())
        {
            String userid=rs.getString(1);
            String name=rs.getString(2);
            recpid.put(userid, name);
        }
        return recpid;
     }
     
 
      public static String getAllUserPassword(String recpid)throws SQLException{
         Connection conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement("Select password from users where userid=?");
        ps.setString(1, recpid);
        ResultSet rs = ps.executeQuery();
       String password =null;
        while (rs.next()) {
            password = rs.getString(1);
           
        }
        return password;
    }
      

    public static boolean UpdateRecptionist(String userid,String password) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("Update users set password=? where userid=?");
        ps.setString(1,password);
        ps.setString(2 ,userid);
        int result = ps.executeUpdate();
        return result == 1;
    }
    
      public static ArrayList<String> getAllUserId()throws SQLException{
         Connection conn = DBConnection.getConnection();
         Statement st = conn.createStatement();
        ResultSet rs= st.executeQuery("Select userid from users where usertype='Receptionist' order by empid");
        ArrayList<String> recpUserid = new ArrayList<>();
        while (rs.next()) {
            String urecpid = rs.getString(1);
            recpUserid.add(urecpid);
        }
        return recpUserid;
    }
   
    
    public static boolean removeAllUserId(String userid) throws SQLException{
         Connection conn = DBConnection.getConnection();
         PreparedStatement ps=conn.prepareStatement("delete from users where userid=?");
         ps.setString(1, userid);
         int result=ps.executeUpdate();
         return result==1;
    }
}