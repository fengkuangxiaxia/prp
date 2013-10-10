<?php

class Home_model extends CI_Model{
	function __construct(){
		parent::__construct();
	}
    
    public function get_all_results(){
        $sql = 'select command.command,command.status,result.result from command,result where command.result_id = result.id';
        $query = $this->db->query($sql);
        if($query->num_rows() == 0){
            return array();
        }
        else{
            return $query->result_array();
        }
    }
    
    public function login($data){
        $username = $data['username'];
        $password = md5($data['password']);
        $sql = "SELECT username, password FROM admin WHERE username= ? AND password= ? ";
        $query = $this->db->query($sql, array($username, $password));
        if ( $query->num_rows() ) return $query->row()->username;
        else return '';
    }
}
?>