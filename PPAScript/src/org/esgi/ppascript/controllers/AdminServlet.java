package org.esgi.ppascript.controllers;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.esgi.model.impl.User;
import org.esgi.model.interfaces.IUser;
import org.esgi.dao.UserDao;

public class AdminServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private Pattern _actionPattern;

	@Override
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);
        _actionPattern = Pattern.compile(".+/([^/]+)$");
        File file = new File(this.getServletContext().getRealPath("/WEB-INF/users/users.xml"));
		UserDao.getSharedUserDao().setFile(file);
    }

    @Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  
	{
		Matcher m = _actionPattern.matcher(request.getRequestURI());
		String action = m.find()?m.group(1):null;
		try
		{
			this.getClass().getMethod(action, HttpServletRequest.class, HttpServletResponse.class).invoke(this, request, response);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			response.sendRedirect(request.getContextPath()+"/pageNotFound");
		} 
	}
    
    public void pageNotFound(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Writer writer = response.getWriter();
		writer.write("<html><head><title>404 PageNotFound</title>></head><body>Page Inconnue !!</body></html>");
	}
    
    public void user_create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	IUser user = new User();
    	user.setLogin("roubert");
    	user.setPassword("mec");
    	user.setCanModify(true);
    	UserDao.getSharedUserDao().addUser(user);
    	System.out.println(UserDao.getSharedUserDao());
    	Writer writer = response.getWriter();
		writer.write("<html><head><title>User Create</title></head><body>"+user.canCreate()+"</body></html>");
    }
}
