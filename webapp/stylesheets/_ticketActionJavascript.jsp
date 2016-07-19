<script type="text/javascript">
//this function quotes a message in the FCK editor
function insertTextIntoEditor(message) {
    var textAreaId = "ticketActionForm:actionMessage";
    var fckEditor = FCKeditorAPI.GetInstance(textAreaId);
    if (fckEditor == null) {
        alert("FCK editor [" + textAreaId + "] not found");
    } else {
	    fckEditor.InsertHtml(message);
    }
}
function insertResponse() {
	var element = document.getElementById("ticketActionForm:response");
	if (element != null) {
		eval("insertResponse"+element.value+"()");
		element.selectedIndex=0;
	}
}
fadeAndUnfade("ticketActionForm:mainButtonGroup", 1000, 2, 500);
function FCKeditor_OnComplete( editorInstance )
{
    editorInstance.Focus();
}
</script>
