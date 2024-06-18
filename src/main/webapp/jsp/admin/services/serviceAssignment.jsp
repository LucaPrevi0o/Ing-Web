<%@ page import="ingweb.main.aziendatrasporti.mo.Service" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.Worker" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.Truck" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var service=(Service)request.getAttribute("serviceList");
    var workerList=(ArrayList<Worker>)request.getAttribute("workerList");
    var truckList=(ArrayList<Truck>)request.getAttribute("truckList");
%>
<html>
    <head>
        <title>Assegnamento servizi</title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/generalStyle.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/checkbox.css">
    </head>
    <body>
        <table>
            <tr class="firstRow"><td colspan="2"><%= service.getName() %></td></tr>
            <tr>
                <td><label for="selectedTruck">Selezione mezzo</label></td>
                <td>
                    <select id="selectedTruck" name="selectedTruck">
                        <% for (var truck: truckList) { %><option value="<%= truck.getCode() %>"><%= truck.display() %></option><% }%>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="selectedWorker">Selezione autista</label></td>
                <td>
                    <select id="selectedWorker" name="selectedWorker">
                        <% for (var worker: workerList) { %><option value="<%= worker.getCode() %>"><%= worker.display() %></option><% }%>
                    </select>
                </td>
            </tr>
        </table>
    </body>
</html>
