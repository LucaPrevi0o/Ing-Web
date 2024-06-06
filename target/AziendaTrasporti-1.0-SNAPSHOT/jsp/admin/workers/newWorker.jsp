<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.Worker" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.License" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var contextPath=request.getContextPath();
    var worker=(Worker)request.getAttribute("worker");
    var licenseList=(ArrayList<License>)request.getAttribute("licenseList");
    if (licenseList==null) licenseList=new ArrayList<>();
%>
<html>
    <head>
        <title>Nuovo dipendente</title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/generalStyle.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataForm.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/checkbox.css">
        <script>
            window.addEventListener("load", function() {

                let addButton=document.querySelector("#addButton");
                let refreshButton=document.querySelector("#refreshButton");

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="WorkerDispatcher.getWorkers";
                    document.dataForm.submit();
                });

                addButton.addEventListener("click", function() {

                    if (document.querySelector("#addButton").value==="Aggiungi dipendente") document.dataForm.action.value="WorkerDispatcher.addWorker";
                    else document.dataForm.action.value="WorkerDispatcher.updateWorker";
                    document.dataForm.submit();
                });
            });
        </script>
    </head>
    <body>
        <form name="dataForm" action="<%= contextPath %>/Dispatcher" method="post">
            <h1>Nuovo autista</h1>
            <hr/>
            <table>
                <tr>
                    <td><label for="name">Nome</label></td>
                    <td colspan="<%= licenseList.size() %>"><input type="text" id="name" name="name" placeholder="Nome dipendente" value="<%= worker==null ? "" : worker.getName() %>" required/></td>
                </tr>
                <tr>
                    <td><label for="surname">Cognome</label></td>
                    <td colspan="<%= licenseList.size() %>"><input type="text" id="surname" name="surname" placeholder="Cognome dipendente" value="<%= worker==null ? "" : worker.getSurname() %>" required/></td>
                </tr>
                <tr>
                    <td><label for="fiscalCode">Codice fiscale</label></td>
                    <td colspan="<%= licenseList.size() %>"><input type="text" id="fiscalCode" name="fiscalCode" placeholder="AAABBB00X00X000X" pattern="^[A-Z]{6}[0-9]{2}[A-Z]{1}[0-9]{2}[A-Z]{1}[0-9]{3}[A-Z]{1}$" value="<%= worker==null ? "" : worker.getFiscalCode() %>" required/></td>
                </tr>
                <tr>
                    <td><label for="birthDate">Data di nascita</label></td>
                    <td colspan="<%= licenseList.size() %>"><input type="date" id="birthDate" name="birthDate" value="<%= worker==null ? "" : worker.getBirthDate() %>" required/></td>
                </tr>
                <tr>
                    <td><label for="telNumber">Numero di telefono</label></td>
                    <td colspan="<%= licenseList.size() %>"><input type="tel" id="telNumber" name="telNumber" placeholder="+39 XXX-XXXXXXX" pattern="^\\\+[0-9]{2} [0-9]{3}-[0-9]{7}$" value="<%= worker==null ? "" : worker.getTelNumber() %>" required/></td>
                </tr>
                <tr>
                    <td rowspan="2">Patenti</td>
                    <% for (var license: licenseList) { %><td>
                        <label for="<%= license.getCategory() %>"><%= license.getCategory() %></label>
                        <input type="checkbox" name="license" id="<%= license.getCategory() %>" value="<%= license.getCategory() %>" <%= worker==null || !worker.getLicenses().contains(license) ? "" : "checked" %>/>
                    </td><% } %>
                </tr>
            </table>
            <br/>
            <div class="styled">
                <input type="button" id="addButton" value="<%= worker==null ? "Aggiungi dipendente" : "Modifica dipendente"%>">
                <input type="button" id="refreshButton" value="Torna alla lista autisti">
            </div>
            <input type="hidden" name="action" value="">
            <input type="hidden" name="code" value="<%= worker==null ? "" : worker.getCode() %>">
        </form>
    </body>
</html>