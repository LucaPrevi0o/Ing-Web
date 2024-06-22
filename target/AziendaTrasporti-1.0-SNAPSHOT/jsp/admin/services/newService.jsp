<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.License" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.Service" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.ClientCompany" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var service=(Service)request.getAttribute("service");
    var licenseList=(ArrayList<License>)request.getAttribute("licenseList");
    if (licenseList==null) licenseList=new ArrayList<>();
    var clientList=(ArrayList<ClientCompany>)request.getAttribute("clientList");
    if (clientList==null) clientList=new ArrayList<>();
%>
<html>
    <head>
        <title><%= service==null ? "Nuovo servizio" : "Modifica dati servizio" %></title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/generalStyle.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/navMenu.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataForm.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/checkbox.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/select.css">
        <script>
            function submitForm() {

                document.activeElement.blur();
                let licenses=document.getElementsByName("license");
                let formData=document.querySelectorAll(":required");
                let i=0, j=0;
                for (let l=0; l<licenses.length; l++) if (licenses[l].checked) i++;
                for (let l=0; l<formData.length; l++) if (formData[l].value==="") j++;
                let submit=(i!==0 && j===0);

                if (document.querySelector("#addButton").value==="Aggiungi servizio") document.dataForm.action.value="ServiceController.addService";
                else document.dataForm.action.value="ServiceController.updateService";
                if (submit) document.dataForm.submit();
                else console.log("wtf");
            }

            window.addEventListener("load", function() {

                let addButton=document.querySelector("#addButton");
                let refreshButton=document.querySelector("#refreshButton");

                window.addEventListener("keydown", function(e) { if (e.key==="Enter") submitForm(); });

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="ServiceController.getServiceList";
                    document.dataForm.submit();
                });

                addButton.addEventListener("click", submitForm);
            });
        </script>
    </head>
    <body>
        <form name="dataForm" action="<%= request.getContextPath() %>/Servizi" method="post">
            <h1><%= service==null ? "Nuovo servizio" : "Modifica dati servizio" %></h1>
            <hr/>
            <table>
                <tr>
                    <td><label for="name">Nome</label></td>
                    <td colspan="<%= licenseList.size() %>"><input type="text" id="name" name="name" placeholder="Nome servizio" value="<%= service==null ? "" : service.getName() %>" required/></td>
                </tr>
                <tr>
                    <td><label for="clientCompany">Cliente</label></td>
                    <td colspan="<%= licenseList.size() %>">
                        <select id="clientCompany" name="clientCompany" required>
                            <% for (var client: clientList) { %><option value="<%= client.getCode() %>"><%= client.display() %></option><% } %>
                        </select>
                    </td>
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
                        <input type="checkbox" name="license" id="<%= license.getCategory() %>" value="<%= license.getCategory() %>" <%= service==null || !service.getValidLicenses().contains(license) ? "" : "checked" %>/>
                    </td><% } %>
                </tr>
            </table>
            <br/>
            <div class="styled">
                <input type="button" id="addButton" value="<%= service==null ? "Aggiungi servizio" : "Modifica servizio"%>"/>
                <input type="button" id="refreshButton" value="Torna alla lista servizi"/>
            </div>
            <input type="hidden" name="action" value=""/>
            <input type="hidden" name="code" value="<%= service==null ? "" : service.getCode() %>"/>
        </form>
    </body>
</html>
