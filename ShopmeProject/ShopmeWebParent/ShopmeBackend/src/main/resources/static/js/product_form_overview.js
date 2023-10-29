dropdownBrands = $("#brand");
dropdownCategories = $("#category");

$(document).ready(() => {
	$("#shortDescription").richText();
	$("#fullDescription").richText();

	dropdownBrands.change(() => {
		dropdownCategories.empty();
		getCategories();
	});
	getCategoriesForNewForm();
});

getCategoriesForNewForm = () => {
	catIdField = $("#categoryId");
	editMode = false;
	
	if (catIdField.length > 0) {
		editMode = true;
	}
	
	if (!editMode) {
		getCategories();
	}
}

getCategories = () => {
	brandId = dropdownBrands.val();
	url = brandModuleURL + "/" + brandId + "/categories";

	$.get(url, function(responseJson) {
		$.each(responseJson, (index, category) => {
			$("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
		});
	});
}

checkUnique = (form) => {
	productId = $("#id").val();
	productName = $("#name").val();
	csrfValue = $("input[name='_csrf']").val();

	params = { id: productId, name: productName, _csrf: csrfValue };

	$.post(checkUniqueUrl, params, (response) => {
		if (response == "OK") {
			form.submit();
		} else if (response == "Duplicate") {
			showWarningModal("There is another product with name: " + productName);
		} else {
			showErrorModal("Unknown response from server");
		}
	}).fail(() => {
		showErrorModal("Could not connect to the server");
	});

	return false;
}

