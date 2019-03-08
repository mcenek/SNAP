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
                        If "Use Frequency" is set, then words are filtered based on how frequently they are used in each article. 
                        You can set the word frequencies to filter unwanted words using the high and lower frequency bounds below. The high word frequency bound will delete any words that occur in the article with a frequencies greater than the upper bound percentile. To remove the low frequency words set the lower percentile bound, and the words that are in each article with the frequencies in the lower bound percentile will be deleted. The most frequently used word will be in the top percentile (or = 1).
                    </li>
                    <li>
                        For example: To remove all words that are in the article more than 20 time, set the upper frequency percentile bound. Similarly, to remove all words that are in the article fewer than 5 times, set the lower frequency percentile bound.
                    </li>
                    <li>
                        The sliding window sets the lexical chaning window width. All tokens within this windows will be fully connected. The window width has to be greater between 2 and 5. 
                    </li>
                </ul>
                <input type="checkbox" name="useFreq"
                    <?php 
                        $use_freq = $this->session->userdata('use_freq');
                        if ($use_freq == 1) {echo "checked";}
                    ?>
                /> Use Frequency Percentiles for word removal.
                <br />
                <br />
                <p class="current_val">
                    Current Frequency Removal Setting:
                    <?php
                        $use_freq = $this->session->userdata('use_freq');
                        if ($use_freq == 1) {
                            echo " Use Frequency";
                        } else if ($use_freq == 0) {
                            echo " Dont Use Frequency";
                        }
                    ?>
                </p>

                <p>High word frequency percentile bound: Enter a number greater than 0 and less than the lower bound</p>
                <input
                    type="number" step="0.01" max="1" name="freq_upper" 
                    value=<?php echo $this->session->userdata('freq_upper_bound'); ?>
                />
                High Word Frequency Percentile Threshold 
                <p class="current_val">Current High Word Frequency Percentile Bound: <?php echo $this->session->userdata('freq_upper_bound'); ?></p>
                <br />
                <br />
                <p>Low word frequency percentile bound: Enter a number greater than the Upper Word Frequency Percentile Bound and less than or equal to 1 </p>
                <input
                    type="number" step="0.01" min="0" name="freq_lower"
                    value=<?php echo $this->session->userdata('freq_lower_bound'); ?>
                />
                Low Word Frequency Percentile Threshold
                <p class="current_val">Current Low Word Frequency Percentile Bound: <?php echo $this->session->userdata('freq_lower_bound'); ?></p>

                <br />
                <br />
                <p>Enter the lexical chaining window width between 2 and 5 </p>
                <input
                    type="number" step="1" min="2" max="5" name="sliding_window"
                    value=<?php echo $this->session->userdata('sliding_window'); ?>
                />
                Lexical Chaining Window Width
                <p class="current_val">Current Window Width: <?php echo $this->session->userdata('sliding_window'); ?></p>

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
                    type="number" step="0.01" min="0.01" max="1"
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
