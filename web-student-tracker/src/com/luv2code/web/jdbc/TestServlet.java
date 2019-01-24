package com.luv2code.web.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.mysql.cj.MysqlConnection;
import com.mysql.cj.protocol.a.authentication.MysqlClearPasswordPlugin;



/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(name="jdbc/web_student_tracker")
	private DataSource datasource; 
		
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Set up PrintWriter
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
					
		// Get Conn to database
			Connection myConn =null;
			Statement myStmt = null;
			ResultSet myRs = null;
			
			try {
				myConn = datasource.getConnection();
		//sql statement
		String sql = "select * from student";
		myStmt = myConn.createStatement();
		
		//Exe Statement
		myRs = myStmt.executeQuery(sql);
		
		//Process ResultSet
		while(myRs.next())
		{
			String email = myRs.getString("email");
			out.println(email);
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
