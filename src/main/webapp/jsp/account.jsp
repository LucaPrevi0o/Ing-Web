<%@ page import="ingweb.main.aziendatrasporti.mo.mo.Account" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<% var loggedAccount=(Account)request.getAttribute("loggedAccount"); %>
<html>
    <head>
        <title>Modifica profilo</title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/generalStyle.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/navMenu.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataTable.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/dataForm.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/checkbox.css">
        <script>
            window.addEventListener("load", function() {

                document.querySelector("#backButton").addEventListener("click", function() {

                    document.querySelector("#action").value="LoginController.doLogin";
                    document.dataForm.submit();
                });

                document.querySelector("#confirmSettings").addEventListener("click", function() {

                    document.querySelector("#action").value="LoginController.editAccount";
                    document.dataForm.submit();
                });
            });
        </script>
    </head>
    <body>
        <form name="dataForm" action="<%= request.getContextPath() %>/Servizi" method="post">
            <h1>Modifica informazioni profilo</h1>
            <hr/>
            <table>
                <tr>
                    <td colspan="2"><label for="username">Username</label></td>
                    <td><input type="text" id="username" name="username" placeholder="Modifica username" value="<%= loggedAccount.getUsername() %>" required></td>
                </tr>
                <tr>
                    <td colspan="2"><label for="password">Password</label></td>
                    <td><input type="password" id="password" name="password" placeholder="Modifica password" value="<%= loggedAccount.getPassword() %>" required></td>
                </tr>
                <tr>
                    <td rowspan="2">Informazioni personali</td>
                    <td><label for="name">Nome completo</label></td>
                    <td><input type="text" id="name" name="name" placeholder="Nome" value="<%= loggedAccount.getFullName() %>" required></td>
                </tr>
                <tr>
                    <td>Livello di accesso</td>
                    <td><%= loggedAccount.getLevel()==Account.ADMIN_LEVEL ? "Amministratore" : (loggedAccount.getLevel()==Account.MANAGER_LEVEL ? "Cliente" : (loggedAccount.getLevel()==Account.WORKER_LEVEL ? "Dipendente" : "---")) %></td>
                </tr>
            </table>
            <br/>
            <div class="styled">
                <input type="button" id="confirmSettings" value="Conferma">
                <input type="button" id="backButton" value="Torna alla home">
            </div>
            <input type="hidden" id="action" name="action">
        </form>
    </body>
</html>
