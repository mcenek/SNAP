<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <link rel="stylesheet" href="<?php echo asset_url(); ?>css/menuStyle.css" type="text/css" />

    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <link rel='stylesheet' type='text/css' href='//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css'>

    <title>Home Page</title>
</head>
<body>
    <?php include 'navi.php';?>
    <div class="container">

    <h1>Home</h1>
    <h2>Welcome <?php echo $firstName; ?></h2><br/>
    </div>
</body>
</html>
