/* ----------------------------------------------------------------------- */
/* 5648-C83, 5648-C84 (C) COPYRIGHT International Business Machines Corp., */ 
/*                        1997, 1998, 1999, 2000                           */
/* All rights reserved * Licensed Materials - Property of IBM              */
/* ----------------------------------------------------------------------- */

/**
 * File:             HelloPervasiveServlet.java
 * Date of Creation: 11/01/1999
 *   
 * Description: Sample Hello Servlet to demonstrate request 
 *              processing from any detectable client type
 *              (ie. HTML Client, Speech Client, Wireless Client, etc.)
 *              using the WAS V3.0.2 PageList Support.
 *
 * Notes:
 *   - If the Webserver serving this servlet is not running with Port 80, then 
 *     the HelloXXXX.jsp files will need to be modified to call the correct URL.
 *
 * @author Michael J. Morton (IBM WebSphere Application Server Development)
 * @version 1.0
 */ 

// Imports
import java.io.*;
import java.lang.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.ibm.servlet.*;

public class HelloPervasiveServlet extends PageListServlet implements Serializable
{
	/*
	* doGet -- Process incoming HTTP GET requests
	* 
	* @param request Object that encapsulates the request to the servlet
	* @param response Object that encapsulates the response from the servlet
	*/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		String pageName = "Hello.page";
		String mlName = getMLNameFromRequest(request);

		// First check if the servlet was invoked with a queryString that
		// contained a Markup-Language value. For example:
		//   http://localhost/HelloPervasive?mlname=VXML
		if (mlName == null)
		{
			// If no ML type was provided in the queryString,then attempt to determine 
			// the client type from the Request and use the ML name as configured in the 
			// client_types.xml file.
			mlName = getMLTypeFromRequest(request);
		}

		try
		{
			// Serve the Request page.
			callPage(mlName, pageName, request, response);
		}
		catch (Exception e)
		{
			handleError(mlName, request, response, e);
		}
	}
}
