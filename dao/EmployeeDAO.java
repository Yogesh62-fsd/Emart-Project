/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emart.dao;

import emart.dbutil.DBConnection;
import emart.pojo.EmployeePojo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public static String getNextEmployeeId() throws SQLException {

        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("Select max(empid) from employees");
        rs.next();
        String empid = rs.getString(1);
        int empno = Integer.parseInt(empid.substring(1));
        empno = empno + 1;
        String Empid = "E" + empno;
        return Empid;
    }

    public static boolean addEmployee(EmployeePojo emp) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("Insert into employees values(?,?,?,?)");
        ps.setString(1, emp.getEmpid());
        ps.setString(2, emp.getEmpname());
        ps.setString(3, emp.getJob());
        ps.setDouble(4, emp.getSal());
        int rows = ps.executeUpdate();
        return rows == 1;
    }

    public static List<EmployeePojo> getAllEmployee() throws SQLException {
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("Select * from employees order by empid");
        ArrayList<EmployeePojo> empList = new ArrayList();

        while (rs.next()) {
            EmployeePojo emp = new EmployeePojo();
            emp.setEmpid(rs.getString(1));
            emp.setEmpname(rs.getString(2));
            emp.setJob(rs.getString(3));
            emp.setSal(rs.getDouble(4));
            empList.add(emp);
        }
        return empList;
    }
//mymehtod to get all empid
   public static ArrayList<String> getAllEmpId() throws SQLException {
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(" Select empid from employees");
        ArrayList<String> empId = new ArrayList();
        while (rs.next()) {
            String empid = rs.getString(1);
          
            empId.add(empid); 
        }
        return empId;
    }
    public static EmployeePojo findEmployeeById(String empid)throws SQLException
    {
         Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement("Select  empname, job ,salary from employees where empid=?");
         ps.setString(1, empid);
         ResultSet rs=ps.executeQuery();
         EmployeePojo emp=null;
         if(rs.next()==true)
                 {
                     emp=new EmployeePojo();
                     emp.setEmpname(rs.getString(1));
                     emp.setJob(rs.getString(2));
                     emp.setSal(rs.getDouble(3));
                 }
         return emp;
    }

    public static boolean UpdateEmployee(EmployeePojo emp) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("Update employees  set empname=?,job=?, salary=? where empid=?");
        ps.setString(1, emp.getEmpname());
        ps.setString(2,emp.getJob());
        ps.setDouble(3,emp.getSal());
        ps.setString(4,emp.getEmpid());
        int result = ps.executeUpdate();
        if(result==0)
          return false;
        else{
               boolean exist=UserDAO.isUserPresent(emp.getEmpid());
               if(exist==false){
                   return true;
               }
             ps=conn.prepareStatement("Update users set username=?,usertype=? where empid=?");
             ps.setString(1, emp.getEmpname());
             ps.setString(2, emp.getJob());
             ps.setString(3, emp.getEmpid());
             int x=ps.executeUpdate();
             return(x==1); 
           }
    }
    public static boolean deleteEmployeeBYId(String empid)throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("Delete from employees where empid=?");
        ps.setString(1, empid);
        int result=ps.executeUpdate();
        return result==1;
        
    }
}
