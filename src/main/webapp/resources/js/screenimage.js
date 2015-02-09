/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

/**
 * 	This file contains all scripts to process Screen
 */

$(window).load(function() {
    var imageContainer = $("#imageContainer");

    if (imageContainer.find("img").size() > 0) {
        imageContainer.photoTagger({
            loadURL : $("#requestContextPath").text() + "/getFieldTags.action",
            getPhotoID : function(imageContainer) {
                return imageContainer.find("img").attr("name");
            },
            showPrompt : showCreateAssignOptions,
            viewTag : function openViewFieldInNewTab_proxy(id) {
            	openViewFieldInNewTab(id)
            },
            additionalTagBehavior : function() {
                $(this).contextMenu({
                    menu : "outlinerContextMenu"
                }, function(action, target, mousePosition) {
                    switch (action) {
                    case "viewField":
                        openViewFieldInNewTab(target.data("id"));
                        break;
                    case "removeFieldLink":
                        removeFieldLink(target);
                        break;
                    default:
                        alert(action);
                        break;
                    }
                });
            },
            beforeDrawPendingTag : function(photoTaggerObject) {
                photoTaggerObject.container.disableContextMenu();
                $(".contextMenu").hide();
            },
            beforeRemovePendingTag : function(photoTaggerObject) {
                photoTaggerObject.container.enableContextMenu();
            },
            activateOnHover : false
        });

        imageContainer.contextMenu({
            menu : "screenshotContextMenu"
        }, function(action, target, mousePosition) {
            switch (action) {
            case "drawMode":
                toOutlinerDrawMode();
                break;
            case "editMode":
                toOutlinerEditMode();
                break;
            default:
                alert(action);
                break;
            }
        });
    }

    toOutlinerViewMode();
});


/**
 * 
 */
function toOutlinerViewMode() {
	if ($("#hasSufficientRoles").text() == 'true') {
		$("#screenshotContextMenu").enableContextMenuItems("#editMode,#drawMode");
	}
	else {
		$("#screenshotContextMenu").disableContextMenuItems("#editMode,#drawMode");
	}

    $("#outlinerContextMenu").disableContextMenuItems("#removeFieldLink").enableContextMenuItems("#viewField");

    $("#imageContainer").photoTagger("viewMode");
    $("#imageContainer").css("cursor", "default");
}

/**
 * 
 */
function toOutlinerDrawMode() {
    $("#screenshotContextMenu").disableContextMenuItems("#drawMode").enableContextMenuItems("#editMode");

    $("#outlinerContextMenu").disableContextMenuItems("#removeFieldLink").enableContextMenuItems("#viewField");

    $("#imageContainer").photoTagger("drawMode");
    $("#imageContainer").css("cursor", "crosshair");
}

/**
 * 
 */
function toOutlinerEditMode() {
    $("#screenshotContextMenu").disableContextMenuItems("#editMode").enableContextMenuItems("#drawMode");

    $("#outlinerContextMenu").disableContextMenuItems("#viewField").enableContextMenuItems("#removeFieldLink");

    $("#imageContainer").photoTagger("editMode");
    $("#imageContainer").css("cursor", "default");
}

/**
 * 
 */
function showCreateAssignOptions() {
	var pendingTag = $("#imageContainer").data("photoTagger").pendingTag;
	
	// for create a new field
	PF('tagLeftPosition').jq.val(pendingTag.position().left);
	PF('tagTopPosition').jq.val(pendingTag.position().top);
	PF('tagWidth').jq.val(pendingTag.width());
	PF('tagHeight').jq.val(pendingTag.height());
	
	// for assign an existing field
	PF('tagAssignLeftPosition').jq.val(pendingTag.position().left);
	PF('tagAssignTopPosition').jq.val(pendingTag.position().top);
	PF('tagAssignWidth').jq.val(pendingTag.width());
	PF('tagAssignHeight').jq.val(pendingTag.height());
	
	PF('dlgCreateAssignFieldOption').show();
}

function createNewField() {
	PF('dlgCreateAssignFieldOption').hide();
	
	PF('dlgCreateField').show();
}

function assignExistingField() {
	PF('dlgCreateAssignFieldOption').hide();
	
	PF('dlgAssignField').show();
}

function onCreateAssignDlgHide() {
	$("#imageContainer").data("photoTagger").clearPendingTag();
	toOutlinerViewMode();
}

function onSaveCreateFieldCompletion(xhr, status, args) {
    if(args.validationFailed || !args.saved) {
    	// TODO: need to do anything?
    }
    else {
    	var dataReturn = args.data;
    	var data = dataReturn.split(',');
    	
    	$("#imageContainer").data("photoTagger").addTag(data[0], data[1], data[2], data[3], data[4], data[5]);
    	
    	PF('dlgCreateField').hide();
    }
}

function onSaveAssignFieldCompletion(xhr, status, args) {
	if(args.validationFailed || !args.saved) {
    	// TODO: need to do anything?
    }
    else {
    	var dataReturn = args.data;
    	var data = dataReturn.split(',');
    	
    	$("#imageContainer").data("photoTagger").addTag(data[0], data[1], data[2], data[3], data[4], data[5]);
    	
    	PF('dlgAssignField').hide();
    }
}

function openViewFieldInNewTab(fieldId) {
    window.open($("#requestContextPath").text() + "/pages/account/field/edit_field.xhtml?id=" + fieldId);
}

function removeFieldLink(outliner) {
    $.post($("#requestContextPath").text() + "/removeFieldLink.action", {
        "fieldId" : outliner.data("id")
    }, function(response) {
        if (response.success == true) {
            $("#imageContainer").data("photoTagger").deleteTag(outliner);
        } else {
            alert("The link is NOT deleted successfully. Please contact Adminstrator. The technical error is " + response.error);
        }
    });
}

function changeImage(object) {
	if ('' == object.value) return;
	
	if ('original' == object.value) {
		Pixastic.revert(screenshotImage);
	}
	else {
		Pixastic.process(screenshotImage, object.value);
	}
}

function changeField(field) {
	
}