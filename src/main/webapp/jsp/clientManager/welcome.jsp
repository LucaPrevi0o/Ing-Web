<%@ page import="ingweb.main.aziendatrasporti.mo.mo.Account" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var loggedAccount=(Account)request.getAttribute("loggedAccount");
    var selectedTab=request.getAttribute("selectedTab");
%>
<html>
    <head>
        <title>Home - <%= loggedAccount.getFullName().toUpperCase() %></title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/generalStyle.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/navMenu.css">
        <script>
            window.addEventListener("load", function() {

                window.onscroll=function() {

                    if (document.body.scrollTop>150 || document.documentElement.scrollTop>150) document.querySelector("#documentElement").classList.add("sticky");
                    else if (document.querySelector("#documentElement").classList.contains("sticky")) document.querySelector("#documentElement").classList.remove("sticky");
                };

                <% if (selectedTab!=null) { %> document.querySelector("#<%= selectedTab %>").classList.add("selected"); <% } %>

                document.querySelector("#logout").addEventListener("click", function() {

                    document.querySelector("#action").value="LoginController.logout";
                    document.tabForm.submit();
                });

                document.querySelector("#services").addEventListener("click", function() {

                    document.querySelector("#action").value="AssignmentController.getAssignments";
                    document.tabForm.submit();
                });

                document.querySelector("#request").addEventListener("click", function() {

                    document.querySelector("#action").value="AssignmentController.openRequest";
                    document.tabForm.submit();
                });

                document.querySelector("#bills").addEventListener("click", function() {

                    document.querySelector("#action").value="BillController.getBills";
                    document.tabForm.submit();
                });

                document.querySelector("#settings").addEventListener("click", function() {

                    document.querySelector("#action").value="LoginController.manageProfile";
                    document.tabForm.submit();
                });
            });
        </script>
    </head>
    <body>
        <h1>Benvenuto, <%= loggedAccount.getFullName() %></h1>
        <h2>Menu di navigazione - Accesso clienti</h2>
        <% if (loggedAccount.getBankCoordinates()==null || loggedAccount.getBankCoordinates().isEmpty()) { %>
            <h3>ATTENZIONE: Coordinate bancarie personali (IBAN) non impostate.</h3>
            <h3>Procedere all'inserimento dei dati dal menu "Modifica profilo".</h3>
        <% } %>
        <nav id="documentElement">
            <form name="tabForm" action="<%= request.getContextPath() %>/Servizi" method="post">
                <div class="styled">
                    <input type="button" id="services" value="Lista servizi">
                    <input type="button" id="request" value="Richiedi servizio">
                    <input type="button" id="bills" value="Richieste pagamento">
                    <hr class="divhr" style="width: 67%">
                    <input type="button" id="logout" value="Torna al login">
                </div>
                <input type="hidden" id="action" name="action">
            </form>
        </nav>
        <% if (selectedTab==null) { %><h3>Seleziona una voce dal menu</h3><% } %>
    </body>
</html>
