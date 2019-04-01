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

import java.rmi.RemoteException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;

public abstract class IncrementBean implements EntityBean
{
	private EntityContext entityContext;
 
	public abstract String getPrimaryKey();
	public abstract void setPrimaryKey(String primaryKey);
	public abstract int getTheValue();
	public abstract void setTheValue(int theValue);

	// EJB mandated methods.
	public IncrementKey ejbCreate(String key)
	throws CreateException
	{
		setPrimaryKey(key);
		setTheValue(0);

		return null;
	}

	public void ejbPostCreate(String key)
	throws CreateException
	{
	}

	public void ejbLoad()
	throws RemoteException
	{
	}

	public void ejbStore()
	throws RemoteException
	{
	}

	public void ejbRemove()
	throws RemoveException, RemoteException
	{
	}

	public void ejbActivate()
	throws RemoteException
	{
	}

	public void ejbPassivate()
	throws RemoteException
	{
	}
 
	public void setEntityContext(EntityContext ctx)
	throws RemoteException
	{
		entityContext = ctx;
	}

	public void unsetEntityContext()
	throws RemoteException
	{
		entityContext = null;
	}

	// Increment Bean Methods
	public int increment() 
	{
		int value = getTheValue();
		value++;
		setTheValue(value);

		return value;
	}
}
