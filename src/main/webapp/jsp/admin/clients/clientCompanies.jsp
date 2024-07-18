<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.mo.ClientCompany" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var clientList=(ArrayList<ClientCompany>)request.getAttribute("clientList");
    if (clientList==null) clientList=new ArrayList<>();
%>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/generalStyle.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <script>
            window.addEventListener("load", function() {

                let removeButtons=document.querySelectorAll("input[name='remove']");
                let updateButtons=document.querySelectorAll("input[name='edit']");
                let refreshButton=document.querySelector("#refreshButton");
                let newClientButton=document.querySelector("#addButton");
                let backButton=document.querySelector("#backButton");

                refreshButton.addEventListener("click", function() { document.dataForm.action.value="ClientController.getClients"; });
                backButton.addEventListener("click", function() { document.dataForm.action.value="LoginController.doLogin"; });
                newClientButton.addEventListener("click", function() { document.dataForm.action.value="ClientController.newClient"; });

                removeButtons.forEach(b => {

                    b.addEventListener("click", function() {

                        document.dataForm.action.value="ClientController.removeClient";
                        document.dataForm.code.value=this.id;
                        document.dataForm.submit();
                    });
                });

                updateButtons.forEach(b => {

                    b.addEventListener("click", function() {

                        document.dataForm.action.value="ClientController.editClient";
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
        <h1>Lista clienti</h1>
        <table>
            <tr class="firstRow">
                <td rowspan="2">Nome</td>
                <td rowspan="2">Ragione sociale</td>
                <td rowspan="2">Sede</td>
                <td colspan="4">Responsabile</td>
                <td rowspan="2" colspan="2">Azioni</td>
            </tr>
            <tr class="firstRow">
                <td>Nome</td>
                <td>Codice fiscale</td>
                <td>Data di nascita</td>
                <td>Numero di teleono</td>
            </tr>
            <% for (var client: clientList) { %>
                <tr>
                    <% for (var field: client.data()) if (!(field instanceof Boolean)) { %><td><%= field %></td><% } %>
                    <td><input type="button" id="<%= client.getCode() %>" name="edit" value="Modifica"></td>
                    <td><input type="button" id="<%= client.getCode() %>" name="remove" value="Rimuovi"></td>
                </tr>
            <% } %>
        </table>
        <%@include file="/jsp/admin/footer.jsp"%>
    </body>
</html>