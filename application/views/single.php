<!DOCTYPE html>
<html>
    <head>
        <script src="https://maps.googleapis.com/maps/api/js?sensor=true" type="text/javascript"></script>
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
                
                <div id="map-canvas" style="width: 350px; height: 280px" align="center"></div>
                <script type="text/javascript"> 
                    var markers;
                    var center;
                    $(document).ready(function(){
                        if(document.URL.indexOf("index.php") > -1)
                            base_url = document.URL.slice(0,document.URL.indexOf("index.php"));
                        else
                            base_url = document.URL.slice(0,document.URL.indexOf("entry/"));            
                        $.ajax({
                            type: 'GET',
                            url: base_url + "/index.php/device/ajax_location/" + <?php echo $device_id?>,
                            async: true,
                            success: function(data){
                            /*
                                for(var i in data["locations"]){
                                    alert(data["locations"][i]);
                                }
                            */
                                markers = data["locations"];
                                center = data["center"];
                            },
                            dataType: "json"
                        });//end of $.ajax
                    });
                    
                    var map;
                    function initialize() {
                        var myLatlng = new google.maps.LatLng(center["latitude"],center["longitude"]);
                        var myOptions = {
                          zoom: 5,
                          center: myLatlng,
                          mapTypeId: google.maps.MapTypeId.ROADMAP
                        }
                        var map = new google.maps.Map(document.getElementById("map-canvas"), myOptions);
                        /*
                        var marker = new google.maps.Marker({
                            position: myLatlng, 
                            map: map,
                            title:"Hello World!"
                        });
                        */
                        for(var i in markers){
                            var marker = new google.maps.Marker({
                                position: new google.maps.LatLng(markers[i]["latitude"],markers[i]["longitude"]), 
                                map: map,
                                title:"时间：" + markers[i]["updateAt"]
                            });
                        }
                    }

                    google.maps.event.addDomListener(window, 'load', initialize);
                </script>
                
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>纬度</th>
                            <th>经度</th>
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