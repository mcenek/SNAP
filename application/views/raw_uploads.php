<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <?php include 'partial/resources.php'; ?>

    <script type="text/javascript" src="<?php echo asset_url(); ?>js/active_preprocess.js"></script>

    <title>Raw Uploads</title>

    <script type="text/javascript">
        var pids;
        function selectAll(box)
        {
            var checkBoxes = document.getElementsByTagName('input');
            for(var i=0; i < checkBoxes.length; i++) {
                if(checkBoxes[i].type == 'checkbox') {
                    checkBoxes[i].checked = box.checked;
                }
            }
        }
        $(function(){
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
            })

            $('#FilePostButton').on('click', function ()
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
                        url: "raw_uploads/batch_preprocess",
                        method: "POST",
                        data: {
                            "checkbox": files,
                            "stemming": $('#stemming').val(),
                            "tokenize": $('#tokenize').val(),
                            "sent_split": $('#sent_split').val(),
                            "pos_tag": $('#pos_tag').val(),
                            "lemmatize": $('#lemmatize').val(),
                            "ner_tag": $('#ner_tag').val(),
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
        <H3>Natural Language Processing</H3>
        <p>
            This page is for uploading raw .txt files and performing Natural Language Processing using provided tool kits,
            resulting in new .txt files.
            <br />
            Provided toolkits allow for Stemming, Tokenization, Lemmatization, Sentence Splitting,
            Parts of Speech Recognition and Name Entity Recognition.
            <br />
            File dates must be present in the file name in the form YYYY-MM-DD ie 2004-05-26.
            <br />
            This date is used as time stamp to later represent the the file's nodes as a layer in 3D space.
        </p>

        <br />

        <?php
            echo $error;
            $message = $this->session->flashdata();
            if (!empty($message['flash_message'])) {
                $html = '<p id="warning">';
                $html .= $message['flash_message'];
                $html .= '</p>';
                echo $html;
            }

            echo validation_errors();
            ?>

        <?php echo form_open_multipart('raw_uploads/upload_text'); ?>
            <div id="upload_area">
                <div class="upload_form" id="upload_form">
                    <input  type="file" name="raw_files[]" id="raw_files[]" multiple="multiple" accept="text/plain"/>
                    <input class="btn btn-primary" type="submit" value="Upload" name="submit"/>
                </div>
            </div>
        </form>

        <?php
        echo '<ul>';
        echo '<form id="checkbox_form" name="checkbox_form" method="post" action="raw_uploads/submit_files" >';
        //echo '<form id="checkbox_form" name="checkbox_form" method="post" action="/submit_files">';
        echo "<input type='checkbox' name='select_all' onClick='selectAll(this)' > Select All<br/>";
        echo '<table><tr><td></td><td>File name</td><td>Time Stamp</td><td>Size</td></tr>';
        foreach ($files as $file => $file_name) {
            $file_parts = pathinfo($file_name);
            echo '<tr><td>';
            if ($file_parts['extension'] == "txt") //Check File Extensions, display only produced files
            {
                
                echo form_checkbox(array(
                    'name' => 'checkbox[]',
                    'id' => 'checkbox[]',
                    'value' => $file_name,
                    'checked' => FALSE,
                ));
                $url = site_url() . '/raw_uploads/display_file/' . $file;
                $file_stat = stat($this->file_dir.'/raw/'.$file_name);
                echo '</td><td><a href="' . $url . '">' . $file_name . '</a></td><td>'.date("F d Y H:i:s.",$file_stat['mtime']).'</td><td>'.round(pow(1024, ((log($file_stat['size']) / log(1024)) - floor(log($file_stat['size']) / log(1024)))),2).array("", "k", "M", "G", "T")[floor(log($file_stat['size']) / log(1024))].'</td></tr>';
            }
        }
        echo '</table>';
        echo '<br/>';

// <div>
//     <ul>
//         <li>Stemming</li><li>Tokenization</li><li>Sentence Splitting</li><li>POS Tagging</li><li>Lemmatization</li><li>Name-Entity-Recogition</li>
//     </ul>
// </div>

echo "<input type='checkbox' name='select_all' onClick='selectAll(this)' > Select All<br/><br/>";
echo '<strong style="color:red">FILES 400KB take roughly 1.5 minutes to process Files larger than this will take Exponentially longer!</strong></br>';

echo '<button class="btn btn-primary" id="FilePostButton" name="file_action" value="batch_preprocess"  type="button">Preprocess</button>
            <button class="btn btn-danger" id="CANCELLBUTTON"  name="file_action" value="kill" type="button">Cancel</button>';

echo form_dropdown('stemming',
    array(
        '' => 'Stemming',
        'porter' => 'Porter',
        //'porter2' => 'Porter2',
        'lancaster' => 'Lancaster'),
    '',
    array(
        'name' => 'stemming',
        'id' => 'stemming',
        'class' => 'stem',
        'data-active' => 'true'));
echo form_dropdown('tokenize',
    array(
        '' => 'Tokenize',
        //'corenlp' => 'CoreNLP',
        'nltk' => 'NLTK',
        //'spacy' => 'spaCy'
    ),
    '',
    array(
        'name' => 'tokenize',
        'id' => 'tokenize',
        'class' => 'preprocess',
        'data-active' => 'true'));
echo form_dropdown('sent_split',
    array(
        '' => 'Sentence Split'),
    '',
    array(
        'name' => 'sent_split',
        'id' => 'sent_split',
        'class' => 'preprocess',
        'data-active' => 'false'));
echo form_dropdown('pos_tag',
    array(
        '' => 'POS Tag'),
    '',
    array(
        'name' => 'pos_tag',
        'id' => 'pos_tag',
        'class' => 'preprocess',
        'data-active' => 'false'));
echo form_dropdown('lemmatize',
    array(
        '' => 'Lemmatize'),
    '',
    array(
        'name' => 'lemmatize',
        'id' => 'lemmatize',
        'class' => 'preprocess',
        'data-active' => 'false'));
echo form_dropdown('ner_tag',
    array(
        '' => 'NER Tag'),
    '',
    array(
        'name' => 'ner_tag',
        'id' => 'ner_tag',
        'class' => 'preprocess',
        'data-active' => 'false'));

for ($i = 0; $i < 5; $i++) {
    echo '<br/>';
}

echo '<button class="btn btn-danger" name="file_action" value="delete" type="submit">Delete</button><button class="btn btn-primary" name="file_action" value="download" type="submit">Download</button>';
echo '</form>';
echo '</ul>';
?>
    </div>
</body>
</html>
