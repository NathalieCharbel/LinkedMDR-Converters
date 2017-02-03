$(document).ready(function() {
	var uniqueIdLinkedmdr=0;
	/** On change file (browse...) **/
	// Get the names of the uploaded files and show row to the table
	$(document).on('change', '.input-file-2rdf', function() { 
		uniqueIdLinkedmdr=0;
		// Remove the previous rows
		$('.table-2rdf> tbody>tr').remove();
		
		// Get file sent by the client
		var files = ($(this).closest('.input-file')).find("input:file")[0].files;
		for (var x = 0; x < files.length; x++) {
				var id=0;
		    // add  row (browse)
	   	    var $row = $('<tr>'+
		    	  //td name
	   	          "<td class='td-name'>"+files[x].name+"  </td>"+
	   	          //td standards
	   	          '<td>'+
		          	  "<div class='td-linkedmdr' >"+
				         "<div class='form-check'>"+
						  "<label class='form-check-label'>"+
						    "<input type='checkbox' class='form-check-input linkedmdr' name='linkedmdr' value=''>"+
						    'LinkedMDR'+
						  "</label>"+
						 "</div>"+
						"</div"+
		           "</td>"+
			          //td actions
		    	   '<td>' +
					  '<a href="#" class="button-delete-2rdf btn btn-danger btn-sm">Delete</a>'+
				   '</td>'+	 
	   	    	'</tr>');
	   	    
	   	    // add row to the table
		    $('.table-2rdf> tbody:last').append($row);
		    
		    // incremente row id
		    id++;
		    
		    // check extension of the file and hide/show checkboxes
			var extension = files[x].name.substr( (files[x].name.lastIndexOf('.') +1) );
		    /** show standards value**/
			if (extension=="xml"){
				// Enabled/Disabled the standards
				$('.table-2rdf>tbody>tr:last').find('.linkedmdr').prop('checked',true);
			}
			else{
				alert ("Unsupported format");
				$('.table-2rdf>tbody>tr:last').remove();
			}   	
		
			}// for
			$('#download-button-linkedmdr').prop('disabled', false);
    });
    
  
    /**Function delete button**/
    $( document ).on('click', '.button-delete-2rdf', function() {
    	$(this).parents('tr').remove();
    	if($('.table-2rdf>tbody>tr').length==0){
    		$('#download-button-linkedmdr').prop('disabled', true); 	
    	}
    });
    
    /** Function modify id **/
    $( document ).on('click', '#download-button-linkedmdr', function() {
 		// For each tr we modify the value of each standards (we need an unique id to link with files)
    	uniqueIdLinkedmdr=0;
 		$('.table-2rdf>tbody>tr').each(function() {
 			$(this).find('.linkedmdr').val(uniqueIdLinkedmdr);
 			uniqueIdLinkedmdr++;
 		});
	 });
    
    
	/** Modify the "tips" when user click "document to rdf" **/
	$(document).on('click', '.li-xml2rdf', function() {
		$(".supported-format-text").text("Supported format: xml/(dc, tei, mpeg-7)");
	});
	
	
});