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
            return $query->result_array();
        }
    }
    
    public function count_all_commands(){
        $sql = 'select count(*) from command';
        $query = $this->db->query($sql);
        $temp = $query->row_array();
        return $temp['count(*)'];
    }
}
?>