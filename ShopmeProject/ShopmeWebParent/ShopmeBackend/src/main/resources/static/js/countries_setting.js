$(document).ready(() => {
	buttonLoad = $("#buttonLoadCountries");
	dropDownCountry = $("#dropDownCountries");
	buttonAddCountry = $("#buttonAddCountry");
	buttonUpdateCountry = $("#buttonUpdateCountry");
	buttonDeleteCountry = $("#buttonDeleteCountry");
	labelCountryName = $("#labelCountryName");
	fieldCountryName = $("#fieldCountryName");
	fieldCountryCode = $("#fieldCountryCode");

	buttonLoad.click(() => {
		loadCountries();
	});

	dropDownCountry.on("change", () => {
		changeFormStateToSelectedCountry();
	});

	buttonAddCountry.click(() => {
		if (buttonAddCountry.val() == "Add") {
			addCountry();
		} else {
			changeCountryFormStatusToNew();
		}
	});

	buttonUpdateCountry.click(() => {
		updateCountry();
	});

	buttonDeleteCountry.click(() => {
		deleteCountry();
	})
});

validateFormCountry = () => {
	formCountry = document.getElementById("formCountry");
	if (!formCountry.checkValidity()) {
		formCountry.reportValidity();
		return false;
	}
	return true;
}

addCountry = () => {
	if (!validateFormCountry()) {
		return;
	}
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
	if (!validateFormCountry()) {
		return;
	}
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	countryId = dropDownCountry.val().split("-")[0];
	jsonData = { id: countryId, name: countryName, code: countryCode };

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
		changeCountryFormStatusToNew();
	}).fail(() => {
		showToastMessage("ERROR: Could not connect to server / server encounter an error")
	});
}

deleteCountry = () => {
	optionValue = dropDownCountry.val();
	countryId = optionValue.split("-")[0];
	url = contextPath + "countries/delete/" + countryId;

	$.ajax({
		type: 'DELETE',
		url: url,
		beforeSend: (xhr) => {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(() => {
		$("#dropDownCountries option[value='" + optionValue + "']").remove();
		changeCountryFormStatusToNew();
		showToastMessage("The country has been deleted.");
	}).fail(() => {
		showToastMessage("ERROR: Could not connect to server / server encounter an error");
	});
}

selectNewlyAddedCountry = (countryId, countryCode, countryName) => {
	optionValue = countryId + "-" + countryCode;
	$("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);
	$("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);

	fieldCountryName.val("").focus();
	fieldCountryCode.val("");
}

changeCountryFormStatusToNew = () => {
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

showToastMessage = (message) => {
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}

showToastMessageState = (message) => {
	$("#toastMessageState").text(message);
	$(".toast").toast('show');
}