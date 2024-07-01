<%@ page import="ingweb.main.aziendatrasporti.mo.mo.Service" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.mo.Worker" %>
<%@ page import="java.sql.Time" %>
<% //global variables
    var serviceList=(ArrayList<Service>)request.getAttribute("serviceList");
    var workerList=(ArrayList<Worker>)request.getAttribute("workerList");
    var startHour=5;
    var startMinute=0;
    var endHour=18;
    var endMinute=0;
    var timeStep=30;
%>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/generalStyle.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <script>

        </script>
    </head>
    <body>
        <%@ include file="/jsp/admin/welcome.jsp" %>
        <hr/>
        <h1>Lista clienti</h1>
        <table>
            <tr>
                <td colspan="<%= workerList.size()+1 %>">
                    <form name="dateForm">
                        <label for="weekDay">Selezionare il giorno: </label>
                        <input type="date" id="weekDay">
                        <input type="button" id="searchDate" name="searchDate" value="Ricerca servizi">
                    </form>
                </td>
            </tr>
            <tr>
                <td>Orario</td>
                <% for (var worker: workerList) { %><td><%= worker.getName() %></td><% } %>
            </tr>
            <% for (var minute=(startHour*60+startMinute); minute<=(endHour*60+endMinute); minute+=timeStep) { %>
            <tr>
                <td><%=minute/60%>:<%=minute%60%><%=(minute%60)==0 ? "0" : ""%></td>
                <% for (var worker: workerList) { %>
                    <% var isAssigned=false; %>
                    <% var name=""; %>
                    <% for (var service: serviceList) { %>
                        <% if (service.getFirstDriver().equals(worker) && service.getStartTime().equals(Time.valueOf(minute/60+":"+minute%60))) { %>
                            <% isAssigned=true; %>
                            <% name=service.getName(); %>
                        <% } %>
                    <% } %>
                    <td><%= isAssigned ? name : "---"%></td>
                <% } %>
            </tr>
            <% } %>
        </table>
    </body>
</html>