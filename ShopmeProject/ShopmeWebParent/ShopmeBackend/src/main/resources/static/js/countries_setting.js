let buttonLoad;
let dropdownCountry;
let buttonAddCountry;
let buttonUpdateCountry;
let buttonDeleteCountry;
let labelCountryName;


$(document).ready(function() {
	buttonLoad = $("#buttonLoadCountries");
	dropDownCountry = $("#dropDownCountries");
	buttonAddCountry = $("#buttonAddCountry");
	buttonUpdateCountry = $("#buttonUpdateCountry");
	buttonDeleteCountry = $("#buttonDeleteCountry");
	labelCountryName = $("#labelCountryName");
	fieldCountryName = $("#fieldCountryName");
	fieldCountryCode = $("#fieldCountryCode");

	buttonLoad.click(function() {
		loadCountries();
	});

	dropDownCountry.on("change", function() {
		changeFormStateToSelectedCountry();
	});

	buttonAddCountry.click(function() {
		if (buttonAddCountry.val() == "Add") {
			addCountry();
		} else {
			changeFormStateToNew();
		}
	});

	buttonUpdateCountry.click(function() {
		updateCountry();
	});
	
	buttonDeleteCountry.click(function() {
		deleteCountry();
	})
});

addCountry = () => {
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();	
	jsonData = { name: countryName, code: countryCode };

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: (xhr) => {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done((countryId) => {
		selectNewlyAddedCountry(countryId, countryCode, countryName);
		showToastMessage("The new country has been added.");
	}).fail(() => {
		showToastMessage("ERROR: Could not connect to server / server encounter an error")
	});
}

updateCountry = () => {
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	countryId = dropDownCountry.val().split("-")[0];	
	jsonData = {id: countryId, name: countryName, code: countryCode };

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: (xhr) => {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done((countryId) => {
		$("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
		$("#dropDownCountries option:selected").text(countryName);
		showToastMessage("The country has been updated.");				
		changeFormStateToNew();
	}).fail(() => {
		showToastMessage("ERROR: Could not connect to server / server encounter an error")
	});
}

deleteCountry = () => {
	optionValue = dropDownCountry.val();
	countryId = optionValue.split("-")[0];	
	url = contextPath + "countries/delete/" + countryId;	
	
	$.get(url, function(responseJSON) {
		$("#dropDownCountries option[value='" + optionValue + "']").remove();
		changeFormStateToNew();
	}).done(() => {
		buttonLoad.val("Refresh Country List");
		showToastMessage("The country has been deleted.");
	}).fail(() => {
		showToastMessage("ERROR: Could not connect to server / server encounter an error")
	});
}

selectNewlyAddedCountry = (countryId, countryCode, countryName) => {
	optionValue = countryId + "-" + countryCode;
	$("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);
	$("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);

	fieldCountryName.val("").focus();
	fieldCountryCode.val("");

}

changeFormStateToNew = () => {
	buttonAddCountry.val("Add");
	labelCountryName.text("Country Name:");
	buttonUpdateCountry.prop("disabled", true);
	buttonDeleteCountry.prop("disabled", true);
	fieldCountryName.val("").focus();
	fieldCountryCode.val("");
}

changeFormStateToSelectedCountry = () => {
	buttonAddCountry.prop("value", "New");
	buttonUpdateCountry.prop("disabled", false);
	buttonDeleteCountry.prop("disabled", false);
	labelCountryName.text("Selected Country:");

	selectedCountryName = $("#dropDownCountries option:selected").text();
	fieldCountryName.val(selectedCountryName);

	countryCode = dropDownCountry.val().split("-")[1];
	fieldCountryCode.val(countryCode)
}

loadCountries = () => {
	url = contextPath + "countries/list";
	$.get(url, function(responseJSON) {
		dropDownCountry.empty();

		$.each(responseJSON, function(index, country) {
			optionValue = country.id + "-" + country.code;
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
		});

	}).done(() => {
		buttonLoad.val("Refresh Country List");
		showToastMessage("All countries have been loaded.");
	}).fail(() => {
		showToastMessage("ERROR: Could not connect to server / server encounter an error")
	});
}

function showToastMessage(message) {
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}