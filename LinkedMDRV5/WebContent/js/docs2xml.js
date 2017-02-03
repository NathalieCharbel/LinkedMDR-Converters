$(document).ready(function() {
	var uniqueId=0;
	/** On change file (browse...) **/
	// Get the names of the uploaded files and show row to the table
	$(document).on('change', '.input-file-2xml', function() { 
		
		uniqueId=0;
		// Remove the previous rows
		$('.table-2xml> tbody>tr').remove();
		
		// Get file sent by the client
		var files = ($(this).closest('.input-file')).find("input:file")[0].files;

		for (var x = 0; x < files.length; x++) {
		    // add row
	   	    var $row = $('<tr>'+
		    	  //td name
	   	          "<td class='td-name'>"+files[x].name+" </td>"+
	   	          //td standards
	   	          '<td>'+
		          	  "<div class='td-standards' >"+
				         "<div class='form-check'>"+
						  "<label class='form-check-label'>"+
						    "<input type='checkbox' class='form-check-input dublincore' name='dc' value=''>"+
						    'Dublin Core'+
						  "</label>"+
						 "</div>"+
						 "<div class='form-check'>"+
						  "<label class='form-check-label'>"+
						    "<input type='checkbox' class='form-check-input tei'  name='tei' value=''>"+
						    'TEI'+
						  "</label>"+
						 "</div>"+
						 "<div class='form-check'>"+
						  "<label class='form-check-label'>"+
						    "<input type='checkbox' class='form-check-input mpeg7'  name='mpeg7' value=''>"+
						    'MPEG-7 Visual Descriptors'+
						  "</label>"+
						 "</div>"+
						"</div"+
		           "</td>"+
			          //td actions
		    	   '<td>' + 
					  '<a href="#" class="button-delete-2xml btn btn-danger btn-sm">Delete</a>'+
				   '</td>'+	 
	   	    	'</tr>');
	   	    
	   	    // add row to the table
		    $('.table-2xml> tbody:last').append($row);
		   
		    // check extension of the file and hide/show checkboxes
			var extension = files[x].name.substr( (files[x].name.lastIndexOf('.') +1) );
		    /** show standards value**/
			if (extension=="jpg" || extension=="JPG" || extension == "PNG" || extension == "png" || extension =="jpeg" || extension == "JPEG"){
				// Enabled/Disabled the standards
				$('.table-2xml>tbody>tr:last').find('.tei').prop('disabled',true);
				
				$('.table-2xml>tbody>tr:last').find('.dublincore').prop('disabled',false);
				$('.table-2xml>tbody>tr:last').find('.dublincore').prop('checked',true);
				
				$('.table-2xml>tbody>tr:last').find('.mpeg7').prop('disabled',false);
				$('.table-2xml>tbody>tr:last').find('.mpeg7').prop('checked',true);
			}
			else if (extension=="doc" || extension == "docx"){
				// Enabled/Disabled the standards
				$('.table-2xml>tbody>tr:last').find('.tei').prop('disabled',false);
				$('.table-2xml>tbody>tr:last').find('.tei').prop('checked',true);
				
				$('.table-2xml>tbody>tr:last').find('.dublincore').prop('disabled',false);
				$('.table-2xml>tbody>tr:last').find('.dublincore').prop('checked',true);
				
				$('.table-2xml>tbody>tr:last').find('.mpeg7').prop('disabled',true);
				
			}
			else if (extension=="pdf"){
				// Enabled/Disabled the standards
				$('.table-2xml>tbody>tr:last').find('.tei').prop('disabled',true);
				
				$('.table-2xml>tbody>tr:last').find('.dublincore').prop('disabled',false);
				$('.table-2xml>tbody>tr:last').find('.dublincore').prop('checked',true);
				
				$('.table-2xml>tbody>tr:last').find('.mpeg7').prop('disabled',true);
				
			}
			else{
				alert ("Unsupported format");
				$('.table-2xml>tbody>tr:last').remove();
			}   	
		
		}// for
		$('#download-button-xml').prop('disabled', false);
	});
    
  
    /**Function delete button**/
    $( document ).on('click', '.button-delete-2xml', function() {
    	// Remove the row
    	$(this).parents('tr').remove();
    	// If there is 0 row , disable the download button
    	if($('.table-2xml>tbody>tr').length==0){
    		$('#download-button-xml').prop('disabled', true);
    	}
    	
    });
    
    /** Function modify id **/
    $( document ).on('click', '#download-button-xml', function() {
 		// For each tr we modify the value of each standards (we need an unique id to link with files)
    	uniqueId=0;
 		$('.table-2xml>tbody>tr').each(function() {
 			$(this).find('.dublincore').val(uniqueId);
 			$(this).find('.tei').val(uniqueId);
 			$(this).find('.mpeg7').val(uniqueId);
 			uniqueId++;
 		});
	 });
    

	
	/** Modify the "tips" when user click "document to xml" **/
	$(document).on('click', '.li-doc2xml', function() {
		$(".supported-format-text").text("Supported format: docx, pdf, jpeg, jpg, png");
	});
	
    
});