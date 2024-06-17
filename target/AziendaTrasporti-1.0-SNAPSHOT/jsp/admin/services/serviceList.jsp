<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.Service" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var serviceList=(ArrayList<Service>)request.getAttribute("serviceList");
    if (serviceList==null) serviceList=new ArrayList<>();
%>
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
                let refreshButton=document.querySelector("#refreshButton");
                let newServiceButton=document.querySelector("#newServiceButton");
                let backButton=document.querySelector("#backButton");

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="ServiceDispatcher.getServiceList";
                    document.dataForm.submit();
                });

                backButton.addEventListener("click", function() {

                    document.dataForm.action.value="LoginDispatcher.validate";
                    document.dataForm.submit();
                });

                newServiceButton.addEventListener("click", function() {

                    document.dataForm.action.value="ServiceDispatcher.newService";
                    document.dataForm.submit();
                });

                removeButtons.forEach(b => {

                    b.addEventListener("click", function() {

                        document.dataForm.action.value="ServiceDispatcher.removeService";
                        document.dataForm.name.value=this.id;
                        document.dataForm.submit();
                    });
                });

                updateButtons.forEach(b => {

                    b.addEventListener("click", function() {

                        document.dataForm.action.value="ServiceDispatcher.editService";
                        document.dataForm.name.value=this.id;
                        document.dataForm.submit();
                    });
                });

                assignButtons.forEach(b => {

                    b.addEventListener("click", function() {

                        document.dataForm.action.value="ServiceDispatcher.assignService";
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
        <h1>Lista servizi</h1>
        <table>
            <tr class="firstRow">
                <td>Nome</td>
                <td>Data</td>
                <td>Orario inizio</td>
                <td>Durata</td>
                <td colspan="3">Azioni</td>
            </tr>
            <% for (var service: serviceList) { %>
            <tr>
                <% for (var field: service.data()) if (!(field instanceof Boolean)) { %><td><%= field %></td><% } %>
                <td><input type="button" id="<%= service.getName()+"."+service.getDate()+"."+service.getStartTime()+"."+service.getDuration() %>" name="edit" value="Modifica"></td>
                <td><input type="button" id="a<%= service.getCode()%>" name="assign" value="Assegna servizio"></td>
                <td><input type="button" id="r<%= service.getCode() %>" name="remove" value="Rimuovi"></td>
            </tr>
            <% } %>
        </table>
        <form name="dataForm" action="<%= request.getContextPath() %>/Dispatcher" method="post">
            <input type="button" id="newServiceButton" value="Nuovo servizio">
            <input type="button" id="refreshButton" value="Aggiorna lista">
            <input type="button" id="backButton" value="Chiudi tab">
            <input type="hidden" name="name">
            <input type="hidden" name="action">
        </form>
    </body>
</html>