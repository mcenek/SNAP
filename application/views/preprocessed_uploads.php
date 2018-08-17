<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
    <script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <link rel='stylesheet' type='text/css' href='//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css'>

    <link rel="stylesheet" href="<?php echo asset_url(); ?>css/menuStyle.css" type="text/css" />

    <title>Preprocessed Uploads</title>

    <style>
        #loader {
            position: absolute;
            left: 50%;
            top: 50%;
            z-index: 1;
            width: 150px;
            height: 150px;
            margin: -75px 0 0 -75px;
            border: 16px solid #f3f3f3;
            border-radius: 50%;
            border-top: 16px solid #3498db;
            width: 120px;
            height: 120px;
            -webkit-animation: spin 2s linear infinite;
            animation: spin 2s linear infinite;
        }

        @-webkit-keyframes spin {
            0% { -webkit-transform: rotate(0deg); }
            100% { -webkit-transform: rotate(360deg); }
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        /* Add animation to "page content" */
        .animate-bottom {
            position: relative;
            -webkit-animation-name: animatebottom;
            -webkit-animation-duration: 1s;
            animation-name: animatebottom;
            animation-duration: 1s
        }

        @-webkit-keyframes animatebottom {
            from { bottom:-100px; opacity:0 }
            to { bottom:0px; opacity:1 }
        }

        @keyframes animatebottom {
            from{ bottom:-100px; opacity:0 }
            to{ bottom:0; opacity:1 }
        }
    </style>
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
                            //console.log("Files processing");
                            var histValues = JSON.parse(test);
                            console.log(histValues);

                            var frequencies = [];
                            frequencies = histValues.Frequency;
                            console.log(frequencies.length)
                            console.log(frequencies.map(normalize(frequencies[0], frequencies[frequencies.length - 1])))
                            var normalizedValues = frequencies.map(normalize(frequencies[0], frequencies[frequencies.length - 1]))
                            if(normalizedValues.length > 0 || normalizedValues != undefined)
                            {
                                var trace = {
                                x: normalizedValues,
                                //histnorm: 'probability',
                                type: 'histogram'//,
                                //cumulative: {enabled: true}
                                };
                                var data = [trace];
                                Plotly.newPlot('HistogramGraph', data);
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
            }


            $('#FinishHist').on('click', function ()
            {
                $.ajax({
                    url: "preprocessed_uploads/FinishHistGeneration",
                    method: "POST",
                    success: function(data)
                    {
                        //console.log("Files processing");
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
                    //console.log(files);

                        $.ajax({
                            url: "preprocessed_uploads/netgen2",
                            method: "POST",
                            data: {
                                "checkbox": files,
                        },
                        success: function(test)
                        {
                            //console.log("Files processing");
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
            and generates Undirected Graph Data Structure .dl files representing the network of words in processed files.
        </p>
        <ul>
            <p class="current_val">Current Use Frequency Setting:
                    <?php $use_freq = $this->session->userdata('use_freq');
if ($use_freq == 1) {
    echo " Use Frequency";
} else if ($use_freq == 0) {
    echo " Dont Use Frequency";
}
?>
            </p>
            <p class="current_val">Current Set Frequency Threshold Lower Bound: <?php echo $this->session->userdata('freq_lower_bound'); ?></p>
            <p class="current_val">Current Set Frequency Threshold Upper Bound: <?php echo $this->session->userdata('freq_upper_bound'); ?></p>
            <br>
        </ul>

        <?php
echo '<div class="pull-right" id="HistogramGraph"></div>';
echo '<div class="hidden" id="loader"></div>';
echo '<ul>';
echo '<form id="checkbox_form" name="checkbox_form" method="post" action="preprocessed_uploads/submit_files">';
echo "<input type='checkbox' name='select_all' onClick='selectAll(this)' > Select All<br/>";
foreach ($files as $file => $file_name) {
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
        echo '<a href="' . $url . '">' . $file_name . '</a><br/>';
    }
}
echo '<button class="btn btn-primary" data-toggle="tooltip" title="Generates Histogram based on selected files!" id="histogramGeneration" name="file_action" value="HistoGram" type="button">Generate Histogram</button>
                    <button class="btn btn-primary" id="NetGenButton" name="file_action" value="netgen" type="button">Network Generation</button>';
//<button class="btn btn-danger" id="CANCELLBUTTON"  name="file_action" value="kill" type="button">Cancel</button>';//if generation can be simplified to  run in the background we can reimplement cancell php current prevents this
echo '<br/>';
echo '<br/>';
echo '<button class="btn btn-danger" name="file_action" value="delete" type="submit">Delete</button>    <button class="btn btn-primary" name="file_action" value="download" type="submit">Download</button>';
echo '</form>';
echo '</ul>';
?>
    </div>
</body>
</html>
