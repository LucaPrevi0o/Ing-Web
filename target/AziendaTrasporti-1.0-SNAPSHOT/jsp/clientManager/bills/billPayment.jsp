<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.mo.ServiceBill" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<% var serviceBill=(ServiceBill)request.getAttribute("serviceBill"); %>
<%@ include file="/jsp/clientManager/welcome.jsp" %>
<html>
    <head>
        <title>Pagamento fattura</title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/generalStyle.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/navMenu.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataForm.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/checkbox.css">
        <script>
            function submitForm() {

                let formData=document.querySelectorAll(":required");
                let j=0;
                for (let l=0; l<formData.length; l++) if (formData[l].value==="") j++;

                document.dataForm.action.value="BillController.confirmPayment";
                if (j===0) document.dataForm.submit();
            }

            window.addEventListener("load", function() {

                let addButton=document.querySelector("#addButton");
                let refreshButton=document.querySelector("#refreshButton");

                window.addEventListener("keydown", function(e) { if (e.key==="Enter") submitForm(); });

                refreshButton.addEventListener("click", function() {

                    document.dataForm.action.value="BillController.getBills";
                    document.dataForm.submit();
                });

                addButton.addEventListener("click", submitForm);
            });
        </script>
    </head>
    <body>
        <hr/>
        <form name="dataForm" action="<%= request.getContextPath() %>/Servizi" method="post">
            <h1>Procedi al pagamento</h1>
            <hr/>
            <table>
                <tr class="firstRow">
                    <td colspan="3">Riepilogo dati servizio</td>
                </tr>
                <tr>
                    <td colspan="2">Nome servizio</td>
                    <td><%= serviceBill.getService().getName() %></td>
                </tr>
                <tr>
                    <td rowspan="3">Dettagli cliente</td>
                    <td>Azienda richiedente</td>
                    <td><%= serviceBill.getService().getClientCompany().display() %></td>
                </tr>
                <tr>
                    <td>Sede</td>
                    <td><%= serviceBill.getService().getClientCompany().getLocation() %></td>
                </tr>
                <tr>
                    <td>Responsabile</td>
                    <td><%= serviceBill.getService().getClientCompany().getManagerName() %> (<%= serviceBill.getService().getClientCompany().getManagerFiscalCode() %>)</td>
                </tr>
                <tr>
                    <td colspan="2">Data</td>
                    <td><%= serviceBill.getService().getDate() %></td>
                </tr>
                <tr>
                    <td colspan="2">Ora inizio</td>
                    <td><%= serviceBill.getService().getStartTime() %></td>
                </tr>
                <tr>
                    <td colspan="2">Durata servizio</td>
                    <td><%= serviceBill.getService().getDuration() %></td>
                </tr>
            </table>
            <br/>
            <table>
                <tr class="firstRow">
                    <td colspan="2">Dettagli pagamento</td>
                </tr>
                <tr>
                    <td>Importo servizio</td>
                    <td><%= serviceBill.getAmount() %></td>
                </tr>
                <tr>
                    <td>Cordinate bancarie di destinazione</td>
                    <td><%= serviceBill.getDestinationBankCoords() %></td>
                </tr>
                <tr>
                    <td>Coordinate bancarie di pagamento</td>
                    <td><%= loggedAccount.getBankCoordinates()==null ? null : loggedAccount.getBankCoordinates() %></td>
                </tr>
            </table>
            <br/>
            <div class="styled">
                <input type="button" id="addButton" value="Procedi al pagamento"/>
                <input type="button" id="refreshButton" value="Torna alla lista fatture"/>
            </div>
            <input type="hidden" name="action" value=""/>
            <input type="hidden" name="code" value="<%= serviceBill.getCode()%>"/>
        </form>
    </body>
</html>
