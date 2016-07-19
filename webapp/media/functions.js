var debug = false;

/**
* Simule a click for a link.
*/
function simulateLinkClick(linkId) {
	var fireOnThis = document.getElementById(linkId)
    if (fireOnThis == null) {
      if (debug) alert("element [" + linkId + "] not found");
      return;
    }
	if (document.createEvent) {
		// Firefox
		var evObj = document.createEvent('MouseEvents');
		evObj.initEvent( 'click', true, false );
		fireOnThis.dispatchEvent(evObj);
	} else if (document.createEventObject) {
		// IE
		fireOnThis.fireEvent('onclick');
		// IE 7
		fireOnThis.click();
	}
}

/** 
 * @return the body element of a table.
 */
function getTableBody(table) {
  var children = table.childNodes;
  for (i = 0; i < children.length; i++) {
    var child = children[i];
    var childTagName = child.tagName;
    if (childTagName != null && childTagName.toLowerCase() == "tbody") {
      return child;
    }
  }
  return null;
}

/**
* HighLight table rows.
*/
function highlightTableRows(tableId) {
    var table = document.getElementById(tableId); 
    if (table == null) {
      if (debug) alert("table [" + tableId + "] not found");
      return;
    }
    var tbody = getTableBody(table);
    if (tbody == null) {
      if (debug) alert("body of table [" + tableId + "] not found");
      return;
    }
    var rows = tbody.getElementsByTagName("tr");
    if (rows == null) {
      if (debug) alert("no row found in the body of table [" + tableId + "]");
      return;
    }
    var previousClass = null;
    // add event handlers so rows light up
    for (i = 0; i < rows.length; i++) {
        rows[i].onmouseover = function() { previousClass = this.className; this.className = 'portlet-table-selected'; };
        rows[i].onmouseout = function() { this.className = previousClass; };
    }
}


/**
* HighLight Li Tags.
* @param id the id of parent LI tags.
*/
function highlightChildrenLiTags(id) {
    var object = document.getElementById(id); 
    if (object == null) {
      if (debug) alert("the element [" + id + "] not found");
      return;
    }
    var liElements = object.getElementsByTagName("li");
    if (liElements == null) {
      if (debug) alert("no LI elements in [" + id + "]");
      return;
    }
    var previousClass = null;
    // add event handlers so rows light up
    for (i = 0; i < liElements.length; i++) {
        liElements[i].onmouseover = function() { previousClass = this.className; this.className = 'hover'; };
        liElements[i].onmouseout = function() { this.className = previousClass; };
    }
}

/*
 * Hide an element.
 */
function hideElement(id){
	var element = document.getElementById(id);
	if (element != null) {
  	  element.style.display = "none";
	} else if (debug) 
      alert("element [" + id + "] not found");
}

/*
 * Hide a button.
 */
function hideButton(id){
	var button = document.getElementById(id);
	if (button != null) {
  	  button.style.visibility = "hidden";
  	  button.style.display = "none";
  	  button.style.width = 0;
  	  button.style.height = 0;
	} else if (debug) 
      alert("button [" + id + "] not found");
}

/*
 * Show an element.
 */
function showElement(id){
	var element = document.getElementById(id);
	if (element != null) {
  	  element.style.display = "block";
	} else if (debug) 
      alert("element [" + id + "] not found");
}

/*
 * Show an element.
 */
function showHideElement(id){
	var element = document.getElementById(id);
	if (element != null) {
	  if (element.style.display == "none") {
  	    element.style.display = "block";
  	  } else {
  	    element.style.display = "none";
  	  }
	} else if (debug) 
      alert("element [" + id + "] not found");
}

/*
 * Hide elements in a table.
 */
function hideTableElements(tableId, elementId){
    var table = document.getElementById(tableId);
    if (table == null) {
      if (debug) alert("table [" + tableId + "] not found");
      return;
    }
    var tbody = getTableBody(table);
    if (tbody == null) {
      if (debug) alert("body of table [" + tableId + "] not found");
      return;
    }
    var rows = tbody.getElementsByTagName("tr");
    if (rows == null) {
      if (debug) alert("no row found in the body of table [" + tableId + "]");
      return;
    }
    // hide element for each row.
    for (i=0; i < rows.length; i++) {
      hideElement(tableId + ":" + i + ":" + elementId);
 	}
}

/*
 * Hide buttons in a table.
 */
function hideTableButtons(tableId, buttonId){
    var table = document.getElementById(tableId);
    if (table == null) {
      if (debug) alert("table [" + tableId + "] not found");
      return;
    }
    var tbody = getTableBody(table);
    if (tbody == null) {
      if (debug) alert("body of table [" + tableId + "] not found");
      return;
    }
    var rows = tbody.getElementsByTagName("tr");
    if (rows == null) {
      if (debug) alert("no row found in the body of table [" + tableId + "]");
      return;
    }
    // hide button for each row.
    for (i=0; i < rows.length; i++) {
      hideButton(tableId + ":" + i + ":" + elementId);
 	}
}

/*
 * Hide elements in a table.
 */
function hideTableButtons(tableId, buttonId){
	hideTableElements(tableId, buttonId);
}

/**
 * focus an element.
 */
function focusElement(id) {
	var element = document.getElementById(id);
	if (element != null) {
		element.focus();
	} else if (debug) {
      alert("element [" + id + "] not found, could not focus");
	}
}

/**
 * focus an fck editor.
 */
function focusFckEditor(id) {
	var element = FCKeditorAPI.GetInstance(id) ;
	if (element != null) {
		element.Focus();
	} else if (debug) {
		alert("FCK editor [" + id + "] not found, could not focus");
	}
}

/**
 * enable/disable an element.
 */
function enableElement(id, setEnabled) {
	var element = document.getElementById(id);
	if (element != null) {
		element.disabled = !setEnabled;
	} else if (debug) {
      alert("element [" + id + "] not found, could not enable/disable");
    }
}

/**
 * enable/disable an element.
 */
function isChecked(id) {
	var element = document.getElementById(id);
	if (element != null) {
		return element.checked;
	} else if (debug) {
      alert("element [" + id + "] not found");
    }
}

function getLeftPosition(theObject) {
	var object = theObject;
	var result = 0;
	while (object) {
	  result += object.offsetLeft;
	  object = object.offsetParent;
	}
	return result;
}

function moveRightInternal(elementId, rightPosition, step, delay) {
	var element = document.getElementById(elementId);
	var position = getLeftPosition(element);
	position += step;
	element.style.left = position;
	if (position < rightPosition) {
		setTimeout('moveRightInternal(\''+elementId+'\', '+rightPosition+', '+step+', '+delay+')', delay);
	}
}

function moveLeftInternal(elementId, leftPosition, step, delay) {
	var element = document.getElementById(elementId);
	var position = getLeftPosition(element);
	position -= step;
	element.style.left = position;
	if (position > leftPosition) {
		setTimeout('moveLeftInternal(\''+elementId+'\', '+leftPosition+', '+step+', '+delay+')', delay);
	}
}

function moveRightThenLeftInternal(elementId, offset, delay, step) {
	var element = document.getElementById(elementId);
	var position = getLeftPosition(element);
	var leftPosition = position;
	var rightPosition = position + offset;
	var stepRight = 10 * step;
	var stepLeft = step;
	moveRightInternal(elementId, rightPosition, stepRight, delay);
	moveLeftInternal(elementId, leftPosition, stepLeft, delay);
}

function moveRightThenLeft(elementId, offset, startDelay, moveDelay, step) {
	var element = document.getElementById(elementId);
	if (element != null) {
		setTimeout('moveRightThenLeftInternal(\''+elementId+'\', '+offset+', '+moveDelay+', '+step+')', startDelay);
	}
}
function setElementOpacity(element, opacity) {
	element.style.opacity = opacity/100;
	element.style.filter = 'alpha(opacity=' + opacity + ')';
}
function fadeAndUnfadeInternal(elementId, fade, delay, opacity, times) {
    var element = document.getElementById(elementId);
    if (element == null) {
    	return;
    }
    setElementOpacity(element, opacity);
    if (fade) {
	   	if (opacity == 0) {
	   		fade = false;
	    }
	} else {
	   	if (opacity == 100) {
	   		times--;
	   		if (times == 0) {
	   			return;
	   		}
	   		fade = true;
	    }
	}
    if (fade) {
    	opacity -= 10;
    } else {
    	opacity += 10;
    }
    setTimeout('fadeAndUnfadeInternal(\''+elementId+'\', '+fade+', '+delay+', '+opacity+', '+times+')', delay);
}
function fadeAndUnfade(elementId, duration, times, startDelay) {
    setTimeout('fadeAndUnfadeInternal(\''+elementId+'\', true, '+duration/20+', 90, '+times+')', startDelay);
}

function openPage(url, title, options) {
	var newWin = window.open(url, title, options);
	if (typeof newWin == 'undefined') { 
		newWin= window.open('', title, options); 
		try { 
			newWin.location.href = url; 
		} catch(e) { 
			alert("error: " + e + "\nYou may have to authorize popups for this site."); 
		} 
	} 
}

function openPage2(url, title, options, popupBlockerMessage) {
	var win = window.open(url, title, options);
	if (!win) { 
		alert(popupBlockerMessage);
	} 
}

function openDownload(url, popupBlockerMessage) {
	openPage2(url, '', '', popupBlockerMessage);
}

function returnFalse() {
	return false;
}

function onFormSubmit(form) {
	onFormSubmit(form, true, true, true);
}

function onFormSubmit2(form, freezeScreen, showText, showImage) {
	var body = document.body;
	body.onclick = returnFalse;
	if (freezeScreen) {
		body.style.opacity = 0.80;
		body.style.filter = 'alpha(opacity=60)'
	}
	if (showImage || showText) {
		var popupId = form.id+":submitPopup";
		var popup = document.getElementById(popupId);
		if (popup) {
			popup.style.visibility = 'visible';
			if (showImage) {
				var popupImageId = form.id+":submitPopupImage";
				var popupImage = document.getElementById(popupImageId);
				if (popupImage) {
					popupImage.style.visibility = 'visible';
					if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)) {
						// a hack for IE that does not animate GIFs once forms are submitted 
						setTimeout('var img = document.getElementById("'+popupImageId+'"); var src = img.src; img.src = ""; img.src = src;', 200); 
					}
				}
			}
			if (showText) {
				var popupTextId = form.id+":submitPopupText";
				var popupText = document.getElementById(popupTextId);
				if (popupText) {
					popupText.style.visibility = 'visible';
				}
			}
		}
	}
}
function isIe7() {
	return window.XMLHttpRequest && window.ActiveXObject;
}

