<?php

class Add_command_model extends CI_Model{
	function __construct(){
		parent::__construct();
	}
    
    public function add_command($origin_command){
        $command = $this->standardize($origin_command);
        $sql = "insert into command(command) values(?)";
        return $this->db->query($sql,array($command));
    }
    
    public function standardize($origin_command){
        $command = $origin_command;
        return $command;
    }
}
?>