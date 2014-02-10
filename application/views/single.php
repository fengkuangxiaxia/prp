<!DOCTYPE html>
<html>
    <head>
        <!--script src="https://maps.googleapis.com/maps/api/js?sensor=true" type="text/javascript"></script-->
        <title><?php echo $title; ?></title> 
    </head>
    <body>
        <div class="container">
            
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">通讯录</h3>
            </div>
            <div class="panel-body">
                <div class="contacts">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>姓名</th>
                                <th>手机号</th>
                            </tr>
                        </thead>
                        <tbody>
                            <?php foreach ($contacts as $row ) {?>
                            <tr>
                                <td><?php echo $row['name']; ?></td>
                                <td><?php echo $row['phoneNumber']; ?></td>
                            </tr>
                            <?php } ?>
                        </tbody>
                    </table>
                </div>
            </div>
            
            <div class="panel-heading">
                <h3 class="panel-title">位置信息</h3>
            </div>
            <div class="panel-body">
                <!--
                <div id="map-canvas" style="width: 350px; height: 280px" align="center"></div>
                <script type="text/javascript"> 
                var map;
                function initialize() {
                  var mapOptions = {
                    zoom: 8,
                    center: new google.maps.LatLng(-34.397, 150.644)
                  };
                  map = new google.maps.Map(document.getElementById('map-canvas'),
                      mapOptions);
                }

                google.maps.event.addDomListener(window, 'load', initialize);
                </script>
                -->
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>经度</th>
                            <th>纬度</th>
                            <th>时间</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php foreach ($locations as $row ) {?>
                        <tr>
                            <td><?php echo $row['latitude']; ?></td>
                            <td><?php echo $row['longitude']; ?></td>
                            <td><?php echo $row['updateAt']; ?></td>
                        </tr>
                        <?php } ?>
                    </tbody>
                </table>
            </div>
        </div>        
       
           
        </div>
        
    </body>
</html>