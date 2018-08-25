<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <?php include 'partial/resources.php'; ?>

    <title>Complete registration</title>
</head>
<body>
    <?php include 'partial/header.php'; ?>

    <div class="container">
        <?php $fattr = array('class' => 'form-signin'); ?>
        <?php echo form_open('complete/completeReg/token/' . $token, $fattr); ?>

            <h4>Almost there! Create a password to finish registration.</h4>
            <h5>
                Hello <span class="link"><?php echo $firstName; ?></span>.
                Your username is <span class="link"><?php echo $email; ?></span>.
            </h5>

            <div class="row">
                <div class="col col-3">
                    <?php echo form_input(array('name' => 'password', 'id' => 'password', 'placeholder' => 'Password', 'class' => 'form-control', 'value' => set_value('password'))); ?>
                    <?php echo form_error('password'); ?>
                    <?php echo form_input(array('name' => 'passconf', 'id' => 'passconf', 'placeholder' => 'Confirm Password', 'class' => 'form-control', 'value' => set_value('passconf'))); ?>
                    <?php echo form_error('passconf'); ?>

                    <?php echo form_hidden('user_id', $user_id); ?>
                    <input type="submit" value="Complete" class="btn btn-primary" />
                </div>
            </div>

        <?php echo form_close(); ?>

        <?php flash_data($this->session->flashdata()); ?>
    </div>
</body>
</html>
