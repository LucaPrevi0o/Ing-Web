<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ingweb.main.aziendatrasporti.AccountManager" %>
<%

    var pageName="";

    //get username/password from login page
    var username=request.getParameter("username");
    var password=request.getParameter("password");
    var loginInfo=new String[]{ username, password };

    //get list of accounts
    var accountList=AccountManager.getAccountList(false);
    var adminList=AccountManager.getAccountList(true);

    System.out.println("Account list:");
    for (var account : accountList) {

        System.out.print("new item: ");
        for (var item: account) System.out.print(item+" ");
        System.out.println();
    }

    System.out.println("\nAdmin list:");
    for (var account : adminList) {

        System.out.print("new item: ");
        for (var item: account) System.out.print(item+" ");
        System.out.println();
    }

    System.out.println("\nParameters: ");
    for (var item: loginInfo) System.out.print(item+" ");
    System.out.println();

    //manage page redirection
    for (var account: adminList)
        if (account.length==loginInfo.length)
            for (var i=0; i<account.length; i++)
                if (account[i].equals(loginInfo[i])) {
                    pageName = "./schedule.jsp";
                    break;
                }

    if (pageName.isEmpty())
        for (var account: accountList)
            if (account.length==loginInfo.length)
                for (var i=0; i<account.length; i++)
                    if (account[i].equals(loginInfo[i])) {
                        pageName = "./login.jsp";
                        break;
                    }

    if (pageName.isEmpty()) {

        System.out.println("Account does not exist");
        pageName="../index.jsp";
    }
    response.sendRedirect(pageName);
%>
