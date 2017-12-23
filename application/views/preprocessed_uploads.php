<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
		<link rel="stylesheet" href="<?php echo asset_url(); ?>css/menuStyle.css" type="text/css" />
		<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
		<link rel='stylesheet' type='text/css' href='//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css'>
		<title>Preprocessed Uploads</title>

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
			<H3>Network Generation</H3>
			<p>This page takes the .txt processed by the provided Natural Language Processing toolkits and generates Undirected Graph Data Structure .dl files representing the network of words in processed files.</p>
			<ul>
				<p class="current_val">Current Use Frequency Setting:
						<?php $use_freq= $this->session->userdata('use_freq');
						if($use_freq == 1)
						{
							echo " Use Frequency";
						}
						else if ($use_freq==0)
						{
							echo " Dont Use Frequency";
						}

						 ?>
				</p>
				<p class="current_val">Current Set Frequency Threshold Lower Bound: <?php echo $this->session->userdata('freq_lower_bound'); ?></p>
				<p class="current_val">Current Set Frequency Threshold Upper Bound: <?php echo $this->session->userdata('freq_upper_bound'); ?></p><br>

			</ul>




			<?php
				echo '<ul>';
				echo '<form id="checkbox_form" name="checkbox_form" method="post" action="preprocessed_uploads/submit_files">';
				echo "<input type='checkbox' name='select_all' onClick='selectAll(this)' > Select All<br/>";
				foreach($files as $file => $file_name)
				{
					$file_parts=pathinfo($file_name);
					if($file_parts['extension']=="txt")//Check File Extensions, display only produced files
					{
						$data = array(
							'name' => 'checkbox[]',
							'id' => 'checkbox[]',
							'value' => $file_name,
							'checked' => FALSE
						);
						echo form_checkbox($data);

						$url = site_url() . '/preprocessed_uploads/display_file/' . $file_name;
						echo '<a href="' .$url. '">' .$file_name. '</a><br/>';
					}
				}
				echo '<button class="btn btn-primary" name="file_action" value="netgen" type="submit">Network Generation</button>';
				echo '<br/>';
				echo '<br/>';
				echo '<button class="btn btn-danger" name="file_action" value="delete" type="submit">Delete</button>	<button class="btn btn-primary" name="file_action" value="download" type="submit">Download</button>';
				echo '</form>';
				echo '</ul>';
			?>
		</div>
	</body>



</html>
