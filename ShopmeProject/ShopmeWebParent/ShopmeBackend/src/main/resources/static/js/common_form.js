$(document).ready(() => {
	$("#buttonCancel").on("click", () => {
		window.location = moduleURL;
	});

	$("#fileImage").change(() => {
		if (!checkFileSize(this)) {
			return;
		}
		showImageThumbnail(this);
	});
});

showImageThumbnail = (fileInput) => {
	let file = fileInput.files[0];
	let reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail").attr("src", e.target.result);
	};

	reader.readAsDataURL(file);
}

checkFileSize = (fileInput) => {
	fileSize = fileInput.files[0].size;

	if (fileSize > MAX_FILE_SIZE) {
		fileInput.setCustomValidity("You must choose an image not exceeds " + Math.round(MAX_FILE_SIZE / 1024) + " KB!");
	} else if (MAX_FILE_SIZE >= 1048576 && fileSize > MAX_FILE_SIZE) {
		fileInput.setCustomValidity("You must choose an image not exceeds " + Math.round(MAX_FILE_SIZE / 1048576) + " MB!");
	}
	else {
		fileInput.setCustomValidity("");
		return true;
	}
	fileInput.reportValidity();
	return false;
}

showModalDialog = (title, message) => {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal("show");
}

showErrorModal = (message) => showModalDialog("Error", message);

showWarningModal = (message) => showModalDialog("Warning", message);