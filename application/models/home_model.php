<?php

class Home_model extends CI_Model{
	function __construct(){
		parent::__construct();
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