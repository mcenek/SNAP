<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <?php include 'partial/resources.php'; ?>

    <link rel="stylesheet" href=<?php echo style_url() . 'register.css'; ?> type="text/css" />

    <title>User Registration</title>
</head>
<body>
    <?php include 'partial/header.php'; ?>

    <div class="container">
        <?php $fattr = array('class' => 'form-signin'); ?>
        <?php echo form_open('register/registerUser', $fattr); ?>
            <h3>New User Registration</h3>

            <input type="text" name="firstName" value="" id="firstName" placeholder="First Name" >
            <?php echo form_error('firstName'); ?>

            <input type="text" name="lastName" value="" id="lastName" placeholder="Last Name">
            <?php echo form_error('lastName'); ?>

            <input type="text" name="email" value="" id="email" placeholder="Email" >
            <?php echo form_error('email'); ?>

            <button class="btn btn-primary" type="submit" value="Sign Up">Sign Up</button>
            <a class="btn btn-primary" href="<?php echo site_url(); ?>/login">Back to Login</a>
        <?php echo form_close(); ?>

        <?php
            $arr = $this->session->flashdata();
            if (!empty($arr['flash_message'])) {
                $html = '<p class="warning">';
                $html .= $arr['flash_message'];
                $html .= '</p>';
                echo $html;
            }
        ?>
    </div>
</body>
</html>
