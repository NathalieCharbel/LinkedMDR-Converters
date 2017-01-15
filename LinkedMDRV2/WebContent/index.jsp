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
			
			<!-- Presentation container -->
			<div class="container-fluid">
			  <br>
			  <p>LinkedMDR allows you to convert documents into xml/standards files </p> 
			  <p>Supported formats are the following : .docx .doc .pdf .png .jpg .jpeg
			  <br>
			  <p>LinkedMDR uses many tools to transform the docs into standards :</p> 
				<ul>
				 <li>Dublin Core: Apache Tika</li> 
				 <li>TEI: OxGarage</li> 
				 <li>MPEG-7: MPEG-7 Visual Descriptors </li> 
				</ul>
			</div>
	
			<div class="container-fluid content-middle">
				<div class="row row-middle">
					<form action="SrvMain" method="post"  enctype="multipart/form-data">
						<div class="col-md-offset-1 col-xs-11 col-sm-11 col-md-11  col-upload">
						
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
		
		<!-- Upload fonction js -->
		<script src="<%=request.getContextPath()%>/js/upload.js"></script>
	</body>
</html>