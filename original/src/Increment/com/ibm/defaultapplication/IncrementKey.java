//**********************************************************************
//
// IBM Confidential OCO Source Material
// 5639-D57 (C) COPYRIGHT International Business Machines Corp. 2002
// The source code for this program is not published or otherwise divested
// of its trade secrets, irrespective of what has been deposited with the
// U.S. Copyright Office.
//
// Author: Michael J. Morton
//
//**********************************************************************
package com.ibm.defaultapplication;

import java.io.Serializable;

public class IncrementKey implements Serializable
{
	public String primaryKey;

	public IncrementKey()
	{
	}

	public IncrementKey(String s)
	{
		primaryKey = s;
	}

	public String getPrimaryKey()
	{
		return primaryKey;
	}

	public int hashCode()
	{
		return primaryKey.hashCode();
	}

	public boolean equals(Object o)
	{
		if (o instanceof IncrementKey)
			return primaryKey.equals(((IncrementKey)o).primaryKey);
		else
			return false;
	}
}
