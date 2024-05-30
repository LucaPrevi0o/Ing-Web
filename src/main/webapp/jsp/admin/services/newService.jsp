<%@ page import="ingweb.main.aziendatrasporti.mo.Worker" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.Truck" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.ClientCompany" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.License" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var contextPath=request.getContextPath();
    var truckList=(ArrayList<Truck>)request.getAttribute("truckList");
    if (truckList==null) truckList=new ArrayList<>();
    var workerList=(ArrayList<Worker>)request.getAttribute("workerList");
    if (workerList==null) workerList=new ArrayList<>();
    var clientList=(ArrayList<ClientCompany>)request.getAttribute("clientList");
    if (clientList==null) clientList=new ArrayList<>();
    var licenseList=(ArrayList<License>)request.getAttribute("licenseList");
    if (licenseList==null) licenseList=new ArrayList<>();
%>
<html>
    <head>
        <title>Nuovo servizio</title>
        <script>
            window.addEventListener("load", function() {

                document.querySelector("#addService").addEventListener("click", function() {

                    document.newServiceForm.action.value="ServiceDispatcher.addService";
                    document.newServiceForm.submit();
                });

                document.querySelector("#editService").addEventListener("click", function() {

                    document.editServiceForm.action.value="ServiceDispatcher.editService";
                    document.editServiceForm.submit();
                });
            });
        </script>
    </head>
    <body>
        <form name="newServiceForm" action="<%= contextPath %>/Dispatcher" method="post">
            <label for="clientList">Selezionare azienda cliente: </label>
            <select id="clientList" name="client">
                <% for (var client: clientList) { %><option value="<%= client.getSocialReason() %>"><%= client.getName()+" ("+client.getLocation()+")"%></option><% } %>
            </select><br/>
            <% for (var license: licenseList) { %>
                <label for="<%= license.getCategory()%>"><%= license.getCategory() %></label>
                <input type="checkbox" name="license" id="<%= license.getCategory()%>">
            <% } %>
            <label for="name">Inserire nome servizio: </label>
            <input type="text" id="name" name="name"><br/>
            <label for="date">Selezionare data servizio: </label>
            <input type="date" id="date" name="date"><br/>
            <label for="startTime">Selezionare orario di inizio servizio: </label>
            <input type="time" id="startTime" name="startTime"><br/>
            <label for="duration">Selezionare durata servizio: </label>
            <input type="time" id="duration" name="duration"><br/>
            <input type="button" id="addService" name="addService" value="Aggiungi nuovo servizio">
            <input type="hidden" name="action">
        </form>
        <form name="editServiceForm" action="<%= contextPath %>/Dispatcher">
            <label for="firstWorkerList">Selezionare autisti: </label>
            <select id="firstWorkerList" name="worker">
                <% for (var worker: workerList) { %><option value="<%= worker.getFiscalCode() %>"><%= worker.getName()+" "+worker.getSurname()%></option><% } %>
            </select>
            <select id="secondWorkerList" name="worker">
                <option id="null">-- Selezionare autista --</option>
                <% for (var worker: workerList) { %><option value="<%= worker.getFiscalCode() %>"><%= worker.getName()+" "+worker.getSurname()%></option><% } %>
            </select><br/>
            <label for="truckList">Selezionare mezzo: </label>
            <select id="truckList" name="truck">
                <% for (var truck: truckList) { %><option value="<%= truck.getNumberPlate() %>"><%= truck.getNumberPlate()+" ("+truck.getBrand()+" "+truck.getModel()+")"%></option><% } %>
            </select><br/>
            <input type="button" id="editService" name="editService" value="Modifica servizio">
            <input type="hidden" name="action">
        </form>
    </body>
</html>
