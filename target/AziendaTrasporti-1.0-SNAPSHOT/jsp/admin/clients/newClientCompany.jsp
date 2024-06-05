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
            <label for="name">Nome:</label>
            <input type="text" id="name" name="name" placeholder="Nome persona" value="<%= clientCompany==null ? "" : clientCompany.getName() %>" required><br/>
            <label for="socialReason">Ragione sociale:</label>
            <input type="text" id="socialReason" name="socialReason" placeholder="Ragione sociale" value="<%= clientCompany==null ? "" : clientCompany.getSocialReason() %>" <%= clientCompany==null ? "" : "readonly" %> required><br/>
            <label for="location">Sede:</label>
            <input type="text" id="location" name="location" placeholder="Nome persona" value="<%= clientCompany==null ? "" : clientCompany.getLocation() %>" required><br/>
            <label for="manager">Responsabile:</label>
            <input type="text" id="manager" name="manager" placeholder="Responsabile" value="<%= clientCompany==null ? "" : clientCompany.getManager() %>" required><br/>
            <input type="button" id="addButton" value="<%= clientCompany==null ? "Aggiungi cliente" : "Modifica dati cliente"%>">
            <input type="button" id="refreshButton" value="Torna alla lista clienti">
            <input type="hidden" name="action" value="">
        </form>
    </body>
</html>
