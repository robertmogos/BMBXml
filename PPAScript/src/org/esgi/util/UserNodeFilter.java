package org.esgi.util;

import org.esgi.model.interfaces.IUser;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeFilter;

public class UserNodeFilter implements NodeFilter
{
	private IUser user_;
	
	public UserNodeFilter(IUser u) 
	{
		this.user_ = u;
	}
	
	public short acceptNode(Node node) 
	{
		String login = this.user_.getLogin();
		if(node.getNodeName().equals("bmb:user"))
		{
			NodeList userNodes = node.getChildNodes();
			if(userNodes != null)
			{
				for(int i=0; i<userNodes.getLength(); i++)
				{
					Node nodeLogin = userNodes.item(i);
					if(nodeLogin.getNodeName().equals("bmb:login"))
					{
						Node nodeText = nodeLogin.getFirstChild();
						if(nodeText != null)
						{
							String nodeValue = nodeText.getNodeValue();
							if(nodeValue != null)
								if(nodeValue.equals(login))
									return NodeFilter.FILTER_ACCEPT;
						}
					}					
				}
				
			}			
		}
		return NodeFilter.FILTER_SKIP;
	}

}
