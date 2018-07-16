jQuery(function ($) {
    //collapsible fieldset
    $("fieldset.collapsible").collapsible();

    $("fieldset.collapsible").collapsible({
        collapsed: true
    });


    //nice select
    $(document).ready(function () {
        $('.form-item select.nice ').niceSelect();
    });

    showHideElementById = function (strId) {
        id = '#' + strId.replace(/:/gi, '\\:');
        $(id).toggle();
    };

    showHideElementByClass = function (cssClass) {
        $(cssClass).toggle();
    };


    buttonClick = function (linkId) {
        id = '#' + linkId.replace(/:/gi, '\\:');
        $(id).trigger('click');
    };

    selectChange = function (selectId, val) {
        id = '#' + selectId.replace(/:/gi, '\\:');
        $(id).val(val);
        $(id).trigger('change');
        //$(id).change();
    };


    storeInSession = function (itemKey, itemValue) {
        $.cookie(itemKey, itemValue);
    };


    $('input[type=\'submit\']').click(function (e) {
        e.stopImmediatePropagation();
    });


    highligh = function (container, what) {
        var newtext = what.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&');
        var test = new RegExp(newtext, 'gi');
        return ( container.text().replace(test, '<span class="highligh">$&</span>'));
    };

    //Filter category Form
    var $FilterInput = $("#ticketActionForm\\:filtreTree");
    var $FilterCancel = $("#ticketActionForm\\:cancelFilterTreeButton");
    if ($FilterInput.length > 0) {
        if ($FilterInput.val().length === 0) {
            $FilterCancel.css('visibility', 'hidden');
        }
        else {
            $FilterCancel.css('visibility', 'visible');
        }
    }

    //---------------------------------Active Tab---------------------------------
    var dataStore = window.sessionStorage;
    var activeTab = null;
    var wrapper_id = "#" + $(".portlet-section-body").children("div:first").attr("id");

    try {
        activeTab = dataStore.getItem(wrapper_id);

        if (activeTab) {
            if ($(activeTab).length) {
                if ($(activeTab).parent().hasClass('without-state') == false) {
                    $('.tabs .tab-link').removeClass('current');
                    $('.tab-content').removeClass('current');
                    $(activeTab).addClass('current');
                    $(activeTab.replace(":", ":tab-")).addClass('current');
                }
            }
        }
    } catch (e) {
        console.log(e);
    }

    $('.tabs .tab-link').click(function () {
        if ($(this).attr('id') && $(".portlet-section-body").children("div:first").attr("id")) {

            var wrapper_id = "#" + $(".portlet-section-body").children("div:first").attr("id");
            var tab_id = $(this).attr('id');
            var tab_content = "tab-" + tab_id.substring(tab_id.indexOf(":") + 1);
            $('.tabs .tab-link').removeClass('current');
            $('.tab-content').removeClass('current');
            $(this).addClass('current');
            $("[id$=" + tab_content + "]").addClass('current');
            if ($(this).hasClass('without-state') == false) {
                dataStore.setItem(wrapper_id, "#" + tab_id.replace(":", "\\:"));
            }

        }
    });

    //treeview
    $(".treeview .leaf").each(function () {
        $(this).addClass("collapsed");
    });



    $(".category.leaf.last").each(function () {
        var prevcellImg = $(this).parent().prev().find('img[onclick]');
        if(prevcellImg.length > 0){
            var onClickValue = prevcellImg.attr('onclick');
            $(this).addClass('no-last');
            var parentTable= $(this).closest('table').clone().addClass("clone");
            $(this).removeClass('last');
            $(this).addClass('parent');
            $(this).attr('onclick',onClickValue);
            $(this).closest('table').next().prepend(parentTable);
        }else{
            $(this).parent().prepend("<span class=\"\">-</span>");
        }

    });

    $(".clone tr").each(function () {
        $(this).prepend("<td width='19'></td>");
    });

    $(".clone tr td:last-of-type .last").each(function () {
        $(this).before("<span class=\"\">-</span>");
    });

    $(".category.leaf.parent").each(function () {
        $(this).parent().prepend("<span>></span>");
    });

    $('.treeview .leaf').click(function (e) {
        var $this = $(this);
        if ($this.hasClass("collapsed")) {
            $this.removeClass("collapsed");
            $this.addClass("expanded");
        }
        else if ($this.hasClass("expanded")) {
            $this.removeClass("expanded");
            $this.addClass("collapsed");
        }
    });

    $(".treeview .department").each(function () {
        var $parentRow = $(this).parent().parent();
        $parentRow.append("<td class=\"extra-cell\"><div><i class=\"fas fa-chevron-down\"></i></div></td>");
    });

    //Highligh text
    if ($("#ticketActionForm\\:filtreTree").length > 0) {
        if ($("#ticketActionForm\\:filtreTree").val().length > 0) {
            $(".treeview .leaf .portlet-section-text").each(function () {
                $(this).html(highligh($(this), $("#ticketActionForm\\:filtreTree").val()));
            });
        }
    }


    //FAQ
    $("#faqsForm\\:tree .parentLeaf").each(function () {
        var $this = $(this);
        var $parentRow = $this.parent().parent().parent();
        var imgNav = $parentRow.find("img[id*=faqsForm]");
        var $onClickImgAttr = imgNav.attr('onclick');
        $this.attr("onclick", $onClickImgAttr);
        $this.css("cursor", "pointer");
    });


    //----------------------Form actions list-------------------------------//
    $('.form-select .actions-header').click(function (e) {
        var $id_block = '#' + $(this).parent().attr('id').replace(':', '\\:');
        $($id_block + ' .actions-list').toggleClass('hideme');
    });

    $('.form-select.actions').mouseleave(function (e) {
        var $id_block = '#' + $(this).attr('id').replace(':', '\\:');
        $($id_block + ' .actions-list').addClass('hideme');
    });

    //masquer select si aucune action disponible
    $(".actions-list-inner").each(function () {
        if ($(this).children().length == 0) {
            $(this).parent().parent().addClass("hideme");
        }
    });

    //GOTO TICKET
    $(".form-goto-ticket  input[type='text']").each(function () {
        $ticketNumberValue = "N° de ticket";
        if ($(this).prop("value") == "") {
            $(this).prop("value", $ticketNumberValue);
        }
        $(this).focus(function () {
            if ($(this).prop("value") == $ticketNumberValue) {
                $(this).prop("value", "");
            }
        });

        $(this).mouseleave(function () {
            if ($(this).prop("value") == "") {
                $(this).prop("value", $ticketNumberValue);
            }
        });
    });


    // Add title on delete bookmark hover
    $(".bookmarks .fa-trash-alt").each(function () {
        $(this).attr("title", "Supprimer le signet");
    });


    //------------------------ticket history---------------------------//
    //hide if no content
    $(".view-ticket_history .action-message").each(function () {
        if ($(this).text().length < 2) {
            $(this).parent().addClass("hideme");
        }
    });

    $(".action--header").each(function () {
        var actionDate = $(this).text().split(':')[0] + ":" + $(this).text().split(':')[1];
        var actionTitle = $(this).text().split(':')[2];
        $(this).text("");
        $(this).wrapInner("<span class=\"action--date\">" + actionDate + "</span><span class=\"action--title\">" + actionTitle + "</span>");
    });

    $('.history-container .action-scope-edit').click(function () {
        $(this).toggle();
        $actionForm = $(this).parent().parent().find('.form-action');
        $actionForm.toggle();

    });

    $('.history-container .action--scope .cancel').click(function () {
        $action = $(this).parent().parent().parent().find('.action-scope-edit');
        $action.toggle();
    });


    //history view more
    $(".history-container thead").each(function () {
        $(this).addClass('hideme');
    });

    if ($.cookie('view-ticket-history') != null) {

        switch ($.cookie('view-ticket-history')) {
            case 'view-more':
                $(".view-ticket_history .view-less").addClass("hideme");
                $(".view-ticket_history .view-more").removeClass("hideme");
                $(".history-container .col-main").removeClass('view--full');
                break;
            case 'view-less':
                $(".view-ticket_history .view-less").removeClass("hideme");
                $(".view-ticket_history .view-more").addClass("hideme");
                $(".history-container .col-main").addClass('view--full');
                viewMoreLessProcess();
                break;
        }
    }

    $('.view-ticket_history .view-more').click(function () {
        $.cookie('view-ticket-history', 'view-less');
        $(this).toggleClass("hideme");
        $(".view-ticket_history .view-less").toggleClass("hideme");

        $(".history-container .col-main").addClass('view--full');
        viewMoreLessProcess();
    })

    $('.view-ticket_history .view-less').click(function () {
        $.cookie('view-ticket-history', 'view-more');
        $(this).toggleClass("hideme");
        $(".view-ticket_history .view-more").toggleClass("hideme");
        $(".history-container .col-main").removeClass('view--full');
        viewMoreLessProcess();
    });

    function viewMoreLessProcess() {
        $(".history-container thead").toggleClass("hideme");
        $(".history-container .portlet-table-header > span").toggleClass("hideme");
        $(".history-container .col-option").toggleClass('hideme');
    }

    // History date et comment
    $(".action--header").each(function () {
        $this = $(this);
        content = $this.next();
        title = $this.find(".action--title");
        content.appendTo(title);
    });


    //view alert
    $('.action-show-alert ').click(function () {
        $(this).next().toggleClass("hideme");
    });

    //ControlPanel Show - Hide actions
    $('.ticketControlPanel-item-actions-header-wrapper').click(function () {
        $(this).next().toggleClass('hideme');
    });

    $('.ticketControlPanel-item-actions').mouseleave(function () {
        $(this).find('.ticketControlPanel-item-actions-wrapper').addClass('hideme');
    });

    $('.actions-header-wrapper').click(function () {
        $(this).next().toggleClass('hideme');
    });

    $('.actions-wrapper').mouseleave(function () {
        $(this).find('.actions-list-wrapper').addClass('hideme');
    });


    //----------------------------dashboard-----------------------------------//


    //rewrite cell content
    $(".dashboard .oddRow td > span, .dashboard .evenRow td > span").each(function () {
        var $cellContent = $(this).text();
        if ($cellContent.indexOf('(') > 0) {
            var textToRemove = "(" + $cellContent.slice($cellContent.indexOf('(') + 1, $cellContent.lastIndexOf(')')) + ")";
            $(this).text($cellContent.replace(textToRemove, ''));
        }
    });
    //Status and priority
    $(".dashboard .ticket-closed ").each(function () {
        $(this).removeClass("ticket-closed");
        $(this).parent().addClass("ticket-closed");
    });


    //select scrollable
    $(".form-select.scrollable select").each(function () {
        $(this).attr("size", "8");
    });


    //Move ticket
    $("#ticketActionForm\\:showCategoriesTreeview").click(function () {
        $(this).parent().toggleClass('current');
        $("#ticketActionForm\\:showCategoriesRecent").parent().removeClass("current");
        $("#ticketActionForm\\:recentCategoriesList").addClass("hideme");
        $("#ticketActionForm\\:categoriesList").removeClass("hideme");
        $("#ticketActionForm\\:tree").removeClass("hideme");

    });

    $("#ticketActionForm\\:showCategoriesRecent").click(function () {
        $(this).parent().toggleClass('current');
        $("#ticketActionForm\\:showCategoriesTreeview").parent().removeClass("current");
        $("#ticketActionForm\\:recentCategoriesList").removeClass("hideme");
        $("#ticketActionForm\\:categoriesList").addClass("hideme");
        $("#ticketActionForm\\:tree").addClass("hideme");
    });

    //Invite users to ticket
    $(document).ready(function () {
        $("#ticketActionForm\\:ldap-search-button").each(function () {
            if (($.cookie('invite-user-select') != 'null')) {
                $(this).addClass("hideme");
                $("#ticketActionForm\\:inviteComment").removeClass('hideme');
                $("#ticketActionForm\\:ldapUid-label").removeClass('hideme');
            }
        });
    });

    $("#viewTicketForm\\:inviteButton").click(function () {
        //$.cookie('invite-user-select', null);
    });

    $("#ticketActionForm\\:showLdapForm").click(function () {
        clearInviteMenuProcess($(this));
        uidsStateProcess("show clear");

        $("#ticketActionForm\\:recentInvitationList").addClass('hideme');
        $("#ticketActionForm\\:inviteTree").addClass('hideme');
        $("#ticketActionForm\\:about-uids").addClass('hideme');
        $("#ticketActionForm\\:inviteComment").addClass('hideme');
        $("#ticketActionForm\\:ldapUid-label").addClass('hideme');
        $("#ticketActionForm\\:ldap-search-button").removeClass('hideme');
        $.cookie('invite-user-select', null);

    });

    $("#ticketActionForm\\:showUIDs").click(function () {
        clearInviteMenuProcess($(this));
        uidsStateProcess("show clear");
        $("#ticketActionForm\\:recentInvitationList").addClass('hideme');
        $("#ticketActionForm\\:inviteTree").addClass('hideme');
        $("#ticketActionForm\\:about-uids").toggleClass('hideme');
        $("#ticketActionForm\\:inviteComment").removeClass('hideme');
        $("#ticketActionForm\\:ldapUid-label").removeClass('hideme');
        $("#ticketActionForm\\:ldap-search-button").addClass('hideme');
        $.cookie('invite-user-select', null);
    });

    $("#ticketActionForm\\:showInviteTree").click(function () {
        $("#ticketActionForm\\:inviteTree").toggleClass('hideme');
        $(".ticketInvite #ticketActionForm\\:tree").show();
        $("#ticketActionForm\\:recentInvitationList").addClass('hideme');
        $("#ticketActionForm\\:about-uids").addClass('hideme');
        $("#ticketActionForm\\:inviteComment").addClass('hideme');
        $("#ticketActionForm\\:ldapUid-label").removeClass('hideme');
        $("#ticketActionForm\\:ldap-search-button").addClass('hideme');
        $.cookie('invite-user-select', null);
        clearInviteMenuProcess($(this));
        uidsStateProcess('clear hide');
    });

    $("#ticketActionForm\\:tree .leaf").click(function () {
        uidsStateProcess("show");
        $(".ticketInvite #ticketActionForm\\:tree").hide();
    });

    $("#ticketActionForm\\:showRecentInvitations").click(function () {
        $("#ticketActionForm\\:recentInvitationList").toggleClass('hideme');
        $("#ticketActionForm\\:inviteTree").addClass('hideme');
        $("#ticketActionForm\\:about-uids").addClass('hideme');
        $("#ticketActionForm\\:inviteComment").addClass('hideme');
        $("#ticketActionForm\\:ldapUid-label").removeClass('hideme');
        $("#ticketActionForm\\:ldap-search-button").addClass('hideme');
        $.cookie('invite-user-select', null);
        clearInviteMenuProcess($(this));
        uidsStateProcess('clear hide');
    });

    $("#ticketActionForm\\:addInvitation").change(function () {
        uidsStateProcess("show");
    });

    $("#ticketActionForm\\:inviteComment input[type='submit']").click(function () {
        $.cookie('invite-user-select', null);
    });


    function clearInviteMenuProcess($current) {
        $current.parent().toggleClass('current');
        $(".ticketInvite .form-menu .form-item > span").each(function () {
            if ($(this).attr('id') != $current.attr('id')) $(this).parent().removeClass('current');
        });
    }

    function uidsStateProcess($action) {
        switch ($action) {
            case 'clear hide':
                $("#ticketActionForm\\:ldapUid").val('');
                $("#ticketActionForm\\:ldapUid").parent().parent().addClass('hideme');

                break;
            case 'show':
                $("#ticketActionForm\\:ldapUid").parent().parent().removeClass('hideme');
                $("#ticketActionForm\\:inviteComment").removeClass('hideme');
                break;
            case 'clear':
                $("#ticketActionForm\\:ldapUid").val('');
                break;
            case 'show clear':
                $("#ticketActionForm\\:ldapUid").parent().parent().removeClass('hideme');
                $("#ticketActionForm\\:inviteComment").removeClass('hideme');

                $("#ticketActionForm\\:ldapUid").val('');
                break;
        }

    }

    //LDAP search result


/*    $('.search-result-data .user-select').click(function (e) {
        $.cookie('invite-user-select', "true");
    });*/


    $(".ldap-search-result .user-data ").each(function () {
        $data = $(this).text().replace(/[\r\n]+(?=[^\r\n])/g, '').split("|");
        $displayName = $data[searchStringInArray("displayName", $data)].match(/{(.*)}/).pop();


        try {
            $composante = $data[searchStringInArray("amuComposante", $data)].match(/{(.*)}/).pop();
        } catch (error) {
            $composante = "nc";
        }


        try {
            $eduPersonAffiliation = $data[searchStringInArray("eduPersonAffiliation", $data)].match(/{(.*)}/).pop();
            if ($eduPersonAffiliation.indexOf("student") > 0) {
                $eduPersonAffiliation = "Etudiant";
            } else if ($eduPersonAffiliation.indexOf("alumn") > 0) {
                $eduPersonAffiliation = "Alumni";
            } else {
                $eduPersonAffiliation = "Personnel";
            }
        } catch (error) {
            $userStatus = "nc";
        }
        $(this).next().find(".user--display-name").text($displayName);
        $(this).next().find(".user--status").html("<span>Statut : " + $eduPersonAffiliation + "</span>");
        $(this).next().find(".user--composante").html("<span>Composante : " + $composante.toUpperCase() + "</span>");

    });

    function searchStringInArray(str, strArray) {
        for (var j = 0; j < strArray.length; j++) {
            if (strArray[j].match(str)) return j;
        }
        return -1;
    }

    //Copy CAS ticket adress to clip board

    $(".view-ticket_secondary_properties .copy").click(function () {
        var $temp = $("<input>");
        $("body").append($temp);
        $temp.val($("#viewTicketForm\\:ticketCasLink").text()).select();
        document.execCommand("copy");
        $temp.remove();
    });

    //Add title on action quote
    $(".action--quote").each(function () {
        $(this).attr("title", "Citer le texte de ce commentaire");
    });

    $(".action--quote").click(function (e) {
        e.preventDefault();
        $('html, body').animate({scrollTop: 0}, '300');
    });

    //Sticky header
    $(window).scroll(function () {
        var sticky = $('.sticky'),
            scroll = $(window).scrollTop();
        if (scroll >= 100) sticky.addClass('fixed');
        else sticky.removeClass('fixed');
    });

    //manager preference
    $("#managerPreferences .selectBooleanCheckbox, #managerPreferences .selectOneMenu").change(function () {
        $submit = $(this).closest("fieldset").find(".form-submit input[type=\'submit\']");
        $submit.addClass('button--primary');
    });

    //responses
    $(".responses .response-label .link").click(function () {
        $(this).parent().find('.response-content').toggleClass('hideme');
        $(this).parent().find('.form-block').toggleClass('hideme');
    });
});
