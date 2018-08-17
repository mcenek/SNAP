<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Complete registration</title>
    <style>
        #warning{color: red; font-weight: bold;}
    </style>
</head>
<body>
    <?php echo validation_errors(); ?>
    <?php
$fattr = array('class' => 'form-signin');
echo form_open('complete/completeReg/token/' . $token, $fattr);
?>
    <fieldset>
        <legend>Almost there! Create a password to finish registration!</legend>
        <h5>Hello <span style="color:blue"><?php echo $firstName; ?></span>. Your username is <span style="color:blue"><?php echo $email; ?></span>. </h5>
        <div class='container'>
            <?php echo form_input(array('name' => 'password', 'id' => 'password', 'placeholder' => 'Password', 'class' => 'form-control', 'value' => set_value('password'))); ?>
            <?php echo form_error('password'); ?>
            <?php echo form_input(array('name' => 'passconf', 'id' => 'passconf', 'placeholder' => 'Confirm Password', 'class' => 'form-control', 'value' => set_value('passconf'))); ?>
            <?php echo form_error('passconf'); ?>
        </div>
        <?php echo form_hidden('user_id', $user_id); ?>
        <div class='container'>
            <?php echo form_submit(array('value' => 'Complete')); ?>
        </div>
    </fieldset>
    <?php echo form_close(); ?>
    <?php $arr = $this->session->flashdata();
if (!empty($arr['flash_message'])) {
    $html = '<p id="warning">';
    $html .= $arr['flash_message'];
    $html .= '</p';
    echo $html;
}
?>
</body>
</html>
