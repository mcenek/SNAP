<?php
//----------------------------------PHP tutorial demo, not to be used in actual NLP pipeline------------------------------------//
class Form extends CI_Controller
{
	function index()
	{
		$this->load->helper(array('form','url'));
		$this->load->library('form_validation');
		$this->form_validation->set_rules('username','Username','callback_username_check');
		$this->form_validation->set_rules('password','Password','required|matches[passconf]');
		$this->form_validation->set_rules('passconf','Password Confirmation','required');
		$this->form_validation->set_rules('email','Email','required|valid_email|is_unique[users.email]');

		if($this->form_validation->run()==FALSE)
		{
			$this->load->view('myform');
		}
		else
		{
			$this->load->view('formsuccess');
		}
	}
	public function username_check($str)
	{
		if($str=='test'){
			$this->form_validation->set_message('username_check','The %s field can not be the word "test"');
			return FALSE;
		}
		else{
			return TRUE;
		}
	}
}
?>