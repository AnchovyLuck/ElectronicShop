$(document).ready(function() {
	buttonLoadForStates = $("#buttonLoadCountriesForStates");
	dropDownCountryForStates = $("#dropDownCountryForStates");
	dropDownStates = $("#dropDownStates");
	buttonAddState = $("#buttonAddState");
	buttonUpdateState = $("#buttonUpdateState");
	buttonDeleteState = $("#buttonDeleteState");
	labelStateName = $("#labelStateName");
	fieldStateName = $("#fieldStateName");
	labelCountryNameState = $("#labelCountryNameState");

	buttonLoadForStates.click(function() {
		loadCountriesForStates();
	});
	
	dropDownCountryForStates.on("change", function() {
		loadStateForCountry();
	});

	dropDownStates.on("change", function() {
		changeFormStatusToSelectedState();
	});

	buttonAddState.click(function() {
		if (buttonAddState.val() == "Add") {
			addState();
		} else {
			changeStateFormStatusToNew();
		}
	});

	buttonUpdateState.click(function() {
		updateState();
	});
	
	buttonDeleteState.click(function() {
		deleteState();
	})
});

addState = () => {
	url = contextPath + "states/save";
	stateName = fieldStateName.val();
	
	selectedCountry = $("#dropDownCountryForStates option:selected");
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();
	
	jsonData = {name: stateName, country: {id: countryId, name: countryName}};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: (xhr) => {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done((stateId) => {
		selectNewlyAddedState(stateId, stateName);
		showToastMessageState("The new state has been added.");
	}).fail(() => {
		showToastMessageState("ERROR: Could not connect to server / server encountered an error.");
	});
}

updateState = () => {
	url = contextPath + "states/save";
	stateName = fieldStateName.val();
	stateId = dropDownStates.val();
	
	selectedCountry = $("#dropDownCountryForStates option:selected");
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();
	
	jsonData = {id: stateId, name: stateName, country: {id: countryId, name: countryName}};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: (xhr) => {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(() => {
		$("#dropDownStates option:selected").text(stateName);
		showToastMessageState("The new state has been updated.");
		changeStateFormStatusToNew();
	}).fail(() => {
		showToastMessageState("ERROR: Could not connect to server / server encountered an error.");
	});
}

deleteState = () => {
	stateId = dropDownStates.val();
	url = contextPath + "states/delete/" + stateId;
	
	$.get(url, function() {
		$("#dropDownStates option[value='" + stateId + "']").remove();
		changeFormStateToNew();
	}).done(() => {
		showToastMessage("The state has been deleted");
	}).fail(() => {
		showToastMessageState("ERROR: Could not connect to server / server encountered an error.");
	});
}

selectNewlyAddedState = (stateId, stateName) => {
	$("<option>").val(stateId).text(stateName).appendTo(dropDownStates);
	
	$("#dropDownStates option[value='" + stateId + "']").prop("selected", true);
}

changeStateFormStatusToNew = () => {
	buttonAddState.val("Add");
	labelStateName.text("State/Province Name:");
	
	buttonUpdateState.prop("disabled", true);
	buttonDeleteState.prop("disabled", true);
	
	fieldStateName.val("").focus();
}

changeFormStatusToSelectedState = () => {
	buttonAddState.prop("value", "New");
	buttonUpdateState.prop("disabled", false);
	buttonDeleteState.prop("disabled", false);
	
	labelStateName.text("Selected State/Province:");
	
	selectedStateName = $("#dropDownStates option:selected").text();
	fieldStateName.val(selectedStateName);
}

loadStateForCountry = () => {
	selectedCountry = $("#dropDownCountryForStates option:selected");
	countryId = selectedCountry.val();
	
	url = contextPath + "states/list_by_country/" + countryId;
	
	$.get(url, function(responseJSON) {
		dropDownStates.empty(); 
		
		$.each(responseJSON, function(index, state) {
			$("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
		});
	}).done(() => {
		changeStateFormStatusToNew();
		showToastMessageState("All states have been loaded for country " + selectedCountry.text() + ".");
	}).fail(() => {
		showToastMessageState("ERROR: Could not connect to server / server encountered an error.");
	});
}

loadCountriesForStates = () => {
	url = contextPath + "countries/list";
	$.get(url, function(responseJSON) {
		dropDownCountryForStates.empty();
		labelCountryNameState.text("Selected Country:");
		
		$.each(responseJSON, function(index, country) {
			$("<option>").val(country.id).text(country.name).appendTo(dropDownCountryForStates);
		});
	}).done(() => {
		buttonLoadForStates.val("Refresh Country List");
		showToastMessageState("All countries have been loaded.");
	}).fail(() => {
		showToastMessageState("ERROR: Could not connect to server / server encountered an error.");
	});
}