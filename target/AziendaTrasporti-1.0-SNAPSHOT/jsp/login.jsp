<%@ page contentType="text/html; charset=UTF-8" %>
<% var access=request.getAttribute("access"); %>
<!DOCTYPE html>
<html>
    <head>
        <title>Login</title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/style/loginForm.css">
        <script>
            window.addEventListener("load", function() {

                document.querySelector("#submitButton").addEventListener("click", function() { document.loginForm.submit(); });
            });
        </script>
    </head>
    <body>
        <h1>Login</h1>
        <h2>Gestione servizi - Portale principale</h2>
        <hr/>
        <% if (access!=null) { if (access.equals("denied")) { %>
            <h2>ACCESSO NON RIUSCITO</h2>
            <h3>La password inserita non è corretta.</h3>
        <% } else if (access.equals("not-permitted")) { %>
            <h2>ACCESSO NON RIUSCITO</h2>
            <h3>Accesso negato.</h3>
        <% } else if (access.equals("not-registered")) { %>
            <h2>ACCESSO NON RIUSCITO</h2>
            <h3>L'utente inserito non è registrato.</h3>
        <% } } %>
        <form name="loginForm" id="loginForm" action="<%= request.getContextPath() %>/Dispatcher" method="post">
            <h1>Accedi al portale</h1>
            <hr/>
            <input type="text" placeholder="Username" id="username" name="username" pattern="[a-z0-9]*" required><br/>
            <input type="password" placeholder="Password" id="password" name="password" pattern="[A-Za-z0-9]*" required><br/>
            <input type="button" id="submitButton" value="Accedi"><br/>
            <input type="hidden" name="action" value="LoginDispatcher.validate">
        </form>
    </body>
</html>
