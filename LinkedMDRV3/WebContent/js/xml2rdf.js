$(document).ready(function() {
	
	/**Add-button / add row**/
	var row=100;
	$(document).on('click', ".add-file-2rdf", function() {         
	    // add seconde row (browse)
   	    var $row = $('<tr>'+
  	    	 //td browse
  	    	 "<td>"+
  	    	 "<div class='input-group input-file input-file-2rdf '>"+
	    		 "<label class='btn btn-sm btn-primary btn-upload'>"+
	    			"Browse...<input style='display: none;' class='file-input' id='item_file_"+row+"' name='"+row+"'  type='file'>"+
	    		 "</label>"+
	    		 "</div>"+
	    	  "</td>"+
	    	  //td name
   	          "<td class='td-name'> </td>"+
   	          //td standards
   	          '<td>'+
	          	  "<div class='td-linkedmdr' style='display: none;' >"+
			         "<div class='form-check'>"+
					  "<label class='form-check-label'>"+
					    "<input type='radio' class='form-check-input linkedmdr' name='linkedmdr' id='item_linkedmdr_"+row+"' value='linkedmdr'>"+
					    'LinkedMDR'+
					  "</label>"+
					 "</div>"+
					"</div"+
	           "</td>"+
		          //td actions
	    	   '<td>' +
				'<label for="download"><button id="download-button-rdf" class="btn btn-primary btn-sm btn-download" disabled> Download</button> </label> <input style="display: none;" class="input-dl" name="download-button" value='+row+'></input>' + 
				  '<a href="#" class="button-delete-2rdf btn btn-danger btn-sm">Delete</a>'+
			   '</td>'+	 
   	    	'</tr>');
   	    
   	    // add row to the table
	    $('.table-2rdf> tbody:last').append($row);
	    
	    // incremente row id
	    row++;
	});
	
	/** On change file (browse...) **/
	// Get the names of the uploaded files and show row to the table
	$(document).on('change', '.input-file-2rdf', function() { 
		
		// Get file sent by the client
		var files = ($(this).closest('.input-file')).find("input:file")[0].files;
		
		for (var i = 0; i < files.length; i++){
			// check extension of the file and hide/show checkboxes
			var extension = files[i].name.substr( (files[i].name.lastIndexOf('.') +1) );
			
			if (extension=="xml"){
	    		$(this).closest("tr").find('.download-selected').show(); 
				// Show the row
	    		$(this).closest("tr").find('.td-name').text(files[i].name);
	    		$(this).closest("tr").find('.td-linkedmdr').show();
	    		$(this).closest("tr").find('.btn-download').prop('disabled', false);
			}
			else{
				alert ("Unsupported format");
			}   	
		}
    	
    });
    
  
    /**Function delete button**/
    $( document ).on('click', '.button-delete-2rdf', function() {
    	$(this).parents('tr').remove();
    });
    
	/** Modify the input value in order to send only one row to the server**/
	$(document).on('click', '#download-button-rdf', function() {
		
		var row_id=null;
	    var linkedmdr=null;
	    
	    $("#selected_item_dc").val("");
	    $("#selected_item_tei").val("");
	    $("#selected_item_mpeg7-vd").val("");
	    $("#selected_item_mpeg7-caliph").val("");
	    $("#selected_item_linkedmdr").val("");
	    
	    row_id=$(this).closest("tr").find(".input-dl").val();
		$("#row_id").val(row_id);
	    // alert(row_id);
		
		if($("#item_linkedmdr_"+row_id).is(':checked')){
			linkedmdr=$("#item_linkedmdr_"+row_id).val();
			$("#selected_item_linkedmdr").val(linkedmdr);
			//alert(linkedmdr);
		}
		     
		//alert("POST : DC :"+dc+", TEI :"+tei+", MPEG-7 :"+mpeg7+", LINKEDMDR : "+linkedmdr);
	});
	
	/** Modify the "tips" when user click "document to rdf" **/
	$(document).on('click', '.li-xml2rdf', function() {
		$(".supported-format-text").text("Supported format: xml/(dc, tei, mpeg-7)");
	});
	
	
});