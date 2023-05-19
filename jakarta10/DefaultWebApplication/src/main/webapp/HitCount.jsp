<%@page session="false"%>
<HTML>
<HEAD><TITLE>IBM WebSphere Hit Count Demonstration</TITLE></H1>
<SCRIPT TYPE="text/javascript">
  function enableLookupButtons() {
    var myButtons = document.getElementsByName("lookup");
      for (i = 0; i < myButtons.length; i++){
         myButtons[i].disabled = false;
      }
  }

  function disableLookupButtons() {
      var myButtons = document.getElementsByName("lookup");
      for (i = 0; i < myButtons.length; i++){
         myButtons[i].disabled = true;
      }
  }

  function enableTransButtons() {
    var myButtons = document.getElementsByName("trans");
      for (i = 0; i < myButtons.length; i++){
         myButtons[i].disabled = false;
      }
  }

  function disableTransButtons() {
      var myButtons = document.getElementsByName("trans");
      for (i = 0; i < myButtons.length; i++){
         myButtons[i].disabled = true;
      }
  }
</SCRIPT>
<BODY bgcolor="cornsilk">
<H1>Hit Count Demonstration</H1>
<P>
<B>
This simple demonstration provides a variety of methods to increment a counter value.
</B>
<FORM METHOD=GET ACTION="hitcount">
</BR>
<B>Select a method of execution:</B>
</BR>
<%
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires",0);
    String selection = (String) request.getAttribute("selection");
    if (selection == null) selection = "";
    String lookup = (String) request.getAttribute("lookup");
    if (lookup == null) lookup = "";
    String msg = (String) request.getAttribute("msg");
    if (msg == null) msg = "";
    String trans = (String) request.getAttribute("trans");
    if (trans == null) trans = "";
    String transMsg = (String) request.getAttribute("transMsg");
    if (transMsg == null) transMsg = "";
%>
<INPUT TYPE=RADIO NAME=selection VALUE=SRV
<%= selection.equals("SRV") ? " CHECKED" : "" %> onClick="if (this.checked) {disableLookupButtons();disableTransButtons()}">Servlet instance variable</BR>
<INPUT TYPE=RADIO NAME=selection VALUE=SS2
<%= selection.equals("SS2") ? " CHECKED" : "" %> onClick="if (this.checked) {disableLookupButtons();disableTransButtons()}">Session state (create if necessary)</BR>
<INPUT TYPE=RADIO NAME=selection VALUE=SS1
<%= selection.equals("SS1") ? " CHECKED" : "" %> onClick="if (this.checked) {disableLookupButtons();disableTransButtons()}">Existing session state only</BR>
<INPUT TYPE=RADIO NAME=selection VALUE=EJB
<%= selection.equals("EJB") ? " CHECKED" : "" %> onClick="if (this.checked) {enableLookupButtons();enableTransButtons()}">Enterprise Java Bean (JPA)</BR>
</BR><B>Transaction type:</B></BR>
<INPUT TYPE=RADIO NAME=trans VALUE=NTX
<%= trans.equals("NTX") ? " CHECKED" : "" %>>None
<INPUT TYPE=RADIO NAME=trans VALUE=CMT
<%= trans.equals("CMT") ? " CHECKED" : "" %>>Commit
<INPUT TYPE=RADIO NAME=trans VALUE=RLB
<%= trans.equals("RLB") ? " CHECKED" : "" %>>Rollback
</BR></BR>
<INPUT TYPE=SUBMIT VALUE="Increment">
</FORM>
<H3><%=msg%></H3>
<H3><%=transMsg%></H3>
</INSERT>
</BODY>
</HTML>
