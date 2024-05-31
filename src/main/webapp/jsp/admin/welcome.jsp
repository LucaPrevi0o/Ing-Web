<%@ page import="ingweb.main.aziendatrasporti.mo.Account" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var loggedAccount=(Account)request.getAttribute("loggedAccount");
    var selectedTab=request.getAttribute("selectedTab");
%>
<html>
    <head>
        <title>Home - <%= loggedAccount.getFullName().toUpperCase() %></title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/navMenu.css">
        <script>
            window.addEventListener("load", function() {

                <% if (selectedTab!=null) { %> document.querySelector("#<%= selectedTab %>").classList.add("selected"); <% } %>
                document.querySelector("#workers").addEventListener("click", function() {

                    document.querySelector("#action").value="WorkerDispatcher.getWorkers";
                    document.tabForm.submit();
                });

                document.querySelector("#trucks").addEventListener("click", function() {

                    document.querySelector("#action").value="TruckDispatcher.getTrucks";
                    document.tabForm.submit();
                });

                document.querySelector("#clients").addEventListener("click", function() {

                    document.querySelector("#action").value="ClientDispatcher.getClients";
                    document.tabForm.submit();
                });

                document.querySelector("#logout").addEventListener("click", function() {

                    document.querySelector("#action").value="LoginDispatcher.logout";
                document.tabForm.submit();
                });
            });
        </script>
    </head>
    <body>
        <h1>Benvenuto, <%= loggedAccount.getFullName() %></h1>
        <h2>Menu principale</h2>
        <nav>
            <form name="tabForm" action="<%= request.getContextPath() %>/Dispatcher" method="post">
                <input type="button" id="workers" value="Visualizza lista autisti">
                <!--input type="submit" id="services" value="Visualizza lista servizi"-->
                <input type="button" id="trucks" value="Visualizza lista mezzi">
                <input type="button" id="clients" value="Visualizza lista clienti">
                <input type="button" id="logout" value="Torna al login">
                <input type="hidden" id="action" name="action">
            </form>
        </nav>
    </body>
</html>
