<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.mo.Service" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.mo.License" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.mo.ClientCompany" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var serviceList=(ArrayList<Service>)request.getAttribute("serviceList");
    if (serviceList==null) serviceList=new ArrayList<>();
    var licenseList=(ArrayList<License>)request.getAttribute("licenseList");
    if (licenseList==null) licenseList=new ArrayList<>();
%>
<%@ include file="/jsp/admin/welcome.jsp" %>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/generalStyle.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/checkbox.css">
        <script>
            window.addEventListener("load", function() {

                let removeButtons=document.querySelectorAll("input[name='remove']");
                let updateButtons=document.querySelectorAll("input[name='edit']");
                let assignButtons=document.querySelectorAll("input[name='assign']");
                let assignedButton=document.querySelector("#assignedList");
                let requestedButton=document.querySelector("#requestList");
                let completedButton=document.querySelector("#completedList");
                let refreshButton=document.querySelector("#refreshButton");
                let newServiceButton=document.querySelector("#addButton");
                let backButton=document.querySelector("#backButton");

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="ServiceController.getServices";
                    document.dataForm.submit();
                });

                backButton.addEventListener("click", function() {

                    document.dataForm.action.value="LoginController.doLogin";
                    document.dataForm.submit();
                });

                newServiceButton.addEventListener("click", function() {

                    document.dataForm.action.value="ServiceController.newService";
                    document.dataForm.submit();
                });

                assignedButton.addEventListener("click", function() {

                    document.dataForm.action.value="AssignmentController.getAssignments";
                    document.dataForm.submit();
                });

                requestedButton.addEventListener("click", function() {

                    document.dataForm.action.value="ServiceController.getRequests";
                    document.dataForm.submit();
                });

                completedButton.addEventListener("click", function() {

                    document.dataForm.action.value="AssignmentController.getCompleted";
                    document.dataForm.submit();
                });

                removeButtons.forEach(b => {

                    b.addEventListener("click", function() {

                        document.dataForm.action.value="ServiceController.removeService";
                        document.dataForm.code.value=this.id;
                        document.dataForm.submit();
                    });
                });

                updateButtons.forEach(b => {

                    b.addEventListener("click", function() {

                        document.dataForm.action.value="ServiceController.editService";
                        document.dataForm.code.value=this.id;
                        document.dataForm.submit();
                    });
                });

                assignButtons.forEach(b => {

                    b.addEventListener("click", function() {

                        document.dataForm.action.value="AssignmentController.newAssignment";
                        document.dataForm.code.value=this.id;
                        document.dataForm.submit();
                    });
                });
            });
        </script>
    </head>
    <body>
        <hr/>
        <h1>Lista servizi</h1>
        <table>
            <tr class="firstRow">
                <td rowspan="2">Nome</td>
                <td rowspan="2">Cliente</td>
                <td rowspan="2">Data</td>
                <td rowspan="2">Orario inizio</td>
                <td rowspan="2">Durata</td>
                <td colspan="<%= licenseList.size() %>">Patenti</td>
                <td colspan="3" rowspan="2">Azioni</td>
            </tr>
            <tr class="firstRow">
                <% for (var license: licenseList) { %><td><%= license.getCategory() %></td><% } %>
            </tr>
            <% for (var service: serviceList) {
                var licenses=service.getValidLicenses();
                if (licenses==null) licenses=new ArrayList<>(); %>
                <tr>
                    <% for (var field: service.data()) if (!(field instanceof Boolean)) { %><td><%= field instanceof ClientCompany ? ((ClientCompany)field).display() : field %></td><% } %>
                    <% for (var license: licenseList) { %>
                        <td><input type="checkbox" <%= licenses.contains(license) ? "checked" : "" %> disabled/></td>
                    <% } %>
                    <td><input type="button" id="<%= service.getCode() %>" name="edit" value="Modifica"></td>
                    <td><input type="button" id="<%= service.getCode() %>" name="assign" value="Assegna servizio"></td>
                    <td><input type="button" id="<%= service.getCode() %>" name="remove" value="Rimuovi"></td>
                </tr>
            <% } %>
        </table>
        <table>
            <tr>
                <td><input type="button" id="assignedList" value="Vedi servizi in corso"/></td>
                <td><input type="button" id="requestList" value="Vedi richieste"/></td>
                <td><input type="button" id="completedList" value="Servizi completati"/></td>
            </tr>
        </table>
        <%@include file="/jsp/admin/footer.jsp"%>
    </body>
</html>