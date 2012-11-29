package org.esgi.dao;

import java.io.File;

import org.esgi.model.impl.User;
import org.esgi.model.interfaces.IUser;
import org.esgi.util.BooleanConverter;
import org.esgi.util.StringEncoder;
import org.esgi.util.UserNodeFilter;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

public class UserDao
{
	private static UserDao sharedUserDao_ = null;

	private File file_;
	private DOMImplementationLS impl_;
	private Document root_;

	public static UserDao getSharedUserDao()
	{
		if(UserDao.sharedUserDao_ == null)
			UserDao.sharedUserDao_ = new UserDao();
		return UserDao.sharedUserDao_;
	}

	private UserDao()
	{
		try 
		{
			DOMImplementationRegistry ir = DOMImplementationRegistry.newInstance();
			this.impl_ = (DOMImplementationLS) ir.getDOMImplementation("LS 3.0");
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setFile(File file)
	{
		this.file_ = file;
		LSParser lsp = this.impl_.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
		lsp.getDomConfig().setParameter("validate", true);
		this.root_ = lsp.parseURI(this.getCrossPlatformFilePath());
	}

	public File getFile()
	{
		return this.file_;
	}

	public Boolean addUser(IUser u)
	{
		if(u != null)
		{
			String login = u.getLogin();
			String pwd = u.getPassword();
			if(login != null && pwd != null)
			{
				if(this.nodeForUser(u) == null)
				{
					Node n = this.userToNode(u);
					if(n != null)
					{
						this.root_.getDocumentElement().appendChild(n);
						LSSerializer lss = this.impl_.createLSSerializer();
						System.out.println( this.getFile().toURI().getPath());
						return lss.writeToURI(this.root_, this.getCrossPlatformFilePath());
					}
				}
			}
		}
		return false;
	}
	

	public IUser getUser(String log, String pwd)
	{
		
		DocumentTraversal dot = (DocumentTraversal)this.root_;
		NodeIterator it = dot.createNodeIterator(this.root_.getDocumentElement(),NodeFilter.SHOW_ALL,null,true);
		String secure_pwd = StringEncoder.encode(pwd, StringEncoder.Algorithm.SHA1);
		Node n = null;
		IUser u = null;
		while((n=it.nextNode())!=null)
		{
			if(n.getNodeName().equals("bmb:user"))
			{
				 u = getUserFromNode(n);
				 if(u.getLogin().equals(log) && u.getPassword().equals(secure_pwd))
					 return u;
			}
		}
		return null;
	}
	
	private String getCrossPlatformFilePath()
	{
		String filePath = this.getFile().toURI().getPath();
		return filePath.replace(" ", "%20");
	}

	private Node nodeForUser(IUser u)
	{
		if(u != null)
		{
			DocumentTraversal dt = (DocumentTraversal)this.root_;
			NodeIterator it = dt.createNodeIterator(this.root_.getDocumentElement(), NodeFilter.SHOW_ALL, new UserNodeFilter(u), true);
			return it.nextNode();
		}
		return null;
	}

	private Node userToNode(IUser u)
	{
		if(u != null && this.root_ != null)
		{
			Node rootNode = this.root_.createElement("bmb:user");
			Node loginNode = this.root_.createElement("bmb:login");
			loginNode.appendChild(this.root_.createTextNode(u.getLogin()));
			Node pwdNode = this.root_.createElement("bmb:pwd");
			pwdNode.appendChild(this.root_.createTextNode(StringEncoder.encode(u.getPassword(), StringEncoder.Algorithm.SHA1)));
			Node rulesNode = this.root_.createElement("bmb:rules");
			rulesNode.appendChild(this.root_.createTextNode(u.getStringRules()));
			rootNode.appendChild(loginNode);
			rootNode.appendChild(pwdNode);
			rootNode.appendChild(rulesNode);
			return rootNode;
		}
		return null;
	}
	
	private IUser getUserFromNode(Node n)
	{
		if(n != null)
		{
			User u = new User();
			NodeList nl = n.getChildNodes();
			for(int i=0 ; i<nl.getLength(); i++)
			{
				Node childNode = nl.item(i);
				if(childNode.getNodeName().equals("bmb:rules"))
				{
					char[] rules = childNode.getFirstChild().getNodeValue().toCharArray();
					u.setIsSuper(BooleanConverter.booleanValue(rules[0]));
					u.setCanCreate(BooleanConverter.booleanValue(rules[1]));
					u.setCanModify(BooleanConverter.booleanValue(rules[2]));
					u.setCanRead(BooleanConverter.booleanValue(rules[3]));
				}
				else if(childNode.getNodeName().equals("bmb:login"))
				{
					u.setLogin(childNode.getFirstChild().getNodeValue());
				}
				else if(childNode.getNodeName().equals("bmb:pwd"))
				{
					u.setPassword(childNode.getFirstChild().getNodeValue());
				}
			}
			return u;
		}
		return null;
	}
	
	@Override
	public String toString()
	{
		DocumentTraversal dot = (DocumentTraversal)this.root_;
		NodeIterator it = dot.createNodeIterator(this.root_.getDocumentElement(),NodeFilter.SHOW_ALL,null,true);
		Node n = null;
		StringBuilder sb = new StringBuilder();
		sb.append("UserDao (\n");
		while((n=it.nextNode())!=null)
		{
			if(n.getNodeName().equals("bmb:user"))
				sb.append("\t"+getUserFromNode(n)+"\n");
		}
		sb.append(")");
		return sb.toString();
	}
}
