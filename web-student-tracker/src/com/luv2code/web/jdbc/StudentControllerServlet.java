package com.luv2code.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private StudentDbUtil studentDbUtil;
    
    @Resource(name="jdbc/web_student_tracker")
    private DataSource dataSource;
    
    
  
	@Override
	public void init() throws ServletException {
		super.init();
		
		//create instance of student db util  and pass to conn pool / DataSource
		try {
			studentDbUtil = new StudentDbUtil(dataSource);
		}catch(Exception e)
		{
			throw new ServletException(e);
		}
	}



	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//list the Student in Mvc fashion
		try {
			//read the command  parameter
			String theCommand= request.getParameter("command"); 
			//if the command is missing , then default to listing student
			if(theCommand==null)
			{
				theCommand="LIST";
			}
			//route the appropriate method
			switch(theCommand)
			{
			case "LIST":
				listStudents(request, response);
				break;
			case "ADD":
				addStudent(request,response);
				break;	
			case "LOAD": 
				loadStudent(request,response);
				break;
			case "UPDATE":
				updateStudent(request,response);
				break;
			case "DELETE":
				deleteStudent(request,response);
				break;
			default:
				listStudents(request, response);
			}
			
			listStudents(request,response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}



	private void deleteStudent(HttpServletRequest request, HttpServletResponse response)throws Exception {
		String theStudentId = request.getParameter("studentId");
		
		
		//delete student from database
		
		studentDbUtil.deleteStudent(theStudentId);
		//send them back to list-student page
		listStudents(request, response);
	}



	private void updateStudent(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
			//read student info from data
			int id = Integer.parseInt(request.getParameter("studentId"));
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String email = request.getParameter("email");
			// create a new Student obj
			Student theStudent = new Student(id,firstName,lastName,email);
			//preform update on database
			
			studentDbUtil.updateStudent(theStudent);
			//send them back to list-student page
			listStudents(request, response);
	}



	private void loadStudent(HttpServletRequest request, HttpServletResponse response)throws Exception {
			//read student id from data
			String theStudentId= request.getParameter("studentId");
			// get student from database
			Student theStudent= studentDbUtil.getStudents(theStudentId);
			//place student in req attribute
			request.setAttribute("THE_STUDENT", theStudent);
			//send to jsp page: update-student-form.jsp
			RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
			dispatcher.forward(request, response);
	}



	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
			//read the student info from form data
		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String email=request.getParameter("email");
		//create a student obj
		Student theStudent=new Student(firstName,lastName,email);
		//add Student to database
		studentDbUtil.addStudent(theStudent);
		
		//send back to main page
		listStudents(request, response);
	}



	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		//get students from DB until
		List<Student> students = studentDbUtil.getStudents();
		
		//add student to req
		request.setAttribute("STUDENT_LIST", students);
		
		//send it to jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-student.jsp");
		dispatcher.forward(request, response);
	}

	
}
