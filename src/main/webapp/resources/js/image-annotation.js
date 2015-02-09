function editFieldInfo(event, tagCanvasSelector, editFieldFormTemplateUrl) {
    event.preventDefault();
    
    var requestContextPath = $("span#requestContextPath").text();

    var tagDiv = $(tagCanvasSelector).data("currentTag");

    $.get(requestContextPath + editFieldFormTemplateUrl + "?fieldId=" + tagDiv.attr("name"), function(response) {
        var fieldDialog = $(response);
        
        fieldDialog.data("enabledTextFields", fieldDialog.find("input:enabled[type='text'][name$='Other']"));
        
        $("form input[type='reset']", fieldDialog).click(function(event) {
            fieldDialog.data("enabledTextFields").each(function() {
                $(this).removeAttr("disabled");
            });
        });

        $("form input[type='submit']", fieldDialog).click(function(event) {
            event.preventDefault();
            
            var editForm = $("#createOrEditFieldFormForScreen", fieldDialog);

            editForm.append(createHiddenInputField("id", tagDiv.attr("name")));
            editForm.append(createHiddenInputField("tagLeftPosition", tagDiv.position().left));
            editForm.append(createHiddenInputField("tagTopPosition", tagDiv.position().top));
            editForm.append(createHiddenInputField("tagWidth", tagDiv.width()));
            editForm.append(createHiddenInputField("tagHeight", tagDiv.height()));
            editForm.append(createHiddenInputField("screenId", $("#screenId").text()));

            $.post(editForm.attr("action"), editForm.serialize(), function(data) {
                if (data.success == true) {
                    window.location.reload(true);
                } else {
                    alert("Request failed: " + data.error);
                }
            });
        });

        fieldDialog.accordion({
            header : ".accordionHeader",
            autoHeight : false
        }).dialog({
            modal : true,
            resize : "auto",
            width : "90%",
            position : "top"
        }).accordion("resize");
    });
}

function addFieldInfo(event, tagCanvasSelector, newFieldFormTemplateUrl) {
    event.preventDefault();
    
    var requestContextPath = $("span#requestContextPath").text();
    
    $.get(requestContextPath + newFieldFormTemplateUrl, function(response) {
        var fieldDialog = $(response);
        
        $("form input[type='reset']", fieldDialog).click(function(event) {
            fieldDialog.find("input[type='text'][name$='Other']").each(function() {
                $(this).attr("disabled", "disabled");
            });
        });
        
        $("form input[type='submit']", fieldDialog).click(function(event) {
            event.preventDefault();

            var form = $("#createOrEditFieldFormForScreen", fieldDialog);

            var tagDiv = $(tagCanvasSelector).data("currentTag");

            form.append(createHiddenInputField("tagLeftPosition", tagDiv.position().left));
            form.append(createHiddenInputField("tagTopPosition", tagDiv.position().top));
            form.append(createHiddenInputField("tagWidth", tagDiv.width()));
            form.append(createHiddenInputField("tagHeight", tagDiv.height()));
            form.append(createHiddenInputField("screenId", $("#screenId").text()));

            $.post(form.attr("action"), form.serialize(), function(data) {
                if (data.success == true) {
                    window.location.reload(true);
                } else {
                    alert("Request failed: " + data.error);
                }
            });
        });
        
        fieldDialog.accordion({
            header : ".accordionHeader",
            autoHeight : false
        }).dialog({
            modal : true,
            resize : "auto",
            width : "90%",
            position : "top"
        }).accordion("resize");
    });
}

function updateFieldPosition(event, tagCanvasSelector, updatePositionUrl) {
    event.preventDefault();

    var requestContextPath = $("span#requestContextPath").text();
    
    var tagDiv = $(tagCanvasSelector).data("currentTag");

    $.post(requestContextPath + updatePositionUrl, {
        id : tagDiv.attr("name"),
        tagLeftPosition : tagDiv.position().left,
        tagTopPosition : tagDiv.position().top,
        tagWidth : tagDiv.width(),
        tagHeight : tagDiv.height(),
        screenId : $("#screenId").text()
    }, function(response) {
        if (response.success == true) {
            window.location.reload(true);
        } else {
            alert("Request failed: " + response.error);
        }
    });
}

function assignExistingField(event, tagCanvasSelector, selectFormTemplateUrl) {
    event.preventDefault();
    
    var requestContextPath = $("span#requestContextPath").text();
    
    $.get(requestContextPath + selectFormTemplateUrl + "/" + $("#screenId").text(), function(response) {
        var searchFieldDialog = $(response);
        
        initializeSearchFieldDataTable(searchFieldDialog);
        
        searchFieldDialog.accordion({
            header : ".accordionHeader",
            autoHeight : false
        }).dialog({
            modal : true,
            resize : "auto",
            width : "90%",
            position : "top"
        }).accordion("resize");
    });
}

function enableTextboxIfOtherIsSelected(event, otherChoiceValue, textBoxSelector) {
    var control = $(event.target);
    
    if (control.val() == otherChoiceValue) {
        $(textBoxSelector).removeAttr("disabled");
        $(textBoxSelector).rules("add", "required");
    } else {
        $(textBoxSelector).attr("disabled", "disabled");
        $(textBoxSelector).rules("remove", "required");
    }
    
    control.parents("form").valid();
}

function triggerOtherChoiceValidation(form) {
    form.find("input[type='radio'][value='other']:checked").each(function() {
        var self = $(this);
        
        var textFieldName = self.attr("name") + "Other";
        form.find("input[type='text'][name='" + textFieldName + "']").rules("add", "required");
        self.parents("form").valid();
    });
    
    form.find("select option[value='other']:selected").each(function() {
        var selectControl = $(this).parents("select");
        
        var textFieldName = selectControl.attr("name") + "Other";
        form.find("input[type='text'][name='" + textFieldName + "']").rules("add", "required");
        selectControl.parents("form").valid();
    });
}

function addMoreRule(event) {
    event.preventDefault();
    var form = $(event.target).parents("form");
    addInputTemplateToSection(form, form.find("#rulesSection"), "ruleInput", "ruleTemplate");
}

function addMoreMessage(event) {
    event.preventDefault();
    var form = $(event.target).parents("form");
    addInputTemplateToSection(form, form.find("#messagesSection"), "messageInput", "messageTemplate");
}

function addInputTemplateToSection(formSection, appendSection, inputClassFilter, templateId) {
    var currentInputCount = formSection.find("[class=\"" + inputClassFilter + "\"]").size();

    var template = $("#" + templateId).clone();
    $(":input", template).each(function(index) {
        var newId = $(this).attr("id").replace("#index#", currentInputCount);
        $(this).attr("id", newId);

        var newName = $(this).attr("name").replace("#index#", currentInputCount);
        $(this).attr("name", newName);
    });

    appendSection.append(template.children());
}

function createHiddenInputField(name, value) {
    return $("<input type='hidden' />").attr("name", name).attr("value", value);
}

var oTable;

function initializeSearchFieldDataTableForDrawingTag(searchDialog) {
    oTable = $("#example",searchDialog).dataTable({
        "bProcessing" : true,
        "bServerSide" : true,
        "sPaginationType" : "full_numbers",
        "sAjaxSource" : $("span#requestContextPath").text() + "/json/searchFieldForScreen.json",
        "aoColumns" : [ {
            "sClass" : "center",
            "bSortable" : false
        }, null, null, null, null,null],
        "aaSorting" : [ [ 1, 'asc' ] ],
        "fnServerParams" : function(aoData) {
            aoData.push({
                "name" : "searchFor",
                "value" : "field"
            });
            aoData.push({
                "name" : "keyword",
                "value" : $("input#keyword", searchDialog).val()
            });
            aoData.push({
                "name" : "screenOpt",
                "value" : $("#screenId").text()
            });
            
        },
        "sDom" : '<"H"lr>t<"F"ip>',
        "bJQueryUI" : true
    });

    $('#form', searchDialog).submit(function(event) {
        event.preventDefault();
        
        var pendingTag = $("#imageContainer").data("photoTagger").pendingTag;

        $.post($("span#requestContextPath").text() + "/updateFieldPosition", {
            id : $("input:radio:checked", searchDialog).val(),
            tagLeftPosition : pendingTag.position().left,
            tagTopPosition : pendingTag.position().top,
            tagWidth : pendingTag.width(),
            tagHeight : pendingTag.height(),
            screenId : $("#screenId").text()
        }, function(response) {
            searchDialog.dialog("destroy");
            $("#imageContainer").photoTagger("clearPendingTag");
            
            if (response.success == true) {
                $("#imageContainer").data("photoTagger").addTag(response.result.fieldId, 
                        response.result.tagInfo.leftTagPosition, 
                        response.result.tagInfo.topTagPosition, 
                        response.result.tagInfo.tagWidth, 
                        response.result.tagInfo.tagHeight,
                        response.result.fieldName);
                
                reloadDataTable();
            } else {
                alert("Request failed: " + response.error);
            }
        });
    });

    $('#example tbody td img').live('click', function() {
        var nTr = $(this).parents('tr')[0];
        if (oTable.fnIsOpen(nTr)) {
            /* This row is already open - close it */
            this.src = $("span#requestContextPath").text() + "/images/pagination/details_open.png";
            oTable.fnClose(nTr);
        } else {
            /* Open this row */
            this.src = $("span#requestContextPath").text() + "/images/pagination/details_close.png";
            oTable.fnOpen(nTr, fnFormatDetails(nTr), 'details');
        }
    });
}