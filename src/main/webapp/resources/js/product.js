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

function onSaveProductCompletion() {
	PF('tabProduct').select(0);
}

function onSearchFilter(searchFilter) {
	PF('globalFilterVar').getJQ().val(searchFilter.value);
	PF('globalFilterVar').getJQ().trigger('keyup');   
}

function onProductChange(product) {
	var selectedProduct = $('#formProductList\\:productFilter option:selected').text();
	if (product.value == 0) {
		selectedProduct = "";
	}
	
	$('#formProductList\\:dtProductList\\:nameFilter\\:filter').val(selectedProduct);
	$('#formProductList\\:dtProductList\\:nameFilter\\:filter').trigger('keyup');
}

function onProductVersionChange(productVersion) {
	var selectedProductVersion = $('#formProductList\\:productVersionFilter option:selected').text();
	if (productVersion.value == 0) {
		selectedProductVersion = "";
	}
	
	$('#formProductList\\:dtProductList\\:versionFilter\\:filter').val(selectedProductVersion);
	$('#formProductList\\:dtProductList\\:versionFilter\\:filter').trigger('keyup');
}