<%@ page import="ingweb.main.aziendatrasporti.mo.Account" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var loggedAccount=(Account)request.getAttribute("loggedAccount");
    var selectedTab=request.getAttribute("selectedTab");
%>
<html>
    <head>
        <title>Home - <%= loggedAccount.getFullName().toUpperCase() %></title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/standard.css"/>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/nav.css"/>
        <script>
            window.addEventListener("load", function() {

                <% if (selectedTab!=null) { %> document.querySelector("#<%= selectedTab %>").classList.add("selected"); <% } %>
                document.querySelector("#workers").addEventListener("click", function() { document.querySelector("#action").value="WorkerDispatcher.getWorkers"; });
                //document.querySelector("#services").addEventListener("click", function() { document.querySelector("#action").value="ServiceDispatcher.getResources"; });
                document.querySelector("#trucks").addEventListener("click", function() { document.querySelector("#action").value="TruckDispatcher.getTrucks"; });
                document.querySelector("#clients").addEventListener("click", function() { document.querySelector("#action").value="ClientDispatcher.getClients"; });
                document.querySelector("#logout").addEventListener("click", function() { document.querySelector("#action").value="LoginDispatcher.logout"; });
            });
        </script>
    </head>
    <body>
        <h1>Benvenuto, <%= loggedAccount.getFullName() %></h1>
        <h2>Menu principale</h2>
        <nav>
            <form action="<%= request.getContextPath() %>/Dispatcher" method="post">
                <div class="styled">
                    <input type="submit" id="workers" value="Visualizza lista autisti">
                    <!--input type="submit" id="services" value="Visualizza lista servizi"-->
                    <input type="submit" id="trucks" value="Visualizza lista mezzi">
                    <input type="submit" id="clients" value="Visualizza lista clienti">
                    <input type="submit" id="logout" name="logout" value="Torna al login">
                </div>
                <input type="hidden" id="action" name="action">
            </form>
        </nav>
    </body>
</html>
