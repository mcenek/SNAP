<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <?php include 'partial/resources.php'; ?>

    <title>Semantic Networks</title>

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
    <?php include 'partial/navi.php'; ?>

    <div class="container">
        <H3>Network Analysis</H3>
        <p>
            This page takes all the resulting .dl files of Undirected Graphs, combines them into a single succinct graph using Gephi.
            <br />
            Nodes are grouped into circles and colored based on their modularity class then sized and ordered along the circle by Betweenness Centrality.
            <br />
            Resulting .gexf file can be used in Gephi if you so choose, .pdf file gives user a brief 2D visualized over view of the network,
            and the .txt file of time stamped dates is used for 3D visualization.
        </p>
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
        <p class="current_val">
            Current Set Modularity Resolution: <?php echo $this->session->userdata('mod_resolution'); ?>
        </p>
        <br />
        <form id="checkbox_form" name="checkbox_form" method="post" action="semantic_networks/submit_files">
            <button class="btn btn-primary" name="file_action" value="netgen" type="submit">Semantic Network Generation</button>
            <br />
            <br />
            <button class="btn btn-danger" name="file_action" value="delete" type="submit">Delete</button>
            <button class="btn btn-primary" name="file_action" value="download" type="submit">Download</button>
            <br/>
            <input type='checkbox' name='select_all' onClick='selectAll(this)' /> Select All
            <table><tr><td></td><td>File name</td><td>Time Stamp</td><td>Size</td></tr>
            <?php
                foreach ($files as $file => $file_name) {
                    $file_parts = pathinfo($file_name);
                    echo '<tr><td>';
                    if ($file_parts['extension'] == "dl") //Check File Extensions, display only produced files
                    {    
                        echo form_checkbox(array(
                            'name' => 'checkbox[]',
                            'id' => 'checkbox[]',
                            'value' => $file_name,
                            'checked' => FALSE,
                        ));
                        $url = site_url() . '/semantic_networks/display_file/' . $file_name;
                        $file_stat = stat($this->file_dir.'/semantic_networks/'.$file_name);
                        echo '</td><td><a href="' . $url . '">' . $file_name . '</a> | </td><td>'.date("F d Y H:i:s.",$file_stat['mtime']).' | </td><td>'.round(pow(1024, ((log($file_stat['size']) / log(1024)) - floor(log($file_stat['size']) / log(1024)))),2).array("", "k", "M", "G", "T")[floor(log($file_stat['size']) / log(1024))].'</td></tr>';
                    }
                }
            ?>
            </table><br/>
            <input type='checkbox' name='select_all' onClick='selectAll(this)' /> Select All
            
        </form>
    </div>
</body>
</html>
