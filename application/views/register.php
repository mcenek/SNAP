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
        <h3>New User Registration</h3>

        <?php if (!$this->done): ?>
            <?php $fattr = array('class' => 'form-signin'); ?>
            <?php echo form_open('register/registerUser', $fattr); ?>
                <input type="text" name="firstName" value="" id="firstName" placeholder="First Name" >
                <?php echo form_error('firstName'); ?>

                <input type="text" name="lastName" value="" id="lastName" placeholder="Last Name">
                <?php echo form_error('lastName'); ?>

                <input type="text" name="email" value="" id="email" placeholder="Email" >
                <?php echo form_error('email'); ?>

                <button class="btn btn-primary" type="submit" value="Sign Up">Sign Up</button>
                <a class="btn btn-primary" href="<?php echo site_url(); ?>/login">Back to Login</a>
            <?php echo form_close(); ?>
        <?php else: ?>
            <p>Thanks, a link has been sent to your provided email, please use it to complete registration.</p>
            <!-- NOTE: Once/if SMTP gets configured, we shouldn't need this disclaimer -->
            <p>Please check your spam folder, as the email may have been filtered there.</p>

            <a class="btn btn-primary" href="<?php echo site_url(); ?>/login">Back to Login</a>
        <?php endif; ?>

        <?php flash_data($this->session->flashdata()); ?>
    </div>
</body>
</html>
