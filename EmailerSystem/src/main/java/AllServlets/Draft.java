package AllServlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Draft
 */
@WebServlet("/Draft")
public class Draft extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Draft() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		String email= (String) request.getSession().getAttribute("email");
		out.println("<html><body><h1>Welcome "+ email +" !"); 
		request.getRequestDispatcher("welcome.html").include(request, response);
		
		HttpSession session=request.getSession(false);
		if(session==null){
			response.sendRedirect("index.html");
		}else{
			
			
			String msg=(String)request.getAttribute("msg");
			if(msg!=null){
				out.print("<p>"+msg+"</p>");
			}
			
			try{
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/emailersystem", "root", "");
				PreparedStatement ps=con.prepareStatement("select * from messageinfo where sender=? and draft='yes' order by id desc");
				ps.setString(1,email);
				ResultSet rs=ps.executeQuery();
				out.print("<h1>Draft mails </h1>");
				out.println("<br>");
				out.print("<table border='1' style='width:700px;'>");
				out.print("<tr style='background-color:grey;color:white'><td>To</td><td>Subject</td><td>Action</td></tr>");
				while(rs.next()){
					out.print("<tr><td>"+rs.getString("receiver")+"</td><td><a href='Viewmail?id="+rs.getString(1)+"' title='Click to View Mail'>"+rs.getString("subject")+"</a></td><td><a href='Deletemail?id="+rs.getString(1)+"'>Delete Mail</a></td></tr>");
				}
				out.print("</table>");
				
				con.close();
				out.print("<h1>Create a draft mail </h1>");
				out.print("<br><br>");
		   request.getRequestDispatcher("composeformdraft.html").include(request, response);
		
		}catch(Exception e){out.print(e); 
			}
		}

		out.close();

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
