<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.ClientCompany" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var contextPath=request.getContextPath();
    var clientCompany=(ClientCompany)request.getAttribute("clientCompany");
%>
<html>
    <head>
        <title>Nuovo cliente</title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/generalStyle.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataForm.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/checkbox.css">
        <script>
            window.addEventListener("load", function() {

                let addButton=document.querySelector("#addButton");
                let refreshButton=document.querySelector("#refreshButton");

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="ClientDispatcher.getClients";
                    document.dataForm.submit();
                });

                addButton.addEventListener("click", function() {

                    if (document.querySelector("#addButton").value==="Aggiungi cliente") document.dataForm.action.value="ClientDispatcher.addClient";
                    else document.dataForm.action.value="ClientDispatcher.updateClient";
                    document.dataForm.submit();
                });
            });
        </script>
    </head>
    <body>
        <form name="dataForm" action="<%= contextPath %>/Dispatcher" method="post">
            <h1>Nuovo cliente</h1>
            <hr/>
            <table>
                <tr>
                    <td colspan="2"><label for="name">Nome</label></td>
                    <td><input type="text" id="name" name="name" placeholder="Nome persona" value="<%= clientCompany==null ? "" : clientCompany.getName() %>" required/></td>
                </tr>
                <tr>
                    <td colspan="2"><label for="socialReason">Ragione sociale</label></td>
                    <td><input type="text" id="socialReason" name="socialReason" placeholder="Ragione sociale" value="<%= clientCompany==null ? "" : clientCompany.getSocialReason() %>" required/></td>
                </tr>
                <tr>
                    <td colspan="2"><label for="location">Sede</label></td>
                    <td><input type="text" id="location" name="location" placeholder="Nome persona" value="<%= clientCompany==null ? "" : clientCompany.getLocation() %>" required/></td>
                </tr>
                <tr><td rowspan="5">Responsabile</td></tr>
                <tr>
                    <td><label for="managerName">Nome</label></td>
                    <td><input type="text" id="managerName" name="managerName" placeholder="Nome responsabile" value="<%= clientCompany==null ? "" : clientCompany.getManagerName() %>" required></td>
                </tr>
                <tr>
                    <td><label for="managerFiscalCode">Codice fiscale</label></td>
                    <td><input type="text" id="managerFiscalCode" name="managerFiscalCode" placeholder="BBBAAA00A00A000A" value="<%= clientCompany==null ? "" : clientCompany.getManagerFiscalCode() %>" required></td>
                </tr>
                <tr>
                    <td><label for="managerBirthDate">Data di nascita</label></td>
                    <td><input type="date" id="managerBirthDate" name="managerBirthDate" value="<%= clientCompany==null ? "" : clientCompany.getManagerBirthDate() %>" required></td>
                </tr>
                <tr>
                    <td><label for="managerTelNumber">Numero di telefono</label></td>
                    <td><input type="text" id="managerTelNumber" name="managerTelNumber" placeholder="+39 XXX-XXXXXXX" value="<%= clientCompany==null ? "" : clientCompany.getManagerTelNumber() %>" required></td>
                </tr>
            </table>
            <br/>
            <div class="styled">
                <input type="button" id="addButton" value="<%= clientCompany==null ? "Aggiungi cliente" : "Modifica dati cliente"%>">
                <input type="button" id="refreshButton" value="Torna alla lista clienti">
                <input type="hidden" name="action" value="">
            </div>
        </form>
    </body>
</html>
