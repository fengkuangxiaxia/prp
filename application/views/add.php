<html>
    <head>
        <script language="JavaScript"> 
            $(document).ready(function(){
                $('.btn').click(function(){
                    $('#form').submit();
                });
            });
            
        </script>
    </head>
    
    <body>
        <div class="container">
		<div class="row">
			<div class="span6">
				<form class="form-inline" id="form" action="<?php echo site_url('home/add_command') ?>" method="POST">
					<input type="hidden" value=1 name="auto">
					<select id="command_type" class="span2" name="command_type" id="command_type">
                        <option value="0">请选择...</option>
						<option value="1">截图</option>
					</select>
					<select id="target" class="span2" name="target" id="target">
                        <option value="0">请选择...</option>
                        <?php foreach($IMEI as $row):?>
                            <option value="<?php echo $row['IMEI'] ?>" <?php  if($row['IMEI'] == $default_IMEI) echo 'selected=selected'?>><?php echo $row['IMEI'] ?></option>
                        <?php endforeach?>
					</select>
					<button class="btn" type="submit">添加</button>
				</form>
			</div>
        </div>
        </div>
    </body>
</html>