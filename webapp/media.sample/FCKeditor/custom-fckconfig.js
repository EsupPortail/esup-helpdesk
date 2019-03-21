FCKConfig.ToolbarSets["actionMessage"] = [
	['Bold','Italic','TextColor','OrderedList','UnorderedList'],['Link','Unlink'],['PasteWord','Table','Smiley']

] ;

FCKConfig.ToolbarSets["faqContent"] = [
	['Undo','Redo'],
	['Cut','Copy','Paste','PasteText'],
	['FontFormat', 'Bold','Italic','Underline','StrikeThrough','-','Subscript','Superscript','TextColor','-','RemoveFormat'],
	['OrderedList','UnorderedList','-','Outdent','Indent'],
	['Link','Unlink'],
	['SpecialChar','Smiley','-','FitWindow','Source']
] ;




FCKConfig.Height = '500';
FCKConfig.SkinPath = FCKConfig.BasePath + 'skins/amu/' ;


FCKConfig.ProcessHTMLEntities = false ;
FCKConfig.IncludeLatinEntities = false ;
FCKConfig.IncludeGreekEntities = false ;

FCKConfig.ProtectedSource.Add( /<script[\s\S]*?<\/script>/gi ) ;
FCKConfig.ProtectedSource.Add( /<iframe[\s\S]*?<\/iframe>/gi ) ;
FCKConfig.ProtectedSource.Add( /<frame[\s\S]*?<\/frame>/gi ) ;

FCKConfig.EnterMode = 'br' ;			// p | div | br
FCKConfig.ShiftEnterMode = 'br' ;	// p | div | br

//FCKConfig.BrowserContextMenuOnCtrl = true ;
FCKConfig.BrowserContextMenu = true ;
FCKConfig.FirefoxSpellChecker   = true ;

FCKConfig.LinkDlgHideTarget = true;
FCKConfig.LinkUpload = false;
FCKConfig.LinkDlgHideAdvanced = true;

FCKConfig.FontFormats	= 'div;p;pre;h3;h4;h5;h6' ;





