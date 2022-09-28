<!DOCTYPE HTML>
<html>

<head>
<title>GER</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel="stylesheet" href="assets/css/main.css" />
<link rel="stylesheet" href="assets/css/modal.css" />
</head>

<body class="homepage is-preload">
	<div id="page-wrapper">

		<!-- Header -->
		<section id="header" class="wrapper">
			<!-- Nav -->
			<nav id="nav">
				<ul>
					<li id="text1"><a href="">Text 1</a></li>
					<li id="text2"><a href="">Text 2</a></li>
					<li id="text3"><a href="">Text 3</a></li>
					<li id="text4"><a href="">Text 4</a></li>
					<li id="text5"><a href="">Text 5</a></li>
					<li id="text6"><a href="">Text 6</a></li>
					<li id="text7"><a href="">Text 7</a></li>
					<li id="text8"><a href="">Text 8</a></li>
					<li id="text9"><a href="">Text 9</a></li>
					<li id="text10"><a href="">Text 10</a></li>
				</ul>
			</nav>

			<!-- Logo -->
			<div id="logo">
				<h1>
					<a href="index.jsp">GER</a>
				</h1>
				<p>A service for Named Entity Recognition in Greek texts</p>
				<section>
					<form>
						<div class="row d-flex justify-content-center">
							<div class="col-3"></div>
							<div class="col-6">
								<textarea name="inputText" id="inputText"
									placeholder="Insert text" rows="4"></textarea>
							</div>
							<div class="col-3"></div>

							<div class="col-12">
								<ul class="actions">
									<li><input type="submit" class="style1"
										value="Babelfy" onClick="findEntities(); return false;"/></li>
									<li><input type="submit" class="style1"
										value="LODSyndesisIE" onClick="findEntitiesWithTranslation(); return false;" /></li>
									<li><input type="reset" class="style2"
										onClick="removeEntities();" value="Reset" /></li>
								</ul>
							</div>
						</div>
					</form>
				</section>
			</div>

		</section>


		<!-- Highlights -->
		<section id="highlights" class="wrapper style3">
			<div id="entities_title" class="title">Entities</div>
			<div class="container">
				<div class="row aln-center" id="toAppend"></div>
			</div>
		</section>

		<!-- Footer -->
		<section id="footer">
				<div id="copyright">
					<ul>
						<li>&copy; Nikos Kontonasios</li>
						<li>Design: <a href="http://html5up.net">HTML5 UP</a></li>
					</ul>
				</div>
		</section>

	</div>

	<!-- Modal -->
	<div class="modal" id="modal">
		<div class="modal-header">
			<div class="modal-title" id="modal-title">Title</div>
			<button class="modal-close" id="modal-close">&times;</button>
		</div>
		<div class="modal-body" id="modal-body"></div>
	</div>
	<div id="overlay"></div>



	<!-- Scripts -->
	<script src="assets/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="assets/js/jquery.dropotron.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="assets/js/browser.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="assets/js/breakpoints.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="assets/js/util.js" type="text/javascript" charset="utf-8"></script>
	<script src="assets/js/main.js" type="text/javascript" charset="utf-8"></script>
	<script src="assets/js/modal.js" type="text/javascript" charset="utf-8"></script>
	<script src="assets/js/sampleTexts.js" type="text/javascript" charset="utf-8"></script>
	<script src="assets/js/entitiesInHTML.js" type="text/javascript" charset="utf-8"></script>
	<script src="assets/js/babelfy.js" type="text/javascript" charset="utf-8"></script>
	<script src="assets/js/translationWithLODsyndesisIE.js" type="text/javascript" charset="utf-8"></script>

</body>

</html>