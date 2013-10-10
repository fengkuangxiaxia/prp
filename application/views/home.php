<html>
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
         <title><?php echo $title; ?></title>       
        <link rel="stylesheet" href="<?php echo base_url(); ?>public/css/base.css">

        <script type="text/javascript" src="<?php echo base_url(); ?>public/js/jquery-2.0.2.min.js"></script>
        <script type="text/javascript" src="<?php echo base_url(); ?>public/js/jquery.mousewheel-3.0.6.pack.js"></script>
        <script type="text/javascript" src="<?php echo base_url(); ?>public/js/ajax3.0.js"></script>

        <link rel="stylesheet" href="<?php echo base_url(); ?>public/bootstrap/css/bootstrap.min.css" >
        <script type="text/javascript" src="<?php echo base_url(); ?>public/bootstrap/js/bootstrap.min.js"></script>

        <link rel="stylesheet" href="<?php echo base_url(); ?>public/fancybox/jquery.fancybox.css" >
        <script type="text/javascript" src="<?php echo base_url(); ?>public/fancybox/jquery.fancybox.js"></script>
        <script type="text/javascript" src="<?php echo base_url(); ?>public/fancybox/jquery.fancybox.pack.js"></script>

        <link rel="stylesheet" href="<?php echo base_url(); ?>public/fancybox/helpers/jquery.fancybox-thumbs.css" >
        <script type="text/javascript" src="<?php echo base_url(); ?>public/fancybox/helpers/jquery.fancybox-thumbs.js"></script>

        <link rel="stylesheet" href="<?php echo base_url() ?>public/css/style.css">
        <script type="text/javascript" src="<?php echo base_url(); ?>public/js/all.js"></script>
    </head>
    <body>
        <a href="<?php echo site_url('home/add');?>"><button class="" id="add" type="button">增加</button></a>
        
        <div class="container">
            <div class="row"><?php echo $this->pagination->create_links(); ?></div>
            <div class="row commandlist">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>命令</th>
                            <th>状态</th>
                            <th>结果</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php foreach ($results as $row ) {?>
                        <tr>
                            <td><?php echo $row['command']; ?></td>
                            <td><?php echo $row['status']; ?></td>
                            <td><?php echo $row['result']; ?></td>
                        </tr>
                        <?php } ?>
                    </tbody>
                </table>
            </div>
        </div>
        
    </body>
</html>