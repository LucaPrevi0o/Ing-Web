<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.License" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.Truck" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var contextPath=request.getContextPath();
    var truck=(Truck)request.getAttribute("truck");
    var licenseList=(ArrayList<License>)request.getAttribute("licenseList");
    if (licenseList==null) licenseList=new ArrayList<>();
%>
<html>
    <head>
        <title>Nuovo mezzo</title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/generalStyle.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataForm.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/checkbox.css">
        <script>
            window.addEventListener("load", function() {

                let addButton=document.querySelector("#addButton");
                let refreshButton=document.querySelector("#refreshButton");

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="TruckDispatcher.getTrucks";
                    document.dataForm.submit();
                });

                addButton.addEventListener("click", function() {

                    if (document.querySelector("#addButton").value==="Aggiungi mezzo") document.dataForm.action.value="TruckDispatcher.addTruck";
                    else document.dataForm.action.value="TruckDispatcher.updateTruck";
                    document.dataForm.submit();
                });
            });
        </script>
    </head>
    <body>
        <form name="dataForm" action="<%= contextPath %>/Dispatcher" method="post">
            <h1>Nuovo mezzo</h1>
            <hr/>
            <table>
                <tr>
                    <td><label for="numberPlate">Targa</label></td>
                    <td colspan="<%= licenseList.size() %>"><input type="text" id="numberPlate" name="numberPlate" placeholder="AA000AA" value="<%= truck==null ? "" : truck.getNumberPlate() %>" <%= truck==null ? "" : "readonly" %> required/></td>
                </tr>
                <tr>
                    <td><label for="brand">Marca:</label></td>
                    <td colspan="<%= licenseList.size() %>"><input type="text" id="brand" name="brand" placeholder="Marca veicolo" value="<%= truck==null ? "" : truck.getBrand() %>" required/></td>
                </tr>
                <tr>
                    <td><label for="model">Modello:</label></td>
                    <td colspan="<%= licenseList.size() %>"><input type="text" id="model" name="model" placeholder="Modello veicolo" value="<%= truck==null ? "" : truck.getModel() %>" required/></td>
                </tr>
                <tr>
                    <td><label for="available">Il mezzo è disponibile:</label></td>
                    <td colspan="<%= licenseList.size() %>"><input type="checkbox" id="available" name="available" value="available" <%= truck==null || !truck.isAvailable() ? "" : "checked" %>/></td>
                </tr>
                <tr>
                    <td rowspan="2">Patenti richieste</td>
                    <% for (var license: licenseList) { %><td>
                        <label for="<%= license.getCategory() %>"><%= license.getCategory() %></label>
                        <input type="checkbox" name="license" id="<%= license.getCategory() %>" value="<%= license.getCategory() %>" <%= truck==null || !truck.getNeededLicenses().contains(license) ? "" : "checked" %>/>
                    </td><% } %>
                </tr>
            </table>
            <br/>
            <div class="styled">
                <input type="button" id="addButton" value="<%= truck==null ? "Aggiungi mezzo" : "Modifica mezzo"%>"/>
                <input type="button" id="refreshButton" value="Torna alla lista mezzi"/>
                <input type="hidden" name="action" value=""/>
            </div>
        </form>
    </body>
</html>
