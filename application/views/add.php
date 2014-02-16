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
					<select id="command_type" class="span2" name="command_type" id="command_type" Onchange="javascript:this.form.auto.value=0;">
                        <option value="0">请选择...</option>
						<option value="1">截图</option>
                        <!--
						<option value="2">待定</option>
						<option value="3">待定</option>
						<option value="4">待定</option>
						<option value="5">待定</option>
						<option value="6">待定</option>
                        -->
					</select>
					<input class="span3" type="text" id="target" name="target" required autocomplete="off"  value="目标手机IMEI" onfocus="if (value =='目标手机IMEI'){value =''}" onblur="if (value ==''){value='目标手机IMEI'}">
					<button class="btn" type="submit">添加</button>
				</form>
			</div>
        </div>
        </div>
    </body>
</html>