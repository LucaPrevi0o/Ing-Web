<%@ page import="java.util.ArrayList" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.mo.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var bilList=(ArrayList<ServiceBill>)request.getAttribute("billList");
    if (bilList==null) bilList=new ArrayList<>();
%>
<%@ include file="/jsp/clientManager/welcome.jsp" %>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/generalStyle.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/checkbox.css">
        <script>
            window.addEventListener("load", function() {

                let detailsButtons=document.querySelectorAll("input[name='payBill']");
                let refreshButton=document.querySelector("#refreshButton");
                let backButton=document.querySelector("#backButton");

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="BillController.getBills";
                    document.dataForm.submit();
                });

                backButton.addEventListener("click", function() {

                    document.dataForm.action.value="LoginController.doLogin";
                    document.dataForm.submit();
                });

                detailsButtons.forEach(b => {

                    b.addEventListener("click", function() {

                        document.dataForm.action.value="BillController.commitPayment";
                        document.dataForm.code.value=this.id;
                        document.dataForm.submit();
                    });
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
                <td colspan="2" rowspan="2">Importo totale</td>
            </tr>
            <tr class="firstRow">
                <td>Nome</td>
                <td>Data</td>
                <td>Ora</td>
            </tr>
            <% for (var bill: bilList) { %>
                <tr>
                    <td><%= bill.getService().getName() %></td>
                    <td><%= bill.getService().getDate() %></td>
                    <td><%= bill.getService().getStartTime() %></td>
                    <td>€ <%= bill.getAmount() %></td>
                    <td><input type="button" id="<%= bill.getCode() %>" name="payBill" value="Procedi al pagamento"></td>
                </tr>
            <% } %>
        </table>
        <%@include file="/jsp/clientManager/footer.jsp"%>
    </body>
</html>