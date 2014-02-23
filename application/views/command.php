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
                            <th>对象</th>
                            <th>状态</th>
                            <th>结果</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php foreach ($results as $row ) {?>
                        <tr>
                            <td><?php echo $row['command_type']; ?></td>
                            <td>
                                <?php if ($row['target_id'] == '错误'){ ?>
                                    <?php echo $row['target_IMEI']; ?>
                                <?php } else{?>
                                    <a href="<?php echo site_url('device/single/').'/'.$row['target_id'] ?>"><?php echo $row['target_IMEI']?></a>
                                <?php } ?>
                            </td>
                            <td><?php echo $row['status']; ?></td>
                            <td>
                                <?php if ($row['result'] == '暂无结果'){ ?>
                                    <?php echo $row['result']; ?></td>
                                <?php } else{?>
                                    <a class="fancybox" rel="commandimage" href="<?php echo base_url()?>uploads/<?php echo $row['result'];?>" title="">查看图片</a>                       
                                <?php } ?>
                            </td>
                        </tr>
                        <?php } ?>
                    </tbody>
                </table>
            </div>
        </div>
        
    </body>
</html>