$(document).ready(() => {
	$("#logoutLink").on("click",(e) => {
		e.preventDefault();
		document.logoutForm.submit();
	});

	customizeDropDownMenu();
	customizeTabs();
});

customizeDropDownMenu = () => {
	$(".navbar .dropdown").hover(
		function() {
			$(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();
		},
		function() {
			$(this).find('.dropdown-menu').first().stop(true, true).delay(100).slideUp();
		}
	);
	$(".dropdown > a").click(function() {
		location.href = this.href;
	});
}

customizeTabs = () => {
	let url = document.location.toString();
	if (url.match('#')) {
		$('.nav-tabs button[data-bs-target="#' + url.split('#')[1] + '"]').tab('show');
	}
	
	$('.nav-tabs a').on('shown.bs.tab', (e) => {
		window.location.hash = e.target.hash;
	});
}

clearFilter = () => {
	window.location = moduleURL;
}

showDeleteConfirmModal = (link, entityName) => {
	entityId = link.attr("entityId");

	$("#yesButton").attr("href", link.attr("href"));
	$("#confirmText").text("Are you sure you want to delete " + entityName + " ID " + entityId + "?");
	$("#confirmModal").modal("show");
}

showPriceTable = () => {
	linkDetailURL =  moduleURL + "/editPrice";
	console.log(linkDetailURL);
	$("#detailModal").modal("show").find(".modal-content").load(linkDetailURL);
}