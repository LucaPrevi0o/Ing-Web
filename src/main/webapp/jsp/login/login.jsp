<%@ page contentType="text/html; charset=UTF-8" %>

<html>
    <head>
        <title>Login</title>
        <link href="../../style/login.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <h1>Login</h1>
        <form id="loginForm" action="./loginManager.jsp" method="get">
            <label for="username">Username: </label>
            <input type="text" placeholder="Username" id="username" name="username" required>
            <br/>
            <label for="password">Password: </label>
            <input type="password" placeholder="Password" id="password" name="password" required>
            <br/>
            <input id="submitButton" type="submit" value="Accedi">
        </form>
    </body>
</html>
