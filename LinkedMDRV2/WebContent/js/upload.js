$(document).ready(function() {
	
	/**Add-button / add row**/
	var row=0;
	$(document).on('click', ".add-file", function() {         
	    // add seconde row (browse)
   	    var $row = $('<tr>'+
  	    	 //td browse
  	    	 "<td>"+
  	    	 "<div class='input-group input-file '>"+
	    		 "<label class='btn btn-sm btn-primary btn-upload'>"+
	    			"Browse...<input style='display: none;' class='file-input' id='item_file_"+row+"' name='"+row+"'  type='file'>"+
	    		 "</label>"+
	    		 "</div>"+
	    	  "</td>"+
	    	  //td name
   	          "<td class='td-name'> </td>"+
   	          //td standards
   	          '<td>'+
	          	  "<div class='td-standards' style='display: none;' >"+
			         "<div class='form-check'>"+
					  "<label class='form-check-label'>"+
					    "<input type='radio' class='form-check-input dublincore' name='standards' id='item_dc_"+row+"' value='dc'>"+
					    'Dublin Core'+
					  "</label>"+
					 "</div>"+
					 "<div class='form-check'>"+
					  "<label class='form-check-label'>"+
					    "<input type='radio' class='form-check-input tei'  name='standards' id='item_tei_"+row+"'  value='tei'>"+
					    'TEI'+
					  "</label>"+
					 "</div>"+
					 "<div class='form-check'>"+
					  "<label class='form-check-label'>"+
					    "<input type='radio' class='form-check-input mpeg7'  name='standards' id='item_mpeg7_"+row+"' value='mpeg7'>"+
					    'MPEG-7'+
					  "</label>"+
					 "</div>"+
					"</div"+
	           "</td>"+
   	           //td linkedmdr
   	           '<td>'+
   	             "<div class='td-linkedmdr' style='display: none;' >"+
					"<div class='form-check'>"+
					  "<label class='form-check-label'>"+
					    "<input type='radio' class='form-check-input linkedmdr'  name='standards' id='item_linkedmdr_"+row+"' value='linkedmdr'>"+
					   " LinkedMDR"+
					  "</label>"+
					"</div>"+
				 "</div>"+
			    "</td>"+
				//td multiple linkedmdr
			    '<td >'+
	   	          "<div class='td-multiple-linkedmdr' style='display: none;' >"+
				      "<div class='form-check'>"+
					  "<label class='form-check-label'>"+
					    "<input type='checkbox' class='form-check-input multiple-linkedmdr'  name='multiple-linkedmdr' value='multiple-linkedmdr'>"+
					   " LinkedMDR"+
					  "</label>"+
					"</div>"+
				 "</div>"+
				"</td>"+
		          //td actions
	    		 '<td>' +
				'<label for="download"><button id="download-button" class="btn btn-primary btn-sm btn-download" disabled> Download</button> </label> <input style="display: none;" class="input-dl" name="download-button" value='+row+'></input>' + 
				  '<a href="#" class="button-delete btn btn-danger btn-sm">Delete</a>'+
				'</td>'+	 
   	    	'</tr>');
   	    
   	    // add row to the table
	    $('table> tbody:last').append($row);
	    
	    // incremente row id
	    row++;
	});
	
	/** On change file (browse...) **/
	// Get the names of the uploaded files and show row to the table
	$(document).on('change', '.input-file', function() { 
		
		// Get file sent by the client
		var files = ($(this).closest('.input-file')).find("input:file")[0].files;
		
		for (var i = 0; i < files.length; i++){
			// check extension of the file and hide/show checkboxes
			var extension = files[i].name.substr( (files[i].name.lastIndexOf('.') +1) );
			
			if (extension=="jpg" || extension == "png" || extension =="jpeg"){
				// Enabled/Disabled the standards
				$(this).closest("tr").find('.tei').prop('disabled',true);
				$(this).closest("tr").find('.dublincore').prop('disabled',false);
				$(this).closest("tr").find('.mpeg7').prop('disabled',false);
				
				// Show the row
	    		$(this).closest("tr").find('.td-name').text(files[i].name);
	    		$(this).closest("tr").find('.td-standards').show();
	    		$(this).closest("tr").find('.td-linkedmdr').show();
	    		$(this).closest("tr").find('.td-multiple-linkedmdr').show();
	    		$(this).closest("tr").find('.download-selected').show();
	    		// Enable the download button
	    		$(this).closest("tr").find('.btn-download').prop('disabled', false);
			}
			else if (extension=="doc" || extension == "docx"){
				// Enabled/Disabled the standards
				$(this).closest("tr").find('.tei').prop('disabled',false);
				$(this).closest("tr").find('.dublincore').prop('disabled',false);
				$(this).closest("tr").find('.mpeg7').prop('disabled',true);
				
				// Show the row
	    		$(this).closest("tr").find('.td-name').text(files[i].name);
	    		$(this).closest("tr").find('.td-standards').show();
	    		$(this).closest("tr").find('.td-linkedmdr').show();
	    		$(this).closest("tr").find('.td-multiple-linkedmdr').show();
	    		$(this).closest("tr").find('.btn-download').prop('disabled', false);
			}
			else{
				alert ("Unsupported format");
			}   	
		}
    	
    });
    
  
    /**Function delete button**/
    $( document ).on('click', '.button-delete', function() {
    	$(this).parents('tr').remove();
    });
    
    /**Function check multiple-linkedmdr and enabled/disabled button linkedmdr**/
	$(document).on('click', '.multiple-linkedmdr', function() {
	    var $boxes = $('input[name=multiple-linkedmdr]:checked');
	    if($boxes.length>=2){
	    	$('.btn-linkedmdr').prop('disabled', false);
	    }
	    else if($boxes.length<2){
	    	$('.btn-linkedmdr').prop('disabled', true);
	    }
	});
	
	/** Modify the input value in order to send only one row to the server**/
	$(document).on('click', '#download-button', function() {
		
		var row_id=null;
		var dc=null;
	    var tei=null;
	    var mpeg7=null;
	    var linkedmdr=null;
	    
	    $("#selected_item_dc").val("");
	    $("#selected_item_tei").val("");
	    $("#selected_item_mpeg7").val("");
	    $("#selected_item_linkedmdr").val("");
	    
	    row_id=$(this).closest("tr").find(".input-dl").val();
		$("#row_id").val(row_id);
	    // alert(row_id);
		
		if($("#item_dc_"+row_id).is(':checked')){
			dc=$("#item_dc_"+row_id).val();
			$("#selected_item_dc").val(dc);
			//alert(dc);
		}
		
		if($("#item_tei_"+row_id).is(':checked')){
			tei=$("#item_tei_"+row_id).val();
		    $("#selected_item_tei").val(tei);
			//alert(tei);
		}
		
		if($("#item_mpeg7_"+row_id).is(':checked')){
			mpeg7=$("#item_mpeg7_"+row_id).val();
			$("#selected_item_mpeg7").val(mpeg7);
			//alert(mpeg7);
			
		}
		
		if($("#item_linkedmdr_"+row_id).is(':checked')){
			linkedmdr=$("#item_linkedmdr_"+row_id).val();
			$("#selected_item_linkedmdr").val(linkedmdr);
			//alert(linkedmdr);
		}
		     
		//alert("POST : DC :"+dc+", TEI :"+tei+", MPEG-7 :"+mpeg7+", LINKEDMDR : "+linkedmdr);
	});
	
    
});