<%@ page import="ingweb.main.aziendatrasporti.mo.Worker" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.License" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var workerList=(ArrayList<Worker>)request.getAttribute("workerList");
    if (workerList==null) workerList=new ArrayList<>();
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
                let newWorkerButton=document.querySelector("#newWorkerButton");
                let backButton=document.querySelector("#backButton");

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="WorkerDispatcher.getWorkers";
                    document.dataForm.submit();
                });

                backButton.addEventListener("click", function() {

                    document.dataForm.action.value="LoginDispatcher.validate";
                    document.dataForm.submit();
                });

                newWorkerButton.addEventListener("click", function() {

                    document.dataForm.action.value="WorkerDispatcher.newWorker";
                    document.dataForm.submit();
                });

                removeButtons.forEach(b => {

                    b.addEventListener("click", function() {

                        document.dataForm.action.value="WorkerDispatcher.removeWorker";
                        document.dataForm.name.value=this.id;
                        document.dataForm.submit();
                    });
                });

                updateButtons.forEach(b => {

                    b.addEventListener("click", function() {

                        document.dataForm.action.value="WorkerDispatcher.editWorker";
                        document.dataForm.name.value=this.id;
                        document.dataForm.submit();
                    });
                });
            });
        </script>
    </head>
    <body>
        <%@ include file="/jsp/admin/welcome.jsp" %>
        <hr/>
        <h1>Lista autisti</h1>
        <table>
            <tr class="firstRow">
                <td rowspan="2">Nome</td>
                <td rowspan="2">Cognome</td>
                <td rowspan="2">Codice fiscale</td>
                <td rowspan="2">Data di nascita</td>
                <td rowspan="2">Numero di telefono</td>
                <td colspan="<%= licenseList.size() %>">Patenti</td>
                <td rowspan="2" colspan="2">Azioni</td>
            </tr>
            <tr class="firstRow"><% for (var license: licenseList) { %><td><%= license.getCategory() %></td><% } %></tr>
            <% for (var worker: workerList) {
                var licenses=worker.getLicenses();
                if (licenses==null) licenses=new ArrayList<>(); %>
                <tr>
                    <% for (var field: worker.asList()) if (!(field instanceof Boolean)) { %><td><%= field %></td><% } %>
                    <% for (var license: licenseList) { %><td><input type="checkbox" <%= licenses.contains(license) ? "checked" : "" %> disabled/></td><% } %>
                    <td><input type="button" id="<%= worker.getName()+"."+worker.getSurname()+"."+worker.getFiscalCode()+"."+worker.getBirthDate()+"."+worker.getTelNumber() %>" name="edit" value="Modifica"></td>
                    <td><input type="button" id="r<%= worker.getFiscalCode() %>" name="remove" value="Rimuovi"></td>
                </tr>
            <% } %>
        </table>
        <form name="dataForm" action="<%= request.getContextPath() %>/Dispatcher" method="post">
            <div class="styled">
                <input type="button" id="newWorkerButton" value="Nuovo autista">
                <input type="button" id="refreshButton" value="Aggiorna lista">
                <input type="button" id="backButton" value="Chiudi tab">
            </div>
            <input type="hidden" name="name">
            <input type="hidden" name="action">
        </form>
    </body>
</html>