<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.*" %>
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

                let updateButtons=document.querySelectorAll("input[name='assign']");
                let refreshButton=document.querySelector("#refreshButton");
                let serviceListButton=document.querySelector("#newServiceButton");
                let backButton=document.querySelector("#backButton");

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="ServiceDispatcher.getServices";
                    document.dataForm.submit();
                });

                backButton.addEventListener("click", function() {

                    document.dataForm.action.value="LoginDispatcher.validate";
                    document.dataForm.submit();
                });

                serviceListButton.addEventListener("click", function() {

                    document.dataForm.action.value="ServiceDispatcher.getServiceList";
                    document.dataForm.submit();
                });

                updateButtons.forEach(b => {

                    b.addEventListener("click", function() {

                        document.dataForm.action.value="ServiceDispatcher.assignService";
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
        <h1>Servizi in corso</h1>
        <table>
            <tr class="firstRow">
                <td>Nome</td>
                <td>Cliente</td>
                <td>Data</td>
                <td>Orario inizio</td>
                <td>Durata</td>
                <td>Primo autista</td>
                <td>Secondo autista</td>
                <td>Mezzo</td>
                <td>Azioni</td>
            </tr>
            <% for (var service: serviceList) { %>
                <tr>
                    <% for (var field: service.asList()) if (!(field instanceof Boolean)) { %><td><%=
                        (field==null ? "---" :
                        (field instanceof ClientCompany ? ((ClientCompany)field).display() :
                        (field instanceof Worker ? ((Worker)field).display() :
                        (field instanceof Truck ? ((Truck)field).display() : field)))) %></td><% } %>
                    <td><input type="button" id="<%= service.getCode() %>" name="assign" value="Modifica assegnamento"></td>
                </tr>
            <% } %>
        </table>
        <form name="dataForm" action="<%= request.getContextPath() %>/Servizi" method="post">
            <div class="styled">
                <input type="button" id="newServiceButton" value="Torna alla lista servizi">
                <input type="button" id="refreshButton" value="Aggiorna lista">
                <input type="button" id="backButton" value="Chiudi tab">
            </div>
            <input type="hidden" name="code">
            <input type="hidden" name="action">
        </form>
    </body>
</html>