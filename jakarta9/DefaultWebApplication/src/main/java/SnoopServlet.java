/*
 * @(#)SnoopServlet.java        1.00 99/03/15
 *
 * Copyright (c) 1999, 20022 International Business Machines. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of IBM.
 * You shall not disclose such Confidential Information and shall use it
 * only in accordance with the terms of the license agreement you entered
 * into with IBM.
 *
 * IBM MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. IBM SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 * CopyrightVersion 1.0
 *
 *
 */

import java.io.*;
import java.util.*;
import java.security.cert.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;


/**
 * Snoop Servlet returns information about the request. This servlet is
 * useful for checking the request parameters from a particular client.
 * SnoopServlet also returns information of existing sessions, application
 * attributes, and request attributes.
 *
 * @version     1.0
 */

@WebServlet(name="Snoop Servlet",
            description="This servlet returns information about the request which includes information about  existing sessions, application attributes, and request attributes.",
            urlPatterns="/snoop/*")
public class SnoopServlet extends HttpServlet
{
        public void doGet (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
        {
                PrintWriter out;

                res.setContentType("text/html");
                out = res.getWriter ();

                out.println("<HTML><HEAD><TITLE>Snoop Servlet</TITLE></HEAD><BODY BGCOLOR=\"#FFFFEE\">");
                out.println("<h1>Snoop Servlet - Request/Client Information</h1>");
                out.println("<h2>Requested URL:</h2>");
                out.println("<TABLE Border=\"2\" WIDTH=\"65%\" BGCOLOR=\"#DDDDFF\">");
                out.println("<tr><td>" + escapeChar(HttpUtils.getRequestURL(req).toString()) + "</td></tr></table><BR><BR>");
                // the following update is needed soon with Jakarta 10.
                // out.println("<tr><td>" + escapeChar(req.getRequestURL().toString()) + "</td></tr></table><BR><BR>");

                out.println("<h2>Servlet Name:</h2>");
                out.println("<TABLE Border=\"2\" WIDTH=\"65%\" BGCOLOR=\"#DDDDFF\">");
                out.println("<tr><td>" + escapeChar(getServletConfig().getServletName()) + "</td></tr></table><BR><BR>");

                Enumeration vEnum = getServletConfig().getInitParameterNames();
                if ( vEnum != null && vEnum.hasMoreElements() )
                {
                        boolean first = true;
                        while ( vEnum.hasMoreElements() )
                        {
                                if ( first )
                                {
                                        out.println("<h2>Servlet Initialization Parameters</h2>");
                                        out.println("<TABLE Border=\"2\" WIDTH=\"65%\" BGCOLOR=\"#DDDDFF\">");
                                        first = false;
                                }
                                String param = (String) vEnum.nextElement();
                                out.println("<tr><td>" + escapeChar(param) + "</td><td>" + escapeChar(getInitParameter(param)) + "</td></tr>");
                        }
                        out.println("</table><BR><BR>");
                }

                vEnum = getServletConfig().getServletContext().getInitParameterNames();
                if ( vEnum != null && vEnum.hasMoreElements() )
                {
                        boolean first = true;
                        while ( vEnum.hasMoreElements() )
                        {
                                if ( first )
                                {
                                        out.println("<h2>Servlet Context Initialization Parameters</h2>");
                                        out.println("<TABLE Border=\"2\" WIDTH=\"65%\" BGCOLOR=\"#DDDDFF\">");
                                        first = false;
                                }
                                String param = (String) vEnum.nextElement();
                                out.println("<tr><td>" + escapeChar(param) + "</td><td>" + escapeChar(getServletConfig().getServletContext().getInitParameter(param)) + "</td></tr>");
                        }
                        out.println("</table><BR><BR>");
                }

                out.println("<h2>Request Information:</h2>");
                out.println("<TABLE Border=\"2\" WIDTH=\"65%\" BGCOLOR=\"#DDDDFF\">");
                print(out, "Request method", req.getMethod());
                print(out, "Request URI", req.getRequestURI());
                print(out, "Request protocol", req.getProtocol());
                print(out, "Servlet path", req.getServletPath());
                print(out, "Path info", req.getPathInfo());
                print(out, "Path translated", req.getPathTranslated());
                print(out, "Character encoding", req.getCharacterEncoding());
                print(out, "Query string", req.getQueryString());
                print(out, "Content length", req.getContentLength());
                print(out, "Content type", req.getContentType());
                print(out, "Server name", req.getServerName());
                print(out, "Server port", req.getServerPort());
                print(out, "Remote user", req.getRemoteUser());
                print(out, "Remote address", req.getRemoteAddr());
                print(out, "Remote host", req.getRemoteHost());
                print(out, "Remote port", req.getRemotePort());
                print(out, "Local address", req.getLocalAddr());
                print(out, "Local host", req.getLocalName());
                print(out, "Local port", req.getLocalPort());
                print(out, "Authorization scheme", req.getAuthType());
                if (req.getLocale() != null)
                {
                        print(out, "Preferred Client Locale", req.getLocale().toString());
                }
                else
                {
                        print(out, "Preferred Client Locale", "none");
                }
                Enumeration ee = req.getLocales();
                while (ee.hasMoreElements())
                {
                        Locale cLocale = (Locale)ee.nextElement();
                        if (cLocale != null)
                        {
                                print(out, "All Client Locales", cLocale.toString());
                        }
                        else
                        {
                                print(out, "All Client Locales", "none");
                        }
                }
                print(out, "Context Path", escapeChar(req.getContextPath()));
                if (req.getUserPrincipal() != null)
                {
                        print(out, "User Principal", escapeChar(req.getUserPrincipal().getName()));
                }
                else
                {
                        print(out, "User Principal", "none");
                }
                out.println("</table><BR><BR>");

                Enumeration e = req.getHeaderNames();
                if ( e.hasMoreElements() )
                {
                        out.println("<h2>Request headers:</h2>");
                        out.println("<TABLE Border=\"2\" WIDTH=\"65%\" BGCOLOR=\"#DDDDFF\">");
                        while ( e.hasMoreElements() )
                        {
                                String name = (String)e.nextElement();
                                out.println("<tr><td>" + escapeChar(name) + "</td><td>" + escapeChar(req.getHeader(name)) + "</td></tr>");
                        }
                        out.println("</table><BR><BR>");
                }

                e = req.getParameterNames();
                if ( e.hasMoreElements() )
                {
                        out.println("<h2>Servlet parameters (Single Value style):</h2>");
                        out.println("<TABLE Border=\"2\" WIDTH=\"65%\" BGCOLOR=\"#DDDDFF\">");
                        while ( e.hasMoreElements() )
                        {
                                String name = (String)e.nextElement();
                                out.println("<tr><td>" + escapeChar(name) + "</td><td>" + escapeChar(req.getParameter(name)) + "</td></tr>");
                        }
                        out.println("</table><BR><BR>");
                }

                e = req.getParameterNames();
                if ( e.hasMoreElements() )
                {
                        out.println("<h2>Servlet parameters (Multiple Value style):</h2>");
                        out.println("<TABLE Border=\"2\" WIDTH=\"65%\" BGCOLOR=\"#DDDDFF\">");
                        while ( e.hasMoreElements() )
                        {
                                String name = (String)e.nextElement();
                                String vals[] = (String []) req.getParameterValues(name);
                                if ( vals != null )
                                {

                                    out.print("<tr><td>" + escapeChar(name) + "</td><td>");
                                    out.print(escapeChar(vals[0]));
                                    for ( int i = 1; i<vals.length; i++ )
                                        out.print(", " + escapeChar(vals[i]));
                                    out.println("</td></tr>");
                                }
                        }
                        out.println("</table><BR><BR>");
                }

                String  cipherSuite = (String)req.getAttribute ("javax.net.ssl.cipher_suite");
                if ( cipherSuite != null )
                {
                        X509Certificate certChain [] = (X509Certificate [])req.getAttribute ("javax.net.ssl.peer_certificates");

                        out.println ("<h2>HTTPS Information:</h2>");
                        out.println("<TABLE Border=\"2\" WIDTH=\"65%\" BGCOLOR=\"#DDDDFF\">");
                        out.println ("<tr><td>Cipher Suite</td><td>" + escapeChar(cipherSuite) + "</td></tr>");

                        if ( certChain != null )
                        {
                                for ( int i = 0; i < certChain.length; i++ )
                                {
                                        out.println ("client cert chain [" + i + "] = " + escapeChar(certChain [i].toString ()));
                                }
                        }
                        out.println("</table><BR><BR>");
                }

                Cookie[] cookies = req.getCookies();
                if ( cookies != null && cookies.length > 0 )
                {
                        out.println("<H2>Client cookies</H2>");
                        out.println("<TABLE Border=\"2\" WIDTH=\"65%\" BGCOLOR=\"#DDDDFF\">");
                        for ( int i=0; i<cookies.length; i++ )
                        {
                                out.println("<tr><td>" + escapeChar(cookies[i].getName()) + "</td><td>" + escapeChar(cookies[i].getValue()) + "</td></tr>");
                        }
                        out.println("</table><BR><BR>");
                }

                e = req.getAttributeNames();
                if ( e.hasMoreElements() )
                {
                        out.println("<h2>Request attributes:</h2>");
                        out.println("<TABLE Border=\"2\" WIDTH=\"65%\" BGCOLOR=\"#DDDDFF\">");
                        while ( e.hasMoreElements() )
                        {
                                String name = (String)e.nextElement();
				out.println("<tr><td>" + escapeChar(name) + "</td><td>" + escapeChar(req.getAttribute(name).toString()) + "</td></tr>");
                        }
                        out.println("</table><BR><BR>");
                }

                e = getServletContext().getAttributeNames();
                if ( e.hasMoreElements() )
                {
                        out.println("<h2>ServletContext attributes:</h2>");
                        out.println("<TABLE Border=\"2\" WIDTH=\"65%\" BGCOLOR=\"#DDDDFF\">");
                        while ( e.hasMoreElements() )
                        {
                                String name = (String)e.nextElement();
                                out.println("<tr><td>" + escapeChar(name) + "</td><td>" + escapeChar(getServletContext().getAttribute(name).toString()) + "</td></tr>");
                        }
                        out.println("</table><BR><BR>");
                }

                HttpSession session = req.getSession(false);
                if ( session != null )
                {
                        out.println("<h2>Session information:</h2>");
                        out.println("<TABLE Border=\"2\" WIDTH=\"65%\" BGCOLOR=\"#DDDDFF\">");
                        print(out, "Session ID", session.getId());
                        print(out, "Last accessed time", new Date(session.getLastAccessedTime()).toString());
                        print(out, "Creation time", new Date(session.getCreationTime()).toString());
                        String mechanism = "unknown";
                        if ( req.isRequestedSessionIdFromCookie() )
                        {
                                mechanism = "cookie";
                        }
                        else if ( req.isRequestedSessionIdFromURL() )
                        {
                                mechanism = "url-encoding";
                        }
                        print(out, "Session-tracking mechanism", mechanism);
                        out.println("</table><BR><BR>");

                        Enumeration vals = session.getAttributeNames();
                        if (vals.hasMoreElements())
                        {
                                out.println("<h2>Session values</h2>");
                                out.println("<TABLE Border=\"2\" WIDTH=\"65%\" BGCOLOR=\"#DDDDFF\">");

                                while (vals.hasMoreElements())
                                {
                                        String name = (String)vals.nextElement();
                                        out.println("<tr><td>" + escapeChar(name) + "</td><td>" + escapeChar(session.getAttribute(name).toString()) + "</td></tr>");
                                }
                                out.println("</table><BR><BR>");
                        }
                }

                out.println("</body></html>");
        }

        private void print (PrintWriter out, String name, String value)
        {
		out.println("<tr><td>" + name + "</td><td>" + (value == null ? "&lt;none&gt;" : escapeChar(value)) + "</td></tr>");
        }

        private void print (PrintWriter out, String name, int value)
        {
                out.print("<tr><td>" + name + "</td><td>");
                if ( value == -1 )
                {
                        out.print("&lt;none&gt;");
                }
                else
                {
                        out.print(value);
                }
                out.println("</td></tr>");
        }


        private String escapeChar(String str) {
            if (str == null) {
              return "";
            }
            char src[] = str.toCharArray();
            int len = src.length;
            for (int i = 0; i < src.length; i++) {
                switch (src[i]) {
                    case '<':    // to "&lt;"
                        len += 3;
                        break;
                    case '>':    // to "&gt;"
                        len += 3;
                        break;
                    case '&':    // to "&amp;"
                        len += 4;
                        break;
                }
            }
            char ret[] = new char[len];
            int j = 0;
            for (int i = 0; i < src.length; i++) {
                switch (src[i]) {
                    case '<':    // to "&lt;"
                        ret[j++] = '&';
                        ret[j++] = 'l';
                        ret[j++] = 't';
                        ret[j++] = ';';
                        break;
                    case '>':    // to "&gt;"
                        ret[j++] = '&';
                        ret[j++] = 'g';
                        ret[j++] = 't';
                        ret[j++] = ';';
                        break;
                    case '&':    // to "&amp;"
                        ret[j++] = '&';
                        ret[j++] = 'a';
                        ret[j++] = 'm';
                        ret[j++] = 'p';
                        ret[j++] = ';';
                        break;
                    default:
                        ret[j++] = src[i];
                        break;
                }
            }
            return new String(ret);
        }
}
