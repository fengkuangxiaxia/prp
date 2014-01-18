<?php

class Add_command_model extends CI_Model{
	function __construct(){
		parent::__construct();
	}
    
    public function add_command($command_type,$command_target){
        $command = $this->standardize($command_type,$command_target);
        $sql = "insert into command(command) values(?)";
        return $this->db->query($sql,array($command));
    }
    
    public function standardize($command_type,$command_target){
        $command = $command_type.":".$command_target;
        return $command;
    }
}
?>