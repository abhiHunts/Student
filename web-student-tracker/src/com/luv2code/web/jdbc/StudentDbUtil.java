package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;



public class StudentDbUtil {
		
		private DataSource dataSource;
		
		public StudentDbUtil(DataSource theDataSource)
		{
				dataSource=theDataSource;
		}
		
		public List<Student> getStudents() throws Exception
		{
			List<Student> students = new ArrayList<>();
			
			Connection myConn=null;
			Statement myStmt=null;
			ResultSet myRs=null;
			
			
			try {
			//get connection
				myConn = dataSource.getConnection();
			//Create stmt
			String sql="select * from student order by last_name";
			myStmt = myConn.createStatement();
			
			//execute
			myRs=myStmt.executeQuery(sql);
			
			//process ResultSet
			while(myRs.next())
			{
				//retrieve data from result set
				int id =myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				//create new Student object
				Student tempStudent = new Student(id,firstName, lastName, email);
				students.add(tempStudent); 
				//add it to the list of Students
				
			}

				return students;
			}finally{
				//close JDBC
				close(myConn,myStmt,myRs);
			}
			
			
		}

		private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
			try {
				
						if(myRs!=null)
							myRs.close();
						if(myConn!=null)
							myConn.close();  // doesn't really close it jsut puts it back to conn pool
						if(myStmt!=null)
							myStmt.close();
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		public void addStudent(Student theStudent) throws SQLException {
			Connection myConn=null;
			PreparedStatement myStmt=null;
		
			try {
				//get db conn
				myConn=dataSource.getConnection();
			//create sql for insert
			String sql="insert into student "
					+ "(first_name,last_name,email) "
					+ "values (?, ?, ?)";
			myStmt=myConn.prepareStatement(sql);
			//set param
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			
			//execute sql
			myStmt.execute();
			}finally {
			//clean up JDBC obj
			close(myConn,myStmt,null);
			}
		}

		public Student getStudents(String theStudentId)throws Exception {
			Student theStudent = null;
			Connection myConn=null;
			PreparedStatement myStmt=null;
			ResultSet myRs=null;
			int studentId;
			try {
				studentId =Integer.parseInt(theStudentId);
				myConn=dataSource.getConnection();
				String sql="select * from student where id=?";
				myStmt=myConn.prepareStatement(sql);
				myStmt.setInt(1, studentId);
			myRs=myStmt.executeQuery();
			
			if(myRs.next())
			{
				String firstName=myRs.getString("first_name");
				String lastName=myRs.getString("last_name");
				String email=myRs.getString("email");
				
				theStudent = new Student(studentId,firstName, lastName, email);
			}
			else { 
				throw new Exception("Could not find student id "+studentId);
			}
			}finally {
				close(myConn,myStmt,myRs);
			}
			
			return theStudent;
		}

		public void updateStudent(Student theStudent)throws Exception {
			Connection myConn=null;
			PreparedStatement myStmt=null;
		
			try {
				//get db conn
				myConn=dataSource.getConnection();
			//create sql for insert
			String sql="update student "
					+ "set first_name=?,last_name=?,email=? "
					+"where id=?";
					
			myStmt=myConn.prepareStatement(sql);
			//set param
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			//execute sql
			myStmt.execute();
			}finally {
			//clean up JDBC obj
			close(myConn,myStmt,null);
			}
		}

		public void deleteStudent(String theStudentId) throws Exception {
			Connection myConn=null;
			PreparedStatement myStmt=null;
		
			try {
				int studentId = Integer.parseInt(theStudentId);
				//get db conn
				myConn=dataSource.getConnection();
			//create sql for insert
			String sql="delete from student where id=?";
					
			myStmt=myConn.prepareStatement(sql);
			//set param
			
			myStmt.setInt(1, studentId);
			//execute sql
			myStmt.execute();
			}finally {
			//clean up JDBC obj
			close(myConn,myStmt,null);
			}
			
		}

		

		
}
