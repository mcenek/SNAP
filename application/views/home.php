<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <?php include 'partial/resources.php'; ?>

    <link rel="stylesheet" href="<?php echo asset_url(); ?>css/menuStyle.css" type="text/css" />

    <title>Home Page</title>
</head>
<body>
    <?php include 'partial/navi.php';?>

    <div class="container">
        <h1>Home</h1>
        <h2>Welcome <?php echo $firstName; ?></h2>
    </div>
</body>
</html>
