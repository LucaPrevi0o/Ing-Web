<%@ page import="ingweb.main.aziendatrasporti.mo.Account" %>
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
                document.querySelector("#workers").addEventListener("click", function() {

                    document.querySelector("#action").value="WorkerController.getWorkers";
                    document.tabForm.submit();
                });

                 document.querySelector("#services").addEventListener("click", function() {

                    document.querySelector("#action").value="ServiceController.getServices";
                    document.tabForm.submit();
                });

                document.querySelector("#trucks").addEventListener("click", function() {

                    document.querySelector("#action").value="TruckController.getTrucks";
                    document.tabForm.submit();
                });

                document.querySelector("#clients").addEventListener("click", function() {

                    document.querySelector("#action").value="ClientController.getClients";
                    document.tabForm.submit();
                });

                document.querySelector("#logout").addEventListener("click", function() {

                    document.querySelector("#action").value="LoginController.logout";
                    document.tabForm.submit();
                });
            });
        </script>
    </head>
    <body>
        <h1>Benvenuto, <%= loggedAccount.getFullName() %></h1>
        <h2>Menu di navigazione</h2>
        <nav id="documentElement">
            <form name="tabForm" action="<%= request.getContextPath() %>/Servizi" method="post" class="styled">
                <input type="button" id="workers" value="Visualizza lista autisti">
                <input type="button" id="services" value="Visualizza lista servizi">
                <input type="button" id="trucks" value="Visualizza lista mezzi">
                <input type="button" id="clients" value="Visualizza lista clienti">
                <input type="button" id="logout" value="Torna al login">
                <input type="hidden" id="action" name="action">
            </form>
        </nav>
        <% if (selectedTab==null) { %><h3>Seleziona una voce dal menu</h3><% } %>
    </body>
</html>
