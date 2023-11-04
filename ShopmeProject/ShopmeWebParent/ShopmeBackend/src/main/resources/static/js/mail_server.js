let switchElements = document.querySelectorAll('[role="switch"]');

switchElements.forEach(element => element.addEventListener("change", function() {
	(element.checked) ? element.value = true : element.value = false;
}));
