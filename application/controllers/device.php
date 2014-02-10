<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Device extends CI_Controller {

	function __construct(){
		parent::__construct();
	}

	public function index($num = 0){
        if ( !$this->session->userdata('admin') ) {
            redirect(site_url('home/login'));
		}
        $header['admin'] = $this->session->userdata('admin');
        $header['device_status'] = 'class="active"';
        $header['command_status'] = '';
        $header['add_status'] = '';
        
        $data['title'] = '设备总览';
        
        $this->load->model('device_model');
        
        $this->load->library('pagination');
        $config['base_url'] = site_url('device/index');
        $config['total_rows'] = $this->device_model->count_all_devices();
        $config['per_page'] = 10;
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

        
        $data['results'] = $this->device_model->get_all_devices($num,$config['per_page']);
        
        $this->load->view('_header',$header);
        $this->load->view('_include');
		$this->load->view('device',$data);
	}
    
    /*设备详细信息显示*/
    function single($device_id){
    
        $data['title'] = '设备详情';
    
        $header['admin'] = $this->session->userdata('admin');
        $header['device_status'] = '';
        $header['command_status'] = '';
        $header['add_status'] = '';
    
        $this->load->model('device_model');
        
        $contacts = $this->device_model->get_device_contacts($device_id);
        $data['contacts'] = $contacts;
        
        $locations = $this->device_model->get_device_locations($device_id);
        $data['locations'] = $locations;
        /*
        echo '<pre>';
        print_r($contacts);
        echo '</pre>';
        */
        
        $this->load->view('_header',$header);
        $this->load->view('single',$data);
        $this->load->view('_include');
        
    }
    
    /*返回更新设备基本信息*/
    function reply_device_info(){
        $IMEI = $this->input->post('IMEI');
        $phoneNumber = $this->input->post('phoneNumber');
        $phoneType = $this->input->post('phoneType');
        $system = $this->input->post('system');

        $this->load->model('device_model');
        $action_result = $this->device_model->reply_device_info($IMEI,$phoneNumber,$phoneType,$system);
        echo ($action_result == 1) ? 'sucess' : 'database error';
        return $action_result;
    }
    
    /*返回更新设备通讯录信息*/
    function reply_device_contacts(){
        $IMEI = $this->input->post('IMEI');
        $contacts = $this->input->post('contacts');

        $this->load->model('device_model');
        $action_result = $this->device_model->reply_device_contacts($IMEI,$contacts);
        echo ($action_result == 1) ? 'sucess' : 'database error';
        return $action_result;
    }
    
    /*返回更新设备位置信息*/
    function reply_device_locations(){
        $IMEI = $this->input->post('IMEI');
        $latitude = $this->input->post('latitude');
        $longitude = $this->input->post('longitude');

        $this->load->model('device_model');
        $action_result = $this->device_model->reply_device_locations($IMEI,$latitude,$longitude);
        echo ($action_result == 1) ? 'sucess' : 'database error';
        return $action_result;
    }   

    /*删除*/
    function delete($device_id){    
        $this->load->model('device_model');
        $action_result = $this->device_model->delete($device_id);
        echo ($action_result == 1) ? 'sucess' : 'database error';
        redirect(site_url('device'));
        return $action_result;
    }
}
?>