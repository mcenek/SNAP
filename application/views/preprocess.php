<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <script type="text/javascript" src="<?php echo asset_url(); ?>js/active_preprocess.js"></script>
    <link rel="stylesheet" href="<?php echo asset_url(); ?>css/menuStyle.css" type="text/css" />

    <style>
        .right{
            position: absolute;
            right: 200px;
            border: 3px solid #73AD21;
        }
        p{
            font-size: 80%;
        }
    </style>
</head>
<body>
    <?php include 'partial/navi.php';?>
    <?php
//Text document to be processed is displayed in this box.
echo form_open('raw_uploads/preprocess', '', array('file_name' => $file_name));
$fattr = array(
    'name' => 'raw_textbox',
    'value' => $raw_text,
    'rows' => '30',
    'cols' => '50');
echo form_textarea($fattr);
?>
    <!-- Processed Text is displayed in this box. -->
    <textarea class="right" name="preprocessed_text" value='' rows='30' cols='50'>
        <?php echo $output; ?>
    </textarea>
    <br/>

    <?php echo validation_errors(); ?>
    <div id="preprocess_dropdowns">

    <?php
/*TODO:  Add labels above menus for easier readability*/
echo form_dropdown('stemming',
    array(
        '' => 'Stemming',
        'porter' => 'Porter',
        'porter2' => 'Porter2',
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
        'corenlp' => 'CoreNLP',
        'nltk' => 'NLTK',
        'spacy' => 'spaCy'),
    '',
    array(
        'name' => 'tokenize',
        'id' => 'tokenize',
        'class' => 'preprocess',
        'data-active' => 'true'));
echo form_dropdown('sent_split',
    array('' => 'Sentence Split'),
    '',
    array(
        'name' => 'sent_split',
        'id' => 'sent_split',
        'class' => 'preprocess',
        'data-active' => 'false'));
echo form_dropdown('pos_tag',
    array('' => 'POS Tag'),
    '',
    array(
        'name' => 'pos_tag',
        'id' => 'pos_tag',
        'class' => 'preprocess',
        'data-active' => 'false'));
echo form_dropdown('lemmatize',
    array('' => 'Lemmatize'),
    '',
    array(
        'name' => 'lemmatize',
        'id' => 'lemmatize',
        'class' => 'preprocess',
        'data-active' => 'false'));
echo form_dropdown('ner_tag',
    array('' => 'NER Tag'),
    '',
    array(
        'name' => 'ner_tag',
        'id' => 'ner_tag',
        'class' => 'preprocess',
        'data-active' => 'false'));
?>
    </div>
    <?php echo form_submit(array('value' => 'Preprocess Text')); ?>
    <?php echo form_close(); ?>
</body>
</html>
