<?php

class Device_model extends CI_Model{
	function __construct(){
		parent::__construct();
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
}
?>