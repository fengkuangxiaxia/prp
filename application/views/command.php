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