<?php

class Command_model extends CI_Model{
	function __construct(){
		parent::__construct();
	}
    
    public function get_all_commands($num,$offset){
        $sql = 'select command.command,command.status,result.result from command,result where command.result_id = result.id LIMIT '. $num. ','.$offset;
        $query = $this->db->query($sql);
        if($query->num_rows() == 0){
            return array();
        }
        else{
            $temp = $query->result_array();
            $result = array();
            foreach($temp as $row){
                $command = explode(":",$row['command']);
                switch($command[0]){
                    case 1: $command_type = '截图';break;
                    default: $command_type = '未知';break;
                }
                $target_id = $this->get_id_by_IMEI($command[1]);
                $target_IMEI = ($target_id == '错误') ? '错误' : $command[1];
                $status = ($row['status'] == 1) ? '完成' : '未完成';
                $phoneNumber = str_replace("-","",$temp[1]);
                
                
                $result[] = array('command_type' => $command_type,'target_id' => $target_id,'target_IMEI' => $target_IMEI,'status' => $status,'result' => $row['result']);
            }
            
            return $result;
        }
    }
    
    public function count_all_commands(){
        $sql = 'select count(*) from command';
        $query = $this->db->query($sql);
        $temp = $query->row_array();
        return $temp['count(*)'];
    }
    
    public function get_id_by_IMEI($IMEI){
        $sql = 'select id from device where IMEI = ?';
        $query = $this->db->query($sql,array($IMEI));
        if($query->num_rows() == 1){
            $id = $query->row_array();
            return $id['id'];
        }
        else{
            return '错误';
        }
    }
}
?>