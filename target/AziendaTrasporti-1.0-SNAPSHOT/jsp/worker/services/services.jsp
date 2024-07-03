<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.mo.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var assignmentList =(ArrayList<Assignment>)request.getAttribute("assignmentList");
    if (assignmentList ==null) assignmentList =new ArrayList<>();
%>
<%@ include file="/jsp/worker/welcome.jsp" %>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/generalStyle.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/navMenu.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/checkbox.css">
        <script>
            window.addEventListener("load", function() {

                let refreshButton=document.querySelector("#refreshButton");
                let backButton=document.querySelector("#backButton");

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="AssignmentController.getAssignments";
                    document.dataForm.submit();
                });

                backButton.addEventListener("click", function() {

                    document.dataForm.action.value="LoginController.doLogin";
                    document.dataForm.submit();
                });
            });
        </script>
    </head>
    <body>
        <hr/>
        <h2>Visualizzazione programma di lavoro</h2>
        <hr/>
        <h1>Servizi in programma</h1>
        <table>
            <tr class="firstRow">
                <td>Servizio</td>
                <td>Primo autista</td>
                <td>Secondo autista</td>
                <td>Mezzo</td>
            </tr>
            <% for (var service: assignmentList) { %>
                <tr>
                    <% for (var field: service.data()) if (!(field instanceof Boolean)) { %>
                        <td>
                            <%= (field==null ? "---" :
                            (field instanceof Service ? ((Service)field).display() :
                            (field instanceof Worker ? ((Worker)field).display() :
                            (field instanceof Truck ? ((Truck)field).display() : field)))) %>
                        </td><% } %>
                </tr>
            <% } %>
        </table>
        <nav>
            <form name="dataForm" action="<%= request.getContextPath() %>/Servizi" method="post">
                <div class="styled">
                    <input type="button" id="refreshButton" value="Aggiorna lista">
                    <input type="button" id="backButton" value="Chiudi tab">
                </div>
                <input type="hidden" name="action">
            </form>
        </nav>
    </body>
</html>