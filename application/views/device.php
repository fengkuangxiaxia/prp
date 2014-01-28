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
                        </tr>
                        <?php } ?>
                    </tbody>
                </table>
            </div>
        </div>
        
    </body>
</html>