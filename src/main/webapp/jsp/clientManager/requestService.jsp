<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.mo.License" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var licenseList=(ArrayList<License>)request.getAttribute("licenseList");
    if (licenseList==null) licenseList=new ArrayList<>();
%>
<%@ include file="/jsp/admin/welcome.jsp" %>
<html>
    <head>
        <title>Richiesta servizio</title>
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

                document.dataForm.action.value="ServiceController.requestService";
                if (submit) document.dataForm.submit();
                else console.log("wtf");
            }

            window.addEventListener("load", function() {

                let addButton=document.querySelector("#addButton");

                window.addEventListener("keydown", function(e) { if (e.key==="Enter") submitForm(); });
                addButton.addEventListener("click", submitForm);
            });
        </script>
    </head>
    <body>
        <hr/>
        <form name="dataForm" action="<%= request.getContextPath() %>/Servizi" method="post">
            <h1>Richiesta nuovo servizio</h1>
            <hr/>
            <table>
                <tr>
                    <td><label for="name">Nome</label></td>
                    <td colspan="<%= licenseList.size() %>"><input type="text" id="name" name="name" placeholder="Nome servizio" required/></td>
                </tr>
                <tr>
                    <td><label for="date">Data</label></td>
                    <td colspan="<%= licenseList.size() %>"><input type="date" id="date" name="date" required/></td>
                </tr>
                <tr>
                    <td><label for="duration">Durata servizio</label></td>
                    <td colspan="<%= licenseList.size() %>"><input type="time" id="duration" name="duration" step="1800" required/></td>
                </tr>
                <tr>
                    <td rowspan="2">Patenti necessarie</td>
                    <% for (var license: licenseList) { %><td>
                        <label for="<%= license.getCategory() %>"><%= license.getCategory() %></label>
                        <input type="checkbox" name="license" id="<%= license.getCategory() %>" value="<%= license.getCategory() %>"/>
                    </td><% } %>
                </tr>
            </table>
            <br/>
            <div class="styled">
                <input type="button" id="addButton" value="Richiedi servizio"/>
            </div>
            <input type="hidden" name="action"/>
        </form>
    </body>
</html>
