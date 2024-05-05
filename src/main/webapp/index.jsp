<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Home page</title>
    <script>
        window.addEventListener("load", function() {

            let b=document.querySelector("#goLogin");
            b.addEventListener("click", function() { window.location.href="jsp/login/login.jsp"; });
        });
    </script>
</head>
<body>
<h1>Sito attualmente in costruzione</h1>
<p>Questo sito è attualmente in costruzione.<br/>Sarà disponibile prima o poi :)</p>
<input type="button" id="goLogin" value="Vai al login">
</body>
</html>
