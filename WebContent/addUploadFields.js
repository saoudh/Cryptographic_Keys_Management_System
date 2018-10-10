$(document)
		.ready(
				function() {
					var max_fields = 10;
					var wrapper = $(".container_form");
					var add_button = $(".add_upload_field");
					var x = 1;
					$(add_button)
							.click(
									function(e) {
										console.log("entered");
										e.preventDefault();
										if (x < max_fields) {

											x++;
											$('.myfileitems')
													.append(
															'<div>Select File to Upload: <input class="fileupload" type="file" name="fileName'
																	+ x
																	+ '">'
																	+ '<a href="#" class="delete">Delete</a></div>'); // add
																														// input
																														// box
										} else {
											alert('You Reached the limits');
										}
									});

					$("#submit_upload").closest('form').on(
							'submit',
							function(e) {
								e.preventDefault();
								$(".fileupload").each(function() {
									if ($(this).get(0).files.length === 0) {
										$(this).closest('div').remove();

									}

								});
								var i = 1;
								// Namen von fileinput-feldern korrigieren
								$(".fileupload").each(
										function() {
											$(this)
													.attr('name',
															"fileName" + i);
											i++;
											$(wrapper).append(
													"<br>correct Filename: "
															+ $(this).attr(
																	"name"));

										}

								);

								this.submit(); //now submit the form
							});

					$(wrapper).on("click", ".delete", function(e) {
						e.preventDefault();
						$(this).parent('div').remove();
						x--;
					})
				});