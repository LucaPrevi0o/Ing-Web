<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.mo.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var bilList=(ArrayList<ServiceBill>)request.getAttribute("billList");
    if (bilList==null) bilList=new ArrayList<>();
%>
<%@ include file="/jsp/admin/welcome.jsp" %>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/generalStyle.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/checkbox.css">
        <script>
            window.addEventListener("load", function() {

                let refreshButton=document.querySelector("#refreshButton");
                let serviceListButton=document.querySelector("#addButton");
                let backButton=document.querySelector("#backButton");

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="BillController.getBills";
                    document.dataForm.submit();
                });

                backButton.addEventListener("click", function() {

                    document.dataForm.action.value="LoginController.doLogin";
                    document.dataForm.submit();
                });

                serviceListButton.addEventListener("click", function() {

                    document.dataForm.action.value="ServiceController.getServices";
                    document.dataForm.submit();
                });
            });
        </script>
    </head>
    <body>
        <hr/>
        <h1>Fatture di pagamento</h1>
        <table>
            <tr class="firstRow">
                <td colspan="3">Resoconto servizio</td>
                <td colspan="3">Dettagli cliente</td>
                <td rowspan="2">Importo totale</td>
            </tr>
            <tr class="firstRow">
                <td>Nome</td>
                <td>Data</td>
                <td>Ora</td>
                <td>Nome azienda</td>
                <td>Sede</td>
                <td>Responsabile</td>
            </tr>
            <% for (var bill: bilList) { %>
                <tr>
                    <td><%= bill.getService().getName() %></td>
                    <td><%= bill.getService().getDate() %></td>
                    <td><%= bill.getService().getStartTime() %></td>
                    <td><%= bill.getService().getClientCompany().getName() %> (<%= bill.getService().getClientCompany().getSocialReason() %>)</td>
                    <td><%= bill.getService().getClientCompany().getLocation() %></td>
                    <td><%= bill.getService().getClientCompany().getManagerName() %> (<%= bill.getService().getClientCompany().getManagerFiscalCode() %>)</td>
                    <td>€ <%= bill.getAmount() %></td>
                </tr>
            <% } %>
        </table>
        <%@include file="/jsp/admin/altFooter.jsp"%>
    </body>
</html>