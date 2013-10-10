<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Home extends CI_Controller {

	function __construct(){
		parent::__construct();
	}
    
	public function index(){
        if ( !$this->session->userdata('admin') ) {
            redirect(site_url('home/login'));
		}
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
	/**
	 * 用户登录，设置session
	 */
	function login(){
		$data['title'] = "登录";
		$data['heading'] = "登录";
		$post['username'] = $this->input->post('username');
		$post['password'] = $this->input->post('password');
		if ( $post['username'] == '' || $post['password'] == '' ) $data['blank'] = 1;
		else {
			$data['blank'] = 0;
            $this->load->model('home_model');
			$data['login'] = $this->home_model->login($post);
			if ( $data['login'] != '' ) {
				$this->session->set_userdata('admin', $data['login']);
				redirect(site_url(''));
			}
		}
		$this->load->view('admin_login', $data);
	}
	/**
	 * 用户登出，销毁session
	 */
	function logout(){
		$this->session->sess_destroy();
		redirect(site_url('login'));
	}
    
    //添加命令的编辑页面    
    public function add(){
        //http://prp.local/index.php/home/add
        $this->load->view('add');
    }
    
    //添加命令的函数
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