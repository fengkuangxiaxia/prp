<html>
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <script type="text/javascript" src="<?php echo base_url(); ?>public/js/jquery-2.0.2.min.js"></script>
        <script language="JavaScript"> 
            $(document).ready(function(){
                $('#save').click(function(){
                    $('form').submit();
                });
            });
            
        </script>
    </head>
    
    <body>
        <form   class="" method="post" action="<?php echo site_url('home/add_command')?>"  id="form"> 
        <button class="" id="save" type="button">保存</button>
        <a href="<?php echo site_url();?>"><button class="" id="back" type="button">列表</button></a>
        <input  class="" name="command" id="command" type="text" value="" >
    </body>
</html>