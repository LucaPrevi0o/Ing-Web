<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.License" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.Truck" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.Account" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var contextPath=request.getContextPath();
    var loggedAccount=(Account)request.getAttribute("loggedAccount");
    var truckList=(ArrayList<Truck>)request.getAttribute("truckList");
    if (truckList==null) truckList=new ArrayList<>();
    var licenseList=(ArrayList<License>)request.getAttribute("licenseList");
    if (licenseList==null) licenseList=new ArrayList<>();
%>
<html>
    <head>
        <title>Lista mezzi</title>
        <script>
            window.addEventListener("load", function() {

                let removeButtons=document.querySelectorAll("input[name='remove']");
                let updateButtons=document.querySelectorAll("input[name='edit']");
                let refreshButton=document.querySelector("#refreshButton");
                let newWorkerButton=document.querySelector("#newTruckButton");
                let backButton=document.querySelector("#backButton");

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="TruckDispatcher.getTrucks";
                    document.dataForm.submit();
                });

                backButton.addEventListener("click", function() {

                    document.dataForm.action.value="LoginDispatcher.validate";
                    document.dataForm.submit();
                });

                newWorkerButton.addEventListener("click", function() {

                    document.dataForm.action.value="TruckDispatcher.newTruck";
                    document.dataForm.submit();
                });

                removeButtons.forEach(b => {

                    b.addEventListener("click", function() {

                        document.dataForm.action.value="TruckDispatcher.removeTruck";
                        document.dataForm.name.value=this.id;
                        document.dataForm.submit();
                    });
                });

                updateButtons.forEach(b => {

                    b.addEventListener("click", function() {

                        document.dataForm.action.value="TruckDispatcher.editTruck";
                        document.dataForm.name.value=this.id;
                        document.dataForm.submit();
                    });
                });
            });
        </script>
    </head>
    <body>
        <h1>Lista mezzi</h1>
        <table>
            <tr>
                <td rowspan="2">Targa</td>
                <td rowspan="2">Marca</td>
                <td rowspan="2">Modello</td>
                <td rowspan="2">Disponibilità</td>
                <td colspan="<%= licenseList.size() %>">Patenti</td>
                <td rowspan="2" colspan="2">Azioni</td>
            </tr>
            <tr><% for (var license: licenseList) { %><td><%= license.getCategory() %></td><% } %></tr>
            <% for (var truck: truckList) {
                var licenses=truck.getNeededLicenses();
                if (licenses==null) licenses=new ArrayList<>(); %>
                <tr>
                    <% for (var field: truck.data()) { %><td><%= field %></td><% } %>
                    <td><input type="checkbox" <%= truck.isAvailable() ? "checked " : "" %> disabled></td>
                    <% for (var license: licenseList) { %>
                        <td><input type="checkbox" <%= licenses.contains(license) ? "checked" : "" %> disabled/></td>
                    <% } %>
                    <td><input type="button" id="<%= truck.getNumberPlate()+"."+truck.getBrand()+"."+truck.getModel()+"."+truck.isAvailable() %>" name="edit" value="Modifica"></td>
                    <td><input type="button" id="r<%= truck.getNumberPlate() %>" name="remove" value="Rimuovi"></td>
                </tr>
            <% } %>
        </table>
        <form name="dataForm" action="<%= contextPath %>/Dispatcher" method="post">
            <input type="button" id="newTruckButton" value="Nuovo mezzo">
            <input type="button" id="refreshButton" value="Aggiorna lista">
            <input type="button" id="backButton" value="Torna alla home">
            <input type="hidden" name="name">
            <input type="hidden" name="action">
            <input type="hidden" name="username" value="<%= loggedAccount.getUsername() %>">
            <input type="hidden" name="password" value="<%= loggedAccount.getPassword() %>">
        </form>
    </body>
</html>