package org.esgi.util;

public class BooleanConverter 
{
	public static Boolean booleanValue(char c)
	{
		return (c == '0')?false:true;
	}
}
