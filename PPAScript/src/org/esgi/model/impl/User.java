package org.esgi.model.impl;

import org.esgi.model.interfaces.IUser;

public class User implements IUser
{
	private String login_;
	private String pwd_;
	private Byte[] rules_;

	public User() 
	{
		rules_ = new Byte[4];
	}
	
	@Override
	public String getLogin() 
	{
		return this.login_;
	}

	@Override
	public void setLogin(String login) 
	{
		this.login_ = login;
	}

	@Override
	public String getPassword()
	{
		return this.pwd_;	
	}

	@Override
	public void setPassword(String password)
	{
		this.pwd_ = password;
	}

	@Override
	public Boolean isSuper() 
	{
		Byte b = this.rules_[0];
		if(b != null && b == 1)
			return true;
		return false;
	}

	@Override
	public void setIsSuper(Boolean value) 
	{
		if(value == true)
			this.rules_[0] = 1;
		else
			this.rules_[0] = 0;
	}

	@Override
	public Boolean canCreate()
	{
		Byte b = this.rules_[1];
		if(b != null && b == 1)
			return true;
		return false;
	}

	@Override
	public void setCanCreate(Boolean value) 
	{
		if(value == true)
			this.rules_[1] = 1;
		else
			this.rules_[1] = 0;	
	}

	@Override
	public Boolean canModify() 
	{
		Byte b = this.rules_[2];
		if(b != null && b == 1)
			return true;
		return false;
	}

	@Override
	public void setCanModify(Boolean value)
	{
		if(value == true)
			this.rules_[2] = 1;
		else
			this.rules_[2] = 0;
	}

	@Override
	public Boolean canRead() 
	{
		Byte b = this.rules_[3];
		if(b != null && b == 1)
			return true;
		return false;
	}

	@Override
	public void setCanRead(Boolean value) 
	{
		if(value == true)
			this.rules_[3] = 1;
		else
			this.rules_[3] = 0;
	}
	
	public String getStringRules()
	{
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<rules_.length; i++)
		{
			Byte b = rules_[i];
			sb.append((b!=null)?b:0);
		}
		return sb.toString();
	}
	
	@Override
	public String toString()
	{
		return "{ login : "+this.login_+", pwd : "+this.pwd_+", rules : "+this.getStringRules()+" }";
	}
}
