<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <?php include 'partial/resources.php'; ?>

    <link rel="stylesheet" href="<?php echo asset_url(); ?>css/menuStyle.css" type="text/css" />

    <title>UAA NLP Login</title>

    <style>
        fieldset > div.container {
            padding: 10px;
        }
        fieldset > div.container > * {
            display: block;
        }
        fieldset > div.container > h3,
        fieldset > div.container > p {
            margin: 10px 0px;
        }
        button {
            margin: 5px 0px;
        }
        #warning {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <?php include 'partial/header.php'; ?>

    <div class ="container">
        <?php
            echo validation_errors();
            $fattr = array('class' => 'form-signin');
            echo form_open('login/verifylogin', $fattr);
        ?>
            <fieldset>
                <div class='container'>
                    <H3>Login</H3>

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
                </div>
            </fieldset>
        <?php echo form_close(); ?>
        <?php 
            $arr = $this->session->flashdata();
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
