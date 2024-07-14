<%@ page import="ingweb.main.aziendatrasporti.mo.mo.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<% var assignment=(Assignment)request.getAttribute("assignment"); %>
<%@ include file="/jsp/clientManager/welcome.jsp" %>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataForm.css">
        <script>
            window.addEventListener("load", function() {

                let problemButton=document.querySelector("#problemButton");
                let refreshButton=document.querySelector("#refreshButton");

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="AssignmentController.getAssignments";
                    document.dataForm.submit();
                });

                problemButton.addEventListener("click", function() {

                    document.dataForm.action.value="AssignmentController.newProblem";
                    document.dataForm.submit();
                });
            });
        </script>
    </head>
    <body>
        <hr/>
        <form name="dataForm" action="<%= request.getContextPath() %>/Servizi" method="post">
            <h1>Dettagli servizio</h1>
            <hr/>
            <table>
                <tr>
                    <td>Nome servizio</td>
                    <td><%= assignment.getService().getName() %></td>
                </tr>
                <tr>
                    <td>Data</td>
                    <td><%= assignment.getService().getDate() %></td>
                </tr>
                <tr>
                    <td>Ora inizio</td>
                    <td><%= assignment.getService().getStartTime() %></td>
                </tr>
                <tr>
                    <td>Durata servizio</td>
                    <td><%= assignment.getService().getDuration() %></td>
                </tr>
                <tr>
                    <td>Primo autista</td>
                    <td><%= assignment.getFirstDriver() %></td>
                </tr>
                <tr>
                    <td>Secondo autista</td>
                    <td><%= assignment.getSecondDriver() %></td>
                </tr>
                <tr>
                    <td>Mezzo di lavoro</td>
                    <td><%= assignment.getTruck().display() %></td>
                </tr>
            </table>
            <br/>
            <div class="styled">
                <input type="button" id="refreshButton" value="Torna a lista servizi">
                <input type="button" id="problemButton" value="Segnala un problema">
            </div>
            <input type="hidden" name="code" value="<%= assignment.getCode() %>">
            <input type="hidden" name="action">
        </form>
    </body>
</html>