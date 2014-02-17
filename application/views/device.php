<!DOCTYPE html>
<html>
    <head>
        <title><?php echo $title; ?></title> 
    </head>
    <body>
        <div class="container">
            <div class="row"><?php echo $this->pagination->create_links(); ?></div>
            <div class="row commandlist">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>IMEI</th>
                            <th>手机号</th>
                            <th>手机型号</th>
                            <th>系统</th>
                            <th>更新日期</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php foreach ($results as $row ) {?>
                        <tr>
                            <td><?php echo $row['id']; ?></td>
                            <td><?php echo $row['IMEI']; ?></td>
                            <td><?php echo $row['phoneNumber']; ?></td>
                            <td><?php echo $row['phoneType']; ?></td>
                            <td><?php echo $row['system']; ?></td>
                            <td><?php echo $row['updateAt']; ?></td>
                            <td>
                                <a href="<?php echo site_url('device/single/').'/'.$row['id'] ?>">查看</a>    
                                <a href="<?php echo site_url('device/delete/').'/'.$row['id'] ?>">删除</a> 
                            </td>
                        </tr>
                        <?php } ?>
                    </tbody>
                </table>
            </div>
        </div>
        
    </body>
</html>