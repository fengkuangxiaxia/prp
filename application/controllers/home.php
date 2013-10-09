<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Home extends CI_Controller {

	function __construct(){
		parent::__construct();
	}
    
	public function index(){
        $this->load->model('home_model');
        
        $data['results'] = $this->home_model->get_all_results();
        
        /*
        echo '<pre>';
        print_r($data['results']);
        echo '</pre>';
        */
        
        //echo site_url();
        
		$this->load->view('home',$data);
	}
    
    public function add(){
        //http://prp.local/index.php/home/add
        $this->load->view('add');
    }
    
    public function add_command(){
        $this->load->model('add_command_model');
        if(empty($_POST['command'])){
            redirect(site_url('home/add'));
        }
        $this->add_command_model->add_command($_POST['command']);
        redirect(site_url('home/add'));
    }
}
?>