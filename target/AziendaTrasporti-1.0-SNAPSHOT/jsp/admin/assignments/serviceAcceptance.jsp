<%@ page import="ingweb.main.aziendatrasporti.mo.mo.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<% var service=(Service)request.getAttribute("service"); %>
<%@ include file="/jsp/admin/welcome.jsp" %>
<html>
    <head>
        <title>Assegnamento servizi</title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataForm.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/checkbox.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/select.css">
        <script>
            function submitForm() {

                document.dataForm.action.value="ServiceController.acceptRequest";
                document.dataForm.submit();
            }

            window.addEventListener("load", function() {

                let addButton=document.querySelector("#assignService");
                let refreshButton=document.querySelector("#backButton");

                window.addEventListener("keydown", function(e) { if (e.key==="Enter") submitForm(); });

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="ServiceController.getServices";
                    document.dataForm.submit();
                });

                addButton.addEventListener("click", submitForm);
            });
        </script>
    </head>
    <body>
        <hr/>
        <form name="dataForm" action="<%= request.getContextPath() %>/Servizi" method="post">
            <h1>Accettazione richiesta servizio</h1>
            <hr/>
            <table>
                <tr class="firstRow"><td colspan="<%= service.getValidLicenses().size()+1 %>">Servizio: <%= service.getName() %></td></tr>
                <tr>
                    <td>Cliente</td>
                    <td colspan="<%= service.getValidLicenses().size() %>"><%= service.getClientCompany().display() %></td>
                </tr>
                <tr>
                    <td><label for="date">Data</label></td>
                    <td colspan="<%= service.getValidLicenses().size() %>"><input type="date" id="date" name="date" required/></td>
                </tr>
                <tr>
                    <td><label for="startTime">Orario inizio</label></td>
                    <td colspan="<%= service.getValidLicenses().size() %>"><input type="time" id="startTime" name="startTime" step="1800" required/></td>
                </tr>
                <tr>
                    <td>Durata servizio</td>
                    <td colspan="<%= service.getValidLicenses().size() %>"><%= service.getDuration() %></td>
                </tr>
                <tr>
                    <td>Patenti necessarie</td>
                    <% for (var license: service.getValidLicenses()) { %><td><%= license.getCategory() %></td><% } %>
                </tr>
            </table>
            <br/>
            <div class="styled">
                <input type="button" id="assignService" name="assignService" value="Accetta servizio"/>
                <input type="button" id="backButton" name="backButton" value="Torna alla lista servizi"/>
            </div>
            <input type="hidden" name="code" value="<%= service.getCode() %>"/>
            <input type="hidden" name="action">
        </form>
    </body>
</html>
