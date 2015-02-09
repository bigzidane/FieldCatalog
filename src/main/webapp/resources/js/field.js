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
	// document ready
});

function onSaveFieldCompletion() {
	PF('tabField').select(0);
}

function onSearchFilter(searchFilter) {
	PF('globalFilterVar').getJQ().val(searchFilter.value);
	PF('globalFilterVar').getJQ().trigger('keyup');   
}

function onFieldChange(field) {
	var selectedField = $('#formFieldList\\:fieldFilter option:selected').text();
	if (field.value == 0) {
		selectedField = "";
	}
	
	$('#formFieldList\\:dtFieldList\\:nameFilter\\:filter').val(selectedField);
	$('#formFieldList\\:dtFieldList\\:nameFilter\\:filter').trigger('keyup');
}

function onScreenChange(screen) {
	var selectedField = $('#formFieldList\\:screenFilter option:selected').text();
	if (screen.value == 0) {
		selectedField = "";
	}
	
	$('#formFieldList\\:dtFieldList\\:screenNameFilter\\:filter').val(selectedField);
	$('#formFieldList\\:dtFieldList\\:screenNameFilter\\:filter').trigger('keyup');
}

function onProductChange(product) {
	var selectedProduct = $('#formFieldList\\:productFilter option:selected').text();
	if (product.value == 0) {
		selectedProduct = "";
	}
	
	$('#formFieldList\\:dtFieldList\\:productNameFilter\\:filter').val(selectedProduct);
	$('#formFieldList\\:dtFieldList\\:productNameFilter\\:filter').trigger('keyup');
}

function onProductVersionChange(productVersion) {
	var selectedProductVersion = $('#formFieldList\\:productVersionFilter option:selected').text();
	if (productVersion.value == 0) {
		selectedProductVersion = "";
	}
	
	$('#formFieldList\\:dtFieldList\\:productVersionFilter\\:filter').val(selectedProductVersion);
	$('#formFieldList\\:dtFieldList\\:productVersionFilter\\:filter').trigger('keyup');
}