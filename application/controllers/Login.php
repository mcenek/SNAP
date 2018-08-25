<?php if (!defined('BASEPATH')) {
    exit('No direct script access allowed');
}

class Login extends CI_Controller
{
    public function __construct()
    {
        parent::__construct();
        $this->load->model('user', '', true);
        $this->load->library('form_validation');
        $this->form_validation->set_error_delimiters('<span class="warning">', '</span>');
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

        // run validation -- if it fails, reload and show the errors
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

            $this->session->set_userdata('logged_in', true);
            // save each column value for user row id in session
            foreach ($user_info as $key => $val) {
                $this->session->set_userdata($key, $val);
            }
            // Set other common pieces of information
            $project = $this->projects->get_project($this->session->userdata('project_id'));
            $this->session->set_userdata('project_name', $project->name);

            redirect(site_url() . '/home');
        }
    }
}
