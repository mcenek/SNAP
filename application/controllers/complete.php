<?php if (!defined('BASEPATH')) exit('No direct script access allowed');
//---------------------This class is used to complete registration of a new user.---------------------------------//
class Complete extends CI_Controller{
	public function __construct(){
		parent::__construct();
		$this->load->model('user', '', TRUE);
		$this->load->library('form_validation');
	}

	public function index(){
		$this->load->helper(array('form'));
		if(empty($this->session->userdata['email'])){
			$this->session->set_flashdata('flash_message', 'email not in userdata');
			redirect(site_url() . 'login');
		}
		$data = $this->session->userdata;
		$this->load->view('complete', $data);
	}

	public function completeReg(){
		//uri->segment() returns the vector value contained in the url at position indicated
		//this can differ depending on if showing index.php in url or not
		$token = base64_decode($this->uri->segment(4));
		$cleanToken = $this->security->xss_clean($token);

		//return false or user value array from corresponding token value
		$user_info = $this->user->isTokenValid($cleanToken);

		if(!$user_info)
		{
			$this->session->set_flashdata('flash_message', 'Token is invalid or expired');
			redirect(site_url());
		}
		$data = array
		(
			'firstName' => $user_info->firstName,
			'email' => $user_info->email,
			'user_id' => $user_info->id,
			'token' => base64_encode($token)
		);

		$this->form_validation->set_rules('password', 'Password', 'required|min_length[5]');
		$this->form_validation->set_rules('passconf', 'Password Confirmation', 'required|matches[password]');

		if($this->form_validation->run() == FALSE){
			$this->load->view('complete', $data);
		}
		else{
			$post = $this->input->post(NULL, TRUE);

			$cleanPost = $this->security->xss_clean($post);

			$hashed = password_hash($cleanPost['password'], PASSWORD_BCRYPT);
			$cleanPost['password'] = $hashed;
			unset($cleanPost['passconf']);

			$data = $this->session->userdata;
			//This directory path will be different on other systems i.e. server username's directory
			$dir_path = '/Users/jessgrunblatt/users-uaa/' . $user_info->email;

			$userInfo = $this->user->updateUserInfo($cleanPost, $dir_path);
			if(!$userInfo){
				$this->session->set_flashdata('flash_message', 'There was a problem updating your user info');
				redirect(site_url() . '/login');
			}

			unset($userInfo->password);

			foreach($userInfo as $key=>$val){
				$this->session->set_userdata($key, $val);
			}
			$this->session->set_flashdata('flash_message', 'Redirect to home');
			//redirect(site_url().'/home');
			redirect(site_url().'/home', 'refresh');
			//redirect('semantic_networks', 'refresh');
		}
	}
}
/*
 * End of complete.php
 * Location: ./application/controllers/complete.php
 */
