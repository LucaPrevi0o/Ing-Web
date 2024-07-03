<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.mo.License" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.mo.Truck" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var truckList=(ArrayList<Truck>)request.getAttribute("truckList");
    if (truckList==null) truckList=new ArrayList<>();
    var licenseList=(ArrayList<License>)request.getAttribute("licenseList");
    if (licenseList==null) licenseList=new ArrayList<>();
%>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/checkbox.css">
        <script>
            window.addEventListener("load", function() {

                let removeButtons=document.querySelectorAll("input[name='remove']");
                let updateButtons=document.querySelectorAll("input[name='edit']");
                let refreshButton=document.querySelector("#refreshButton");
                let newWorkerButton=document.querySelector("#newTruckButton");
                let backButton=document.querySelector("#backButton");
                let filter=document.querySelector("#filter");

                filter.addEventListener("click", function() {

                    document.dataForm.action.value="TruckController.getTrucks";
                    document.dataForm.code.value="filter";
                    document.dataForm.submit();
                });

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="TruckController.getTrucks";
                    document.dataForm.submit();
                });

                backButton.addEventListener("click", function() {

                    document.dataForm.action.value="LoginController.validate";
                    document.dataForm.submit();
                });

                newWorkerButton.addEventListener("click", function() {

                    document.dataForm.action.value="TruckController.newTruck";
                    document.dataForm.submit();
                });

                removeButtons.forEach(b => {

                    b.addEventListener("click", function() {

                        document.dataForm.action.value="TruckController.removeTruck";
                        document.dataForm.code.value=this.id;
                        document.dataForm.submit();
                    });
                });

                updateButtons.forEach(b => {

                    b.addEventListener("click", function() {

                        document.dataForm.action.value="TruckController.editTruck";
                        document.dataForm.code.value=this.id;
                        document.dataForm.submit();
                    });
                });
            });
        </script>
    </head>
    <body>
        <%@ include file="/jsp/admin/welcome.jsp" %>
        <hr/>
        <h1>Lista mezzi</h1>
        <table>
            <tr class="firstRow">
                <td rowspan="2">Targa</td>
                <td rowspan="2">Marca</td>
                <td rowspan="2">Modello</td>
                <td rowspan="2">Disponibilità - <input id="filter" type="button" value="Filtra"></td>
                <td colspan="<%= licenseList.size() %>">Patenti</td>
                <td rowspan="2" colspan="2">Azioni</td>
            </tr>
            <tr class="firstRow"><% for (var license: licenseList) { %><td><%= license.getCategory() %></td><% } %></tr>
            <% for (var truck: truckList) {
                var licenses=truck.getNeededLicenses();
                if (licenses==null) licenses=new ArrayList<>(); %>
                <tr>
                    <% for (var field: truck.data()) if (!(field instanceof Boolean)) { %><td><%= field %></td><% } %>
                    <td><input type="checkbox" <%= truck.isAvailable() ? "checked " : "" %> disabled></td>
                    <% for (var license: licenseList) { %>
                        <td><input type="checkbox" <%= licenses.contains(license) ? "checked" : "" %> disabled/></td>
                    <% } %>
                    <td><input type="button" id="<%= truck.getCode() %>" name="edit" value="Modifica"></td>
                    <td><input type="button" id="<%= truck.getCode() %>" name="remove" value="Rimuovi"></td>
                </tr>
            <% } %>
        </table>
        <nav>
            <form name="dataForm" action="<%= request.getContextPath() %>/Servizi" method="post">
                <div class="styled">
                    <input type="button" id="newTruckButton" value="Nuovo mezzo">
                    <input type="button" id="refreshButton" value="Aggiorna lista">
                    <input type="button" id="backButton" value="Chiudi tab">
                </div>
                <input type="hidden" name="code">
                <input type="hidden" name="action">
            </form>
        </nav>
    </body>
</html>