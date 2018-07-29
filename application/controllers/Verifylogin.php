<?php if (!defined('BASEPATH')) {
    exit('No direct script access allowed');
}

class Verifylogin extends CI_Controller
{
    public function __construct()
    {
        parent::__construct();
        $this->load->model('user', '', true);
        $this->load->library('form_validation');
    }

    public function index()
    {
        //Checking for these fields being filled
        $this->form_validation->set_rules('username', 'Username', 'trim|required');
        $this->form_validation->set_rules('password', 'Password', 'trim|required|callback_check_database');
        //Apply validation rules
        if ($this->form_validation->run() == false) {
            //Return the same view for failed validation
            $this->load->view('login');
        } else {
            redirect('home', 'refresh');
        }
    }

    public function check_database($password)
    {
        //Field validation succeeded, validate against database
        $username = $this->input->post('username');

        //query the database in login function of user_model class
        $result = $this->user->login($username, $password);
        if ($result) {
            $sess_array = array();
            foreach ($result as $row) {
                $sess_array = array(
                    'email' => $row->email,
                    'username' => $row->username,
                );
                $this->session->set_userdata('logged_in', $sess_array);
            }
            return true;
        } else {
            $this->form_validation->set_message('check_database', 'Invalid username or password');
            return false;
        }
    }
}
