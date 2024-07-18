<nav class="footer">
    <form name="dataForm" action="<%= request.getContextPath() %>/Servizi" method="post">
        <div class="styled">
            <input type="button" id="refreshButton" value="Aggiorna lista">
            <input type="button" id="backButton" value="Chiudi tab">
            <hr class="divhr" style="width: 75%">
            <input type="button" id="settings" value="Modifica profilo">
        </div>
        <input type="hidden" name="code">
        <input type="hidden" name="action">
    </form>
</nav>