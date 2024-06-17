<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.License" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.Service" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var contextPath=request.getContextPath();
    var service=(Service)request.getAttribute("service");
    var licenseList=(ArrayList<License>)request.getAttribute("licenseList");
    if (licenseList==null) licenseList=new ArrayList<>();
%>
<html>
    <head>
        <title>Nuovo servizio</title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/generalStyle.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataForm.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/checkbox.css">
        <script>
            window.addEventListener("load", function() {

                let addButton=document.querySelector("#addButton");
                let refreshButton=document.querySelector("#refreshButton");

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="ServiceDispatcher.getServiceList";
                    document.dataForm.submit();
                });

                addButton.addEventListener("click", function() {

                    if (document.querySelector("#addButton").value==="Aggiungi servizio") document.dataForm.action.value="ServiceDispatcher.addService";
                    else document.dataForm.action.value="ServiceDispatcher.updateService";
                    document.dataForm.submit();
                });
            });
        </script>
    </head>
    <body>
        <form name="dataForm" action="<%= contextPath %>/Dispatcher" method="post">
            <h1>Nuovo servizio</h1>
            <hr/>
            <table>
                <tr>
                    <td><label for="name">Nome</label></td>
                    <td colspan="<%= licenseList.size() %>"><input type="text" id="name" name="name" placeholder="Nome dipendente" value="<%= service==null ? "" : service.getName() %>" required/></td>
                </tr>
                <tr>
                    <td><label for="date">Data</label></td>
                    <td colspan="<%= licenseList.size() %>"><input type="date" id="date" name="date" value="<%= service==null ? "" : service.getDate() %>" required/></td>
                </tr>
                <tr>
                    <td><label for="startTime">Orario inizio</label></td>
                    <td colspan="<%= licenseList.size() %>"><input type="time" id="startTime" name="startTime" value="<%= service==null ? "" : service.getStartTime() %>" required/></td>
                </tr>
                <tr>
                    <td><label for="duration">Durata servizio</label></td>
                    <td colspan="<%= licenseList.size() %>"><input type="time" id="duration" name="duration" value="<%= service==null ? "" : service.getDuration() %>" required/></td>
                </tr>
                <tr>
                    <td rowspan="2">Patenti necessarie</td>
                    <% for (var license: licenseList) { %><td>
                    <label for="<%= license.getCategory() %>"><%= license.getCategory() %></label>
                    <input type="checkbox" name="license" id="<%= license.getCategory() %>" value="<%= license.getCategory() %>" <%= service ==null || !service.getValidLicenses().contains(license) ? "" : "checked" %>/>
                </td><% } %>
                </tr>
            </table>
            <br/>
            <div class="styled">
                <input type="button" id="addButton" value="<%= service==null ? "Aggiungi servizio" : "Modifica servizio"%>">
                <input type="button" id="refreshButton" value="Torna alla lista servizi">
            </div>
            <input type="hidden" name="action" value="">
            <input type="hidden" name="code" value="<%= service==null ? "" : service.getCode() %>">
        </form>
    </body>
</html>
