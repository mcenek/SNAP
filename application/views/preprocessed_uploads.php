<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <?php include 'partial/resources.php'; ?>

        <script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
        <script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
        <link rel="stylesheet" href="<?php echo asset_url(); ?>css/menuStyle.css" type="text/css" />
        <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
        <!-- <link rel='stylesheet' type='text/css' href='//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css'> -->


    <link rel="stylesheet" href=<?php echo style_url() . 'preprocessed_uploads.css'; ?> type="text/css" />

    <title>Preprocessed Uploads</title>

    <script type="text/javascript">
        var pids;
        function selectAll(box) {
            var checkBoxes = document.getElementsByTagName('input');
            for(var i=0; i < checkBoxes.length; i++) {
                if(checkBoxes[i].type == 'checkbox') {
                    checkBoxes[i].checked = box.checked;
                }
            }
        }
        $(function()
        {
            $('#CANCELLBUTTON').on('click', function ()
            {
                //console.log(pids);
                var returnPIDs = [];
                returnPIDs = pids.Pids;
                var checkBoxes = $('input:checked[name^="checkbox"]');
                var files = [];
                $.each(checkBoxes, function(index, item)
                {
                    files.push($(item).val());
                })

                console.log("my PIds: " + returnPIDs);
                    $.ajax({
                    url: "raw_uploads/kill_process",
                    method: "POST",
                    data: {"PIDS": returnPIDs,
                            "checkbox": files,
                        },
                    success: function(results)
                    {
                        console.log("Files deleted");
                    }
                });
            });


            $('#histogramGeneration').on('click', function ()
            {

                var checkBoxes = $('input:checked[name^="checkbox"]');
                var files = [];
                if(checkBoxes != null)
                {
                    $.each(checkBoxes, function(index, item)
                    {
                        files.push($(item).val());
                    })

                    console.log(files);
                    $("#loader").removeClass('hidden');
                    $.ajax({
                        url: "preprocessed_uploads/HistoGen",
                        method: "POST",
                        data: {
                            "checkbox": files,
                        },
                        success: function(test)
                        {
                            console.log("Files processing");
                            var histValues = JSON.parse(test);
                            console.log(histValues);

                            var frequencies = [];
                            frequencies = histValues.Frequency;
                            console.log(frequencies.length)
                            console.log(frequencies.map(normalize(frequencies[0], frequencies[frequencies.length - 1])))
                            var normalizedValues = frequencies.map(normalize(frequencies[0], frequencies[frequencies.length - 1]))
                            //var normalizedValues = normalizedValues.map(x=>Math.log(x))
                            if(normalizedValues.length > 0 || normalizedValues != undefined)
                            {
                                var trace = {
                                    x: normalizedValues,
                                    //histnorm: 'probability',
                                    type: 'histogram'//,
                                    //cumulative: {enabled: true}
                                };
                                var layout = {
                                    title: 'Plot of word freqiencies in selected files',
                                    xaxis: {
                                       title: 'word frequency (bins)',
                                       showgrid: false,
                                       zeroline: false
                                    },
                                    yaxis: {
                                       title: 'word count (of given frequency)',
                                       showline: false
                                    }
                                };
                                var data = [trace];
                                Plotly.newPlot('HistogramGraph', data, layout);
                            }

                                $("#loader").hide();
                        },
                        complete: function()
                        {
                            $("#loader").hide();
                        }
                        });
                    }
                    $("#loader").addClass('Hidden');
            });

            function normalize(min, max) {
                var delta = max - min;
                return function (val) {
                    return (val - min) / delta;
                };
            };


            $('#FinishHist').on('click', function ()
            {
                $.ajax({
                    url: "preprocessed_uploads/FinishHistGeneration",
                    method: "POST",
                    success: function(data)
                    {
                        console.log("Files processing");
                        var histValues = JSON.parse(data);
                        console.log(histValues);
                    }
                });
            });

            $('#NetGenButton').on('click', function ()
            {
                var checkBoxes = $('input:checked[name^="checkbox"]');
                var files = [];
                if(checkBoxes != null)
                {
                    $.each(checkBoxes, function(index, item)
                    {
                        files.push($(item).val());
                    })
                    console.log(files);

                        $.ajax({
                            url: "preprocessed_uploads/NetGen",
                            method: "POST",
                            data: {
                                "checkbox": files,
                        },
                        success: function(test)
                        {
                            console.log("Files processing"+test);
                            pids = JSON.parse(test);
                            console.log(pids);

                        }
                    });
                }
            });
        })
    </script>
</head>

<body>
    <?php include 'partial/navi.php';?>

    <div class="container">
        <H3>Network Generation</H3>
        <p>
            This page takes the .txt processed by the provided Natural Language Processing toolkits
            and generates Undirected Graph Data Structure .dl files representing the network of words in each processed file.
            The graph is created by lexical chaning.
        </p>
        <p>
            To take out the guessing game of what is the word distribution in the articles, please use the "Generate Histogram" at the bottom
            of the page to help you decide what filter values should you use to keep the right words and delete the meaningless words.
        </p>
        <ul>
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
            <p class="current_val">Current High Word Frequency Threshold Bound: <?php echo $this->session->userdata('freq_upper_bound'); ?>
            </p>
            <p class="current_val">Current Low Word Frequency Threshold Bound: <?php echo $this->session->userdata('freq_lower_bound'); ?>
            </p>
            <p class="current_val">Current Lexical Chaning Window Width: <?php echo $this->session->userdata('sliding_window'); ?>
            </p>
        </ul>

        <div class="pull-right" id="HistogramGraph"></div>
        <div class="hidden" id="loader"></div>

        <button class="btn btn-primary" data-toggle="tooltip" title="Generates Histogram based on selected files!"
            id="histogramGeneration" name="file_action" value="HistoGram" type="button">
            Generate Histogram
        </button>
        <button class="btn btn-primary" data-toggle="tooltip" title="Network Generation" id="NetGenButton" name="file_action" value="netgen2" type="button">Network Generation</button>
        <br/>
        <button class="btn btn-danger" name="file_action" value="delete" type="submit">Delete</button>
        <button class="btn btn-primary" name="file_action" value="download" type="submit">Download</button>

        <form id="checkbox_form" name="checkbox_form" method="post" action="preprocessed_uploads/submit_files">
            <input type='checkbox' name='select_all' onClick='selectAll(this)' > Select All<br/>
            <table><tr><td></td><td>File name</td><td>Time Stamp</td><td>Size</td></tr>
            <?php
              foreach ($files as $file => $file_name) {
                    echo '<tr><td>';
                    $file_parts = pathinfo($file_name);
                    if ($file_parts['extension'] == "txt") //Check File Extensions, display only produced files
                    {
                        $data = array(
                            'name' => 'checkbox[]',
                            'id' => 'checkbox[]',
                            'value' => $file_name,
                            'checked' => FALSE,
                        );
                        echo form_checkbox($data);
                        $url = site_url() . '/preprocessed_uploads/display_file/' . $file_name;
                        $file_stat = stat($this->file_dir.'/preprocessed/'.$file_name);
                        echo '</td><td><a href="' . $url . '">' . $file_name . '</a> | </td><td>'.date("F d Y H:i:s.",$file_stat['mtime']).' | </td><td>'.round(pow(1024, ((log($file_stat['size']) / log(1024)) - floor(log($file_stat['size']) / log(1024)))),2).array("", "k", "M", "G", "T")[floor(log($file_stat['size']) / log(1024))].'</td></tr>';
                    }
                }
            ?>
            </table>
            <br/>
            
        </form>
    </div>
</body>
</html>
