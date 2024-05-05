<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ingweb.main.aziendatrasporti.AccountManager" %>
<%
    //get username/password from login page
    var username=request.getParameter("username");
    var password=request.getParameter("password");
    var loginInfo=new String[]{username, password};

    //get list of accounts
    var accountList=AccountManager.getAccountList(false);
    var adminList=AccountManager.getAccountList(true);
    for (var account : accountList) {
        for (var item: account) System.out.println(item);
    }

    //manage page redirection
    if (adminList.contains(loginInfo)) response.sendRedirect("../admin/schedule.jsp"); //redirect admin accounts to schedule page
    else if (accountList.contains(loginInfo)) response.sendRedirect("./login.jsp"); //refresh login for non-admin
    else response.sendRedirect("../../index.jsp"); //execute redirection
%>
