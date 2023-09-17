$(document).ready(function() {
	$("#buttonCancel").on("click", function() {
		window.location = moduleURL;
	});

	$("#fileImage").change(function() {
		if (!checkFileSize(this)) {
			return;
		}
		showImageThumbnail(this);
	});
});

function showImageThumbnail(fileInput) {
	let file = fileInput.files[0];
	let reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail").attr("src", e.target.result);
	};

	reader.readAsDataURL(file);
}

function checkFileSize(fileInput) {
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

function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal("show");
}

function showErrorModal(message) {
	showModalDialog("Error", message);
}

function showWarningModal(message) {
	showModalDialog("Warning", message);
}