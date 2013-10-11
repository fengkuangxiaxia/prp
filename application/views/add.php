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
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
						<option value="6">6</option>
					</select>
					<input class="span3" type="text" id="target" name="target" required autocomplete="off">
					<button class="btn" type="submit">添加</button>
				</form>
			</div>
        </div>
        </div>
    </body>
</html>