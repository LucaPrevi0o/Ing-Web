<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ingweb.main.aziendatrasporti.mo.mo.Service" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<% var service=(Service)request.getAttribute("service"); %>
<%@ include file="/jsp/admin/welcome.jsp" %>
<html>
    <head>
        <title>Nuova fattura</title>
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

                document.dataForm.action.value="BillController.addBill";
                if (j===0) document.dataForm.submit();
            }

            window.addEventListener("load", function() {

                let addButton=document.querySelector("#addButton");
                let refreshButton=document.querySelector("#refreshButton");

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
            <h1>Fattura di pagamento</h1>
            <hr/>
            <table>
                <tr class="firstRow">
                    <td colspan="3">Riepilogo dati servizio</td>
                </tr>
                <tr>
                    <td colspan="2">Nome servizio</td>
                    <td><%= service.getName() %></td>
                </tr>
                <tr>
                    <td rowspan="3">Dettagli cliente</td>
                    <td>Azienda richiedente</td>
                    <td><%= service.getClientCompany().display() %></td>
                </tr>
                <tr>
                    <td>Sede</td>
                    <td><%= service.getClientCompany().getLocation() %></td>
                </tr>
                <tr>
                    <td>Responsabile</td>
                    <td><%= service.getClientCompany().getManagerName() %> (<%= service.getClientCompany().getManagerFiscalCode() %>)</td>
                </tr>
                <tr>
                    <td colspan="2">Data</td>
                    <td><%= service.getDate() %></td>
                </tr>
                <tr>
                    <td colspan="2">Ora inizio</td>
                    <td><%= service.getStartTime() %></td>
                </tr>
                <tr>
                    <td colspan="2">Durata servizio</td>
                    <td><%= service.getDuration() %></td>
                </tr>
            </table>
            <br/>
            <table>
                <tr class="firstRow">
                    <td colspan="2">Dettagli pagamento</td>
                </tr>
                <tr>
                    <td><label for="amount">Importo servizio</label></td>
                    <td><input id="amount" name="amount" type="number" placeholder="€ 100.00" step="0.01" min="0.0" required></td>
                </tr>
                <tr>
                    <td><label for="bankCoordinates">Coordinate bancarie di destinazione</label></td>
                    <td><input id="bankCoordinates" name="bankCoordinates" type="text" placeholder="Coordinate (IBAN)" value="<%= loggedAccount.getBankCoordinates()==null ? null : loggedAccount.getBankCoordinates() %>" required></td>
                </tr>
            </table>
            <br>
            <div class="styled">
                <input type="button" id="addButton" value="Invia fattura"/>
                <input type="button" id="refreshButton" value="Torna alla lista servizi"/>
            </div>
            <input type="hidden" name="action" value=""/>
            <input type="hidden" name="code" value="<%= service.getCode() %>"/>
        </form>
    </body>
</html>
