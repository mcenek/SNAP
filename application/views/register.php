<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>User Registration</title>

        <link rel="stylesheet" href="<?php echo asset_url(); ?>css/menuStyle.css" type="text/css" />
        <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
        <link rel='stylesheet' type='text/css' href='//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css'>
        <style>
            #warning{ color: red; font-weight: bold; }
        </style>
    </head>
    <body>
        <?php include 'partial/header.php'; ?>
        <div class ="container">
            <h1>New User</h1>
            <?php echo validation_errors(); ?>
            <?php $fattr = array('class' => 'form-signin');
echo form_open('register/registerUser', $fattr);?>
                <fieldset>
                    <H3>Registration</H3>

                    <input type="text" name="firstName" value="" id="firstName" placeholder="First Name" >
                    <?php echo form_error('firstName'); ?>
                    </br>
                    <input type="text" name="lastName" value="" id="lastName" placeholder="Last Name">
                    <?php echo form_error('lastName'); ?>
                    </br>
                    <input type="text" name="email" value="" id="email" placeholder="Email" >
                    <?php echo form_error('email'); ?>
                    </br>
                    <button class="btn btn-primary" type="submit" value="Sign Up">Sign Up</button>

                </fieldset>
                <?php echo form_close(); ?>
                <?php $arr = $this->session->flashdata();
if (!empty($arr['flash_message'])) {
    $html = '<p id="warning">';
    $html .= $arr['flash_message'];
    $html .= '</p>';
    echo $html;
}
?>
        </div>
    </body>
</html>
