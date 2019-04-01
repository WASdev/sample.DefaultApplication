//
// 5630-A23, 5630-A22, (C) Copyright IBM Corporation, 1997, 2019
// All rights reserved. Licensed Materials Property of IBM
// Note to US Government users: Documentation related to restricted rights
// Use, duplication or disclosure is subject to restrictions set forth in GSA ADP Schedule with IBM Corp.
// This page may contain other proprietary notices and copyright information, the terms of which must be observed and followed.
//
//
import java.io.*;
import javax.annotation.*;
import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.transaction.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.ibm.defaultapplication.IncrementSSB;

/**
 * This servlet demonstrates various methods that can be used to increment a counter. The methods are:
 *   Servlet instance variable, or
 *   Session object, or
 *   JPA
 */

@WebServlet(name="Hit Count Servlet", 
            description="This servlet demonstrates the various ways to increment a counter. The methods used are: Servlet instance variable, Session object, and JPA.", 
            urlPatterns="/hitcount")
public class HitCount extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private int count = 0;
    
 	@Resource
 	private UserTransaction tx;
    
	@EJB
	private IncrementSSB inc;
	
    public void service (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        String       msg = "";
        String       selection = req.getParameter("selection");
        String       lookup = req.getParameter("lookup");
        String       trans = req.getParameter("trans");
        String       transMsg = "";

        try
        {
            if ("CMT".equals(trans) || "RLB".equals(trans)) {
              tx.begin();
            }
            if (selection == null || selection.length() == 0) {
                msg= "Please select a method of execution above.";
            } else if (selection.equals("SRV")) {
                // Increment the servlet variable.
                count++;
                msg = "Hit Count value for (servlet instance): " + count;
            } else if (selection.equals("SS1")) {
                msg = incrementSession(req, false);
            } else if (selection.equals("SS2")) {
                msg = incrementSession(req, true);
            } else if (selection.equals("EJB")) {
                msg = incrementEJB(lookup);
            }
        } catch (Exception e) {
            System.out.println("Hit Count: Failed to begin transaction - Operation performed was not transactional!");
            e.printStackTrace();
        } finally {
          try {
              if ("RLB".equals(trans)) {
            	  tx.rollback();
            	  transMsg = "Transaction Rollback completed.";
              } else if ("CMT".equals(trans)){
                  tx.commit();
                  transMsg = "Transaction Commit completed.";
              }
              
          } catch (Exception e) {
              transMsg = "Transaction " + (("RLB".equals(trans)) ? "Rollback" : "Commit") + " failed!";
              System.out.println("Hit Count: " + transMsg);
              e.printStackTrace();
          }
        }

        if ("EJB".equals(selection)) {
          msg = getValueEJB(lookup);
        }

        // Set value and call page the JSP.
        req.setAttribute("msg", msg);
        req.setAttribute("transMsg", transMsg);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/HitCount.jsp");
        rd.forward(req, res);
    }

    private String incrementSession(HttpServletRequest req, boolean create) {
        String msg = "";
        // Increment session state, creating if necessary.
        HttpSession session = req.getSession(create);
        if (session == null)
        {
            if (create) {
                msg = "Error - Create of new session failed!";
            } else {
                msg = "Information: A new session must be created first for this selection!";
            }
        } else {
            Integer value = (Integer)session.getAttribute("count");
            if ( value == null)
            {
                if (create) {
                  value = new Integer(1);
                  msg = "Hit Count value for (new session): 1";
                } else {
                    msg = "Error - Session exists, but count key was not found!";
                }
            }
            else
            {
                value = new Integer(value.intValue() + 1);
                msg = "Hit Count value for (existing session): " + value;
            }
            session.setAttribute("count", value);
        }
        return msg;
    }

    private String getValueEJB(String lookup) {
      String       msg = "";
      try {
          // Reflect the real value in the database.
          msg = "Hit Count value for (Increment EJB): " + inc.getTheValue();
      } catch (Exception e) {
          msg = "Error - getValue method on Increment EJB failed!";
          System.out.println("Hit Count: " + msg);
          e.printStackTrace();
      }
      return msg;
    }

    private String incrementEJB(String lookup) {
    	String msg = "";
    	int ejbCount = -1;

        try {
            ejbCount = inc.increment();
            msg = "Hit Count value for (Increment EJB): " + ejbCount;
        } catch (Exception e) {

            msg = "Error - increment method on Increment EJB failed!";
            System.out.println("Hit Count: " + msg);
            e.printStackTrace();
        }

        return msg;
    }
}
