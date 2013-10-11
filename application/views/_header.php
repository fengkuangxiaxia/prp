<!DOCTYPE html>
<html>

    <body>
        <div class="navbar navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container">
                    <a class="brand" href="#">Android</a>

                    <ul class="nav">
                        <li <?php echo $command_status ?>>
                            <a href="<?php echo site_url('') ?>">
                                命令总览
                            </a>
                        </li>
                        <li <?php echo $add_status ?>>
                            <a href="<?php echo site_url('home/add') ?>">
                                增加命令
                            </a>
                        </li>

                    </ul>
                    <ul class="nav pull-right">
                        <li class="dropdown">
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#">管理员：<?php echo $admin;?><b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li>
                                    <a href="<?php echo site_url('home/logout')?>">注销</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </body>
</html>