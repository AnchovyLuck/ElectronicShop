function addNextDetailSection() {
	allDivDetails = $("[id^='divDetail']");
	divDetailsCount = allDivDetails.length;
	

	htmlDetailSection = `
		<div class="row pb-2" id="divDetail${divDetailsCount}">
			<div class="col-1">
				<label class="col-form-label pb-2 ps-5">Name:</label>
			</div>
			<div class="col-5">
				<input type="text" name="detailNames" class="form-control w-75">
			</div>
			<div class="col-1">
				<label class="col-form-label pb-2 ps-5">Value:</label>
			</div>
			<div class="col-5">
				<input type="text" name="detailValues" class="form-control w-75">
			</div>
		</div>
	`;

	$("#divProductDetails").append(htmlDetailSection);
	
	previousDivDetailSection = allDivDetails.last();
	previousDivDetailID = previousDivDetailSection.attr("id");

	htmlLinkRemove = `
		<a class="fa-solid fa-circle-xmark fa-xl icon-dark pt-2" href="javascript:removeDetailSectionById('${previousDivDetailID}')" title="Remove this detail"></a>
	`;
	previousDivDetailSection.append(htmlLinkRemove);
	
	$("input[name='detailNames']").last().focus();
}

function removeDetailSectionById(id) {
	$("#" + id).remove();
}