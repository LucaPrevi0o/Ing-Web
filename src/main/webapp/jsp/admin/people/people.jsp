<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ingweb.main.aziendatrasporti.*" %>
<%
    var name=request.getParameter("name");
    var surname=request.getParameter("surname");
    var cf=request.getParameter("cf");
    var birthdate=request.getParameter("birthdate");
    var telNumber=request.getParameter("telNumber");
    var params=new String[]{name, surname, cf, birthdate, telNumber};

    if (params[0]!=null) { PeopleManager.addPerson(params); }

    var peopleList=PeopleManager.getPeopleList();
    var dataList=DatabaseQueryManager.getParams();
%>
<html>
    <head>
        <title>Lista autisti</title>
    </head>
    <body>
        <h1>Lista autisti</h1>
        <table>
            <tr><% for (var i: dataList) { %><td><%= i %></td><% } %></tr>
            <% for (var record: peopleList) { %>
            <tr><% for (var field: record) { %><td><%= field %></td><% } %></tr>
            <% } %>
        </table>
        <form action="people.jsp">
            <label for="name">Nome:</label>
            <input type="text" id="name" name="name" placeholder="Nome persona"><br/>
            <label for="surname">Cognome:</label>
            <input type="text" id="surname" name="surname" placeholder="Cognome persona"><br/>
            <label for="cf">Codice fiscale:</label>
            <input type="text" id="cf" name="cf" placeholder="CCCNNN00M00A000X"><br/>
            <label for="birthdate">Data di nascita:</label>
            <input type="date" id="birthdate" name="birthdate"><br/>
            <label for="telNumber">Numero telefonico:</label>
            <input type="tel" id="telNumber" name="telNumber" placeholder="+39 XXX-XXXXXXX"><br/>
            <input type="submit" id="addPerson" value="Aggiungi persona">
        </form>
    </body>
</html>
