<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <?php include 'partial/resources.php'; ?>

    <title>Settings</title>

    <script language="javascript">
        $('#project_selector').on('change', function() {
            $('#delete_btn').prop('disabled', true).siblings().prop('disabled', false);
        });
    </script>
</head>
<body>
    <?php include 'partial/navi.php'; ?>

    <div class="container">
        <h3>Current Project</h3>
        <ul>
            <form id="settings" name="settings" method="post" action="settings/save_settings">
                <p>Projects</p>
                <select id="project_selector" name="project">
                    <?php foreach ($projects as $project): ?>
                        <option
                            value="<?php echo $project['project_id']; ?>"
                            <?php if ($project['project_id'] == $this->session->userdata('project_id')) {echo 'selected';}?>
                        >
                            <?php echo $project['name']; ?>
                        </option>
                    <?php endforeach;?>
                </select>
                <p class="current_val">
                    Current Project: <?php if ($current_project) {echo $current_project->name;}?>
                </p>
                <button id="delete_btn" class="btn btn-danger" name="file_action" value="delete" type="submit">Delete</button>
                <button class="btn btn-primary" name="file_action" value="current_project" type="submit">Save</button>
            </form>
        </ul>

        <h3>Create New Project</h3>
        <ul>
            <form id="settings" name="settings" method="post" action="settings/create_directory">
                <input type="text" maxlength="28" name="directory_name" />
                <button class="btn btn-primary" value="create_directory" type="submit">Create</button>
            </form>
        </ul>

        <h3>Network Generation Settings</h3>
        <ul>
            <form id="settings" name="settings" method="post" action="settings/save_settings">
                <ul>
                    <li>
                        If Use Frequency is set, then words are filtered based on how frequently they are used in each article. 
                        You can set a frequency count for words to be filtered: lower bound and upper bound. The lower bound will
                        delete any words that occur in the articles fewer than the lower bound count. The upper bound frequency count will
                        delete all words used more than frequency count. 
                    </li>
                    <li>
                        For example: lower bound frequency count = 5 and upper bound frequency = 35 means that any words used fewer than 
                        5 time or more than 35 times in the article will be deleted. 
                    </li>
                </ul>
                <input type="checkbox" name="useFreq"
                    <?php 
                        $use_freq = $this->session->userdata('use_freq');
                        if ($use_freq == 1) {echo "checked";}
                    ?>
                /> Use Frequency
                <br />
                <br />
                <p class="current_val">
                    Current Use Frequency Setting:
                    <?php
                        $use_freq = $this->session->userdata('use_freq');
                        if ($use_freq == 1) {
                            echo " Use Frequency";
                        } else if ($use_freq == 0) {
                            echo " Dont Use Frequency";
                        }
                    ?>
                </p>

                <p>Enter a number greater than 0 and less than the Upper bound</p>
                <input
                    type="number" min="0" name="freq_upper" 
                    value=<?php echo $this->session->userdata('freq_upper_bound'); ?>
                />
                Frequency Threshold Lower Bound 
                <p class="current_val">Current Set Lower Bound: <?php echo $this->session->userdata('freq_upper_bound'); ?></p>
                <br />
                <br />
                <p>Enter a number greater than the Lower bound and less than or equal to 100 </p>
                <input
                    type="number" max="100" name="freq_lower"
                    value=<?php echo $this->session->userdata('freq_lower_bound'); ?>
                />
                Frequency Threshold Upper Bound
                <p class="current_val">Current Set Upper Bound: <?php echo $this->session->userdata('freq_lower_bound'); ?></p>

                <button class="btn btn-primary" name="file_action" value="net_gen_set" type="submit">Save</button>
            </form>
        </ul>

        <h3>Network Analysis Settings</h3>
        <ul>
            <form id="settings" name="settings" method="post" action="settings/save_settings">
                <p>Layout</p>
                <select name="layout">
                    <option value="0"<?php $layout = $this->session->userdata('layout');if ($layout == 0) {echo "selected";}?>>Circular Star Layout</option>
                    <option value = "1"<?php $layout = $this->session->userdata('layout');if ($layout == 1) {echo "selected";}?>>Radial Axis Layout</option>
                    <option value = "2"<?php $layout = $this->session->userdata('layout');if ($layout == 2) {echo "selected";}?>>Yifan Hu Layout</option>
                    <option value = "3"<?php $layout = $this->session->userdata('layout');if ($layout == 3) {echo "selected";}?>>Force Atlas Layout</option>
                </select>
                <p class="current_val">
                    Current Layout:
                    <?php
                        $layout = $this->session->userdata('layout');
                        if ($layout == 0) {
                            echo " Circular Star";
                        } else if ($layout == 1) {
                            echo " Radial Axis";
                        } else if ($layout == 2) {
                            echo " Yifan Hu Layout";
                        } else if ($layout == 3) {
                            echo " Force Atlas Layout";
                        }
                    ?>
                </p>
                <br />
                <input
                    type="number" step="0.0001" min="0.0001" max="1"
                    value="<?php echo $this->session->userdata('mod_resolution'); ?>"
                    name="mod_resolution" id="mod_resolution"
                />
                Modularity Resolution
                <p class="current_val">Current Set Modularity Resolution: <?php echo $this->session->userdata('mod_resolution'); ?></p>

                <button class="btn btn-primary" name="file_action" value="net_ana_set" type="submit">Save</button>
            </form>
        </ul>

        <h3>Network Visualization Settings</h3>
        <ul>
            <form id="settings" name="settings" method="post" action="settings/save_settings">
                <p>Enter the number of days you would like nodes to be connected to each other between layers. </p>
                <input
                    type="number" min="1" value="<?php echo $this->session->userdata('date_range'); ?>"
                    name="date_range" id="date_range"
                /> Date Range
                <p class="current_val">Current Set Date Range: <?php echo $this->session->userdata('date_range'); ?></p>
                <br />

                <input
                    type="number" step="0.01" value="<?php echo $this->session->userdata('skew_x'); ?>"
                    name="skew_x" id="skew_x"
                /> Skew X
                <p class="current_val">Current Set Skew X: <?php echo $this->session->userdata('skew_x'); ?></p>
                <br />

                <input
                    type="number" step="0.01" value="<?php echo $this->session->userdata('skew_y'); ?>"
                    name="skew_y" id="skew_y"
                /> Skew Y
                <p class="current_val">Current Set Skew Y: <?php echo $this->session->userdata('skew_y'); ?></p>
                <br />

                <input
                    type="number" step="0.01" value="<?php echo $this->session->userdata('skew_z'); ?>"
                    name="skew_z" id="skew_z"
                /> Skew Z
                <p class="current_val">Current Set Skew Z: <?php echo $this->session->userdata('skew_z'); ?></p>
                <br />

                <h5>Mesh Shape</h5>
                <select name="shape">
                    <option value = "hybrid" <?php $shape = $this->session->userdata('shape');if ($shape == "hybrid") {echo "selected";}?>>Hybrid</option>
                    <option value="wire" <?php $shape = $this->session->userdata('shape');if ($shape == "wire") {echo "selected";}?>>Wire</option>
                    <option value = "solid" <?php $shape = $this->session->userdata('shape');if ($shape == "solid") {echo "selected";}?>>Solid</option>
                    <option value = "point" <?php $shape = $this->session->userdata('shape');if ($shape == "point") {echo "selected";}?>>Point</option>
                </select>
                <p class="current_val">Current Shape: <?php echo $this->session->userdata('shape'); ?></p>

                <button class="btn btn-primary" name="file_action" value="net_vis_set" type="submit">Save</button>
            </form>
        </ul>
    </div>
</body>
</html>
