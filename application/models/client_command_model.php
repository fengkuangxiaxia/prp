<?php

class Client_command_model extends CI_Model{
	function __construct(){
		parent::__construct();
	}
    
    function get_command(){
        $sql = 'select id,command from `command` where status = 0 order by id';
        $query = $this->db->query($sql);
        $commands = $query->result_array();
        if(!empty($commands)){    
            $result = '';
            foreach($commands as $command){
                $result = $result.$command['id'].':'.$command['command'].'%';
            }
            return substr($result,0,strlen($result) - 1);
        }
        else{
            return 'noCommand';
        }
    }
    
    function reply_command($command_id,$result){
        
        $sql = 'select count(*) from command where id = ? and status = 0';
        $query = $this->db->query($sql,array($command_id));
        $count = $query->row_array();
        
        if($count['count(*)'] == 0){
            return -1;
        }
        else{
            $sql = 'insert into result(result) values(?)';
            $this->db->query($sql,array($result));
            
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