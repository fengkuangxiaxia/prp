<?php

class Device_model extends CI_Model{
	function __construct(){
		parent::__construct();
        /*
            info_table中info_type字段的对应关系：
                1：  通讯录 phone_contact表
                2:   位置信息 phone_location表
        */
	}
    
    public function get_all_devices($num,$offset){
        $sql = 'select * from device LIMIT '. $num. ','.$offset;
        $query = $this->db->query($sql);
        if($query->num_rows() == 0){
            return array();
        }
        else{
            return $query->result_array();
        }
    }
    
    public function count_all_devices(){
        $sql = 'select count(*) from device';
        $query = $this->db->query($sql);
        $temp = $query->row_array();
        return $temp['count(*)'];
    }
    
    /*返回更新设备基本信息*/
    function reply_device_info($IMEI,$phoneNumber,$phoneType,$system){
        $IMEI = base64_decode($IMEI);
        $phoneNumber = base64_decode($phoneNumber);
        $phoneType = base64_decode($phoneType);
        $system = base64_decode($system);
        
        $sql = 'select count(*) from device where IMEI = ?';
        $query = $this->db->query($sql,array($IMEI));
        $count = $query->row_array();
        
        if($count['count(*)'] == 0){
            $sql = 'insert into device(IMEI,phoneNumber,phoneType,system) values(?,?,?,?)';
            $this->db->query($sql,array($IMEI,$phoneNumber,$phoneType,$system));
            return 1;
        }
        else{
            $sql = 'update device set phoneNumber = ?,phoneType = ?,system = ? where IMEI = ?';
            $this->db->query($sql,array($IMEI,$phoneNumber,$phoneType,$system));
            return 1;   
        }
    }
    
    /*返回更新设备通讯录信息*/
    function reply_device_contacts($IMEI,$contacts){
        $IMEI = base64_decode($IMEI);
        $contacts = base64_decode($contacts);
        
        $sql = 'select id from device where IMEI = ?';
        $query = $this->db->query($sql,array($IMEI));
                
        if($query->num_rows() == 0){
            return -1;
        }
        else{
            $temp = $query->row_array();
            $device_id = $temp['id'];
            
            $sql = 'delete from info_table where device_id = ? and info_type = ?';
            $query = $this->db->query($sql,array($device_id,1));
            
            $contacts = explode("\n",$contacts);
            foreach($contacts as $contact){
                $temp = explode(":",$contact);
                $name = $temp[0];
                $phoneNumber = str_replace("-","",$temp[1]);
                
                $sql = 'select id from phone_contact where name = ? and phoneNumber = ?';
                $query = $this->db->query($sql,array($name,$phoneNumber));
                                
                if($query->num_rows() == 0){
                    $sql = 'insert into phone_contact(name,phoneNumber) values(?,?)';
                    $query = $this->db->query($sql,array($name,$phoneNumber));
                    
                    $sql = 'select id from phone_contact ORDER BY id desc LIMIT 1';
                    $query = $this->db->query($sql);
                    $temp_result = $query->row_array();
                    
                    $sql = 'insert into info_table(device_id,info_id,info_type) values(?,?,?)';
                    $query = $this->db->query($sql,array($device_id,$temp_result['id'],1));
                }
                else{
                    $temp_result = $query->row_array();
                    
                    $sql = 'insert into info_table(device_id,info_id,info_type) values(?,?,?)';
                    $query = $this->db->query($sql,array($device_id,$temp_result['id'],1));
                }
            }
            return 1;
        }
    }
    
    function get_device_contacts($device_id){
        $sql = 'select info_id from info_table where device_id = ? and info_type = ?';
        $query = $this->db->query($sql,array($device_id,1)); 
        if($query->num_rows() == 0){
            return array();
        }
        else{
            $contacts = array();
            $temp_results = $query->result_array();
            foreach($temp_results as $temp_result){
                $sql = 'select name,phoneNumber from phone_contact where id = ?';
                $query = $this->db->query($sql,array($temp_result['info_id'])); 
                $temp = $query->row_array();
                array_push($contacts,$temp);
            }
            return $contacts;
        }
    }
 
    /*返回更新设备位置信息*/
    function reply_device_locations($IMEI,$latitude,$longitude){
        $IMEI = base64_decode($IMEI);
        $latitude = base64_decode($latitude);
        $longitude = base64_decode($longitude);
        
        $sql = 'select id from device where IMEI = ?';
        $query = $this->db->query($sql,array($IMEI));
                
        if($query->num_rows() == 0){
            return -1;
        }
        else{
            $temp = $query->row_array();
            $device_id = $temp['id'];

            $sql = 'insert into phone_location(latitude,longitude) values(?,?)';
            $query = $this->db->query($sql,array($latitude,$longitude));
            
            $sql = 'select id from phone_location ORDER BY id desc LIMIT 1';
            $query = $this->db->query($sql);
            $temp_result = $query->row_array();
            
            $sql = 'insert into info_table(device_id,info_id,info_type) values(?,?,?)';
            $query = $this->db->query($sql,array($device_id,$temp_result['id'],2));

            return 1;
        }
    }
 
    function get_device_locations($device_id){
        $sql = 'select info_id from info_table where device_id = ? and info_type = ? ORDER BY info_id DESC LIMIT 10';
        $query = $this->db->query($sql,array($device_id,2)); 
        if($query->num_rows() == 0){
            return array();
        }
        else{
            $locations = array();
            $temp_results = $query->result_array();
            foreach($temp_results as $temp_result){
                $sql = 'select latitude,longitude,updateAt from phone_location where id = ?';
                $query = $this->db->query($sql,array($temp_result['info_id'])); 
                $temp = $query->row_array();
                array_push($locations,$temp);
            }
            return $locations;
        }
    }
    
    //设备位置中心点
    function get_device_locations_center($locations){
        $latitude = 0.0;
        $longitude = 0.0;
        foreach($locations as $row){
            $latitude = $latitude + $row['latitude'];
            $longitude = $longitude + $row['longitude'];
        }
        $latitude = $latitude / count($locations);
        $longitude = $longitude / count($locations);
        return array('latitude'=>$latitude,'longitude'=>$longitude);
    }
    
    function delete($device_id){
        $sql = 'select info_id from info_table where device_id = ? and info_type = ?';
        $query = $this->db->query($sql,array($device_id,2)); 
        $temp = $query->result_array();
        foreach($temp as $row){
            $sql = 'delete from phone_location where id = ?';
            $query = $this->db->query($sql,array($row['info_id']));     
        }
        
        $sql = 'delete from info_table where device_id = ?';
        $query = $this->db->query($sql,array($device_id)); 
        
        $sql = 'delete from device where id = ?';
        $query = $this->db->query($sql,array($device_id)); 
        return 1;
    }
}
?>