<?php

class Home_model extends CI_Model{
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
        return $query->row_array()['count(*)'];
    }
    
    public function login($data){
        $username = $data['username'];
        $password = md5($data['password']);
        $sql = "SELECT username, password FROM admin_user WHERE username= ? AND password= ? ";
        $query = $this->db->query($sql, array($username, $password));
        if ( $query->num_rows() ) return $query->row()->username;
        else return '';
    }
}
?>