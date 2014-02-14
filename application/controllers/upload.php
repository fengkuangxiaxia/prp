<?php
class Upload extends CI_Controller {

    function __construct()
    {
        parent::__construct();
        $this->load->helper(array('form', 'url'));
    }

    public function showForm() {
        $this->load->view('upload_form');
    }

    public function uploadFile() {
        // Header for xml outputing.
        header('Content-type: text/xml; charset=utf-8');

        $config['upload_path'] = './uploads/';
        $config['allowed_types'] = 'gif|jpg|png';
        $config['max_size'] = '2048';
        $config['max_width']  = '2048';
        $config['max_height']  = '2048';

        $this->load->library('upload', $config);
        if ( ! $this->upload->do_upload())
        {
            // Get error message.
            $error = $this->upload->display_errors();

            // Prepare template.
            $xmlData = file_get_contents(TEMPLATE_XML_DIR . "upload_result.xml");
            $xmlData = str_replace("{IS_SUCCESSFUL}", 0, $xmlData);
            $xmlData = str_replace("{ERROR}", $error, $xmlData);
            echo $xmlData;
        }
        else
        {
            $data = array('upload_data' => $this->upload->data());
            // Prepare template.
            $xmlData = file_get_contents(TEMPLATE_XML_DIR . "upload_result.xml");
            $xmlData = str_replace("{IS_SUCCESSFUL}", 1, $xmlData);
            $xmlData = str_replace("{ERROR}", "", $xmlData);
            echo $xmlData;
        }
    }
}
?>