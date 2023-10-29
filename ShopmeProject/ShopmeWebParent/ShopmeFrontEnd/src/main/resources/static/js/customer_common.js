loadStateForCountry = () => {
	selectedCountry = $("#country option:selected");
	url = contextPath + "settings/list_states_by_country/" + selectedCountry.val();

	$.get(url, (responseJSON) => {
		dataListState.empty();

		$.each(responseJSON, (index, state) => {
			$("<option>").val(state.name).text(state.name).appendTo(dataListState);
		});
	});
}

checkPasswordMatch = (confirmPassword) => {
	if (confirmPassword.value != $("#password").val()) {
		confirmPassword.setCustomValidity("Mật khẩu không trùng khớp!");
	} else {
		confirmPassword.setCustomValidity("");
	}
}

checkEmailUnique = (form) => {
	url = contextPath + "customers/check_unique_email";
	customerEmail = $("#email").val();
	csrfValue = $("input[name='_csrf']").val();

	params = {email: customerEmail, _csrf: csrfValue };

	$.post(url, params, (response) => {
		if (response == "OK") {
			form.submit();
		} else if (response == "Duplicated") {
			showWarningModal("There is another customer with email: " + customerEmail);
		} else {
			showErrorModal("Unknown response from server!");
		}
	}).fail(() => {
		showErrorModal("Could not connect to the server!");
	});
}

showModalDialog = (title, message) => {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal("show");
}

showErrorModal = (message) => showModalDialog("Error", message);

showWarningModal = (message) => showModalDialog("Warning", message);