<!DOCTYPE html>
<head>
    <title><?php echo $title; ?></title> 

  <style>
    .form-signin {
        max-width: 300px;
        padding: 29px 29px 29px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
      }
      .form-signin input[type="text"],
      .form-signin input[type="password"] {
        font-size: 16px;
        height: auto;
        padding: 7px 9px;
      }
  </style>
</head>
<body>
	<div class="container">
    <div class="row mt50">
      <div class="span2 offset2 tc">
        <img class="w80" src="<?php echo base_url(); ?>public/images/book.png"/>
        <p class="f18 mt5">Android</p>
        <p class="f12">Administrator</p>
      </div>
      <div class="span4">
        <form class="form-signin" action="#" method="POST">
          <fieldset>
            <?php if ( $blank ) { ?>
            <div class="control-group">
              <input type="text" class="input-block-level" name="username" id="username" placeholder="Enter your name" autocomplete="off" required autofocus="autofocus">
            </div>
            <div class="control-group">
              <input type="password" class="input-block-level" name="password" id="password" placeholder="Password" required>
            </div>
            <button class="btn btn-primary btn-large btn-block" type="submit">登录</button>
            <?php } 
            else echo "<p class='f18'>用户名或密码错误！</p><a class='btn btn-primary btn-large btn-block' href=".site_url("home/login").">返回</a>";
            ?>
          </fieldset>
        </form>
      </div>
    </div>

  </div>
</body>
</html>
