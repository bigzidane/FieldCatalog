/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

/**
 * 	This file contains all scripts to process Product pages
 */

$(document).ready(function() {
	/*PF('editorBusinessRuleWidget').jq.find('iframe').contents().find('body').blur( function () {
		businessRuleUpdate();
	})*/
});

function onSaveScreenCompletion() {
	PF('tabScreen').select(0);
}

function onSearchFilter(searchFilter) {
	PF('globalFilterVar').getJQ().val(searchFilter.value);
	PF('globalFilterVar').getJQ().trigger('keyup');   
}

function onScreenChange(screen) {
	var selectedScreen = $('#formScreenList\\:screenFilter option:selected').text();
	if (screen.value == 0) {
		selectedScreen = "";
	}
	
	$('#formScreenList\\:dtScreenList\\:nameFilter\\:filter').val(selectedScreen);
	$('#formScreenList\\:dtScreenList\\:nameFilter\\:filter').trigger('keyup');
}

function onProductChange(product) {
	var selectedProduct = $('#formScreenList\\:productFilter option:selected').text();
	if (product.value == 0) {
		selectedProduct = "";
	}
	
	$('#formScreenList\\:dtScreenList\\:productNameFilter\\:filter').val(selectedProduct);
	$('#formScreenList\\:dtScreenList\\:productNameFilter\\:filter').trigger('keyup');
}

function onProductVersionChange(productVersion) {
	var selectedProductVersion = $('#formScreenList\\:productVersionFilter option:selected').text();
	if (productVersion.value == 0) {
		selectedProductVersion = "";
	}
	
	$('#formScreenList\\:dtScreenList\\:productVersionFilter\\:filter').val(selectedProductVersion);
	$('#formScreenList\\:dtScreenList\\:productVersionFilter\\:filter').trigger('keyup');
}