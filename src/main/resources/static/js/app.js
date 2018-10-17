$("#show-sidebar").click(()=>{
	$(".sidebar-fixed").toggle();
}
);

$("#show-password").click(()=>{
	let x = $("#password");
		if (x.is("[type = password]")) {
			x.attr("type", "text")
		} else {
			x.attr("type", "password")
		}
});
$('#file-logo').change(function(evt) {
	var img = $('#img-logo');
	var logoInp = $('#logo');
	var file = evt.target.files;
	if (file.length > 0) {
		var reader = new FileReader();
		reader.readAsDataURL(file[0]);
		reader.onload = function() {
			var image = reader.result;
			img.attr('src', image);
			logoInp.val(image);
		}
	}
});