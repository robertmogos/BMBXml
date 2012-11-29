package org.esgi.model.interfaces;

public interface IUser 
{
	public String getLogin();
	public void setLogin(String login);
	
	public String getPassword();
	public void setPassword(String password);
	
	public Boolean isSuper();
	public void setIsSuper(Boolean value);
	
	public Boolean canCreate();
	public void setCanCreate(Boolean value);
	
	public Boolean canModify();
	public void setCanModify(Boolean value);

	public Boolean canRead();
	public void setCanRead(Boolean value);
	
	public String getStringRules();
}
