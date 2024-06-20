<%@ page import="ingweb.main.aziendatrasporti.mo.Service" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.Worker" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.Truck" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var contextPath=request.getContextPath();
    var service=(Service)request.getAttribute("service");
    var workerList=(ArrayList<Worker>)request.getAttribute("workerList");
    var truckList=(ArrayList<Truck>)request.getAttribute("truckList");
%>
<html>
    <head>
        <title>Assegnamento servizi</title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/generalStyle.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataForm.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/checkbox.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/select.css">
    </head>
    <body>
        <form name="dataForm" action="<%= contextPath %>/Servizi" method="post">
            <h1>Assegnamento servizio</h1>
            <hr/>
            <table>
                <tr class="firstRow"><td colspan="2">Servizio: <%= service.getName() %></td></tr>
                <tr><td>Data</td><td><%= service.getDate() %></td></tr>
                <tr><td>Cliente</td><td><%= service.getClientCompany() %></td></tr>
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
                <tr>
                    <td><label for="secondWorker">Selezione secondo autista</label></td>
                    <td>
                        <select id="secondWorker" name="secondWorker">
                            <option value="none">-- Selezionare secondo autista -- </option>
                            <% for (var worker: workerList) { %><option value="<%= worker.getCode() %>"><%= worker.display() %></option><% }%>
                        </select>
                    </td>
                </tr>
            </table>
            <div class="styled">
                <input type="button" name="assignService" value="Assegna servizio"/>
                <input type="button" name="backButton" value="Torna alla lista servizi">
            </div>
        </form>
    </body>
</html>
