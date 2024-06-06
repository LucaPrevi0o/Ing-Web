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
            <table>
                <tr>
                    <td><label for="name">Nome</label></td>
                    <td><input type="text" id="name" name="name" placeholder="Nome persona" value="<%= clientCompany==null ? "" : clientCompany.getName() %>" required/></td>
                </tr>
                <tr>
                    <td><label for="socialReason">Ragione sociale</label></td>
                    <td><input type="text" id="socialReason" name="socialReason" placeholder="Ragione sociale" value="<%= clientCompany==null ? "" : clientCompany.getSocialReason() %>" <%= clientCompany==null ? "" : "readonly" %> required/></td>
                </tr>
                <tr>
                    <td><label for="location">Sede</label></td>
                    <td><input type="text" id="location" name="location" placeholder="Nome persona" value="<%= clientCompany==null ? "" : clientCompany.getLocation() %>" required/></td>
                </tr>
                <tr>
                    <td><label for="manager">Responsabile</label></td>
                    <td><input type="text" id="manager" name="manager" placeholder="Responsabile" value="<%= clientCompany==null ? "" : clientCompany.getManager() %>" required/></td>
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
