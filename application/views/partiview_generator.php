<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<link rel="stylesheet" href="<?php echo asset_url(); ?>css/menuStyle.css" type="text/css" />
		<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
		<link rel='stylesheet' type='text/css' href='//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css'>
		<title>Network Visualization</title>

		<script type="text/javascript">
		    function selectAll(box) {
				var checkBoxes = document.getElementsByTagName('input');
				for(var i=0; i < checkBoxes.length; i++) {
					if(checkBoxes[i].type == 'checkbox') {
						checkBoxes[i].checked = box.checked;
					}
				}
			}
		</script>
		
	</head>
	<body>
		<?php include 'navi.php'; ?>
		<div class="container">
			<H3>Network Visualization</H3>
			<p>Using the .gexf and .txt files from Gephi's Network Analysis, this page will generate files for use in Partiview<br />
			These files include: nodes.speck, edges.speck, mesh.speck, .cf, .cmap, and .sct.</p>
			<ul>
				<p class="current_val">Current Set Date Range: <?php echo $this->session->userdata('date_range'); ?></p>
				<p class="current_val">Current Set Skew X: <?php echo $this->session->userdata('skew_x'); ?></p>
				<p class="current_val">Current Set Skew Y: <?php echo $this->session->userdata('skew_y'); ?></p>
				<p class="current_val">Current Set Skew Z: <?php echo $this->session->userdata('skew_z'); ?></p>
				<p class="current_val">Current Shape: <?php echo $this->session->userdata('shape'); ?></p>
			</ul>
			
			<?php
				echo '<ul>';

				echo '<form id="checkbox_form" name="checkbox_form" method="post" action="partiview_generator/submit_files">';
				echo "<input type='checkbox' name='select_all' onClick='selectAll(this)' > Select All<br/>";
				foreach($files as $file => $file_name)
				{
					$file_parts=pathinfo($file_name);

					if(($file_name=="*.gexf")){
						echo $file_name;
					}

					if(!is_dir($file_name)){
						if(($file_name=="completeLayout.gexf")
						||($file_name=="completeLayout.pdf")
						||($file_name=="FileDates.txt")
						||($file_name=="completeLayout.cmap")
						||($file_name=="completeLayout.cf")
						||($file_name=="completeLayout.sct")
						||($file_name=="completeLayout_edges.speck")
						||($file_name=="completeLayout_nodes.speck")
						||($file_name=="completeLayout_mesh.speck")
						||($file_parts['extension']=="gexf")
						||($file_parts['extension']=="txt")
						||($file_parts['extension']=="cmap")
						||($file_parts['extension']=="cf")
						||($file_parts['extension']=="speck")
						||($file_parts['extension']=="pdf")
						||($file_parts['extension']=="label"))
						//if(($file_parts['extension']=="gexf")
						// || ($file_parts['extension']=="pdf")
						// || ($file_parts['extension']=="speck")
						// || ($file_parts['extension']=="cf")
						// || ($file_parts['extension']=="cmap")
						// || ($file_parts['extension']=="sct")
						// || ($file_parts['extension']=="txt"))//Check File Extensions, display only produced files
						{
							echo form_checkbox(array(
								'name' => 'checkbox[]',
								'id' => 'checkbox[]',
								'value' => $file_name,
								'checked' => FALSE
							));

							$url = site_url() . '/partiview_generator/display_file/' . $file_name;
							echo '<a href="' .$url. '">' .$file_name. '</a><br/>';
						}
					}
				}

				echo '<button class="btn btn-primary" name="file_action" value="visualize" type="submit">Network Visualization</button>';

				//echo ' <button class="btn btn-primary" name="file_action" value="kill" type="submit">Stop</button>';

				echo '<br/>';
				echo '<br/>';

				echo '<button class="btn btn-danger" name="file_action" value="delete" type="submit">Delete</button>	<button class="btn btn-primary" name="file_action" value="download" type="submit">Download</button>';

				echo '</form>';
				echo '</ul>';
			?>
		</div>
	</body>
</html>