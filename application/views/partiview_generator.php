<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
		<link rel="stylesheet" href="<?php echo asset_url(); ?>css/menuStyle.css" type="text/css" />
		<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
		<link rel='stylesheet' type='text/css' href='//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css'>
		<div id="moveview"></div>
		<title>Network Visualization</title>
		<script type="text/javascript">
		    function selectAll(box) { // TODO this really needs to be refined
				var checkBoxes = document.getElementsByTagName('input');
				for(var i=0; i < checkBoxes.length; i++) {
					if(checkBoxes[i].type == 'checkbox') {
						checkBoxes[i].checked = box.checked;
					}
				}
			}

		</script>
		<style>
			body {
				font-family: "Lato", sans-serif;
			}

			/* Style the tab */
			div.tab {
				overflow: hidden;
				border: 1px solid #ccc;
				background-color: #f1f1f1;
			}
			/* Style the buttons inside the tab */
			div.tab button {
				background-color: inherit;
				float: left;
				border: none;
				outline: none;
				cursor: pointer;
				padding: 14px 16px;
				transition: 0.3s;
				font-size: 17px;
			}
			/* Change background color of buttons on hover */
			div.tab button:hover {
				background-color: #ddd;
			}
			/* Create an active/current tablink class */
			div.tab button.active {
				background-color: #ccc;
			}
			/* Style the tab content */
			.tabcontent {
				display: none;
				padding: 6px 12px;
				border: 1px solid #ccc;
				border-top: none;
			}
			.black {
			  background: #000000;
			}

			#visPanel {
				display: flex;
				width: 90vw;
				height: 90vh;
				border: 10px solid #0f0f0f;
				margin-left: auto;
				margin-right: auto;
				margin-bottom: 200px;
			}
			canvas{
				width: 100%;
				height: 100%;
	        }
			#controlpanel {
				background: #000000;
				width: 200px;
				/*flex: 1 1 30%;*/
  			  	border-left: 10px solid #0f0f0f;
			    overflow: hidden;
				padding: 1em;
				color: ivory;
			}
			#visualizer {
				flex: 1 1 70%;
			}
			#placeholderCanvas {
				background: #000000;
			}
			#searchField {
				width: 150px;
				box-sizing: border-box;
				border: 2px solid #ccc;
				border-radius: 2px;
				font-size: 16px;
				background-color: white;
				margin-bottom: 10px;
			}
			#actionButtons {
				margin: 0.5em;
			}

			.loader {
		      position: absolute;
			  border: 16px solid #f3f3f3;
			  border-radius: 50%;
			  border-top: 16px solid #3498db;
			  width: 120px;
			  height: 120px;
			  -webkit-animation: spin 2s linear infinite; /* Safari */
			  animation: spin 2s linear infinite;
			}
			/* Safari */
			@-webkit-keyframes spin {
			  0% { -webkit-transform: rotate(0deg); }
			  100% { -webkit-transform: rotate(360deg); }
			}
			@keyframes spin {
			  0% { transform: rotate(0deg); }
			  100% { transform: rotate(360deg); }
			}
		</style>
	</head>
	<body>
		<?php include 'navi.php'; ?>

		<div class="container">
			<H3>Network Visualization</H3>
			<p>
				Using the .gexf files from Gephi's Network Analysis, this page
				will generate files for use in Partiview or in browser with
				threejs.<br />
				These files include: nodes.speck, edges.speck, mesh.speck, .cf,
				.cmap, and three.txt.
			</p>

			<ul>
				<p class="current_val">Current Set Date Range: <?php echo $this->session->userdata('date_range'); ?></p>
				<p class="current_val">Current Set Skew X: <?php echo $this->session->userdata('skew_x'); ?></p>
				<p class="current_val">Current Set Skew Y: <?php echo $this->session->userdata('skew_y'); ?></p>
				<p class="current_val">Current Set Skew Z: <?php echo $this->session->userdata('skew_z'); ?></p>
				<p class="current_val">Current Shape: <?php echo $this->session->userdata('shape'); ?></p>
			</ul>

			<p>
				Select what Network Analysis files you would like to generate for visualization then click Make 3D Visualization files.<br />
				To run a file in the visualizer select what file you would like to run then click Launch 3D Visualization.
			</p>

			<div class="tab">
			  <button class="tablinks active" onclick="openCity(event, 'first')">Network Analysis Files</button>
			  <button class="tablinks" onclick="openCity(event, 'second')">In Browser 3D Visualization Files</button>
			  <button class="tablinks" onclick="openCity(event, 'third')">Party View Files</button>
			</div>

			<div id="first" class="tabcontent" style="display: block;">
			  <?php
			    	foreach($files as $file => $file_name)
							{
								$file_parts=pathinfo($file_name);

								if($file_parts['extension']=="gexf")
								{
									echo form_checkbox(array(
										'name' => 'checkbox[]',
										'id' => 'checkbox[]',
										'value' => $file_name,
										'checked' => FALSE
									));

									$url = site_url() . '/partiview_generator/display_file' . $file_name;
									echo '<a href="' .$url. '">' .$file_name. '</a><br/>';
								}
							}
							echo '<form id="checkbox_form" name="checkbox_form" method="post" action="partiview_generator/submit_files">';
							echo "<input type='checkbox' name='select_all' onClick='selectAll(this)' > Select All<br/>";
							echo '<br/>';
							echo ' <button class="btn btn-primary" name="file_action" value="download" type="submit">Make 3D Visualization files</button>';
							?>
			</div>
			<div id="second" class="tabcontent">
				 <?php
			    	foreach($files as $file => $file_name)
							{
								$file_parts=pathinfo($file_name);

								if($file_parts['extension']=="txt")//Check File Extensions, display only produced files
								{
									echo form_checkbox(array(
										'name' => 'checkbox[]',
										'id' => 'checkbox[]',
										'value' => $file_name,
										'checked' => FALSE
									));

									$url = site_url() . '/partiview_generator/display_file' . $file_name;
									echo '<a href="' .$url. '">' .$file_name. '</a><br/>';

								}
							}
							echo '<form id="checkbox_form" name="checkbox_form" method="post" action="partiview_generator/submit_files">';
							echo "<input type='checkbox' name='select_all' onClick='selectAll(this)' > Select All<br/>";
							echo '<br/>';
							echo ' <button class="btn btn-primary" name="file_action" value="Launch" style="display: none;" type="submit">Launch 3D Visualization</button>';
							        /*might need to change the value of launch to something else*/
							?>
			</div>
			<div id="third" class="tabcontent">
				 <?php
			    	foreach($files as $file => $file_name)
							{
								$file_parts=pathinfo($file_name);

								if(($file_parts['extension']=="speck")
								|| ($file_parts['extension']=="cf")
								|| ($file_parts['extension']=="cmap"))
								{
									echo form_checkbox(array(
										'name' => 'checkbox[]',
										'id' => 'checkbox[]',
										'value' => $file_name,
										'checked' => FALSE
									));

									$url = site_url() . '/partiview_generator/display_file' . $file_name;
									echo '<a href="' .$url. '">' .$file_name. '</a><br/>';
								}
							}
							echo '<form id="checkbox_form" name="checkbox_form" method="post" action="partiview_generator/submit_files">';
							echo "<input type='checkbox' name='select_all' onClick='selectAll(this)' > Select All<br/>";
							?>
			</div>

			<p id="actionButtons">
				<button class="btn btn-primary" type="button" onClick="fireVisualizer()">Start Visualization</button>
				<button class="btn btn-primary" type="button" onclick="stopVisualizer()">Stop Visualization</button>
				<button class="btn btn-danger" name="file_action" value="delete" type="submit">Delete</button>
				<button class="btn btn-primary" name="file_action" value="download" type="submit">Download</button>
			</p>
		</div>

		<div id="visPanel">
			<div id="visualizer">
				<div class="loader" id="loadId" style="display:none; margin-left:25%; margin-top:25%;"></div>
				<canvas id="placeholderCanvas"></canvas>
			</div>
			<div id="controlpanel">
				<h3>Object Information</h3>
				<p>
					Object: <label id="Object"></label><br>
					Name: <label id="Name"></label><br>
					Date: <label id="Date"></label><br>
					Size: <label id="Size"></label>
				</p>
				<h3>Controls</h3>
				<p>Search Visualizer:</p>
				<input id="searchField" type="text" maxlength="28" name="directory_name">
				<p>
					Visible:
					<p><input type="checkbox" name="labelsCheck" onclick="toggleLabels(this.checked)" checked> Labels</p>
					<p><input type="checkbox" name="nodesCheck" onclick="toggleNodes(this.checked)" checked> Nodes</p>
					<p><input type="checkbox" name="noodlesCheck" onclick="toggleNoodles(this.checked)" checked> Noodles</p>
					<p><input type="checkbox" name="egdesCheck" onclick="toggleEdges(this.checked)" checked> Edges</p>
				</p>
		   </div>
		</div>

		<script>
			function showLoadingWheel()
			{
				document.getElementById("loadId").style.display = "block";
			}
			function hideLoadingWheel() // this is called from visualize.js
			{
				document.getElementById("loadId").style.display = "none";
			}

			function fireVisualizer() {
				// just a local wrapper for the init function in visualize.js
				window.location.hash = "visPanel";
				$('#placeholderCanvas').hide();
				showLoadingWheel();
				init();
				// loading wheel is hidden from visualize.js
			}
			function stopVisualizer() {
				// just a local wrapper for the stop function in visualize.js
				$('#placeholderCanvas').show();
				closeScene();
				$('canvas:not(#placeholderCanvas)').remove();
			}

			function openCity(evt, cityName) {
			    var i, tabcontent, tablinks;
			    tabcontent = document.getElementsByClassName("tabcontent");
			    for (i = 0; i < tabcontent.length; i++) {
			        tabcontent[i].style.display = "none";
			    }
			    tablinks = document.getElementsByClassName("tablinks");
			    for (i = 0; i < tablinks.length; i++) {
			        tablinks[i].className = tablinks[i].className.replace(" active", "");
			    }
			    document.getElementById(cityName).style.display = "block";
			    evt.currentTarget.className += " active";
			}

			var assetPath = "<?php echo asset_url(); ?>";

		</script>
	    <script type="text/javascript" src="<?php echo asset_url(); ?>js/three.js"></script>
	    <script type="text/javascript" src="<?php echo asset_url(); ?>js/OrbitControls.js"></script>
	    <script type="text/javascript" src="<?php echo asset_url(); ?>js/Detector.js"></script>
	    <script type="text/javascript" src="<?php echo asset_url(); ?>js/stats.min.js"></script>
	    <script type="text/javascript" src="<?php echo asset_url(); ?>js/THREE.TextTexture/THREE.TextTexture.js"></script>
	    <script type="text/javascript" src="<?php echo asset_url(); ?>js/THREE.TextSprite/THREE.TextSprite.js"></script>
		<script type="text/javascript" src="<?php echo asset_url(); ?>js/visualize.js"></script>
	</body>
</html>
