<?php
class Login extends CI_Controller
{

    public function __construct()
    {
        parent::__construct();
        $this->load->model('user', '', true);
        $this->load->library('form_validation');
    }

    public function index()
    {
        $this->load->helper(array('form'));
        $this->load->view('login');
    }

    public function verifyLogin()
    {
        $this->form_validation->set_rules('email', 'Email', 'required|valid_email');
        $this->form_validation->set_rules('password', 'Password', 'required');

        if ($this->form_validation->run() == false) {
            $this->load->view('login');
        } else {
            $post = $this->input->post();
            // xss_clean is intended to prevent a hacker from using Cross Site Scripting attacks to hijack cookies or do other malicious things.
            $clean = $this->security->xss_clean($post);

            $user_info = $this->user->checkLogin($clean);

            if (!$user_info) {
                $this->session->set_flashdata('flash_message', 'The login was unsuccessful');
                redirect(site_url() . '/login');
            }
            $key = 'logged_in';
            $this->session->set_userdata('logged_in', true);
            // save each column value for user row id in session
            foreach ($user_info as $key => $val) {
                $this->session->set_userdata($key, $val);
            }
            redirect(site_url() . '/home');
        }
    }
}
