<?php if(!defined('BASEPATH')) exit('No direct script access allowed');

class Home extends CI_Controller{
	public function __construct(){
		parent::__construct();
		$this->load->model('user', '', TRUE);
	}

	public function index(){
		if($this->session->userdata('logged_in')){
			//$session_data = $this->session->userdata('logged_in');
			//$data['email'] = $session_data['email'];
			$data = $this->session->userdata;
			$this->load->view('home', $data);
		}
		else{
			//$this->session->unset_userdata('logged_in');
			//session_destroy();
			//If no session redirect back to login
			redirect('login', 'refresh');
		}
	}

	public function logout(){
		$this->session->unset_userdata('logged_in');
		session_destroy();
		redirect('home', 'refresh');
	}
}
