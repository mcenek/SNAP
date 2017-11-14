<?php
class Blog extends CI_Controller {
	public function index()
	{
		echo 'Hello World!';
	}

	public function _remap($method)
	{
		if ($method == 'index')
		{
			$this->$method();
		}
		else
		{
			$this->default_method();
		}
	}
}
