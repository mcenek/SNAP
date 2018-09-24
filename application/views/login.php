<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <?php include 'partial/resources.php'; ?>

    <link rel="stylesheet" href=<?php echo style_url() . 'login.css'; ?> type="text/css" />

    <title>UAA NLP Login</title>
</head>
<body>
    <?php include 'partial/header.php'; ?>

    <div class="container">
        <?php
            $fattr = array('class' => 'form-signin');
            echo form_open('login/verifylogin', $fattr);
        ?>
            <h3>Login</h3>

            <input type="text" name="email" value="" id="email" placeholder="Email">
            <?php echo form_error('email'); ?>
            <input type="password" name="password" value="" id="password" placeholder="Password">
            <?php echo form_error('password'); ?>
            <button class="btn btn-primary" type="submit" value="Login">Login</button>

            <p>
                Don't have an account? Click to <a href="<?php echo site_url(); ?>/register">Register</a>
                <br />
                Forgot your password? Click <a href="<?php echo site_url(); ?>/forgotpass">Here</a>
            </p>
        <?php echo form_close(); ?>
        <?php flash_data($this->session->flashdata()); ?>
    </div>
</body>
</html>
