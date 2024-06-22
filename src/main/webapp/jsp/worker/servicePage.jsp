<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var loggedAccount=(Account)request.getAttribute("loggedAccount");
    var serviceList=(ArrayList<Service>)request.getAttribute("serviceList");
    if (serviceList==null) serviceList=new ArrayList<>();
%>
<html>
    <head>
        <title>Home - <%= loggedAccount.getFullName().toUpperCase() %></title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/generalStyle.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/navMenu.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/checkbox.css">
        <script>
            window.addEventListener("load", function() {

                let refreshButton=document.querySelector("#refreshButton");
                let backButton=document.querySelector("#backButton");

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="LoginDispatcher.validate";
                    document.dataForm.submit();
                });

                backButton.addEventListener("click", function() {

                    document.dataForm.action.value="LoginDispatcher.logout";
                    document.dataForm.submit();
                });
            });
        </script>
    </head>
    <body>
        <h1>Benvenuto, <%= loggedAccount.getFullName() %></h1>
        <h2>Visualizzazione programma di lavoro</h2>
        <hr/>
        <h1>Servizi in programma</h1>
        <table>
            <tr class="firstRow">
                <td>Nome</td>
                <td>Cliente</td>
                <td>Data</td>
                <td>Orario inizio</td>
                <td>Durata</td>
                <td>Mezzo</td>
            </tr>
            <% for (var service: serviceList) { %>
                <tr>
                    <% for (var field: service.shortList()) if (!(field instanceof Boolean)) { %><td><%=
                        (field==null ? "---" :
                        (field instanceof ClientCompany ? ((ClientCompany)field).display() :
                        (field instanceof Truck ? ((Truck)field).getNumberPlate() : field))) %></td><% } %>
                </tr>
            <% } %>
        </table>
        <form name="dataForm" action="<%= request.getContextPath() %>/Servizi" method="post">
            <div class="styled">
                <input type="button" id="refreshButton" value="Aggiorna lista">
                <input type="button" id="backButton" value="Torna al login">
            </div>
            <input type="hidden" name="action">
        </form>
    </body>
</html>