<%@ page import="ingweb.main.aziendatrasporti.mo.mo.Service" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.mo.Worker" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.mo.Truck" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var service=(Service)request.getAttribute("service");
    var workerList=(ArrayList<Worker>)request.getAttribute("workerList");
    if (workerList==null) workerList=new ArrayList<>();
    var truckList=(ArrayList<Truck>)request.getAttribute("truckList");
    if (truckList==null) truckList=new ArrayList<>();
%>
<%@ include file="/jsp/admin/welcome.jsp" %>
<html>
    <head>
        <title>Assegnamento servizi</title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataForm.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/checkbox.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/select.css">
        <script>
            function submitForm() {

                let firstDriver=document.querySelector("#firstWorker");
                let secondDriver=document.querySelector("#secondWorker");
                let selectedTruck=document.querySelector("#selectedTruck");
                console.log(firstDriver+" - "+secondDriver+" - "+selectedTruck);
                document.dataForm.action.value="AssignmentController.addAssignment";
                if (firstDriver!==null && secondDriver!==null && selectedTruck!==null && firstDriver!==secondDriver) document.dataForm.submit();
            }

            window.addEventListener("load", function() {

                let addButton=document.querySelector("#assignService");
                let refreshButton=document.querySelector("#backButton");

                window.addEventListener("keydown", function(e) { if (e.key==="Enter") submitForm(); });

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="ServiceController.getServices";
                    document.dataForm.submit();
                });

                addButton.addEventListener("click", submitForm);
            });
        </script>
    </head>
    <body>
        <hr/>
        <form name="dataForm" action="<%= request.getContextPath() %>/Servizi" method="post">
            <h1>Assegnamento servizio</h1>
            <hr/>
            <table>
                <tr class="firstRow"><td colspan="2">Servizio: <%= service.getName() %></td></tr>
                <tr><td>Cliente</td><td><%= service.getClientCompany().display() %></td></tr>
                <tr><td>Data</td><td><%= service.getDate() %></td></tr>
                <tr><td>Ora inizio</td><td><%= service.getStartTime() %></td></tr>
                <tr><td>Durata servizio</td><td><%= service.getDuration() %></td></tr>
                <tr>
                    <td><label for="selectedTruck">Selezione mezzo</label></td>
                    <td>
                        <select id="selectedTruck" name="selectedTruck" required>
                            <% for (var truck: truckList) { %><option value="<%= truck.getCode() %>"><%= truck.display() %></option><% }%>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label for="firstWorker">Selezione autista</label></td>
                    <td>
                        <select id="firstWorker" name="firstWorker" required>
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
            <br/>
            <div class="styled">
                <input type="button" id="assignService" name="assignService" value="Assegna servizio"/>
                <input type="button" id="backButton" name="backButton" value="Torna alla lista servizi"/>
            </div>
            <input type="hidden" name="code" value="<%= service.getCode() %>"/>
            <input type="hidden" name="action">
        </form>
    </body>
</html>
