<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.mo.Assignment" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<% var assignment=(Assignment)request.getAttribute("assignment"); %>
<%@ include file="/jsp/worker/welcome.jsp" %>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataForm.css">
        <title>Segnala un problema</title>
        <script>
            window.addEventListener("load", function() {

                let addButton=document.querySelector("#addButton");
                let refreshButton=document.querySelector("#refreshButton");

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="AssignmentController.getAssignments";
                    document.dataForm.submit();
                });

                addButton.addEventListener("click", function() {

                    document.dataForm.action.value="AssignmentController.signalAssignment";
                    document.dataForm.submit();
                });
            });
        </script>
    </head>
    <body>
        <form name="dataForm" action="<%= request.getContextPath() %>/Servizi" method="post">
            <h1><label for="comment">Segnala un problema</label></h1>
            <hr>
            <textarea id="comment" name="comment" placeholder="Segnala un problema..." maxlength="500" required></textarea>
            <input type="hidden" name="action">
            <input type="hidden" name="code" value="<%= assignment.getCode() %>">
            <div class="styled">
                <input type="button" id="addButton" value="Aggiungi commento">
                <input type="button" id="refreshButton" value="Torna alla lista servizi">
            </div>
        </form>
    </body>
</html>
