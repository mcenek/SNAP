<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <?php include 'partial/resources.php'; ?>

    <link rel="stylesheet" href=<?php echo style_url() . 'partiview_generator.css'; ?> type="text/css" />

    <title>Network Visualization</title>

    <script type="text/javascript">
        function selectAll(box) { // TODO: this really needs to be refined
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
    <?php include 'partial/navi.php'; ?>

    <div class="container">
        <h3>Network Visualization</h3>
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
            Select what Network Analysis files you would like to generate for
            visualization then click Make 3D Visualization files.
            <br />
            To run a file in the visualizer select what file you would like to
            run then click Launch 3D Visualization.
        </p>

        <ul class="nav nav-tabs" role-"tablist">
            <li class="nav-item">
                <a class="nav-link active" data-toggle="tab" role="tab" href="#networkFiles">Network Analysis Files</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" data-toggle="tab" role="tab" href="#visualization">In Browser 3D Visualization Files</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" data-toggle="tab" role="tab" href="#partiviewFiles">Parti-View Files</a>
            </li>
        </ul>

        <div class="tab-content">
            <div id="networkFiles" class="tab-pane fade show active" role="tabpanel">
                <form method="post" action="partiview_generator/submit_files">
                    <?php
                        foreach ($files as $file => $file_name) {
                            $file_parts = pathinfo($file_name);

                            if ($file_parts['extension'] == 'gexf') {
                                echo form_checkbox(array(
                                    'name' => 'checkbox[]',
                                    'value' => $file_name,
                                    'checked' => false,
                                ));

                                $url = site_url() . '/partiview_generator/display_file' . $file_name;
                                echo '<a href="' . $url . '">' . $file_name . '</a><br/>';
                            }
                        }
                    ?>

                    <input type="checkbox" name="select_all" onClick="selectAll(this)">Select All</input>
                    <br/>
                    <br/>
                    <button class="btn btn-primary" name="file_action" value="vis_gen" type="submit">
                        Make 3D Visualization files
                    </button>
                    <button class="btn btn-danger" name="file_action" value="delete" type="submit">Delete</button>
                    <button class="btn btn-primary" name="file_action" value="download" type="submit">Download</button>
                </form>
            </div>
            <div id="visualization" class="tab-pane fade" role="tabpanel">
                <form method="post" action="partiview_generator/submit_files">
                    <?php
                        foreach ($files as $file => $file_name) {
                            $file_parts = pathinfo($file_name);

                            if ($file_parts['extension'] == "txt") {
                                echo form_checkbox(array(
                                    'name' => 'checkbox[]',
                                    'value' => $file_name,
                                    'checked' => false,
                                ));

                                $url = site_url() . '/partiview_generator/display_file' . $file_name;
                                echo '<a href="' . $url . '">' . $file_name . '</a><br/>';
                            }
                        }
                    ?>
                    <input type="checkbox" name="select_all" onClick="selectAll(this)"> Select All
                    <button class="btn btn-danger" name="file_action" value="delete" type="submit">Delete</button>
                    <button class="btn btn-primary" name="file_action" value="download" type="submit">Download</button>
                </form>
            </div>
            <div id="partiviewFiles" class="tab-pane fade" role="tabpanel">
                <form method="post" action="partiview_generator/submit_files">
                    <?php
                        foreach ($files as $file => $file_name) {
                            $file_parts = pathinfo($file_name);

                            if ($file_parts['extension'] == 'speck'
                                || $file_parts['extension'] == 'cf'
                                || $file_parts['extension'] == 'cmap') {
                                echo form_checkbox(array(
                                    'name' => 'checkbox[]',
                                    'value' => $file_name,
                                    'checked' => false,
                                ));

                                $url = site_url() . '/partiview_generator/display_file' . $file_name;
                                echo '<a href="' . $url . '">' . $file_name . '</a><br/>';
                            }
                        }
                    ?>
                    <input type='checkbox' name='select_all' onClick='selectAll(this)'> Select All
                    <button class="btn btn-danger" name="file_action" value="delete" type="submit">Delete</button>
                    <button class="btn btn-primary" name="file_action" value="download" type="submit">Download</button>
                </form>
            </div>
        </div>

        <p id="actionButtons">
            <button class="btn btn-primary" type="button" onClick="fireVisualizer()">Start Visualization</button>
            <button class="btn btn-primary" type="button" onclick="stopVisualizer()">Stop Visualization</button>
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
        function showLoadingWheel() {
            document.getElementById("loadId").style.display = "block";
        }
        function hideLoadingWheel() {
            // this is called from visualize.js
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

        var assetPath = "<?php echo asset_url(); ?>";

        // TODO: the threejs imports should be minified versions, if possible
    </script>
    <script type="text/javascript" src="<?php echo asset_url(); ?>js/three.js"></script>
    <script type="text/javascript" src="<?php echo asset_url(); ?>js/OrbitControls.js"></script>
    <script type="text/javascript" src="<?php echo asset_url(); ?>js/THREE.TextTexture/THREE.TextTexture.js"></script>
    <script type="text/javascript" src="<?php echo asset_url(); ?>js/THREE.TextSprite/THREE.TextSprite.js"></script>
    <script type="text/javascript" src="<?php echo asset_url(); ?>js/visualize.js"></script>
</body>
</html>
