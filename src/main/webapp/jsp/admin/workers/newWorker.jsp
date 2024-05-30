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
        <link rel="stylesheet" type="text/css" href="<%= contextPath %>/style/standard.css">
        <link rel="stylesheet" type="text/css" href="<%= contextPath %>/style/form.css">
        <link rel="stylesheet" type="text/css" href="<%= contextPath %>/style/checkbox.css">
        <script>
            window.addEventListener("load", function() {

                let addButton=document.querySelector("#addButton");
                let backButton=document.querySelector("#backButton");
                let refreshButton=document.querySelector("#refreshButton");

                backButton.addEventListener("click", function() {

                    document.dataForm.action.value="LoginDispatcher.validate";
                    document.dataForm.submit();
                });

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
            <label for="name">Nome</label><br>
            <input type="text" id="name" name="name" placeholder="Nome persona" value="<%= worker==null ? "" : worker.getName() %>" required/><br/>
            <label for="surname">Cognome</label><br>
            <input type="text" id="surname" name="surname" placeholder="Cognome persona" value="<%= worker==null ? "" : worker.getSurname() %>" required/><br/>
            <label for="fiscalCode">Codice Fiscale</label><br>
            <input type="text" id="fiscalCode" name="fiscalCode" placeholder="AAABBB00X00X000X" pattern="^[A-Z]{6}[0-9]{2}[A-Z]{1}[0-9]{2}[A-Z]{1}[0-9]{3}[A-Z]{1}$" title="Inserire un codice fiscale nel formato richiesto" value="<%= worker==null ? "" : worker.getFiscalCode() %>" <%= worker==null ? "" : "readonly" %> required/><br/>
            <label for="birthDate">Data di nascita</label><br>
            <input type="date" id="birthDate" name="birthDate" value="<%= worker==null ? "" : worker.getBirthDate() %>" required/><br/>
            <label for="telNumber">Numero di telefono</label><br>
            <input type="tel" id="telNumber" name="telNumber" placeholder="+39 XXX-XXXXXXX" pattern="^\\\+[0-9]{2} [0-9]{3}-[0-9]{7}$" title="Inserire un numero cellulare nel formato richiesto" value="<%= worker==null ? "" : worker.getTelNumber() %>" required><br/>
            <table>
                <tr>
                    <td>Patenti</td>
                    <% for (var license: licenseList) { %><td>
                        <label for="<%= license.getCategory() %>"><%= license.getCategory() %></label>
                        <input type="checkbox" name="license" id="<%= license.getCategory() %>" value="<%= license.getCategory() %>" <%= worker==null || !worker.getLicenses().contains(license) ? "" : "checked" %>>
                    </td><% } %>
                </tr>
            </table><br/>
            <div class="styled">
                <input type="button" id="addButton" value="<%= worker==null ? "Aggiungi dipendente" : "Modifica dipendente"%>">
                <input type="button" id="refreshButton" value="Torna alla lista autisti">
                <input type="button" id="backButton" value="Torna alla home">
            </div>
            <input type="hidden" name="action" value="">
        </form>
    </body>
</html>
