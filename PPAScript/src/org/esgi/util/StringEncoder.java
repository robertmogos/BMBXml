package org.esgi.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringEncoder 
{
	public enum Algorithm
	{
		MD5,
		SHA1
	}
	
	public static String encode(String txt, Algorithm algorithm)
    {
		if(txt != null)
		{
			byte[] uniqueKey = txt.getBytes();
	        byte[] hash = null;
	        try
	        {
	            hash = MessageDigest.getInstance(algorithm.toString()).digest(uniqueKey);
	        }
	        catch (NoSuchAlgorithmException e)
	        {
	        	return null;
	        }
	        StringBuilder hashString = new StringBuilder();
	        for (int i = 0; i < hash.length; i++)
	        {
	            String hex = Integer.toHexString(hash[i]);
	            int length = hex.length();
	            if(length == 1)
	            {
	                hashString.append('0');
	                hashString.append(hex.charAt(0));
	            }
	            else
	                hashString.append(hex.substring(length - 2));
	        }
	        return hashString.toString();
		}
        return null;
    }
}
