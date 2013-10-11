<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Home extends CI_Controller {

	function __construct(){
		parent::__construct();
	}
    
	public function index($num = 0){
        if ( !$this->session->userdata('admin') ) {
            redirect(site_url('home/login'));
		}
         

        $header['admin'] = $this->session->userdata('admin');
        $data['title'] = '命令总览';
        
        $this->load->model('home_model');
        
        $this->load->library('pagination');
        $config['base_url'] = site_url('home/index');
        $config['total_rows'] = $this->home_model->count_all_commands();
        $config['per_page'] = 2;
        $config['num_links'] = 3;
        $config['full_tag_open'] = '<div class="pagination"><ul>';
        $config['full_tag_close'] = '</ul></div>';
        $config['next_link'] = '下一页 &gt;';
        $config['prev_link'] = '&lt; 上一页';
        $config['num_tag_open'] = '<li>';
        $config['num_tag_close'] = '</li>';
        $config['next_tag_open'] = '<li>';
        $config['next_tag_close'] = '</li>';
        $config['prev_tag_open'] = '<li>';
        $config['prev_tag_close'] = '</li>';
        $config['cur_tag_open'] = '&nbsp;<li class="active"><span>';
        $config['cur_tag_close'] = '</span></li>';
        $this->pagination->initialize($config);

        
        $data['results'] = $this->home_model->get_all_commands($num,$config['per_page']);
        
        /*
        echo '<pre>';
        print_r($data['results']);
        echo '</pre>';
        */
        
        //echo site_url();
        $this->load->view('_header',$header);
        $this->load->view('_include');
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
        $this->load->view('_include');
		$this->load->view('admin_login', $data);
	}
	/**
	 * 用户登出，销毁session
	 */
	function logout(){
		$this->session->sess_destroy();
		redirect(site_url('home/login'));
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
    
    //测试专用函数
    public function test(){
        $this->load->view('_header');
        $this->load->view('include');
        $this->load->view('test');
    }
}
?>