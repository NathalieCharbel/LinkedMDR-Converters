<%@page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html lang="fr">
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		
		<title>LinkedMDR</title>
		
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/css/bootstrap.css">
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/css/bootstrap-theme.css">
	    <link rel="stylesheet"
			href="<%=request.getContextPath()%>/css/linkedMDR.css">
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/css/font-awesome.css">
	</head>
	
	<body>
		<div class="container content-full">
			
			<!-- Include fragment topbar.jsp to the main page -->
			<jsp:include page="topbar.jsp" />
			
			<div class="container-fluid ">
				<div class="row row-top-design">
				</div>
			</div>
			<div class="container-fluid content-middle">
				<div class="row row-middle">
					<form action="SrvMain" method="post"  enctype="multipart/form-data">
					
					    <!-- Presentation container -->
						<div class="col-xs-3 col-sm-3 col-md-3 col-presentation">
							<div class="panel panel-default panel-upload">
								<div class="panel-heading"><i class="fa fa-list-alt"></i>  <strong>Tips</strong> <small> </small></div>
								<div class="panel-body">
								 <p class="supported-format-text">Supported format: docx, pdf, jpeg, jpg, png</p>
								 <p><b> Steps: </b></p>
								 <ul>
									 <li>select the files you want to transform</li>
								  	 <li>select the transformation</li>
								 	 <li>download the converted file (format: xml/dc, tei, mpeg-7)</li>
								 </ul>
					         	 </div>
					         	 </div>
						</div>
						
						<div class=" col-xs-9 col-sm-9 col-md-9  col-upload">
						
							<!-- Include fragment upload.jsp to the main page -->
							<jsp:include page="upload.jsp" />
							
						</div>
					</form>
				</div>
			</div>
			
			<!-- Footer -->
			<footer class="container-fluid text-center">
			</footer>
			
		</div>
	
		<!-- Jquery -->
		<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
	
		<!-- Bootstrap -->
		<script src="<%=request.getContextPath()%>/js/bootstrap.js"></script>
		
		<!-- Upload fonction js/ docx2xml -->
		<script src="<%=request.getContextPath()%>/js/docs2xml.js"></script>
		
		<!-- Upload fonction js/ xml2rdf -->
		<script src="<%=request.getContextPath()%>/js/xml2rdf.js"></script>
	</body>
</html>