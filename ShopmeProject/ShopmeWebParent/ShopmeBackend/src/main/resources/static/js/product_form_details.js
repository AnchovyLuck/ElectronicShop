$(document).ready(function() {
	$("a[name='linkRemoveDetail']").each(function(index) {
		$(this).click(function() {
			removeDetailSectionByIndex(index);
		});
	});

});

function addNextDetailSection() {
	allDivDetails = $("[id^='divDetail']");
	divDetailsCount = allDivDetails.length;

	htmlDetailSection = `
		<div class="row pb-2" id="divDetail${divDetailsCount}">
		<input type="hidden" name="detailIDs" th:value="0">
			<div class="col-1">
				<label class="col-form-label pb-2 ps-5">Name:</label>
			</div>
			<div class="col-4">
				<input type="text" name="detailNames" class="form-control w-75">
			</div>
			<div class="col-1">
				<label class="col-form-label pb-2 ps-5">Value:</label>
			</div>
			<div class="col-4">
				<input type="text" name="detailValues" class="form-control w-75">
			</div>
	`;

	$("#divProductDetails").append(htmlDetailSection);

	previousDivDetailSection = allDivDetails.last();
	previousDivDetailID = previousDivDetailSection.attr("id");

	htmlLinkRemove = `
		<div class="col-1"><a name="linkRemoveDetail" class="fa-solid fa-circle-xmark fa-xl icon-dark text-end" href="javascript:removeDetailSectionById('${previousDivDetailID}')" title="Remove this detail"></a></div></div>
	`;

	previousDivDetailSection.append(htmlLinkRemove);

	$("input[name='detailNames']").last().focus();
}

function removeDetailSectionById(id) {
	$("#" + id).remove();
}

function removeDetailSectionByIndex(index) {
	$("#divDetail" + index).remove();
}