//
// 5630-A23, 5630-A22, (C) Copyright IBM Corporation, 1997, 2001, 2002
// All rights reserved. Licensed Materials Property of IBM
// Note to US Government users: Documentation related to restricted rights
// Use, duplication or disclosure is subject to restrictions set forth in GSA ADP Schedule with IBM Corp.
// This page may contain other proprietary notices and copyright information, the terms of which must be observed and followed.
//
//
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.ejb.FinderException;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.transaction.UserTransaction;

import com.ibm.defaultapplication.*;

/**
 * This servlet demonstrates various methods that can be used to increment a counter. The methods are: 
 *   Servlet instance variable, or
 *   Session object, or
 *   CMP EJB
 */
public class HitCount extends HttpServlet
{
	private Increment inc = null;
	private int count = 0;
	private InitialContext ic = null;

	private String currentEJBLookup = "GBL";

	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);

		// Create InitialContext.
		try
		{
			ic = new InitialContext();
		}
		catch (Exception e)
		{
			throw new ServletException(e);
		}
	}

	public void service (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		PrintWriter  out;
		String[]     p;
		String       msg = null;
		String       selection = "";
		String       lookup = "";
		String       trans = "NTX";
		String       transMsg = "";
		String       getIncMsgs = null;
		UserTransaction tx = null;
		boolean error = false;

		int ejbCount = 0;

		// Create a transaction if specified.
		p = req.getParameterValues("trans");
		if (p != null)
		{
			trans = p[0];
		}

		if (trans.equals("CMT") ||trans.equals("RLB"))
		{
			try
			{
				tx = (UserTransaction)ic.lookup("java:comp/UserTransaction");
			}
			catch (Exception e)
			{
				tx = null;
				error = true;

				transMsg = "Failed to find UserTransaction - Operation performed was not transactional!";
				System.out.println("Hit Count: " + transMsg);
				e.printStackTrace();
			}

			if (tx != null)
			{
				try
				{
					tx.begin();
				}
				catch (Exception e)
				{
					tx = null;
					error = true;

					transMsg = "Failed to begin transaction - Operation performed was not transactional!";
					System.out.println("Hit Count: " + transMsg);
					e.printStackTrace();
				}
			}

			if (!error)
			{
				if (trans.equals("CMT"))
				{
					transMsg = "Transaction: Commit is selected.";
				}
				else if (trans.equals("RLB"))
				{
					transMsg = "Transaction: Rollback is selected.";
				}
			}
		}
		else
		{
			transMsg = "";
		}

		// What shall we do now?
		p = req.getParameterValues("selection"); 
		if (p != null)
		{
			selection = p[0];
		}

		if (selection.length() == 0)
		{
			msg= "Please select a method of execution above.";
		}
		else if (selection.equals("SRV"))
		{
			// Increment the servlet variable.
			count++;
			msg = "Hit Count value for (servlet instance): " + count;
		}
		else if (selection.equals("SS1"))
		{
			// Increment only if the session state exists.
			HttpSession session = req.getSession(false);
			if (session == null)
			{
				msg= "Information: A new session must be created first for this selection!";
			}
			else
			{
				Integer value = (Integer)session.getAttribute("count");
				if (value == null)
				{
					msg = "Error - Session exists, but count key was not found!";
				}
				else
				{
					value = new Integer(value.intValue() + 1);
					session.setAttribute("count", value);
					msg = "Hit Count value for (existing session): " + value;
				}
			}
		}
		else if (selection.equals("SS2"))
		{
			// Increment session state, creating if necessary.
			HttpSession session = req.getSession(true);
			if (session == null)
			{
				msg = "Error - Create of new session failed!";
			}

			else
			{
				Integer value = (Integer)session.getAttribute("count");
				if ( value == null )
				{
					value = new Integer(1);
					msg = "Hit Count value for (new session): 1";
				}
				else
				{
					value = new Integer(value.intValue() + 1);
					msg = "Hit Count value for (existing session): " + value;
				}
				session.setAttribute("count", value);
			}
		}
		else if (selection.equals("EJB"))
		{
			p = req.getParameterValues("lookup");
			if (p != null)
			{
				lookup = p[0];
			}
			else
			{
				lookup = currentEJBLookup;
			}

			ejbCount = 0;
			if ((inc == null) || (!lookup.equals(currentEJBLookup)))
			{
				currentEJBLookup = lookup;
				getIncMsgs = getInc(currentEJBLookup);
			}

			if (inc == null)
			{
				msg = getIncMsgs;
			}
			else
			{
				try
				{
					ejbCount = inc.increment();
				}
				catch (Exception e)
				{
					inc = null;

					msg = "Error - increment method on Increment EJB failed!";
					System.out.println("Hit Count: " + msg);
					e.printStackTrace();
				}

				if (inc != null)
				{
					msg = "Hit Count value for (Increment EJB): " + ejbCount;
				}
			}
		}

		if (tx != null)
		{
			if (trans.equals("CMT"))
			{
				try
				{
					tx.commit();
					transMsg = "Transaction Commit completed.";
				}
				catch (Exception e)
				{
					tx = null;

					transMsg = "Transaction Commit failed!";
					System.out.println("Hit Count: " + transMsg);
					e.printStackTrace();
				}
			}
			else
			{
				try
				{
					tx.rollback();
					transMsg = "Transaction: Rollback completed.";
				}
				catch (Exception e)
				{
					tx = null;

					transMsg = "Transaction Rollback failed!";
					System.out.println("Hit Count: " + transMsg);
					e.printStackTrace();
				}
			}

			try
			{
				// Reflect the real value in the database.
				if (selection.equals("EJB"))
				{
					msg = "Hit Count value for (Increment EJB): " + inc.getTheValue();
				}
			}
			catch (Exception e)
			{
				inc = null;

				msg = "Error - getValue method on Increment EJB failed!";
				System.out.println("Hit Count: " + msg);
				e.printStackTrace();
			}
		}

		// Set value and call page the JSP.
		req.setAttribute("msg", msg);
		req.setAttribute("transMsg", transMsg);
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/HitCount.jsp");
		rd.forward(req, res);
	}

	synchronized private String getInc(String namespace)
	{
		Object homeObject = null;
		int countFailure = 0;
		String failureMsg = null;

		IncrementHome incHome = null;

		try
		{
			String lookupString = null;

			if (namespace.equals("LCL"))
			{
				lookupString = "java:comp/env/Increment";
			}
			else
			{
				lookupString = "Increment";
			}

			homeObject = ic.lookup(lookupString);
		}
		catch (Exception e)
		{
			countFailure = 1;

			failureMsg = "Error - Home interface for Increment EJB could not be found!";
			System.out.println("Hit Count: " + failureMsg);
			e.printStackTrace();
		}

		incHome = (IncrementHome)javax.rmi.PortableRemoteObject.narrow(homeObject, IncrementHome.class);
		String incKeyString = "HitCountKey";

		if (countFailure == 0)
		{
			try
			{
				IncrementKey incKey = new IncrementKey(incKeyString);
				inc = incHome.findByPrimaryKey(incKey);
			}
			catch (FinderException e)
			{
				countFailure = 2;
			}
			catch (Exception e)
			{
				countFailure = 3;

				failureMsg = "Error - Finder for Increment EJB failed!";
				System.out.println("Hit Count: " + failureMsg);
				e.printStackTrace();
			}
		}

		if (countFailure == 2)
		{
			try
			{
				inc = incHome.create(incKeyString);
			}
			catch (Exception e)
			{
				failureMsg = "Error - Create for Increment EJB failed!";
				System.out.println("Hit Count: " + failureMsg);
				e.printStackTrace();
			}
		}

		return failureMsg;
	}
}